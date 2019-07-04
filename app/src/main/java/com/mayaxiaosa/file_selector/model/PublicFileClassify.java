package com.mayaxiaosa.file_selector.model;

import java.io.Serializable;

/**
 * 共享文件分类
 * Created by Administrator on 2017/12/19.
 */

public class PublicFileClassify implements Serializable {
    private int PFileClassify_ID;
    private int Unit_ID;
    private String PFileClassifyName;
    private int OrderNo;
    private String PublicFile_ID;
    private String Unit_ID1;
    private String User_ID;
    private String Title;
    private String FileInfo_ID;
    private String PFileClassify_ID1;
    private String ReMark;
    private String CreateTime;
    private boolean isSelected;

    public PublicFileClassify() {
    }

    public PublicFileClassify(int PFileClassify_ID, String PFileClassifyName) {
        this.PFileClassify_ID = PFileClassify_ID;
        this.PFileClassifyName = PFileClassifyName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getPFileClassify_ID() {
        return PFileClassify_ID;
    }

    public void setPFileClassify_ID(int PFileClassify_ID) {
        this.PFileClassify_ID = PFileClassify_ID;
    }

    public int getUnit_ID() {
        return Unit_ID;
    }

    public void setUnit_ID(int unit_ID) {
        Unit_ID = unit_ID;
    }

    public String getPFileClassifyName() {
        return PFileClassifyName;
    }

    public void setPFileClassifyName(String PFileClassifyName) {
        this.PFileClassifyName = PFileClassifyName;
    }

    public int getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(int orderNo) {
        OrderNo = orderNo;
    }

    public String getPublicFile_ID() {
        return PublicFile_ID;
    }

    public void setPublicFile_ID(String publicFile_ID) {
        PublicFile_ID = publicFile_ID;
    }

    public String getUnit_ID1() {
        return Unit_ID1;
    }

    public void setUnit_ID1(String unit_ID1) {
        Unit_ID1 = unit_ID1;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getFileInfo_ID() {
        return FileInfo_ID;
    }

    public void setFileInfo_ID(String fileInfo_ID) {
        FileInfo_ID = fileInfo_ID;
    }

    public String getPFileClassify_ID1() {
        return PFileClassify_ID1;
    }

    public void setPFileClassify_ID1(String PFileClassify_ID1) {
        this.PFileClassify_ID1 = PFileClassify_ID1;
    }

    public String getReMark() {
        return ReMark;
    }

    public void setReMark(String reMark) {
        ReMark = reMark;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }
}
