package kr.co.crim.oss.rimdrive.storage.service.impl;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.co.crim.oss.rimdrive.common.utils.Constant;
import kr.co.crim.oss.rimdrive.common.utils.FileUtil;
import kr.co.crim.oss.rimdrive.common.utils.PropertyConfigurerHelper;
import kr.co.crim.oss.rimdrive.files.service.FilesValidatorService;
import kr.co.crim.oss.rimdrive.storage.service.StorageHandlerService;

@Service("storageHandlerService")
public class StorageHandlerServiceImpl implements StorageHandlerService {
    
    @Resource(name = "filesValidatorService")
    private FilesValidatorService filesValidatorService;

    @Override
    public String getRootPath(String storageId) throws Exception {

	String rootPath = "";

	try {
	    rootPath = PropertyConfigurerHelper.getDriveRoot();

	    if (StringUtils.isNotBlank(storageId)) {
		rootPath += File.separator;
		rootPath += storageId;
	    }
	} catch (Exception e) {
	    rootPath = "";
	}

	return rootPath;
    }

    @Override
    public String getPhysicalPath(String storageId, String uri) throws Exception {

	String result = "";

	try {
	    String rootUri = getRootPath(storageId);
	    Path rootPath = Paths.get(rootUri);

	    if (Files.exists(rootPath)) {
		StringBuffer fileuri = new StringBuffer();

		fileuri.append(rootPath);
		fileuri.append(File.separator);
		fileuri.append(uri);

		Path filePath = Paths.get(fileuri.toString());
		result = filePath.toAbsolutePath().toString();
	    }
	} catch (Exception e) {
	    result = "";
	}

	return result;
    }

    @Override
    public String getPhysicalPathByFileType(String storageId, String uri, String fileType) throws Exception {

	String result = "";

	try {
	    String rootUri = getRootPath(storageId);
	    Path rootPath = Paths.get(rootUri);

	    StringBuffer fileuri = new StringBuffer();

	    fileuri.append(rootPath);
	    fileuri.append(File.separator);

	    if (Constant.FILE_TYPE_FILES.equals(fileType) && !StringUtils.startsWith(uri, Constant.STORAGE_MOUNT_FILE_PATH)) {
		fileuri.append(Constant.STORAGE_MOUNT_FILE_PATH);
	    } else if (Constant.FILE_TYPE_TRASH.equals(fileType) && !StringUtils.startsWith(uri, Constant.STORAGE_MOUNT_TRASH_PATH)) {
		fileuri.append(Constant.STORAGE_MOUNT_TRASH_PATH);
	    } else if (Constant.FILE_TYPE_VERSION.equals(fileType) && !StringUtils.startsWith(uri, Constant.STORAGE_MOUNT_VERSION_PATH)) {
		fileuri.append(Constant.STORAGE_MOUNT_VERSION_PATH);
	    } else if (Constant.FILE_TYPE_TEMP.equals(fileType) && !StringUtils.startsWith(uri, Constant.STORAGE_MOUNT_TEMP_PATH)) {
		fileuri.append(Constant.STORAGE_MOUNT_TEMP_PATH);
	    }

	    fileuri.append(File.separator);
	    fileuri.append(uri);

	    Path filePath = Paths.get(fileuri.toString());
	    result = filePath.toAbsolutePath().toString();
	} catch (Exception e) {
	    result = "";
	}

	return result;
    }

    @Override
    public String getZipDownloadPhysicalPath(String storageId, String uri) throws Exception {

	String result = "";

	try {

	    String zipUri = PropertyConfigurerHelper.getZipDownload();
	    Path zipPath = Paths.get(zipUri);

	    StringBuffer fileuri = new StringBuffer();

	    fileuri.append(zipPath);
	    fileuri.append(File.separator);
	    fileuri.append(uri);

	    if (StringUtils.isNotBlank(storageId)) {
		fileuri.append(File.separator);
		fileuri.append(storageId);
	    }

	    Path filePath = Paths.get(fileuri.toString());
	    result = filePath.toAbsolutePath().toString();

	} catch (Exception e) {
	    result = "";
	}

	return result;
    }

    @Override
    public boolean isDirectory(String physicalPath) throws Exception {

	boolean result = false;

	try {
	    Path path = Paths.get(physicalPath);

	    if (Files.isDirectory(path, NOFOLLOW_LINKS)) {
		result = true;
	    }
	} catch (Exception e) {
	    result = false;
	}

	return result;
    }

    @Override
    public boolean createStorage(String storageId) throws Exception {

	boolean result = false;

	try {
	    String path = getRootPath(storageId);
	    Path rootPath = Paths.get(path);

	    if (!Files.exists(rootPath)) {

		Files.createDirectories(rootPath.toAbsolutePath());
		result = Files.exists(rootPath);
	    } else {
		result = true;
	    }

	} catch (IOException e) {
	    result = false;
	}

	return result;
    }

    @Override
    public boolean createFolder(String folderUri) throws Exception {
	boolean result = false;

	try {
	    Path path = Paths.get(folderUri);

	    if (!Files.exists(path)) {

		Files.createDirectories(path.toAbsolutePath());
		result = Files.exists(path);
	    } else {
		result = true;
	    }
	} catch (IOException e) {
	    result = false;
	}

	return result;
    }

