package kr.co.crim.oss.rimdrive.share.controller;

import java.net.URLEncoder;
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
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.crim.oss.rimdrive.common.service.ReturnVO;
import kr.co.crim.oss.rimdrive.common.utils.CommonUtils;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.common.utils.LoginInfoHelper;
import kr.co.crim.oss.rimdrive.common.utils.MessageSourceHelper;
import kr.co.crim.oss.rimdrive.files.service.FilesService;
import kr.co.crim.oss.rimdrive.files.service.FilesValidatorService;
import kr.co.crim.oss.rimdrive.files.service.FolderInfoVO;
import kr.co.crim.oss.rimdrive.share.service.ShareVO;
import kr.co.crim.oss.rimdrive.share.service.SharingLinksListVO;
import kr.co.crim.oss.rimdrive.share.service.SharingLinksService;

@Controller
@RequestMapping(value = "/sl", method = { RequestMethod.GET, RequestMethod.POST })
public class SharingLinksController {

    @Resource(name = "sharingLinksService")
    private SharingLinksService sharingLinksService;

    @Resource(name = "filesService")
    private FilesService filesService;

    @Resource(name = "filesValidatorService")
    private FilesValidatorService filesValidatorService;

    @RequestMapping(value = "/list.ros")
    public String getList(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();

	String storageId = req.getParameter("rim_sid");

	ReturnVO returnVO = new ReturnVO();

	if ( StringUtils.isAnyBlank(storageId) ) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));
	}
	else{

	    FolderInfoVO folderInfo = new FolderInfoVO();
	    folderInfo.setStorageId(storageId);
	    folderInfo.setPermissions(Constant.AUTH_TYPE_WRITE);

	    List<?> resultList = sharingLinksService.getListByStorageId(storageId, userId, CommonUtils.getPagingParameter(req));

	    int  totalSize = resultList.size();

	    Map<String, Object> returnDataMap = new HashMap<String, Object>();
	    returnDataMap.put("folderInfo",folderInfo );
	    returnDataMap.put("files", resultList );
	    returnDataMap.put("totalSize", totalSize );

	    model.addAttribute("data", returnDataMap);
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg") );

	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/fileinfo.ros")
    public String fileInfo(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();

	String storageId = req.getParameter("rim_sid");
	String strFileId = req.getParameter("rim_fid");

	ReturnVO returnVO = new ReturnVO();

	if ( StringUtils.isAnyBlank(storageId,strFileId) ) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    long fileId = CommonUtils.parseLong(strFileId);

	    SharingLinksListVO sharingLinksListVO = sharingLinksService.getInfoByFileId(storageId, userId, fileId);
	    if (sharingLinksListVO != null) {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
		model.addAttribute("data", sharingLinksListVO);

	    } else {
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));
	    }
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/info.ros")
    public String getShare(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();

	String storageId = req.getParameter("rim_sid");
	String strFileId = req.getParameter("rim_fid");

	ReturnVO returnVO = new ReturnVO();

	if ( StringUtils.isAnyBlank(storageId, strFileId) ) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	}
	else{

	    long fileId = CommonUtils.parseLong(strFileId);

	    ShareVO shareVO = sharingLinksService.getShare(storageId,fileId,userId);
	    model.addAttribute("data", shareVO);
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg") );
	}

	model.addAttribute("status", returnVO);

	return "pageJsonView";
    }

    @RequestMapping(value = "/add.ros")
    public String addShareInfo(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();

	String targetStorageId = req.getParameter("rim_sid");
	String strFileId = req.getParameter("rim_fid");

	ReturnVO returnVO = new ReturnVO();

	if ( StringUtils.isAnyBlank(targetStorageId, strFileId) ) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else{

	    long fileId = CommonUtils.parseLong(strFileId);

	    if( sharingLinksService.addShare(targetStorageId,fileId,userId) > 0 ){

		ShareVO shareVO = sharingLinksService.getShare(targetStorageId,fileId,userId);
		model.addAttribute("data", shareVO);
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg") );

	    } else {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg") );

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

	} else{

	    long shareId = CommonUtils.parseLong(strShareId);

	    if( sharingLinksService.deleteShare(userId,shareId) > 0 ){

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg") );

	    } else {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg") );
	    }
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/guest/downloadtoken.ros")
    public String downloadToken(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String token = req.getParameter("rim_token");
	String message = MessageSourceHelper.getMessage("common.fail.msg");

	if (StringUtils.isBlank(token)) {

	    message = MessageSourceHelper.getMessage("common.required.msg", new String[] { "token" });

	} else {

	    ShareVO shareInfo = sharingLinksService.getShare(token);

	    if (shareInfo != null) {

		String storageId = shareInfo.getStorageId();
		long fileId = shareInfo.getFileId();
		String userId = shareInfo.getCreateUid();

		long[] fileIdData = { fileId };

		return filesService.streamDownload(model, storageId, userId, fileIdData);
	    }
	}

	return String.format("redirect:%s?code=%s&message=%s", Constant.FAIL_API, Constant.COMMOM_RESULT_FAIL, URLEncoder.encode(message, "UTF-8"));
    }
    
}


