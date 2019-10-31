package kr.co.crim.oss.rimdrive.personalstorage;

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
import kr.co.crim.oss.rimdrive.files.service.PagingVO;
import kr.co.crim.oss.rimdrive.files.service.StorageService;
import kr.co.crim.oss.rimdrive.files.service.StorageVO;

@Controller("PFilesController")
@RequestMapping(value = "/p/files")
public class FilesController {

    @Resource(name = "storageService")
    private StorageService storageService;

    @Resource(name = "filesService")
    private FilesService filesService;

    @Resource(name = "filesValidatorService")
    private FilesValidatorService filesValidatorService;

    @RequestMapping(value = "/storageusage.ros")
    public String getStorageUsage(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();

	ReturnVO returnVO = new ReturnVO();

	StorageVO storageVO = storageService.getStorageInfo(storageId);

	if (storageVO != null) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	    model.addAttribute("data", storageVO);

	} else {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));

	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/info.ros")
    public String getInfo(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();

	String strFileId = req.getParameter("rim_fid");
	long fileId = CommonUtils.parseLong(strFileId);

	ReturnVO returnVO = new ReturnVO();

	if (fileId < 1) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    FileListVO fileInfo = filesService.getInfoIncludeBookMarkByFileId(storageId, fileId, userId);

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

    @RequestMapping(value = "/foldersubinfo.ros")
    public String getFolderSubInfo(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();

	String strFileId = req.getParameter("rim_fid");
	long fileId = CommonUtils.parseLong(strFileId);

	ReturnVO returnVO = new ReturnVO();

	if (fileId < 1) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));
	    
	} else {

	    FileListVO fileInfo = filesService.getInfoByFileId(storageId, fileId);

	    if (fileInfo != null) {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
		FolderSubInfoVO folderSubInfo = filesService.getFolderSubInfoAll(storageId, fileInfo.getPath());
		model.addAttribute("data", folderSubInfo);

	    } else {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));
	    }
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/infobypath.ros")
    public String getInfoByPath(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();

	String path = req.getParameter("rim_path");

	ReturnVO returnVO = new ReturnVO();
	returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	if ( !StringUtils.isBlank(path)) {
	    FileListVO fileInfo = filesService.getInfoByPath(storageId, path);
	    model.addAttribute("data", fileInfo);
	}
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/list.ros")
    public String getList(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();

	String path = req.getParameter("rim_path");

	if (StringUtils.isBlank(path)) {
	    path = Constant.FILE_SEPARATOR;
	}

	FolderInfoVO folderInfo = filesService.getFolderInfo(storageId, path);
	FolderSubInfoVO folderSubInfo = filesService.getFolderSubInfo(storageId, path);

	PagingVO pagingVO = filesService.getListByPathPaging(storageId, userId, path, CommonUtils.getPagingParameter(req));

	Map<String, Object> returnDataMap = new HashMap<String, Object>();
	returnDataMap.put("folderInfo", folderInfo);
	returnDataMap.put("folderSubInfo", folderSubInfo);
	returnDataMap.put("files", pagingVO.getResultList());
	returnDataMap.put("totalSize", pagingVO.getTotalRows());

	ReturnVO returnVO = new ReturnVO();
	returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));

	model.addAttribute("data", returnDataMap);
	model.addAttribute("status", returnVO);

	return "pageJsonView";
    }

    @RequestMapping(value = "/folderlist.ros")
    public String getFolderList(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();

	String strFileId = req.getParameter("rim_fid");
	long fileId = CommonUtils.parseLong(strFileId);

	ReturnVO returnVO = new ReturnVO();
	
	if ( fileId < 0 ) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {
	    
	    List<?> resultList = null;
	    
	    if (fileId == 0) {
		resultList = filesService.getFolderListAll(storageId, userId);
	    } else {
		resultList = filesService.getFolderListByParentId(storageId, userId, fileId);
	    }
	    
	    model.addAttribute("data", resultList);
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));

	}

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/newfolder.ros")
    public String makeFolder(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();

	String path = req.getParameter("rim_path");
	String folderName = req.getParameter("rim_folder_nm");

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isAnyBlank(path, folderName)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    folderName = FileUtil.removeTailDots(folderName);

	    FileListVO folderInfo = filesService.makeFolder(storageId, userId, path, folderName, true);
	    if (folderInfo != null) {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
		model.addAttribute("data", folderInfo);

	    } else {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));
	    }
	}

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/rename.ros")
    public String setReName(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();

	String path = req.getParameter("rim_path");
	String reName = req.getParameter("rim_re_nm");
	String strFileId = req.getParameter("rim_fid");

	long fileId = CommonUtils.parseLong(strFileId);

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isAnyBlank(strFileId, path, reName) || fileId < 1) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    String targetFileUri = FileUtil.getFilePath(path, reName);
	    reName = FileUtil.removeTailDots(reName);

	    FileListVO fileInfo = filesService.setReName(storageId, userId, fileId, path, reName, targetFileUri);
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

    @RequestMapping(value = "/delete.ros")
    public String doDelete(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();

	String fileIds = req.getParameter("rim_fids");

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isAnyBlank(fileIds)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    long[] fileIdData = CommonUtils.convertStringToArrayTypeLong(fileIds, ",", "0");

	    List<?> resultList = filesService.delete(storageId, userId, fileIdData);

	    if (resultList.size() > 0) {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.request.result.cnt", new Object[] { fileIdData.length, fileIdData.length - resultList.size(), resultList.size() }));
		model.addAttribute("data", resultList);

	    } else {

		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	    }
	}

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/copy.ros")
    public String doCopy(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();
	String targetStorageId = LoginInfoHelper.getStorageId();

	String sourceStorageId = req.getParameter("rim_s_sid");
	String sourceUri = req.getParameter("rim_s_uri");
	String targetUri = req.getParameter("rim_t_uri");
	String fileIds = req.getParameter("rim_fids");
	String actionType = req.getParameter("rim_atype");

	if (!StringUtils.equalsAny(actionType, Constant.FILE_ACTION_TYPE_NEWNAME, Constant.FILE_ACTION_TYPE_REWRITE)) {
	    actionType = Constant.FILE_ACTION_TYPE_CANCEL;
	}

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isAnyBlank(sourceStorageId, sourceUri, targetUri, fileIds)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    long[] fileIdData = CommonUtils.convertStringToArrayTypeLong(fileIds, ",", "0");

	    List<?> duplicatefileList = filesValidatorService.getDuplcationFileInfo(targetStorageId, sourceStorageId, userId, fileIdData, targetUri);

	    if (duplicatefileList != null && duplicatefileList.size() > 0) {
		String duplicateFileNames = CommonUtils.getDuplicateFileName(duplicatefileList);
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.exist.name.msg", new Object[] { duplicatefileList.size(), duplicateFileNames }));
	    } else {

		List<?> resultList = filesService.copy(userId, targetStorageId, targetUri, sourceStorageId, sourceUri, fileIdData, actionType);

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

    @RequestMapping(value = "/move.ros")
    public String doMove(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String userId = LoginInfoHelper.getUserId();
	String targetStorageId = LoginInfoHelper.getStorageId();

	String sourceStorageId = req.getParameter("rim_s_sid");
	String sourceUri = req.getParameter("rim_s_uri");
	String targetUri = req.getParameter("rim_t_uri");
	String fileIds = req.getParameter("rim_fids");

	String storageType = req.getParameter("rim_storage_type");
	String currentStorageType = Constant.STORAGE_ACCESS_TYPE_PERSONAL;

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isAnyBlank(sourceStorageId, sourceUri, targetUri, fileIds)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else if ( !StringUtils.equals(storageType, currentStorageType )|| !StringUtils.equals(sourceStorageId, targetStorageId)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.move.storage.not.equals"));

	} else {

	    long[] fileIdData = CommonUtils.convertStringToArrayTypeLong(fileIds, ",", "0");

	    List<?> duplicatefileList = filesValidatorService.getDuplcationFileInfo(targetStorageId, sourceStorageId, userId, fileIdData, targetUri);

	    if (duplicatefileList != null && duplicatefileList.size() > 0) {
		String duplicateFileNames = CommonUtils.getDuplicateFileName(duplicatefileList);
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.exist.name.msg", new Object[] { duplicatefileList.size(), duplicateFileNames }));

	    } else {
		List<?> resultList = filesService.move(userId, targetStorageId, targetUri, sourceStorageId, sourceUri, fileIdData);

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

    @RequestMapping(value = "/down.ros")
    public String doDownload(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();

	String fileIds = req.getParameter("rim_fids");
	
	if (!StringUtils.isBlank(fileIds)) {

	    long[] fileIdData = CommonUtils.convertStringToArrayTypeLong(fileIds, ",", "0");

	    return filesService.streamDownload(model, storageId, userId, fileIdData);
	}
	
	return "";
    }

    @RequestMapping(value = "/checkUploadSize.ros")
    public String checkUploadSize(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String strfileSize = req.getParameter("rim_fsize");
	long fileSize = CommonUtils.parseLong(strfileSize);

	ReturnVO returnVO = new ReturnVO();
	if (!filesValidatorService.checkUploadForStorageSize(storageId, fileSize)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.storage.not.enough"));

	} else {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	}

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/checkUpload.ros")
    public String checkUpload(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();

	String targetUri = req.getParameter("rim_t_uri");
	String fileName = req.getParameter("rim_fnm");
	String strfileSize = req.getParameter("rim_fsize");

	long fileSize = CommonUtils.parseLong(strfileSize);

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isAnyBlank(targetUri, fileName)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else if (!filesValidatorService.checkUploadForStorageSize(storageId, fileSize)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.storage.not.enough"));

	} else if(fileSize >=  PropertyConfigurerHelper.getMaxUploadSize() ){

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.upload.file.maxsize.exceed", new String[] { CommonUtils.readableFileSize( PropertyConfigurerHelper.getMaxUploadSize()) }));

	} else {

	    FileListVO fileListVO = filesService.getInfoExistFileByName(storageId, userId, targetUri, fileName);
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	    model.addAttribute("data", fileListVO);
	}

	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

    @RequestMapping(value = "/upload.ros")
    public String doUpload(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	MultipartHttpServletRequest multiPartRequest = (MultipartHttpServletRequest) req;
	MultipartFile file = multiPartRequest.getFile("rimUploadFile");

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();

	String targetUri = req.getParameter("targetUri");
	String isRewrite = req.getParameter("isRewrite");
	
	if (StringUtils.isBlank(isRewrite)) {
	    isRewrite = Constant.COMMOM_NO;
	}

	ReturnVO returnVO = new ReturnVO();
	FileListVO fileInfo = null;

	if (StringUtils.isAnyBlank(targetUri) || ( file.isEmpty() && StringUtils.isBlank(file.getName())) )  {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    if (StringUtils.equals(Constant.COMMOM_YES, isRewrite)) {

		fileInfo = filesService.makeFile(storageId, userId, targetUri, file, true);

	    } else {
		fileInfo = filesService.makeFile(storageId, userId, targetUri, file, false);
	    }

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

}
