package kr.co.crim.oss.rimdrive.files.service;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import kr.co.crim.oss.rimdrive.common.utils.CommonUtils;

public class StorageVO implements Serializable {

    private static final long serialVersionUID = 2798263833351639675L;

    private String storageId;
    private String ownerId;
    private String quota;
    private Date createDate;
    private String createUid;
    private Date modifyDate;
    private String modifyUid;

    private long fileSize;
    private long trashSize;
    private long versionSize;
    private long usedSize;
    private long freeSize;
    private int usedPercent;
    private int filePercent;
    private int trashPercent;
    private int versionPercent;

    private String displayFileSize;
    private String displayTrashSize;
    private String displayVersionSize;
    private String displayUsedSize;
    private String displayFreeSize;

    public String getStorageId() {
	return storageId;
    }

    public void setStorageId(String storageId) {
	this.storageId = storageId;
    }

    public String getOwnerId() {
	return ownerId;
    }

    public void setOwnerId(String ownerId) {
	this.ownerId = ownerId;
    }

    public String getQuota() {
	return quota;
    }

    public void setQuota(String quota) {
	this.quota = quota;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public String getCreateUid() {
	return createUid;
    }

    public void setCreateUid(String createUid) {
	this.createUid = createUid;
    }

    public Date getModifyDate() {
	return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
	this.modifyDate = modifyDate;
    }

    public String getModifyUid() {
	return modifyUid;
    }

    public void setModifyUid(String modifyUid) {
	this.modifyUid = modifyUid;
    }

    public long getFileSize() {
	return fileSize;
    }

    public void setFileSize(long fileSize) {
	this.fileSize = fileSize;
	this.displayFileSize = CommonUtils.readableFileSize(fileSize);
    }

    public long getTrashSize() {
	return trashSize;
    }

    public void setTrashSize(long trashSize) {
	this.trashSize = trashSize;
	this.displayTrashSize = CommonUtils.readableFileSize(trashSize);
    }

    public long getVersionSize() {
	return versionSize;
    }

    public void setVersionSize(long versionSize) {
	this.versionSize = versionSize;
	this.displayVersionSize = CommonUtils.readableFileSize(versionSize);
    }

    public long getUsedSize() {
	return usedSize;
    }

    public void setUsedSize(long usedSize) {
	this.usedSize = usedSize;
	this.displayUsedSize = CommonUtils.readableFileSize(usedSize);
    }

    public long getFreeSize() {
	return freeSize;
    }

    public void setFreeSize(long freeSize) {
	this.freeSize = freeSize;
	this.displayFreeSize = CommonUtils.readableFileSize(freeSize);
    }

    public int getUsedPercent() {
	return usedPercent;
    }

    public void setUsedPercent(int usedPercent) {
	this.usedPercent = usedPercent;
    }

    public int getFilePercent() {
	return filePercent;
    }

    public void setFilePercent(int filePercent) {
	this.filePercent = filePercent;
    }

    public int getTrashPercent() {
	return trashPercent;
    }

    public void setTrashPercent(int trashPercent) {
	this.trashPercent = trashPercent;
    }

    public int getVersionPercent() {
	return versionPercent;
    }

    public void setVersionPercent(int versionPercent) {
	this.versionPercent = versionPercent;
    }

    public String getDisplayFileSize() {
	return this.displayFileSize;
    }

    public String getDisplayTrashSize() {
	return this.displayTrashSize;
    }

    public String getDisplayVersionSize() {
	return this.displayVersionSize;
    }

    public String getDisplayFreeSize() {
	return this.displayFreeSize;
    }

    public String getDisplayUsedSize() {
	return this.displayUsedSize;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}
