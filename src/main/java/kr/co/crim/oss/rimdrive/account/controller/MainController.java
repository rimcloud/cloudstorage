package kr.co.crim.oss.rimdrive.account.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.crim.oss.rimdrive.common.service.RimDriveVO;
import kr.co.crim.oss.rimdrive.common.utils.LoginInfoHelper;
import kr.co.crim.oss.rimdrive.common.utils.PropertyConfigurerHelper;

@Controller
public class MainController {

    @RequestMapping(value = { "/", "/index.do" })
    public String actionIndex(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	RimDriveVO rimDriveVO =  LoginInfoHelper.getRimDriveVO(req, res);
	model.addAttribute("rimDriveVO", rimDriveVO);

	Boolean isAuthenticated = LoginInfoHelper.isAuthenticated();
	if (isAuthenticated) {
	    return "redirect:/main.do";
	}

	model.addAttribute("footer", PropertyConfigurerHelper.getLoginFooter());
	return "account/login";

    }

    @RequestMapping(value = "/main.do")
    public String actionMain(HttpServletRequest req, HttpServletResponse res,ModelMap model) throws Exception {

	RimDriveVO rimDriveVO =  LoginInfoHelper.getRimDriveVO(req, res);
	model.addAttribute("rimDriveVO", rimDriveVO);

	Boolean isAuthenticated = LoginInfoHelper.isAuthenticated();

	if (!isAuthenticated) {
	    return "redirect:/index.do";
	}
	return "redirect:/page/files.do";
    }

}
