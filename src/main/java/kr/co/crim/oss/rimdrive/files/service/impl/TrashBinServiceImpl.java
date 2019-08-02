package kr.co.crim.oss.rimdrive.files.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.common.utils.FileUtil;
import kr.co.crim.oss.rimdrive.common.utils.LoginInfoHelper;
import kr.co.crim.oss.rimdrive.files.service.FileListVO;
import kr.co.crim.oss.rimdrive.files.service.FilesService;
import kr.co.crim.oss.rimdrive.files.service.FilesValidatorService;
import kr.co.crim.oss.rimdrive.files.service.FolderSubInfoVO;
import kr.co.crim.oss.rimdrive.files.service.PagingVO;
import kr.co.crim.oss.rimdrive.files.service.TrashBinService;
import kr.co.crim.oss.rimdrive.files.service.TrashBinVO;
import kr.co.crim.oss.rimdrive.files.service.VersionService;
import kr.co.crim.oss.rimdrive.storage.service.StorageHandlerService;

@Service("trashBinService")
public class TrashBinServiceImpl implements TrashBinService {

    @Resource(name = "filesService")
    private FilesService filesService;

    @Resource(name = "filesValidatorService")
    private FilesValidatorService filesValidatorService;

    @Resource(name = "storageHandlerService")
    private StorageHandlerService storageHandlerService;

    @Resource(name = "trashBinDAO")
    private TrashBinDAO trashBinDAO;

    @Resource(name = "versionDAO")
    private VersionDAO versionDAO;

    @Resource(name = "versionService")
    private VersionService versionService;

    @Override
    public FolderSubInfoVO getFolderSubInfo(String storageId, String path, Map<String, Object> pagingMap) throws Exception {

	if (StringUtils.equals("/", path)) {
	    path = Constant.STORAGE_MOUNT_TRASH_PATH;
	} else {
	    path = Constant.STORAGE_MOUNT_TRASH_PATH + path;
	}

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("path", path);
	FolderSubInfoVO returnVO = trashBinDAO.selectFolderSubInfo(new ParamDaoVO(paramMap));
	if (returnVO == null) {
	    returnVO = new FolderSubInfoVO();
	}
	return returnVO;
    }

    @Override
    public TrashBinVO getListByFileId(String storageId, long fileId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	long[] fileIds = { fileId };
	paramMap.put("fileIds", fileIds);
	return trashBinDAO.selectTrashByFileId(new ParamDaoVO(paramMap));
    }

