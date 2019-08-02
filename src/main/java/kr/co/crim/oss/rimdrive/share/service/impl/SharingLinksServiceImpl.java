package kr.co.crim.oss.rimdrive.share.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.share.service.ShareVO;
import kr.co.crim.oss.rimdrive.share.service.SharingLinksListVO;
import kr.co.crim.oss.rimdrive.share.service.SharingLinksService;

@Service("sharingLinksService")
public class SharingLinksServiceImpl implements SharingLinksService {

    @Resource(name = "sharingLinksDAO")
    private SharingLinksDAO sharingLinksDAO;

    @Override
    public List<?> getListByStorageId(String storageId, String userId, Map<String, Object> pagingMap) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.putAll(pagingMap);
	return sharingLinksDAO.selectList(new ParamDaoVO(paramMap));
    }

    @Override
    public SharingLinksListVO getInfoByFileId(String storageId, String userId, long fileId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("fileId", fileId);
	return sharingLinksDAO.selectFileInfo(new ParamDaoVO(paramMap));
    }

    @Override
    public ShareVO getShare(String storageId, long fileId, String userId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("fileId", fileId);
	paramMap.put("userId", userId);
	return sharingLinksDAO.selectShare(new ParamDaoVO(paramMap));
    }

    @Override
    public ShareVO getShare(String token) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("token", token);
	return sharingLinksDAO.selectShare(new ParamDaoVO(paramMap));
    }

    @Override
    public int addShare(String storageId, long fileId, String userId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("fileId", fileId);
	paramMap.put("userId", userId);
	return sharingLinksDAO.insertShare(new ParamDaoVO(paramMap));
    }

    @Override
    public int deleteShare(String userId, long shareId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("shareId", shareId);
	return sharingLinksDAO.deleteShare(new ParamDaoVO(paramMap));
    }

}
