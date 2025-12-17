package com.snek.frameworklib.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Display.TextDisplay;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;




@Mixin(TextDisplay.class)
public interface TextDisplayAccessorMixin {
    @Invoker("getText")
    Component invokeGetText();

    @Invoker("getLineWidth")
    int invokeGetLineWidth();

    @Invoker("getTextOpacity")
    byte invokeGetTextOpacity();

    @Invoker("getBackgroundColor")
    int invokeGetBackground();

    @Invoker("setText")
    void invokeSetText(Component text);

    @Invoker("setLineWidth")
    void invokeSetLineWidth(int width);

    @Invoker("setTextOpacity")
    void invokeSetTextOpacity(byte opacity);

    @Invoker("setBackgroundColor")
    void invokeSetBackground(int color);
}