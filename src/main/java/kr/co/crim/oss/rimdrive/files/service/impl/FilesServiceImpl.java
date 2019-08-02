package kr.co.crim.oss.rimdrive.files.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.utils.CommonUtils;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.common.utils.FileUtil;
import kr.co.crim.oss.rimdrive.files.service.FileListVO;
import kr.co.crim.oss.rimdrive.files.service.FileVO;
import kr.co.crim.oss.rimdrive.files.service.FilesService;
import kr.co.crim.oss.rimdrive.files.service.FilesValidatorService;
import kr.co.crim.oss.rimdrive.files.service.FolderInfoVO;
import kr.co.crim.oss.rimdrive.files.service.FolderSubInfoVO;
import kr.co.crim.oss.rimdrive.files.service.PagingVO;
import kr.co.crim.oss.rimdrive.files.service.TrashBinService;
import kr.co.crim.oss.rimdrive.files.service.VersionService;
import kr.co.crim.oss.rimdrive.search.service.BookMarkService;
import kr.co.crim.oss.rimdrive.search.service.TagService;
import kr.co.crim.oss.rimdrive.share.service.SharingOutService;
import kr.co.crim.oss.rimdrive.storage.service.StorageHandlerService;

@Service("filesService")
public class FilesServiceImpl implements FilesService {

    private static final Logger logger = LoggerFactory.getLogger(FilesServiceImpl.class);
    
    @Resource(name = "filesValidatorService")
    private FilesValidatorService filesValidatorService;

    @Resource(name = "filesDAO")
    private FilesDAO filesDAO;

    @Resource(name = "versionDAO")
    private VersionDAO versionDAO;

    @Resource(name = "trashBinService")
    private TrashBinService trashBinService;

    @Resource(name = "versionService")
    private VersionService versionService;

    @Resource(name = "bookMarkService")
    private BookMarkService bookMarkService;

    @Resource(name = "tagService")
    private TagService tagService;

    @Resource(name = "sharingOutService")
    private SharingOutService sharingOutService;

    @Resource(name = "storageHandlerService")
    private StorageHandlerService storageHandlerService;

    @Override
    public long getFileIdByNameIgnoreCase(String storageId, String path, String name) throws Exception {

	long returnValue = 0L;

	path = FileUtil.getStoragMountPath(path);

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("path", path);
	paramMap.put("name", name);

	try {
	    long fileId = filesDAO.selectFileIdByNameIgnoreCase(new ParamDaoVO(paramMap));
	    returnValue = fileId;
	} catch (Exception e) {
	    returnValue = 0L;
	}

	return returnValue;
    }

    @Override
    public FileListVO getInfoByPath(String storageId, String path) throws Exception {

	if (StringUtils.equals(path, Constant.FILE_SYSTEM_SEPARATOR))
	    path = Constant.FILE_SEPARATOR;
	else
	    path = FileUtil.getStoragMountPath(path);

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("path", path);

	return filesDAO.selectFileInfo(new ParamDaoVO(paramMap));
    }

    @Override
    public FileListVO getInfoByPathIgnoreCase(String storageId, String path) throws Exception {

	if (StringUtils.equals(path, Constant.FILE_SYSTEM_SEPARATOR))
	    path = Constant.FILE_SEPARATOR;
	else
	    path = FileUtil.getStoragMountPath(path);

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("path", path);

	return filesDAO.selectFileInfoIgnoreCase(new ParamDaoVO(paramMap));
    }

    @Override
    public FileListVO getInfoByFileId(String storageId, long fileId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("fileId", fileId);

	return filesDAO.selectFileInfo(new ParamDaoVO(paramMap));
    }

    @Override
    public FileListVO getInfoIncludeBookMarkByFileId(String storageId, long fileId, String userId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("fileId", fileId);

	return filesDAO.selectFileInfo(new ParamDaoVO(paramMap));
    }

    @Override
    public List<?> getListByFileIds(String storageId, String userId, long[] fileIds) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("fileIds", fileIds);

