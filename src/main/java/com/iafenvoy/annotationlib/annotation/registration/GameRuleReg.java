package com.iafenvoy.annotationlib.annotation.registration;

import com.iafenvoy.annotationlib.annotation.UnusedYet;
import net.minecraft.world.GameRules;

import java.lang.annotation.*;

/**
 * <p>Register this game rule with given ID.</p>
 * <p>Field name will be used if no ID provided.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @since 1.0
 */
@UnusedYet
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface GameRuleReg {
    /**
     * The name of game rule
     *
     * @return {@link String }
     */
    String name() default "";

    /**
     * The type of game rule
     *
     * @return {@link GameRules.Category }
     */
    GameRules.Category category();

    /**
     * Whether to use the mod id as prefix
     * @return boolean
     */
    boolean modIdPrefix() default true;
}
