package kr.co.crim.oss.rimdrive.account.service;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import kr.co.crim.oss.rimdrive.common.service.SessionVO;

public interface LoginService {

    public boolean checkLoginForWebDav(String userId, String password) throws Exception;

    public boolean checkLogin(String userId, String password) throws Exception;

    public boolean checkLogin(String userId, String password, String ip, String mac) throws Exception;

    public String checkLoginGetUserId(String userId, String password, String ip, String mac) throws Exception;

    public LoginInfoVO checkLoginGetLoginInfo(String userId, String password, String ip, String mac) throws Exception;
    
    public boolean setLogin(String userId) throws Exception ;

    public boolean setLogin(String userId, String ssoToken) throws Exception ;

    public void setLogout(HttpServletRequest req) throws Exception;

    public SessionVO getUserLoginInfo(String userId) throws Exception;
    
    public SessionVO getUserLoginId(String userId) throws Exception;

    public List<?> getListByEmpNm(String empNm) throws Exception;
}
