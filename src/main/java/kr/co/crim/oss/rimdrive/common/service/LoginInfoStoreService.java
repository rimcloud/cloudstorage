package kr.co.crim.oss.rimdrive.common.service;

public interface LoginInfoStoreService {

    public SessionVO getLoginInfo() throws Exception;
 
    public Boolean setLoginInfo(SessionVO vo) throws Exception;
    
    public Boolean isAuthenticated() throws Exception;

    public Boolean clearLoginInfo() throws Exception;
    
}




