package kr.co.crim.oss.rimdrive.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import kr.co.crim.oss.rimdrive.common.utils.FileUtil;

@Component("zipFileStreamDownLoadView")
public class DownloadStreamZipView extends AbstractView {

    private static final Logger logger = LoggerFactory.getLogger(DownloadStreamZipView.class);

    private static final int COMPRESSION_LEVEL = 0;
    private static final String COMPRESSION_EXT_NAME = "zip";

    private HttpServletRequest req;
    private HttpServletResponse res;

    public DownloadStreamZipView() {
	setContentType("application/octet-stream; charset=utf-8");
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest req, HttpServletResponse res) throws Exception {

	this.req = req;
	this.res = res;

	String type = (String) model.get("type");

	File sourceFolder = null;
	List<File> filelist = null;

	if (StringUtils.equals(type, "streamDownloadAll")) {
	    filelist = (List<File>) model.get("filelist");

	    if (filelist == null || filelist.size() <= 0) {
		return;
	    }

	} else {
	    sourceFolder = (File) model.get("sourceFolder");

	    if (sourceFolder == null) {
		return;
	    }
	}

	String fileName = (String) model.get("fileName");
	File descFolder = (File) model.get("descFolder");

	if (StringUtils.isBlank(fileName) || descFolder == null) {
	    return;
	}

	this.setResponseContentType(req, res);

	try {

	    fileName = FileUtil.stripTimeStamp(fileName);
	    this.setDownloadFileName(fileName);

	    if (StringUtils.equals(type, "streamDownloadAll")) {
		this.streamZip(filelist, descFolder, fileName, "UTF-8", true);
	    } else {
		this.streamZip(sourceFolder, descFolder, fileName, Charset.defaultCharset().name(), true);
	    }

	} catch (Exception e) {
	    logger.error("Excetpion", e);
	    return;
	}
    }

    private void setDownloadFileName(String fileName) throws UnsupportedEncodingException {

	String browser = getBrowser(req);
	String disposition = getDisposition(fileName, browser);

	res.setHeader("Content-Disposition", disposition);
	res.setHeader("Content-Transfer-Encoding", "binary;");
	res.setHeader("Set-Cookie", "fileDownload=true; path=/");
	res.setHeader("Pragma", "no-cache;");
	res.setHeader("Expires", "-1;");

    }

    private String getBrowser(HttpServletRequest request) {
	String header = request.getHeader("User-Agent");

	if (header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1)
	    return "MSIE";
	else if (header.indexOf("Chrome") > -1)
	    return "Chrome";
	else if (header.indexOf("Opera") > -1)
	    return "Opera";
	return "Firefox";
    }

