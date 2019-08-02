package kr.co.crim.oss.rimdrive.files.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

public interface FilesService {

    public long getFileIdByNameIgnoreCase(String storageId, String path, String name) throws Exception;

    public FileListVO getInfoByPath(String storageId,String path) throws Exception;

    public FileListVO getInfoByPathIgnoreCase(String storageId,String path) throws Exception;

    public FileListVO getInfoByFileId(String storageId, long fileId) throws Exception;

    public FileListVO getInfoIncludeBookMarkByFileId(String storageId, long fileId, String userId) throws Exception;

    public FolderInfoVO getFolderInfo(String storageId, String path) throws Exception;

    public FolderSubInfoVO getFolderSubInfo(String storageId, String path) throws Exception;

    public FolderSubInfoVO getFolderSubInfoAll(String storageId, String path) throws Exception;
    
    public List<?> getFileListByPaths(String storageId, String[] paths) throws Exception;

    public List<?> getListExistFileByFileIds(String targetStorageId, String sourceStorageId, String userId, String targetUri, long[] fileIds) throws Exception;

    public FileListVO getInfoExistFileByName(String storageId, String userId, String targetUri, String name) throws Exception;

    public List<?> getListByPath(String storageId, String userId, String path, Map<String, Object> pagingMap) throws Exception;

    public PagingVO getListByPathPaging(String storageId, String userId, String path, Map<String, Object> pagingMap) throws Exception;

    public List<?> getListAllByPath(String storageId, String userId, String path, Map<String, Object> pagingMap) throws Exception;

    public List<?> getFolderListByParentId(String storageId, String userId, long parentId) throws Exception;

    public List<?> getFolderListByParentPath(String storageId, String userId, String path) throws Exception;

    public List<?> getFileListByParentPath(String storageId, String userId, String path) throws Exception;
    
    public List<?> getFolderListAll(String storageId, String userId) throws Exception;

    public List<?> getListByFileIds(String storageId, String userId, long[] fileIds) throws Exception;

    public List<?> getListByParentIdAndFileIds(String storageId, Long parentId, long[] fileIds) throws Exception;

    public boolean makeFolderAll(String storageId, String userId, String targetUri) throws Exception;

    public FileListVO makeFolder(String storageId, String userId, String targetUri, String folderName, boolean isRetFolderInfo) throws Exception;

    public FileListVO makeFile(String storageId, String userId, String targetUri, MultipartFile mutipartFile, boolean isRewrite) throws Exception;

    public FileListVO makeFileByFile(String storageId, String userId, String targetUri, File file, String fileName, boolean isRewrite) throws Exception;

    public FileListVO setReName(String storageId, String userId, long fileId, String parentUri, String reName, String targetFileUri) throws Exception;

    public List<?> delete(String storageId, String userId, long[] fileIds) throws Exception;

    public List<?> copy(String userId, String targetStorageId, String targetUri, String sourceStorageId, String sourceUri, long[] fileIds, String isRewrite) throws Exception;

    public List<?> move(String userId, String targetStorageId, String targetUri, String sourceStorageId, String sourceUri, long[] fileIds) throws Exception;

    public boolean moveAndRename(String userId, String targetStorageId, String targetUri, String sourceStorageId, String sourceUri, long sourceFileId, String targetReName) throws Exception;

    public int updateFileInfo(String storageId, String userId, long fileId, long size, String mimeType) throws Exception;

    public File downloadFile(String storageId, String userId, FileListVO fileListVO) throws Exception;

    public String streamDownload(ModelMap model, String storageId, String userId, long[] fileIds) throws Exception;

    public boolean makeAllSystemFolder(String storageId, String userId) throws Exception;

    public boolean makeSystemFolder(String storageId, String userId, String folderType) throws Exception;
    
}
