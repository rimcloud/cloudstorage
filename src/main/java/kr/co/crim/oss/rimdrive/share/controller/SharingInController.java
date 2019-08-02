package kr.co.crim.oss.rimdrive.share.controller;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.crim.oss.rimdrive.common.service.ReturnVO;
import kr.co.crim.oss.rimdrive.common.utils.CommonUtils;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.common.utils.FileUtil;
import kr.co.crim.oss.rimdrive.common.utils.LoginInfoHelper;
import kr.co.crim.oss.rimdrive.common.utils.MessageSourceHelper;
import kr.co.crim.oss.rimdrive.common.utils.PropertyConfigurerHelper;
import kr.co.crim.oss.rimdrive.files.service.FileListVO;
import kr.co.crim.oss.rimdrive.files.service.FilesService;
import kr.co.crim.oss.rimdrive.files.service.FilesValidatorService;
import kr.co.crim.oss.rimdrive.files.service.FolderInfoVO;
import kr.co.crim.oss.rimdrive.files.service.FolderSubInfoVO;
import kr.co.crim.oss.rimdrive.files.service.StorageService;
import kr.co.crim.oss.rimdrive.share.service.SharingInService;

@Controller
@RequestMapping(value = "/si", method = { RequestMethod.GET, RequestMethod.POST })
public class SharingInController {

    @Resource(name = "sharingInService")
    private SharingInService sharingInService;

    @Resource(name = "storageService")
    private StorageService storageService;

    @Resource(name = "filesService")
    private FilesService filesService;

    @Resource(name = "filesValidatorService")
    private FilesValidatorService filesValidatorService;

    @RequestMapping(value = "/toplist.ros")
    public String getTopList(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();
	String deptCd = LoginInfoHelper.getDeptCd();
	String storageId = LoginInfoHelper.getStorageId();

	String path = req.getParameter("rim_path");

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isBlank(path)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    FolderInfoVO folderInfo = new FolderInfoVO();
	    folderInfo.setStorageId(storageId);
	    folderInfo.setPath(Constant.FILE_SEPARATOR);

	    List<?> resultList = sharingInService.getTopList(storageId, userId, deptCd, CommonUtils.getPagingParameter(req));

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

    @RequestMapping(value = "/sublist.ros")
    public String getSubList(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();
	String deptCd = LoginInfoHelper.getDeptCd();

	String sharePath = req.getParameter("rim_path");

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isAnyBlank(sharePath)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    FolderInfoVO folderInfo = sharingInService.getShareInFolderInfoBySharePath(LoginInfoHelper.getStorageId(), userId, deptCd, sharePath);

	    String originStorageId = folderInfo.getOriginStorageId();
	    String originFilePath = folderInfo.getOriginPath();

	    FolderSubInfoVO folderSubInfo = sharingInService.getFolderSubInfo(originStorageId, originFilePath);
	    List<?> resultList = sharingInService.getSubListByPath(originStorageId, userId, originFilePath, sharePath, CommonUtils.getPagingParameter(req));

	    int totalSize = resultList.size();

	    Map<String, Object> returnDataMap = new HashMap<String, Object>();
	    returnDataMap.put("folderInfo", folderInfo);
	    returnDataMap.put("folderSubInfo", folderSubInfo);
	    returnDataMap.put("files", resultList);
	    returnDataMap.put("totalSize", totalSize);

	    model.addAttribute("data", returnDataMap);
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));

	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/files/info.ros")
    public String getFileInfo(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();
	String deptCd = LoginInfoHelper.getDeptCd();

	String sharePath = req.getParameter("rim_path");
	String storageId = req.getParameter("rim_sid");
	String strFileId = req.getParameter("rim_fid");
	long fileId = CommonUtils.parseLong(strFileId);

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isAnyBlank(sharePath, storageId) || fileId < 1) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    FolderInfoVO folderInfo = sharingInService.getShareInFolderInfoBySharePath(storageId, userId, deptCd, sharePath);

	    FileListVO fileInfo = filesService.getInfoByFileId(folderInfo.getOriginStorageId(), fileId);

