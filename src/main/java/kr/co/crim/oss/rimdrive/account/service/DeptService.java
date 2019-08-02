package kr.co.crim.oss.rimdrive.account.service;

import java.util.List;

public interface DeptService {

    public List<?> getDeptList() throws Exception;

    public List<?> getSearchList(String siteCd, String searchId, String searchText) throws Exception;

    public DeptVO getDeptVOByEmpId(String empId) throws Exception;
    
}
