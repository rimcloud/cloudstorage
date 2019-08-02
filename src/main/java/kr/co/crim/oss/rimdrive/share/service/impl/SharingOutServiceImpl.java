package kr.co.crim.oss.rimdrive.share.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.share.service.ShareVO;
import kr.co.crim.oss.rimdrive.share.service.SharingOutListVO;
import kr.co.crim.oss.rimdrive.share.service.SharingOutService;

@Service("sharingOutService")
public class SharingOutServiceImpl implements SharingOutService {

    @Resource(name = "sharingOutDAO")
    private SharingOutDAO sharingOutDAO;

    @Override
    public List<?> getListByStorageId(String storageId, String userId, Map<String, Object> pagingMap) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.putAll(pagingMap);
	return sharingOutDAO.selectList(new ParamDaoVO(paramMap));
    }

    @Override
    public SharingOutListVO getInfoByFileId(String storageId, String userId, long fileId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("fileId", fileId);
	return sharingOutDAO.selectFileInfo(new ParamDaoVO(paramMap));
    }


    @Override
    public ShareVO getShare(String storageId, long fileId, String userId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("fileId", fileId);
	paramMap.put("userId", userId);
	return sharingOutDAO.selectShare(new ParamDaoVO(paramMap));
    }

    @Override
    public ShareVO getShareByShareId(long shareId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("shareId", shareId);
	return sharingOutDAO.selectShare(new ParamDaoVO(paramMap));
    }

    @Override
    public List<?> getShareTargetListByShareId(long shareId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("shareId", shareId);
	return sharingOutDAO.selectShareTargetList(new ParamDaoVO(paramMap));
    }

    private int addShare(String storageId, long fileId, String userId, String accessPw, String expireDate, String message) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("fileId", fileId);
	paramMap.put("userId", userId);
	return sharingOutDAO.insertShare(new ParamDaoVO(paramMap));
    }

    private int addShareTarget(String userId, long shareId, Object insertTarget) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("userId", userId);
	paramMap.put("shareId", shareId);
	paramMap.put("insertTarget", insertTarget);
	return sharingOutDAO.insertShareTarget(new ParamDaoVO(paramMap));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public int addShareAll(String storageId, long fileId, String userId, String accessPw, String expireDate, String message, Map<String, Object> paramMap) throws Exception {
	int retValue = 0;

	retValue = this.addShare(storageId, fileId, userId, accessPw, expireDate, message);
	if (retValue > 0) {
	    ShareVO shareVO = this.getShare(storageId, fileId, userId);

	    if (!ObjectUtils.isEmpty(paramMap.get("insertTarget"))) {
		this.addShareTarget(userId, shareVO.getShareId(), paramMap.get("insertTarget"));
	    }
	}

	return retValue;
    }

    private int deleteShare(long shareId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("shareId", shareId);
	return sharingOutDAO.deleteShare(new ParamDaoVO(paramMap));
    }

    private int deleteShareTarget(long shareId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("shareId", shareId);
	return sharingOutDAO.deleteShareTarget(new ParamDaoVO(paramMap));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public int deleteShareAll(String userId, long shareId) throws Exception {

	this.deleteShareTarget(shareId);
	return this.deleteShare(shareId);
    }

    private int updateShareTarget(String userId, long shareId,String permissions, Object updateTarget ) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("userId", userId);
	paramMap.put("shareId", shareId);
	paramMap.put("permissions", permissions);
	paramMap.put("updateTarget", updateTarget);
	return sharingOutDAO.updateShareTarget(new ParamDaoVO(paramMap));
    }

    private int deleteShareTarget(Object deleteTarget) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("deleteTarget", deleteTarget);
	return sharingOutDAO.deleteShareTarget(new ParamDaoVO(paramMap));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public int updateShareAll(String userId, long shareId, String accessPw, String expireDate, String message, Map<String, Object> paramMap) throws Exception {

	if (!ObjectUtils.isEmpty(paramMap.get("insertTarget"))) {
	    this.addShareTarget(userId, shareId, paramMap.get("insertTarget"));
	}

	if (!ObjectUtils.isEmpty(paramMap.get("updateTargetR"))) {
	    this.updateShareTarget(userId, shareId, Constant.AUTH_TYPE_READ, paramMap.get("updateTargetR"));
	}

	if (!ObjectUtils.isEmpty(paramMap.get("updateTargetW"))) {
	    this.updateShareTarget(userId, shareId, Constant.AUTH_TYPE_WRITE, paramMap.get("updateTargetW"));
	}

	if (!ObjectUtils.isEmpty(paramMap.get("deleteTarget"))) {
	    this.deleteShareTarget(paramMap.get("deleteTarget"));
	}

	return 1;
    }
}
