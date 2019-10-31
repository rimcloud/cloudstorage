package kr.co.crim.oss.rimdrive.share.service;

import org.apache.commons.lang3.builder.ToStringBuilder;

import kr.co.crim.oss.rimdrive.common.utils.CommonUtils;

public class SharingOutListVO {

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

    private String displaySize = "";
    private String shareTp = "";
    private String bookmarked = "N";
    private String icon;

    private String createUserId;
    private String modifyUserId;
    private java.util.Date createDate;
    private java.util.Date modifyDate;
    private String displayModifyDate;
    private String displayCreateDate;

    private int shareWithCnt;
    private String shareWithAll;
    private java.util.Date shareDate;
    private String displayShareDate;

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
	this.path = CommonUtils.getDisplayPath(path);
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
	this.displaySize = CommonUtils.readableFileSize(size);
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

    public String getDisplaySize() {
	return displaySize;
    }

    public String getShareTp() {
	return shareTp;
    }

    public void setShareTp(String shareTp) {
	this.shareTp = shareTp;
    }

    public String getBookmarked() {
	return bookmarked;
    }

    public void setBookmarked(String bookmarked) {
	this.bookmarked = bookmarked;
    }

    public String getIcon() {
	return icon;
    }

    public void setIcon(String icon) {
	this.icon = icon;
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

    public String getDisplayModifyDate() {
	return displayModifyDate;
    }

    public void setDisplayModifyDate(String displayModifyDate) {
	this.displayModifyDate = displayModifyDate;
    }

    public String getDisplayCreateDate() {
	return displayCreateDate;
    }

    public void setDisplayCreateDate(String displayCreateDate) {
	this.displayCreateDate = displayCreateDate;
    }

    public void setDisplaySize(String displaySize) {
	this.displaySize = displaySize;
    }

    public int getShareWithCnt() {
	return shareWithCnt;
    }

    public void setShareWithCnt(int shareWithCnt) {
	this.shareWithCnt = shareWithCnt;
    }

    public String getShareWithAll() {
	return shareWithAll;
    }

    public void setShareWithAll(String shareWithAll) {
	this.shareWithAll = shareWithAll;
    }

    public java.util.Date getShareDate() {
	return shareDate;
    }

    public void setShareDate(java.util.Date shareDate) {
	this.shareDate = shareDate;
    }

    public String getDisplayShareDate() {
	return displayShareDate;
    }

    public void setDisplayShareDate(String displayShareDate) {
	this.displayShareDate = displayShareDate;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}
