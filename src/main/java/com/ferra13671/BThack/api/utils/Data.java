package com.ferra13671.BThack.api.utils;

public class Data<T> {

    private T value;

    public Data() {}

    public Data(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
