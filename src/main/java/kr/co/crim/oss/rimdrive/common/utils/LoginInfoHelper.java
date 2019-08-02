package kr.co.crim.oss.rimdrive.common.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import kr.co.crim.oss.rimdrive.common.service.LoginInfoService;
import kr.co.crim.oss.rimdrive.common.service.RimDriveVO;
import kr.co.crim.oss.rimdrive.common.service.SessionVO;

public class LoginInfoHelper {

    private static LoginInfoService loginInfoService;

    public LoginInfoService getLoginInfoService() {
	return loginInfoService;
    }

    public void setLoginInfoService(LoginInfoService loginInfoService) {
	LoginInfoHelper.loginInfoService = loginInfoService;
    }

    public static RimDriveVO getRimDriveVO(HttpServletRequest req, HttpServletResponse res) throws Exception{
	return loginInfoService.getRimDriveVO(req, res);
    }

    public static Boolean isAuthenticated() throws Exception {
	Boolean returnValue = loginInfoService.isAuthenticated();

	if (returnValue == true) {
	    if (StringUtils.isEmpty(getUserId()) || StringUtils.isEmpty(getStorageId())) {

		returnValue = false;
	    }
	}

	return returnValue;
    }
    
    public static String getUserId() throws Exception {
	return getSessionInfo("userId");
    }

    public static String getStorageId() throws Exception {
	return getSessionInfo("storageId");
    }

    public static String getUserNm() throws Exception {
	return getSessionInfo("userNm");
    }

    public static String getDeptCd() throws Exception {
	return getSessionInfo("deptCd");
    }

    private static String getSessionInfo(String item) throws Exception {
	String returnValue = "";
	try {
	    SessionVO vo = loginInfoService.getSessionVO();

	    if ( vo != null){
		switch (item) {
		case "userId":
		    returnValue = vo.getUserId();
		    break;
		case "authId":
		    returnValue = vo.getAuthId();
		    break;
		case "storageId":
		    returnValue = vo.getStorageId();
		    break;
		case "userNm":
		    returnValue = vo.getUserNm();
		    break;
		case "deptCd":
		    returnValue = vo.getDeptCd();
		    break;
		case "checkDate":
		    returnValue = vo.getCheckDate();
		    break;
		case "ssoToken":
		    returnValue = vo.getSsoToken();
		    break;
		case "remoteAddr":
		    returnValue = vo.getRemoteAddr();
		    break;
		default:
		    returnValue = "";
		    break;
		}
	    }
	} catch ( Exception e){
	    returnValue = "";
	}

	return returnValue;
    }
}
