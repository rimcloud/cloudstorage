package kr.co.crim.oss.rimdrive.search.controller;

import java.util.HashMap;
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
import kr.co.crim.oss.rimdrive.files.service.FilesService;
import kr.co.crim.oss.rimdrive.files.service.FilesValidatorService;
import kr.co.crim.oss.rimdrive.files.service.PagingVO;
import kr.co.crim.oss.rimdrive.files.service.StorageService;
import kr.co.crim.oss.rimdrive.search.service.SearchService;
import kr.co.crim.oss.rimdrive.share.service.SharingInService;

@Controller
@RequestMapping(value = "/s")
public class SearchController {

    @Resource(name = "filesService")
    private FilesService filesService;

    @Resource(name = "searchService")
    private SearchService searchService;

    @Resource(name = "storageService")
    private StorageService storageService;

    @Resource(name = "filesValidatorService")
    private FilesValidatorService filesValidatorService;

    @Resource(name = "sharingInService")
    private SharingInService sharingInService;

    @RequestMapping(value = "/list.ros")
    public String getList(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String storageId = LoginInfoHelper.getStorageId();
	String userId = LoginInfoHelper.getUserId();
	String deptCd = LoginInfoHelper.getDeptCd();

	String searchPath  = req.getParameter("rim_search_path");;
	String searchDoc = req.getParameter("rim_search_doc");
	String searchTags = req.getParameter("rim_search_tags");
	String searchSize = req.getParameter("rim_search_size");
	String searchModifydateStart = req.getParameter("rim_search_modify_date_start");
	String searchModifydateEnd = req.getParameter("rim_search_modify_date_end");
	String searchOwnerName = req.getParameter("rim_search_owner_nm");
	String searchText = req.getParameter("rim_search_text");

	ReturnVO returnVO = new ReturnVO();
	Map<String, Object> searchMap = new HashMap<String, Object>();

	searchMap.put("storageAccessType", "P");
	searchMap.put("searchPath", searchPath);
	if (!StringUtils.isBlank(searchDoc)) {
	    searchMap.put("searchDoc", searchDoc.split(","));
	}
	searchMap.put("searchTags", searchTags);
	searchMap.put("searchSize", searchSize);
	searchMap.put("searchModifydateStart", searchModifydateStart);
	searchMap.put("searchModifydateEnd", searchModifydateEnd);
	searchMap.put("searchOwnerName", searchOwnerName);
	searchMap.put("searchText", searchText);
	searchMap.put("userId", userId);
	searchMap.put("deptCd", deptCd);

	searchMap.put("searchPosStorageId", storageId);

	Map<String, Object> returnDataMap = new HashMap<String, Object>();

	PagingVO pagingVO = searchService.getListPaging(searchMap, CommonUtils.getPagingParameter(req));

	returnDataMap.put("files", pagingVO.getResultList());
	returnDataMap.put("totalSize", pagingVO.getTotalRows());

	model.addAttribute("data", returnDataMap);
	returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));
	model.addAttribute("status", returnVO);
	return "pageJsonView";
    }

}
