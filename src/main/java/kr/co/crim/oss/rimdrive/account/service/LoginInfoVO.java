package kr.co.crim.oss.rimdrive.account.service;

import java.io.Serializable;

public class LoginInfoVO implements Serializable {

    private static final long serialVersionUID = -1808768149688844957L;

    private String loginid;
    private String sitecd;
    private String status;
    private String message;
    
    
    public String getLoginid() {
        return loginid;
    }
    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }
    
    public String getSitecd() {
        return sitecd;
    }
    public void setSitecd(String sitecd) {
        this.sitecd = sitecd;
    }
    
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
}
