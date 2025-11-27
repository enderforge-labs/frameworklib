package com.snek.frameworklib.input;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.graphics.core.Context;
import com.snek.frameworklib.graphics.core.InteractionBlocker;
import com.snek.frameworklib.utils.scheduler.RateLimiter;

import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.level.Level;








/**
 * A utility class that handles clicks from players.
 * <p>
 * This is responsible for sending click events to contexts.
 */
public abstract class ClickReceiver {
    private static final @NotNull Map<@NotNull UUID, @Nullable RateLimiter> clickLimiters = new HashMap<>();
    private ClickReceiver() {}




    /**
     * Handles left and right clicks on contexts.
     * @param world The world the player is in.
     * @param player The player.
     * @param hand The hand used.
     * @param clickType The type of click (LEFT click or RIGHT click).
     * @return FAIL if the player clicked a context, PASS if not.
     */
    private static @NotNull InteractionResult onClick(final @NotNull Level world, final @NotNull Player player, final @NotNull InteractionHand hand, final @NotNull ClickAction clickType) {

        // Skip player if they are dead or in spectator mode
        if(player.isSpectator() || player.isDeadOrDying()) return InteractionResult.PASS;


        // Handle limiter
        RateLimiter limiter = clickLimiters.get(player.getUUID());
        if(limiter == null) {
            limiter = new RateLimiter();
            clickLimiters.put(player.getUUID(), limiter);
        }
        if(limiter.attempt()) limiter.renewCooldown(2);
        else return InteractionResult.FAIL;


        // Send click to the player's contexts and return if one is present
        @Nullable Context topMost = Context.findTopMostContext(player);
        if(topMost != null) {
            if(topMost.forwardClick(player, clickType)) {
                return InteractionResult.FAIL;
            }
        }


        return InteractionResult.PASS;
    }




    /**
     * Handles left and right clicks on contexts.
     * <p>
     * Must be called on AttackEntityCallback and UseEntityCallback events.
     * @param world The world the player is in.
     * @param player The player.
     * @param hand The hand used.
     * @param clickType The type of click (LEFT click or RIGHT click).
     * @param entity The entity.
     * @return FAIL if the player clicked a context, PASS if not.
     */
    public static @NotNull InteractionResult onClickEntity(final @NotNull Level world, final @NotNull Player player, final @NotNull InteractionHand hand, final @NotNull ClickAction clickType, final @NotNull Entity entity) {
        if(entity instanceof Interaction && entity.hasCustomName() && entity.getCustomName().getString().equals(InteractionBlocker.ENTITY_CUSTOM_NAME)) {
            return onClick(world, player, hand, clickType);
        }
        return InteractionResult.PASS;
    }




    /**
     * Handles left and right clicks on blocks before the interaction blocker is spawned.
     * <p>
     * Must be called on AttackBlockCallback and UseBlockCallback events.
     * @param world The world the player is in.
     * @param player The player.
     * @param hand The hand used.
     * @param clickType The type of click (LEFT click or RIGHT click).
     * @param pos The position of the clicked block.
     * @return FAIL if the player clicked a context, PASS if not.
     */
    public static @NotNull InteractionResult onClickBlock(final @NotNull Level world, final @NotNull Player player, final @NotNull InteractionHand hand, final @NotNull ClickAction clickType, final @NotNull Vec3i pos) {

        // Check ray casting result
        return onClick(world, player, hand, clickType);
    }




    /**
     * Handles right clicks on blocks before the interaction blocker is spawned.
     * <p>
     * Must be called on useItemCallback events.
     * @param world The world the player is in.
     * @param player The player.
     * @param hand The hand used.
     * @return FAIL if the player clicked a context, PASS if not.
     */
    public static @NotNull InteractionResult onUseItem(final @NotNull Level world, final @NotNull Player player, final @NotNull InteractionHand hand) {
        return onClick(world, player, hand, ClickAction.SECONDARY);
    }
}
