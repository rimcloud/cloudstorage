package kr.co.crim.oss.rimdrive.files.service.impl;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import kr.co.crim.oss.rimdrive.common.utils.FileUtil;
import kr.co.crim.oss.rimdrive.files.service.FileListVO;
import kr.co.crim.oss.rimdrive.files.service.FilesService;
import kr.co.crim.oss.rimdrive.files.service.FilesValidatorService;
import kr.co.crim.oss.rimdrive.files.service.StorageService;
import kr.co.crim.oss.rimdrive.files.service.StorageVO;
import kr.co.crim.oss.rimdrive.files.service.TrashBinService;
import kr.co.crim.oss.rimdrive.files.service.TrashBinVO;
import kr.co.crim.oss.rimdrive.share.service.SharingInService;
import kr.co.crim.oss.rimdrive.storage.service.StorageHandlerService;

@Service("filesValidatorService")
public class FilesValidatorServiceImpl implements FilesValidatorService {

    private static final LinkOption[] LINK_OPTION = { LinkOption.NOFOLLOW_LINKS };

    @Resource(name = "filesService")
    private FilesService filesService;

    @Resource(name = "trashBinService")
    private TrashBinService trashBinService;

    @Resource(name = "sharingInService")
    private SharingInService sharingInService;

    @Resource(name = "storageHandlerService")
    private StorageHandlerService storageHandlerService;

    @Resource(name = "storageService")
    private StorageService storageService;


    @Override
    public FileListVO getFileInfoByPath(String storageId, String path) throws Exception {

	return filesService.getInfoByPath(storageId, path);
    }

    @Override
    public FileListVO getFileInfoByPathIgnoreCase(String storageId, String path) throws Exception {

	return filesService.getInfoByPathIgnoreCase(storageId, path);
    }

    @Override
    public List<?> getDuplcationFileInfo(String targetStorageId, String sourceStorageId, String userId, long[] fileIds, String targetPath) throws Exception {

	String targetUri = FileUtil.getStoragMountPath(targetPath);

	return filesService.getListExistFileByFileIds(targetStorageId, sourceStorageId, userId, targetUri, fileIds);
    }

    @Override
    public TrashBinVO getTrashBinInfoByFileId(String storageId, String userId, long fileId) throws Exception {

	return trashBinService.getListByFileId(storageId, fileId);
    }

    @Override
    public List<Object> removeFileListFormFileIds(long[] fileIds, List<FileListVO> fileList) throws Exception {

	List<Object> sourceFileIdsList = new ArrayList<Object>();

	if (fileIds.length != fileList.size()) {

	    List<Object> targetFileIdsList = new ArrayList<Object>();

	    for (long fileId : fileIds)
		sourceFileIdsList.add(fileId);

	    for (FileListVO fileListVO : fileList)
		targetFileIdsList.add(fileListVO.getFileId());

	    sourceFileIdsList.removeAll(targetFileIdsList);
	}

	return sourceFileIdsList;
    }

    @Override
    public boolean checkFileForFileSystem(String storageId, String path, String fileType) throws Exception {
	String filePathStr = storageHandlerService.getPhysicalPathByFileType(storageId, path, fileType);
	Path filePath = Paths.get(filePathStr);

	return checkFileForFileSystemByPath(filePath);
    }

    @Override
    public boolean checkFileForFileSystemByPhysicalPath(String physicalPath) throws Exception {

	boolean result = false;
	Path filePath = null;

	try {
	    if (!StringUtils.isBlank(physicalPath)) {
		filePath = Paths.get(physicalPath);
		result = checkFileForFileSystemByPath(filePath);
	    }
	} catch (Exception e) {
	    result = false;
	}

	return result;
    }

    @Override
    public boolean checkFileForFileSystemByPath(Path filePath) throws Exception {
	boolean result = false;

	try {
	    if (Files.exists(filePath, LINK_OPTION))
		result = true;
	} catch (Exception e) {
	    result = false;

	}

	return result;
    }

    @Override
    public boolean checkUploadForStorageSize(String storageId, long fileSize) throws Exception {
	boolean result = true;
	StorageVO storageVO = storageService.getStorageInfo(storageId);
	long uploadableSize = storageVO.getFreeSize();

	if (fileSize > uploadableSize) {
	    result = false;
	}

	return result;
    }

    @Override
    public boolean checkRestoreForStorageSize(String userId, String storageId) throws Exception {
	boolean result = false;

	StorageVO storageVO = storageService.getStorageInfo(storageId);
	long uploadableSize = storageVO.getFreeSize();

	if (uploadableSize > 0) {
	    result = true;
	}

	return result;
    }

}
