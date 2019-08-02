package kr.co.crim.oss.rimdrive.account.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.crim.oss.rimdrive.account.service.EmpVO;
import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.service.impl.SqlSessionMetaDAO;

@Repository("empDAO")
public class EmpDAO extends SqlSessionMetaDAO {

    public List<?> selectList(ParamDaoVO paramDaoVO) throws Exception {
   	return sqlSessionMeta.selectList("empDAO.selectList", paramDaoVO);
    }

    public List<?> selectSearchList(ParamDaoVO paramDaoVO) throws Exception {
   	return sqlSessionMeta.selectList("empDAO.selectSearchList", paramDaoVO);
    }

    public EmpVO selectEmpInfo(ParamDaoVO paramDaoVO) throws Exception {
   	return sqlSessionMeta.selectOne("empDAO.selectEmpInfo", paramDaoVO);
    }

    public int selectStorageInfo(ParamDaoVO paramDaoVO) throws Exception {
   	return sqlSessionMeta.selectOne("empDAO.selectStorageInfo", paramDaoVO);
    }
    
    public int insertStorageInfo(ParamDaoVO paramDaoVO) throws Exception {
   	return sqlSessionMeta.insert("empDAO.insertStorageInfo", paramDaoVO);
    }

    public int updateQuotaByOwnerId(ParamDaoVO paramDaoVO) throws Exception {
   	return sqlSessionMeta.update("empDAO.updateQuotaByOwnerId", paramDaoVO);
    }
    
    public int selectUserInfo(ParamDaoVO paramDaoVO) throws Exception {
   	return sqlSessionMeta.selectOne("empDAO.selectUserInfo", paramDaoVO);
    }
    
    public int insertUserInfo(ParamDaoVO paramDaoVO) throws Exception {
   	return sqlSessionMeta.insert("empDAO.insertUserInfo", paramDaoVO);
    }

}

