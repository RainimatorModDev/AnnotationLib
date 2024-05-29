package com.iafenvoy.annotationlib.registry;

import com.iafenvoy.annotationlib.AnnotationLib;
import com.iafenvoy.annotationlib.EntryPointLoader;
import com.iafenvoy.annotationlib.annotation.AnnotationProcessor;
import com.iafenvoy.annotationlib.annotation.ModId;
import com.iafenvoy.annotationlib.annotation.TargetId;
import com.iafenvoy.annotationlib.annotation.registration.*;
import com.iafenvoy.annotationlib.api.IAnnotatedRegistryEntry;
import com.iafenvoy.annotationlib.util.IAnnotationProcessor;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.potion.Potion;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
            String name = getName(field, autoRegister);
            if (name != null) {
                boolean registered = false;
                try {//Item, Block, EntityType, SoundEvent, StatusEffect, ParticleType, Enchantment, Potion, KeyBinding, BlockEntityType
                    Object obj = field.get(null);
                    if (Item.class.isAssignableFrom(field.getType())) {
                        linkableChanged = true;
                        register(Registry.ITEM, modId, name, (Item) obj);
                        registered = true;
                    } else if (Block.class.isAssignableFrom(field.getType())) {
                        linkableChanged = true;
                        register(Registry.BLOCK, modId, name, (Block) obj);
                        registered = true;
                    } else if (EntityType.class.isAssignableFrom(field.getType())) {
                        register(Registry.ENTITY_TYPE, modId, name, (EntityType<?>) obj);
                        RegistrationHelper.processEntity(clazz, field, obj);
                        registered = true;
                    } else if (SoundEvent.class.isAssignableFrom(field.getType())) {
                        register(Registry.SOUND_EVENT, modId, name, (SoundEvent) obj);
                        registered = true;
                    } else if (StatusEffect.class.isAssignableFrom(field.getType())) {
                        register(Registry.STATUS_EFFECT, modId, name, (StatusEffect) obj);
                        registered = true;
                    } else if (Enchantment.class.isAssignableFrom(field.getType())) {
                        register(Registry.ENCHANTMENT, modId, name, (Enchantment) obj);
                        registered = true;
                    } else if (Potion.class.isAssignableFrom(field.getType())) {
                        register(Registry.POTION, modId, name, (Potion) obj);
                        registered = true;
                    } else if (BlockEntityType.class.isAssignableFrom(field.getType())) {
                        register(Registry.BLOCK_ENTITY_TYPE, modId, name, (BlockEntityType<?>) obj);
                        registered = true;
                    } else if (EntryPointLoader.isClientSide) {
                        if (KeyBinding.class.isAssignableFrom(field.getType())) {
                            KeyBindingHelper.registerKeyBinding((KeyBinding) obj);
                            RegistrationHelper.processKeybindings(field, obj);
                            registered = true;
                        }
                    } else
                        AnnotationLib.LOGGER.error("Cannot register this item since this type is not implemented yet: " + field.getName());
                } catch (IllegalAccessException e) {
                    AnnotationLib.LOGGER.error("Fail to get object: " + field.getName(), e);
                }
                if (!registered)
                    AnnotationLib.LOGGER.warn(String.format("Field %s in class %s is not marked as requiring registration, game may crash with this.", field.getName(), clazz.getName()));
            } else {
                try {
                    Object obj = field.get(null);
                    ItemReg itemReg = field.getAnnotation(ItemReg.class);
                    if (itemReg != null) {
                        name = itemReg.value().isBlank() ? field.getName() : itemReg.value();
                        register(Registry.ITEM, modId, name.toLowerCase(), (Item) obj);
                        if (!itemReg.group().value().isBlank()) {
                            TargetId targetId = itemReg.group();
                            RegistrationGroup.add(new Identifier(targetId.namespace().isBlank() ? modId : targetId.namespace(), targetId.value()), field);
                        }
                    }
                    ParticleReg particleReg = field.getAnnotation(ParticleReg.class);
                    if (particleReg != null) {
                        if (DefaultParticleType.class.isAssignableFrom(field.getType())) {
                            if (!particleReg.name().isBlank())
                                name = particleReg.name();
                            if (name == null) name = field.getName().toLowerCase();
                            register(Registry.PARTICLE_TYPE, modId, name, (DefaultParticleType) obj);
                            if (EntryPointLoader.isClientSide) {
                                Class<?> particleClass = particleReg.value();
                                RegistrationHelper.processParticle(particleClass, obj);
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    AnnotationLib.LOGGER.error("Fail to get object: " + field.getName(), e);
                }
            }
        }
        //After we complete all register, check non-linked objects
        if (linkableChanged)
            RegistrationLink.findIfCanLink();
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
