package com.snek.frameworklib.graphics.core;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.mixin.InteractionAccessorMixin;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.scheduler.Scheduler;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;








/**
 * A special interaction entity that is used to block unwanted players interactions.
 * <p>
 * This stops client-side clicks on blocks and entities behind the targeted UI
 * <p>
 * and client-side item use events, preventing annoying UI flashes and visual artifacts.
 */
public class InteractionBlocker {
    private @NotNull InteractionAccessorMixin getAccessibleDisplay() { return (InteractionAccessorMixin)heldEntity; }


    // Despawn delay, avoids accidental clicks after the UI is removed
    public static final int DESPAWN_DELAY = 10;


    // Entity names and command source identifier
    public static final String ENTITY_CUSTOM_NAME               = FrameworkLib.LIB_ID + ".ui.interactionblocker";
    public static final String ENTITY_CUSTOM_NAME_UNINITIALIZED = ENTITY_CUSTOM_NAME + ".uninitialized";
    public static final String UPDATE_COMMAND_SOURCE_NAME       = ENTITY_CUSTOM_NAME + ".patch";
    public static final String UPDATE_COMMAND =
        "execute as @e[type=minecraft:interaction,name=" + ENTITY_CUSTOM_NAME_UNINITIALIZED + "] " +
        "run data modify entity @s Air set value 1000"
    ;


    // In-world data
    private final @NotNull Interaction heldEntity;
    private final @NotNull Level level;




    /**
     * Creates a new InteractionBlocker.
     * @param level The level to create this interaction in.
     */
    public InteractionBlocker(final @NotNull Level level, final float w, final float h) {
        this.level = level;
        heldEntity = new Interaction(EntityType.INTERACTION, level);
        getAccessibleDisplay().invokeSetWidth(w);
        getAccessibleDisplay().invokeSetHeight(h);
    }




    /**
     * Checks for stray interaction entities and purges them.
     * <p>
     * Must be called on entity load event.
     * @param entity The entity.
     */
    public static void onEntityLoad(@NotNull Entity entity) {
        if(entity instanceof Interaction) {
            final Level level = entity.level();
            if(
                level != null &&
                entity.hasCustomName() &&
                entity.getCustomName().getString().equals(ENTITY_CUSTOM_NAME)
            ) {
                entity.remove(RemovalReason.KILLED);
            }
        }
    }




    /**
     * Spawns the interaction entity into the level.
     * @param pos The coordinates at which to spawn the entity.
     */
    public void spawn(final @NotNull Vector3d pos) {

        // Spawn the entity, move it to the specified coords and set a temporary name to allow the command to recognize it
        level.addFreshEntity(heldEntity);
        heldEntity.setPos(pos.x, pos.y, pos.z);
        heldEntity.setCustomNameVisible(false);
        heldEntity.setCustomName(new Txt(ENTITY_CUSTOM_NAME_UNINITIALIZED).get());


        //!  ╭────────────────────────────────────────────────────────────────────────────────────────────────────────────────╮
        //!  │                                                                                                              //!
        //!  │                        Manually update the entity using a vanilla data modify command.                       //!
        //!  │                                Use a custom source to silence command feedback.                              //!
        //!  │                                                                                                              //!
        //!  │            This workaround is used to fix a 1.20.1 issue that makes interactions spawned by the mod          //!
        //!  │                        unable to detect player clicks until their nbt data is modified.                      //!
        //!  │                                                                                                              //!
        //!  │                                                                                                              //!
        /*!  │  // Create the custom command source and use DUMMY as output to silence it                                   //!
        /*!  │  */final MinecraftServer server = FrameworkLib.getServer();                                                  //!
        /*!  │  */final CommandSourceStack source = new CommandSourceStack(                                                 //!
        /*!  │  */    CommandSource.NULL, Vec3.ZERO, Vec2.ZERO, (ServerLevel)heldEntity.level(),                            //!
        /*!  │  */    4, UPDATE_COMMAND_SOURCE_NAME, Component.literal(UPDATE_COMMAND_SOURCE_NAME), server, (Entity)null    //!
        /*!  │  */);                                                                                                        //!
        //!  │                                                                                                              //!
        //!  │                                                                                                              //!
        /*!  │  // Execute the command using the custom command source                                                      //!
        /*!  │  */try {                                                                                                     //!
        /*!  │  */    final int result = server.getCommands().getDispatcher().execute(UPDATE_COMMAND, source);              //!
        /*!  │  */    if(result == 0) {                                                                                     //!
        /*!  │  */        FrameworkLib.LOGGER.warn("Interaction entity update command found no matching entities");         //!
        /*!  │  */    }                                                                                                     //!
        /*!  │  */} catch(final CommandSyntaxException e) {                                                                 //!
        /*!  │  */    FrameworkLib.LOGGER.error("Incorrect syntax for interaction entity update command", e);               //!
        /*!  │  */}                                                                                                         //!
        //!  │                                                                                                              //!
        //!  ╰────────────────────────────────────────────────────────────────────────────────────────────────────────────────╯


        // Replace the temporary name with the actual custom name. This name is the one that will be used to purge stray interactions
        heldEntity.setCustomName(new Txt(ENTITY_CUSTOM_NAME).get());
    }




    /**
     * Teleports the interaction entity to the specified position.
     * @param pos The position.
     */
    public void teleport(final @NotNull Vector3d pos) {
        heldEntity.teleportTo(pos.x, pos.y, pos.z);
    }




    /**
     * Removes the interaction entity from the level
     */
    public void despawn() {
        Scheduler.schedule(DESPAWN_DELAY, () -> {
            heldEntity.remove(RemovalReason.KILLED);
        });
    }
}
