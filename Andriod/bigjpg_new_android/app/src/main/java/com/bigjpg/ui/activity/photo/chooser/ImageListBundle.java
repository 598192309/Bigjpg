package com.bigjpg.ui.activity.photo.chooser;


public class ImageListBundle {
    String imagePath;
    boolean isChecked;

    public ImageListBundle(String path, boolean isChecked) {
        this.imagePath = path;
        this.isChecked = isChecked;
    }
}