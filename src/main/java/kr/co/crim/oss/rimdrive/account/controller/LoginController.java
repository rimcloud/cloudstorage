package kr.co.crim.oss.rimdrive.account.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.crim.oss.rimdrive.account.service.LoginService;
import kr.co.crim.oss.rimdrive.common.service.ReturnVO;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.common.utils.MessageSourceHelper;

@Controller
@RequestMapping(value = "/account")
public class LoginController {

    @Resource(name = "loginService")
    private LoginService loginService;

    @RequestMapping(value = "/login/checklogin.ros")
    public String apiGuestCheckLogin(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = req.getParameter("rim_uid");
	String password = req.getParameter("rim_pwd");
	
	ReturnVO returnVO = new ReturnVO();
	if (StringUtils.isAnyBlank(userId, password)) {
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));
	} else {
	    boolean checkLoginValue = loginService.checkLogin(userId, password);
	    if ( checkLoginValue ) {
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("success.login.msg"));
	    } else {
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.login.msg"));
	    }
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/logout.ros")
    public String doLogout( HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	loginService.setLogout(req);

	ReturnVO returnVO = new ReturnVO();
	returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("success.logout.msg"));

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

}
