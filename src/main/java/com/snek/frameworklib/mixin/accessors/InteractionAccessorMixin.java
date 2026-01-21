package com.snek.frameworklib.mixin.accessors;

import net.minecraft.world.entity.Interaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;




/**
 * An accessor mixin for {@link Interaction}.
 * @since v1.1.0
 */
@Mixin(Interaction.class)
public interface InteractionAccessorMixin {
    @Invoker("setWidth")
    void invokeSetWidth(float width);

    @Invoker("setHeight")
    void invokeSetHeight(float height);
}