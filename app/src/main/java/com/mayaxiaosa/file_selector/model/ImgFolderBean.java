package com.mayaxiaosa.file_selector.model;

/**
 * Created by Administrator on 2017/12/19.
 */

@SuppressWarnings("DefaultFileTemplate")
public class ImgFolderBean {
    /**当前文件夹的路径*/
    private String dir;
    /**第一张图片的路径，用于做文件夹的封面图片*/
    private String fistImgPath;
    /**文件夹名*/
    private String name;
    /**文件夹中图片的数量*/
    private int count;

    public ImgFolderBean() {
    }

    public ImgFolderBean(String dir, String fistImgPath, String name, int count) {
        this.dir = dir;
        this.fistImgPath = fistImgPath;
        this.name = name;
        this.count = count;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getFistImgPath() {
        return fistImgPath;
    }

    public void setFistImgPath(String fistImgPath) {
        this.fistImgPath = fistImgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
