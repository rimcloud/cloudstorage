package kr.co.crim.oss.rimdrive.common.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import kr.co.crim.oss.rimdrive.common.service.LoginInfoStoreService;
import kr.co.crim.oss.rimdrive.common.service.SessionVO;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.common.utils.LoginInfoHelper;
import kr.co.crim.oss.rimdrive.common.utils.PropertyConfigurerHelper;

public class LoginInfoStoreServiceSessionImpl implements LoginInfoStoreService {

    private static final Logger logger = LoggerFactory.getLogger(LoginInfoStoreServiceSessionImpl.class);

    @Override
    public SessionVO getLoginInfo() throws Exception {

	if (!this.isAuthenticated()) {
	    return null;
	}
	return (SessionVO) RequestContextHolder.getRequestAttributes().getAttribute("sessionVO", RequestAttributes.SCOPE_SESSION);

    }

    @Override
    public Boolean setLoginInfo(SessionVO vo) throws Exception {
	boolean returnValue = false;

	try {
	    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	    HttpSession session = request.getSession();

	    clearLoginInfo();

	    String isAccessByLoginToken = (String) request.getAttribute("isAccessByLoginToken");
	    if(StringUtils.isBlank(isAccessByLoginToken) || !StringUtils.equals(Constant.COMMOM_YES, isAccessByLoginToken)){
		isAccessByLoginToken = Constant.COMMOM_NO;
	    } 
	    vo.setIsAccessByLoginToken(isAccessByLoginToken);
	    
	    session.setMaxInactiveInterval(PropertyConfigurerHelper.getMaxInactiveInterval());
	    session.setAttribute("sessionVO", vo);
	    
	    returnValue = true;
	    
	} catch (Exception e) {
	    returnValue = false;
	}
	return returnValue;
    }

    @Override
    public Boolean isAuthenticated() throws Exception {

	boolean returnValue = true;

	if (RequestContextHolder.getRequestAttributes() == null) {
	    return false;
	}
	if (RequestContextHolder.getRequestAttributes().getAttribute("sessionVO", RequestAttributes.SCOPE_SESSION) == null) {
	    returnValue = false;
	} else {
	    returnValue = true;
	}
	return returnValue;
    }

    @Override
    public Boolean clearLoginInfo() throws Exception {
	boolean returnValue = true;

	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	HttpSession session = request.getSession(false);

	if (session != null) {
	    String userId = LoginInfoHelper.getUserId();
	    session.removeAttribute("sessionVO");
	}

	return returnValue;
    }

}
