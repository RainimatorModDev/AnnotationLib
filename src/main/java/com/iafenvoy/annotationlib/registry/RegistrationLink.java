package com.iafenvoy.annotationlib.registry;

import com.iafenvoy.annotationlib.AnnotationLib;
import com.iafenvoy.annotationlib.annotation.TargetId;
import com.iafenvoy.annotationlib.util.TargetType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SkullItem;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RegistrationLink {
    private static final CopyOnWriteArrayList<Linkable> NEED_TO_LINK = new CopyOnWriteArrayList<>();

    private static void tryLink(Linkable linkable) {
        switch (linkable.type) {
            case BLOCK -> {
                Block wantToLink = Registries.BLOCK.get(linkable.target.get(0));
                if (wantToLink == Blocks.AIR) //cannot find or haven't registered yet.
                    NEED_TO_LINK.add(linkable);
                else
                    linkable.link(new BlockItem(wantToLink, new FabricItemSettings()));
            }
            case SKULL -> {
                Block wantToLink1 = Registries.BLOCK.get(linkable.target.get(0));
                Block wantToLink2 = Registries.BLOCK.get(linkable.target.get(1));
                if (wantToLink1 == Blocks.AIR || wantToLink2 == Blocks.AIR) //cannot find or haven't registered yet.
                    NEED_TO_LINK.add(linkable);
                else
                    linkable.link(new SkullItem(wantToLink1, wantToLink2, new FabricItemSettings()));
            }
            default -> throw new UnsupportedOperationException(linkable.type + " is not implement yet.");
        }
    }

    public static void link(String modId, TargetType type, List<TargetId> target, Field field) {
        List<Identifier> ids = target.stream().map(x -> new Identifier(x.namespace().isBlank() ? modId : x.namespace(), x.value())).toList();
        tryLink(new Linkable(type, ids, field));
    }

    public static void findIfCanLink() {
        synchronized (NEED_TO_LINK) {
            NEED_TO_LINK.forEach(RegistrationLink::tryLink);
            NEED_TO_LINK.removeAll(NEED_TO_LINK.stream().filter(Linkable::isLinked).toList());
        }
    }

    public static void postEndRegister() {
        synchronized (NEED_TO_LINK) {
            if (NEED_TO_LINK.size() > 0)
                AnnotationLib.LOGGER.warn("The following objects didn't link successfully and will keep the default value: " + String.join(", ", NEED_TO_LINK.stream().map(x -> {
                    Field field = x.field;
                    return field.getDeclaringClass().getName() + "." + field.getName();
                }).toList()));
        }
    }

    private static class Linkable {
        private final TargetType type;
        private final List<Identifier> target;
        private final Field field;
        private boolean linked = false;

        public Linkable(TargetType type, List<Identifier> target, Field field) {
            this.type = type;
            this.target = target;
            this.field = field;
        }

        public void link(Object obj) {
            int modifier = this.field.getModifiers();
            /*
                Field with static final will be transformed into a constant.
                In Java 11-, we can change the modifier of a field.
                In Java 12+, the modifier field is filtered by reflect system due to safety issues.
                So we cannot remove final modifier of a constant safely. :(
             */
            if (Modifier.isStatic(modifier) && Modifier.isFinal(modifier))
                throw new UnsupportedOperationException("Field with static final modifier cannot use @link since this is a unsafe operation in Java 12+. :(");
            if (Modifier.isPrivate(modifier))
                this.field.setAccessible(true);
            try {
                if (Item.class.isAssignableFrom(this.field.getType()))
                    RegistrationManager.register(Registries.ITEM, target.get(0), (Item) obj);
                this.field.set(null, obj);
            } catch (IllegalAccessException e) {
                AnnotationLib.LOGGER.error("Fail to set object: " + field.getName(), e);
            }
            this.linked = true;
        }

        public boolean isLinked() {
            return linked;
        }
    }
}
