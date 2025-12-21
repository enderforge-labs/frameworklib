package com.snek.frameworklib.data_types.displays;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.mixin.accessors.TextDisplayAccessorMixin;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.scheduler.Scheduler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Display.TextDisplay;
import net.minecraft.world.level.Level;







/**
 * A wrapper for Minecraft's TextDisplay.
 * <p>
 * This class allows for better customization and more readable code.
 */
public class CustomTextDisplay extends CustomDisplay {
    public @NotNull TextDisplay getRawDisplay() {
        assert Require.nonNull(heldEntity, "held entity");
        return (TextDisplay)heldEntity;
    }
    private @NotNull TextDisplayAccessorMixin getAccessibleDisplay() {
        assert Require.nonNull(heldEntity, "held entity");
        return (TextDisplayAccessorMixin)heldEntity;
    }


    // Component cache and flag used to remove the text when the opacity value is lower than 26
    public static final @NotNull Component EMPTY_TEXT = new Txt().get();
    private             @NotNull Component textCache = EMPTY_TEXT;
    private final boolean noTextUnderA26;


    // Opacity cache. This is used to understand if the entity is currently interpolating from less than 26 to 26 or more
    // or vice versa, avoiding making changes until the interpolation is complete.
    private final @NotNull int[] lastAlpha = new int[3];
    private long lastAlphaUpdate = 0;
    private boolean lastAlphaInitialized = false;
    private void shiftLastAlpha(final int newAlpha) {
        if(lastAlphaUpdate >= Scheduler.getTickNum()) {
            lastAlpha[0] = newAlpha;
            return;
        }

        if(!lastAlphaInitialized) {
            lastAlphaInitialized = true;
            lastAlpha[2] = 0;
            lastAlpha[1] = 0;
            lastAlpha[0] = newAlpha;
        }
        else {
            lastAlpha[2] = lastAlpha[1];
            lastAlpha[1] = lastAlpha[0];
            lastAlpha[0] = newAlpha;
        }
    }


    /**
     * This method flushes the opacity cache and ensures the displayed text doesn't remain
     * in an incorrect state after safety delays performed during short animations.
     * <p>
     * Must be called at the end of each animation tick. //FIXME this can cause issues if the transition ticks are not aligned wit the step size.
     */
    public void tick() {
        shiftLastAlpha(getTextOpacity());

        if(noTextUnderA26) {
            final boolean a0 = lastAlpha[0] < 26; //! Always equal to the current (or target) opacity
            final boolean a1 = lastAlpha[1] < 26;
            final boolean a2 = lastAlpha[2] < 26;
            if(a0 == a1 && a1 != a2) setText(textCache);
        }
    }




    /**
     * Creates a new CustomTextDisplay using an existing TextDisplay.
     * @param rawDisplay The display entity.
     * @param noTextUnderA26 Whether the text should not be rendered when the opacity is lower than {@code 26},
     *     as opposed to forcing a minimum opacity value.
     */
    public CustomTextDisplay(final @NotNull TextDisplay rawDisplay, final boolean noTextUnderA26) {
        super(rawDisplay);
        this.noTextUnderA26 = noTextUnderA26;
    }


    /**
     * Creates a new CustomTextDisplay in the specified level.
     * @param level The level.
     * @param noTextUnderA26 Whether the text should not be rendered when the opacity is lower than {@code 26},
     *     as opposed to forcing a minimum opacity value.
     */
    public CustomTextDisplay(final @NotNull Level level, final boolean noTextUnderA26) {
        super(new TextDisplay(EntityType.TEXT_DISPLAY, level));
        this.noTextUnderA26 = noTextUnderA26;
    }


    /**
     * Creates a new CustomTextDisplay using an existing TextDisplay.
     * @param rawDisplay The display entity.
     */
    public CustomTextDisplay(final @NotNull TextDisplay rawDisplay) {
        this(rawDisplay, true);
    }


    /**
     * Creates a new CustomTextDisplay in the specified level.
     * @param level The level.
     */
    public CustomTextDisplay(final @NotNull Level level) {
        this(level, true);
    }




    /**
     * Sets a new text value to the entity.
     * <p>
     * This is equivalent to changing the entity's "text" NBT.
     * @param text The new value.
     */
    public void setText(final @NotNull Component text) {
        assert Require.nonNull(text, "text");

        final boolean hideText = noTextUnderA26 && lastAlpha[0] < 26 && lastAlpha[1] < 26;
        getAccessibleDisplay().invokeSetText(hideText ? EMPTY_TEXT : text);
        textCache = text.copy();
    }


