package com.maoding.user.dao.impl;


import com.maoding.core.base.dao.GenericDao;
import com.maoding.user.dao.AppUseDao;
import com.maoding.user.dao.UserDao;
import com.maoding.user.entity.AppUseEntity;
import com.maoding.user.entity.UserEntity;
import com.sun.javafx.collections.MappingChange;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("appUseDao")
public class AppUseDaoImpl extends GenericDao<AppUseEntity> implements AppUseDao {

    public List<AppUseEntity> getAppUseList(){
        Map<String,String> map = new HashMap<String,String>();
        return this.sqlSession.selectList("AppUseEntityMapper.selectList", map);
    }
}
