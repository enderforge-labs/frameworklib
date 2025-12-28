package com.snek.frameworklib.graphics.basic.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.displays.CustomDisplay;
import com.snek.frameworklib.data_types.displays.CustomTextDisplay;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.generated.FontData;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.core.styles.ElmStyle;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.FontSize;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.scheduler.LoopTaskHandler;
import com.snek.frameworklib.utils.scheduler.Scheduler;

import net.minecraft.server.level.ServerLevel;








/**
 * The base class of {@link FancyTextElm} and {@link SimpleTextElm}.
 * <p>
 * This class is sealed as the two subclasses are handled differently throughout the library's codebase.
 */
public abstract sealed class __base_TextElm extends Elm permits FancyTextElm, SimpleTextElm {

    // Constants
    public static final char  ELLIPSIS_CHAR = '…';   // The ellipsis character to use when truncating text
    public static final int   SCROLL_DELAY  = 8;     // How often to move the text by SCROLL_AMOUNT pixels, in ticks
    public static final int   SCROLL_AMOUNT = 1;     // The number of characters to move the text by, every iteration
    public static final float SCROLL_BOUNDARY_DELAY = 20f / SCROLL_DELAY; // The amount of cycles to wait for before and after scrolling the text
    public static final int   ENTITY_MARGIN_WIDTH_PX = 2;
    public static final int   ENTITY_MARGIN_HEIGHT_PX = 2;


    // In-world data
    public abstract @NotNull CustomTextDisplay getTextDisplay();


    // Entity total size cache. This represents size the entity would have using the text stored in the style and the default transform. Measured in blocks
    protected float entityTotSizeCacheX = 0;
    protected float entityTotSizeCacheY = 0;

    // Entity visual size cache. This represents the actual size the entity has in the level when using the default transform. Measured in blocks
    protected float entityVisualSizeCacheX = 0;
    protected float entityVisualSizeCacheY = 0;


    // Scrolling text data
    private LoopTaskHandler textAutoScrollHandler = null;
    private int currentStartIndex = 0;
    private float boundaryElapsedIterations = 0;
    private int lastEnd = 0;
    private int lastStartIndex = 0;




    /**
     * Creates a new __base_TextElm using the specified entity.
     * @param level The level to create the element in.
     * @param entity The entity to use for the new element.
     * @param style The style to use.
     */
    protected __base_TextElm(final @NotNull ServerLevel level, final @NotNull CustomDisplay entity, final @NotNull ElmStyle style) {
        super(level, entity, style);
    }




    //TODO comment
    public void updateTotTextSizeCache() {
        final Vector2f s = __calcTextSizeCache(getStyle(SimpleTextElmStyle.class).getText().getString());
        entityTotSizeCacheX = s.x;
        entityTotSizeCacheY = s.y;
    }



    //TODO comment
    public void updateVisualTextSizeCache() {
        final Vector2f s = __calcTextSizeCache(getTextDisplay().getText().getString());
        entityVisualSizeCacheX = s.x;
        entityVisualSizeCacheY = s.y;
    }




    //TODO comment
    public Vector2f __calcTextSizeCache(final @NotNull String string) {
        assert Require.nonNull(string, "string");

        // Set cache to 0 if the text is empty.
        if(string.isEmpty()) {
            return new Vector2f(0);
        }

        // Calculate the number of lines. Set the cache to 0 if there are none
        final String[] lines = string.split("\n");
        if(lines.length == 0) {
            return new Vector2f(0);
        }



        // If lines are present, find the longest one and save its length
        double maxWidth = 0;
        for(final String line : lines) {
            final double w = FontSize.getStringWidth(line);
            if(w > maxWidth) maxWidth = w;
        }
        final float x = (float)maxWidth;


        // Calculate line height
        final int lineNum = lines.length;
        // entitySizeCacheY = (lineNum == 1 ? 0f : lineNum - 1) * 2 + lineNum * (float)FontSize.getHeight(); //TODO remove if not used
        final float y = lineNum * (float)FontSize.getHeight();


        // Return
        return new Vector2f(x, y);
    }








