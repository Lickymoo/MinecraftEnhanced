package com.buoobuoo.minecraftenhanced.core.util;

@FunctionalInterface
public interface ToBooleanFunction<T>  {

    boolean applyAsBoolean(T value);
}
