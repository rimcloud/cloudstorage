package kr.co.crim.oss.rimdrive.common.service.impl;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;

public abstract class SqlSessionMetaDAO {

    @Resource(name="sqlSessionMeta")
    protected SqlSessionTemplate  sqlSessionMeta;

}
