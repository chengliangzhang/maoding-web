package com.maoding.core.base.dao;


import com.maoding.core.base.dto.CoreEditDTO;
import com.maoding.core.base.entity.BaseEntity;
import com.maoding.core.util.BeanUtils;
import com.maoding.core.util.StringUtils;
import com.maoding.core.util.TraceUtils;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/30
 * 类名: com.maoding.core.base.dao.BaseEditDao
 * 作者: 张成亮
 * 描述: 用于读写数据库的接口
 **/
public interface BaseEditDao<T extends BaseEntity> extends BaseViewDao<T>,Mapper<T>,MySqlMapper<T> {
    //FIXME 特别注意，该接口不能被扫描到，否则会出错
    String UNSELECTED = "0";

    /**
     * 描述       通过更新申请进行记录更新
     * 日期       2018/8/16
     * @author   张成亮
     * @param    request 更新申请
     *                   accountId 申请者用户编号
     * @return   返回更新后的记录
     **/
    default T updateById(@NotNull CoreEditDTO request){
        Class<T> clazz = getT();
        T entity = BeanUtils.createFrom(request,clazz);
        entity.setUpdateBy(request.getAccountId());
        return updateById(entity);
    }

    /**
     * 描述       通过更新申请进行记录更新，需要判断记录是否被选中更新
     * 日期       2018/8/16
     * @author   张成亮
     * @param    request 更新申请
     *                   accountId 申请者用户编号
     *                   isSelected 是否被选中进行更新
     * @return   如果选中进行更新，返回更新后的记录，否则返回空
     **/
    default T update(@NotNull CoreEditDTO request){
        T entity = null;
        if (StringUtils.isNotSame(request.getIsSelected(), UNSELECTED)) {
            entity = updateById(request);
        }
        return entity;
    }

    default T updateById(@NotNull T request){
        T entity = null;
        //如果entity内的id不为空,则从数据库内读取，如果为空，则新增，如果不为空，则更改
        if (StringUtils.isNotEmpty(request.getId())){
            entity = selectById(request.getId());
            if (entity != null) {
                //修改
                BeanUtils.copyProperties(request, entity);
                entity.resetUpdateDate();
                updateByPrimaryKeySelective(entity);
            }
        }

        //如果entity的id为空，或者数据库内没有此记录，则新增记录
        if (entity == null) {
            entity = request;
            insert(entity);
        }
        return entity;
    }

    default int insert(@NotNull T entity){
        if (StringUtils.isEmpty(entity.getId())){
            entity.initEntity();
        } else if (entity.getCreateDate() == null) {
            entity.resetCreateDate();
        }
        return insertSelective(entity);
    }

    default void deleteById(@NotNull CoreEditDTO request){
        //如果更改请求内的id不为空,且被选中，则从数据库内删除
        if (StringUtils.isNotSame(request.getIsSelected(), UNSELECTED)) {
            Class<T> clazz = getT();
            try {
                T entity = clazz.newInstance();
                entity.setId(request.getId());
                deleteById(entity);
            } catch (InstantiationException | IllegalAccessException e) {
                TraceUtils.error("删除记录时出现错误");
            }
        }
    }

    default void deleteById(@NotNull T request){
        //如果entity内的id不为空,则从数据库内删除
        if (StringUtils.isNotEmpty(request.getId())){
            deleteByPrimaryKey(request);
        }
    }

    default int BatchInsert(List<T> recordList){
        return insertList(recordList);
    }
}