    /**
     * Sets a new line width value to the entity.
     * <p>
     * This is equivalent to changing the entity's "line_width" NBT.
     * @param width The new value.
     */
    public void setLineWidth(final int width) {
        assert Require.positive(width, "line width");
        getAccessibleDisplay().invokeSetLineWidth(width);
    }


    /**
     * Retrieves the entity's text value.
     * @return The current text.
     */
    public @NotNull Component getText() {
        return textCache;
    }


    /**
     * Retrieves the entity's line width value.
     * @return The current line width.
     */
    public int getLineWidth() {
        return getAccessibleDisplay().invokeGetLineWidth();
    }


    /**
     * Returns the actual text the entity is displaying, as opposed to the cached value, meaning that
     * this method returns an empty Component when {@code noTextUnderA26} is set to true and the opacity is less than {@code 26}.
     * @return The text value.
     */
    public @NotNull Component getTrueText() {
        return getAccessibleDisplay().invokeGetText();
    }




    /**
     * Sets the opacity value of the rendered text.
     * @param opacity The opacity value.
     * <p>
     * Values smaller than {@code 26} are converted to {@code 26} unless {@code noTextUnderA26} is set to {@code true}, in which case the text is not rendered at all.
     * This is done because minecraft ignores these values and usually makes the text fully opaque instead of fully transparent, rendering animations jittery.
     * <p>
     * Notice: Minecraft's interpolation is broken. Opacity values are NOT converted back to {@code 0-255} range
     * before interpolating, but the raw byte value ({@code 0 to 127}, {@code -128 to -1}) is used instead.
     */
    public void setTextOpacity(final int opacity) {
        assert Require.inRange(opacity, 0, 255, "text opacity");

        int a = opacity;
        shiftLastAlpha(a);
        lastAlphaUpdate = Scheduler.getTickNum();

        if(a < 26) {
            if(noTextUnderA26 && lastAlpha[1] < 26) {
                getAccessibleDisplay().invokeSetText(EMPTY_TEXT);
            }
            else {
                getAccessibleDisplay().invokeSetText(textCache);
                a = 26;
            }
        }
        else if(lastAlpha[1] >= 26 && lastAlpha[2] < 26) {
            getAccessibleDisplay().invokeSetText(textCache);
        }
        getAccessibleDisplay().invokeSetTextOpacity((byte)(a > 127 ? a - 256 : a));
    }


    /**
     * Retrieves the entity's text opacity value.
     * @return The current text opacity.
     */
    public int getTextOpacity() {
        final int a = getAccessibleDisplay().invokeGetTextOpacity();
        return a < 0 ? a + 256 : a;
    }


    /**
     * Sets a new background color value to the entity.
     * <p>
     * This is equivalent to changing the entity's "background" NBT.
     * @param argb The new value.
     */
    public void setBackground(final @NotNull Vector4i argb) {
        assert Require.nonNull(argb, "background color");
        assert Require.inRange(argb.x, 0, 255, "background color alpha");
        assert Require.inRange(argb.y, 0, 255, "background color red");
        assert Require.inRange(argb.z, 0, 255, "background color greeb");
        assert Require.inRange(argb.w, 0, 255, "background color blue");
        getAccessibleDisplay().invokeSetBackground((argb.x << 24) | (argb.y << 16) | (argb.z << 8) | argb.w);
    }


    /**
     * Retrieves the entity's background color value.
     * @return The current background color.
     */
    public @NotNull Vector4i getBackground() {
        final int bg = getAccessibleDisplay().invokeGetBackground();
        return new Vector4i((bg >> 24) & 0xFF, (bg >> 16) & 0xFF, (bg >> 8) & 0xFF, bg & 0xFF);
    }


    /**
     * Sets a new text alignment value to the entity.
     * <p>
     * This is equivalent to changing the entity's "text alignment" NBT.
     * @param alignment The new value.
     */
    public void setTextAlignment(final @NotNull TextAlignment alignment) {
        assert Require.nonNull(alignment, "text alignment");
        assert Require.nonNull(heldEntity, "held entity");

        final CompoundTag nbt = new CompoundTag();
        heldEntity.save(nbt);
        nbt.putString("alignment", alignment.asString());
        heldEntity.load(nbt);
    }
}
