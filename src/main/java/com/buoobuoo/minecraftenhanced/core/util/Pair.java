package com.buoobuoo.minecraftenhanced.core.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pair<L, R> {

    private R right;
    private L left;

    public Pair(L left, R right){
        this.right = right;
        this.left = left;
    }

    public static <E,T> Pair<E, T> of(E l, T r) {
        return new Pair<E,T>(l, r);
    }
}