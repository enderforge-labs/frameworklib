package com.snek.frameworklib.graphics.basic.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.basic.elements.ItemElm;
import com.snek.frameworklib.graphics.core.styles.Style;
import com.snek.frameworklib.utils.Easings;

import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;








/**
 * The default style of the generic {@link ItemElm} element.
 * @since v1.1.0
 */
public class ItemStyle extends Style {
    public static final float PRIMER_ANIMATION_SCALE = 0.00001f;

    private @NotNull Flagged<@NotNull ItemStack> item = null;
    private @NotNull Flagged<@NotNull ItemDisplayContext> displayContext = null;




    /**
     * Creates a new ItemStyle.
     */
    public ItemStyle(final boolean reset) {
        super(false);
        if(reset) resetAll();
    }

    /**
     * Creates a new ItemStyle.
     */
    public ItemStyle() {
        this(true);
    }


    @Override
    public void resetAll() {
        resetItem();
        resetDisplayContext();
        super.resetAll();
    }

    @Override
    public void flagAll() {
        editItem();
        editDisplayContext();
        super.flagAll();
    }




    @Override
    public @Nullable Animation getDefaultPrimerAnimation() {
        return new Animation(
            new Transition()
            .additiveTransform(new Transform().scale(PRIMER_ANIMATION_SCALE))
        );
    }


    @Override
    public @Nullable Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(Style.S_TIME, Easings.sineOut)
            .additiveTransform(new Transform().scale(1f / PRIMER_ANIMATION_SCALE))
        );
    }




    // Default
    public @NotNull ItemStack          getDefaultItem          () { return Items.AIR.getDefaultInstance(); }
    public @NotNull ItemDisplayContext getDefaultDisplayContext() { return ItemDisplayContext.FIXED; }
    //! ^ Use FIXED for consistent scales and orientations (FIXED = item frame)
    //! Other contexts are all over the place, especially for vanilla items
    //! Writing an exception for every single item is unfeasable


    // Reset
    public void resetItem          () { item           = Flagged.from(getDefaultItem()); }
    public void resetDisplayContext() { displayContext = Flagged.from(getDefaultDisplayContext()); }


    // Set
    public void setItem(final @NotNull ItemStack item) {
        assert Require.nonNull(item, "item");
        this.item.set(item);
    }
    public void setDisplayContext(final @NotNull ItemDisplayContext displayContext) {
        assert Require.nonNull(displayContext, "display context");
        this.displayContext.set(displayContext);
    }


    // Get flagged
    public final @NotNull Flagged<@NotNull ItemStack>          getFlaggedItem          () { return item; }
    public final @NotNull Flagged<@NotNull ItemDisplayContext> getFlaggedDisplayContext() { return displayContext; }


    // Get
    public final @NotNull ItemStack          getItem          () { return item.get(); }
    public final @NotNull ItemDisplayContext getDisplayContext() { return displayContext.get(); }


    // Edit
    public @NotNull ItemStack          editItem          () { return item.edit(); }
    public @NotNull ItemDisplayContext editDisplayContext() { return displayContext.edit(); }


    // With
    public @NotNull ItemStyle withItem          (final @NotNull ItemStack                    item) { setItem          (item);           return this; }
    public @NotNull ItemStyle withDisplayContext(final @NotNull ItemDisplayContext displayContext) { setDisplayContext(displayContext); return this; }
}