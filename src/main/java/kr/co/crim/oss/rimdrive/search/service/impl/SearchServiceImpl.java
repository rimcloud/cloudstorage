package kr.co.crim.oss.rimdrive.search.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.files.service.PagingVO;
import kr.co.crim.oss.rimdrive.search.service.SearchService;

@Service("searchService")
public class SearchServiceImpl implements SearchService {

    @Resource(name = "searchDAO")
    private SearchDAO searchDAO;

    public PagingVO getListPaging(Map<String, Object> searchMap, Map<String, Object> pagingMap) throws Exception {

	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.putAll(searchMap);
	paramMap.putAll(pagingMap);

	PagingVO pagingVO = new PagingVO();
	pagingVO.setResultList(searchDAO.selectList(new ParamDaoVO(paramMap)));
	pagingVO.setTotalRows(searchDAO.selectListTotalRows(new ParamDaoVO(paramMap)));

	return pagingVO;

    }

}
