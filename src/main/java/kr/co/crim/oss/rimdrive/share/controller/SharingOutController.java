package kr.co.crim.oss.rimdrive.share.controller;

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
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.crim.oss.rimdrive.common.service.ReturnVO;
import kr.co.crim.oss.rimdrive.common.utils.CommonUtils;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.common.utils.LoginInfoHelper;
import kr.co.crim.oss.rimdrive.common.utils.MessageSourceHelper;
import kr.co.crim.oss.rimdrive.files.service.FileListVO;
import kr.co.crim.oss.rimdrive.files.service.FilesService;
import kr.co.crim.oss.rimdrive.files.service.FolderInfoVO;
import kr.co.crim.oss.rimdrive.share.service.ShareTargetVO;
import kr.co.crim.oss.rimdrive.share.service.ShareVO;
import kr.co.crim.oss.rimdrive.share.service.SharingOutListVO;
import kr.co.crim.oss.rimdrive.share.service.SharingOutService;

@Controller
@RequestMapping(value = "/so", method = { RequestMethod.GET, RequestMethod.POST })
public class SharingOutController {

    private static final Logger logger = LoggerFactory.getLogger(SharingOutController.class);

    @Resource(name = "filesService")
    private FilesService filesService;

    @Resource(name = "sharingOutService")
    private SharingOutService sharingOutService;

