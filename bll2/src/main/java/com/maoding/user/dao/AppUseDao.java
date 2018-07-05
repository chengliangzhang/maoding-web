package com.maoding.user.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.user.entity.AppUseEntity;
import com.maoding.user.entity.UserAttachEntity;
import com.maoding.user.entity.UserEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：AppUseDao
 * 类描述：AppUseDao
 * 作    者：MaoSF
 * 日    期：2015年11月23日-下午2:44:39
 */
public interface AppUseDao extends BaseDao<AppUseEntity>{

    /**
     * 方法描述：getAppUseList
     * 作        者：TangY
     * 日        期：2016年3月21日-上午10:29:56
     * @param paraMap
     * @return
     */
    public List<AppUseEntity> getAppUseList();

}
