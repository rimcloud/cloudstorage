package kr.co.crim.oss.rimdrive.account.service;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class EmpVO implements Serializable {

    private static final long serialVersionUID = -4219671556531475620L;

    private String empId;
    private String empNm;
    private String hlofcYn;
    private String cloudAllowYn;
    private String createTp;
    private String ldapDeptCd;
    private String ldapChgObjYn;
    private String gpkiAuthValue;
    private String loginId;
    private Date createDate;
    private String createUid;
    private Date modifyDate;
    private String modifyUid;
    private String pwdChgObjYn;
    private String grade;
    private String sortSord;
    private String deptCd;
    private String deptNm;
    private int quota;
    private String siteCd;

    public String getEmpId() {
	return empId;
    }

    public void setEmpId(String empId) {
	this.empId = empId;
    }

    public String getEmpNm() {
	return empNm;
    }

    public void setEmpNm(String empNm) {
	this.empNm = empNm;
    }

    public String getHlofcYn() {
	return hlofcYn;
    }

    public void setHlofcYn(String hlofcYn) {
	this.hlofcYn = hlofcYn;
    }

    public String getCloudAllowYn() {
	return cloudAllowYn;
    }

    public void setCloudAllowYn(String cloudAllowYn) {
	this.cloudAllowYn = cloudAllowYn;
    }

    public String getCreateTp() {
	return createTp;
    }

    public void setCreateTp(String createTp) {
	this.createTp = createTp;
    }

    public String getLdapDeptCd() {
	return ldapDeptCd;
    }

    public void setLdapDeptCd(String ldapDeptCd) {
	this.ldapDeptCd = ldapDeptCd;
    }

    public String getLdapChgObjYn() {
	return ldapChgObjYn;
    }

    public void setLdapChgObjYn(String ldapChgObjYn) {
	this.ldapChgObjYn = ldapChgObjYn;
    }

    public String getGpkiAuthValue() {
	return gpkiAuthValue;
    }

    public void setGpkiAuthValue(String gpkiAuthValue) {
	this.gpkiAuthValue = gpkiAuthValue;
    }

    public String getLoginId() {
	return loginId;
    }

    public void setLoginId(String loginId) {
	this.loginId = loginId;
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

    public String getPwdChgObjYn() {
	return pwdChgObjYn;
    }

    public void setPwdChgObjYn(String pwdChgObjYn) {
	this.pwdChgObjYn = pwdChgObjYn;
    }

    public String getGrade() {
	return grade;
    }

    public void setGrade(String grade) {
	this.grade = grade;
    }

    public String getSortSord() {
	return sortSord;
    }

    public void setSortSord(String sortSord) {
	this.sortSord = sortSord;
    }

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

    public int getQuota() {
	return quota;
    }

    public void setQuota(int quota) {
	this.quota = quota;
    }

    public String getSiteCd() {
        return siteCd;
    }

    public void setSiteCd(String siteCd) {
        this.siteCd = siteCd;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}