    @Override
    public boolean copy(String sourceUri, String targetUri) throws Exception {

	boolean result = false;

	try {
	    Path sourcePath = Paths.get(sourceUri);

	    if (Files.isDirectory(sourcePath, NOFOLLOW_LINKS)) {
		result = copyFolder(sourceUri, targetUri);
	    } else {
		result = copyFile(sourceUri, targetUri);
	    }
	} catch (Exception e) {
	    result = false;
	}

	return result;
    }

    @Override
    public boolean copyFile(String sourceUri, String targetUri) throws Exception {

	boolean result = false;

	try {

	    Path sourcePath = Paths.get(sourceUri);
	    Path targetPath = Paths.get(targetUri);

	    if (Files.isReadable(sourcePath)) {
		Files.copy(sourcePath, targetPath, COPY_ATTRIBUTES);
		result = Files.exists(targetPath);
	    }
	} catch (IOException e) {
	    result = false;
	}

	return result;
    }

    private boolean copyFolder(String sourceUri, String targetUri) throws Exception {

	boolean result = false;

	try {

	    Path sourcePath = FileSystems.getDefault().getPath(sourceUri);
	    Path targetPath = FileSystems.getDefault().getPath(targetUri);

	    File sourceFile = new File(sourceUri);
	    File targetFile = new File(targetUri);

	    if (Files.isReadable(sourcePath)) {
		FileSystemUtils.copyRecursively(sourceFile, targetFile);
		result = Files.exists(targetPath);
	    }
	} catch (IOException e) {
	    result = false;
	}

	return result;
    }

    @Override
    public boolean move(String sourceUri, String targetUri) throws Exception {
	boolean result = false;

	Path sourcePath = null;
	Path targetPath = null;

	try {
	    sourcePath = FileSystems.getDefault().getPath(sourceUri);
	    targetPath = FileSystems.getDefault().getPath(targetUri);

	    if (Files.isReadable(sourcePath)) {
		Files.move(sourcePath, targetPath, StandardCopyOption.ATOMIC_MOVE);

		result = Files.exists(targetPath);
	    }
	} catch (Exception e) {
	    result = false;
	    e.printStackTrace();
	}

	return result;
    }

    @Override
    public boolean deleteFile(String sourceUri) throws Exception {

	boolean result = false;
	try {
	    Path sourcePath = Paths.get(sourceUri);

	    if (Files.isWritable(sourcePath)) {
		Files.delete(sourcePath);
		result = true;
	    }
	} catch (Exception e) {
	    result = false;
	}

	return result;
    }

    @Override
    public boolean deleteFolder(String sourceUri) throws Exception {

	boolean result = false;
	Path sourcePath = FileSystems.getDefault().getPath(sourceUri);

	try {
	    File file = new File(sourceUri);
	    if (Files.isWritable(sourcePath)) {
		result = FileSystemUtils.deleteRecursively(file);
	    }

	} catch (Exception e) {
	    result = false;
	}

	return result;
    }

    @Override
    public boolean deleteTrashFilesFolder(String trashPath) throws Exception {

	boolean result = false;

	try {
	    File trashFile = new File(trashPath);

	    File[] innerFiles = trashFile.listFiles();

	    for (File file : innerFiles) {
		FileSystemUtils.deleteRecursively(file);
	    }
	    result = true;

	} catch (Exception e) {
	    result = false;
	}

	return result;
    }
    
    @Override
    public boolean createFile(MultipartFile multipartFile, String targetUri) throws Exception {

	boolean result = false;

	try {

	    File serverFile = new File(targetUri);

	    if (!serverFile.exists()) {

		serverFile.createNewFile();
		multipartFile.transferTo(serverFile);

		Path targetPath = FileSystems.getDefault().getPath(targetUri);
		result = Files.exists(targetPath);
	    }

	} catch (Exception e) {
	    result = false;
	}

	return result;
    }

    @Override
    public File getFile(String sourceUri) throws Exception {

	File file = null;

	try {
	    file = new File(sourceUri);

	} catch (Exception e) {
	    file = null;
	}

	return file;
    }
    
    @Override
    public File convertInputStreamToTempFile(InputStream inputStream, String fileName, String storageId) throws Exception {

	File tempfile = null;
	OutputStream outputStream = null;

	try {
	    fileName = FileUtil.getTimeStampName(fileName, Constant.FILE_TYPE_TEMP, 0);

	    String tempUri = PropertyConfigurerHelper.getDriveTemp();

	    if (filesValidatorService.checkFileForFileSystemByPhysicalPath(tempUri)) {
		tempfile = new File(FileUtil.getFilePath(tempUri, "tmp_" + fileName));

		outputStream = new FileOutputStream(tempfile);
		IOUtils.copy(inputStream, outputStream);
	    }

	} catch (Exception e) {
	    tempfile = null;
	    if (outputStream != null) {
		try {
		    outputStream.close();
		} catch (Exception ee) {
		    outputStream = null;
		}
	    }
	} finally {
	    if (outputStream != null) {
		try {
		    outputStream.close();
		} catch (Exception ee) {
		    outputStream = null;
		}
	    }
	}

	return tempfile;
    }

}
