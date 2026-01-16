package com.snek.frameworklib.data_types.containers;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;


//TODO properly document the class and every method
//TODO add @NotNull @Nullable and final to members, parameters and return types
//TODO properly format the code, split long one liners


public class Result<T, E> {

    private final Option<T> innerOption;
    private final Option<E> err;
    // innerOption and err can never both be None or both be Some. if they are, the object is invalid, and someone is a spoon.

    private Result(final T object) {
        this.innerOption = Option.Some(object);
        this.err = Option.None();
    }
    private Result(final E err, final Void ignored) { // This is because java doesn't allow you to have identical constructors with different definitions.
        this.innerOption = Option.None();
        this.err = Option.Some(err);
    }
    public static <T,E> Result<T,E> Ok(final T obj) {
        return new Result<>(obj);
    }
    public static <T,E> Result<T,E> Err(final E err) {
        return new Result<>(err,null); // null is ignored. it must be there because java is mildly annoying.
    }

    public boolean is_ok() {
        return innerOption.isSome();
    }
    public boolean is_ok_and(final Predicate<T> function) {
        return innerOption.isSomeAnd(function);
    }
    public boolean is_err() {
        return err.isSome();
    }
    public boolean is_err_and(final Predicate<E> function) {
        return err.isSomeAnd(function);
    }
    public Option<T> ok() {
        return innerOption;
    }
    public Option<E> err() {
        return err;
    }
    public T unwrap() {
        return this.innerOption.unwrap();
    }
    public T expect(final String msg) {
        return this.innerOption.expect(msg);
    }
    public T unwrap_or(final T default_value) {
        return this.innerOption.unwrapOr(default_value);
    }
    public T unwrap_or_else(final Function<E,T> function) {
        if(this.is_ok()) {return this.unwrap();}
        return function.apply(this.err.unwrap());
    }
    public E expect_err(final String msg) {
        return this.err.expect(msg);
    }
    public E unwrap_err() {
        return this.err.unwrap();
    }

    public <U> Result<U,E> map(final Function<T,U> function) {
        if(this.is_err()) {return Result.Err(err.unwrap());}
        return Result.Ok(this.ok().map(function).unwrap());
    }
    public <U> U map_or(final Function<T,U> function, final U default_value) {
        return this.ok().mapOr(function,default_value);
    }
    public <U> U map_or_else(final Function<T,U> function, final Function<E,U> default_function) {
        if(this.is_ok()) {return this.ok().map(function).unwrap();}
        return this.err().map(default_function).unwrap();
    }
    public <F> Result<T,F> map_err(final Function<E,F> function) {
        if(this.is_ok()) {return Result.Ok(this.unwrap());}
        return Result.Err(this.err().map(function).unwrap());
    }
    public Result<T,E> inspect(final Consumer<T> consumer) {
        this.ok().inspect(consumer);
        return this;
    }
    public Result<T,E> inspect_err(final Consumer<E> consumer) {
        this.err().inspect(consumer);
        return this;
    }
}
