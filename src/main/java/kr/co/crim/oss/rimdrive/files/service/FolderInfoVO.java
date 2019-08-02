package kr.co.crim.oss.rimdrive.files.service;

import kr.co.crim.oss.rimdrive.common.utils.CommonUtils;

public class FolderInfoVO {

    private long fileId = 0;
    private String storageId;
    private String name;
    private String path;
    private String pathHash;

    private String permissions;

    private String originStorageId;
    private String originName;
    private String originPath;
    private String originUserName;
    
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
	this.path = CommonUtils.getDisplayPath(path);
    }

    public String getPathHash() {
	return pathHash;
    }

    public void setPathHash(String pathHash) {
	this.pathHash = pathHash;
    }

    public String getPermissions() {
	return permissions;
    }

    public void setPermissions(String permissions) {
	this.permissions = permissions;
    }

    public String getOriginStorageId() {
        return originStorageId;
    }

    public void setOriginStorageId(String originStorageId) {
        this.originStorageId = originStorageId;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getOriginPath() {
        return originPath;
    }

    public void setOriginPath(String originPath) {
        this.originPath = CommonUtils.getDisplayPath(originPath);
    }

    public String getOriginUserName() {
        return originUserName;
    }

    public void setOriginUserName(String originUserName) {
        this.originUserName = originUserName;
    }

}
