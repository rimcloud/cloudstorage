package kr.co.crim.oss.rimdrive.search.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.search.service.TagService;

@Service("tagService")
public class TagServiceImpl implements TagService {

    @Resource(name = "tagDAO")
    private TagDAO tagDAO;

    @Override
    public int updateTag(String storageId, String userId, long fileId, String searchTag) throws Exception{
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("storageId", storageId);
	paramMap.put("userId", userId);
	paramMap.put("fileId", fileId);
	paramMap.put("searchTag", searchTag);
	return tagDAO.updateSearchTag(new ParamDaoVO(paramMap));
    }
    
}




