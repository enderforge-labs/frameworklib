package com.snek.frameworklib.graphics.basic.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.data_types.ui.TextAlignment;
import com.snek.frameworklib.data_types.ui.TextOverflowBehaviour;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;








public class SimpleTextElmStyle extends ElmStyle {
    public static final float DEFAULT_TEXT_SCALE = 0.3f;

    private @NotNull Flagged<@NotNull Component>             text                  = null;
    private @NotNull Flagged<@NotNull TextAlignment>         textAlignment         = null;
    private @NotNull Flagged<@NotNull Integer>               textOpacity           = null;
    private @NotNull Flagged<@NotNull TextOverflowBehaviour> textOverflowBehaviour = null;




    @Override
    public @NotNull Transform getDefaultTransform () {
        return new Transform().scale(DEFAULT_TEXT_SCALE);
    }




    /**
     * Creates a new default TextElmStyle.
     */
    public SimpleTextElmStyle() {
        super();
    }


    @Override
    public void resetAll() {
        resetText();
        resetTextAlignment();
        resetTextOpacity();
        super.resetAll();
    }




    @Override
    public @Nullable Animation getDefaultPrimerAnimation() {
        return new Animation(
            new Transition()
            .targetOpacity(0)
        );
    }


    @Override
    public @Nullable Animation getDefaultSpawnAnimation() {
        return new Animation(
            new Transition(ElmStyle.S_TIME, Easings.sineOut)
            .targetOpacity(255)
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
    public @NotNull TextAlignment         getDefaultTextAlignment        () { return TextAlignment.CENTER          ; }
    public          int                   getDefaultTextOpacity          () { return 255                           ; }
    public @NotNull TextOverflowBehaviour getDefaultTextOverflowBehaviour() { return TextOverflowBehaviour.ELLIPSIS; }


    // Reset functions
    public void resetText                  () { text                  = Flagged.from(getDefaultText()                 ); }
    public void resetTextAlignment         () { textAlignment         = Flagged.from(getDefaultTextAlignment()        ); }
    public void resetTextOpacity           () { textOpacity           = Flagged.from(getDefaultTextOpacity()          ); }
    public void resetTextOverflowBehaviour () { textOverflowBehaviour = Flagged.from(getDefaultTextOverflowBehaviour()); }


    // Setters
    public void setText                 (final @NotNull Component             _text                 ) { text                 .set(_text                 ); }
    public void setTextAlignment        (final @NotNull TextAlignment         _textAlignment        ) { textAlignment        .set(_textAlignment        ); }
    public void setTextOpacity          (final          int                   _textOpacity          ) { textOpacity          .set(_textOpacity          ); }
    public void setTextOverflowBehaviour(final @NotNull TextOverflowBehaviour _textOverflowBehaviour) { textOverflowBehaviour.set(_textOverflowBehaviour); }


    // Flagged getters
    public @NotNull Flagged<@NotNull Component>             getFlaggedText                 () { return text;                  }
    public @NotNull Flagged<@NotNull TextAlignment>         getFlaggedTextAlignment        () { return textAlignment;         }
    public @NotNull Flagged<@NotNull Integer>               getFlaggedTextOpacity          () { return textOpacity;           }
    public @NotNull Flagged<@NotNull TextOverflowBehaviour> getFlaggedTextOverflowBehaviour() { return textOverflowBehaviour; }


    // Getters
    public @NotNull Component             getText                 () { return text                 .get(); }
    public @NotNull TextAlignment         getTextAlignment        () { return textAlignment        .get(); }
    public          int                   getTextOpacity          () { return textOpacity          .get(); }
    public @NotNull TextOverflowBehaviour getTextOverflowBehaviour() { return textOverflowBehaviour.get(); }


    // Edit getters
    public @NotNull Component             editText                 () { return text                 .edit(); }
    public @NotNull TextOverflowBehaviour editTextOverflowBehaviour() { return textOverflowBehaviour.edit(); }
    //!                                   editTextAlignment Primitive types cannot be edited
    //!                                   editTextOpacity   Primitive types cannot be edited
}
