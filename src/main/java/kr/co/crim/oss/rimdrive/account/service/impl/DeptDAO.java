package kr.co.crim.oss.rimdrive.account.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.crim.oss.rimdrive.account.service.DeptVO;
import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;
import kr.co.crim.oss.rimdrive.common.service.impl.SqlSessionMetaDAO;

@Repository("deptDAO")
public class DeptDAO extends SqlSessionMetaDAO {

    public List<?> selectList(ParamDaoVO paramDaoVO) throws Exception {
   	return sqlSessionMeta.selectList("deptDAO.selectList", paramDaoVO);
    }

    public List<?> selectSearchList(ParamDaoVO paramDaoVO) throws Exception {
   	return sqlSessionMeta.selectList("deptDAO.selectSearchList", paramDaoVO);
    }

    public DeptVO selectDeptInfoByEmpId(ParamDaoVO paramDaoVO) throws Exception {
   	return (DeptVO)sqlSessionMeta.selectOne("deptDAO.selectDeptInfoByEmpId", paramDaoVO);
    }
    
}

