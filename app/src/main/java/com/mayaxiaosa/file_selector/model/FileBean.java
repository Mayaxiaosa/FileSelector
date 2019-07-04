package com.mayaxiaosa.file_selector.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/18.
 */

@SuppressWarnings("DefaultFileTemplate")
public class FileBean implements Serializable {
    private int PublicFile_ID;
    private String FileName;
    private String FileUrl;
    private String ReMark;
    private String Title;
    private String UploadUserName;
    private String UploadDate;
    private String CreateTime;
    private int FileInfo_ID;
    private String FileAppPath;

    private int AccessType;
    private String PFileClassifyName;
    private int IsCreate;
    private int RNo;
    private int icon;

    private int UserFile_ID;
    private int SrcType;
    private int SrcFileID;
    private int IsOtherPlate;
    private String UFileClassifyName;
    private String UploadUser_ID;


    public int getUserFile_ID() {
        return UserFile_ID;
    }

    public void setUserFile_ID(int userFile_ID) {
        UserFile_ID = userFile_ID;
    }

    public int getSrcType() {
        return SrcType;
    }

    public void setSrcType(int srcType) {
        SrcType = srcType;
    }

    public int getSrcFileID() {
        return SrcFileID;
    }

    public void setSrcFileID(int srcFileID) {
        SrcFileID = srcFileID;
    }

    public int getIsOtherPlate() {
        return IsOtherPlate;
    }

    public void setIsOtherPlate(int isOtherPlate) {
        IsOtherPlate = isOtherPlate;
    }

    public String getUFileClassifyName() {
        return UFileClassifyName;
    }

    public void setUFileClassifyName(String UFileClassifyName) {
        this.UFileClassifyName = UFileClassifyName;
    }

    public String getUploadUser_ID() {
        return UploadUser_ID;
    }

    public void setUploadUser_ID(String uploadUser_ID) {
        UploadUser_ID = uploadUser_ID;
    }

    private String FileSize;
    private boolean isSelected;
    private String base64;

    private String status;

    public FileBean(String path, String fileName, int icon , String fileSize) {
        FileName = fileName;
        FileAppPath = path;
        FileSize = fileSize;
        this.icon = icon;
    }
    public FileBean(String path, String fileName, String fileSize) {
        FileName = fileName;
        FileAppPath = path;
        FileSize = fileSize;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getFileSize() {
        return FileSize;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setFileSize(String fileSize) {
        FileSize = fileSize;
    }

    public int getPublicFile_ID() {
        return PublicFile_ID;
    }

    public void setPublicFile_ID(int publicFile_ID) {
        PublicFile_ID = publicFile_ID;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileUrl() {
        return FileUrl;
    }

    public void setFileUrl(String fileUrl) {
        FileUrl = fileUrl;
    }

    public String getReMark() {
        return ReMark;
    }

    public void setReMark(String reMark) {
        ReMark = reMark;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUploadUserName() {
        return UploadUserName;
    }

    public void setUploadUserName(String uploadUserName) {
        UploadUserName = uploadUserName;
    }

    public String getUploadDate() {
        return UploadDate;
    }

    public void setUploadDate(String uploadDate) {
        UploadDate = uploadDate;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getFileAppPath() {
        return FileAppPath;
    }

    public void setFileAppPath(String fileAppPath) {
        FileAppPath = fileAppPath;
    }

    public int getAccessType() {
        return AccessType;
    }

    public void setAccessType(int accessType) {
        AccessType = accessType;
    }

    public int getFileInfo_ID() {
        return FileInfo_ID;
    }

    public void setFileInfo_ID(int fileInfo_ID) {
        FileInfo_ID = fileInfo_ID;
    }

    public String getPFileClassifyName() {
        return PFileClassifyName;
    }

    public void setPFileClassifyName(String PFileClassifyName) {
        this.PFileClassifyName = PFileClassifyName;
    }

    public int getIsCreate() {
        return IsCreate;
    }

    public void setIsCreate(int isCreate) {
        IsCreate = isCreate;
    }

    public int getRNo() {
        return RNo;
    }

    public void setRNo(int RNo) {
        this.RNo = RNo;
    }
}
