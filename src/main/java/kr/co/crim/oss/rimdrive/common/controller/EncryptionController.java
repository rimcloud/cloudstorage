package kr.co.crim.oss.rimdrive.common.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.crim.oss.rimdrive.common.service.ReturnVO;
import kr.co.crim.oss.rimdrive.common.utils.CommonUtils;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.common.utils.MessageSourceHelper;

@Controller
@RequestMapping(value = "/en")
public class EncryptionController {

    @RequestMapping(value = "/en.ros")
    public String doEncrypt(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	Map<String, Object> reqMap = CommonUtils.convertReqParamMapToMap(req);

	String encryptString = CommonUtils.set3DESParam(reqMap);

	ReturnVO returnVO = new ReturnVO();
	returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	model.addAttribute("data", encryptString);
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/de.ros")
    public String doDecrypt(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String token = req.getParameter("rim_token");

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isBlank(token)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {
	    Map<String, Object> returnDataMap = CommonUtils.get3DESParam(token);

	    model.addAttribute("data", returnDataMap);
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	}

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }
}
