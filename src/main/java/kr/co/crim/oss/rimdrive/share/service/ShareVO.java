package kr.co.crim.oss.rimdrive.share.service;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ShareVO {

    private long shareId;
    private String shareTp;
    private String storageId;
    private long fileId;
    private String token;
    private Date modifyDate;
    private String displayModifyDate;
    private String modifyUid;
    private Date createDate;
    private String displayCreateDate;
    private String createUid;

    public long getShareId() {
	return shareId;
    }

    public void setShareId(long shareId) {
	this.shareId = shareId;
    }

    public String getShareTp() {
	return shareTp;
    }

    public void setShareTp(String shareTp) {
	this.shareTp = shareTp;
    }

    public String getStorageId() {
	return storageId;
    }

    public void setStorageId(String storageId) {
	this.storageId = storageId;
    }

    public long getFileId() {
	return fileId;
    }

    public void setFileId(long fileId) {
	this.fileId = fileId;
    }

    public String getToken() {
	return token;
    }

    public void setToken(String token) {
	this.token = token;
    }

    public Date getModifyDate() {
	return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
	this.modifyDate = modifyDate;
    }

    public String getDisplayModifyDate() {
	return displayModifyDate;
    }

    public void setDisplayModifyDate(String displayModifyDate) {
	this.displayModifyDate = displayModifyDate;
    }

    public String getModifyUid() {
	return modifyUid;
    }

    public void setModifyUid(String modifyUid) {
	this.modifyUid = modifyUid;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public String getDisplayCreateDate() {
	return displayCreateDate;
    }

    public void setDispayCreateDate(String displayCreateDate) {
	this.displayCreateDate = displayCreateDate;
    }

    public String getCreateUid() {
	return createUid;
    }

    public void setCreateUid(String createUid) {
	this.createUid = createUid;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}
