package com.snek.frameworklib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.brigadier.ParseResults;
import com.snek.frameworklib.FrameworkLib;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;







/**
 * By default, when an in-game command generates an exception, Minecraft only prints the exception error in chat.
 * This mixin intercepts exceptions and prints their stack trace in the console to make debugging it easier.
 */
@Mixin(Commands.class)
public class CommandStackTraceMixin {


    @Inject(
        method = "performCommand",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;",
            ordinal = 1
            //! ^ Set ordinal to 1 for the second call to Component.literal, which contains the exception catch block. 0 is syntax.
        ),
        locals = LocalCapture.CAPTURE_FAILSOFT,
        cancellable = true
    )


    private void onCommandExecutionException(
        ParseResults<CommandSourceStack> parseResults,
        String command,
        CallbackInfoReturnable<Integer> cir,
        CommandSourceStack source,
        Exception exception
    ) {
        FrameworkLib.LOGGER.error(
            "Command \"{}\" from \"{}\" threw an exception:",
            command,
            source.getTextName(),
            exception
        );
    }
}