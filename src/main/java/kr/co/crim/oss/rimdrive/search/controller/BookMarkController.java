package kr.co.crim.oss.rimdrive.search.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import kr.co.crim.oss.rimdrive.files.service.FileListVO;
import kr.co.crim.oss.rimdrive.files.service.FolderInfoVO;
import kr.co.crim.oss.rimdrive.search.service.BookMarkService;

@Controller
@RequestMapping(value = "/bm")
public class BookMarkController {

    @Resource(name = "bookMarkService")
    private BookMarkService bookMarkService;

    @RequestMapping(value = "/list.ros")
    public String getList(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId 	= LoginInfoHelper.getUserId();
	String storageId= req.getParameter("rim_sid");

	ReturnVO returnVO = new ReturnVO();
	if ( StringUtils.isBlank(storageId) ) {
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));
	}
	else{

	    FolderInfoVO folderInfo = new FolderInfoVO();
	    folderInfo.setStorageId(storageId);
	    folderInfo.setPermissions(Constant.AUTH_TYPE_WRITE);

	    List<?> resultList = bookMarkService.getListByUserId(storageId, userId, CommonUtils.getPagingParameter(req));

	    int  totalSize = resultList.size();

	    Map<String, Object> returnDataMap = new HashMap<String, Object>();
	    returnDataMap.put("folderInfo", folderInfo);
	    returnDataMap.put("files", resultList);
	    returnDataMap.put("totalSize", totalSize);

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));

	    model.addAttribute("data", returnDataMap);
	    model.addAttribute("status", returnVO);

	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/info.ros")
    public String getInfo(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();

	String storageId = req.getParameter("rim_sid");
	String strFileId = req.getParameter("rim_fid");
	long fileId = CommonUtils.parseLong(strFileId);

	ReturnVO returnVO = new ReturnVO();

	if ( StringUtils.isBlank(storageId) || fileId < 1 ) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    FileListVO fileListVO = bookMarkService.getInfoByFileId(storageId, userId, fileId);
	    if (fileListVO != null) {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
		model.addAttribute("data", fileListVO);

	    } else {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));

	    }
	}

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/add.ros")
    public String add(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId 	= LoginInfoHelper.getUserId();
	String storageId= req.getParameter("rim_sid");
	String strFileId = req.getParameter("rim_fid");
	long fileId = CommonUtils.parseLong(strFileId);

	ReturnVO returnVO = new ReturnVO();
	if (StringUtils.isBlank(storageId)  || fileId < 1 ) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	}
	else{

	    if (bookMarkService.insertBookMark(storageId,userId, fileId) > 0) {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));

	    }else{

		 returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));
	    }
	}

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/delete.ros")
    public String delete(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId 	= LoginInfoHelper.getUserId();
	String storageId= req.getParameter("rim_sid");
	String strFileId= req.getParameter("rim_fid");
	long fileId = CommonUtils.parseLong(strFileId);

	ReturnVO returnVO = new ReturnVO();
	if (StringUtils.isBlank(storageId) || fileId < 1 ) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));
	}
	else{

	    if (bookMarkService.deleteBookMark(storageId,userId, fileId) > 0) {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));

	    }else{

		 returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));
	    }
	}

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }
}

