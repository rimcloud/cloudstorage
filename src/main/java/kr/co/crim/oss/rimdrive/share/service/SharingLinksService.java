package kr.co.crim.oss.rimdrive.share.service;

import java.util.List;
import java.util.Map;

public interface SharingLinksService {
    
    public List<?> getListByStorageId(String storageId, String userId, Map<String, Object> pagingMap) throws Exception;
    
    public SharingLinksListVO getInfoByFileId(String storageId, String userId, long fileId) throws Exception;
    
    public ShareVO getShare(String storageId, long fileId, String userId) throws Exception;
    
    public ShareVO getShare(String token) throws Exception;
    
    public int addShare(String storageId, long fileId, String userId) throws Exception;
    
    public int deleteShare(String userId,long shareId) throws Exception;
    
}