    //TODO this might need to be cached
    //TODO this might need to be cached
    //TODO check subclasses too
    /**
     * Calculates the in-world height the TextDisplay entity associated with this element would have if it were displaying the full text stored in the style.
     * <p>
     * Notice: The height can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p>
     * Notice: Wrapped lines are counted as one.
     * <p>
     * Notice: This height value does not include the eneity's margin. Use {@link #calcTotEntityHeightWithMargins()} to account for margins.
     * @return The height in blocks.
     */
    public float calcTotEntityHeight() {
        final float r = entityTotSizeCacheY == 0 ? 0 : entityTotSizeCacheY;
        return r * calcForegroundTransform().getScale().y;
    }

    /**
     * Calculates the in-world height of the TextDisplay entity associated with this element.
     * <p>
     * Notice: The height can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p>
     * Notice: Wrapped lines are counted as one.
     * <p>
     * Notice: This height value does not include the eneity's margin. Use {@link #calcTotEntityHeightWithMargins()} to account for margins.
     * @return The height in blocks.
     */
    public float calcVisualEntityHeight() {
        final float r = entityVisualSizeCacheY == 0 ? 0 : entityVisualSizeCacheY;
        return r * calcForegroundTransform().getScale().y;
    }




    /**
     * Calculates the in-world height the TextDisplay entity associated with this element would have if it were displaying the full text stored in the style.
     * <p>
     * Notice: The height can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p>
     * Notice: Wrapped lines are counted as one.
     * <p>
     * Notice: This height value includes the eneity's margin. Use {@link #calcTotEntityHeight()} to ignore them.
     * @return The height in blocks.
     */
    public float calcTotEntityHeightWithMargins() {
        final float r = entityTotSizeCacheY == 0 ? 0 : entityTotSizeCacheY;
        final float margin = ENTITY_MARGIN_HEIGHT_PX * 2f / FontData.TEXT_PIXEL_BLOCK_RATIO;
        return (r + margin) * calcForegroundTransform().getScale().y;
    }

    /**
     * Calculates the in-world height of the TextDisplay entity associated with this element.
     * <p>
     * Notice: The height can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p>
     * Notice: Wrapped lines are counted as one.
     * <p>
     * Notice: This height value includes the eneity's margin. Use {@link #calcTotEntityHeight()} to ignore them.
     * @return The height in blocks.
     */
    public float calcVisualEntityHeightWithMargins() {
        final float r = entityVisualSizeCacheY == 0 ? 0 : entityVisualSizeCacheY;
        final float margin = ENTITY_MARGIN_HEIGHT_PX * 2f / FontData.TEXT_PIXEL_BLOCK_RATIO;
        return (r + margin) * calcForegroundTransform().getScale().y;
    }








    //TODO this might need to be cached
    //TODO this might need to be cached
    //TODO check subclasses too
    /**
     * Calculates the in-world width the TextDisplay entity associated with this element would have if it were displaying the full text stored in the style.
     * <p>
     * Notice: The width can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p>
     * Notice: Wrapped lines are counted as one.
     * <p>
     * Notice: This width value includes the eneity's margin. Use {@link #calcTotEntityWidthWithMargins()} to account for margins.
     * @return The width in blocks.
     */
    public float calcTotEntityWidth() {
        final float r = entityTotSizeCacheX == 0 ? 0 : entityTotSizeCacheX;
        return r * calcForegroundTransform().getScale().x;
    }

    /**
     * Calculates the in-world width of the TextDisplay entity associated with this element.
     * <p>
     * Notice: The width can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p>
     * Notice: Wrapped lines are counted as one.
     * <p>
     * Notice: This width value includes the eneity's margin. Use {@link #calcTotEntityWidthWithMargins()} to account for margins.
     * @return The width in blocks.
     */
    public float calcVisualEntityWidth() {
        final float r = entityVisualSizeCacheX == 0 ? 0 : entityVisualSizeCacheX;
        return r * calcForegroundTransform().getScale().x;
    }




