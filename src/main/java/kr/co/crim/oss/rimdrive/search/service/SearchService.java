package kr.co.crim.oss.rimdrive.search.service;

import java.util.Map;

import kr.co.crim.oss.rimdrive.files.service.PagingVO;

public interface SearchService {

    public PagingVO getListPaging(Map<String, Object> searchMap, Map<String, Object> pagingMap) throws Exception;
    
}




