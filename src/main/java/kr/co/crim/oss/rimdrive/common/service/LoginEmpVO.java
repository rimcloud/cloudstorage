package kr.co.crim.oss.rimdrive.common.service;

import java.io.Serializable;

public class LoginEmpVO implements Serializable {

    private String empId;
    private String loginId;
    private String userNm;
    private String deptCd;
    private String grade;
    private String cloudAllowYn;
    private String deptNm;
    private String whleDeptCd;
    private String whleDeptNm;

    public String getEmpId() {
        return empId;
    }
    public void setEmpId(String empId) {
        this.empId = empId;
    }
    public String getLoginId() {
        return loginId;
    }
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
    public String getUserNm() {
        return userNm;
    }
    public void setUserNm(String userNm) {
        this.userNm = userNm;
    }
    public String getDeptCd() {
        return deptCd;
    }
    public void setDeptCd(String deptCd) {
        this.deptCd = deptCd;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getCloudAllowYn() {
        return cloudAllowYn;
    }
    public void setCloudAllowYn(String cloudAllowYn) {
        this.cloudAllowYn = cloudAllowYn;
    }
    public String getDeptNm() {
        return deptNm;
    }
    public void setDeptNm(String deptNm) {
        this.deptNm = deptNm;
    }
    public String getWhleDeptCd() {
        return whleDeptCd;
    }
    public void setWhleDeptCd(String whleDeptCd) {
        this.whleDeptCd = whleDeptCd;
    }
    public String getWhleDeptNm() {
        return whleDeptNm;
    }
    public void setWhleDeptNm(String whleDeptNm) {
        this.whleDeptNm = whleDeptNm;
    }

}