    private String getDisposition(String filename, String browser) throws UnsupportedEncodingException {
	String encodedFilename = null;

	if (browser.equals("MSIE")) {
	    encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
	} else if (browser.equals("Firefox")) {
	    encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
	} else if (browser.equals("Opera")) {
	    encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
	} else if (browser.equals("Chrome")) {
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < filename.length(); i++) {
		char c = filename.charAt(i);
		if (c > '~') {
		    sb.append(URLEncoder.encode("" + c, "UTF-8"));
		} else {
		    sb.append(c);
		}
	    }
	    encodedFilename = sb.toString();
	}
	return "attachment; filename=\"" + encodedFilename + "\"";
    }

    private void addToZipFile(FileInputStream fis, ZipOutputStream zos) throws Exception {

	IOUtils.copyLarge(fis, zos);
	fis.close();
	zos.closeEntry();
    }

    private void streamZip(File src, File destDir, String fileName, String charSetName, boolean includeSrc) throws Exception {

	if (fileName == null) {
	    fileName = src.getName();
	    fileName += ".";
	    fileName += COMPRESSION_EXT_NAME;

	    if (!src.isDirectory()) {
		int pos = fileName.lastIndexOf(".");
		if (pos > 0) {
		    fileName = fileName.substring(0, pos);
		}
	    }
	}

	ensureDestDir(destDir);

	File zippedFile = new File(destDir, fileName);
	if (!zippedFile.exists()) {
	    zippedFile.createNewFile();
	}

	streamFileZip(src, charSetName, includeSrc);
    }

    private void streamFileZip(File src, String charsetName, boolean includeSrc) throws Exception {

	Stack<File> stack = new Stack<File>();
	File root = null;

	if (src.isDirectory()) {
	    if (includeSrc) {
		stack.push(src);
		root = src.getParentFile();
	    } else {
		File[] fs = src.listFiles();

		for (int i = 0; i < fs.length; i++) {
		    stack.push(fs[i]);
		}
		root = src;
	    }
	} else {
	    stack.push(src);
	    root = src.getParentFile();
	}

	ZipOutputStream zos = null;

	zos = new ZipOutputStream(res.getOutputStream());

	FileInputStream fis = null;

	File f = null;

	try {

	    zos.putNextEntry(new ZipEntry("/"));

	    while (!stack.isEmpty()) {
		f = stack.pop();
		String name = toPath(root, f);

		if (f.isDirectory()) {

		    File[] fs = f.listFiles();

		    for (int i = 0; i < fs.length; i++) {
			if (fs[i].isDirectory()) {
			    stack.push(fs[i]);
			} else {
			    stack.add(0, fs[i]);
			}
		    }
		} else {
		    try {
			zos.putNextEntry(new ZipEntry(name));
			zos.setLevel(COMPRESSION_LEVEL);

			fis = new FileInputStream(f);

			if (fis != null) {
			    addToZipFile(fis, zos);
			}
		    } catch (IllegalArgumentException e) {

			logger.error("IllegalArgumentException file name : {} ", f.getAbsoluteFile(), e);
		    }
		}
	    }
	} catch (Exception e) {

	    if (zos != null) {
		try {
		    zos.closeEntry();
		    zos.close();
		} catch (Exception ee) {
		    zos = null;
		}
	    }
	    if (fis != null) {
		try {
		    fis.close();
		} catch (Exception ee) {
		    fis = null;
		}
	    }

	    logger.error("Excetpion", e);
	} finally {
	    if (zos != null) {
		try {
		    zos.closeEntry();
		    zos.close();
		} catch (Exception ee) {
		    zos = null;
		}
	    }
	    if (fis != null) {
		try {
		    fis.close();
		} catch (Exception ee) {
		    fis = null;
		}
	    }
	}
    }

    private File streamZip(List<File> src, File destDir, String fileName, String charSetName, boolean includeSrc) throws Exception {

	File zippedFile = null;

	if (src.size() > 0) {
	    if (fileName == null) {
		fileName = src.get(0).getName();
		fileName += ".";
		fileName += COMPRESSION_EXT_NAME;

		if (!src.get(0).isDirectory()) {
		    int pos = fileName.lastIndexOf(".");
		    if (pos > 0) {
			fileName = fileName.substring(0, pos);
		    }
		}
	    }

	    ensureDestDir(destDir);

	    zippedFile = new File(destDir, fileName);

	    if (!zippedFile.exists()) {
		zippedFile.createNewFile();
	    }
	    streamListZip(src, charSetName, includeSrc);
	}

	return zippedFile;
    }

    private void streamListZip(List<File> src, String charsetName, boolean includeSrc) throws Exception {

	Stack<File> stack = new Stack<File>();
	File root = null;

	if (src.size() > 0) {

	    for (int i = 0; i < src.size(); i++) {
		if (src.get(i).isDirectory()) {
		    if (includeSrc) {
			stack.push(src.get(i));
			root = src.get(i).getParentFile();
		    } else {
			File[] fs = src.get(i).listFiles();
			for (int j = 0; j < fs.length; j++) {
			    stack.push(fs[j]);
			}
			root = src.get(i);
		    }
		} else {
		    stack.push(src.get(i));
		    root = src.get(i).getParentFile();
		}
	    }
	}

	ZipOutputStream zos = null;

	zos = new ZipOutputStream(res.getOutputStream());

	FileInputStream fis = null;

	String name = "";

	try {
	    while (!stack.isEmpty()) {
		File f = stack.pop();
		name = toPath(root, f);

		if (f.isDirectory()) {
		    File[] fs = f.listFiles();
		    for (int i = 0; i < fs.length; i++) {
			if (fs[i].isDirectory())
			    stack.push(fs[i]);
			else
			    stack.add(0, fs[i]);
		    }
		} else {

		    ZipEntry ze = new ZipEntry(name);
		    zos.putNextEntry(ze);
		    zos.setLevel(COMPRESSION_LEVEL);

		    fis = new FileInputStream(f);

		    if (fis != null) {
			addToZipFile(fis, zos);
		    }
		}
	    }
	} catch (Exception e) {
	    if (zos != null) {
		try {
		    zos.closeEntry();
		    zos.close();
		} catch (Exception ee) {
		    zos = null;
		}
	    }
	    if (fis != null) {
		try {
		    fis.close();
		} catch (Exception ee) {
		    fis = null;
		}
	    }

	    logger.error("Excetpion", e);
	} finally {
	    if (zos != null) {
		try {
		    zos.closeEntry();
		    zos.close();
		} catch (Exception ee) {
		    zos = null;
		}
	    }
	    if (fis != null) {
		try {
		    fis.close();
		} catch (Exception ee) {
		    fis = null;
		}
	    }
	}
    }

    private String toPath(File root, File dir) {
	String path = dir.getAbsolutePath();
	path = path.substring(root.getAbsolutePath().length()).replace(File.separatorChar, '/');
	if (path.startsWith("/"))
	    path = path.substring(1);
	if (dir.isDirectory() && !path.endsWith("/"))
	    path += "/";
	return path;
    }

    private void ensureDestDir(File dir) throws IOException {

	if (!dir.exists()) {
	    dir.mkdirs();
	}
    }

}
