package com.snek.frameworklib.data_types.containers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;








/**
 * A simple container for two objects of different types.
 * @param <F> The type of the first value.
 * @param <S> The type of the second value.
 */
public class Pair<F, S> {

    // Values
    private @Nullable F first;
    private @Nullable S second;

    // Getters
    public @Nullable F getFirst () { return first;  }
    public @Nullable S getSecond() { return second; }

    // Setters
    public void setFirst (final @Nullable F _first ) { first  = _first;  }
    public void setSecond(final @Nullable S _second) { second = _second; }




    /**
     * Creates a new Pair, setting both elements to {@code null}.
     */
    public Pair() {
        first  = null;
        second = null;
    }


    /**
     * Creates a new Pair using the specified values.
     * @param _first The first value.
     * @param _second The second value.
     */
    public Pair(final @Nullable F _first, final @Nullable S _second) {
        first  = _first;
        second = _second;
    }


    /**
     * Creates a new Pair using the specified values.
     * @param <F> The type of the first value.
     * @param <S> The type of the second value.
     * @param _first The first value.
     * @param _second The second value.
     * @return The newly created Pair.
     */
    public static <F, S> @NotNull Pair<@Nullable F, @Nullable S> from(final @Nullable F _first, final @Nullable S _second) {
        return new Pair<>(_first, _second);
    }
}
