package kr.co.crim.oss.rimdrive.account.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import kr.co.crim.oss.rimdrive.account.service.EmpService;
import kr.co.crim.oss.rimdrive.account.service.LoginInfoVO;
import kr.co.crim.oss.rimdrive.account.service.LoginService;
import kr.co.crim.oss.rimdrive.account.service.LoginVO;
import kr.co.crim.oss.rimdrive.common.service.LoginInfoStoreService;
import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.service.SessionVO;
import kr.co.crim.oss.rimdrive.common.utils.MessageSourceHelper;
import kr.co.crim.oss.rimdrive.files.service.FilesService;
import kr.co.crim.oss.rimdrive.files.service.FilesValidatorService;
import kr.co.crim.oss.rimdrive.files.service.impl.StorageDAO;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Resource(name = "loginInfoStoreService")
    private LoginInfoStoreService loginInfoStoreService;

    @Resource(name = "loginDAO")
    private LoginDAO loginDAO;

    @Resource(name = "empService")
    private EmpService empService;

    @Resource(name = "empDAO")
    private EmpDAO empDAO;

    @Resource(name = "deptDAO")
    private DeptDAO deptDAO;

    @Resource(name = "storageDAO")
    private StorageDAO storageDAO;

    @Resource(name = "filesService")
    private FilesService filesService;

    @Resource(name = "filesValidatorService")
    private FilesValidatorService filesValidatorService;

    @Override
    public boolean checkLoginForWebDav(String userId, String password, String siteCd) throws Exception {
	String ip = "";
	String mac = "";
	return checkLogin(userId, password, siteCd, ip, mac);
    }

    @Override
    public boolean checkLogin(String userId, String password, String siteCd) throws Exception {
	String ip = "";
	String mac = "";
	return checkLogin(userId, password, siteCd, ip, mac);

    }

    @Override
    public boolean checkLogin(String userId, String password, String siteCd, String ip, String mac) throws Exception {
	boolean isAuthenticated = false;

	isAuthenticated = checkLoginLocal(userId, password, siteCd, ip, mac);

	return isAuthenticated;

    }

    @Override
    public String checkLoginGetUserId(String userId, String password, String siteCd, String ip, String mac) throws Exception {
	String loginUserId = "";

	loginUserId = checkLoginLocalGetUserId(userId, password, siteCd, ip, mac);

	return loginUserId;
    }

    @Override
    public LoginInfoVO checkLoginGetLoginInfo(String userId, String password, String siteCd, String ip, String mac) throws Exception {

	LoginInfoVO loginInfo = null;

	loginInfo = new LoginInfoVO();

	if (getUserLoginId(userId, siteCd) != null) {

	    String loginUserId = checkLoginLocalGetUserId(userId, password, siteCd, ip, mac);

	    if (StringUtils.isNoneBlank(loginUserId)) {
		loginInfo.setStatus("true");
		loginInfo.setLoginid(loginUserId);
	    } else {
		loginInfo.setStatus("false");
		loginInfo.setMessage(MessageSourceHelper.getMessage("fail.invaild.passwd"));
	    }
	} else {
	    loginInfo.setStatus("false");
	    loginInfo.setMessage(MessageSourceHelper.getMessage("fail.exist.not.emp"));
	}

	return loginInfo;
    }

    private boolean checkLoginLocal(String userId, String password, String siteCd, String ip, String mac) throws Exception {

	HashMap<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("userId", userId);
	paramMap.put("password", password);
	paramMap.put("siteCd", siteCd);

	LoginVO loginVO = loginDAO.selectCheckLogin(new ParamDaoVO(paramMap));
	
	if (loginVO == null || loginVO.getUserId().equals("")) {
	    return false;
	}

	return setLogin(loginVO.getUserId());

    }

    private String checkLoginLocalGetUserId(String userId, String password, String siteCd, String ip, String mac) throws Exception {

	String returnValue = "";
	HashMap<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("userId", userId);
	paramMap.put("password", password);
	paramMap.put("siteCd", siteCd);

	LoginVO loginVO = loginDAO.selectCheckLogin(new ParamDaoVO(paramMap));
	if (loginVO == null || loginVO.getUserId().equals("")) {

	} else {
	    returnValue = loginVO.getUserId();
	}

	return returnValue;

    }

    @Override
    public boolean setLogin(String userId) throws Exception {
	String ssoToken = userId;
	return setLogin(userId, ssoToken);
    }

    @Override
    public boolean setLogin(String userId, String ssoToken) throws Exception {
	
	empService.addEmpOnlyStorage("amin", userId, userId);

	SessionVO sessionVO = getUserLoginInfo(userId);

	if (StringUtils.isBlank(sessionVO.getStorageId())) {
	    return false;
	}

	Date now = new Date();

	sessionVO.setCheckDate(now.toString());
	sessionVO.setSsoToken(ssoToken);

	loginInfoStoreService.setLoginInfo(sessionVO);

	return true;
    }

    @Override
    public void setLogout(HttpServletRequest req) throws Exception {

	loginInfoStoreService.clearLoginInfo();

    }

    @Override
    public SessionVO getUserLoginInfo(String userId) throws Exception {
	return loginDAO.selectUserLoginInfo(new ParamDaoVO("userId", userId));
    }

    @Override
    public SessionVO getUserLoginId(String userId, String siteCd) throws Exception {

	HashMap<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("userId", userId);
	
	return loginDAO.selectUserLoginId(new ParamDaoVO(paramMap));
    }

    @Override
    public List<?> getListByEmpNm(String empNm) throws Exception {

	return loginDAO.selectEmpLoginInfo(new ParamDaoVO("empNm", empNm));
    }

}
