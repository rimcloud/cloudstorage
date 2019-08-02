package kr.co.crim.oss.rimdrive.share.service;

public class ShareInfoVO {

    private long fileId = 0;
    private String storageId;
    private String name = ""; 
    private String path;
    private String permissions;
    private String pathHash;
    private String oringinName;
    private String oringinPath;

    private java.util.Date shareDate;

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getPathHash() {
        return pathHash;
    }

    public void setPathHash(String pathHash) {
        this.pathHash = pathHash;
    }

    public String getOringinName() {
        return oringinName;
    }

    public void setOringinName(String oringinName) {
        this.oringinName = oringinName;
    }

    public String getOringinPath() {
        return oringinPath;
    }

    public void setOringinPath(String oringinPath) {
        this.oringinPath = oringinPath;
    }

    public java.util.Date getShareDate() {
        return shareDate;
    }

    public void setShareDate(java.util.Date shareDate) {
        this.shareDate = shareDate;
    }
    
}
