package kr.co.crim.oss.rimdrive.common.utils.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.crim.oss.rimdrive.common.utils.LoginInfoHelper;
import kr.co.crim.oss.rimdrive.common.utils.MessageSourceHelper;

public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

    private static final String indexUri = "/index.do";
    private static final String NoLoginURIList[] = { "/error", "/webjars", "/resources", "/guest", "/external", "/account/login" , "/api" , "/rimdrive_exploer" };

    private boolean isNoLoginURI(String reqURI) {
	boolean checkURI = false;
	for (int i = 0; i < NoLoginURIList.length; i++) {
	    if (reqURI.contains(NoLoginURIList[i])) {
		checkURI = true;
		break;
	    }
	}
	if (reqURI.contains(indexUri)) {
	    checkURI = true;
	}

	return checkURI;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	    throws Exception {

	String userAgent = request.getHeader("User-Agent");

	if (StringUtils.isNotBlank(userAgent)) {

	    String [] checkAgents = {"Microsoft-WebDAV", "DavClnt" };

	    for (String agent : checkAgents) {
		if (StringUtils.startsWith(userAgent, agent)) {
		    response.setStatus(response.SC_OK);
		    return true;
		}
	    }
	}

	String loginPageURL = request.getContextPath() + "/index.do";
	String reqURI   = request.getRequestURI();

	boolean isLogin = false;

	if (!isNoLoginURI(reqURI) ) {

	    isLogin = LoginInfoHelper.isAuthenticated();

	    if ( isLogin == false) {

		boolean reqestJsonCheck = checkRequestJson(request);

		if ( reqestJsonCheck ) {
		    response.sendError(response.SC_UNAUTHORIZED, MessageSourceHelper.getMessage("fail.auth.msg"));
		} else {

		    response.sendRedirect(loginPageURL);
		}
		return false;

	    }
	}
	
	return super.preHandle(request, response, handler);
    }

    private boolean checkRequestJson(HttpServletRequest request) {

	return StringUtils.lowerCase(request.getHeader("Accept")).contains("application/json");
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
	    ModelAndView modelAndView) throws Exception {

	super.postHandle(request, response, handler, modelAndView);
    }


}
