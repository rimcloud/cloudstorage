package kr.co.crim.oss.rimdrive.account.service;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class LoginVO implements Serializable {

    private static final long serialVersionUID = -1808768149688844957L;

    private String userId;
    private String displayNm;
    private String passwdStatus;

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getDisplayNm() {
	return displayNm;
    }

    public void setDisplayNm(String displayNm) {
	this.displayNm = displayNm;
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
