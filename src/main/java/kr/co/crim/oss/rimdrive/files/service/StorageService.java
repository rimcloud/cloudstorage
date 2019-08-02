package kr.co.crim.oss.rimdrive.files.service;

public interface StorageService {

    public StorageVO getStorageInfo(String storageId) throws Exception;

    public String getStorageId(String userId) throws Exception;

}




