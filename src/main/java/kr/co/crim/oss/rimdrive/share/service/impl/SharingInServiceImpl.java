package kr.co.crim.oss.rimdrive.share.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.common.utils.FileUtil;
import kr.co.crim.oss.rimdrive.files.service.FolderInfoVO;
import kr.co.crim.oss.rimdrive.files.service.FolderSubInfoVO;
import kr.co.crim.oss.rimdrive.share.service.SharingInService;

@Service("sharingInService")
public class SharingInServiceImpl implements SharingInService {

    @Resource(name = "sharingInDAO")
    private SharingInDAO sharingInDAO;

    @Override
    public List<?> getTopList(String storageId, String userId, String deptCd, Map<String, Object> pagingMap) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("deptCd", deptCd);
	paramMap.putAll(pagingMap);
	return sharingInDAO.selectTopList(new ParamDaoVO(paramMap));
    }

    @Override
    public List<?> getSubListByPath(String storageId, String userId, String path, String sharePath, Map<String, Object> pagingMap) throws Exception {

   	path = FileUtil.getStoragMountPath(path);
   	Map<String, Object> paramMap = new HashMap<String, Object>();
   	paramMap.put("storageId", storageId);
   	paramMap.put("userId", userId);
   	paramMap.put("path", path);
   	paramMap.put("sharePath", sharePath);
   	paramMap.putAll(pagingMap);
   	return sharingInDAO.selectSubList(new ParamDaoVO(paramMap));

    }

    @Override
    public FolderSubInfoVO getFolderSubInfo(String storageId, String path) throws Exception {
	path = FileUtil.getStoragMountPath(path);
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("path", path);
	FolderSubInfoVO returnVO = sharingInDAO.selectFolderSubInfo(new ParamDaoVO(paramMap));
	if (returnVO == null)
	    returnVO = new FolderSubInfoVO();

	return returnVO;
    }

    @Override
    public FolderInfoVO getShareInFolderInfoBySharePath(String storageId, String userId, String deptCd, String sharePath) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("shareStorageType", Constant.STORAGE_ACCESS_TYPE_PERSONAL_SHARINGIN);
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("deptCd", deptCd);
	paramMap.put("sharePath", sharePath);
	return sharingInDAO.selectShareInFolderInfoBySharePath(new ParamDaoVO(paramMap));

    }
    
}