    @Override
    public PagingVO getListPagingByPath(String storageId, String path, Map<String, Object> pagingMap) throws Exception {
	path = FileUtil.getStoragMountPath(path);
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("path", path);
	paramMap.putAll(pagingMap);
	PagingVO paingVO = new PagingVO();
	paingVO.setResultList(trashBinDAO.selectList(new ParamDaoVO(paramMap)));
	paingVO.setTotalRows(trashBinDAO.selectTotalRowsByParam(new ParamDaoVO(paramMap)));
	return paingVO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public boolean moveFileToFileTrash(String storageId, String userId, FileListVO fileListVO, long timeStamp) throws Exception {

	boolean result = false;

	String parentPath = FileUtil.getOriginalPathFromPath(fileListVO.getPath());

	try {
	    if (filesValidatorService.getTrashBinInfoByFileId(storageId, userId, fileListVO.getFileId()) != null) {
		this.deleteTrashByFileId(storageId, fileListVO.getFileId());
	    }

	    Map<String, Object> paramMap = new HashMap<String, Object>();
	    paramMap.put("fileId", fileListVO.getFileId());
	    paramMap.put("originalName", fileListVO.getName());
	    paramMap.put("originalPath", parentPath);
	    paramMap.put("userId", userId);
	    paramMap.put("timestamp", String.valueOf(timeStamp));
	    trashBinDAO.insertFileInfo(new ParamDaoVO(paramMap));

	    String trashFileRename = FileUtil.getTimeStampName(fileListVO.getName(), Constant.FILE_TYPE_TRASH, timeStamp);
	    String trashFilesPath = FileUtil.getFilePath(Constant.STORAGE_MOUNT_TRASH_PATH, Constant.STORAGE_MOUNT_FILE_PATH);

	    result = this.trashRename(storageId, fileListVO.getFileId(), fileListVO.getFileType(), fileListVO.getPath(), trashFilesPath, trashFileRename);
	} catch (Exception e) {
	    result = false;
	}

	return result;
    }

    private boolean trashRename(String storageId, long fileId, String fileType, String sourcePath, String targetPath, String rename) throws Exception {

	boolean result = false;

	sourcePath = FileUtil.getStoragMountPath(sourcePath);
	targetPath = FileUtil.getStoragMountPath(targetPath);

	String strTargetPath = FileUtil.getFilePath(targetPath, rename);

	String physicalSourcePath = storageHandlerService.getPhysicalPathByFileType(storageId, sourcePath, Constant.FILE_TYPE_FILES);
	String physicalTargetPath = storageHandlerService.getPhysicalPathByFileType(storageId, strTargetPath, Constant.FILE_TYPE_FILES);

	boolean validFlag = filesValidatorService.checkFileForFileSystemByPhysicalPath(physicalSourcePath);
	if (validFlag) {
	    result = storageHandlerService.move(physicalSourcePath, physicalTargetPath);
	}

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("fileId", fileId);
	paramMap.put("sourcePath", sourcePath);
	paramMap.put("targetPath", targetPath);
	paramMap.put("rename", rename);

	int resultValue = trashBinDAO.updateTrashFileInfoParent(new ParamDaoVO(paramMap));

	if (resultValue > 0) {
	    if (StringUtils.equals(fileType, Constant.FILE_TYPE_DIRECTORY)) {
		trashBinDAO.updateTrashPathForSubFileByParentIdMove(new ParamDaoVO(paramMap));
	    }

	    if (!validFlag) {
		this.deleteFileByFileId(storageId, fileId);
	    }
	}

	return result;
    }

    private boolean deleteTrash(String storageId, String userId, FileListVO fileVO, long trashFilesFolderFileId) throws Exception {

	int retunDeleteTrash = 1;

	boolean result = false;

	if (fileVO.getParentId() == trashFilesFolderFileId) {
	    retunDeleteTrash = this.deleteTrashByFileId(storageId, fileVO.getFileId());
	}

	if (retunDeleteTrash > 0) {
	    if (StringUtils.equals(fileVO.getFileType(), Constant.FILE_TYPE_DIRECTORY))
		versionService.deleteAllIncludeSubFile(storageId, userId, fileVO.getFileId(), fileVO.getPath());
	    else if (StringUtils.equals(fileVO.getFileType(), Constant.FILE_TYPE_FILES))
		versionService.deleteAll(storageId, userId, fileVO.getFileId());

	    if (this.deleteFileByFileId(storageId, fileVO.getFileId()) > 0) {

		String trashFilesPath = getTrashFilesPath(storageId);
		String sourcePath = FileUtil.getFilePath(trashFilesPath, fileVO.getName());
		String sourcePhysicalPath = storageHandlerService.getPhysicalPath(storageId, sourcePath);

		if (filesValidatorService.checkFileForFileSystemByPhysicalPath(sourcePhysicalPath)) {
		    result = storageHandlerService.deleteFolder(sourcePhysicalPath);
		}
	    }
	}

	return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public List<?> delete(String storageId, String userId, long[] fileIds) throws Exception {

	long trashFilesFolderFileId = this.getTrashFilesFileId(storageId);

	@SuppressWarnings("unchecked")
	List<FileListVO> deleteFiles = (List<FileListVO>) filesService.getListByFileIds(storageId, userId, fileIds);

	List<Object> resultList = filesValidatorService.removeFileListFormFileIds(fileIds, deleteFiles);

	for (FileListVO fileVO : deleteFiles) {

	    this.deleteTrash(storageId, userId, fileVO, trashFilesFolderFileId);
	}
	return resultList;
    }

    private String checkRestoreTrashFile(String storageId, String restoreFilePath, String fileName) throws Exception {

	String ext = FileUtil.getFileExt(fileName);
	String onlyFileName = FileUtil.getOnlyFileName(fileName);
	String restoreFileName = fileName;

	for (int i = 0; i < 1000; i++) {
	    String restoreFileFullPath = FileUtil.getFilePath(restoreFilePath, restoreFileName);

	    if (filesValidatorService.checkFileForFileSystem(storageId, restoreFileFullPath, Constant.FILE_TYPE_FILES)) {
		restoreFileName = onlyFileName;
		restoreFileName += " (";
		restoreFileName += Constant.FILE_DUPLICATE_RESTORE_NAME;
		if (i > 0)
		    restoreFileName += i;
		restoreFileName += ")";
		if (!StringUtils.isBlank(ext))
		    restoreFileName += "." + ext;
	    } else {
		break;
	    }
	}

	return restoreFileName;
    }

    private boolean reStoreTrash(String storageId, TrashBinVO trashbinVO, FileListVO fileVO) throws Exception {

	boolean result = true;

	String restoreFilePath = FileUtil.getFilePath(Constant.STORAGE_MOUNT_FILE_PATH, trashbinVO.getOriginalPath());

	if (filesValidatorService.getFileInfoByPath(storageId, restoreFilePath) == null) {
	    result = filesService.makeFolderAll(storageId, LoginInfoHelper.getUserId(), restoreFilePath);
	}

	if (result) {

	    String trashFilesPath = getTrashFilesPath(storageId);
	    String sourcePath = FileUtil.getFilePath(trashFilesPath, fileVO.getName());

	    String restoreFileName = trashbinVO.getOriginalName();

	    restoreFileName = checkRestoreTrashFile(storageId, restoreFilePath, restoreFileName);
	    result = this.trashRename(storageId, trashbinVO.getFileId(), fileVO.getFileType(), sourcePath, restoreFilePath, restoreFileName);

	    if (result) {
		this.deleteTrashByFileId(storageId, trashbinVO.getFileId());
	    }
	}

	return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public List<?> reStore(String storageId, String userId, long[] fileIds) throws Exception {

	@SuppressWarnings("unchecked")
	List<FileListVO> reStoreFiles = (List<FileListVO>) filesService.getListByFileIds(storageId, userId, fileIds);

	List<Object> resultList = filesValidatorService.removeFileListFormFileIds(fileIds, reStoreFiles);

	for (FileListVO fileVO : reStoreFiles) {

	    TrashBinVO trashbinVO = this.getListByFileId(storageId, fileVO.getFileId());

	    if (trashbinVO != null) {

		this.reStoreTrash(storageId, trashbinVO, fileVO);

	    }
	}
	return resultList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public boolean deleteAll(String storageId, String userId) throws Exception {

	boolean result = false;

	String trashBinPath = FileUtil.getFilePath(Constant.STORAGE_MOUNT_TRASH_PATH, Constant.STORAGE_MOUNT_FILE_PATH);
	trashBinPath = FileUtil.getFilePath(trashBinPath, "%");

	int resultTrash = this.deleteTrashByStorageId(storageId, trashBinPath);

	int resultFile = 0;

	if (resultTrash > 0) {
	    resultFile = this.deleteFileByTrashFilesFileId(storageId, userId);
	}

	if (resultFile > 0) {
	    String trashFilesUri = this.getTrashFilesPath(storageId);
	    String trashFilesPhysicalPath = storageHandlerService.getPhysicalPath(storageId, trashFilesUri);

	    if (filesValidatorService.checkFileForFileSystemByPhysicalPath(trashFilesPhysicalPath)) {
		result = storageHandlerService.deleteTrashFilesFolder(trashFilesPhysicalPath);
	    }
	}

	return result;
    }

    private int deleteTrashByFileId(String storageId, long fileId) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("fileId", fileId);

	return trashBinDAO.deleteTrashByFileId(new ParamDaoVO(paramMap));
    }

    private int deleteTrashByStorageId(String storageId, String trashPath) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("trashPath", trashPath);

	return trashBinDAO.deleteTrashByStorageId(new ParamDaoVO(paramMap));
    }

    private int deleteFileByFileId(String storageId, long fileId) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("fileId", fileId);

	return trashBinDAO.deleteFileByFileId(new ParamDaoVO(paramMap));
    }

    private int deleteFileByTrashFilesFileId(String storageId, String userId) throws Exception {

	long trashFilesFolderFileId = getTrashFilesFileId(storageId);

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("fileId", trashFilesFolderFileId);

	return trashBinDAO.deleteFileByTrashFilesFileId(new ParamDaoVO(paramMap));
    }

    private String getTrashFilesPath(String storageId) throws Exception {

	String trashRootPath = Constant.STORAGE_MOUNT_TRASH_PATH;
	trashRootPath += Constant.FILE_SEPARATOR;
	trashRootPath += Constant.STORAGE_MOUNT_FILE_PATH;

	return trashRootPath;
    }

    private long getTrashFilesFileId(String storageId) throws Exception {

	String trashRootPath = Constant.STORAGE_MOUNT_TRASH_PATH + Constant.FILE_SEPARATOR + Constant.STORAGE_MOUNT_FILE_PATH;

	FileListVO fileListVO = filesService.getInfoByPath(storageId, trashRootPath);

	return fileListVO.getFileId();
    }

}
