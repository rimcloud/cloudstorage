package kr.co.crim.oss.rimdrive.common.service;

import java.util.List;
import java.util.Map;

public class PagingListVO {

    int totalConut = 0;
    List<?> resultList = null;
    Map<String, Object> pagingMap = null;

    public int getTotalConut() {
	return totalConut;
    }

    public void setTotalConut(int totalConut) {
	this.totalConut = totalConut;
    }

    public List<?> getResultList() {
	return resultList;
    }

    public void setResultList(List<?> resultList) {
	this.resultList = resultList;
    }

    public Map<String, Object> getPagingMap() {
	return pagingMap;
    }

    public void setPagingMap(Map<String, Object> pagingMap) {
	this.pagingMap = pagingMap;
    }
}