	return filesDAO.selectFileListByFileIds(new ParamDaoVO(paramMap));
    }

    @Override
    public List<?> getListByParentIdAndFileIds(String storageId, Long parentId, long[] fileIds) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("parentId", parentId);
	paramMap.put("fileIds", fileIds);

	return filesDAO.selectFileListByFileIds(new ParamDaoVO(paramMap));
    }

    @Override
    public FolderInfoVO getFolderInfo(String storageId, String path) throws Exception {

	if (StringUtils.equals(path, Constant.FILE_SYSTEM_SEPARATOR))
	    path = Constant.FILE_SEPARATOR;
	else
	    path = FileUtil.getStoragMountPath(path);

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("path", path);
	FolderInfoVO returnVO = filesDAO.selectFolderInfo(new ParamDaoVO(paramMap));
	if (returnVO == null)
	    returnVO = new FolderInfoVO();

	return returnVO;
    }

    @Override
    public FolderSubInfoVO getFolderSubInfo(String storageId, String path) throws Exception {
	path = FileUtil.getStoragMountPath(path);
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("path", path);
	FolderSubInfoVO returnVO = filesDAO.selectFolderSubInfo(new ParamDaoVO(paramMap));
	if (returnVO == null)
	    returnVO = new FolderSubInfoVO();

	return returnVO;
    }

    @Override
    public FolderSubInfoVO getFolderSubInfoAll(String storageId, String path) throws Exception {
	path = FileUtil.getStoragMountPath(path);
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("path", path);
	FolderSubInfoVO returnVO = filesDAO.selectFolderSubInfoAll(new ParamDaoVO(paramMap));
	if (returnVO == null)
	    returnVO = new FolderSubInfoVO();

	return returnVO;
    }

    @Override
    public List<?> getFileListByPaths(String storageId, String[] paths) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("paths", paths);

	return filesDAO.selectFileListByPaths(new ParamDaoVO(paramMap));
    }

    @Override
    public List<?> getListExistFileByFileIds(String targetStorageId, String sourceStorageId, String userId, String targetUri, long[] fileIds) throws Exception {

	String targetPath = FileUtil.getStoragMountPath(targetUri);

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("targetStorageId", targetStorageId);
	paramMap.put("sourceStorageId", sourceStorageId);
	paramMap.put("userId", userId);
	paramMap.put("targetPath", targetPath);
	paramMap.put("fileIds", fileIds);

	return filesDAO.selectListExistFileByFileIds(new ParamDaoVO(paramMap));
    }

    @Override
    public FileListVO getInfoExistFileByName(String storageId, String userId, String targetUri, String name) throws Exception {

	String targetPath = FileUtil.getStoragMountPath(targetUri);

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("targetPath", targetPath);
	paramMap.put("name", name);

	return filesDAO.selectInfoExistFileByName(new ParamDaoVO(paramMap));
    }

    @Override
    public List<?> getListByPath(String storageId, String userId, String path, Map<String, Object> pagingMap) throws Exception {

	path = FileUtil.getStoragMountPath(path);
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("path", path);
	paramMap.putAll(pagingMap);
	return filesDAO.selectList(new ParamDaoVO(paramMap));
    }

    @Override
    public PagingVO getListByPathPaging(String storageId, String userId, String path, Map<String, Object> pagingMap) throws Exception {

	path = FileUtil.getStoragMountPath(path);
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("path", path);
	paramMap.putAll(pagingMap);
	PagingVO pagingVO = new PagingVO();
	pagingVO.setResultList(filesDAO.selectList(new ParamDaoVO(paramMap)));
	pagingVO.setTotalRows(filesDAO.selectListTotalRows(new ParamDaoVO(paramMap)));

	return pagingVO;
    }

    @Override
    public List<?> getListAllByPath(String storageId, String userId, String path, Map<String, Object> pagingMap) throws Exception {
	path = FileUtil.getStoragMountPath(path);
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("path", path);
	paramMap.putAll(pagingMap);
	return filesDAO.selectListAll(new ParamDaoVO(paramMap));
    }

    @Override
    public List<?> getFolderListByParentId(String storageId, String userId, long parentId) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("parentId", parentId);
	paramMap.put("fileTp", Constant.FILE_TYPE_DIRECTORY);

	return filesDAO.selectList(new ParamDaoVO(paramMap));
    }


    @Override
    public List<?> getFolderListByParentPath(String storageId, String userId, String path) throws Exception {

	path = FileUtil.getStoragMountPath(path);
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("path", path);
	paramMap.put("fileTp", Constant.FILE_TYPE_DIRECTORY);

	return filesDAO.selectList(new ParamDaoVO(paramMap));
    }

    @Override
    public List<?> getFileListByParentPath(String storageId, String userId, String path) throws Exception {

	path = FileUtil.getStoragMountPath(path);
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("path", path);
	paramMap.put("fileTp", Constant.FILE_TYPE_FILES);

	return filesDAO.selectList(new ParamDaoVO(paramMap));
    }

    @Override
    public List<?> getFolderListAll(String storageId, String userId) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);

	return filesDAO.selectFolderListAll(new ParamDaoVO(paramMap));
    }

    private int deleteFileByFileId(String storageId, String userId, long fileId) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("fileId", fileId);

	return filesDAO.deleteFileByFileId(new ParamDaoVO(paramMap));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public boolean makeFolderAll(String storageId, String userId, String targetUri) throws Exception {

	boolean result = true;
	List<String> pathList = FileUtil.getParentsPath(targetUri);

	for (String path : pathList) {
	    FileListVO folderInfo = this.getInfoByPath(storageId, path);

	    if (folderInfo == null) {
		String parentPath = FileUtil.getParentPathFromPath(path);
		String folderName = FileUtil.getNameFromPath(path);

		this.makeFolder(storageId, userId, parentPath, folderName, false);
	    }
	}

	return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public FileListVO makeFolder(String storageId, String userId, String targetUri, String folderName, boolean isRetFolderInfo) throws Exception {

	String targetFullPath = null;

	if (StringUtils.equals(targetUri, Constant.FILE_SYSTEM_SEPARATOR)) {
	    targetFullPath = folderName;
	}
	else if (StringUtils.equals(targetUri, Constant.STORAGE_MOUNT_TRASH_PATH)) {
	    targetFullPath = FileUtil.getFilePath(targetUri, folderName);
	} else {
	    targetUri = FileUtil.getStoragMountPath(targetUri);
	    targetFullPath = FileUtil.getFilePath(targetUri, folderName);
	}

	FileListVO returnFolderInfo = null;

	FileListVO folderInfo = this.getInfoByPath(storageId, targetUri);

	if (folderInfo != null) {

	    long parentId = folderInfo.getFileId();

	    FileVO fileVO = new FileVO();
	    fileVO.setStorageId(storageId);
	    fileVO.setFileType(Constant.FILE_TYPE_DIRECTORY);
	    fileVO.setPath(targetFullPath);
	    fileVO.setParentId(parentId);
	    fileVO.setName(folderName);
	    fileVO.setSize(0);
	    fileVO.setCreateUserId(userId);
	    fileVO.setModifyUserId(userId);

	    int result = filesDAO.insertFileInfo(new ParamDaoVO(fileVO));

	    if (result > 0) {

		String physicalPath = storageHandlerService.getPhysicalPathByFileType(storageId, targetFullPath, Constant.FILE_TYPE_FILES);

		boolean checkResult = false;

		boolean createResult = storageHandlerService.createFolder(physicalPath);

		if (createResult) {
		    checkResult = true;
		}

		if (checkResult) {
		    if (isRetFolderInfo) {
			returnFolderInfo = this.getInfoByPath(storageId, targetFullPath);
		    } else {
			returnFolderInfo = new FileListVO();
		    }
		}
	    }
	}

	return returnFolderInfo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public FileListVO makeFile(String storageId, String userId, String targetUri, MultipartFile mutipartFile, boolean isRewrite) throws Exception {
	FileListVO retFileInfo = null;

	FileListVO folderInfo = this.getInfoByPath(storageId, targetUri);

	if (folderInfo == null) {
	    this.makeFolderAll(storageId, userId, targetUri);
	    folderInfo = this.getInfoByPath(storageId, targetUri);
	}

	if (folderInfo != null) {

	    String fileName = mutipartFile.getOriginalFilename();

	    long fileSize = mutipartFile.getSize();

	    String mimeType = mutipartFile.getContentType();
	    String targetPath = FileUtil.getStoragMountPath(targetUri);
	    String targetFile = FileUtil.getFilePath(targetPath, fileName);

	    long parentId = folderInfo.getFileId();

	    FileVO fileVO = new FileVO();
	    fileVO.setStorageId(storageId);
	    fileVO.setSize(fileSize);
	    fileVO.setMimeType(mimeType);
	    fileVO.setModifyUserId(userId);

	    int result = 0;
	    boolean isDuplicateCaseFileName = false;
	    String orgFilePath = "";

	    if (isRewrite) {
		String fileFullPath = FileUtil.getFilePath(targetUri, fileName);

		FileListVO orgfileVO = this.getInfoByPathIgnoreCase(storageId, fileFullPath);

		if (orgfileVO != null) {
		    fileVO.setFileId(orgfileVO.getFileId());

		    if (StringUtils.equals(orgfileVO.getName(), fileName)) {
			result = filesDAO.updateFileInfoSize(new ParamDaoVO(fileVO));
		    } else {
			isDuplicateCaseFileName = true;
			orgFilePath = orgfileVO.getPath();

			fileVO.setPath(targetFile);
			fileVO.setName(fileName);
			result = filesDAO.updateFileInfo(new ParamDaoVO(fileVO));
		    }

		    if (result > 0) {
			if (!isDuplicateCaseFileName)
			    versionService.makeVersion(storageId, orgfileVO.getFileId(), userId);
			else
			    versionService.makeVersionIgnoreCase(storageId, orgfileVO.getFileId(), userId, orgfileVO.getPath(), orgfileVO.getName());

		    }
		}
	    }

	    if (result == 0) {
		fileVO.setPath(targetFile);
		fileVO.setParentId(parentId);
		fileVO.setName(fileName);
		fileVO.setFileType(Constant.FILE_TYPE_FILES);
		fileVO.setCreateUserId(userId);

		result = filesDAO.insertFileInfo(new ParamDaoVO(fileVO));
	    }

	    if (result > 0) {

		String orgPhysicalPath = "";

		if (isDuplicateCaseFileName) {
		    orgPhysicalPath = storageHandlerService.getPhysicalPathByFileType(storageId, orgFilePath, Constant.FILE_TYPE_FILES);
		} else {
		    orgPhysicalPath = storageHandlerService.getPhysicalPathByFileType(storageId, targetFile, Constant.FILE_TYPE_FILES);
		}

		String tempFolderPath = FileUtil.getFilePath(Constant.STORAGE_MOUNT_FILE_PATH, CommonUtils.getToDay());
		String tempFolderPhysicalPath = storageHandlerService.getPhysicalPathByFileType(storageId, tempFolderPath, Constant.FILE_TYPE_TEMP);
		storageHandlerService.createFolder(tempFolderPhysicalPath);

		String tempfileName = FileUtil.getTimeStampName(fileName, Constant.FILE_TYPE_TEMP, 0L);
		String physicalTempPath = storageHandlerService.getPhysicalPathByFileType(storageId, FileUtil.getFilePath(tempFolderPath, tempfileName), Constant.FILE_TYPE_TEMP);
		storageHandlerService.move(orgPhysicalPath, physicalTempPath);
		storageHandlerService.deleteFile(physicalTempPath);

		String physicalPath = storageHandlerService.getPhysicalPathByFileType(storageId, targetFile, Constant.FILE_TYPE_FILES);

		boolean createResult = storageHandlerService.createFile(mutipartFile, physicalPath);

		if (createResult) {
		    if (isDuplicateCaseFileName) {
			storageHandlerService.deleteFile(orgPhysicalPath);
		    }

		    retFileInfo = this.getInfoByPath(storageId, targetFile);
		}
	    }
	}

	return retFileInfo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public FileListVO makeFileByFile(String storageId, String userId, String targetUri, File file, String fileName, boolean isRewrite) throws Exception {
	FileListVO retFileInfo = null;

	try {
	    FileListVO folderInfo = this.getInfoByPath(storageId, targetUri);

	    if (folderInfo == null) {
		this.makeFolderAll(storageId, userId, targetUri);
		folderInfo = this.getInfoByPath(storageId, targetUri);
	    }

	    if (folderInfo != null) {

		if (!isRewrite) {
		    fileName = this.getReNameDuplicateFile(storageId, userId, targetUri, fileName, "");
		}

		long fileSize = file.length();

		String mimeType = "application/octet-stream";
		String targetPath = FileUtil.getStoragMountPath(targetUri);
		String targetFile = FileUtil.getFilePath(targetPath, fileName);

		long parentId = folderInfo.getFileId();

		FileVO fileVO = new FileVO();
		fileVO.setStorageId(storageId);
		fileVO.setSize(fileSize);
		fileVO.setMimeType(mimeType);
		fileVO.setModifyUserId(userId);

		int result = 0;
		boolean isDuplicateCaseFileName = false;
		String orgFilePath = "";

		if (isRewrite) {
		    String fileFullPath = FileUtil.getFilePath(targetUri, fileName);

		    FileListVO orgfileVO = filesValidatorService.getFileInfoByPathIgnoreCase(storageId, fileFullPath);

		    if (orgfileVO != null) {
			fileVO.setFileId(orgfileVO.getFileId());

			if (StringUtils.equals(orgfileVO.getName(), fileName)) {
			    result = filesDAO.updateFileInfoSize(new ParamDaoVO(fileVO));
			} else {
			    isDuplicateCaseFileName = true;
			    orgFilePath = orgfileVO.getPath();

			    fileVO.setPath(targetFile);
			    fileVO.setName(fileName);
			    result = filesDAO.updateFileInfo(new ParamDaoVO(fileVO));
			}

			if (result > 0 && orgfileVO.getSize() > 0) {
			    if (!isDuplicateCaseFileName)
				versionService.makeVersion(storageId, orgfileVO.getFileId(), userId);
			    else
				versionService.makeVersionIgnoreCase(storageId, orgfileVO.getFileId(), userId, orgfileVO.getPath(), orgfileVO.getName());
			}
		    }
		}

		if (result == 0) {
		    fileVO.setPath(targetFile);
		    fileVO.setParentId(parentId);
		    fileVO.setName(fileName);
		    fileVO.setFileType(Constant.FILE_TYPE_FILES);
		    fileVO.setCreateUserId(userId);

		    result = filesDAO.insertFileInfo(new ParamDaoVO(fileVO));
		}

		if (result > 0) {

		    String orgPhysicalPath = "";

		    if (isDuplicateCaseFileName) {
			orgPhysicalPath = storageHandlerService.getPhysicalPathByFileType(storageId, orgFilePath, Constant.FILE_TYPE_FILES);
		    } else {
			orgPhysicalPath = storageHandlerService.getPhysicalPathByFileType(storageId, targetFile, Constant.FILE_TYPE_FILES);
		    }

		    if (filesValidatorService.checkFileForFileSystemByPhysicalPath(orgPhysicalPath)) {

			String tempFolderPath = FileUtil.getFilePath(Constant.STORAGE_MOUNT_FILE_PATH, CommonUtils.getToDay());
			String tempFolderPhysicalPath = storageHandlerService.getPhysicalPathByFileType(storageId, tempFolderPath, Constant.FILE_TYPE_TEMP);

			if (!filesValidatorService.checkFileForFileSystemByPhysicalPath(tempFolderPhysicalPath)) {
			    storageHandlerService.createFolder(tempFolderPhysicalPath);
			}

			String tempfileName = FileUtil.getTimeStampName(fileName, Constant.FILE_TYPE_TEMP, 0L);
			String physicalTempPath = storageHandlerService.getPhysicalPathByFileType(storageId, FileUtil.getFilePath(tempFolderPath, tempfileName), Constant.FILE_TYPE_TEMP);

			storageHandlerService.move(orgPhysicalPath, physicalTempPath);
			storageHandlerService.deleteFile(physicalTempPath);
		    }

		    String physicalPath = storageHandlerService.getPhysicalPathByFileType(storageId, targetFile, Constant.FILE_TYPE_FILES);

		    boolean moveResult = false;

		    moveResult = storageHandlerService.move(file.getAbsolutePath(), physicalPath);
		    
		    if (moveResult) {
			if (isDuplicateCaseFileName) {
			    storageHandlerService.deleteFile(orgPhysicalPath);
			}

			retFileInfo = this.getInfoByPath(storageId, targetFile);
		    }
		}
	    }


	} catch (Exception e) {
	    retFileInfo = null;
	    
	    e.printStackTrace();

	}
	return retFileInfo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public FileListVO setReName(String storageId, String userId, long fileId, String parentUri, String reName, String targetFileUri) throws Exception {
	FileListVO retFileInfo = null;

	FileListVO orgFileInfo = this.getInfoByFileId(storageId, fileId);
	
	if (orgFileInfo != null) {
	    String orgFilePath = storageHandlerService.getPhysicalPathByFileType(storageId, orgFileInfo.getPath(), Constant.FILE_TYPE_FILES);

	    String newPath = FileUtil.getStoragMountPath(parentUri);
	    String newFile = FileUtil.getFilePath(newPath, reName);

	    String newFilePath = storageHandlerService.getPhysicalPathByFileType(storageId, newFile, Constant.FILE_TYPE_FILES);

	    if (!StringUtils.equals(orgFilePath, newFilePath)) {
		boolean moveResult = storageHandlerService.move(orgFilePath, newFilePath);
		if (moveResult) {
		    Map<String, Object> paramMap = new HashMap<String, Object>();
		    paramMap.put("storageId", storageId);
		    paramMap.put("fileId", fileId);
		    paramMap.put("userId", userId);
		    paramMap.put("name", reName);
		    paramMap.put("path", newFile);

		    int ret = filesDAO.updateFileName(new ParamDaoVO(paramMap));

		    if (ret > 0) {
			if (StringUtils.equals(orgFileInfo.getFileType(), Constant.FILE_TYPE_DIRECTORY)) {
			    paramMap.put("oldPath", FileUtil.getStoragMountPath(orgFileInfo.getPath()));
			    filesDAO.updatePathForSubFileByParentId(new ParamDaoVO(paramMap));
			}

			if (StringUtils.equals(orgFileInfo.getFileType(), Constant.FILE_TYPE_FILES)) {
			    versionService.reNameVersionName(storageId, fileId, reName, targetFileUri);
			} else if (StringUtils.equals(orgFileInfo.getFileType(), Constant.FILE_TYPE_DIRECTORY)) {
			    versionService.moveVersionFolderPath(storageId, orgFileInfo.getPath(), storageId, targetFileUri);
			}

			retFileInfo = this.getInfoByFileId(storageId, fileId);
		    }
		}
	    }
	}
	return retFileInfo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public List<?> delete(String storageId, String userId, long[] fileIds) throws Exception {

	long timeStamp = FileUtil.getTimeStamp();

	@SuppressWarnings("unchecked")
	List<FileListVO> deleteFiles = (List<FileListVO>) this.getListByFileIds(storageId, userId, fileIds);

	List<Object> resultList = new ArrayList<Object>();

	for (FileListVO fileVO : deleteFiles) {
	    trashBinService.moveFileToFileTrash(storageId, userId, fileVO, timeStamp);
	}
	return resultList;
    }

    private boolean move(String userId, String sourceStorageId, long fileId, String targetStorageId, String sourcePath, String targetPath, String sourceName) throws Exception {

	boolean result = false;

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("sourceStorageId", sourceStorageId);
	paramMap.put("sourcePath", sourcePath + Constant.FILE_SEPARATOR + sourceName);
	paramMap.put("fileId", fileId);
	paramMap.put("targetStorageId", targetStorageId);
	paramMap.put("targetPath", targetPath);
	paramMap.put("modifyUserId", userId);

	int resultValue = filesDAO.updateFileInfoParent(new ParamDaoVO(paramMap));

	if (resultValue > 0) {
	    filesDAO.updatePathForSubFileByParentIdMove(new ParamDaoVO(paramMap));

	    String sourceFullPath = FileUtil.getFilePath(sourcePath, sourceName);
	    String targetFullPath = FileUtil.getFilePath(targetPath, sourceName);

	    String physicalSourcePath = storageHandlerService.getPhysicalPathByFileType(sourceStorageId, sourceFullPath, Constant.FILE_TYPE_FILES);
	    String physicalTargetPath = storageHandlerService.getPhysicalPathByFileType(targetStorageId, targetFullPath, Constant.FILE_TYPE_FILES);

	    storageHandlerService.move(physicalSourcePath, physicalTargetPath);
	}

	return result;
    }
    
    private boolean copy(String userId, String sourceStorageId, String sourcePath, long fileId, String targetStorageId, String targetPath, String sourceFileName, String actionType) throws Exception {
	boolean result = false;

	Map<String, Object> paramMap = new HashMap<String, Object>();
	String sourceFullPath = FileUtil.getFilePath(sourcePath, sourceFileName);
	String targetFullPath = FileUtil.getFilePath(targetPath, sourceFileName);

	if (StringUtils.equalsIgnoreCase(Constant.FILE_ACTION_TYPE_NEWNAME, actionType)) {
	    sourceFileName = this.getReNameDuplicateFile(targetStorageId, userId, targetPath, sourceFileName, Constant.FILE_COPY_POSTFIX);
	    targetFullPath = FileUtil.getFilePath(targetPath, sourceFileName);

	    paramMap.put("fileNm", sourceFileName);
	    paramMap.put("sourcePath", sourceFullPath);
	    paramMap.put("targetPath", targetFullPath);
	} else if (StringUtils.equalsIgnoreCase(Constant.FILE_ACTION_TYPE_REWRITE, actionType)) {

	    if (filesValidatorService.getFileInfoByPath(targetStorageId, targetFullPath) != null) {
		deleteFileByFileId(targetStorageId, userId, fileId);
	    }
	    paramMap.put("sourcePath", sourcePath);
	    paramMap.put("targetPath", targetPath);
	} else {
	    paramMap.put("sourcePath", sourcePath);
	    paramMap.put("targetPath", targetPath);
	}

	paramMap.put("sourceStorageId", sourceStorageId);
	paramMap.put("fileId", fileId);
	paramMap.put("targetStorageId", targetStorageId);
	paramMap.put("modifyUserId", userId);

	int resultValue = filesDAO.insertCopyIncloudSubFile(new ParamDaoVO(paramMap));

	if (resultValue > 0) {
	    filesDAO.updateParentIdByPathForCopy(new ParamDaoVO(paramMap));

	    String physicalSourcePath = storageHandlerService.getPhysicalPathByFileType(sourceStorageId, sourceFullPath, Constant.FILE_TYPE_FILES);
	    String physicalTargetPath = storageHandlerService.getPhysicalPathByFileType(targetStorageId, targetFullPath, Constant.FILE_TYPE_FILES);

	    boolean copyResult = storageHandlerService.copy(physicalSourcePath, physicalTargetPath);

	    if (copyResult) {
		result = true;
	    }
	}
	
	return result;
    }
    
    private String getReNameDuplicateFile(String storageId, String userId, String path, String fileName, String postFix) throws Exception {

	String ext = FileUtil.getFileExt(fileName);
	String onlyFileName = FileUtil.getOnlyFileName(fileName);
	String renameFileName = fileName;

	for (int i = 0; i < 1000; i++) {
	    String renameFileFullPath = FileUtil.getFilePath(path, renameFileName);

	    if (filesValidatorService.getFileInfoByPath(storageId, renameFileFullPath) != null) {
		renameFileName = onlyFileName;
		renameFileName += " (";
		if (StringUtils.isNotBlank(postFix)) {
		    renameFileName += postFix;
		    if (i > 0)
			renameFileName += i;
		} else {
		    renameFileName += (i + 1);
		}
		renameFileName += ")";
		if (StringUtils.isNotBlank(ext)) {
		    renameFileName += ".";
		    renameFileName += ext;
		}
	    } else {
		break;
	    }
	}

	return renameFileName;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public List<?> copy(String userId, String targetStorageId, String targetUri, String sourceStorageId, String sourceUri, long[] fileIds, String actionType) throws Exception {

	String sourcePath = FileUtil.getStoragMountPath(sourceUri);
	String targetPath = FileUtil.getStoragMountPath(targetUri);

	@SuppressWarnings("unchecked")
	List<FileListVO> copyFiles = (List<FileListVO>) this.getListByFileIds(sourceStorageId, userId, fileIds);

	for (FileListVO fileListVO : copyFiles) {
	    this.copy(userId, sourceStorageId, sourcePath, fileListVO.getFileId(), targetStorageId, targetPath, fileListVO.getName(), actionType);
	}

	return new ArrayList<>();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public List<?> move(String userId, String targetStorageId, String targetUri, String sourceStorageId, String sourceUri, long[] fileIds) throws Exception {

	String sourcePath = FileUtil.getStoragMountPath(sourceUri);
	String targetPath = FileUtil.getStoragMountPath(targetUri);

	@SuppressWarnings("unchecked")
	List<FileListVO> moveFiles = (List<FileListVO>) this.getListByFileIds(sourceStorageId, null, fileIds);

	if (moveFiles != null && moveFiles.size() > 0) {

	    for (FileListVO sourceFileVO : moveFiles) {

		this.move(userId, sourceStorageId, sourceFileVO.getFileId(), targetStorageId, sourcePath, targetPath, sourceFileVO.getName());
	    }
	}
	return new ArrayList<>();
    }

    private boolean moveAndRename(String userId, String sourceStorageId, long fileId, String targetStorageId, String sourcePath, String targetPath, String sourceName, String reName) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("sourceStorageId", sourceStorageId);
	paramMap.put("sourcePath", sourcePath + "/" + sourceName);
	paramMap.put("fileId", fileId);
	paramMap.put("targetStorageId", targetStorageId);
	paramMap.put("targetPath", targetPath);
	paramMap.put("reName", reName);
	paramMap.put("modifyUserId", userId);

	int resultValue = filesDAO.updateRenameFileInfoParent(new ParamDaoVO(paramMap));

	if (resultValue > 0) {
	    filesDAO.updatePathForSubFileByParentIdMove(new ParamDaoVO(paramMap));

	    String sourceFullPath = FileUtil.getFilePath(sourcePath, sourceName);
	    String targetFullPath = FileUtil.getFilePath(targetPath, reName);

	    String physicalSourcePath = storageHandlerService.getPhysicalPathByFileType(sourceStorageId, sourceFullPath, Constant.FILE_TYPE_FILES);
	    String physicalTargetPath = storageHandlerService.getPhysicalPathByFileType(targetStorageId, targetFullPath, Constant.FILE_TYPE_FILES);

	    storageHandlerService.move(physicalSourcePath, physicalTargetPath);
	}

	return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public boolean moveAndRename(String userId, String targetStorageId, String targetUri, String sourceStorageId, String sourceUri, long sourceFileId, String targetReName) throws Exception {

	boolean result = false;

	String sourcePath = FileUtil.getStoragMountPath(sourceUri);
	String targetPath = FileUtil.getStoragMountPath(targetUri);

	FileListVO sourceFileVO = this.getInfoByFileId(sourceStorageId, sourceFileId);

	if (sourceFileVO != null) {
	    String targetFullPath = FileUtil.getFilePath(targetPath, targetReName);
	    FileListVO targetFileVO =  this.getInfoByPath(targetStorageId, targetFullPath);

	    if (targetFileVO == null) {

		boolean moveResult = this.moveAndRename(userId, sourceStorageId, sourceFileVO.getFileId(), targetStorageId, sourcePath, targetPath, sourceFileVO.getName(), targetReName);

		if (moveResult) {

		    if (StringUtils.equals(sourceFileVO.getFileType(), Constant.FILE_TYPE_FILES)) {
			if (StringUtils.equals(sourcePath, targetPath)) {
			    targetUri = FileUtil.getFilePath(targetUri, targetReName);
			    versionService.reNameVersionName(sourceStorageId, sourceFileVO.getFileId(), targetReName, targetUri);
			} else {
			    versionService.moveVersionFilePath(sourceStorageId, FileUtil.addFirstSplashPath(sourceUri), targetStorageId, targetUri, sourceFileVO.getFileId());
			}
		    } else if (StringUtils.equals(sourceFileVO.getFileType(), Constant.FILE_TYPE_DIRECTORY)) {
			sourceUri = FileUtil.getFilePath(FileUtil.addFirstSplashPath(sourceUri), sourceFileVO.getName());
			targetUri = FileUtil.getFilePath(targetUri, sourceFileVO.getName());
			versionService.moveVersionFolderPath(sourceStorageId, FileUtil.addFirstSplashPath(sourceUri), targetStorageId, targetUri);
		    }

		    result = true;
		}
	    }

	}
	return result;
    }

    @Override
    public int updateFileInfo(String storageId, String userId, long fileId, long size, String mimeType) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("fileId", fileId);
	paramMap.put("size", size);
	paramMap.put("mimeType", mimeType);
	paramMap.put("modifyUserId", userId);

	return filesDAO.updateFileInfoSize(new ParamDaoVO(paramMap));
    }

    @Override
    public File downloadFile(String storageId, String userId, FileListVO fileListVO) throws Exception {
	String physicalFilePath = storageHandlerService.getPhysicalPathByFileType(storageId, fileListVO.getPath(), Constant.FILE_TYPE_FILES);

	return storageHandlerService.getFile(physicalFilePath);
    }

    @Override
    public String streamDownload(ModelMap model, String storageId, String userId, long[] fileIds) throws Exception {

	@SuppressWarnings("unchecked")
	List<FileListVO> downloadFiles = (List<FileListVO>) this.getListByFileIds(storageId, userId, fileIds);

	String resultView = "";

	try {

	    if (downloadFiles.size() == 1) {
		FileListVO fileListVO = downloadFiles.get(0);
		if (fileListVO.getFileType().equals(Constant.FILE_TYPE_DIRECTORY)) {
		    resultView = this.streamDownloadDirectory(model, storageId, userId, fileListVO);
		} else {
		    resultView = this.streamDownloadFile(model, storageId, userId, fileListVO);
		}
	    } else {
		resultView = this.streamDownloadAll(model, storageId, userId, downloadFiles);
	    }

	} catch (Exception e) {
	    resultView = "";
	}

	return resultView;
    }

    private String streamDownloadFile(ModelMap model, String storageId, String userId, FileListVO fileListVO) throws Exception {
	String resultView = "";
	String physicalFilePath = storageHandlerService.getPhysicalPathByFileType(storageId, fileListVO.getPath(), Constant.FILE_TYPE_FILES);

	File file = storageHandlerService.getFile(physicalFilePath);

	if (file != null) {
	    model.put("fileName", file.getName());
	    model.put("downloadFile", file);
	    model.put("fileSize", fileListVO.getSize());

	    resultView = "fileDownLoadView";
	}
	return resultView;
    }

    private String streamDownloadDirectory(ModelMap model, String storageId, String userId, FileListVO fileListVO) throws Exception {

	String resultView = "";

	String sourceFolderPath = storageHandlerService.getPhysicalPathByFileType(storageId, fileListVO.getPath(), Constant.FILE_TYPE_FILES);

	String descFolderPath = FileUtil.getFilePath(Constant.STORAGE_MOUNT_FILE_PATH, CommonUtils.getToDay());
	String descFolderPhysicalPath = storageHandlerService.getZipDownloadPhysicalPath(storageId, descFolderPath);

	storageHandlerService.createFolder(descFolderPhysicalPath);

	String fileName = FileUtil.getTimeStampName(fileListVO.getName() + ".zip", Constant.FILE_TYPE_TEMP, 0L);
	File sourceFolder = storageHandlerService.getFile(sourceFolderPath);
	File descFolder = storageHandlerService.getFile(descFolderPhysicalPath);

	model.put("fileName", fileName);
	model.put("sourceFolder", sourceFolder);
	model.put("descFolder", descFolder);
	model.put("type", "streamDownloadDirectory");

	resultView = "zipFileStreamDownLoadView";

	return resultView;
    }

    private String streamDownloadAll(ModelMap model, String storageId, String userId, List<FileListVO> downloadFiles) throws Exception {

	String resultView = "";
	List<File> filelist = new ArrayList<File>();

	for (FileListVO fileListVO : downloadFiles) {
	    String sourceFolderPath = storageHandlerService.getPhysicalPathByFileType(storageId, fileListVO.getPath(), Constant.FILE_TYPE_FILES);

	    filelist.add(new File(sourceFolderPath));
	}

	if (filelist.size() > 0) {
	    String descFolderPath = FileUtil.getFilePath(Constant.STORAGE_MOUNT_FILE_PATH, CommonUtils.getToDay());
	    String descFolderPhysicalPath = storageHandlerService.getZipDownloadPhysicalPath(storageId, descFolderPath);

	    storageHandlerService.createFolder(descFolderPhysicalPath);

	    String fileName = "";
	    String parentFolderPath = FileUtil.getParentPathFromPath(downloadFiles.get(0).getPath());

	    if (Constant.FILE_SEPARATOR.equals(parentFolderPath)) {

		String filePhysicalPath = storageHandlerService.getPhysicalPathByFileType(storageId, downloadFiles.get(0).getPath(), Constant.FILE_TYPE_FILES);

		if (storageHandlerService.isDirectory(filePhysicalPath))
		    fileName = downloadFiles.get(0).getName();
		else
		    fileName = FileUtil.getOnlyFileName(downloadFiles.get(0).getName());

		fileName += ".zip";
		fileName = FileUtil.getTimeStampName(fileName, Constant.FILE_TYPE_TEMP, 0L);
	    } else {
		fileName = FileUtil.getTimeStampName(FileUtil.getNameFromPath(parentFolderPath) + ".zip", Constant.FILE_TYPE_TEMP, 0L);
	    }

	    File descFolder = storageHandlerService.getFile(descFolderPhysicalPath);

	    model.put("fileName", fileName);
	    model.put("filelist", filelist);
	    model.put("descFolder", descFolder);
	    model.put("type", "streamDownloadAll");

	    resultView = "zipFileStreamDownLoadView";
	}

	return resultView;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public boolean makeAllSystemFolder(String storageId, String userId) throws Exception {
	boolean makeResult = false;

	if (this.makeSystemFolder(storageId, userId, Constant.FILE_TYPE_ROOT)) {
	    if (this.makeSystemFolder(storageId, userId, Constant.FILE_TYPE_FILES)) {
		if (this.makeSystemFolder(storageId, userId, Constant.FILE_TYPE_TRASH)) {
		    makeResult = true;
		}
	    }
	}

	return makeResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public boolean makeSystemFolder(String storageId, String userId, String folderType) throws Exception {

	boolean makeResult = false;

	if (StringUtils.equals(Constant.FILE_TYPE_ROOT, folderType)) {

	    if (this.getInfoByPath(storageId, Constant.FILE_SEPARATOR) == null) {
		if (this.insertRootFolder(storageId, userId) > 0) {
		    if (storageHandlerService.createStorage(storageId)) {
			makeResult = true;
		    }
		}
	    } else {
		if (storageHandlerService.createStorage(storageId)) {
		    makeResult = true;
		}
	    }

	} else if (StringUtils.equals(Constant.FILE_TYPE_FILES, folderType)) {

	    String filesfolderName = Constant.STORAGE_MOUNT_FILE_PATH;

	    if (this.getInfoByPath(storageId, filesfolderName) == null) {
		if (this.makeFolder(storageId, userId, Constant.FILE_SYSTEM_SEPARATOR, filesfolderName, false) != null) {
		    makeResult = true;
		}
	    } else {

		String filesPhysicalPath = storageHandlerService.getPhysicalPathByFileType(storageId, Constant.FILE_SEPARATOR, Constant.FILE_TYPE_FILES);

		makeResult = storageHandlerService.createFolder(filesPhysicalPath);
	    }

	} else if (StringUtils.equals(Constant.FILE_TYPE_TRASH, folderType)) {

	    boolean trashMakeResult = true;
	    String trashbinfolderName = Constant.STORAGE_MOUNT_TRASH_PATH;

	    if (this.getInfoByPath(storageId, trashbinfolderName) == null) {

		if (this.makeFolder(storageId, userId, Constant.FILE_SYSTEM_SEPARATOR, trashbinfolderName, false) == null)
		    trashMakeResult = false;

	    } else {
		String trashbinPhysicalPath = storageHandlerService.getPhysicalPathByFileType(storageId, Constant.FILE_SEPARATOR, Constant.FILE_TYPE_TRASH);

		trashMakeResult = storageHandlerService.createFolder(trashbinPhysicalPath);
	    }

	    if (trashMakeResult) {

		String trashbinFilesfolderName = Constant.STORAGE_MOUNT_FILE_PATH;

		if (this.getInfoByPath(storageId, FileUtil.getFilePath(trashbinfolderName, trashbinFilesfolderName)) == null) {

		    if (this.makeFolder(storageId, userId, trashbinfolderName, trashbinFilesfolderName, false) != null) {
			makeResult = true;
		    }

		} else {
		    String trashbinFilesPhysicalPath = storageHandlerService.getPhysicalPathByFileType(storageId, Constant.STORAGE_MOUNT_FILE_PATH, Constant.FILE_TYPE_TRASH);

		    makeResult = storageHandlerService.createFolder(trashbinFilesPhysicalPath);
		}
	    }
	}

	return makeResult;
    }

    private int insertRootFolder(String storageId, String userId) throws Exception {

	long parentId = -1;

	FileVO fileVO = new FileVO();
	fileVO.setStorageId(storageId);
	fileVO.setFileType(Constant.FILE_TYPE_DIRECTORY);
	fileVO.setPath(Constant.FILE_SEPARATOR);
	fileVO.setParentId(parentId);
	fileVO.setName(null);
	fileVO.setSize(0);
	fileVO.setCreateUserId(userId);
	fileVO.setModifyUserId(userId);

	return filesDAO.insertFileInfo(new ParamDaoVO(fileVO));
    }

}
