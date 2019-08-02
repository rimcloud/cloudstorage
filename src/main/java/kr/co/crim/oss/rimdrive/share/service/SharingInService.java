package kr.co.crim.oss.rimdrive.share.service;

import java.util.List;
import java.util.Map;

import kr.co.crim.oss.rimdrive.files.service.FolderInfoVO;
import kr.co.crim.oss.rimdrive.files.service.FolderSubInfoVO;

public interface SharingInService {

    public List<?> getTopList(String storageId,String userId, String deptCd , Map<String, Object> pagingMap) throws Exception;

    public List<?> getSubListByPath(String storageId, String userId, String path, String sharePath, Map<String, Object> pagingMap) throws Exception;

    public FolderSubInfoVO getFolderSubInfo(String storageId, String path) throws Exception;

    public FolderInfoVO getShareInFolderInfoBySharePath(String storageId, String userId,  String deptCd,  String sharePath) throws Exception;

}




