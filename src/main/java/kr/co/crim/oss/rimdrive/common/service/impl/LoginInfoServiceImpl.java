package kr.co.crim.oss.rimdrive.common.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.crim.oss.rimdrive.common.service.LoginInfoService;
import kr.co.crim.oss.rimdrive.common.service.LoginInfoStoreService;
import kr.co.crim.oss.rimdrive.common.service.RimDriveVO;
import kr.co.crim.oss.rimdrive.common.service.SessionVO;
import kr.co.crim.oss.rimdrive.common.utils.PropertyConfigurerHelper;

public class LoginInfoServiceImpl implements LoginInfoService {

    @Resource(name = "loginInfoStoreService")
    private LoginInfoStoreService loginInfoStoreService;

    @Override
    public RimDriveVO getRimDriveVO(HttpServletRequest req, HttpServletResponse res) throws Exception{

	RimDriveVO rimDriveVO = new RimDriveVO();
	rimDriveVO.setLogoText(PropertyConfigurerHelper.getLogoText());
	rimDriveVO.setLogoImg(PropertyConfigurerHelper.getLogoPath());

	return rimDriveVO;
    }

    @Override
    public SessionVO getSessionVO() throws Exception{

	if (!loginInfoStoreService.isAuthenticated()){
	    return null;
	}
	return loginInfoStoreService.getLoginInfo();

    }

    @Override
    public Boolean isAuthenticated() throws Exception {
	boolean isAuthenticated = false;

	isAuthenticated = loginInfoStoreService.isAuthenticated();

	return isAuthenticated;
    }

    @Override
    public Boolean isAuthenticatedLocalOnly() throws Exception {
	boolean isAuthenticated = false;

	isAuthenticated = loginInfoStoreService.isAuthenticated();

	return isAuthenticated;
    }

}




