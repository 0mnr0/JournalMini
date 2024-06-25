package com.svl.journalmini;

public class Student {
    private String name;
    private String imageUrl;
    private int gamePoints;
    private int position;

    public Student(String name, String imageUrl, int gamePoints, int pos) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.gamePoints = gamePoints;
        this.position = pos;
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

    public int getStudentPosition() {
        return position;
    }
}

