package kr.co.crim.oss.rimdrive.share.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.service.impl.SqlSessionMetaDAO;
import kr.co.crim.oss.rimdrive.files.service.FolderInfoVO;
import kr.co.crim.oss.rimdrive.files.service.FolderSubInfoVO;

@Repository("sharingInDAO")
public class SharingInDAO extends SqlSessionMetaDAO{

    public int selectTotalRows() throws Exception {
	return (int) sqlSessionMeta.selectOne("sharingInDAO.selectTotalRows");
    }

    public List<?> selectTopList(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("sharingInDAO.selectTopList", paramDaoVO);
    }

    public List<?> selectSubList(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("sharingInDAO.selectSubList", paramDaoVO);
    }

    public FolderSubInfoVO selectFolderSubInfo(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("sharingInDAO.selectFolderSubInfo", paramDaoVO);
    }

    public FolderInfoVO selectShareInFolderInfoBySharePath(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("sharingInDAO.selectShareInFolderInfoBySharePath", paramDaoVO);
    }

}




