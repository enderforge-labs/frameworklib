package com.snek.frameworklib.graphics.core.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.core.elements.Elm;

import net.minecraft.world.entity.Display.BillboardConstraints;







/**
 * The default style of the generic {@link Elm} element.
 */
public class Style {
    public static final int S_TIME = 5; // Spawn   animation time. Measured in ticks
    public static final int D_TIME = 5; // Despawn animation time. Measured in ticks


    // Data
    private @NotNull  Flagged<@NotNull Transform>                transform = null;   // The current transformation
    private @NotNull  Flagged<@NotNull Float>                    viewRange = null;   // The view range. 1.0f = 64 blocks
    private @NotNull  Flagged<@NotNull BillboardConstraints> billboardMode = null;   // The billboard mode. Defines how the rendered entity is rotated relatively to the player's camera

    // Animations
    private @NotNull Flagged<@Nullable Animation>         primerAnimation  = null;   // The animation used to prepare the element to receive the spawning animation. Applied instantly.
    private @NotNull Flagged<@Nullable Animation>          spawnAnimation  = null;   // The spawning animation. Played when the entity is spawned into the level
    private @NotNull Flagged<@Nullable Animation>         despawnAnimation = null;   // The despawning animation. Played before the entity is removed from the level
    private @NotNull Flagged<@Nullable Animation>   inversePrimerAnimation = null;   // The inverse primer animation. Played after the entity is removed from the level




    /**
     * Creates a new ElmStyle.
     */
    public Style(final boolean reset) {
        if(reset) resetAll();
    }

    /**
     * Creates a new ElmStyle.
     */
    public Style() {
        this(true);
    }




    /**
     * Resets all the fields to their default value.
     */
    public void resetAll() {
        resetTransform();
        resetViewRange();
        resetBillboardMode();
        resetPrimerAnimation();
        resetSpawnAnimation();
        resetDespawnAnimation();
        resetInversePrimerAnimation();
    }

    /**
     * Flags all the fields.
     */
    public void flagAll() {
        editTransform();
        editViewRange();
        editBillboardMode();
        editPrimerAnimation();
        editSpawnAnimation();
        editDespawnAnimation();
        editInversePrimerAnimation();
    }




    // Default value providers
    public @NotNull  Transform            getDefaultTransform              () { return new Transform(); }
    public           float                getDefaultViewRange              () { return 0.2f; }
    public @NotNull  BillboardConstraints getDefaultBillboardMode          () { return BillboardConstraints.FIXED; }
    public @Nullable Animation            getDefaultPrimerAnimation        () { return null; }
    public @Nullable Animation            getDefaultSpawnAnimation         () { return null; }
    public @Nullable Animation            getDefaultDespawnAnimation       () { return null; }
    public @Nullable Animation            getDefaultInversePrimerAnimation () { return null; }


    // Reset functions
    public void resetTransform             () { transform              = Flagged.from(getDefaultTransform()             ); }
    public void resetViewRange             () { viewRange              = Flagged.from(getDefaultViewRange()             ); }
    public void resetBillboardMode         () { billboardMode          = Flagged.from(getDefaultBillboardMode()         ); }
    public void resetPrimerAnimation       () { primerAnimation        = Flagged.from(getDefaultPrimerAnimation()       ); }
    public void resetSpawnAnimation        () { spawnAnimation         = Flagged.from(getDefaultSpawnAnimation()        ); }
    public void resetDespawnAnimation      () { despawnAnimation       = Flagged.from(getDefaultDespawnAnimation()      ); }
    public void resetInversePrimerAnimation() { inversePrimerAnimation = Flagged.from(getDefaultInversePrimerAnimation()); }


