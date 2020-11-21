package com.bigjpg.ui.activity.photo.chooser;


import java.util.ArrayList;

/**
 * 一级GridView中每个item的数据模型
 * 
 */
public class ImageGroup  {
    /**
     * 文件夹名
     */
    private String dirName = "";

    /**
     * 文件夹下所有图片
     */
    private ArrayList<ImageListBundle> images = new ArrayList<>();

    private boolean isChecked = false;

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    /**
     * 获取第一张图片的路径(作为封面)
     * 
     * @return
     */
    public String getFirstImgPath() {
        if (images.size() > 0) {
            return images.get(0).imagePath;
        }
        return "";
    }

    /**
     * 获取图片数量
     * 
     * @return
     */
    public int getImageCount() {
        return images.size();
    }

    public ArrayList<ImageListBundle> getImages() {
        return images;
    }

    /**
     * 添加一张图片
     * 
     * @param image
     */
    public void addImage(ImageListBundle image) {
        if (images == null) {
            images = new ArrayList<>();
        }
        images.add(image);
    }

    @Override
    public String toString() {
        return "ImageGroup [firstImgPath=" + getFirstImgPath() + ", dirName=" + dirName
                + ", imageCount=" + getImageCount() + "]";
    }

    /**
     * <p>
     * 重写该方法
     * <p>
     * 使只要图片所在的文件夹名称(dirName)相同就属于同一个图片组
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ImageGroup)) {
            return false;
        }
        return dirName.equals(((ImageGroup)o).dirName);
    }

    public boolean isChecked(){
        return isChecked;
    }

    public void setChecked(boolean isChecked){
        this.isChecked = isChecked;
    }



}
