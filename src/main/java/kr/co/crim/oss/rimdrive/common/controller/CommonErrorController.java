package kr.co.crim.oss.rimdrive.common.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import kr.co.crim.oss.rimdrive.common.service.RimDriveVO;
import kr.co.crim.oss.rimdrive.common.utils.CommonUtils;
import kr.co.crim.oss.rimdrive.common.utils.LoginInfoHelper;

@Controller
@RequestMapping("/common/error")
public class CommonErrorController {
    private static final Logger logger = LoggerFactory.getLogger(CommonErrorController.class);

    @RequestMapping(value = "/fail.do")
    public String pageFail(HttpServletRequest req, HttpServletResponse res, Model model){

	try{
	    RimDriveVO rimDriveVO =  LoginInfoHelper.getRimDriveVO(req, res);
	    model.addAttribute("rimDriveVO", rimDriveVO);
	} catch (Exception e) {

	    logger.error("Excetpion", e);
	}

	String code = req.getParameter("rim_code");
	String message = req.getParameter("rim_message");

	if (StringUtils.isAllBlank(code, message)) {
	    Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(req);

	    if (flashMap != null) {
		message = (String) flashMap.get("rim_message");
		model.addAttribute("code", flashMap.get("rim_code"));
	    }
	} else {
	    model.addAttribute("code", code);
	}
	
	model.addAttribute("message", StringEscapeUtils.escapeHtml4(message));

	return "common/fail";
    }

    @RequestMapping(value = "/throwable.do")
    public String throwable(HttpServletRequest req, Model model){
	model.addAttribute("javax.servlet.error.message","예외가 발생하였습니다.");
	return getResponse(req,model);
    }

    @RequestMapping(value = "/exception.do")
    public String exception(HttpServletRequest req, Model model){
	model.addAttribute("javax.servlet.error.message","예외가 발생하였습니다.");
	return getResponse(req,model);
    }

    @RequestMapping(value = "/400.do")
    public String pageError400(HttpServletRequest req, Model model){
	logger.info("page error code 400");
	model.addAttribute("javax.servlet.error.status_code", 400);
	model.addAttribute("javax.servlet.error.message","잘못된 요청입니다.");
	return getResponse(req,model);
    }

    @RequestMapping(value = "/403.do")
    public String pageError403(HttpServletRequest req, Model model){
	logger.info("page error code 403");
	model.addAttribute("javax.servlet.error.status_code", 403);
	model.addAttribute("javax.servlet.error.message","접근이 금지되었습니다.");
	return getResponse(req,model);
    }

    @RequestMapping(value = "/404.do")
    public String pageError404(HttpServletRequest req, Model model){
	logger.info("page error code 404");
	model.addAttribute("javax.servlet.error.status_code", 404);
	model.addAttribute("javax.servlet.error.message","요청하신 페이지는 존재하지 않습니다.");
	return getResponse(req, model);
    }

    @RequestMapping(value = "/405.do")
    public String pageError405(HttpServletRequest req, Model model){
	logger.info("page error code 405");
	model.addAttribute("javax.servlet.error.status_code", 405);
	model.addAttribute("javax.servlet.error.message","요청된 메소드가 허용되지 않습니다.");
	return getResponse(req,model);
    }

    @RequestMapping(value = "/500.do")
    public String pageError500(HttpServletRequest req, Model model){
	logger.info("page error code 500");
	model.addAttribute("javax.servlet.error.status_code", 500);
	model.addAttribute("javax.servlet.error.message","서버에 오류가 발생하였습니다.");
	return getResponse(req,model);
    }

    @RequestMapping(value = "/503.do")
    public String pageError503(HttpServletRequest req, Model model){
	logger.info("page error code 503");
	model.addAttribute("javax.servlet.error.status_code", 503);
	model.addAttribute("javax.servlet.error.message","서비스를 사용할수 없습니다.");
	return getResponse(req,model);
    }

    private void pageErrorLog(HttpServletRequest req){
	logger.info("getRequestURI => {}", req.getRequestURI());
	logger.info("status_code : {} ", req.getAttribute("javax.servlet.error.status_code"));
	logger.info("exception_type {}", req.getAttribute("javax.servlet.error.exception_type"));
	logger.info("message : {}", req.getAttribute("javax.servlet.error.message"));
	logger.info("request_uri : {}", req.getAttribute("javax.servlet.error.request_uri"));
	logger.info("exception : {}", req.getAttribute("javax.servlet.error.exception"));
	logger.info("servlet_name : {}", req.getAttribute("javax.servlet.error.servlet_name"));
    }

    private String getResponse(HttpServletRequest req, Model model) {
	pageErrorLog(req);

	String reqURI = "";

	if (req.getAttribute("javax.servlet.error.request_uri") == null) {
	    reqURI = req.getRequestURI();
	} else {
	    reqURI = req.getAttribute("javax.servlet.error.request_uri").toString();
	}
	
	Map<String, Object> map = new HashMap<String, Object>();
	model.addAttribute("status", "fail");
	model.addAttribute("status_code", req.getAttribute("javax.servlet.error.status_code"));
	model.addAttribute("exception_type", req.getAttribute("javax.servlet.error.exception_type"));
	model.addAttribute("request_uri", req.getAttribute("javax.servlet.error.request_uri") == null ? "" : req.getAttribute("javax.servlet.error.request_uri"));
	model.addAttribute("exception", req.getAttribute("javax.servlet.error.exception"));
	model.addAttribute("servlet_name", req.getAttribute("javax.servlet.error.servlet_name"));
	model.addAttribute("error_time", CommonUtils.getDate("yyyyMMddHHmmss "));
	
	if (reqURI.contains(".do") || reqURI.contains(".ros")) {
	    return "common/error";
	} else {
	    
	    return "common/error";
	}
    }
    
}