    /**
     * Calculates the in-world width the TextDisplay entity associated with this element would have if it were displaying the full text stored in the style.
     * <p>
     * Notice: The width can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p>
     * Notice: Wrapped lines are counted as one.
     * <p>
     * Notice: This width value includes the eneity's margin. Use {@link #calcTotEntityWidth()} to ignore them
     * @return The width in blocks.
     */
    public float calcTotEntityWidthWithMargins() {
        final float r = entityTotSizeCacheX == 0 ? 0f : entityTotSizeCacheX;
        final float margin = ENTITY_MARGIN_WIDTH_PX * 2f / FontData.TEXT_PIXEL_BLOCK_RATIO ;
        return (r + margin) * calcForegroundTransform().getScale().x;
    }

    /**
     * Calculates the in-world width of the TextDisplay entity associated with this element.
     * <p>
     * Notice: The width can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p>
     * Notice: Wrapped lines are counted as one.
     * <p>
     * Notice: This width value includes the eneity's margin. Use {@link #calcTotEntityWidth()} to ignore them
     * @return The width in blocks.
     */
    public float calcVisualEntityWidthWithMargins() {
        final float r = entityVisualSizeCacheX == 0 ? 0f : entityVisualSizeCacheX;
        final float margin = ENTITY_MARGIN_WIDTH_PX * 2f / FontData.TEXT_PIXEL_BLOCK_RATIO ;
        return (r + margin) * calcForegroundTransform().getScale().x;
    }








    private @NotNull Transform calcForegroundTransform() {
        /**/ if(this instanceof final SimpleTextElm e) { return                     e.__calcTransform();  }
        else if(this instanceof final FancyTextElm  e) { return e.__calcTransformFg(e.__calcTransform()); }

        //! This is never actually called. __base_TextElm is a sealed class that only permits SimpleTextElm and FancyTextElm
        assert Require.fail("calcForegroundTransform called on invalid class");
        throw new RuntimeException();
    }







    /**
     * Updates the overflow behaviour.
     * <p>
     * This automatically handles scrolling tasks and resets the text when needed.
     */
    protected void updateOverflowBehaviour() {

        // Cancel previous scroll task
        if(textAutoScrollHandler != null) {
            textAutoScrollHandler.cancel();
            textAutoScrollHandler = null;
        }


        // Cache data
        final Txt text = new Txt(getStyle(SimpleTextElmStyle.class).getText());
        final TextOverflowBehaviour behaviour = getStyle(SimpleTextElmStyle.class).getTextOverflowBehaviour();
        final float totWidth = calcTotEntityWidth();
        final float maxWidth = absSize.x;


        // Overflow (or the text fits): Reset text
        if(behaviour == TextOverflowBehaviour.OVERFLOW || totWidth <= maxWidth) {
            getTextDisplay().setText(text.get());
            updateVisualTextSizeCache();
        }


        // Cache data for the other cases
        else {
            final float xScale = calcForegroundTransform().getScale().x;
            final float pixelsInOneBlock = FontData.TEXT_PIXEL_BLOCK_RATIO / xScale;
            final int maxWidthPx = Math.round(maxWidth * pixelsInOneBlock);
            final @NotNull String textString = text.getString();
            switch(behaviour) {


                // Truncate: Compute truncated text
                //! OVERFLOW is handled before the switch. This is only for fallthrough
                case OVERFLOW, TRUNCATE: {
                    getTextDisplay().setText(text.substring(0, FontSize.calcMaxStringEnd(textString, 0, maxWidthPx)).get());
                    updateVisualTextSizeCache();
                    break;
                }


                // Ellipsis: Compute truncated text and add ellipsis character
                case ELLIPSIS: {
                    final int restrictedMaxWidthPx = Math.max(0, maxWidthPx - FontSize.getCharWidthPx(ELLIPSIS_CHAR));
                    getTextDisplay().setText(text.substring(0, FontSize.calcMaxStringEnd(textString, 0, restrictedMaxWidthPx)).cat(ELLIPSIS_CHAR).get());
                    updateVisualTextSizeCache();
                    break;
                }




                // Scroll: Start a new scroll task if the text doesn't fit
                case SCROLL: {

                    // Reset scroll data
                    currentStartIndex = 0;
                    boundaryElapsedIterations = 0;
                    lastStartIndex = 0;


                    // Calculate the longest substring that fits, starting from the end
                    final @NotNull String flippedString = new StringBuilder(text.getString()).reverse().toString();
                    final int endSegmentWidth = FontSize.calcMaxStringEnd(flippedString, 0, maxWidthPx);


                    // Run the first iteration instantly
                    //! This is needed in order to compute the correct visual width cache and proper spawn animations
                    //! This immediate call runs before the entity spawns
                    runScrollTask(text, xScale, maxWidthPx, textString, endSegmentWidth, false);
                    //! ^ Don't start animations as they would create a feedback look with this method and crash the server

                    // Start a new scroll task that runs every SCROLL_DELAY
                    textAutoScrollHandler = Scheduler.loop(SCROLL_DELAY, SCROLL_DELAY, () -> {
                        runScrollTask(text, xScale, maxWidthPx, textString, endSegmentWidth, true);
                    });
                    break;
                }
            }
        }
    }




