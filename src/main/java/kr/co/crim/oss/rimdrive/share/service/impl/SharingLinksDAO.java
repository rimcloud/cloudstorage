package kr.co.crim.oss.rimdrive.share.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.service.impl.SqlSessionMetaDAO;
import kr.co.crim.oss.rimdrive.files.service.FolderSubInfoVO;
import kr.co.crim.oss.rimdrive.share.service.ShareVO;
import kr.co.crim.oss.rimdrive.share.service.SharingLinksListVO;

@Repository("sharingLinksDAO")
public class SharingLinksDAO extends SqlSessionMetaDAO{
    
    public int selectTotalRows() throws Exception {
	return (int) sqlSessionMeta.selectOne("sharingLinksDAO.selectTotalRows");
    }

    public List<?> selectList(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("sharingLinksDAO.selectList", paramDaoVO);
    }
    
    public FolderSubInfoVO selectFolderSubInfo(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("sharingLinksDAO.selectFolderSubInfo", paramDaoVO);
    }
    
    public SharingLinksListVO selectFileInfo(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("sharingLinksDAO.selectFileInfo", paramDaoVO);
    }
    
    public ShareVO selectShare(ParamDaoVO paramDaoVO) throws Exception {
	return (ShareVO)sqlSessionMeta.selectOne("sharingLinksDAO.selectShare", paramDaoVO);
    }
    
    public int insertShare(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.insert("sharingLinksDAO.insertShare", paramDaoVO);
    }
   
    public int deleteShare(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.delete("sharingLinksDAO.deleteShare", paramDaoVO);
    }
    
}




