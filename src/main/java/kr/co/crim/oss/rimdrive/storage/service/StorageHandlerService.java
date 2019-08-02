package kr.co.crim.oss.rimdrive.storage.service;

import java.io.File;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface StorageHandlerService {

    public String getRootPath(String storageId) throws Exception;

    public String getPhysicalPath(String storageId, String uri) throws Exception;

    public String getPhysicalPathByFileType(String storageId, String uri, String fileType) throws Exception;

    public String getZipDownloadPhysicalPath(String storageId, String uri) throws Exception;

    public boolean isDirectory(String physicalPath) throws Exception;

    public boolean createStorage(String storageId) throws Exception;

    public boolean createFolder(String uri) throws Exception;

    public boolean copy(String sourceUri, String targetUri) throws Exception;

    public boolean copyFile(String sourceUri, String targetUri) throws Exception;

    public boolean move(String sourceUri, String targetUri) throws Exception;

    public boolean deleteFile(String sourceUri) throws Exception;

    public boolean deleteFolder(String sourceUri) throws Exception;

    public boolean deleteTrashFilesFolder(String trashPath) throws Exception;

    public boolean createFile(MultipartFile file, String targetUri) throws Exception;

    public File getFile(String sourceUri) throws Exception;

    public File convertInputStreamToTempFile(InputStream inputStream, String fileName, String storageId) throws Exception;

}
