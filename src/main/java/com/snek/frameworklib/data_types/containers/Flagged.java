package com.snek.frameworklib.data_types.containers;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;








/**
 * A wrapper class that can track value changes of the contained object.
 * <p>
 * Notice: Flagged values are created as flagged. Use {@link #unflag()} if you want them to start as not flagged.
 * @param <T> The type of the object to store.
 * @since v1.1.0
 */
public class Flagged<T> {
    private @Nullable T value;
    private boolean flag = true;


    /**
     * Creates a new Flagged value.
     * @param value The initial value.
     */
    private Flagged(final @Nullable T value) {
        this.value = value;
    }

    /**
     * Creates a new Flagged value.
     * @param <T> The type of the object to store.
     * @param value The initial value.
     * @return The newly created Flagged object.
     */
    public static <T> Flagged<T> from(final @Nullable T value) {
        return new Flagged<>(value);
    }




    /**
     * Returns the current value without flagging the object.
     * @return The value.
     */
    public @Nullable T get() {
        return value;
    }


    /**
     * Sets a new value.
     * <p>
     * Flags the object if {@code !this.get().equals(value)}.
     * @param value The new value.
     */
    public void set(final @Nullable T value) {
        if(!Objects.equals(this.value, value)) flag = true;
        this.value = value;
    }


    /**
     * Flags the object and returns a reference to its value.
     * <p>
     * This method always flags the object without checking for changes.
     * <p>
     * Notice:
     *     This returns a reference to the object, not a copy.
     *     For immutable types (e.g., Integer, String), reassignment creates a new object
     *     instead of modifying the internal value. You should use {@link #set(T)} instead.
     * @return The object reference.
     */
    public @Nullable T edit() {
        flag = true;
        return value;
    }




    /**
     * Returns the current value of the flag.
     * @return Whether the value has changed.
     */
    public boolean isFlagged() {
        return flag;
    }


    /**
     * Unflags the Flagged object.
     */
    public void unflag() {
        flag = false;
    }
}
