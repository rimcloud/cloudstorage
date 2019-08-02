package kr.co.crim.oss.rimdrive.common.controller;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.crim.oss.rimdrive.files.service.StorageService;

@Controller
public class CommonController {

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
    
    @Resource(name = "storageService")
    private StorageService storageService;
    
    @RequestMapping(value = "/guest/setChangeLocaleByCookie.do")
    public String setChangeLocaleByCookie(@RequestParam(required = false) String locale, ModelMap map,
	    HttpServletRequest request, HttpServletResponse response) throws Exception{
	
 	Cookie cookie = new Cookie("rimdrive.locale", locale);
	cookie.setMaxAge(60 * 60 * 24 * 30);
	cookie.setPath("/");
	response.addCookie(cookie);
	
	return "redirect:/";
    }
  
}




