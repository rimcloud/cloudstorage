package kr.co.crim.oss.rimdrive.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

@Component("fileDownLoadView")
public class DownloadView extends AbstractView {

    private static final Logger logger = LoggerFactory.getLogger(DownloadView.class);

    public DownloadView() {
	setContentType("applicaiton/download;charset=utf-8");
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest req, HttpServletResponse res) throws Exception {

	String fileName = (String) model.get("fileName");
	File downloadFile = (File) model.get("downloadFile");

	long fileSize = 0;

	try {
	    fileSize = (long) model.get("fileSize");
	} catch (Exception e) {
	    fileSize = 0;
	}

	this.setResponseContentType(req, res);

	if (StringUtils.isEmpty(fileName) || downloadFile == null) {
	    return;
	}

	try {
	    this.setDownloadFileName(fileName, req, res);

	    fileSize = 0;
	    this.downloadFile(downloadFile, fileSize, req, res);

	} catch (Exception e) {
	    logger.error("Excetpion", e);

	    return;
	}
    }

    private void setDownloadFileName(String fileName, HttpServletRequest req, HttpServletResponse res) throws UnsupportedEncodingException {
	String userAgent = req.getHeader("User-Agent");

	if (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("Edge")) {
	    fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
	} else {
	    fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
	}

	res.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
	res.setHeader("Content-Transfer-Encoding", "binary");
	res.setHeader("Set-Cookie", "fileDownload=true; path=/");
    }

    private void downloadFile(File downloadFile, long fileSize, HttpServletRequest req, HttpServletResponse res) throws Exception {

	FileInputStream fis = new FileInputStream(downloadFile);
	OutputStream os = res.getOutputStream();

	int bytesRead = -1;

	try {
	    res.setContentLength((int) downloadFile.length());

	    int BUFFER_SIZE = 1024 * 4;
	    byte[] buf = new byte[BUFFER_SIZE];

	    while ((bytesRead = fis.read(buf)) != -1) {
		os.write(buf, 0, bytesRead);
	    }

	} catch (Exception e) {
	} finally {
	    try {
		if (fis != null)
		    fis.close();
	    } catch (IOException ioe) {
	    }
	    try {
		if (os != null)
		    os.close();
	    } catch (IOException ioe) {
	    }
	}
    }


}
