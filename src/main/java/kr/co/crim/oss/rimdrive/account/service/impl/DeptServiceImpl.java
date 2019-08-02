package kr.co.crim.oss.rimdrive.account.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.crim.oss.rimdrive.account.service.DeptService;
import kr.co.crim.oss.rimdrive.account.service.DeptVO;
import kr.co.crim.oss.rimdrive.common.service.ParamDaoVO;

@Service("deptService")
public class DeptServiceImpl implements DeptService {

    @Resource(name = "deptDAO")
    private DeptDAO deptDAO;

    @Override
    public List<?> getDeptList() throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();

	return deptDAO.selectList(new ParamDaoVO(paramMap));
    }

    @Override
    public List<?> getSearchList(String siteCd, String searchId, String searchText) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();

	paramMap.put("siteCd", siteCd);
	paramMap.put("searchId", searchId);
	paramMap.put("searchText", searchText);

	return deptDAO.selectSearchList(new ParamDaoVO(paramMap));
    }
    
    @Override
    public DeptVO getDeptVOByEmpId(String empId) throws Exception {
	Map<String, Object> paramMap = new HashMap<String, Object>();
	paramMap.put("empId", empId);

	return deptDAO.selectDeptInfoByEmpId(new ParamDaoVO(paramMap));
    }
    
}
