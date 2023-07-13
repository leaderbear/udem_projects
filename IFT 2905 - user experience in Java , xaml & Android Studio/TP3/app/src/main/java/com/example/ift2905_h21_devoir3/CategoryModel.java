package com.example.ift2905_h21_devoir3;

public class CategoryModel {
    private final int image;
    private final String title;
    private boolean fav;

    public CategoryModel(int image, String title, boolean fav) {
        this.image = image;
        this.title = title;
        this.fav = fav;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }
}
