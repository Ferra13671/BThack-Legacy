package com.ferra13671.BThack.api.Category;

import java.util.ArrayList;
import java.util.List;

public class Categories {
    private static final List<Category> categories = new ArrayList<>();

    public static final Category COMBAT = register("COMBAT");
    public static final Category MISC = register("MISC");
    public static final Category CLIENT = register("CLIENT");
    public static final Category RENDER = register("RENDER");
    public static final Category MOVEMENT = register("MOVEMENT");
    public static final Category PLAYER = register("PLAYER");
    public static final Category WORLD = register("WORLD");

    public static final Category HUD = new Category("HUD");

    public static List<Category> getCategories() {
        return new ArrayList<>(categories);
    }

    public static Category register(String name) {
        Category category = Category.of(name);
        categories.add(category);
        return category;
    }
}