    private void runScrollTask(final @NotNull Txt text, final float xScale, final int maxWidthPx, final String textString, final int endSegmentWidth, final boolean startAnimations) {
        assert Require.nonNull(text, "text");
        assert Require.nonNull(textString, "text string");
        assert Require.nonNegative(maxWidthPx, "max pixel width");
        assert Require.nonNegative(endSegmentWidth, "end segment width");

        // Update elapsed ticks and return if in delay period
        if(boundaryElapsedIterations < SCROLL_BOUNDARY_DELAY) {
            ++boundaryElapsedIterations;

            // Reset text position if end delay has passed
            if(boundaryElapsedIterations > 0) {
                currentStartIndex = 0;
                lastStartIndex = 0;
            }
            else return;
        }


        // Find new end
        final int end = FontSize.calcMaxStringEnd(textString, currentStartIndex, maxWidthPx);


        // Create and start scrolling animation (if requested)
        if(startAnimations) {
            final double moveAmountL = FontSize.getStringWidth(textString.substring(lastStartIndex, currentStartIndex));
            final double moveAmountR = currentStartIndex == 0 ? 0 : FontSize.getStringWidth(textString.substring(lastEnd, end));
            final @NotNull Transform transform0 = new Transform().moveX((float)((+moveAmountL + moveAmountR) / 2 * xScale));
            final @NotNull Transform transform1 = new Transform().moveX((float)((-moveAmountL - moveAmountR) / 2 * xScale));
            applyAnimationNow(new Transition(                            ).additiveTransform(transform0));
            applyAnimation   (new Transition(SCROLL_DELAY - 1, Easings.linear).additiveTransform(transform1));
        }


        // Shift string value by SCROLL_AMOUNT
        getTextDisplay().setText(text.substring(currentStartIndex, end).get());
        updateVisualTextSizeCache();
        lastStartIndex = currentStartIndex;
        currentStartIndex += SCROLL_AMOUNT;


        // If the remaining text fits, stop scrolling for the specified delay and restart the cycle
        if(currentStartIndex > text.length() - endSegmentWidth) {
            boundaryElapsedIterations = -SCROLL_BOUNDARY_DELAY;
        }


        // Update last end
        lastEnd = end;
    }




    // Force refresh overflow calculations when the absolute size changes.
    @Override
    public void updateAbsSizeSelf() {
        super.updateAbsSizeSelf();
        updateOverflowBehaviour();
    }
    @Override
    public void updateAbsSizeInverseSelf() {
        super.updateAbsSizeInverseSelf();
        updateOverflowBehaviour();
    }




    @Override
    public void spawn(final Vector3d pos, final boolean animate) {
        if(!isSpawned) {
            updateTotTextSizeCache();
            //! This forces the visual text to update.
            //! This is needed in order to get an accurate visualTextSizeCache and calculate proper spawn animations
            updateOverflowBehaviour();
            updateVisualTextSizeCache();
            super.spawn(pos, animate);
        }
    }

    @Override
    public void despawn(final boolean animate) {
        if(isSpawned) {
            super.despawn(animate);
            if(textAutoScrollHandler != null) {
                textAutoScrollHandler.cancel();
                textAutoScrollHandler = null;
            }
        }
    }
}
