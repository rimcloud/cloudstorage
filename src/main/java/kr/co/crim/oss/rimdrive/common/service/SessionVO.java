package kr.co.crim.oss.rimdrive.common.service;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SessionVO implements Serializable {

    private static final long serialVersionUID = -9050661062251419215L;

    private String userId;
    private String storageId;
    private String userNm;
    private String authId;
    private String deptCd;
    private String ssoToken;
    private String checkDate;
    private String remoteAddr;
    private String winclientUseAbleYn;
    private String isAccessByLoginToken;
    private String passwdStatus;

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getStorageId() {
	return storageId;
    }

    public void setStorageId(String storageId) {
	this.storageId = storageId;
    }

    public String getUserNm() {
	return userNm;
    }

    public void setUserNm(String userNm) {
	this.userNm = userNm;
    }

    public String getAuthId() {
	return authId;
    }

    public void setAuthId(String authId) {
	this.authId = authId;
    }

    public String getDeptCd() {
	return deptCd;
    }

    public void setDeptCd(String deptCd) {
	this.deptCd = deptCd;
    }

    public String getCheckDate() {
	return checkDate;
    }

    public void setCheckDate(String checkDate) {
	this.checkDate = checkDate;
    }

    public String getSsoToken() {
        return ssoToken;
    }

    public void setSsoToken(String ssoToken) {
        this.ssoToken = ssoToken;
    }


    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getWinclientUseAbleYn() {
        return winclientUseAbleYn;
    }

    public void setWinclientUseAbleYn(String winclientUseAbleYn) {
        this.winclientUseAbleYn = winclientUseAbleYn;
    }

    public String getIsAccessByLoginToken() {
        return isAccessByLoginToken;
    }

    public void setIsAccessByLoginToken(String isAccessByLoginToken) {
        this.isAccessByLoginToken = isAccessByLoginToken;
    }

    public String getPasswdStatus() {
        return passwdStatus;
    }

    public void setPasswdStatus(String passwdStatus) {
        this.passwdStatus = passwdStatus;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}
