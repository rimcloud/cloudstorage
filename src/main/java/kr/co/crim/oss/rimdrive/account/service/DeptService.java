package kr.co.crim.oss.rimdrive.account.service;

import java.util.List;

public interface DeptService {

    public List<?> getDeptList() throws Exception;
    
    public List<?> getSearchList(String searchId, String searchText) throws Exception;

    public DeptVO getDeptVOByEmpId(String empId) throws Exception;
    
    public List<?> getSubDeptList(String deptCd, boolean includeNoneOp) throws Exception;

    public List<?> getTopDeptList() throws Exception;

    public DeptVO getDeptVO(String deptCd) throws Exception;
    
}
