package com.iafenvoy.annotationlib.util;

import com.iafenvoy.annotationlib.annotation.Link;
import net.minecraft.block.Block;
import net.minecraft.block.SkullBlock;


/**
 * <p>Link target type for @{@link Link} annotation.</p>
 *
 * @author IAFEnvoy
 * @version 1.0
 * @see Link
 * @since 1.0
 */
public enum TargetType {
    /**
     * Link to a {@link Block} and need 1 target
     */
    BLOCK,
    /**
     * Link to a {@link SkullBlock} and need 2 targets
     */
    SKULL
}
