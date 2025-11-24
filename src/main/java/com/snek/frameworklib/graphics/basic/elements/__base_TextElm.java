package com.snek.frameworklib.graphics.basic.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.displays.CustomDisplay;
import com.snek.frameworklib.data_types.displays.CustomTextDisplay;
import com.snek.frameworklib.data_types.ui.TextOverflowBehaviour;
import com.snek.frameworklib.generated.FontData;
import com.snek.frameworklib.graphics.basic.styles.ElmStyle;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.graphics.core.Elm;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.FontSize;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.scheduler.Scheduler;
import com.snek.frameworklib.utils.scheduler.TaskHandler;

import net.minecraft.server.level.ServerLevel;








public abstract sealed class __base_TextElm extends Elm permits FancyTextElm, SimpleTextElm {

    // Constants
    public static final char ELLIPSIS_CHAR = '…';       // The ellipsis character to use when truncating text
    public static final int SCROLL_DELAY = 4;           // How often to move the text by SCROLL_AMOUNT pixels, in ticks
    public static final int SCROLL_AMOUNT = 1;          // The number of characters to move the text by, every iteration
    public static final float SCROLL_BOUNDARY_DELAY = 20f / SCROLL_DELAY; // The amount of cycles to wait for before and after scrolling the text


    // In-world data
    public abstract @NotNull CustomTextDisplay getTextDisplay();


    // Entity size cache. This represents the actual size the entity has in the world when using the default transform. Measured in blocks
    protected float entitySizeCacheX = 0;
    protected float entitySizeCacheY = 0;


    // Scrolling text data
    private TaskHandler textAutoScrollHandler = null;
    private int currentStartIndex = 0;
    private float boundaryElapsedIterations = 0;
    private int lastEnd = 0;
    private int lastStartIndex = 0;




    protected __base_TextElm(final @NotNull ServerLevel _world, final @NotNull CustomDisplay _entity, final @NotNull ElmStyle _style) {
        super(_world, _entity, _style);
    }




    public void updateTotTextSizeCache() {
        final String string = getStyle(SimpleTextElmStyle.class).getText().getString();

        // Set cache to 0 if the text is empty.
        // final String string = getStyle(SimpleTextElmStyle.class).getText().getString(); //TODO remove
        // final String string = getTextDisplay().getText().getString(); //TODO CACHE THIS TOO
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
        for(int i = 0; i < lines.length; ++i) {
            final double w = FontSize.getStringWidth(lines[i]);
            if(w > maxWidth) maxWidth = w;
        }
        entitySizeCacheX = (float)maxWidth;


        // Calculate line height
        final int lineNum = lines.length;
        entitySizeCacheY = (lineNum == 1 ? 0f : lineNum - 1) * 2 + lineNum * (float)FontSize.getHeight();
    }








    //TODO this might need to be cached
    //TODO this might need to be cached
    //TODO this might need to be cached
    //TODO check subclasses too
    /**
     * Calculates the in-world height of the TextDisplay entity associated with a TextDisplay or FancyTextDisplay.
     * <p> NOTICE: The height can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p> NOTICE: Wrapped lines are counted as one.
     * @return The height in blocks.
     */
    public float calcEntityHeight() {

        // Return 0 if width cache is 0
        if(entitySizeCacheY == 0) {
            return 0;
        }

        // If not, get the transform and calculate the visual height using it
        return entitySizeCacheY * calcForegroundTransform().getScale().y;
    }




    //TODO this might need to be cached
    //TODO this might need to be cached
    //TODO this might need to be cached
    //TODO check subclasses too
    /**
     * Calculates the in-world width of the TextDisplay entity associated with a TextDisplay or FancyTextDisplay.
     * <p> NOTICE: The width can be inaccurate as a lot of assumptions are made to calculate it. The returned value is the best possible approximation.
     * <p> NOTICE: Wrapped lines are counted as one.
     * @return The width in blocks.
     */
    public float calcEntityWidth() {

        // Return 0 if width cache is 0
        if(entitySizeCacheX == 0) {
            return 0;
        }

        // If not, get the transform and calculate the visual width using it
        return entitySizeCacheX * calcForegroundTransform().getScale().x;
    }


    private @NotNull Transform calcForegroundTransform() {
        /**/ if(this instanceof SimpleTextElm e) { return                     e.__calcTransform();  }
        else if(this instanceof FancyTextElm  e) { return e.__calcTransformFg(e.__calcTransform()); }

        //! This is never actually called. __base_TextElm is a sealed class that only permits SimpleTextElm and FancyTextElm
        else throw new RuntimeException();
    }







    /**
     * //TODO
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
                        applyAnimation   (new Transition(SCROLL_DELAY, Easings.linear).additiveTransform(transform1));


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
    public void spawn(Vector3d pos) {
        updateTotTextSizeCache();
        super.spawn(pos);
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
