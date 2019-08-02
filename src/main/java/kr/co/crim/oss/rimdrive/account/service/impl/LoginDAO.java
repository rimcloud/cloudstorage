package kr.co.crim.oss.rimdrive.account.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.crim.oss.rimdrive.account.service.LoginVO;
import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.service.SessionVO;
import kr.co.crim.oss.rimdrive.common.service.impl.SqlSessionMetaDAO;

@Repository("loginDAO")
public class LoginDAO extends SqlSessionMetaDAO {

    public LoginVO selectCheckLogin(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("loginDAO.selectCheckLogin", paramDaoVO);
    }

    public SessionVO selectUserLoginInfo(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("loginDAO.selectUserLoginInfo", paramDaoVO);
    }

    public SessionVO selectUserLoginId(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectOne("loginDAO.selectUserId", paramDaoVO);
    }

    public List<?> selectEmpLoginInfo(ParamDaoVO paramDaoVO) throws Exception {
	return sqlSessionMeta.selectList("loginDAO.selectEmpLoginInfo", paramDaoVO);
    }

}
