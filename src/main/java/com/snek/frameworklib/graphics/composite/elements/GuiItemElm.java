package com.snek.frameworklib.graphics.composite.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.basic.elements.ItemElm;
import com.snek.frameworklib.graphics.basic.elements.TextElm;
import com.snek.frameworklib.graphics.basic.styles.TextStyle;
import com.snek.frameworklib.graphics.composite.styles.GuiItemStyle;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.Utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;







/**
 * A special {@link ItemElm} meant for use in 2D contexts.
 * <p>
 * It displays an ItemStack and an optional count.
 * <p>
 * This element automatically resizes the count text and the item to match its current dimensions.
 * @since v1.2.0
 */
public class GuiItemElm extends ItemElm {
    public static final float Z_SCALE = 0.001f;
    private final @Nullable TextElm text;




    /**
     * Creates a new GuiItemElm with the specified style and count.
     * @param level The level to spawn associated entities in.
     * @param style The style.
     * @param count The count to display.
     */
    public GuiItemElm(@NotNull ServerLevel level, @NotNull GuiItemStyle style, final @Nullable Integer count) {
        super(level, style.withDisplayContext(ItemDisplayContext.GUI));

        // Add count text element
        if(count != null) {
            assert Require.positive(count, "count");
            final Div e = addChild(new __GuiItemElm_Count(level, new TextStyle()
                .withText(new Txt(Utils.formatAmount(count)).white().get())
                .withFontSize(64)
            ));
            e.setSize(new Vector2f(1));
            text = (TextElm)e;
        }
        else {
            text = null;
        }
    }

    /**
     * Creates a new GuiItemElm with the default style and the specified count.
     * @param level The level to spawn associated entities in.
     * @param count The count to display.
     */
    public GuiItemElm(@NotNull ServerLevel level, final @Nullable Integer count) {
        this(level, new GuiItemStyle(), count);
    }

    /**
     * Creates a new GuiItemElm with the default style and the specified item and count.
     * @param level The level to spawn associated entities in.
     * @param item The item to display.
     * @param count The count to display.
     */
    public GuiItemElm(@NotNull ServerLevel level, final @NotNull ItemStack item, final @Nullable Integer count) {
        this(level, (GuiItemStyle)new GuiItemStyle().withItem(item), count);
    }

    /**
     * Creates a new GuiItemElm with the specified style, item and count.
     * @param level The level to spawn associated entities in.
     * @param style The style.
     * @param item The item to display.
     * @param count The count to display.
     */
    public GuiItemElm(@NotNull ServerLevel level, @NotNull GuiItemStyle style, final @NotNull ItemStack item, final @Nullable Integer count) {
        this(level, (GuiItemStyle)style.withItem(item), count);
    }

    /**
     * Creates a new GuiItemElm with the specified style.
     * @param level The level to spawn associated entities in.
     * @param style The style.
     */
    public GuiItemElm(@NotNull ServerLevel level, @NotNull GuiItemStyle style) {
        this(level, style, (Integer)null); //TODO replace with Option
    }

    /**
     * Creates a new GuiItemElm with the default style.
     * @param level The level to spawn associated entities in.
     */
    public GuiItemElm(@NotNull ServerLevel level) {
        this(level, new GuiItemStyle(), (Integer)null); //TODO replace with Option
    }

    /**
     * Creates a new GuiItemElm with the default style and the specified item.
     * @param level The level to spawn associated entities in.
     * @param item The item to display.
     */
    public GuiItemElm(@NotNull ServerLevel level, final @NotNull ItemStack item) {
        this(level, (GuiItemStyle)new GuiItemStyle().withItem(item), (Integer)null); //TODO replace with Option
    }

    /**
     * Creates a new GuiItemElm with the specified style and item.
     * @param level The level to spawn associated entities in.
     * @param style The style.
     * @param item The item to display.
     */
    public GuiItemElm(@NotNull ServerLevel level, @NotNull GuiItemStyle style, final @NotNull ItemStack item) {
        this(level, (GuiItemStyle)style.withItem(item), (Integer)null); //TODO replace with Option
    }




    //! Scale the item to match the element's size
    @Override
    public @NotNull Transform __calcTransform() {
        final float size = Math.min(getAbsSize().x, getAbsSize().y);
        return super.__calcTransform().scale(size, size, Z_SCALE).rotY((float)Math.PI);
    }


    //! Enforce a 1:1 aspect ratio
    //TODO move to Elm. Make this automatic
    //TODO AspectRatio.FREE
    //TODO AspectRatio.1_1
    //TODO AspectRatio.3_4
    //TODO AspectRatio.16_9
    @Override
    public void updateAbsSizeSelf() {
        if(parent == null) {
            final float minDim = Math.min(localSize.x, localSize.y);
            absSize.set(minDim, minDim);
        }
        else {
            final Vector2f unconstrained = new Vector2f(parent.getAbsSize()).mul(localSize);
            final float minDim = Math.min(unconstrained.x, unconstrained.y);
            absSize.set(minDim, minDim);
        }
        updateAbsPosSelf();
    }
}