package kr.co.crim.oss.rimdrive.files.service;

import java.io.Serializable;

public class FileVO implements Serializable {

    private static final long serialVersionUID = 267081318418400523L;

    private long fileId = 0;
    private String storageId;
    private String fileType;
    private String path;
    private String pathHash;

    private long parentId = 0;
    private String name = "";
    private long size = 0;

    private String searchTag;
    private String mimeType;

    private String createUserId;
    private String modifyUserId;
    private java.util.Date createDate;
    private java.util.Date modifyDate;
    
    public long getFileId() {
	return fileId;
    }

    public void setFileId(long fileId) {
	this.fileId = fileId;
    }

    public String getStorageId() {
	return storageId;
    }

    public void setStorageId(String storageId) {
	this.storageId = storageId;
    }

    public String getFileType() {
	return fileType;
    }

    public void setFileType(String fileType) {
	this.fileType = fileType;
    }

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = path;
    }

    public String getPathHash() {
	return pathHash;
    }

    public void setPathHash(String pathHash) {
	this.pathHash = pathHash;
    }

    public long getParentId() {
	return parentId;
    }

    public void setParentId(long parentId) {
	this.parentId = parentId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public long getSize() {
	return size;
    }

    public void setSize(long size) {
	this.size = size;
    }

    public String getSearchTag() {
	return searchTag;
    }

    public void setSearchTag(String searchTag) {
	this.searchTag = searchTag;
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

    public String getModifyUserId() {
	return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
	this.modifyUserId = modifyUserId;
    }

    public java.util.Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(java.util.Date createDate) {
	this.createDate = createDate;
    }

    public java.util.Date getModifyDate() {
	return modifyDate;
    }

    public void setModifyDate(java.util.Date modifyDate) {
	this.modifyDate = modifyDate;
    }

}
