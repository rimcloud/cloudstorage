package kr.co.crim.oss.rimdrive.share.service;

import java.util.List;
import java.util.Map;

public interface SharingOutService {

    public List<?> getListByStorageId(String storageId, String userId, Map<String, Object> pagingMap) throws Exception;

    public SharingOutListVO getInfoByFileId(String storageId, String userId, long fileId) throws Exception;

    public ShareVO getShare(String storageId, long fileId, String userId) throws Exception;

    public ShareVO getShareByShareId(long shareId) throws Exception;

    public List<?> getShareTargetListByShareId(long shareId) throws Exception;

    public int addShareAll(String storageId, long fileId, String userId, String accessPw,String expireDate,String message, Map<String, Object> paramMap)throws Exception;

    public int deleteShareAll(String userId, long shareId) throws Exception;

    public int updateShareAll(String userId, long shareId,String accessPw,String expireDate,String message, Map<String, Object> paramMap) throws Exception;

}




