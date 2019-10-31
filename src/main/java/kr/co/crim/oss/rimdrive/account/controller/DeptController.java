package kr.co.crim.oss.rimdrive.account.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.crim.oss.rimdrive.account.service.DeptService;
import kr.co.crim.oss.rimdrive.common.service.ReturnVO;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.common.utils.MessageSourceHelper;

@Controller
@RequestMapping(value = "/account/dept")
public class DeptController {

    @Resource(name = "deptService")
    private DeptService deptService;

    @RequestMapping(value = "/list.ros")
    public String getList(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	ReturnVO returnVO = new ReturnVO();

	List<?> resultList = deptService.getDeptList();
	returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	model.addAttribute("data", resultList);

	model.addAttribute("status", returnVO);

	return "pageJsonView";
    }

    @RequestMapping(value = "/search.ros")
    public String getSearchList(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String searchId = req.getParameter("rim_search_id");
	String searchText = req.getParameter("rim_search_text");

	ReturnVO returnVO = new ReturnVO();

	List<?> deptList = deptService.getSearchList(searchId, searchText);

	returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));

	model.addAttribute("data", deptList);
	model.addAttribute("status", returnVO);

	return "pageJsonView";
    }

}
