package kr.co.crim.oss.rimdrive.files.service;

import org.apache.commons.lang3.builder.ToStringBuilder;

import kr.co.crim.oss.rimdrive.common.utils.CommonUtils;

public class FolderSubInfoVO {

    private long size = 0;
    private String displaySize;
    private int subDirCount = 0;
    private int subFileCount = 0;

    public long getSize() {
	return size;
    }

    public void setSize(long size) {
	this.size = size;
	this.displaySize = CommonUtils.readableFileSize(size);
    }

    public String getDisplaySize() {
	return displaySize == null ? CommonUtils.readableFileSize(this.size) : displaySize;
    }

    public int getSubDirCount() {
	return subDirCount;
    }

    public void setSubDirCount(int subDirCount) {
	this.subDirCount = subDirCount;
    }

    public int getSubFileCount() {
	return subFileCount;
    }

    public void setSubFileCount(int subFileCount) {
	this.subFileCount = subFileCount;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}
