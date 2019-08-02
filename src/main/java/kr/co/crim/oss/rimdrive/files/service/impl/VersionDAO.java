package kr.co.crim.oss.rimdrive.files.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.service.impl.SqlSessionMetaDAO;
import kr.co.crim.oss.rimdrive.files.service.VersionVO;


@Repository("versionDAO")
public class VersionDAO extends SqlSessionMetaDAO {

    public List<?> selectList(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("versionDAO.selectList", paramDaoVO);
    }

    public int selectListAllTotalRows(ParamDaoVO paramDaoVO) throws Exception {
	return (int) sqlSessionMeta.selectOne("versionDAO.selectListAllTotalRows" , paramDaoVO );
    }
    
    public List<?> selectListAll(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("versionDAO.selectListAll", paramDaoVO);
    }
    
    public List<?> selectListByVersionNo(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("versionDAO.selectListByVersionNo", paramDaoVO);
    }

    public int deleteListByVersionNo(ParamDaoVO paramDaoVO) throws Exception {
 	return sqlSessionMeta.delete("versionDAO.deleteListByVersionNo", paramDaoVO);
     }
    
    public List<?> selectListIncludeSubFile(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("versionDAO.selectListIncludeSubFile", paramDaoVO);
    }

    public VersionVO selectVersionInfo(ParamDaoVO paramDaoVO) throws Exception {
	return (VersionVO) sqlSessionMeta.selectOne("versionDAO.selectInfo", paramDaoVO);
    }

    public int delete(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.delete("versionDAO.delete", paramDaoVO);
    }

    public int clear(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.delete("versionDAO.clear", paramDaoVO);
    }

    public int deleteAll(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.delete("versionDAO.deleteAll", paramDaoVO);
    }

    public int deleteAllIncludeSubFile(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.delete("versionDAO.deleteAllIncludeSubFile", paramDaoVO);
    }
    
    public int insertVersion(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.insert("versionDAO.insertVersion", paramDaoVO);
    }

    public int updateVersionFileReName(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.update("versionDAO.updateVersionFileReName", paramDaoVO);
    }
    
    public int updateVersionFilePathForMove(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.update("versionDAO.updateVersionFilePathForMove", paramDaoVO);
    }

    public int updateVersionFolderPathForMove(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.update("versionDAO.updateVersionFolderPathForMove", paramDaoVO);
    }
    
}
