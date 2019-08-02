package kr.co.crim.oss.rimdrive.common.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginInfoService {

    public RimDriveVO getRimDriveVO(HttpServletRequest req, HttpServletResponse res) throws Exception;
    
    public SessionVO getSessionVO() throws Exception;
    
    public Boolean isAuthenticated() throws Exception;
    
    public Boolean isAuthenticatedLocalOnly() throws Exception;

}




