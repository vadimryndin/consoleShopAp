package pl.coherentsolutions.core.catalog;

import pl.coherentsolutions.core.category.Category;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Catalog implements Serializable {
    private final String name;
    private final List<Category> categories;

    public Catalog(String name) {
        this.name = name;
        this.categories = new ArrayList<>();
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    public void addCategories(List<Category> categories) {
        this.categories.addAll(categories);
    }

    @Override
    public String toString() {
        return "Catalog{" +
                "name='" + name + '\'' +
                ", categories=" + categories +
                '}';
    }

    public List<Category> getCategories() {
        return this.categories;
    }

    public String getName() {
        return name;
    }
}
