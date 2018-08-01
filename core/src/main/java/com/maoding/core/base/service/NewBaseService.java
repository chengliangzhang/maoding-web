package com.maoding.core.base.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Wuwq on 2016/12/14.
 * 业务层基类（不带增删查改通用方法，目的是为了让业务更纯粹）
 */
@Service
@Transactional(rollbackFor = Exception.class)
public abstract class NewBaseService {
    /** 日志对象，不能用于static方法 */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    public Object AopContextCurrentProxy(){
        return AopContext.currentProxy();
    }
}

