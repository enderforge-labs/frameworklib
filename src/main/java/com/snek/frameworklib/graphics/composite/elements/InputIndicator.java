package com.snek.frameworklib.graphics.composite.elements;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.frameworklib.data_types.graphics.AlignmentX;
import com.snek.frameworklib.data_types.graphics.AlignmentY;
import com.snek.frameworklib.data_types.graphics.TextAlignment;
import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.layout.Div;
import com.snek.frameworklib.graphics.basic.elements.PanelElm;
import com.snek.frameworklib.graphics.basic.elements.SimpleTextElm;
import com.snek.frameworklib.graphics.basic.styles.SimpleTextElmStyle;
import com.snek.frameworklib.graphics.composite.styles.InputIndicator_MouseButtonDown_S;
import com.snek.frameworklib.graphics.composite.styles.InputIndicator_MouseButtonUp_S;
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

    // Display data
    private final List<Div> elmList = new ArrayList<>();
    private boolean childrenSpawned = false;



    /**
     * Creates a new InputIndicator.
     * @param button The button to display.
     */
    public InputIndicator(final @NotNull ServerLevel level, final @NotNull ClickAction button) {
        super();
        assert Require.nonNull(level, "level");
        assert Require.nonNull(button, "button");
        Div e;


        // Create mouse display element
        final Div m = new PanelElm(level, new InputIndicator_MouseButtonDown_S());
        elmList.add(m);
        m.setSize(MOUSE_SIZE);
        m.setAlignment(AlignmentX.LEFT, AlignmentY.BOTTOM);
        {
            // Add mouse button
            e = m.addChild(new PanelElm(level, new InputIndicator_MouseButtonUp_S()));
            e.setSize(BUTTON_SIZE);
            e.setAlignment(button == ClickAction.PRIMARY ? AlignmentX.LEFT : AlignmentX.RIGHT, AlignmentY.TOP);
        }


        // Add text element
        e = new SimpleTextElm(level, new SimpleTextElmStyle()
            .withFontSize(6)
            .withTextAlignment(TextAlignment.LEFT)
        );
        elmList.add(e);
        e.setSize(new Vector2f(1 - MOUSE_SIZE.x - BUTTON_TEXT_SPACING, 1f));
        e.setAlignment(AlignmentX.RIGHT, AlignmentY.BOTTOM);
        text = (SimpleTextElm)e;
    }




    /**
     * Updates the display to show the new action.
     * @param description The description of the action associated with a button click. Can be null.
     */
    public void updateDisplay(final @Nullable String description) {

        // If the description is not null and the display is hidden, show it and update the text
        if(description != null) {
            text.getStyle(SimpleTextElmStyle.class).setText(new Txt(description).lightGray().get());

            // Add stored children (once)
            if(children.isEmpty()) {
                for(final Div e : elmList) {
                    addChild(e);
                }
            }

            // Spawn the children if needed
            if(!childrenSpawned) {
                for(final Div c : children) {
                    c.spawn(canvas.getContext().getSpawnPos(), true);
                }
                childrenSpawned = true;
            }
        }

        // If the description is null and the display is visible, hide it.
        else {

            // Despawn the children if needed
            if(childrenSpawned) {
                for(final Div c : children) {
                    c.despawn(true);
                }
                childrenSpawned = false;
            }
        }
    }
}
