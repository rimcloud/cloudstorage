package kr.co.crim.oss.rimdrive.search.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.service.impl.SqlSessionMetaDAO;
import kr.co.crim.oss.rimdrive.files.service.FileListVO;

@Repository("bookMarkDAO")
public class BookMarkDAO extends SqlSessionMetaDAO{

    public int selectTotalRows() throws Exception{
   	return (int)sqlSessionMeta.selectOne("bookMarkDAO.selectTotalRows");
    }

    public List<?> selectList(ParamDaoVO paramDaoVO) throws Exception{
   	return sqlSessionMeta.selectList("bookMarkDAO.selectList",paramDaoVO);
    }

    public FileListVO selectFileInfo(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("bookMarkDAO.selectFileInfo", paramDaoVO);
    }

    public int insertBookMark(ParamDaoVO paramDaoVO) throws Exception{
   	return sqlSessionMeta.insert("bookMarkDAO.insertBookMark",paramDaoVO);
    }

    public int deleteBookMark(ParamDaoVO paramDaoVO) throws Exception{
   	return sqlSessionMeta.delete("bookMarkDAO.deleteBookMark",paramDaoVO);
    }

}




