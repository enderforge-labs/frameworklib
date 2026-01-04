package com.snek.frameworklib.graphics.basic.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.basic.elements.FancyTextElm;
import com.snek.frameworklib.graphics.core.styles.ElmStyle;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;








/**
 * The default style of the generic {@link FancyTextElm} element.
 */
public class SimpleTextElmStyle extends ElmStyle {
    public static final float DEFAULT_TEXT_SCALE = 0.3f;
    public static final int   DEFAULT_FONT_SIZE  = 12;
    public static final float TEXT_FONT_FACTOR = DEFAULT_TEXT_SCALE / DEFAULT_FONT_SIZE;

    public static final float SPAWN_ANIMATION_SHIFT = 0.025f;

    private @NotNull Flagged<@NotNull Component>             text                  = null;
    private @NotNull Flagged<@NotNull TextOverflowBehaviour> textOverflowBehaviour = null;
    private @NotNull Flagged<@NotNull TextAlignment>         textAlignment         = null;
    private @NotNull Flagged<@NotNull Integer>               textOpacity           = null;
    private @NotNull Flagged<@NotNull Integer>               fontSize              = null;



    /**
     * Creates a new SimpleTextElmStyle.
     */
    public SimpleTextElmStyle(final boolean reset) {
        super(false);
        if(reset) resetAll();
    }

    /**
     * Creates a new SimpleTextElmStyle.
     */
    public SimpleTextElmStyle() {
        this(true);
    }


    @Override
    public void resetAll() {
        resetText();
        resetTextOverflowBehaviour();
        resetTextAlignment();
        resetTextOpacity();
        resetFontSize();
        super.resetAll();
    }

    @Override
    public void flagAll() {
        editText();
        editTextOverflowBehaviour();
        editTextAlignment();
        editTextOpacity();
        editFontSize();
        super.flagAll();
    }




    @Override
    public @Nullable Animation getDefaultPrimerAnimation() {
        return new Animation(
            new Transition()
            .targetOpacity(0)
            .additiveTransform(new Transform().moveX(SPAWN_ANIMATION_SHIFT))
        );
    }


    @Override
    public @Nullable Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .targetOpacity(255)
            .additiveTransform(new Transform().moveX(-SPAWN_ANIMATION_SHIFT))
        );
    }


    @Override
    public @Nullable Animation getDefaultDespawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.D_TIME, Easings.sineOut)
            .targetOpacity(0)
        );
    }




    // Default value providers
    public @NotNull Component             getDefaultText                 () { return new Txt().get()               ; }
    public @NotNull TextOverflowBehaviour getDefaultTextOverflowBehaviour() { return TextOverflowBehaviour.OVERFLOW; }
    public @NotNull TextAlignment         getDefaultTextAlignment        () { return TextAlignment.CENTER          ; }
    public          int                   getDefaultTextOpacity          () { return 255                           ; }
    public          int                   getDefaultFontSize             () { return DEFAULT_FONT_SIZE             ; }


    // Reset functions
    public void resetText                  () { text                  = Flagged.from(getDefaultText()                 ); }
    public void resetTextOverflowBehaviour () { textOverflowBehaviour = Flagged.from(getDefaultTextOverflowBehaviour()); }
    public void resetTextAlignment         () { textAlignment         = Flagged.from(getDefaultTextAlignment()        ); }
    public void resetTextOpacity           () { textOpacity           = Flagged.from(getDefaultTextOpacity()          ); }
    public void resetFontSize              () { fontSize              = Flagged.from(getDefaultFontSize()             ); }


    // Setters
    public void setText(final @NotNull Component text) {
        assert Require.nonNull(text, "text");
        this.text.set(text);
    }
    public void setTextOverflowBehaviour(final @NotNull TextOverflowBehaviour textOverflowBehaviour) {
        assert Require.nonNull(textOverflowBehaviour, "text overflow behaviour");
        this.textOverflowBehaviour.set(textOverflowBehaviour);
    }
    public void setTextAlignment(final @NotNull TextAlignment textAlignment) {
        assert Require.nonNull(textAlignment, "text alignment");
        this.textAlignment.set(textAlignment);
    }
    public void setTextOpacity(final int textOpacity) {
        assert Require.inRange(textOpacity, 0, 255, "opacity");
        this.textOpacity.set(textOpacity);
    }
    public void setFontSize(final int fontSize) {
        assert Require.positive(fontSize, "font size");
        this.fontSize.set(fontSize);
    }


    // Flagged getters
    public @NotNull Flagged<@NotNull Component>             getFlaggedText                 () { return text;                  }
    public @NotNull Flagged<@NotNull TextOverflowBehaviour> getFlaggedTextOverflowBehaviour() { return textOverflowBehaviour; }
    public @NotNull Flagged<@NotNull TextAlignment>         getFlaggedTextAlignment        () { return textAlignment;         }
    public @NotNull Flagged<@NotNull Integer>               getFlaggedTextOpacity          () { return textOpacity;           }
    public @NotNull Flagged<@NotNull Integer>               getFlaggedFontSize             () { return fontSize;              }


    // Getters
    public @NotNull Component             getText                 () { return text                 .get(); }
    public @NotNull TextOverflowBehaviour getTextOverflowBehaviour() { return textOverflowBehaviour.get(); }
    public @NotNull TextAlignment         getTextAlignment        () { return textAlignment        .get(); }
    public          int                   getTextOpacity          () { return textOpacity          .get(); }
    public          int                   getFontSize             () { return fontSize             .get(); }


    // Edit getters
    public @NotNull Component             editText                 () { return text                 .edit(); }
    public @NotNull TextOverflowBehaviour editTextOverflowBehaviour() { return textOverflowBehaviour.edit(); }
    public @NotNull TextAlignment         editTextAlignment        () { return textAlignment        .edit(); }
    public          int                   editTextOpacity          () { return textOpacity          .edit(); }
    public          int                   editFontSize             () { return fontSize             .edit(); }


    // With setters
    public @NotNull SimpleTextElmStyle withText                 (final @NotNull Component             text                 ) { setText                 (text                 ); return this; }
    public @NotNull SimpleTextElmStyle withTextOverflowBehaviour(final @NotNull TextOverflowBehaviour textOverflowBehaviour) { setTextOverflowBehaviour(textOverflowBehaviour); return this; }
    public @NotNull SimpleTextElmStyle withTextAlignment        (final @NotNull TextAlignment         textAlignment        ) { setTextAlignment        (textAlignment        ); return this; }
    public @NotNull SimpleTextElmStyle withTextOpacity          (final          int                   textOpacity          ) { setTextOpacity          (textOpacity          ); return this; }
    public @NotNull SimpleTextElmStyle withFontSize             (final          int                   fontSize             ) { setFontSize             (fontSize             ); return this; }
}