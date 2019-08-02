package kr.co.crim.oss.rimdrive.files.service.impl;

import java.io.File;
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

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.utils.CommonUtils;
import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.common.utils.FileUtil;
import kr.co.crim.oss.rimdrive.files.service.FileListVO;
import kr.co.crim.oss.rimdrive.files.service.FilesService;
import kr.co.crim.oss.rimdrive.files.service.FilesValidatorService;
import kr.co.crim.oss.rimdrive.files.service.PagingVO;
import kr.co.crim.oss.rimdrive.files.service.VersionService;
import kr.co.crim.oss.rimdrive.files.service.VersionVO;
import kr.co.crim.oss.rimdrive.storage.service.StorageHandlerService;

@Service("versionService")
public class VersionServiceImpl implements VersionService {
    private static final Logger logger = LoggerFactory.getLogger(VersionServiceImpl.class);

    @Resource(name = "versionDAO")
    private VersionDAO versionDAO;

    @Resource(name = "filesService")
    private FilesService filesService;

    @Resource(name = "filesValidatorService")
    private FilesValidatorService filesValidatorService;

    @Resource(name = "storageHandlerService")
    private StorageHandlerService storageHandlerService;
    
    @Override
    public List<?> getListByFileId(String storageId, String userId, long fileId) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("fileId", fileId);
	return versionDAO.selectList(new ParamDaoVO(paramMap));
    }
    
    @Override
    public List<?> getListByVersionNo(String storageId, String userId, long[] versionNo) throws Exception {
	
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("versionNo", versionNo);
	return versionDAO.selectListByVersionNo(new ParamDaoVO(paramMap));
    }
    
    @Override
    public PagingVO getListPagingByStorageId(String storageId, String userId,String searchSize, Map<String, Object> pagingMap) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("searchSize", searchSize);
	paramMap.putAll(pagingMap);
	PagingVO pagingVO = new PagingVO();
	pagingVO.setResultList(versionDAO.selectListAll(new ParamDaoVO(paramMap)));
	pagingVO.setTotalRows(versionDAO.selectListAllTotalRows(new ParamDaoVO(paramMap)));
	return pagingVO;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public int deleteList(String storageId, String userId, long[] versionNo) throws Exception {
	int result = 0;
	@SuppressWarnings("unchecked")
	List<VersionVO> versionList = (List<VersionVO>) this.getListByVersionNo(storageId, userId, versionNo);

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("versionNo", versionNo);
	result = versionDAO.deleteListByVersionNo(new ParamDaoVO(paramMap));

	if (result > 0) {
	    for (VersionVO versionVO : versionList) {
		String physicalPath = storageHandlerService.getPhysicalPath(storageId, versionVO.getPath());
		if (filesValidatorService.checkFileForFileSystemByPhysicalPath(physicalPath)) {
		    storageHandlerService.deleteFile(physicalPath);
		}
	    }
	}

	return result;
    }
    
    @Override
    public List<?> getListByFileIdByOrder(String storageId, String userId, long fileId, String sort, String sortdirection) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("fileId", fileId);
	paramMap.put("sort", sort);
	paramMap.put("sortdirection", sortdirection);
	return versionDAO.selectList(new ParamDaoVO(paramMap));
    }

    @Override
    public VersionVO getVersionInfo(long versionNo, long fileId) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("versionNo", versionNo);
	paramMap.put("fileId", fileId);
	return versionDAO.selectVersionInfo(new ParamDaoVO(paramMap));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public int delete(String storageId, String userId, long versionNo, long fileId) throws Exception {
	int result = 0;
	VersionVO versionVO = this.getVersionInfo(versionNo, fileId);
	if (versionVO != null) {

	    Map<String, Object> paramMap = new HashMap<String, Object>();
	    paramMap.put("storageId", storageId);
	    paramMap.put("userId", userId);
	    paramMap.put("versionNo", versionNo);
	    paramMap.put("fileId", fileId);
	    result = versionDAO.delete(new ParamDaoVO(paramMap));

	    String physicalPath = storageHandlerService.getPhysicalPath(storageId, versionVO.getPath());
	    if (filesValidatorService.checkFileForFileSystemByPhysicalPath(physicalPath)) {
		storageHandlerService.deleteFile(physicalPath);
	    }

	}
	return result;
    }

    @Override
    public int clear(String storageId, String userId , String count) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("count", count);
	return versionDAO.clear(new ParamDaoVO(paramMap));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public int deleteAll(String storageId, String userId, long fileId) throws Exception {
	int result = 0;

	@SuppressWarnings("unchecked")
	List<VersionVO> versionList = (List<VersionVO>) this.getListByFileId(storageId, userId, fileId);

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("fileId", fileId);
	result = versionDAO.deleteAll(new ParamDaoVO(paramMap));

	if (result > 0) {
	    for (VersionVO versionVO : versionList) {
		String physicalPath = storageHandlerService.getPhysicalPath(storageId, versionVO.getPath());
		if (filesValidatorService.checkFileForFileSystemByPhysicalPath(physicalPath)) {
		    storageHandlerService.deleteFile(physicalPath);
		}
	    }
	}
	return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public int deleteAllIncludeSubFile(String storageId, String userId, long fileId, String path) throws Exception {
	int result = 0;

	if (this.getSubAllListByFileId(storageId, userId, fileId).size() > 0) {

	    Map<String, Object> paramMap = new HashMap<String, Object>();
	    paramMap.put("storageId", storageId);
	    paramMap.put("userId", userId);
	    paramMap.put("fileId", fileId);
	    result = versionDAO.deleteAllIncludeSubFile(new ParamDaoVO(paramMap));

	    if (result > 0) {
		String physicalPath = storageHandlerService.getPhysicalPathByFileType(storageId, path, Constant.FILE_TYPE_VERSION);

		if (filesValidatorService.checkFileForFileSystemByPhysicalPath(physicalPath)) {
		    storageHandlerService.deleteFolder(physicalPath);
		}
	    }
	}
	return result;
    }
    
    @Override
    public File download(String storageId, String userId, long versionNo, long fileId) throws Exception{

        VersionVO versionVO = this.getVersionInfo(versionNo, fileId);

	File file = null;
	String filePath = storageHandlerService.getPhysicalPathByFileType(storageId, versionVO.getPath(), Constant.FILE_TYPE_FILES);
	try {
	    file = storageHandlerService.getFile(filePath);
	} catch (Exception e) {
	    file = null;
	}
	return file;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public boolean reStore(String storageId, String userId, long versionNo, long fileId) throws Exception{
	boolean result = false;

	VersionVO versionVO = this.getVersionInfo(versionNo, fileId);
	FileListVO fileVO = filesService.getInfoByFileId(storageId, fileId);

	if (fileVO != null && versionVO != null) {

	    String sourcePath = storageHandlerService.getPhysicalPathByFileType(storageId, fileVO.getPath(), Constant.FILE_TYPE_FILES);

	    if (filesValidatorService.checkFileForFileSystemByPhysicalPath(sourcePath)) {

		this.makeVersion(fileVO, userId, false);

		String renameSourcePath = storageHandlerService.getPhysicalPathByFileType(storageId, FileUtil.getTimeStampName(fileVO.getPath() + Constant.FILE_RENAME_TEMP, Constant.FILE_TYPE_TEMP, 0), Constant.FILE_TYPE_FILES);

		if (storageHandlerService.move(sourcePath, renameSourcePath)) {
		    String restoreVersionPath = storageHandlerService.getPhysicalPath(storageId, versionVO.getPath());

		    if (storageHandlerService.copyFile(restoreVersionPath, sourcePath)) {
			filesService.updateFileInfo(storageId, userId, fileId, versionVO.getSize(), versionVO.getMimeType());

			this.delete(storageId, userId, versionNo, fileId);

			storageHandlerService.deleteFile(renameSourcePath);

			result = true;
		    } else {
			storageHandlerService.move(renameSourcePath, sourcePath);
		    }
		}
	    }
	}

	return result;
    }

    private boolean makeVersion(FileListVO fileVO, String userId) throws Exception {
	return makeVersion(fileVO, userId, true);
    }

    private boolean makeVersion(FileListVO fileVO, String userId, boolean maxCountCheck) throws Exception {

	long cuurentTimestamp = FileUtil.getTimeStamp();

	String newVersionFileName = FileUtil.getTimeStampName(fileVO.getName(), Constant.FILE_TYPE_VERSION, cuurentTimestamp);
	String newVersionPath = FileUtil.getRemoveFirstPath(fileVO.getPath(), Constant.FILE_TYPE_FILES);
	newVersionPath = FileUtil.getParentPathFromPath(newVersionPath);

	String newVersionFilePath = FileUtil.getFilePath(Constant.STORAGE_MOUNT_VERSION_PATH, newVersionPath);
	newVersionFilePath = FileUtil.getFilePath(newVersionFilePath, newVersionFileName);

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("fileId", fileVO.getFileId());
	paramMap.put("name", newVersionFileName);
	paramMap.put("path", newVersionFilePath);
	paramMap.put("size", fileVO.getSize());
	paramMap.put("mimeType", fileVO.getMimeType());
	paramMap.put("createUserId", userId);

	int result = versionDAO.insertVersion(new ParamDaoVO(paramMap));

	if (result > 0) {

	    @SuppressWarnings("unchecked")
	    List<VersionVO> versionList = (List<VersionVO>) this.getListByFileIdByOrder(fileVO.getStorageId(), userId, fileVO.getFileId(), "ctime", "ASC");

	    if ( maxCountCheck ){
        	    if (10 < versionList.size()) {
        		long versionNo = versionList.get(0).getVersionNo();
        		this.delete(fileVO.getStorageId(), userId, versionNo, fileVO.getFileId());
        	    }
	    }
	    
	    String sourcePhysicalPath = storageHandlerService.getPhysicalPathByFileType(fileVO.getStorageId(), fileVO.getPath(), Constant.FILE_TYPE_FILES);
	    String targetPhysicalPath = storageHandlerService.getPhysicalPathByFileType(fileVO.getStorageId(), newVersionFilePath, Constant.FILE_TYPE_VERSION);

	    this.makeVersionFolder(fileVO.getStorageId(), newVersionPath);
	    storageHandlerService.copyFile(sourcePhysicalPath, targetPhysicalPath);

	    return true;
	}

	return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public boolean makeVersion(String targetStorageId, long fileId, String userId) throws Exception {

	boolean result = false;

	FileListVO fileVO = filesService.getInfoByFileId(targetStorageId, fileId);

	if (fileVO != null && fileVO.getSize() > 0) {
	    if (filesValidatorService.checkFileForFileSystem(fileVO.getStorageId(), fileVO.getPath(), Constant.FILE_TYPE_FILES)) {
		result = this.makeVersion(fileVO, userId);
	    }
	}

	return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public boolean makeVersionIgnoreCase(String targetStorageId, long fileId, String userId, String orgFilePath, String orgFileName) throws Exception {

	boolean result = false;

	FileListVO fileVO = filesService.getInfoByFileId(targetStorageId, fileId);
	fileVO.setPath(orgFilePath);
	fileVO.setName(orgFileName);

	if (fileVO != null && fileVO.getSize() > 0) {
	    if (filesValidatorService.checkFileForFileSystem(fileVO.getStorageId(), fileVO.getPath(), Constant.FILE_TYPE_FILES)) {
		result = this.makeVersion(fileVO, userId);
	    }
	}

	return result;
    }

    private void makeVersionFolder(String storageId, String path) throws Exception {

	String versionPath = "";

	if (!StringUtils.startsWith(path, Constant.STORAGE_MOUNT_VERSION_PATH)) {
	    String versionRootPath = storageHandlerService.getPhysicalPath(storageId, Constant.STORAGE_MOUNT_VERSION_PATH);
	    versionPath = FileUtil.getFilePath(versionRootPath, path);
	} else {
	    versionPath = storageHandlerService.getPhysicalPathByFileType(storageId, path, Constant.FILE_TYPE_VERSION);
	}

	if (!filesValidatorService.checkFileForFileSystemByPhysicalPath(versionPath))
	    storageHandlerService.createFolder(versionPath);
    }

    private boolean moveVersionPhysicalFile(String sourceStorageId, String sourcePath, String targetStorageId, String targetPath) throws Exception {

	boolean result = false;

	String physicalSourcePath = storageHandlerService.getPhysicalPath(sourceStorageId, sourcePath);

	if (filesValidatorService.checkFileForFileSystemByPhysicalPath(physicalSourcePath)) {

	    String physicalTargetPath = storageHandlerService.getPhysicalPathByFileType(targetStorageId, targetPath, Constant.FILE_TYPE_VERSION);
	    String physicalParentTargetPath = FileUtil.getParentPhysicalPathFromPath(physicalTargetPath);

	    if (!filesValidatorService.checkFileForFileSystemByPhysicalPath(physicalParentTargetPath)) {
		this.makeVersionFolder(targetStorageId, FileUtil.getParentPathFromPath(targetPath));
	    }

	    result = storageHandlerService.move(physicalSourcePath, physicalTargetPath);
	}

	return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public boolean reNameVersionName(String storageId, long fileId, String targetName, String targetPath) throws Exception {

	boolean result = false;
	int lengthTimeStamp = 12;

	@SuppressWarnings("unchecked")
	List<VersionVO> versionList = (List<VersionVO>)this.getListByFileId(storageId, null, fileId);

	if (versionList != null) {

	    targetPath = FileUtil.getFilePath(Constant.STORAGE_MOUNT_VERSION_PATH, targetPath);

	    Map<String, Object> paramMap = new HashMap<String, Object>();
	    paramMap.put("fileId", fileId);
	    paramMap.put("targetName", targetName);
	    paramMap.put("targetPath", targetPath);
	    paramMap.put("lengthTimeStamp", lengthTimeStamp);

	    int updateResult = versionDAO.updateVersionFileReName(new ParamDaoVO(paramMap));

	    if (updateResult > 0) {
		for (VersionVO versionVO : versionList) {

		    String orgTimeStamp = FileUtil.getVersionTimestampFromName(versionVO.getName());
		    String orgTargetPath = FileUtil.getTimeStampName(targetPath, Constant.FILE_TYPE_VERSION, CommonUtils.parseLong(orgTimeStamp));

		    this.moveVersionPhysicalFile(storageId, versionVO.getPath(), storageId, orgTargetPath);
		}
		result = true;
	    }
	}
	return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public boolean moveVersionFilePath(String sourceStorageId, String sourcePath, String targetStorageId, String targetPath, long fileId) throws Exception {

	boolean result = false;

	@SuppressWarnings("unchecked")
	List<VersionVO> versionList = (List<VersionVO>)this.getListByFileId(sourceStorageId, null, fileId);

	if (versionList != null) {

	    sourcePath = FileUtil.getFilePath(Constant.STORAGE_MOUNT_VERSION_PATH, sourcePath);
	    targetPath = FileUtil.getFilePath(Constant.STORAGE_MOUNT_VERSION_PATH, targetPath);

	    sourcePath = FileUtil.getLastFilePathAddSeparator(sourcePath);
	    targetPath = FileUtil.getLastFilePathAddSeparator(targetPath);

	    Map<String, Object> paramMap = new HashMap<String, Object>();
	    paramMap.put("fileId", fileId);
	    paramMap.put("sourcePath", sourcePath);
	    paramMap.put("targetPath", targetPath);

	    int updateResult = versionDAO.updateVersionFilePathForMove(new ParamDaoVO(paramMap));

	    if (updateResult > 0) {

		for (VersionVO versionVO : versionList) {
		    String orgTargetPath = FileUtil.getFilePath(targetPath, versionVO.getName());

		    this.moveVersionPhysicalFile(sourceStorageId, versionVO.getPath(), targetStorageId, orgTargetPath);
		}
		result = true;
	    }
	}

	return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public boolean moveVersionFolderPath(String sourceStorageId, String sourcePath, String targetStorageId, String targetPath) throws Exception {
	boolean result = false;

	sourcePath = FileUtil.getFilePath(Constant.STORAGE_MOUNT_VERSION_PATH, sourcePath);
	targetPath = FileUtil.getFilePath(Constant.STORAGE_MOUNT_VERSION_PATH, targetPath);

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("sourcePath", sourcePath);
	paramMap.put("targetPath", targetPath);

	int updateResult = versionDAO.updateVersionFolderPathForMove(new ParamDaoVO(paramMap));

	if (updateResult > 0) {
	    this.moveVersionPhysicalFile(sourceStorageId, sourcePath, targetStorageId, targetPath);
	    result = true;
	}

	return result;
    }
    
    private List<?> getSubAllListByFileId(String storageId, String userId, long fileId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("fileId", fileId);
	return versionDAO.selectListIncludeSubFile(new ParamDaoVO(paramMap));
    }

}
