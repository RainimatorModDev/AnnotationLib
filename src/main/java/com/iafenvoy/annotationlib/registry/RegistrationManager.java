package com.iafenvoy.annotationlib.registry;

import com.iafenvoy.annotationlib.AnnotationLib;
import com.iafenvoy.annotationlib.EntryPointLoader;
import com.iafenvoy.annotationlib.annotation.AnnotationProcessor;
import com.iafenvoy.annotationlib.annotation.CallbackHandler;
import com.iafenvoy.annotationlib.annotation.ModId;
import com.iafenvoy.annotationlib.annotation.TargetId;
import com.iafenvoy.annotationlib.annotation.registration.*;
import com.iafenvoy.annotationlib.api.IAnnotatedRegistryEntry;
import com.iafenvoy.annotationlib.util.IAnnotationProcessor;
import com.iafenvoy.annotationlib.util.UncheckedMethods;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.lang.reflect.*;
import java.util.List;

@AnnotationProcessor(IAnnotatedRegistryEntry.class)
public class RegistrationManager implements IAnnotationProcessor {
    @Override
    public void process(Class<?> clazz) {
        ModId modIdAnnotation = clazz.getAnnotation(ModId.class);
        if (modIdAnnotation == null) return;
        //Has @ModId
        boolean linkableChanged = false;
        String modId = modIdAnnotation.value();
        Field[] fields = clazz.getDeclaredFields();
        boolean autoRegister = clazz.getAnnotation(RegisterAll.class) != null;
        for (Field field : fields) {
            int modifier = field.getModifiers();
            if (!Modifier.isStatic(modifier))//Ignore non-static field
                continue;
            //@Group
            tryPutGroup(modId, field);
            if (ItemStack.class.isAssignableFrom(field.getType())) continue;
            //@Link
            if (tryPutLink(modId, field)) continue;
            boolean registered = false;
            try {
                Object obj = field.get(null);
                String name = getName(field, autoRegister);
                if (Item.class.isAssignableFrom(field.getType())) {
                    ItemReg itemReg = field.getAnnotation(ItemReg.class);
                    if (itemReg != null) {
                        name = itemReg.value().isBlank() ? field.getName() : itemReg.value();
                        register(Registries.ITEM, modId, name.toLowerCase(), (Item) obj);
                        if (!itemReg.group().value().isBlank()) {
                            TargetId targetId = itemReg.group();
                            RegistrationGroup.add(new Identifier(targetId.namespace().isBlank() ? modId : targetId.namespace(), targetId.value()), field);
                        }
                        registered = true;
                    } else if (name != null) {
                        register(Registries.ITEM, modId, name, (Item) obj);
                        registered = true;
                    }
                } else if (Block.class.isAssignableFrom(field.getType()) && name != null) {
                    linkableChanged = true;
                    register(Registries.BLOCK, modId, name, (Block) obj);
                    registered = true;
                } else if (EntityType.class.isAssignableFrom(field.getType()) && name != null) {
                    register(Registries.ENTITY_TYPE, modId, name, (EntityType<?>) obj);
                    RegistrationHelper.processEntity(clazz, field, obj);
                    registered = true;
                } else if (SoundEvent.class.isAssignableFrom(field.getType()) && name != null) {
                    register(Registries.SOUND_EVENT, modId, name, (SoundEvent) obj);
                    registered = true;
                } else if (StatusEffect.class.isAssignableFrom(field.getType()) && name != null) {
                    register(Registries.STATUS_EFFECT, modId, name, (StatusEffect) obj);
                    registered = true;
                } else if (ItemGroup.class.isAssignableFrom(field.getType()) && name != null) {
                    register(Registries.ITEM_GROUP, modId, name, (ItemGroup) obj);
                    registered = true;
                } else if (ParticleType.class.isAssignableFrom(field.getType()) && name != null) {
                    ParticleReg particleReg = field.getAnnotation(ParticleReg.class);
                    if (!particleReg.name().isBlank())
                        name = particleReg.name();
                    if (name != null) {
                        try {
                            Constructor<? extends ParticleFactory<ParticleEffect>> constructor = particleReg.value().getConstructor(SpriteProvider.class);
                            ParticleType<? extends ParticleEffect> particleType = (ParticleType<?>) obj;
                            register(Registries.PARTICLE_TYPE, modId, name, particleType);
                            if (EntryPointLoader.isClientSide)
                                ParticleFactoryRegistry.getInstance().register(UncheckedMethods.getParticleType(particleType), provider -> {
                                    try {
                                        return constructor.newInstance(provider);
                                    } catch (InvocationTargetException | InstantiationException |
                                             IllegalAccessException e) {
                                        AnnotationLib.LOGGER.error("Cannot create particle provider with constructor: " + field.getName(), e);
                                        throw new RuntimeException(e);
                                    }
                                });
                            registered = true;
                        } catch (NoSuchMethodException e) {
                            AnnotationLib.LOGGER.error("Cannot find constructor: " + field.getName(), e);
                        }
                    }
                } else if (Enchantment.class.isAssignableFrom(field.getType()) && name != null) {
                    register(Registries.ENCHANTMENT, modId, name, (Enchantment) obj);
                    registered = true;
                } else if (name != null)
                    AnnotationLib.LOGGER.error("Cannot register this item since this type is not implemented yet: " + field.getName());
            } catch (IllegalAccessException e) {
                AnnotationLib.LOGGER.error("Fail to get object: " + field.getName(), e);
            }
            if (!registered)
                AnnotationLib.LOGGER.warn(String.format("Field %s in class %s is not marked as requiring registration, game may crash with this.", field.getName(), clazz.getName()));
        }
        //After we complete all register, check non-linked objects
        if (linkableChanged)
            RegistrationLink.findIfCanLink();
        //find @CallbackHandler
        for (Method method : clazz.getMethods()) {
            CallbackHandler callback = method.getAnnotation(CallbackHandler.class);
            if (Modifier.isStatic(method.getModifiers()) && callback != null)
                if (method.getParameterCount() == 0 && method.getReturnType() == Void.TYPE) {
                    if (Modifier.isPrivate(method.getModifiers()))
                        method.setAccessible(true);
                    try {
                        method.invoke(null);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        AnnotationLib.LOGGER.error("Fail to invoke method: " + method.getName(), e);
                    }
                } else
                    AnnotationLib.LOGGER.warn(String.format("Method %s in class %s has a wrong signature, see @CallbackHandler for more info.", method.getName(), clazz.getName()));
        }
    }

    //return null if there is no need to register
    private String getName(Field field, boolean autoRegister) {
        ObjectReg objectReg = field.getAnnotation(ObjectReg.class);
        if (!autoRegister && objectReg == null) return null;
        String name = field.getName();
        if (objectReg != null && !objectReg.value().isBlank())
            name = objectReg.value();
        return name.toLowerCase();
    }

    private void tryPutGroup(String modId, Field field) {
        Group group = field.getAnnotation(Group.class);
        if (group == null) return;
        TargetId targetId = group.value();
        RegistrationGroup.add(new Identifier(targetId.namespace().isBlank() ? modId : targetId.namespace(), targetId.value()), field);
    }

    private boolean tryPutLink(String modId, Field field) {
        Link link = field.getAnnotation(Link.class);
        if (link == null) return false;
        List<TargetId> targets = link.target().value().isBlank() ? List.of(link.targets()) : List.of(link.target());
        RegistrationLink.link(modId, link.type(), targets, field);
        return true;
    }

    public static <T> void register(Registry<T> registry, String modId, String name, T obj) {
        register(registry, new Identifier(modId, name), obj);
    }

    public static <T> void register(Registry<T> registry, Identifier id, T obj) {
        Registry.register(registry, id, obj);
    }
}
