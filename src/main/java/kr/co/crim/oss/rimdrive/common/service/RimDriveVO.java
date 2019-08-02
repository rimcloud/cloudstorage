package kr.co.crim.oss.rimdrive.common.service;

import java.io.Serializable;
import java.util.Date;

public class RimDriveVO implements Serializable {

    private static final long serialVersionUID = 1833504171003893606L;

    private String domain;
    private String logoText;
    private String logoImg;
    private String siteName;
    private Date modifyDate;
    private String modifyUid;

    public String getDomain() {
	return domain;
    }

    public void setDomain(String domain) {
	this.domain = domain;
    }

    public String getLogoText() {
	return logoText;
    }

    public void setLogoText(String logoText) {
	this.logoText = logoText;
    }

    public String getLogoImg() {
	return logoImg;
    }

    public void setLogoImg(String logoImg) {
	this.logoImg = logoImg;
    }

    public String getSiteName() {
	return siteName;
    }

    public void setSiteName(String siteName) {
	this.siteName = siteName;
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

    @Override
    public String toString() {
	return "";
    }

}
