package kr.co.crim.oss.rimdrive.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import kr.co.crim.oss.rimdrive.files.service.FileListVO;

public class CommonUtils {

    public static String readableFileSize(long bytesValue, int pointNumber) {

	int divideValue = 0;
	boolean checkMinusValue = false;

	if (bytesValue < 0) {
	    bytesValue = bytesValue * (-1);
	    checkMinusValue = true;
	}

	if (bytesValue < 1024) {
	    if (checkMinusValue)
		bytesValue = bytesValue * (-1);
	    return bytesValue + " B";
	}

	for (; bytesValue > 1024 * 1024; bytesValue >>= 10) {
	    divideValue++;
	}

	String strReadableFileSize = String.format("%." + pointNumber + "f %cB", bytesValue / 1024f, "kMGTPE".charAt(divideValue));

	return (checkMinusValue ? "-" + strReadableFileSize : strReadableFileSize);
    }

    public static String readableFileSize(long bytesValue) {
	return readableFileSize(bytesValue, 0);
    }

    public static long parseLong(String value) {
	long returnValue = -1;
	try {
	    if (!StringUtils.isEmpty(value)) {
		returnValue = Long.parseLong(value);
	    }
	} catch (Exception e) {
	    returnValue = -1;
	}
	return returnValue;
    }

    public static String getDisplayPath(String path) {
	String returnValue = path;

	if (StringUtils.startsWith(path, Constant.STORAGE_MOUNT_TRASH_PATH)) {
	    returnValue = StringUtils.substringAfter(path, Constant.STORAGE_MOUNT_TRASH_PATH);
	} else if (StringUtils.startsWith(path, Constant.STORAGE_MOUNT_VERSION_PATH)) {
	    returnValue = StringUtils.substringAfter(path, Constant.STORAGE_MOUNT_VERSION_PATH);
	} else if (StringUtils.startsWith(path, Constant.STORAGE_MOUNT_FILE_PATH)) {
	    returnValue = StringUtils.substringAfter(path, Constant.STORAGE_MOUNT_FILE_PATH);
	}

	return returnValue;
    }


    public static String getToDay() {
	String strFormat = "yyyyMMdd";

	return getDate(strFormat);
    }
    
    public static String getDate(String strFormat) {

	if (StringUtils.isEmpty(strFormat)) {
	    strFormat = "yyyyMMdd";
	}

	SimpleDateFormat formatter = new SimpleDateFormat(strFormat);

	return formatter.format(new Date());
    }

    public static long[] convertStringToArrayTypeLong(String strLong, String seperator, String defaultValue) {

	long[] dataArray = null;

	if (StringUtils.isEmpty(seperator)) {
	    seperator = ",";
	}

	if (!StringUtils.isEmpty(strLong)) {
	    String[] strArray = strLong.split(seperator);

	    dataArray = new long[strArray.length];
	    for (int i = 0; i < strArray.length; i++) {
		dataArray[i] = parseLong(strArray[i]);
	    }
	} else {
	    dataArray = new long[1];
	    dataArray[0] = parseLong(defaultValue);
	}

	return dataArray;

    }

    public static String convertArrayToString(String[] strArray, String delimiter, String firstValue, String lastValue, int firstIndex, int lastIndex) {

	String returnValue = "";

	int arrLength = strArray.length;

	if (lastIndex == -1) {
	    lastIndex = strArray.length;
	}

	if (arrLength > 0 && arrLength > firstIndex && arrLength >= lastIndex) {
	    returnValue = firstValue;
	    for (int i = firstIndex; i < lastIndex; i++) {
		returnValue += delimiter + strArray[i];
	    }
	    returnValue += lastValue;
	}

	return returnValue;
    }

    public static Map<String, Object> convertReqParamMapToMap(HttpServletRequest req) {

	Map<String, Object> dataMap = new HashMap<String, Object>();
	Map paramHashMap = req.getParameterMap();

	Iterator it = paramHashMap.keySet().iterator();

	while (it.hasNext()) {
	    String key = it.next().toString();
	    String[] parameters = req.getParameterValues(key);

	    if (parameters.length > 1) {
		dataMap.put(key, parameters);
	    } else {
		dataMap.put(key, req.getParameter(key));
	    }
	}

	return dataMap;
    }

