package kr.co.crim.oss.rimdrive.search.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.crim.oss.rimdrive.common.service.ReturnVO;
import kr.co.crim.oss.rimdrive.common.utils.CommonUtils;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.common.utils.LoginInfoHelper;
import kr.co.crim.oss.rimdrive.common.utils.MessageSourceHelper;
import kr.co.crim.oss.rimdrive.search.service.TagService;

@Controller
@RequestMapping(value = "/tag")
public class TagController {

    @Resource(name = "tagService")
    private TagService tagService;

    @RequestMapping(value = "/update.ros")
    public String update(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();
	String storageId = req.getParameter("rim_sid");
	String strFileId = req.getParameter("rim_fid");
	String searchTag = req.getParameter("rim_search_tag");

	ReturnVO returnVO = new ReturnVO();
	if (StringUtils.isAnyBlank(storageId,strFileId)) {
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));
	} else {
	    long fileId = CommonUtils.parseLong(strFileId);

	    if (tagService.updateTag(storageId, userId, fileId, searchTag) > 0) {
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	    } else {
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));
	    }
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }
    
}
