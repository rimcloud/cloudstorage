package kr.co.crim.oss.rimdrive.account.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.crim.oss.rimdrive.account.service.EmpService;
import kr.co.crim.oss.rimdrive.common.service.ReturnVO;
import kr.co.crim.oss.rimdrive.common.utils.CommonUtils;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.common.utils.MessageSourceHelper;

@Controller
@RequestMapping(value = "/account/emp")
public class EmpController {

    @Resource(name = "empService")
    private EmpService empService;

    @RequestMapping(value = "/list.ros")
    public String getList(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String deptCd = req.getParameter("rim_dept_cd");

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isBlank(deptCd)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    List<?> empList = empService.getListByDeptCd(deptCd, CommonUtils.getPagingParameter(req));
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));

	    model.addAttribute("data", empList);
	}

	model.addAttribute("status", returnVO);

	return "pageJsonView";
    }

    @RequestMapping(value = "/search.ros")
    public String getSearchList(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String searchId = req.getParameter("rim_search_id");
	String searchText = req.getParameter("rim_search_text");
	String searchType = req.getParameter("rim_search_type");
	
	if( StringUtils.isBlank(searchType)) {
	    searchType = "name";
	}

	ReturnVO returnVO = new ReturnVO();

	if( searchType.equals("email")) {
	    List<?> empList = empService.getSearchListByEmail(searchText);
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	    model.addAttribute("data", empList); 
	}else {
	    List<?> empList = empService.getSearchList(searchId, searchText);
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	    model.addAttribute("data", empList);
	}
	
	model.addAttribute("status", returnVO);

	return "pageJsonView";
    }

}
