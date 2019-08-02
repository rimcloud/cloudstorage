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

import kr.co.crim.oss.rimdrive.common.service.ReturnVO;
import kr.co.crim.oss.rimdrive.common.utils.CommonUtils;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.common.utils.LoginInfoHelper;
import kr.co.crim.oss.rimdrive.common.utils.MessageSourceHelper;
import kr.co.crim.oss.rimdrive.files.service.FilesValidatorService;
import kr.co.crim.oss.rimdrive.files.service.FolderInfoVO;
import kr.co.crim.oss.rimdrive.files.service.FolderSubInfoVO;
import kr.co.crim.oss.rimdrive.files.service.PagingVO;
import kr.co.crim.oss.rimdrive.files.service.TrashBinService;

@Controller("PTrashBinController")
@RequestMapping(value = "/p/t")
public class TrashBinController {

    @Resource(name = "trashBinService")
    private TrashBinService trashBinService;
    

    @Resource(name = "filesValidatorService")
    private FilesValidatorService filesValidatorService;


    @RequestMapping(value = "/list.ros")
    public String getList(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();

	String path = req.getParameter("rim_path");

	if (StringUtils.isBlank(path)) {
	    path = Constant.FILE_SEPARATOR;
	}

	FolderInfoVO folderInfo = new FolderInfoVO();
	folderInfo.setStorageId(storageId);
	folderInfo.setPermissions(Constant.AUTH_TYPE_WRITE);
	FolderSubInfoVO folderSubInfo = trashBinService.getFolderSubInfo(storageId, path, CommonUtils.getPagingParameter(req));

	PagingVO pagingVO = trashBinService.getListPagingByPath(storageId, path, CommonUtils.getPagingParameter(req));

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

    @RequestMapping(value = "/clear.ros")
    public String doClear(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();

	ReturnVO returnVO = new ReturnVO();

	boolean result = trashBinService.deleteAll(storageId, userId);

	if (result) {
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	} else {
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.fail.msg"));
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

	if (StringUtils.isBlank(fileIds)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    long[] fileIdData = CommonUtils.convertStringToArrayTypeLong(fileIds, ",", fileIds);

	    List<?> resultList = trashBinService.delete(storageId, userId, fileIdData);

	    if (resultList.size() > 0) {
		model.addAttribute("data", resultList);
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.request.result.cnt",
			new Object[] { fileIdData.length, fileIdData.length - resultList.size(), resultList.size() }));
	    } else {
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	    }

	}
	model.addAttribute("status", returnVO);
        return "pageJsonView";
    }

    @RequestMapping(value = "/restore.ros")
    public String doReStore(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();

	String fileIds = req.getParameter("rim_fids");

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isBlank(fileIds)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    long[] fileIdData = CommonUtils.convertStringToArrayTypeLong(fileIds, ",", fileIds);

	    if (filesValidatorService.checkRestoreForStorageSize(userId, storageId)) {
		List<?> resultList = trashBinService.reStore(storageId, userId, fileIdData);

		if (resultList.size() > 0) {
		    model.addAttribute("data", resultList);
		    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.request.result.cnt", new Object[] { fileIdData.length, fileIdData.length - resultList.size(), resultList.size() }));
		} else {
		    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
		}
	    } else {
		returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("fail.storage.not.enough"));
	    }

	}
	model.addAttribute("status", returnVO);
        return "pageJsonView";
    }
 
}

