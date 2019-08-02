package kr.co.crim.oss.rimdrive.search.service.impl;

import org.springframework.stereotype.Repository;

import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.service.impl.SqlSessionMetaDAO;

@Repository("tagDAO")
public class TagDAO extends SqlSessionMetaDAO {

    public int updateSearchTag(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.update("tagDAO.updateSearchTag", paramDaoVO);
    }

}




