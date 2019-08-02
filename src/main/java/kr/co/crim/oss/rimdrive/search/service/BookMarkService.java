package kr.co.crim.oss.rimdrive.search.service;

import java.util.List;
import java.util.Map;

import kr.co.crim.oss.rimdrive.files.service.FileListVO;

public interface BookMarkService {

    public List<?> getListByUserId(String storageId, String userId, Map<String, Object> pagingMap) throws Exception;

    public FileListVO getInfoByFileId(String storageId, String userId, long fileId) throws Exception;

    public int insertBookMark(String storageId, String userId, long fileId) throws Exception;

    public int deleteBookMark(String storageId, String userId, long fileId) throws Exception;

}




