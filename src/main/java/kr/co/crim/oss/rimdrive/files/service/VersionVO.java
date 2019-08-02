package kr.co.crim.oss.rimdrive.files.service;

import java.io.Serializable;

public class VersionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private long versionNo;
    private long fileId;
    private String name;
    private String path;
    private long size;
    private String mimeType;
    private String createUserId;
    private String createUserName;
    private java.util.Date createDate;
    private String displayCreateDate;

    public long getVersionNo() {
	return versionNo;
    }

    public void setVersionNo(long versionNo) {
	this.versionNo = versionNo;
    }

    public long getFileId() {
	return fileId;
    }

    public void setFileId(long fileId) {
	this.fileId = fileId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = path;
    }

    public long getSize() {
	return size;
    }

    public void setSize(long size) {
	this.size = size;
    }

    public String getMimeType() {
	return mimeType;
    }

    public void setMimeType(String mimeType) {
	this.mimeType = mimeType;
    }

    public String getCreateUserId() {
	return createUserId;
    }

    public void setCreateUserId(String createUserId) {
	this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public java.util.Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(java.util.Date createDate) {
	this.createDate = createDate;
    }

    public String getDisplayCreateDate() {
        return displayCreateDate;
    }

    public void setDisplayCreateDate(String displayCreateDate) {
        this.displayCreateDate = displayCreateDate;
    }

}
