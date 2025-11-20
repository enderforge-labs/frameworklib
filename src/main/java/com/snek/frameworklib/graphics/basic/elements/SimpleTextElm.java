package com.snek.frameworklib.graphics.basic.elements;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector4i;

import com.snek.frameworklib.FrameworkLib;
import com.snek.frameworklib.data_types.animations.InterpolatedData;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.data_types.displays.CustomDisplay;
import com.snek.frameworklib.data_types.displays.CustomTextDisplay;
import com.snek.frameworklib.data_types.ui.TextAlignment;
import com.snek.frameworklib.graphics.basic.styles.ElmStyle;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.scheduler.TaskHandler;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;








/**
 * An element that can display text.
 * This class has transparent background. For a text element with background color, use FancyTextElm.
 */
public non-sealed class SimpleTextElm extends __base_TextElm {

    // In-world data
    private @NotNull CustomTextDisplay  getThisEntity() { return getEntity(CustomTextDisplay.class); }
    private @NotNull SimpleTextElmStyle getThisStyle () { return getStyle (SimpleTextElmStyle.class); }


    // Scrolling text data
    private TaskHandler textAutoScrollHandler = null;




    /**
     * Creates a new SimpleTextElm using an existing entity and a custom style.
     * @param _world The world in which to place the element.
     * @param _entity The display entity.
     * @param _style The custom style.
     */
    protected SimpleTextElm(final @NotNull ServerLevel _world, final @NotNull CustomDisplay _entity, final @NotNull ElmStyle _style) {
        super(_world, _entity, _style);
        getThisEntity().setBackground(new Vector4i(0, 0, 0, 0));
        getThisEntity().setLineWidth(Integer.MAX_VALUE);
    }


    /**
     * Creates a new SimpleTextElm using a custom style.
     * @param _world The world in which to place the element.
     * @param _style The custom style.
     */
    protected SimpleTextElm(final @NotNull ServerLevel _world, final @NotNull ElmStyle _style) {
        this(_world, new CustomTextDisplay(_world), _style);
    }


    /**
     * Creates a new SimpleTextElm using the default style.
     * @param _world The world in which to place the element.
     */
    public SimpleTextElm(final @NotNull ServerLevel _world) {
        this(_world, new CustomTextDisplay(_world), new SimpleTextElmStyle());
    }




    @Override
    public void flushStyle() {

        // Handle text first (transform depends on it)
        { final Flagged<Component> f = getThisStyle().getFlaggedText();
        if(f.isFlagged()) {
            getThisEntity().setText(f.get());
            updateEntitySizeCacheX();
            updateEntitySizeCacheY();
            f.unflag();
        }}


        // Handle transform calculations separately
        {
            final Flagged<Transform> f = getThisStyle().getFlaggedTransform();
            if(f.isFlagged() || getThisStyle().getFlaggedTextAlignment().isFlagged() || getThisStyle().getFlaggedText().isFlagged()) {
                final Transform t = __calcTransform();
                if(getThisStyle().getTextAlignment() == TextAlignment.LEFT ) t.moveX(-(getAbsSize().x - calcEntityWidth()) / 2f);
                if(getThisStyle().getTextAlignment() == TextAlignment.RIGHT) t.moveX(+(getAbsSize().x - calcEntityWidth()) / 2f);
                getThisEntity().setTransformation(t.moveZ(EPSILON * epsilonPolarity).toMinecraftTransform());
                f.unflag();
            }
        }


        // Call superconstructor (transform is already unflagged) and handle the other values normally
        super.flushStyle();
        { final Flagged<Integer> f = getThisStyle().getFlaggedTextOpacity();
        if(f.isFlagged()) {
            getThisEntity().setTextOpacity(f.get());
            f.unflag();
        }}
        { final Flagged<TextAlignment> f = getThisStyle().getFlaggedTextAlignment();
        if(f.isFlagged()) {
            getThisEntity().setTextAlignment(f.get());
            f.unflag();
        }}
    }




    @Override
    protected void __applyTransitionStep(final @NotNull InterpolatedData d) {
        super.__applyTransitionStep(d);
        if(d.hasOpacity()) getThisStyle().setTextOpacity(d.getOpacity());
    }




    @Override
    protected @NotNull InterpolatedData __generateInterpolatedData() {
        return new InterpolatedData(
            getThisStyle().getTransform().copy(),
            null,
            null,
            getThisStyle().getTextOpacity()
        );
    }
    @Override
    protected @NotNull InterpolatedData __generateInterpolatedData(int index) {
        final InterpolatedData fd = futureDataQueue.get(index);
        return new InterpolatedData(
            fd.getTransform().copy(),
            null,
            null,
            fd.getOpacity()
        );
    }




    @Override
    public void spawn(final @NotNull Vector3d pos) {
        super.spawn(pos);

        //TODO remove
        // // Set tracking custom name
        // getThisEntity().setCustomNameVisible(false);
        // getThisEntity().setCustomName(new Txt(ENTITY_CUSTOM_NAME).get());
    }


    @Override
    public void despawn() {
        super.despawn();
    }


    @Override
    public boolean stepTransition() {
        final boolean r = super.stepTransition();
        getThisEntity().tick();
        return r;
    }
}
