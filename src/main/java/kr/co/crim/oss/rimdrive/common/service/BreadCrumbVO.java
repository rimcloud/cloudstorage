package kr.co.crim.oss.rimdrive.common.service;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BreadCrumbVO {

    private String Name;
    private String Url;

    public String getName() {
	return Name;
    }

    public void setName(String name) {
	Name = name;
    }

    public String getUrl() {
	return Url;
    }

    public void setUrl(String url) {
	Url = url;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }

}
