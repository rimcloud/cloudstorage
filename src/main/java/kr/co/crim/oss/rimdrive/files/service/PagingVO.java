package kr.co.crim.oss.rimdrive.files.service;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PagingVO {

    private List<?> resultList = null;
    private int totalRows = 0;

    public List<?> getResultList() {
	return resultList;
    }

    public void setResultList(List<?> resultList) {
	this.resultList = resultList;
    }

    public int getTotalRows() {
	return totalRows;
    }

    public void setTotalRows(int totalRows) {
	this.totalRows = totalRows;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}
