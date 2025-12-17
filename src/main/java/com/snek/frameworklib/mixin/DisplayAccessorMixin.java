package com.snek.frameworklib.mixin;

import net.minecraft.util.Brightness;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Display.BillboardConstraints;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.math.Transformation;




@Mixin(Display.class)
public interface DisplayAccessorMixin {


    @Invoker("getBillboardConstraints")
    BillboardConstraints invokeGetBillboardMode();

    @Invoker("getViewRange")
    float invokeGetViewRange();

    @Invoker("getBrightnessOverride")
    Brightness invokeGetBrightness();

    @Invoker("getWidth")
    float invokeGetFrustumCullingBoundingBoxWidth();

    @Invoker("getHeight")
    float invokeGetFrustumCullingBoundingBoxHeight();




    @Invoker("setTransformation")
    void invokeSetTransformation(Transformation transformation);

    @Invoker("setInterpolationDuration")
    void invokeSetInterpolationDuration(int duration);

    @Invoker("setInterpolationDelay")
    void invokeSetStartInterpolation(int delay);

    @Invoker("setBillboardConstraints")
    void invokeSetBillboardMode(BillboardConstraints mode);

    @Invoker("setViewRange")
    void invokeSetViewRange(float range);

    @Invoker("setBrightnessOverride")
    void invokeSetBrightness(Brightness brightness);

    @Invoker("setWidth")
    void invokeSetFrustumCullingBoundingBoxWidth(float width);

    @Invoker("setHeight")
    void invokeSetFrustumCullingBoundingBoxHeight(float height);
}