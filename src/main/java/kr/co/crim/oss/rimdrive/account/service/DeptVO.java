package kr.co.crim.oss.rimdrive.account.service;

import java.io.Serializable;
import java.util.Date;

public class DeptVO implements Serializable {

    private static final long serialVersionUID = 930271292316831072L;

    private String deptCd;
    private String deptNm;
    private String uprDeptCd;
    private String whleDeptCd;
    private String deptLevel;
    private String optYn;
    private int sortSord;
    private String hasSubDept;
    private Date modifyDate;
    private String modifyUid;
    private Date createDate;
    private String createUid;

    public String getDeptCd() {
	return deptCd;
    }

    public void setDeptCd(String deptCd) {
	this.deptCd = deptCd;
    }

    public String getDeptNm() {
	return deptNm;
    }

    public void setDeptNm(String deptNm) {
	this.deptNm = deptNm;
    }

    public String getUprDeptCd() {
	return uprDeptCd;
    }

    public void setUprDeptCd(String uprDeptCd) {
	this.uprDeptCd = uprDeptCd;
    }

    public String getWhleDeptCd() {
	return whleDeptCd;
    }

    public void setWhleDeptCd(String whleDeptCd) {
	this.whleDeptCd = whleDeptCd;
    }

    public String getDeptLevel() {
	return deptLevel;
    }

    public void setDeptLevel(String deptLevel) {
	this.deptLevel = deptLevel;
    }

    public String getOptYn() {
	return optYn;
    }

    public void setOptYn(String optYn) {
	this.optYn = optYn;
    }

    public int getSortSord() {
	return sortSord;
    }

    public void setSortSord(int sortSord) {
	this.sortSord = sortSord;
    }

    public String getHasSubDept() {
        return hasSubDept;
    }

    public void setHasSubDept(String hasSubDept) {
        this.hasSubDept = hasSubDept;
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

}
