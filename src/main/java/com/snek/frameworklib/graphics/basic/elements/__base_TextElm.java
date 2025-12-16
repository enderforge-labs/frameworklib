package com.snek.frameworklib.graphics.basic.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.displays.CustomDisplay;
import com.snek.frameworklib.data_types.displays.CustomTextDisplay;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.generated.FontData;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.core.styles.ElmStyle;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.FontSize;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.scheduler.Scheduler;
import com.snek.frameworklib.utils.scheduler.TaskHandler;

import net.minecraft.server.level.ServerLevel;








/**
 * The base class of {@link FancyTextElm} and {@link SimpleTextElm}.
 * <p>
 * This class is sealed as the two subclasses are handled differently throughout the library's codebase.
 */
public abstract sealed class __base_TextElm extends Elm permits FancyTextElm, SimpleTextElm {

    // Constants
    public static final char ELLIPSIS_CHAR = '…';   // The ellipsis character to use when truncating text
    public static final int  SCROLL_DELAY  = 8;     // How often to move the text by SCROLL_AMOUNT pixels, in ticks
    public static final int  SCROLL_AMOUNT = 1;     // The number of characters to move the text by, every iteration
    public static final float SCROLL_BOUNDARY_DELAY = 20f / SCROLL_DELAY; // The amount of cycles to wait for before and after scrolling the text
    public static final int  ENTITY_MARGIN_WIDTH_PX = 2;
    public static final int  ENTITY_MARGIN_HEIGHT_PX = 2;


    // In-world data
    public abstract @NotNull CustomTextDisplay getTextDisplay();


    // Entity size cache. This represents the actual size the entity has in the level when using the default transform. Measured in blocks
    protected float entitySizeCacheX = 0;
    protected float entitySizeCacheY = 0;


    // Scrolling text data
    private TaskHandler textAutoScrollHandler = null;
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




    public void updateTotTextSizeCache() {
        final String string = getStyle(SimpleTextElmStyle.class).getText().getString();

        // Set cache to 0 if the text is empty.
        if(string.isEmpty()) {
            entitySizeCacheX = 0f;
            entitySizeCacheY = 0f;
            return;
        }

        // Calculate the number of lines. Set the cache to 0 if there are none
        final String[] lines = string.split("\n");
        if(lines.length == 0) {
            entitySizeCacheX = 0f;
            entitySizeCacheY = 0f;
            return;
        }

        // If lines are present, find the longest one and save its length
        double maxWidth = 0;
        for(String line : lines) {
            final double w = FontSize.getStringWidth(line);
            if(w > maxWidth) maxWidth = w;
        }
        entitySizeCacheX = (float)maxWidth;


        // Calculate line height
        final int lineNum = lines.length;
        // entitySizeCacheY = (lineNum == 1 ? 0f : lineNum - 1) * 2 + lineNum * (float)FontSize.getHeight(); //TODO remove if not used
        entitySizeCacheY = lineNum * (float)FontSize.getHeight();
    }








    //TODO this might need to be cached
    //TODO this might need to be cached
    //TODO check subclasses too
    /**
     * Calculates the in-world height of the TextDisplay entity associated with this element.
     * <p>
     * Notice: The height can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p>
     * Notice: Wrapped lines are counted as one.
     * <p>
     * Notice: This height value does not include the eneity's margin. Use {@link #calcEntityHeightWithMargins()} to account for margins.
     * @return The height in blocks.
     */
    public float calcEntityHeight() {
        final float r = entitySizeCacheY == 0 ? 0 : entitySizeCacheY;
        return r * calcForegroundTransform().getScale().y;
    }


    /**
     * Calculates the in-world height of the TextDisplay entity associated with this element.
     * <p>
     * Notice: The height can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p>
     * Notice: Wrapped lines are counted as one.
     * <p>
     * Notice: This height value includes the eneity's margin. Use {@link #calcEntityHeight()} to ignore them.
     * @return The height in blocks.
     */
    public float calcEntityHeightWithMargins() {
        final float r = entitySizeCacheY == 0 ? 0 : entitySizeCacheY;
        final float margin = ENTITY_MARGIN_HEIGHT_PX * 2f / FontData.TEXT_PIXEL_BLOCK_RATIO;
        return (r + margin) * calcForegroundTransform().getScale().y;
    }




    //TODO this might need to be cached
    //TODO this might need to be cached
    //TODO check subclasses too
    /**
     * Calculates the in-world width of the TextDisplay entity associated with this element.
     * <p>
     * Notice: The width can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p>
     * Notice: Wrapped lines are counted as one.
     * <p>
     * Notice: This width value includes the eneity's margin. Use {@link #calcEntityWidthWithMargins()} to account for margins.
     * @return The width in blocks.
     */
    public float calcEntityWidth() {
        final float r = entitySizeCacheX == 0 ? 0 : entitySizeCacheX;
        return r * calcForegroundTransform().getScale().x;
    }




