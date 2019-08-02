package kr.co.crim.oss.rimdrive.files.service;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface VersionService {

    public VersionVO getVersionInfo(long versionNo, long fileId) throws Exception;

    public List<?> getListByFileId(String storageId, String userId, long fileId) throws Exception;
    
    public List<?> getListByVersionNo(String storageId, String userId, long[] versionNo) throws Exception;

    public PagingVO getListPagingByStorageId(String storageId, String userId,String searchSize, Map<String, Object> pagingMap) throws Exception;

    public int deleteList(String storageId, String userId, long[] versionNo) throws Exception;
    
    public List<?> getListByFileIdByOrder(String storageId, String userId, long fileId, String sort, String sortdirection) throws Exception;

    public int delete(String storageId, String userId, long versionNo, long fileId) throws Exception;

    public int clear(String storageId, String userId, String count) throws Exception;

    public int deleteAll(String storageId, String userId, long fileId) throws Exception;

    public int deleteAllIncludeSubFile(String storageId, String userId, long fileId, String path) throws Exception;

    public File download(String storageId, String userId, long versionNo, long fileId) throws Exception;

    public boolean reStore(String storageId, String userId, long versionNo, long fileId) throws Exception;

    public boolean makeVersion(String targetStorageId, long fileId, String userId) throws Exception;

    public boolean makeVersionIgnoreCase(String targetStorageId, long fileId, String userId, String orgFilePath, String orgFileName) throws Exception;

    public boolean reNameVersionName(String storageId, long fileId, String targetName, String targetPath) throws Exception;

    public boolean moveVersionFilePath(String sourceStorageId, String sourcePath, String targetStorageId, String targetPath, long fileId) throws Exception;

    public boolean moveVersionFolderPath(String sourceStorageId, String sourcePath, String targetStorageId, String targetPath) throws Exception;
    
}