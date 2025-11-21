package com.snek.frameworklib.utils;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Option<T> {
    /// Rust-Like wrapper of Optional, can also convert to Result, and will never return null.
    private final Optional<T> innerOptional;
    private Option(T object) {
        innerOptional = Optional.of(object);
    }
    private Option() {
        innerOptional = Optional.empty();
    }
    // Constructors are private. use Option.Some() and Option.None()
    public static <T> Option<T> Some(T object) {
        return new Option<>(object);
    }
    public static <T> Option<T> None() {
        return new Option<>();
    }

    public boolean is_some() {
        return innerOptional.isPresent();
    }
    public boolean is_none(){
        return  innerOptional.isEmpty();
    }

    public boolean is_some_and(Function<T, Boolean> f){
        if (this.is_some()) {
            return f.apply(this.unwrap());
        }
        return false;
    }
    public boolean is_none_or(Function<T, Boolean> f) {
        if (this.is_some()) {
            return f.apply(this.unwrap());
        }
        return true;
    }

    public T expect(String message) {
        return innerOptional.orElseThrow(() -> new NoSuchElementException(message));
    }
    public T unwrap() {
        return innerOptional.orElseThrow();
    }
    public T unwrap_or(T default_value){
        return innerOptional.orElse(default_value);
    }
    public T unwrap_or_else(Supplier<? extends T> supplier) {
        return innerOptional.orElseGet(supplier);
    }
    public <E> Result<T,E> ok_or(E err) {
        if (this.is_some()) {
            return Result.Ok(this.unwrap());
        }
        return Result.Err(err);
    }
    public <E> Result<T,E> ok_or_else(Supplier<E> f) {
        if (this.is_some()) {
            return Result.Ok(this.unwrap());
        }
        return Result.Err(f.get());
    }
    public Option<T> filter(Function<T, Boolean> filter){
        if (this.is_some_and(filter)) {
            return this;
        }
        return Option.None();
    }
    public Option<T> inspect(Consumer<T> c){
        if (this.is_some()) {
            c.accept(this.unwrap());
        }
        return this;
    }
    public <U> Option<U> map(Function<T,U> f){
        if (this.is_none()) {
            return Option.None();
        }
        return Option.Some(f.apply(this.unwrap()));
    }
    public <U> U map_or(Function<T,U> f, U default_value) {
        return this.map(f).unwrap_or(default_value);
    }
    public <U> U map_or_else(Function<T,U> f, Supplier<U> supplier) {
        return this.map(f).unwrap_or_else(supplier);
    }

    public <U> Option<U> and(Option<U> input) {
        if (input.is_some() && this.is_some()) {
            return input;
        }
        return Option.None();
    }
    public Option<T> or(Option<T> input) {
        if (this.is_some()) {return this;}
        if (input.is_some()) {return input;}
        return Option.None();
    }
    public Option<T> xor(Option<T> input) {
        if (this.is_some() && input.is_none()) {return this;}
        if (this.is_none() && input.is_some()) {return input;}
        return Option.None();
    }
    public <U> Option<U> and_then(Function<T,Option<U>> function) {
        if (this.is_some()) {
            return function.apply(this.unwrap());
        }
        return Option.None();
    }
    public Option<T> or_else(Supplier<Option<T>> function) {
        if (this.is_some()) {return this;}
        return function.get();
    }



}