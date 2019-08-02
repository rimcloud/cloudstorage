package kr.co.crim.oss.rimdrive.common.controller;

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

import kr.co.crim.oss.rimdrive.account.service.DeptService;
import kr.co.crim.oss.rimdrive.account.service.EmpService;
import kr.co.crim.oss.rimdrive.account.service.EmpVO;
import kr.co.crim.oss.rimdrive.account.service.LoginService;
import kr.co.crim.oss.rimdrive.common.service.ReturnVO;
import kr.co.crim.oss.rimdrive.common.service.RimDriveVO;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.common.utils.LoginInfoHelper;
import kr.co.crim.oss.rimdrive.common.utils.MessageSourceHelper;
import kr.co.crim.oss.rimdrive.common.utils.PropertyConfigurerHelper;
import kr.co.crim.oss.rimdrive.files.service.FileListVO;
import kr.co.crim.oss.rimdrive.files.service.FilesService;
import kr.co.crim.oss.rimdrive.files.service.StorageService;
import kr.co.crim.oss.rimdrive.share.service.ShareVO;
import kr.co.crim.oss.rimdrive.share.service.SharingInService;
import kr.co.crim.oss.rimdrive.share.service.SharingLinksService;

@Controller
public class PageController {

    @Resource(name = "empService")
    private EmpService empService;

    @Resource(name = "loginService")
    private LoginService loginService;

    @Resource(name = "deptService")
    private DeptService deptService;

    @Resource(name = "storageService")
    private StorageService storageService;

    @Resource(name = "filesService")
    private FilesService filesService;

    @Resource(name = "sharingInService")
    private SharingInService sharingInService;

    @Resource(name = "sharingLinksService")
    private SharingLinksService sharingLinksService;

    private void setPageModelMap(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	RimDriveVO rimDriveVO = LoginInfoHelper.getRimDriveVO(req, res);
	model.addAttribute("rimDriveVO", rimDriveVO);
	model.addAttribute("storageVO", storageService.getStorageInfo(LoginInfoHelper.getStorageId()));

	Map<String, Object> userConfig = new HashMap<String, Object>();
	userConfig.put("webOfficeYn", PropertyConfigurerHelper.getWebOfficeYn() );

	model.addAttribute("userConfig", userConfig);
    }

    @RequestMapping(value = "/page/files.do")
    public String pageFiles(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	this.setPageModelMap(req, res, model);

	return "files/files";
    }

    @RequestMapping(value = "/page/popup.do")
    public String pagePopup(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String pageViewName = req.getParameter("rim_view");

	return "popup/" + pageViewName;
    }

    @RequestMapping(value = "/page/guest/sl.do")
    public String pageSharingLinks(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	RimDriveVO rimDriveVO =  LoginInfoHelper.getRimDriveVO(req, res);
	model.addAttribute("rimDriveVO", rimDriveVO);

	String token = req.getParameter("rim_token");
	
	ShareVO shareVO = sharingLinksService.getShare(token);
	if (shareVO != null) {
	    EmpVO empVO = empService.getEmpInfoByEmpId( shareVO.getCreateUid() );
	    FileListVO fileListVO = filesService.getInfoByFileId(shareVO.getStorageId(),shareVO.getFileId());
	    
	    model.addAttribute("empVO", empVO);
	    model.addAttribute("fileListVO", fileListVO);
	    model.addAttribute("sharingLinksVO", shareVO);
	    model.addAttribute("token", token);
	}

	model.addAttribute("footer", PropertyConfigurerHelper.getLoginFooter());
	return "files/sharinglinks";
    }

    @RequestMapping(value = "/page/guest/emplist.do")
    public String getList(HttpServletRequest req, HttpServletResponse res, ModelMap model) throws Exception {

	String empNm = req.getParameter("rim_emp_nm");

	ReturnVO returnVO = new ReturnVO();

	if (StringUtils.isBlank(empNm)) {

	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_FAIL, MessageSourceHelper.getMessage("common.invalid.msg"));

	} else {

	    List<?> empList = loginService.getListByEmpNm(empNm);
	    returnVO.setReturnInfo(Constant.COMMOM_RESULT_SUCCESS, MessageSourceHelper.getMessage("common.success.msg"));

	    model.addAttribute("data", empList);
	}

	model.addAttribute("status", returnVO);

	return "pageJsonView";
    }
    

}
