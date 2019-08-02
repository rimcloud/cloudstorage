package kr.co.crim.oss.rimdrive.account.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.crim.oss.rimdrive.account.service.EmpService;
import kr.co.crim.oss.rimdrive.account.service.EmpVO;
import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.files.service.FilesService;

@Service("empService")
public class EmpServiceImpl implements EmpService {

    @Resource(name = "empDAO")
    private EmpDAO empDAO;

    @Resource(name = "filesService")
    private FilesService filesService;

    private static final Logger logger = LoggerFactory.getLogger(EmpServiceImpl.class);

    @Override
    public List<?> getListByDeptCd(String deptCd, Map<String, Object> pagingMap) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("deptCd", deptCd);
	paramMap.putAll(pagingMap);
	return empDAO.selectList(new ParamDaoVO(paramMap));
    }

    @Override
    public List<?> getSearchList(String siteCd, String searchId, String searchText) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("siteCd", siteCd);
	paramMap.put("searchId", searchId);
	paramMap.put("searchText", searchText);
	return empDAO.selectSearchList(new ParamDaoVO(paramMap));
    }

    @Override
    public List<?> getSearchListByEmail(String siteCd, String searchText) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("email", searchText);
	return empDAO.selectSearchList(new ParamDaoVO(paramMap));
    }

    @Override
    public EmpVO getEmpInfoByEmpId(String empId) throws Exception{
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("empId", empId);
	return empDAO.selectEmpInfo(new ParamDaoVO(paramMap));
    }

    @Override
    public boolean isExistStorageInfo(String userId) throws Exception{
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("userId", userId);
	if( empDAO.selectStorageInfo(new ParamDaoVO(paramMap)) > 0 )
	    return true;
	return false;
    }

    @Override
    public boolean isExistUserInfo(String userId) throws Exception{
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("userId", userId);
	if( empDAO.selectUserInfo(new ParamDaoVO(paramMap)) > 0 )
	    return true;
	return false;
    }

    private int insertStorageInfo(String storageId, String ownerId, String createUserId, String quota) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("ownerId", ownerId);
	paramMap.put("quota", quota);
	paramMap.put("ownerTp", Constant.OWNER_TYPE_USER);
	paramMap.put("createUserId", createUserId);

	return empDAO.insertStorageInfo(new ParamDaoVO(paramMap));
    }

    private int updateQuotaByOwnerId(String ownerId, String modifyUserId, String quota) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("ownerId", ownerId);
	paramMap.put("quota", quota);
	paramMap.put("modifyUserId", modifyUserId);

	return empDAO.updateQuotaByOwnerId(new ParamDaoVO(paramMap));
    }

    private int insertUserInfo(String userId, String userNm, String password) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("userId", userId);
	paramMap.put("displayNm", userNm);
	paramMap.put("password", password);

	return empDAO.insertUserInfo(new ParamDaoVO(paramMap));
    }

    @Override
    public boolean addEmpOnlyStorage(String actionUserId,String empId, String password) throws Exception{

	boolean result = false;

	String storageId = empId;

	try {

	    EmpVO empVO = getEmpInfoByEmpId(empId);
	    if (empVO != null) {
		if (StringUtils.equals("Y", empVO.getHlofcYn()) && StringUtils.equals("Y", empVO.getCloudAllowYn())) {

		    String userNm = empVO.getEmpNm();
		    String quota = Integer.toString(empVO.getQuota());

		    if (!isExistStorageInfo(empId)) {
			insertStorageInfo(storageId, empId, actionUserId, quota);
		    }

		    if (!isExistUserInfo(empId)) {
			insertUserInfo(empId, userNm, password);
		    }

		    filesService.makeAllSystemFolder(storageId, empId);
		}

		result = true;

	    }

	} catch (Exception e) {

	    result = false;
	    logger.error("Excetpion", e);
	}

	return result;
    }

    @Override
    public boolean changeEmpOnlyStorage(String actionUserId,String empId) throws Exception{

	boolean result = false;

	String storageId = empId;

	try {

	    EmpVO empVO = getEmpInfoByEmpId(empId);
	    if (empVO != null) {

		String quota = Integer.toString(empVO.getQuota());

		if(!isExistStorageInfo(empId)) {
		   insertStorageInfo(storageId ,empId, actionUserId, quota);
		}else {
		    updateQuotaByOwnerId(empId, actionUserId, quota);
		}

		result = true;
	    }

	} catch (Exception e) {

	    result = false;
	    logger.error("Excetpion", e);
	}

	return result;
    }
    


}