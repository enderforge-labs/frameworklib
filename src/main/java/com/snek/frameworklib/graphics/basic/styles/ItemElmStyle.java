package com.snek.frameworklib.graphics.basic.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.basic.elements.ItemElm;
import com.snek.frameworklib.graphics.core.styles.ElmStyle;

import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;








/**
 * The default style of the generic {@link ItemElm} element.
 */
public class ItemElmStyle extends ElmStyle {
    private @NotNull Flagged<@NotNull ItemStack> item = null;
    private @NotNull Flagged<@NotNull ItemDisplayContext> displayContext = null;




    /**
     * Creates a new ItemElmStyle.
     */
    public ItemElmStyle(final boolean reset) {
        super(false);
        if(reset) resetAll();
    }

    /**
     * Creates a new ItemElmStyle.
     */
    public ItemElmStyle() {
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
    public @NotNull ItemElmStyle withItem          (final @NotNull ItemStack                    item) { setItem          (item);           return this; }
    public @NotNull ItemElmStyle withDisplayContext(final @NotNull ItemDisplayContext displayContext) { setDisplayContext(displayContext); return this; }
}