package kr.co.crim.oss.rimdrive.share.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.service.impl.SqlSessionMetaDAO;
import kr.co.crim.oss.rimdrive.share.service.ShareVO;
import kr.co.crim.oss.rimdrive.share.service.SharingOutListVO;

@Repository("sharingOutDAO")
public class SharingOutDAO extends SqlSessionMetaDAO{

    public int selectTotalRows() throws Exception {
	return (int) sqlSessionMeta.selectOne("sharingOutDAO.selectTotalRows");
    }

    public List<?> selectList(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("sharingOutDAO.selectList", paramDaoVO);
    }

    public SharingOutListVO selectFileInfo(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("sharingOutDAO.selectFileInfo", paramDaoVO);
    }

    public ShareVO selectShare(ParamDaoVO paramDaoVO) throws Exception {
	return (ShareVO)sqlSessionMeta.selectOne("sharingOutDAO.selectShare", paramDaoVO);
    }

    public List<?> selectShareTargetList(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("sharingOutDAO.selectShareTargetList", paramDaoVO);
    }

    public int insertShare(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.insert("sharingOutDAO.insertShare", paramDaoVO);
    }

    public int insertShareTarget(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.insert("sharingOutDAO.insertShareTarget", paramDaoVO);
    }

    public int deleteShare(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.delete("sharingOutDAO.deleteShare", paramDaoVO);
    }

    public int deleteShareTarget(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.delete("sharingOutDAO.deleteShareTarget", paramDaoVO);
    }

    public int updateShareTarget(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.update("sharingOutDAO.updateShareTarget", paramDaoVO);
    }

}




