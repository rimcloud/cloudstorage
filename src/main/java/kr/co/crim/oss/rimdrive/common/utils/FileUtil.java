package kr.co.crim.oss.rimdrive.common.utils;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    static final String REGEX_POSTFIX_SERIAL_FILE = " \\(([" + Constant.FILE_COPY_SIBLING_INDEX_START + "-9]|[1-9]\\d+)(\\)(\\.\\w+)?)$";
    static final String REGEX_POSTFIX_SERIAL_FOLDER = " \\(([" + Constant.FILE_COPY_SIBLING_INDEX_START + "-9]|[1-9]\\d+)(\\))$";
    static final String REGEX_POSTFIX_ONLY_FILE = "((\\.\\w+)?)$";
    static final String REGEX_POSTFIX_ONLY_FOLDER = "$";
    static final String REGEX_EXT = "(\\.\\w+)$";

    public static Properties props = new Properties();


    public static boolean validateFileName(String name) {

	boolean result = false;

	if (name.trim().length() > 0) {
	    String charProhibited = "[\\\\/:*?\"<>|]";
	    String nameProhibited = "^((CON)|(PRN)|(AUX)|(NUL)|(COM[1-9])|(LPT[1-9]))$";
	    Pattern pChar = Pattern.compile(charProhibited, Pattern.CASE_INSENSITIVE);
	    Pattern pName = Pattern.compile(nameProhibited, Pattern.CASE_INSENSITIVE);
	    Matcher mChar = pChar.matcher(name);
	    name = StringUtils.substringBefore(name, ".");
	    Matcher mName = pName.matcher(name);

	    result = !mChar.find() && !mName.matches();
	}

	return result;
    }

    public static String digestPathHash(String path) {
	String rtnMD5 = "";

	try {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(path.getBytes("UTF-8"));
	    byte byteData[] = md.digest();
	    StringBuffer sb = new StringBuffer();

	    for (byte byteTmp : byteData) {
		sb.append(Integer.toString((byteTmp & 0xff) + 0x100, 16).substring(1));

	    }
	    rtnMD5 = sb.toString();
	} catch (Exception e) {
	    rtnMD5 = "";
	}
	return rtnMD5;
    }

    public static String getFirstPath(String path) throws Exception {
	String trimPath = FileUtil.trimFirstSlash(path);
	String[] arrayPath = trimPath.split(Constant.FILE_SEPARATOR);

	if (arrayPath.length > 0)
	    return arrayPath[0];

	return path;
    }

    public static String addFirstSplashPath(String path) throws Exception {
	if (StringUtils.isBlank(path) || path.equals(Constant.FILE_SEPARATOR)) {
	    return Constant.FILE_SEPARATOR;
	}
	int len = path.length();
	if (len == 0) {
	    return Constant.FILE_SEPARATOR;
	}
	char lastChar = path.charAt(0);
	if (lastChar != '/') {
	    return Constant.FILE_SEPARATOR + path;
	}
	return path;
    }

    public static String addLastSplashPath(String path) throws Exception {
	if (StringUtils.isBlank(path) || path.equals(Constant.FILE_SEPARATOR)) {
	    return Constant.FILE_SEPARATOR;
	}
	int len = path.length();
	if (len == 0) {
	    return Constant.FILE_SEPARATOR;
	}
	char lastChar = path.charAt(len - 1);
	if (lastChar != '/') {
	    return path + Constant.FILE_SEPARATOR;
	}
	return path;
    }

    public static String trimFirstSlash(String path) {
	int len = path.length();
	if (len == 0) {
	    return path;
	}
	char lastChar = path.charAt(0);
	if (lastChar != '/') {
	    return path;
	}

	return path.substring(1, len);
    }

    public static String trimLastSlash(String path) {
	int len = path.length();
	if (len == 0) {
	    return path;
	}
	char lastChar = path.charAt(len - 1);
	if (lastChar != '/') {
	    return path;
	}

	return path.substring(0, len - 1);
    }

    public static List<String> getParentsPath(String path) {
	if (StringUtils.startsWith(path, Constant.FILE_SEPARATOR))
	    path = StringUtils.right(path, path.length() - 1);

	String[] sa = path.split(Constant.FILE_SEPARATOR);
	List<String> pa = new ArrayList<String>();

	for (int i = 0; i < sa.length; i++) {
	    String p = "";
	    for (int j = 0; j <= i; j++) {
		p += sa[j] + (j < i ? Constant.FILE_SEPARATOR : "");
	    }
	    pa.add(p);
	}
	return pa;
    }


    public static String getOnlyFileName(String fileName) {

	int pos = fileName.lastIndexOf(".");

	if (pos > 0)
	    fileName = fileName.substring(0, pos);

	return fileName;
    }

    public static String getFileExt(String fileName) {

	return FilenameUtils.getExtension(fileName);
    }


    public static String removeTailDots(String fileName) {

	fileName = fileName.trim();

	while (fileName.endsWith(".") || fileName.endsWith(" ")) {
	    fileName = fileName.substring(0, fileName.length() - 1);
	}
	return fileName.trim();
    }

    public static String getNameFromPath(String path) {
	return FilenameUtils.getName(path);
    }

    public static String getParentPathFromPath(String path) {
	String name = "";

	if (path.lastIndexOf(Constant.FILE_SEPARATOR) <= 0) {
	    name = Constant.FILE_SEPARATOR;
	} else {
	    name = path.substring(0, path.lastIndexOf(Constant.FILE_SEPARATOR));
	}
	return name;
    }

    public static String getParentPhysicalPathFromPath(String physicalPath) {
	String name = "";

	if (StringUtils.lastIndexOf(physicalPath, File.separator) <= 0) {
	    name = File.separator;
	    ;
	} else {
	    name = StringUtils.substring(physicalPath, 0, StringUtils.lastIndexOf(physicalPath, File.separator));
	}

	return name;
    }

    public static String getOriginalPathFromPath(String path) {
	String name = "";

	if (path.lastIndexOf(Constant.FILE_SEPARATOR) < 1) {
	    name = Constant.FILE_DELIMITER_TRASHBIN;
	} else {
	    name = path.substring(1, path.lastIndexOf(Constant.FILE_SEPARATOR));
	}
	return name;
    }

    public static String getVersionTimestampFromName(String name) {
	String result = "";
	String versionDelimeter = Constant.FILE_DELIMITER_VERSION;
	String REGEXP = "(" + versionDelimeter + ")" + "(\\d{10,10})$";
	Pattern pTs = Pattern.compile(REGEXP);
	Matcher mTs = null;
	mTs = pTs.matcher(name);
	if (mTs.find()) {
	    result = mTs.group(2);
	}
	return result;
    }

    public static String stripTimeStamp(String name) {
	String result = name;
	String deleteDelimeter = Constant.FILE_DELIMITER_DELETE;
	String versionDelimeter = Constant.FILE_DELIMITER_VERSION;
	String tempDelimeter = Constant.FILE_DELIMITER_TEMP;

	String REGEXP_VERSION = "(" + versionDelimeter + "\\d{10,10})$";
	String REGEXP_DELETE = "(" + deleteDelimeter + "\\d{10,10})$";
	String REGEXP_TEMP = "(" + tempDelimeter + "\\d{10,10})$";

	Pattern pTs = Pattern.compile(REGEXP_TEMP);
	Matcher mTs = pTs.matcher(name);
	if (mTs.find()) {
	    result = name.replaceAll(mTs.group(0) + "$", "");
	    return result;
	}

	pTs = Pattern.compile(REGEXP_VERSION);
	mTs = pTs.matcher(name);
	if (mTs.find()) {
	    result = name.replaceAll(mTs.group(0) + "$", "");
	    return result;
	}

	pTs = Pattern.compile(REGEXP_DELETE);
	mTs = pTs.matcher(name);
	if (mTs.find()) {
	    result = name.replaceAll(mTs.group(0) + "$", "");
	}

	return result;
    }

    public static long getTimeStamp() {

	Date date = new Date();
	long unixTime = date.getTime() / 1000;

	return unixTime;
    }

    public static long getTimeStamp(Date date) {

	long unixTime = 0;

	if (date != null)
	    unixTime = date.getTime() / 1000;

	return unixTime;
    }

    public static String getTimeStampName(String name, String type, long unixTime) {

	if (unixTime <= 0L) {
	    unixTime = getTimeStamp();
	}

	String fileName = "";

	if (Constant.FILE_TYPE_TRASH.equals(type)) {
	    fileName = name + Constant.FILE_DELIMITER_DELETE + String.valueOf(unixTime);
	} else if (Constant.FILE_TYPE_VERSION.equals(type)) {
	    fileName = name + Constant.FILE_DELIMITER_VERSION + String.valueOf(unixTime);
	} else if (Constant.FILE_TYPE_TEMP.equals(type)) {
	    fileName = name + Constant.FILE_DELIMITER_TEMP + String.valueOf(unixTime);
	} else {
	    fileName = name + String.valueOf(unixTime);
	}

	return fileName;
    }

    public static String getRemoveFirstPath(String path, String type) {

	String newPath = "";

	if (Constant.FILE_TYPE_FILES.equals(type)) {
	    newPath = path.replaceFirst(Constant.STORAGE_MOUNT_FILE_PATH + Constant.FILE_SEPARATOR, "");
	} else if (Constant.FILE_TYPE_TRASH.equals(type)) {
	    newPath = path.replaceFirst(Constant.STORAGE_MOUNT_TRASH_PATH + Constant.FILE_SEPARATOR, "");
	} else if (Constant.FILE_TYPE_VERSION.equals(type)) {
	    newPath = path.replaceFirst(Constant.STORAGE_MOUNT_VERSION_PATH + Constant.FILE_SEPARATOR, "");
	}

	return newPath;
    }

    public static String getFilePath(String path, String fileName) {
	String pathSeparator = "";

	if (StringUtils.endsWith(path, Constant.FILE_SEPARATOR)) {
	    path = getParentPathFromPath(path);
	}

	if (StringUtils.equals(path, "") || StringUtils.equals(path, Constant.FILE_SEPARATOR)) {
	    pathSeparator = "";
	} else if (StringUtils.equals(fileName, Constant.FILE_DELIMITER_TRASHBIN)) {
	    fileName = "";
	} else if (!StringUtils.startsWith(fileName, Constant.FILE_SEPARATOR)) {
	    pathSeparator = Constant.FILE_SEPARATOR;
	}

	return path + pathSeparator + fileName;
    }

    public static String getLastFilePathAddSeparator(String path) {

	if (!StringUtils.endsWith(path, Constant.FILE_SEPARATOR)) {
	    path += Constant.FILE_SEPARATOR;
	}

	return path;
    }

    public static boolean checkPathType(String path, String type) {

	if (StringUtils.equals(Constant.FILE_TYPE_TRASH, type)) {
	    if (StringUtils.startsWith(path, Constant.STORAGE_MOUNT_TRASH_PATH) || StringUtils.startsWith(path, Constant.FILE_SEPARATOR + Constant.STORAGE_MOUNT_TRASH_PATH)) {
		return true;
	    }
	} else if (StringUtils.equals(Constant.FILE_TYPE_FILES, type)) {
	    if (StringUtils.startsWith(path, Constant.STORAGE_MOUNT_FILE_PATH) || StringUtils.startsWith(path, Constant.FILE_SEPARATOR + Constant.STORAGE_MOUNT_FILE_PATH)) {
		return true;
	    }
	} else if (StringUtils.equals(Constant.FILE_TYPE_VERSION, type)) {
	    if (StringUtils.startsWith(path, Constant.STORAGE_MOUNT_VERSION_PATH) || StringUtils.startsWith(path, Constant.FILE_SEPARATOR + Constant.STORAGE_MOUNT_VERSION_PATH)) {
		return true;
	    }
	}

	return false;
    }

    public static String getStoragMountPath(String path) {

	if (!FileUtil.checkPathType(path, Constant.FILE_TYPE_TRASH)) {
	    String pathSeparator = Constant.FILE_SEPARATOR;

	    if (StringUtils.equals(path, "") || StringUtils.equals(path, Constant.FILE_SEPARATOR)) {

		path = Constant.STORAGE_MOUNT_FILE_PATH;

	    } else if (!StringUtils.startsWith(path, Constant.STORAGE_MOUNT_FILE_PATH)) {

		if (StringUtils.startsWith(path, Constant.FILE_SEPARATOR)) {
		    path = Constant.STORAGE_MOUNT_FILE_PATH + path;
		} else {
		    path = Constant.STORAGE_MOUNT_FILE_PATH + pathSeparator + path;
		}

	    }
	}

	return path;
    }

    public static MultipartFile convertFileToMultipartFile(HttpServletRequest req, String fileName) {

	MultipartFile multipartFile = null;

	try {

	    String tempName = FileUtil.getTimeStampName(fileName, Constant.FILE_TYPE_TEMP, 0L);
	    multipartFile = new MockMultipartFile(tempName, fileName, "application/octet-stream", req.getInputStream());

	} catch (Exception e) {
	    multipartFile = null;
	}

	return multipartFile;
    }


}
