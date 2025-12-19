package com.snek.frameworklib.data_types.containers;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.frameworklib.debug.Assert;








/**
 * An ArrayDequeue that allows indexing in O(1) time.
 * @param <E> The type of the objects to store.
 */
public class IndexedArrayDeque<E> extends AccessibleArrayDeque<E> {
    public IndexedArrayDeque() {
        super();
    }



    /**
     * Returns the element at the specified position.
     * @param index The index of the element to return.
     * @return The element.
     * @throws IndexOutOfBoundsException if {@code index} is out of range
     *     ({@code index < 0 || index >= size()})
     */
    @SuppressWarnings("unchecked")
    public @Nullable E get(final int index) {
        if(index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }

        return (E)elements[(head + index) % elements.length];
    }



    /**
     * Returns the element at the specified position.
     * <p>
     * If the element is not present, new elements are added to the queue until the desired size is reached.
     * @param index The index of the element to return.
     * @param supplier The supplier function used to create new elements.
     * @return The element at index {@code index}.
     * @throws IndexOutOfBoundsException if {@code index} is out of range
     *     ({@code index < 0})
     */
    public @Nullable E getOrAdd(final int index, final @NotNull Supplier<E> supplier) {
        Assert.requireNonNull(supplier, "supplier");
        if(index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }

        while(index >= size()) add(supplier.get());
        return get(index);
    }



    /**
     * Sets a new value to the element at the specified position.
     * @param index The index of the element to set.
     * @param value The new value.
     * @throws IndexOutOfBoundsException if {@code index} is out of range
     *     ({@code index < 0 || index >= size()})
     */
    public void set(final int index, final @Nullable E value) {
        if(index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }

        elements[(head + index) % elements.length] = value;
    }
}