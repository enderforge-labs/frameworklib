package com.snek.frameworklib.graphics.basic.styles;

import org.jetbrains.annotations.NotNull;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.basic.elements.ItemElm;
import com.snek.frameworklib.graphics.core.styles.ElmStyle;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;








/**
 * The default style of the generic {@link ItemElm} element.
 */
public class ItemElmStyle extends ElmStyle {
    private @NotNull Flagged<@NotNull ItemStack> item = null;




    /**
     * Creates a new ItemElmStyle.
     */
    public ItemElmStyle() {
        super();
    }


    @Override
    public void resetAll() {
        resetItem();
        super.resetAll();
    }

    @Override
    public void flagAll() {
        editItem();
        super.flagAll();
    }




    public @NotNull ItemStack getDefaultItem() { return Items.AIR.getDefaultInstance(); }
    public void resetItem() { item = Flagged.from(getDefaultItem()); }
    public void setItem(final @NotNull ItemStack item) {
        assert Require.nonNull(item, "item");
        this.item.set(item);
    }
    public @NotNull Flagged<@NotNull ItemStack> getFlaggedItem() { return item; }
    public @NotNull ItemStack getItem() { return item.get(); }
    public @NotNull ItemStack editItem() { return item.edit(); }
}