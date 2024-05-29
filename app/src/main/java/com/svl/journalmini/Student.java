package com.svl.journalmini;

public class Student {
    private String name;
    private String imageUrl;
    private int gamePoints;

    public Student(String name, String imageUrl, int gamePoints) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.gamePoints = gamePoints;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getGamePoints() {
        return gamePoints;
    }
}

