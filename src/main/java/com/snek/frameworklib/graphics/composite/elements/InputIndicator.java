package com.snek.frameworklib.graphics.composite.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.core.elements.Elm;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.graphics.composite.styles.InputIndicator_MouseButtonDown_S;
import com.snek.frameworklib.graphics.composite.styles.InputIndicator_MouseButtonUp_S;
import com.snek.frameworklib.graphics.composite.styles.InputIndicator_Text_S;
import com.snek.frameworklib.utils.Txt;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ClickAction;








/**
 * A display for click action descriptions defined by the element that's currently being hovered on.
 */
public class InputIndicator extends Div {
    public static final @NotNull Vector2f BUTTON_SIZE = new Vector2f(0.5f, 0.4f);   // The size of a mouse button compared to the mouse display
    public static final @NotNull Vector2f MOUSE_SIZE  = new Vector2f(0.025f, 1f);   // The size of the mouse display compared to the InputIndicator
    public static final float BUTTON_TEXT_SPACING = 0.025f / 2f;                    // The distance between mouse display and text
    private final @NotNull SimpleTextElm text;




    /**
     * Creates a new InputIndicator.
     * @param button The button to display.
     */
    public InputIndicator(final @NotNull ServerLevel world, final @NotNull ClickAction button) {
        super();
        Div e;


        // Create mouse display element
        final Div m = addChild(new PanelElm(world, new InputIndicator_MouseButtonDown_S()));
        m.setSize(MOUSE_SIZE);
        m.setAlignment(AlignmentX.LEFT, AlignmentY.BOTTOM);
        {
            // Add mouse button
            e = m.addChild(new PanelElm(world, new InputIndicator_MouseButtonUp_S()));
            e.setSize(BUTTON_SIZE);
            e.setAlignment(button == ClickAction.PRIMARY ? AlignmentX.LEFT : AlignmentX.RIGHT, AlignmentY.TOP);
        }


        // Add text element
        e = addChild(new SimpleTextElm(world, new InputIndicator_Text_S()));
        e.setSize(new Vector2f(1 - MOUSE_SIZE.x - BUTTON_TEXT_SPACING, 1f));
        e.setAlignment(AlignmentX.RIGHT, AlignmentY.BOTTOM);
        ((Elm)e).getStyle(SimpleTextElmStyle.class).setTextAlignment(TextAlignment.LEFT);
        text = (SimpleTextElm)e;
    }




    @Override
    public void spawn(@NotNull Vector3d pos, boolean animate) {
        // Empty
    }




    /**
     * Updates the display to show the new action.
     * @param description The description of the action associated with a button click. Can be null.
     */
    public void updateDisplay(final @Nullable String description) {

        // If the description is not null and the display is hidden, show it and update the text
        if(description != null) {
            text.getStyle(SimpleTextElmStyle.class).setText(new Txt(description).lightGray().get());
            super.spawn(canvas.getContext().getSpawnPos(), false);
        }

        // If the description is null and the display is visible, hide it.
        else {
            super.despawn(false);
        }
    }
}
