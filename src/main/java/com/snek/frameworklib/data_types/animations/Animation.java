package com.snek.frameworklib.data_types.animations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;








/**
 * An animation expressed as a list of Transitions.
 */
public class Animation {
    private final @NotNull List<@NotNull Transition> transitions = new ArrayList<>();
    private final int totalDuration;


    /**
     * Creates a new Animation.
     * @param transitions One or more transitions.
     */
    public Animation(final @NotNull Transition... transitions) {
        int _totalDuration = 0;
        for(final Transition t : transitions) {
            this.transitions.add(new Transition(t));
            _totalDuration += t.getDuration();
        }

        this.totalDuration = _totalDuration;
    }


    /**
     * Inverts each of the transitions and flips their order.
     * <p>
     * This makes the animation look like it's being played backwards.
     * <p>
     * Notice: Background color, Background alpha, and opacity values are not affected.
     */
    public void invert() {
        for(final Transition t : transitions) {
            t.invert();
        }
        Collections.reverse(transitions);
    }


    /**
     * Returns the list of transitions that make up this animation as a read-only list.
     * @return The translations.
     */
    public @NotNull List<@NotNull Transition> getTransitions() {
        return Collections.unmodifiableList(transitions);
    }


    /**
     * Returns the total duration of this animation, expressed in ticks.
     * @return The total duration.
     */
    public int getTotalDuration() {
        return totalDuration;
    }
}
