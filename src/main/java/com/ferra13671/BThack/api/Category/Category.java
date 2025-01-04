package com.ferra13671.BThack.api.Category;

public record Category(String name) {

    public static Category of(String name) {
        return new Category(name);
    }
}
