package kr.co.crim.oss.rimdrive.files.service;

import java.nio.file.Path;
import java.util.List;

public interface FilesValidatorService {

    public FileListVO getFileInfoByPath(String storageId, String path) throws Exception;

    public FileListVO getFileInfoByPathIgnoreCase(String storageId, String path) throws Exception;

    public List<?> getDuplcationFileInfo(String targetStorageId, String sourceStorageId, String userId, long[] fileIds, String targetPath) throws Exception;

    public TrashBinVO getTrashBinInfoByFileId(String storageId, String userId, long fileId) throws Exception;

    public List<Object> removeFileListFormFileIds(long[] fileIds, List<FileListVO> fileList) throws Exception;

    public boolean checkFileForFileSystem(String storageId, String path, String fileType) throws Exception;

    public boolean checkFileForFileSystemByPhysicalPath(String physicalPath) throws Exception;

    public boolean checkFileForFileSystemByPath(Path filePath) throws Exception;

    public boolean checkUploadForStorageSize(String storageId, long fileSize) throws Exception;

    public boolean checkRestoreForStorageSize(String userId, String storageId) throws Exception;

}