	    if (fileInfo != null) {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
		model.addAttribute("data", fileInfo);

	    } else {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));
	    }
	}

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/files/infobypath.ros")
    public String getInfoByPath(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();
	String deptCd = LoginInfoHelper.getDeptCd();
	String storageId = req.getParameter("rim_sid");
	String sharePath = req.getParameter("rim_path");

	ReturnVO returnVO = new ReturnVO();
	returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	if (!StringUtils.isAnyBlank(storageId, sharePath)) {

	    FolderInfoVO folderInfo = sharingInService.getShareInFolderInfoBySharePath(storageId, userId, deptCd, sharePath);

	    FileListVO fileInfo = filesService.getInfoByPath(folderInfo.getOriginStorageId(), folderInfo.getOriginPath());
	    model.addAttribute("data", fileInfo);
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/files/newfolder.ros")
    public String makeFolder(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();
	String deptCd = LoginInfoHelper.getDeptCd();

	String path = req.getParameter("rim_path");
	String folderName = req.getParameter("rim_folder_nm");

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isAnyBlank(path, folderName)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    FolderInfoVO folderInfo = sharingInService.getShareInFolderInfoBySharePath(LoginInfoHelper.getStorageId(), userId, deptCd, path);

	    String originStorageId = folderInfo.getOriginStorageId();
	    String originFilePath = folderInfo.getOriginPath();

	    folderName = FileUtil.removeTailDots(folderName);

	    FileListVO newFolderInfo = filesService.makeFolder(originStorageId, userId, originFilePath, folderName, true);

	    if (newFolderInfo != null) {
		String newSharePath = getSharePathForMergeNewFile(path, newFolderInfo.getName());
		newFolderInfo.setPath(newSharePath);

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
		model.addAttribute("data", newFolderInfo);

	    } else {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));
	    }
	}

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/files/rename.ros")
    public String setReName(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();
	String deptCd = LoginInfoHelper.getDeptCd();

	String path = req.getParameter("rim_path");
	String reName = req.getParameter("rim_re_nm");
	String strFileId = req.getParameter("rim_fid");

	long fileId = CommonUtils.parseLong(strFileId);

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isAnyBlank(strFileId, path, reName) || fileId < 1) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    FolderInfoVO folderInfo = sharingInService.getShareInFolderInfoBySharePath(LoginInfoHelper.getStorageId(), userId, deptCd, path);

	    String originStorageId = folderInfo.getOriginStorageId();
	    String originFilePath = folderInfo.getOriginPath();

	    String targetFileUri = FileUtil.getFilePath(originFilePath, reName);

	    reName = FileUtil.removeTailDots(reName);

	    FileListVO newfileInfo = filesService.setReName(originStorageId, userId, fileId, originFilePath, reName, targetFileUri);
	    if (newfileInfo != null) {

		String newSharePath = getSharePathForMergeNewFile(path, newfileInfo.getName());
		newfileInfo.setPath(newSharePath);

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
		model.addAttribute("data", newfileInfo);
	    } else {
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));
	    }
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/files/delete.ros")
    public String doDelete(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();

	String sharePath = req.getParameter("rim_path");
	String fileIds = req.getParameter("rim_fids");

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isAnyBlank(sharePath, fileIds)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    long[] fileIdData = CommonUtils.convertStringToArrayTypeLong(fileIds, ",", "0");

	    String originStorageId = "";

	    List<?> resultList = filesService.delete(originStorageId, userId, fileIdData);

	    if (resultList.size() > 0) {

		model.addAttribute("data", resultList);
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.request.result.cnt", new Object[] { fileIdData.length, fileIdData.length - resultList.size(), resultList.size() }));

	    } else {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	    }

	}

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/files/copy.ros")
    public String doCopy(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();
	String deptCd = LoginInfoHelper.getDeptCd();

	String sourceStorageId = req.getParameter("rim_s_sid");
	String targetStorageId = req.getParameter("rim_t_sid");
	String sourceUri = req.getParameter("rim_s_uri");
	String targetUri = req.getParameter("rim_t_uri");
	String fileIds = req.getParameter("rim_fids");
	String actionType = req.getParameter("rim_atype");

	if (!StringUtils.equalsAny(actionType, Constant.FILE_ACTION_TYPE_NEWNAME, Constant.FILE_ACTION_TYPE_REWRITE)) {
	    actionType = Constant.FILE_ACTION_TYPE_CANCEL;
	}

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isAnyBlank(sourceStorageId, targetStorageId, sourceUri, targetUri, fileIds)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {
	    long[] fileIdData = CommonUtils.convertStringToArrayTypeLong(fileIds, ",", "0");

	    FolderInfoVO folderInfo = sharingInService.getShareInFolderInfoBySharePath(LoginInfoHelper.getStorageId(), userId, deptCd, targetUri);


	    if (folderInfo != null) {
		targetStorageId = folderInfo.getOriginStorageId();
	    }

	    String targetPath = folderInfo.getOriginPath();

	    List<?> duplicatefileList = filesValidatorService.getDuplcationFileInfo(targetStorageId, sourceStorageId, userId, fileIdData, targetPath);

	    if (duplicatefileList != null && duplicatefileList.size() > 0) {

	    } else {

		List<?> resultList = filesService.copy(userId, targetStorageId, targetPath, sourceStorageId, sourceUri, fileIdData, actionType);

		if (resultList.size() > 0) {
		    model.addAttribute("data", resultList);
		    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.request.result.cnt", new Object[] { fileIdData.length, fileIdData.length - resultList.size(), resultList.size() }));
		} else {
		    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
		}
	    }
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/files/move.ros")
    public String doMove(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();
	String deptCd = LoginInfoHelper.getDeptCd();

	String pSourceStorageId = req.getParameter("rim_s_sid");
	String pTargetStorageId = req.getParameter("rim_t_sid");
	String pSourceUri = req.getParameter("rim_s_uri");
	String pTargetUri = req.getParameter("rim_t_uri");
	String fileIds = req.getParameter("rim_fids");

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isAnyBlank(pSourceStorageId, pTargetStorageId, pSourceUri, pTargetUri, fileIds)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else if (!StringUtils.equals(pSourceStorageId, pTargetStorageId)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.move.storage.not.equals"));

	} else {

	    long[] fileIdData = CommonUtils.convertStringToArrayTypeLong(fileIds, ",", "0");

	    String sourceStorageId = "";
	    String sourceUri = "";

	    FolderInfoVO targetFolderInfo = sharingInService.getShareInFolderInfoBySharePath(pTargetStorageId, userId, deptCd, pTargetUri);

	    String targetStorageId = "";
	    String targetUri = "";

	    if (targetFolderInfo != null) {
		targetStorageId = targetFolderInfo.getOriginStorageId();
		targetUri = targetFolderInfo.getOriginPath();
	    }

	    if (StringUtils.isAnyBlank(sourceStorageId, targetStorageId) || !StringUtils.equals(sourceStorageId, targetStorageId)) {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.move.storage.not.equals"));

		String sourceFilePath = sourceUri;
		String targetFilePath = targetUri;

		List<?> duplicatefileList = filesValidatorService.getDuplcationFileInfo(targetStorageId, sourceStorageId, userId, fileIdData, targetFilePath);

		if (duplicatefileList != null && duplicatefileList.size() > 0) {

		} else {

		    List<?> resultList = filesService.move(userId, targetStorageId, targetFilePath, sourceStorageId, sourceFilePath, fileIdData);

		    if (resultList.size() > 0) {
			model.addAttribute("data", resultList);
			returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.request.result.cnt", new Object[] { fileIdData.length, fileIdData.length - resultList.size(), resultList.size() }));
		    } else {
			returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
		    }
		}
	    }
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }
    
    @RequestMapping(value = "/files/down.ros")
    public String doDownload(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();
	
	String storageId = req.getParameter("rim_sid");
	String fileIds = req.getParameter("rim_fids");
	String path = req.getParameter("rim_path");
	
	if (!StringUtils.isAnyBlank(fileIds, path, storageId)) {

	    long[] fileIdData = CommonUtils.convertStringToArrayTypeLong(fileIds, ",", "0");

	    return filesService.streamDownload(model, storageId, userId, fileIdData);
	}
	
	return "";
    }
    
    @RequestMapping(value = "/files/checkUploadSize.ros")
    public String checkUploadSize(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();
	String deptCd = LoginInfoHelper.getDeptCd();

	String targetUri = req.getParameter("rim_t_uri");
	String strfileSize = req.getParameter("rim_fsize");
	long fileSize = CommonUtils.parseLong(strfileSize);

	ReturnVO returnVO = new ReturnVO();
	if (StringUtils.isBlank(targetUri) || fileSize < 0) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    String pathArr[] = StringUtils.split(targetUri, Constant.FILE_SEPARATOR);

	    if (pathArr.length < 1) {
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));
	    } else {

		String topPath = "/" + pathArr[0];
		FolderInfoVO folderInfo = sharingInService.getShareInFolderInfoBySharePath(LoginInfoHelper.getStorageId(), userId, deptCd, topPath);
		String originStorageId = folderInfo.getOriginStorageId();

		if (!filesValidatorService.checkUploadForStorageSize(originStorageId, fileSize)) {

		    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.storage.not.enough"));

		} else {

		    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
		}
	    }
	}

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/files/checkUpload.ros")
    public String checkUpload(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();
	String deptCd = LoginInfoHelper.getDeptCd();
	String targetUri = req.getParameter("rim_t_uri");
	String fileName = req.getParameter("rim_fnm");
	String strfileSize = req.getParameter("rim_fsize");

	long fileSize = CommonUtils.parseLong(strfileSize);

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isAnyBlank(targetUri, fileName)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	}  else if(fileSize >=  PropertyConfigurerHelper.getMaxUploadSize() ){

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.upload.file.maxsize.exceed", new String[] { CommonUtils.readableFileSize( PropertyConfigurerHelper.getMaxUploadSize()) }));

	} else {

	    String pathArr[] = StringUtils.split(targetUri, Constant.FILE_SEPARATOR);

	    if (pathArr.length < 1) {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	    } else {

		String topPath = "/" + pathArr[0];

		FolderInfoVO folderInfo = sharingInService.getShareInFolderInfoBySharePath(LoginInfoHelper.getStorageId(), userId, deptCd, topPath);

		String originStorageId = folderInfo.getOriginStorageId();
		String originTopFilePath = folderInfo.getOriginPath();
		String originFilePath = originTopFilePath + CommonUtils.convertArrayToString(pathArr, Constant.FILE_SEPARATOR, "", "", 1, -1);

		if (!filesValidatorService.checkUploadForStorageSize(originStorageId, fileSize)) {
		    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.storage.not.enough"));
		} else {
		    FileListVO fileListVO = filesService.getInfoExistFileByName(originStorageId, userId, originFilePath, fileName);
		    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
		    model.addAttribute("data", fileListVO);
		}
	    }
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/files/upload.ros")
    public String doUpload(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	MultipartHttpServletRequest multiPartRequest = (MultipartHttpServletRequest) req;
	MultipartFile file = multiPartRequest.getFile("rimUploadFile");

	String userId = LoginInfoHelper.getUserId();
	String deptCd = LoginInfoHelper.getDeptCd();

	String targetUri = req.getParameter("rim_t_uri");
	String isRewrite = req.getParameter("rim_is_rewrite");

	if (StringUtils.isBlank(isRewrite)) {
	    isRewrite = "N";
	}

	ReturnVO returnVO = new ReturnVO();
	FileListVO fileInfo = null;

	String pathArr[] = StringUtils.split(targetUri, Constant.FILE_SEPARATOR);

	if (StringUtils.isBlank(targetUri) || (file.isEmpty() && StringUtils.isBlank(file.getName())) || pathArr.length < 1) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    String topPath = "/" + pathArr[0];

	    FolderInfoVO folderInfo = sharingInService.getShareInFolderInfoBySharePath(LoginInfoHelper.getStorageId(), userId, deptCd, topPath);

	    String originStorageId = folderInfo.getOriginStorageId();
	    String originTopFilePath = folderInfo.getOriginPath();
	    String originFilePath = originTopFilePath + CommonUtils.convertArrayToString(pathArr, Constant.FILE_SEPARATOR, "", "", 1, -1);

	    boolean isFileRewrite = StringUtils.equals(Constant.COMMOM_YES, isRewrite);

	    fileInfo = filesService.makeFile(originStorageId, userId, originFilePath, file, isFileRewrite);

	    if (fileInfo != null) {
		String newSharePath = getSharePathForMergeNewFile(targetUri, fileInfo.getName());
		fileInfo.setPath(newSharePath);

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
		model.addAttribute("data", fileInfo);

	    }
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    private String getSharePathForMergeNewFile(String ParentSharePath, String newFileName) {

	return ParentSharePath + Constant.FILE_SEPARATOR + newFileName;
    }
   
}
