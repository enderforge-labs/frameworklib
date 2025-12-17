package com.snek.frameworklib.mixin;

import net.minecraft.world.entity.Interaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;




@Mixin(Interaction.class)
public interface InteractionAccessorMixin {
    @Invoker("setWidth")
    void invokeSetWidth(float width);

    @Invoker("setHeight")
    void invokeSetHeight(float height);
}