package kr.co.crim.oss.rimdrive.search.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.service.impl.SqlSessionMetaDAO;

@Repository("searchDAO")
public class SearchDAO extends SqlSessionMetaDAO{

    public int selectTotalRows() throws Exception{
   	return (int)sqlSessionMeta.selectOne("searchDAO.selectTotalRows");
    }

    public List<?> selectList(ParamDaoVO paramDaoVO) throws Exception{
    	return sqlSessionMeta.selectList("searchDAO.selectList",paramDaoVO);
    }
    
    public int selectListTotalRows(ParamDaoVO paramDaoVO) throws Exception{
    	return  (int)sqlSessionMeta.selectOne("searchDAO.selectListTotalRows",paramDaoVO);
    }

}




