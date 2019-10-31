package kr.co.crim.oss.rimdrive.account.service;

import java.util.List;
import java.util.Map;

public interface EmpService {

    public List<?> getListByDeptCd(String deptCd, Map<String, Object> pagingMap) throws Exception;

    public List<?> getSearchList(String searchId, String searchText) throws Exception;

    public List<?> getSearchListByEmail(String searchText) throws Exception;

    public EmpVO getEmpInfoByEmpId(String empId) throws Exception;

    public boolean isExistStorageInfo(String userId) throws Exception;

    public boolean isExistUserInfo(String userId) throws Exception;

    public boolean addEmpOnlyStorage(String actionUserId, String empId, String password) throws Exception;

    public boolean changeEmpOnlyStorage(String actionUserId, String empId) throws Exception;

}