    /**
     * Calculates the in-world width of the TextDisplay entity associated with this element.
     * <p>
     * Notice: The width can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p>
     * Notice: Wrapped lines are counted as one.
     * <p>
     * Notice: This width value includes the eneity's margin. Use {@link #calcEntityWidth()} to ignore them
     * @return The width in blocks.
     */
    public float calcEntityWidthWithMargins() {
        final float r = entitySizeCacheX == 0 ? 0f : entitySizeCacheX;
        final float margin = ENTITY_MARGIN_WIDTH_PX * 2f / FontData.TEXT_PIXEL_BLOCK_RATIO ;
        return (r + margin) * calcForegroundTransform().getScale().x;
    }




    private @NotNull Transform calcForegroundTransform() {
        /**/ if(this instanceof SimpleTextElm e) { return                     e.__calcTransform();  }
        else if(this instanceof FancyTextElm  e) { return e.__calcTransformFg(e.__calcTransform()); }

        //! This is never actually called. __base_TextElm is a sealed class that only permits SimpleTextElm and FancyTextElm
        else throw new RuntimeException();
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
        final float totWidth = calcEntityWidth();
        final float maxWidth = absSize.x;


        // Overflow (or the text fits): Reset text
        if(behaviour == TextOverflowBehaviour.OVERFLOW || totWidth <= maxWidth) {
            getTextDisplay().setText(text.get());
        }


        // Cache data for the other cases
        else {
            final float xScale = calcForegroundTransform().getScale().x;
            final float pixelsInOneBlock = FontData.TEXT_PIXEL_BLOCK_RATIO / xScale;
            final int maxWidthPx = Math.round(maxWidth * pixelsInOneBlock);
            final @NotNull String textString = text.getString();
            switch(behaviour) {


                // Truncate: Compute truncated text
                case OVERFLOW, TRUNCATE: {
                    getTextDisplay().setText(text.substring(0, FontSize.calcMaxStringEnd(textString, 0, maxWidthPx)).get());
                    break;
                }


                // Ellipsis: Compute truncated text and add ellipsis character
                case ELLIPSIS: {
                    final int restrictedMaxWidthPx = Math.max(0, maxWidthPx - FontSize.getCharWidthPx(ELLIPSIS_CHAR));
                    getTextDisplay().setText(text.substring(0, FontSize.calcMaxStringEnd(textString, 0, restrictedMaxWidthPx)).cat(ELLIPSIS_CHAR).get());
                    break;
                }




                // Scroll: Start a new scroll task if the text doesn't fit
                case SCROLL: {

                    // Reset scroll data
                    currentStartIndex = 0;
                    boundaryElapsedIterations = 0;
                    lastEnd = 0; //! Prob not needed since currentStartIndex==0 doesn't use lastEnd
                    lastStartIndex = 0;


                    // Calculate the longest substring that fits, starting from the end
                    final @NotNull String flippedString = new StringBuilder(text.getString()).reverse().toString();
                    final int endSegmentWidth = FontSize.calcMaxStringEnd(flippedString, 0, maxWidthPx);


                    // Start a new scroll task that runs every SCROLL_DELAY
                    textAutoScrollHandler = Scheduler.loop(0, SCROLL_DELAY, () -> {

                        // Update elapsed ticks and return if in delay period
                        if(boundaryElapsedIterations < SCROLL_BOUNDARY_DELAY) {
                            ++boundaryElapsedIterations;

                            // Reset text position if end delay has passed
                            if(boundaryElapsedIterations > 0) {
                                currentStartIndex = 0;
                                lastEnd = 0; //! Prob not needed since currentStartIndex==0 doesn't use lastEnd
                                lastStartIndex = 0;
                            }
                            else return;
                        }


                        // Find new end
                        final int end = FontSize.calcMaxStringEnd(textString, currentStartIndex, maxWidthPx);


                        // Create and start scrolling animation
                        final double moveAmountL = FontSize.getStringWidth(textString.substring(lastStartIndex, currentStartIndex));
                        final double moveAmountR = currentStartIndex == 0 ? 0 : FontSize.getStringWidth(textString.substring(lastEnd, end));
                        final @NotNull Transform transform0 = new Transform().moveX((float)((+moveAmountL + moveAmountR) / 2 * xScale));
                        final @NotNull Transform transform1 = new Transform().moveX((float)((-moveAmountL - moveAmountR) / 2 * xScale));
                        applyAnimationNow(new Transition(                            ).additiveTransform(transform0));
                        applyAnimation   (new Transition(SCROLL_DELAY - 1, Easings.linear).additiveTransform(transform1));


                        // Shift string value by SCROLL_AMOUNT
                        getTextDisplay().setText(text.substring(currentStartIndex, end).get());
                        lastStartIndex = currentStartIndex;
                        currentStartIndex += SCROLL_AMOUNT;


                        // If the remaining text fits, stop scrolling for the specified delay and restart the cycle
                        if(currentStartIndex > text.length() - endSegmentWidth) {
                            boundaryElapsedIterations = -SCROLL_BOUNDARY_DELAY;
                        }


                        // Update last end
                        lastEnd = end;
                    });
                    break;
                }
            }
        }
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
    public void spawn(Vector3d pos, final boolean animate) {
        updateTotTextSizeCache();
        super.spawn(pos, animate);
    }

    @Override
    public void despawn(final boolean animate) {
        super.despawn(animate);
        if(textAutoScrollHandler != null) {
            textAutoScrollHandler.cancel();
            textAutoScrollHandler = null;
        }
    }
}
