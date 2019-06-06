package com.carrot.carrotnote.util;

import java.util.ArrayList;
import java.util.List;

public abstract class ConcatAdapter<T> {
    public static final String SEPARATOR = "!";
    private List<T> list = new ArrayList<>();

    public ConcatAdapter<T> add(T item) {
        list.add(item);
        return this;
    }

    public String concat() {
        return concat(list);
    }

    abstract public String concat(List<T> list);

    public List<T> split() {
        return list;
    }

    abstract public List<T> split(String msg);
}
