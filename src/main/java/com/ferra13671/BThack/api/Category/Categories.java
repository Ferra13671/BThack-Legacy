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

    public static final Category HUD = new Category("HUD");

    public static List<Category> getCategories() {
        return new ArrayList<>(categories);
    }

    public static Category get(String name) {
        if (HUD.name().equals(name)) return HUD;
        for (Category category : categories)
            if (category.name().equals(name)) return category;
        return null;
    }

    public static Category register(String name) {
        Category category = new Category(name);
        categories.add(category);
        return category;
    }
}
