package kr.co.crim.oss.rimdrive.personalstorage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.crim.oss.rimdrive.common.service.ReturnVO;
import kr.co.crim.oss.rimdrive.common.utils.CommonUtils;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.common.utils.FileUtil;
import kr.co.crim.oss.rimdrive.common.utils.LoginInfoHelper;
import kr.co.crim.oss.rimdrive.common.utils.MessageSourceHelper;
import kr.co.crim.oss.rimdrive.files.service.FilesValidatorService;
import kr.co.crim.oss.rimdrive.files.service.PagingVO;
import kr.co.crim.oss.rimdrive.files.service.VersionService;

@Controller("PVersionController")
@RequestMapping(value = "/p/v")
public class VersionController {

    private static final Logger logger = LoggerFactory.getLogger(VersionController.class);

    @Resource(name = "versionService")
    private VersionService versionService;

    @Resource(name = "filesValidatorService")
    private FilesValidatorService filesValidatorService;

    @RequestMapping(value = "/list.ros")
    public String getFileList(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();

	String strFileId = req.getParameter("rim_fid");
	long fileId = CommonUtils.parseLong(strFileId);

	ReturnVO returnVO = new ReturnVO();

	if (fileId < 1) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    List<?> versionList = versionService.getListByFileId(storageId, userId, fileId);

	    model.addAttribute("data", versionList);
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));

	}

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }
    
    @RequestMapping(value = "/listall.ros")
    public String getFileListAll(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();
	String searchSize = req.getParameter("rim_search_size");

	PagingVO pagingVO = versionService.getListPagingByStorageId(storageId, userId, searchSize , CommonUtils.getPagingParameter(req));

	Map<String, Object> returnDataMap = new HashMap<String, Object>();
	returnDataMap.put("files", pagingVO.getResultList());
	returnDataMap.put("totalSize", pagingVO.getTotalRows());

	ReturnVO returnVO = new ReturnVO();
	returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));

	model.addAttribute("data", returnDataMap);
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/deletelist.ros")
    public String doDeleteList(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();
	String strVersionNoList = req.getParameter("rim_vno_list");
	
	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isBlank(strVersionNoList)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));
	}
	else {
	
	    long[] versionNoData = CommonUtils.convertStringToArrayTypeLong(strVersionNoList, ",", strVersionNoList);
	    
	    if (versionService.deleteList(storageId, userId, versionNoData ) > 0) {
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	    } else {
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));
	    }
	    
	}	

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }
    
    @RequestMapping(value = "/delete.ros")
    public String doDelete(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();
	String strFileId = req.getParameter("rim_fid");
	String strVersionNo = req.getParameter("rim_vno");

	long fileId = CommonUtils.parseLong(strFileId);
	long versionNo = CommonUtils.parseLong(strVersionNo);

	ReturnVO returnVO = new ReturnVO();

	if (fileId < 1 || versionNo < 1) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    if (versionService.delete(storageId, userId, versionNo, fileId) > 0) {
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	    } else {
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));
	    }

	}

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/clear.ros")
    public String doClear(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();
	String count = req.getParameter("rim_count");
	
	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isBlank(count)) {
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));
	}else {
	    versionService.clear(storageId, userId , count );
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	}	
	
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }
    
    @RequestMapping(value = "/deleteAll.ros")
    public String doDeleteAll(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();
	String strFileId = req.getParameter("rim_fid");
	long fileId = CommonUtils.parseLong(strFileId);

	ReturnVO returnVO = new ReturnVO();

	if (fileId < 1) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    if (versionService.deleteAll(storageId, userId, fileId) > 0) {
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	    } else {
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));
	    }

	}

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/restore.ros")
    public String doReStore(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();

	String strVersionNo = req.getParameter("rim_vno");
	String strFileId = req.getParameter("rim_fid");

	long fileId = CommonUtils.parseLong(strFileId);
	long versionNo = CommonUtils.parseLong(strVersionNo);

	ReturnVO returnVO = new ReturnVO();

	if (fileId < 1 || versionNo < 1) {
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));
	} else {

	    if (versionService.reStore(storageId, userId, versionNo, fileId)) {
		List<?> versionList = versionService.getListByFileId(storageId, userId, fileId);
		model.addAttribute("data", versionList);
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	    } else {
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));
	    }
	}

	model.addAttribute("status", returnVO);

	return "pageJsonView";
    }
    
    @RequestMapping(value = "/down.ros")
    public String doDownload(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();
	
	String strFileId = req.getParameter("rim_fid");
	String strVersionNo = req.getParameter("rim_vno");

	if (! StringUtils.isAnyBlank(strFileId, strVersionNo)) {

	    long fileId = CommonUtils.parseLong(strFileId);
	    long versionNo = CommonUtils.parseLong(strVersionNo);

	    if (fileId > 0 || versionNo > 0) {

		File file = versionService.download(storageId, userId, versionNo, fileId);

		if (file != null && file.length() > 0) {
		    String fileName = FileUtil.stripTimeStamp(file.getName());
		    model.put("fileName", fileName);
		    model.put("downloadFile", file);

		    return "fileDownLoadView";
		}
	    }
	}

	return "";
    }
    
}
