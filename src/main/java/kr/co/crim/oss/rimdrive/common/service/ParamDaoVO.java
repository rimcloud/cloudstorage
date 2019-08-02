package kr.co.crim.oss.rimdrive.common.service;

import java.util.HashMap;
import java.util.Map;

public class ParamDaoVO {

    private Map<String, Object> paramMap = new HashMap<String, Object>();

    public ParamDaoVO(String key, String value) throws Exception {
	Map<String, Object> pMap = new HashMap<String, Object>();
	pMap.put(key, value);
	this.setParam(pMap);
    }

    public ParamDaoVO(Object obj) throws Exception {
	this.setParam(obj);
    }

    public Object getParam() {
	return paramMap.get("param");
    }

    public void setParam(Object param) {
	this.paramMap.put("param", param);
    }

    public Map<String, Object> getParamMap() {
	return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
	this.paramMap = paramMap;
    }
}
