package com.snek.frameworklib.graphics.functional.elements;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.snek.frameworklib.debug.Require;
import com.snek.frameworklib.graphics.functional.styles.TextInputElmStyle;
import com.snek.frameworklib.graphics.interfaces.Clickable;
import com.snek.frameworklib.input.MessageReceiver;
import com.snek.frameworklib.utils.Txt;
import com.snek.frameworklib.utils.scheduler.Scheduler;
import com.snek.frameworklib.utils.scheduler.TaskHandler;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;








/**
 * A generic text input class that allows the user to enter a string input through the chat after clicking the element.
 * <p>
 * This element automatically handles style.text updates.
 * Don't call {@code .setText} directly. Use {@link #setDIsplayedText(Component)} instead.
 */
public abstract class TextInputElm extends FancyButtonElm {
    public static final int CURSOR_TOGGLE_DELAY = 10;

    private final @Nullable Component        clickFeedbackMessage;
    private       @Nullable TaskHandler inputStateHandler = null;
    private                 boolean     cursorToggleState = true;
    private                 boolean     inputState        = false;


    private @Nullable Component displayedText = null;




    /**
     * Creates a new TextInputElm using a custom style.
     * @param level The level in which to place the element.
     * @param lmbActionName The name of the action associated with left clicks.
     * @param rmbActionName The name of the action associated with right clicks.
     * @param clickFeedbackMessage The message to show to the player when they click the element.
     * @param style The custom style.
     */
    protected TextInputElm(
        final @NotNull ServerLevel level,
        final @Nullable String lmbActionName,
        final @Nullable String rmbActionName,
        final @Nullable Component clickFeedbackMessage,
        final @NotNull TextInputElmStyle style
    ) {
        super(level, lmbActionName, rmbActionName, 1, style);
        this.clickFeedbackMessage = clickFeedbackMessage;
    }


    /**
     * Creates a new TextInputElm using the default style.
     * @param level The level in which to place the element.
     * @param lmbActionName The name of the action associated with left clicks.
     * @param rmbActionName The name of the action associated with right clicks.
     * @param clickFeedbackMessage The message to show to the player when they click the element.
     */
    protected TextInputElm(
        final @NotNull ServerLevel level,
        final @Nullable String lmbActionName,
        final @Nullable String rmbActionName,
        final @Nullable Component clickFeedbackMessage
    ) {
        this(level, lmbActionName, rmbActionName, clickFeedbackMessage, new TextInputElmStyle());
    }



    //TODO maybe change the entity's text directly like how text elements do it with scrollings?
    //TODO it might make the logic simpler. though it has to be compatible with the scrolling text resolution

    //TODO maybe change the entity's text directly like how text elements do it with scrollings?
    //TODO it might make the logic simpler. though it has to be compatible with the scrolling text resolution

    /**
     * Changes the text displayed in the element when it leaves the input state.
     * @param displayedText The text to display. Can be null.
     */
    public void setDisplayedText(final @Nullable Component displayedText) {
        this.displayedText = displayedText;
    }
    public void forceTextUpdate() {
        getStyle(TextInputElmStyle.class).setText(displayedText);
        flushStyle();
    }




    @Override
    public void onClick(final @NotNull Player player, final @NotNull ClickAction click, final @NotNull Vector2f coords) {
        super.onClick(player, click, coords);
        if(!inputState) {
            enterInputState();
            Clickable.playSound(player);
        }
        if(clickFeedbackMessage != null) player.displayClientMessage(clickFeedbackMessage, true);
        MessageReceiver.setCallback(player, this::__internal_messageCallback);
    }


    /**
     * Enters the input state.
     * <p>
     * This hides the text to show a blinking cursor.
     */
    protected void enterInputState() {
        if(!inputState) {
            inputState = true;
            cursorToggleState = true;
            inputStateHandler = Scheduler.loop(0, CURSOR_TOGGLE_DELAY, () -> {
                getStyle(TextInputElmStyle.class).setText(new Txt(cursorToggleState ? "|" : " ").get());
                flushStyle();
                cursorToggleState = !cursorToggleState;
            });
        }
    }


    /**
     * Exists the input state.
     * <p>
     * This stops the blinking animation, hides the cursor and shows the text.
     */
    protected void exitInputState() {
        if(inputState) {
            inputState = false;
            if(inputStateHandler != null) inputStateHandler.cancel();
            forceTextUpdate();
        }
    }


    /**
     * A wrapper for the message callback.
     * @param s The input string.
     * @return True if the string s is recognized as an input and should not be broadcasted to the chat, false otherwise.
     */
    protected boolean __internal_messageCallback(final @NotNull String s) {
        assert Require.nonNull(s, "input string");

        final boolean r = messageCallback(s);
        if(r) exitInputState();
        return r;
    }


    /**
     * The callback function called when a chat input is received.
     * @param s The input string.
     * @return True if the string s is recognized as an input and should not be broadcasted to the chat, false otherwise.
     */
    protected abstract boolean messageCallback(@NotNull String s);




    @Override
    public void onHoverExit(final @Nullable Player player) {
        super.onHoverExit(player);
        exitInputState();
        if(player != null) MessageReceiver.removeCallback(player);
    }
}
