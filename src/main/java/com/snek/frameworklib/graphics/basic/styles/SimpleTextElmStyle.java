package com.snek.frameworklib.graphics.basic.styles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.data_types.animations.Animation;
import com.snek.frameworklib.data_types.animations.Transform;
import com.snek.frameworklib.data_types.animations.Transition;
import com.snek.frameworklib.data_types.containers.Flagged;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.data_types.graphics.TextOverflowBehaviour;
import com.snek.frameworklib.graphics.core.styles.ElmStyle;
import com.snek.frameworklib.utils.Easings;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.network.chat.Component;








/**
 * The default style of the generic {@link FancyTextElm} element.
 */
public class SimpleTextElmStyle extends ElmStyle {
    public static final float DEFAULT_TEXT_SCALE = 0.3f;

    private @NotNull Flagged<@NotNull Component>             text                  = null;
    private @NotNull Flagged<@NotNull TextOverflowBehaviour> textOverflowBehaviour = null;
    private @NotNull Flagged<@NotNull TextAlignment>         textAlignment         = null;
    private @NotNull Flagged<@NotNull Integer>               textOpacity           = null;




    /**
     * Creates a new SimpleTextElmStyle.
     */
    public SimpleTextElmStyle() {
        super();
    }




    @Override
    public @NotNull Transform getDefaultTransform () {
        return new Transform().scale(DEFAULT_TEXT_SCALE);
    }


    @Override
    public void resetAll() {
        resetText();
        resetTextOverflowBehaviour();
        resetTextAlignment();
        resetTextOpacity();
        super.resetAll();
    }

    @Override
    public void flagAll() {
        editText();
        editTextOverflowBehaviour();
        editTextAlignment();
        editTextOpacity();
        super.flagAll();
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
    public @NotNull TextOverflowBehaviour getDefaultTextOverflowBehaviour() { return TextOverflowBehaviour.OVERFLOW; }
    public @NotNull TextAlignment         getDefaultTextAlignment        () { return TextAlignment.CENTER          ; }
    public          int                   getDefaultTextOpacity          () { return 255                           ; }


    // Reset functions
    public void resetText                  () { text                  = Flagged.from(getDefaultText()                 ); }
    public void resetTextOverflowBehaviour () { textOverflowBehaviour = Flagged.from(getDefaultTextOverflowBehaviour()); }
    public void resetTextAlignment         () { textAlignment         = Flagged.from(getDefaultTextAlignment()        ); }
    public void resetTextOpacity           () { textOpacity           = Flagged.from(getDefaultTextOpacity()          ); }


    // Setters
    public void setText                 (final @NotNull Component             text                 ) { this.text                 .set(text                 ); }
    public void setTextOverflowBehaviour(final @NotNull TextOverflowBehaviour textOverflowBehaviour) { this.textOverflowBehaviour.set(textOverflowBehaviour); }
    public void setTextAlignment        (final @NotNull TextAlignment         textAlignment        ) { this.textAlignment        .set(textAlignment        ); }
    public void setTextOpacity          (final          int                   textOpacity          ) { this.textOpacity          .set(textOpacity          ); }


    // Flagged getters
    public @NotNull Flagged<@NotNull Component>             getFlaggedText                 () { return text;                  }
    public @NotNull Flagged<@NotNull TextOverflowBehaviour> getFlaggedTextOverflowBehaviour() { return textOverflowBehaviour; }
    public @NotNull Flagged<@NotNull TextAlignment>         getFlaggedTextAlignment        () { return textAlignment;         }
    public @NotNull Flagged<@NotNull Integer>               getFlaggedTextOpacity          () { return textOpacity;           }


    // Getters
    public @NotNull Component             getText                 () { return text                 .get(); }
    public @NotNull TextOverflowBehaviour getTextOverflowBehaviour() { return textOverflowBehaviour.get(); }
    public @NotNull TextAlignment         getTextAlignment        () { return textAlignment        .get(); }
    public          int                   getTextOpacity          () { return textOpacity          .get(); }


    // Edit getters
    public @NotNull Component             editText                 () { return text                 .edit(); }
    public @NotNull TextOverflowBehaviour editTextOverflowBehaviour() { return textOverflowBehaviour.edit(); }
    public @NotNull TextAlignment         editTextAlignment        () { return textAlignment        .edit(); }
    public          int                   editTextOpacity          () { return textOpacity          .edit(); }
}