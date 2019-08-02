package kr.co.crim.oss.rimdrive.files.service;

import java.util.List;
import java.util.Map;

public interface TrashBinService {
    
    public TrashBinVO getListByFileId(String storageId, long fileIds) throws Exception;
    
    public PagingVO getListPagingByPath(String storageId, String path, Map<String, Object> pagingMap) throws Exception;
    
    public FolderSubInfoVO getFolderSubInfo(String storageId,String path, Map<String, Object> pagingMap) throws Exception;
    
    public boolean moveFileToFileTrash(String storageId, String userId, FileListVO fileListVO, long timeStamp) throws Exception;
    
    public boolean deleteAll(String storageId, String userId) throws Exception;
    
    public List<?> delete(String storageId, String userId, long[] fileIds) throws Exception;
    
    public List<?> reStore(String storageId, String userId, long[] fileIds) throws Exception;

}




