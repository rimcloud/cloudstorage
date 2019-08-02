package kr.co.crim.oss.rimdrive.share.service;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ShareTargetVO {

    private long shareTargetNo;
    private long shareId;
    private String shareWithUid;
    private String permissions;
    private String targetTp;
    private String shareWithName;

    public long getShareTargetNo() {
	return shareTargetNo;
    }

    public void setShareTargetNo(long shareTargetNo) {
	this.shareTargetNo = shareTargetNo;
    }

    public long getShareId() {
	return shareId;
    }

    public void setShareId(long shareId) {
	this.shareId = shareId;
    }

    public String getShareWithUid() {
	return shareWithUid;
    }

    public void setShareWithUid(String shareWithUid) {
	this.shareWithUid = shareWithUid;
    }

    public String getPermissions() {
	return permissions;
    }

    public void setPermissions(String permissions) {
	this.permissions = permissions;
    }

    public String getTargetTp() {
	return targetTp;
    }

    public void setTargetTp(String targetTp) {
	this.targetTp = targetTp;
    }

    public String getShareWithName() {
        return shareWithName;
    }

    public void setShareWithName(String shareWithName) {
        this.shareWithName = shareWithName;
    }

    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}
