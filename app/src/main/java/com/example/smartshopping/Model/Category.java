package com.example.smartshopping.Model;

public class Category {
    int background;
    String cat_name,cat_id;

    public Category(){

    }

    public Category(int background, String cat_name, String cat_id) {
        this.background = background;
        this.cat_name = cat_name;
        this.cat_id = cat_id;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }
}
