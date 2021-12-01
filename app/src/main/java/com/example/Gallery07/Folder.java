package com.example.Gallery07;

import android.widget.ImageView;

public class Folder {
    private String folderName;
    private int folderImage;

    public Folder(String folderName, int folderImage) {
        this.folderName = folderName;
        this.folderImage = folderImage;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getFolderImage() {
        return folderImage;
    }

    public void setFolderImage(int folderImage) {
        this.folderImage = folderImage;
    }

    @Override
    public String toString() {
        return "Folder{" +
                "folderName='" + folderName + '\'' +
                ", folderImage=" + folderImage +
                '}';
    }
}