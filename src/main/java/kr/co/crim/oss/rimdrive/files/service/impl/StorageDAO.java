package kr.co.crim.oss.rimdrive.files.service.impl;

import org.springframework.stereotype.Repository;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.service.impl.SqlSessionMetaDAO;
import kr.co.crim.oss.rimdrive.files.service.StorageVO;

@Repository("storageDAO")
public class StorageDAO extends SqlSessionMetaDAO {

    public StorageVO selectStorageInfo(ParamDaoVO paramDaoVO) throws Exception {
	return (StorageVO) sqlSessionMeta.selectOne("storageDAO.selectStorageInfo", paramDaoVO);
    }

    public String selectStorageId(ParamDaoVO paramDaoVO) throws Exception {
	return (String) sqlSessionMeta.selectOne("storageDAO.selectStorageId", paramDaoVO);
    }

}