    public static String set3DESParam(Map<String, Object> dataMap) {

	StringBuffer buffer = new StringBuffer();
	String encryptString = null;

	try {

	    for (String key : dataMap.keySet()) {
		if (buffer.length() > 0) {
		    buffer.append("||");
		}
		String value = (String) dataMap.get(key);
		buffer.append((key != null ? key : ""));
		buffer.append(":");
		buffer.append(value != null ? value : "");
	    }

	    String plainText = buffer.toString();
	    encryptString = CrytoUtils._encrypt(plainText);

	} catch (Exception e) {
	    encryptString = null;
	}
	return encryptString;
    }

    public static Map<String, Object> get3DESParam(String encrypt) {
	Map<String, Object> dataMap = new HashMap<String, Object>();
	try {
	    String encryptParam = CrytoUtils._decrypt(encrypt);
	    String[] strArray = encryptParam.split("\\|\\|");

	    for (int i = 0; i < strArray.length; i++) {

		String[] strMap = StringUtils.split(strArray[i], ":", 2);

		if (strMap.length == 2) {
		    dataMap.put(strMap[0], strMap[1]);
		} else if (strMap.length == 1) {
		    dataMap.put(strMap[0], "");
		}
	    }

	} catch (Exception e) {
	    dataMap = null;
	}
	return dataMap;
    }

    public static Map<String, Object> getPagingParameter(HttpServletRequest req) {
	Map<String, Object> dataMap = new HashMap<String, Object>();

	if (req != null) {
	    dataMap.put("sortId", req.getParameter("rim_sort_id"));
	    dataMap.put("sort", req.getParameter("rim_sort"));
	    dataMap.put("fromDate", req.getParameter("rim_from_date"));
	    dataMap.put("toDate", req.getParameter("rim_to_date"));
	    dataMap.put("searchId", req.getParameter("rim_search_id"));
	    dataMap.put("searchText", req.getParameter("rim_search_text"));
	    dataMap.put("limit", req.getParameter("rim_limit"));
	    dataMap.put("offset", req.getParameter("rim_offset"));
	    dataMap.put("nowPageIndex", req.getParameter("rim_now_page_index"));
	}

	return dataMap;
    }
    
    public static String getDuplicateFileName(List<?> fileList) {

	String duplicateFileNames = "";

	try {

	    for (int i = 0; i < fileList.size(); i++) {
		duplicateFileNames += ((FileListVO) fileList.get(i)).getName();
		duplicateFileNames += "<BR>";

		if (i >= 9) {
		    duplicateFileNames += "...";
		    break;
		}
	    }
	} catch (Exception e) {
	    duplicateFileNames = "";

	}

	return duplicateFileNames;
    }

    public static boolean checkSupprotDocExt(String docExt) throws Exception {

	docExt = docExt.toLowerCase();

	if (StringUtils.equalsAny(docExt, "hwp", "hwpx", "doc", "docx", "xls", "xlsx", "ppt", "pptx"))
	    return true;
	
	return false;
    }

    public static String getWebOfficeAppName(String docExt, boolean isReadOnly) {

	String appName = null;
	docExt = docExt.toLowerCase();

	if (StringUtils.equalsAny(docExt, "hwp", "doc", "docx")) {
	    if (isReadOnly)
		appName = "WRITE_VIEWER";
	    else
		appName = "WRITE_EDITOR";
	} else if (StringUtils.equalsAny(docExt, "xls", "xlsx")) {
	    if (isReadOnly)
		appName = "CALC_VIEWER";
	    else
		appName = "CALC_EDITOR";
	} else if (StringUtils.equalsAny(docExt, "ppt", "pptx")) {
	    if (isReadOnly)
		appName = "SHOW_VIEWER";
	    else
		appName = "SHOW_EDITOR";
	}

	return appName;
    }


}
