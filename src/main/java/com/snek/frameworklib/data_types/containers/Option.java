package com.snek.frameworklib.data_types.containers;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;




//TODO properly document the class and every method
//TODO add @NotNull @Nullable and final to members, parameters and return types
//TODO properly format the code, split long one liners

public class Option<T> {
    // Rust-Like wrapper of Optional, can also convert to Result, and will never return null.
    private final Optional<T> innerOptional;
    private Option(final T object) {
        innerOptional = Optional.of(object);
    }
    private Option() {
        innerOptional = Optional.empty();
    }
    // Constructors are private. use Option.Some() and Option.None()
    public static <T> Option<T> Some(final T object) {
        return new Option<>(object);
    }
    public static <T> Option<T> None() {
        return new Option<>();
    }

    public boolean is_some() {
        return innerOptional.isPresent();
    }
    public boolean is_none() {
        return  innerOptional.isEmpty();
    }

    public boolean is_some_and(final Function<T, Boolean> f) {
        if(this.is_some()) {
            return f.apply(this.unwrap());
        }
        return false;
    }
    public boolean is_none_or(final Function<T, Boolean> f) {
        if(this.is_some()) {
            return f.apply(this.unwrap());
        }
        return true;
    }

    public T expect(final String message) {
        return innerOptional.orElseThrow(() -> new NoSuchElementException(message));
    }
    public T unwrap() {
        return innerOptional.orElseThrow();
    }
    public T unwrap_or(final T default_value) {
        return innerOptional.orElse(default_value);
    }
    public T unwrap_or_else(final Supplier<? extends T> supplier) {
        return innerOptional.orElseGet(supplier);
    }
    public <E> Result<T,E> ok_or(final E err) {
        if(this.is_some()) {
            return Result.Ok(this.unwrap());
        }
        return Result.Err(err);
    }
    public <E> Result<T,E> ok_or_else(final Supplier<E> f) {
        if(this.is_some()) {
            return Result.Ok(this.unwrap());
        }
        return Result.Err(f.get());
    }
    public Option<T> filter(final Function<T, Boolean> filter) {
        if(this.is_some_and(filter)) {
            return this;
        }
        return Option.None();
    }
    public Option<T> inspect(final Consumer<T> c) {
        if(this.is_some()) {
            c.accept(this.unwrap());
        }
        return this;
    }
    public <U> Option<U> map(final Function<T,U> f) {
        if(this.is_none()) {
            return Option.None();
        }
        return Option.Some(f.apply(this.unwrap()));
    }
    public <U> U map_or(final Function<T,U> f, final U default_value) {
        return this.map(f).unwrap_or(default_value);
    }
    public <U> U map_or_else(final Function<T,U> f, final Supplier<U> supplier) {
        return this.map(f).unwrap_or_else(supplier);
    }

    public <U> Option<U> and(final Option<U> input) {
        if(input.is_some() && this.is_some()) {
            return input;
        }
        return Option.None();
    }
    public Option<T> or(final Option<T> input) {
        if(this.is_some()) {return this;}
        if(input.is_some()) {return input;}
        return Option.None();
    }
    public Option<T> xor(final Option<T> input) {
        if(this.is_some() && input.is_none()) {return this;}
        if(this.is_none() && input.is_some()) {return input;}
        return Option.None();
    }
    public <U> Option<U> and_then(final Function<T,Option<U>> function) {
        if(this.is_some()) {
            return function.apply(this.unwrap());
        }
        return Option.None();
    }
    public Option<T> or_else(final Supplier<Option<T>> function) {
        if(this.is_some()) {return this;}
        return function.get();
    }
}