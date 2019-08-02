package kr.co.crim.oss.rimdrive.files.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.files.service.StorageService;
import kr.co.crim.oss.rimdrive.files.service.StorageVO;

@Service("storageService")
public class StorageServiceImpl implements StorageService{

    @Resource(name = "storageDAO")
    private StorageDAO storageDAO;


    @Override
    public StorageVO getStorageInfo(String storageId) throws Exception {
	return storageDAO.selectStorageInfo(new ParamDaoVO("storageId",storageId));
    }

    @Override
    public String getStorageId(String userId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("userId", userId);
	return storageDAO.selectStorageId(new ParamDaoVO(paramMap));
    }

}




