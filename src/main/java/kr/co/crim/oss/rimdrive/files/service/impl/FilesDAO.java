package kr.co.crim.oss.rimdrive.files.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.service.impl.SqlSessionMetaDAO;
import kr.co.crim.oss.rimdrive.files.service.FileListVO;
import kr.co.crim.oss.rimdrive.files.service.FolderInfoVO;
import kr.co.crim.oss.rimdrive.files.service.FolderSubInfoVO;

@Repository("filesDAO")
public class FilesDAO extends SqlSessionMetaDAO {

    public int selectTotalRows() throws Exception {
	return (int) sqlSessionMeta.selectOne("filesDAO.selectTotalRows");
    }

    public List<FileListVO> selectList(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("filesDAO.selectList", paramDaoVO);
    }

    public List<FileListVO> selectFolderListAll(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("filesDAO.selectFolderListAll", paramDaoVO);
    }

    public int selectListTotalRows(ParamDaoVO paramDaoVO) throws Exception {
	return (int) sqlSessionMeta.selectOne("filesDAO.selectListTotalRows", paramDaoVO);
    }

    public List<FileListVO> selectListAll(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("filesDAO.selectFolderListAll", paramDaoVO);
    }

    public List<?> selectFileListByFileIds(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("filesDAO.selectFileListByFileIds", paramDaoVO);
    }

    public List<?> selectFileListByPaths(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("filesDAO.selectFileListByPaths", paramDaoVO);
    }

    public List<?> selectListExistFileByFileIds(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("filesDAO.selectListExistFileByFileIds", paramDaoVO);
    }

    public FileListVO selectInfoExistFileByName(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("filesDAO.selectInfoExistFileByName", paramDaoVO);
    }

    public FileListVO selectFileInfo(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("filesDAO.selectFileInfo", paramDaoVO);
    }

    public FileListVO selectFileInfoIgnoreCase(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("filesDAO.selectFileInfoIgnoreCase", paramDaoVO);
    }

    public FolderInfoVO selectFolderInfo(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("filesDAO.selectFolderInfo", paramDaoVO);
    }

    public long selectFileIdByNameIgnoreCase(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("filesDAO.selectFileIdByNameIgnoreCase", paramDaoVO);
    }

    public FolderSubInfoVO selectFolderSubInfo(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("filesDAO.selectFolderSubInfo", paramDaoVO);
    }

    public FolderSubInfoVO selectFolderSubInfoAll(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("filesDAO.selectFolderSubInfoAll", paramDaoVO);
    }

    public int updateFileName(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.update("filesDAO.updateFileName", paramDaoVO);
    }

    public int updatePathForSubFileByParentId(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.update("filesDAO.updatePathForSubFileByParentId", paramDaoVO);
    }

    public int insertFileInfo(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.insert("filesDAO.insertFileInfo", paramDaoVO);
    }

    public int updateFileInfoParent(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.update("filesDAO.updateFileInfoParent", paramDaoVO);
    }

    public int updatePathForSubFileByParentIdMove(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.update("filesDAO.updatePathForSubFileByParentIdMove", paramDaoVO);
    }

    public int updateRenameFileInfoParent(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.update("filesDAO.updateRenameFileInfoParent", paramDaoVO);
    }

    public int insertCopyIncloudSubFile(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.insert("filesDAO.insertCopyIncloudSubFile", paramDaoVO);
    }

    public int updateParentIdByPathForCopy(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.update("filesDAO.updateParentIdByPathForCopy", paramDaoVO);
    }

    public int deleteFileByFileId(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.delete("filesDAO.deleteFileByFileId", paramDaoVO);
    }
    
    public int updateFileInfoSize(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.update("filesDAO.updateFileInfoSize", paramDaoVO);
    }

    public int updateFileInfo(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.update("filesDAO.updateFileInfo", paramDaoVO);
    }

}
