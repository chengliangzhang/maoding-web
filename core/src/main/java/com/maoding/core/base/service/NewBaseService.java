package com.maoding.core.base.service;

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
    public Object AopContextCurrentProxy(){
        return AopContext.currentProxy();
    }
}