    @RequestMapping(value = "/list.ros")
    public String getList(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();
	String storageId = req.getParameter("rim_sid");

	ReturnVO returnVO = new ReturnVO();
	if (StringUtils.isEmpty(storageId)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    FolderInfoVO folderInfo = new FolderInfoVO();
	    folderInfo.setStorageId(storageId);
	    folderInfo.setPermissions(Constant.AUTH_TYPE_WRITE);

	    List<?> resultList =  sharingOutService.getListByStorageId(storageId, userId, CommonUtils.getPagingParameter(req));

	    int totalSize = resultList.size();

	    Map<String, Object> returnDataMap = new HashMap<String, Object>();
	    returnDataMap.put("folderInfo", folderInfo);
	    returnDataMap.put("files", resultList);
	    returnDataMap.put("totalSize", totalSize);

	    model.addAttribute("data", returnDataMap);
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/files/info.ros")
    public String fileInfo(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();

	String storageId = req.getParameter("rim_sid");
	String strFileId = req.getParameter("rim_fid");

	ReturnVO returnVO = new ReturnVO();

	if ( StringUtils.isAnyBlank(storageId,strFileId) ) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    long fileId = CommonUtils.parseLong(strFileId);

	    SharingOutListVO sharingOutListVO = sharingOutService.getInfoByFileId(storageId, userId, fileId);
	    if (sharingOutListVO != null) {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
		model.addAttribute("data", sharingOutListVO);

	    } else {
		FileListVO fileInfo = filesService.getInfoIncludeBookMarkByFileId(storageId, fileId, userId);

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
		model.addAttribute("data", fileInfo);
	    }
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/shareinfo.ros")
    public String getShareInfo(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();

	String storageId = req.getParameter("rim_sid");
	String strFileId = req.getParameter("rim_fid");

	ReturnVO returnVO = new ReturnVO();

	if ( StringUtils.isAnyBlank(storageId,strFileId) ) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    long fileId = CommonUtils.parseLong(strFileId);

	    Map<String, Object> returnDataMap = new HashMap<String, Object>();
	    ShareVO shareVO = sharingOutService.getShare(storageId, fileId, userId);
	    if (shareVO != null) {

		List<?> listShareTargetVO = sharingOutService.getShareTargetListByShareId(shareVO.getShareId());

		returnDataMap.put("shareVO", shareVO);
		returnDataMap.put("listShareTargetVO", listShareTargetVO);
		model.addAttribute("data", returnDataMap);

	    }

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/add.ros")
    public String addShareInfo(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();

	String targetStorageId = req.getParameter("rim_sid");
	String strFileId = req.getParameter("rim_fid");
	String accessPw = req.getParameter("rim_access_pw");
	String expireDate = req.getParameter("rim_expire_date");
	String message = req.getParameter("rim_message");
	String insertTarget = req.getParameter("rim_insert_target");

	ReturnVO returnVO = new ReturnVO();

	if ( StringUtils.isAnyBlank(targetStorageId, strFileId) ) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else if (!StringUtils.equals(LoginInfoHelper.getStorageId(), targetStorageId)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.share.add.storage.not.equals"));

	} else {

	    long fileId = CommonUtils.parseLong(strFileId);

	    ObjectMapper mapper = new ObjectMapper();
	    List<ShareTargetVO> listInsertTarget = mapper.readValue(insertTarget, new TypeReference<List<ShareTargetVO>>() {});

	    for (int i = 0; i < listInsertTarget.size(); i++) {
		if ( StringUtils.equals(userId, listInsertTarget.get(i).getShareWithUid()))
		    listInsertTarget.remove(i);
	    }

	    if(listInsertTarget.size() > 0){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("insertTarget",  listInsertTarget);

		if (sharingOutService.addShareAll(targetStorageId, fileId, userId, accessPw, expireDate, message, paramMap) > 0) {

        	    ShareVO shareVO = sharingOutService.getShare(targetStorageId, fileId, userId);
        	    Map<String, Object> returnDataMap = new HashMap<String, Object>();
        	    List<?> listShareTargetVO = sharingOutService.getShareTargetListByShareId(shareVO.getShareId());

        	    returnDataMap.put("shareVO", shareVO);
        	    returnDataMap.put("listShareTargetVO", listShareTargetVO);
        	    model.addAttribute("data", returnDataMap);
        	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
		} else {
		    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));
		}
	    } else {
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.sharing.list"));
	    }
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/delete.ros")
    public String deleteShareInfo(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();

	String strShareId   = req.getParameter("rim_share_id");
	String targetStorageId = req.getParameter("rim_sid");

	ReturnVO returnVO = new ReturnVO();

	if ( StringUtils.isAnyBlank( strShareId, targetStorageId ) ) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else if (!StringUtils.equals(LoginInfoHelper.getStorageId(), targetStorageId)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.share.add.storage.not.equals"));

	} else {

	    long shareId = CommonUtils.parseLong(strShareId);

	    if( sharingOutService.deleteShareAll(userId,shareId) > 0 ){

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg") );

	    } else {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg") );
	    }
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/update.ros")
    public String updateShareInfo(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();

	String targetStorageId = req.getParameter("rim_sid");
	String strShareId = req.getParameter("rim_share_id");

	String insertTarget = req.getParameter("rim_insert_target");
	String updateTargetR = req.getParameter("rim_update_target_r");
	String updateTargetW = req.getParameter("rim_update_target_w");
	String deleteTarget = req.getParameter("rim_delete_target");

	ReturnVO returnVO = new ReturnVO();
	if ( StringUtils.isAnyBlank( strShareId, targetStorageId ) ) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else if (!StringUtils.equals(LoginInfoHelper.getStorageId(), targetStorageId)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.share.add.storage.not.equals"));

	} else {

	    long shareId = CommonUtils.parseLong(strShareId);

	    ObjectMapper mapper = new ObjectMapper();
	    List<ShareTargetVO> listInsertTarget = mapper.readValue(insertTarget, new TypeReference<List<ShareTargetVO>>() {});
	    List<ShareTargetVO> listUpdateRTarget = mapper.readValue(updateTargetR, new TypeReference<List<ShareTargetVO>>() {});
	    List<ShareTargetVO> listUpdateWTarget = mapper.readValue(updateTargetW, new TypeReference<List<ShareTargetVO>>() {});
	    List<ShareTargetVO> listDeleteTarget = mapper.readValue(deleteTarget, new TypeReference<List<ShareTargetVO>>() {});

	    for (int i = 0; i < listInsertTarget.size(); i++) {
		if ( StringUtils.equals(userId, listInsertTarget.get(i).getShareWithUid()))
		    listInsertTarget.remove(i);
	    }
	    for (int i = 0; i < listUpdateRTarget.size(); i++) {
		if ( StringUtils.equals(userId, listUpdateRTarget.get(i).getShareWithUid()))
		    listUpdateRTarget.remove(i);
	    }
	    for (int i = 0; i < listUpdateWTarget.size(); i++) {
		if ( StringUtils.equals(userId, listUpdateWTarget.get(i).getShareWithUid()))
		    listUpdateWTarget.remove(i);
	    }

	    Map<String, Object> paramMap = new HashMap<String, Object>();
	    if (listInsertTarget.size() > 0)
		paramMap.put("insertTarget",  listInsertTarget);
	    if (listUpdateRTarget.size() > 0)
		paramMap.put("updateTargetR", listUpdateRTarget);
	    if (listUpdateWTarget.size() > 0)
		paramMap.put("updateTargetW", listUpdateWTarget);
	    if (listDeleteTarget.size() > 0)
		paramMap.put("deleteTarget",  listDeleteTarget);

	    if (!paramMap.isEmpty()) {

		if (sharingOutService.updateShareAll(userId, shareId, "", "", "", paramMap) > 0) {

		    ShareVO shareVO = sharingOutService.getShareByShareId(shareId);

		    Map<String, Object> returnDataMap = new HashMap<String, Object>();
		    List<?> listShareTargetVO = sharingOutService.getShareTargetListByShareId(shareVO.getShareId());

		    returnDataMap.put("shareVO", shareVO);
		    returnDataMap.put("listShareTargetVO", listShareTargetVO);
		    model.addAttribute("data", returnDataMap);
		    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));

		} else {
		    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));
		}
	    }
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }
    
    @RequestMapping(value = "/files/down.ros")
    public String doDownload(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();
	String fileIds = req.getParameter("rim_fids");
	String storageId = req.getParameter("rim_sid");

	if (!StringUtils.isAnyBlank(storageId, fileIds)) {

	    long[] fileIdData = CommonUtils.convertStringToArrayTypeLong(fileIds, ",", "0");
	    
	    return filesService.streamDownload(model, storageId, userId, fileIdData);
	}

	return "";
    }
    
}
