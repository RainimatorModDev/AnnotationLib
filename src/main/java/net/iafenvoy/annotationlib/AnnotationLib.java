package net.iafenvoy.annotationlib;

import com.mojang.logging.LogUtils;
import net.iafenvoy.annotationlib.annotation.RegisterItem;
import net.iafenvoy.annotationlib.test.TestItemClass;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

@Mod("annotationlib")
public class AnnotationLib {
    private static final Logger LOGGER = LogUtils.getLogger();

    public AnnotationLib() {
        load("test_mod_id", new TestItemClass());
    }

    public static void load(String modId, Object obj){
        int counter = 0;
        DeferredRegister<Item> register = DeferredRegister.create(ForgeRegistries.ITEMS, modId);
        for (Field f : obj.getClass().getFields()) {
            Annotation[] a = f.getAnnotationsByType(RegisterItem.class);
            if (a.length == 0) continue;
            if (Modifier.isStatic(f.getModifiers()) && f.getType().isAssignableFrom(Supplier.class)) {
                RegisterItem annotation = (RegisterItem) a[0];
                try{
                    register.register(annotation.id(), (Supplier<Item>) f.get(obj));
                }catch (IllegalAccessException e){
                    e.printStackTrace();
                }
                counter++;
            } else
                LOGGER.error("Find a non-item field with @RegisterItem");
        }
        register.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Success register " + counter + " item(s) for mod id " + modId);
    }
}
