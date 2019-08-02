package kr.co.crim.oss.rimdrive.files.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.service.impl.SqlSessionMetaDAO;
import kr.co.crim.oss.rimdrive.files.service.FolderSubInfoVO;
import kr.co.crim.oss.rimdrive.files.service.TrashBinVO;

@Repository("trashBinDAO")
public class TrashBinDAO extends SqlSessionMetaDAO {

    public int selectTotalRows() throws Exception {
	return (int) sqlSessionMeta.selectOne("trashBinDAO.selectTotalRows");
    }

    public int selectTotalRowsByParam(ParamDaoVO paramDaoVO) throws Exception {
	return (int) sqlSessionMeta.selectOne("trashBinDAO.selectTotalRowsByParam", paramDaoVO );
    }

    public FolderSubInfoVO selectFolderSubInfo(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("trashBinDAO.selectFolderSubInfo", paramDaoVO);
    }

    public TrashBinVO selectTrashByFileId(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("trashBinDAO.selectListByFileIds", paramDaoVO);
    }

    public List<?> selectListByFileIds(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("trashBinDAO.selectListByFileIds", paramDaoVO);
    }


    public List<?> selectList(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("trashBinDAO.selectList", paramDaoVO);
    }

    public int insertFileInfo(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.insert("trashBinDAO.insertFileInfo", paramDaoVO);
    }

    public int updateTrashFileInfoParent(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.update("trashBinDAO.updateTrashFileInfoParent", paramDaoVO);
    }

    public int updateTrashPathForSubFileByParentIdMove(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.update("trashBinDAO.updateTrashPathForSubFileByParentIdMove", paramDaoVO);
    }

    public int deleteTrashByFileId(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.delete("trashBinDAO.deleteTrashByFileId", paramDaoVO);
    }

    public int deleteTrashByStorageId(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.delete("trashBinDAO.deleteTrashByStorageId", paramDaoVO);
    }

    public int deleteFileByFileId(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.update("trashBinDAO.deleteFileByFileId", paramDaoVO);
    }

    public int deleteFileByTrashFilesFileId(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.update("trashBinDAO.deleteFileByTrashFilesFileId", paramDaoVO);
    }
}
