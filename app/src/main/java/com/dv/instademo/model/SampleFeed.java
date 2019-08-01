package com.dv.instademo.model;

public class SampleFeed {

    private String id;
    private String title;
    private int image;
    private String description;

    public SampleFeed(String id, String title, int image, String description) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }
}
