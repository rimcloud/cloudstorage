package kr.co.crim.oss.rimdrive.search.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.files.service.FileListVO;
import kr.co.crim.oss.rimdrive.search.service.BookMarkService;

@Service("bookMarkService")
public class BookMarkServiceImpl implements BookMarkService {

    @Resource(name = "bookMarkDAO")
    private BookMarkDAO bookMarkDAO;

    @Override
    public List<?> getListByUserId(String storageId, String userId, Map<String, Object> pagingMap) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.putAll(pagingMap);

	return bookMarkDAO.selectList(new ParamDaoVO(paramMap));
    }

    @Override
    public FileListVO getInfoByFileId(String storageId, String userId, long fileId) throws Exception{
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("fileId", fileId);
	return bookMarkDAO.selectFileInfo(new ParamDaoVO(paramMap));
    }


    @Override
    public int insertBookMark(String storageId, String userId, long fileId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("fileId", fileId);
	return bookMarkDAO.insertBookMark(new ParamDaoVO(paramMap));
    }

    @Override
    public int deleteBookMark(String storageId, String userId, long fileId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("fileId", fileId);
	return bookMarkDAO.deleteBookMark(new ParamDaoVO(paramMap));
    }

}