    // Setters
    public void setTransform(final @NotNull  Transform transform) {
        assert Require.nonNull(transform, "transform");
        this.transform.set(transform);
    }
    public void setViewRange(final float viewRange) {
        assert Require.nonNegative(viewRange, "view range");
        this.viewRange.set(viewRange);
    }
    public void setBillboardMode(final @NotNull  BillboardConstraints billboardMode) {
        assert Require.nonNull(billboardMode, "billboard mode");
        this.billboardMode.set(billboardMode);
    }
    public void setPrimerAnimation       (final @Nullable Animation animation) { this.primerAnimation       .set(animation); }
    public void setSpawnAnimation        (final @Nullable Animation animation) { this.spawnAnimation        .set(animation); }
    public void setDespawnAnimation      (final @Nullable Animation animation) { this.despawnAnimation      .set(animation); }
    public void setInversePrimerAnimation(final @Nullable Animation animation) { this.inversePrimerAnimation.set(animation); }


    // Flagged getters
    public final @NotNull Flagged<@NotNull  Transform>            getFlaggedTransform       () { return transform;              }
    public final @NotNull Flagged<@NotNull  Float>                getFlaggedViewRange       () { return viewRange;              }
    public final @NotNull Flagged<@NotNull  BillboardConstraints> getFlaggedBillboardMode   () { return billboardMode;          }
    public final @NotNull Flagged<@Nullable Animation>            getFlaggedPrimerAnimation () { return primerAnimation;        }
    public final @NotNull Flagged<@Nullable Animation>            getFlaggedSpawnAnimation  () { return spawnAnimation;         }
    public final @NotNull Flagged<@Nullable Animation>            getFlaggedDespawnAnimation() { return despawnAnimation;       }
    public final @NotNull Flagged<@Nullable Animation>     getFlaggedInversePrimerAnimation () { return inversePrimerAnimation; }


    // Getters
    public final @NotNull  Transform            getTransform       () { return transform             .get(); }
    public final           float                getViewRange       () { return viewRange             .get(); }
    public final @NotNull  BillboardConstraints getBillboardMode   () { return billboardMode         .get(); }
    public final @Nullable Animation            getPrimerAnimation () { return primerAnimation       .get(); }
    public final @Nullable Animation            getSpawnAnimation  () { return spawnAnimation        .get(); }
    public final @Nullable Animation            getDespawnAnimation() { return despawnAnimation      .get(); }
    public final @Nullable Animation     getInversePrimerAnimation () { return inversePrimerAnimation.get(); }


    // Edit getters
    public @NotNull  Transform           editTransform       () { return transform             .edit(); }
    public           float               editViewRange       () { return viewRange             .edit(); }
    public @NotNull BillboardConstraints editBillboardMode   () { return billboardMode         .edit(); }
    public @Nullable Animation           editPrimerAnimation () { return primerAnimation       .edit(); }
    public @Nullable Animation           editSpawnAnimation  () { return spawnAnimation        .edit(); }
    public @Nullable Animation           editDespawnAnimation() { return despawnAnimation      .edit(); }
    public @Nullable Animation    editInversePrimerAnimation () { return inversePrimerAnimation.edit(); }


    // With setters
    public @NotNull Style withTransform             (final @NotNull  Transform            transform    ) { setTransform             (transform    ); return this; }
    public @NotNull Style withViewRange             (final           float                viewRange    ) { setViewRange             (viewRange    ); return this; }
    public @NotNull Style withBillboardMode         (final @NotNull  BillboardConstraints billboardMode) { setBillboardMode         (billboardMode); return this; }
    public @NotNull Style withPrimerAnimation       (final @Nullable Animation            animation    ) { setPrimerAnimation       (animation    ); return this; }
    public @NotNull Style withSpawnAnimation        (final @Nullable Animation            animation    ) { setSpawnAnimation        (animation    ); return this; }
    public @NotNull Style withDespawnAnimation      (final @Nullable Animation            animation    ) { setDespawnAnimation      (animation    ); return this; }
    public @NotNull Style withInversePrimerAnimation(final @Nullable Animation            animation    ) { setInversePrimerAnimation(animation    ); return this; }
}
