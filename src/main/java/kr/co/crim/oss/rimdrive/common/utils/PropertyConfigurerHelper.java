package kr.co.crim.oss.rimdrive.common.utils;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class PropertyConfigurerHelper {

    private static Properties configRimDrive;

    public PropertyConfigurerHelper(Properties configCloudrimDrive) {
	PropertyConfigurerHelper.configRimDrive = configCloudrimDrive;
    }

    public static String getValue(String key)  {
	String returnValue = "";

	try{
	    returnValue = configRimDrive.getProperty(key);
	} catch ( Exception e){
	    returnValue = "";
	}

	return returnValue;
    }

    public static String getRimdriveVersion() throws Exception {
	String version = configRimDrive.getProperty("rimdrive.version");

	if (StringUtils.isBlank(version)) {
	    return "1.0.0_community";
	}
	return version;
    }

    public static String getLogoText() throws Exception {
	String text = configRimDrive.getProperty("rimdrive.logo.text");

	if (StringUtils.isBlank(text)) {
	    return "RimDrive";
	}
	return text;
    }

    public static String getLogoPath() throws Exception {

	return "/resources/images/common/logo.png";
    }

    public static String getLoginFooter() throws Exception {

	return "C L O U D R I M @ 2 0 1 5";
    }

    public static String getTripleDESkey() throws Exception {
	return "os_key2019";
    }

    public static String getADESkey() throws Exception {
	return "os_key_spec_2019";
    }

    public static int getMaxInactiveInterval() throws Exception {
	return 86400;
    }

    public static String getDriveRoot() throws Exception {
	String rootPath = configRimDrive.getProperty("rimdrive.storage.mount.root");

	if (StringUtils.isBlank(rootPath)) {
	    return File.separator;
	}
	return rootPath;
    }

    public static String getZipDownload() throws Exception {
	String rootPath = configRimDrive.getProperty("rimdrive.storage.mount.zip.download");

	if (StringUtils.isBlank(rootPath)) {
	    return File.separator;
	}
	return rootPath;
    }

    public static String getDriveTemp() throws Exception {
	String tempPath = configRimDrive.getProperty("rimdrive.storage.mount.temp");

	if (StringUtils.isBlank(tempPath)) {
	    return File.separator;
	}
	return tempPath;
    }

    public static long getMaxUploadSize() throws Exception {
	return 4294967296L;
    }
    
    public static String getWebOfficeYn() throws Exception {
	String useWebOffice = configRimDrive.getProperty("rimdrive.external.web.office.yn");

	if (!StringUtils.equalsIgnoreCase(useWebOffice, Constant.COMMOM_YES)) {
	    useWebOffice = Constant.COMMOM_NO;
	}
	return useWebOffice;
    }
    
    public static String getWebOfficeUrl() throws Exception {
	String webOfficeUrl = configRimDrive.getProperty("rimdrive.external.web.office.url");

	if (StringUtils.isBlank(webOfficeUrl)) {
	    webOfficeUrl = "";
	}
	return webOfficeUrl;
    }

}
