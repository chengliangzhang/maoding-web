package com.maoding.dynamic.service;

import com.maoding.dynamic.dto.ProjectDynamicDTO;
import com.maoding.dynamic.dto.QueryDynamicDTO;
import com.maoding.dynamic.entity.DynamicDO;

import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/6/7.
 */
public interface DynamicService {
    /**
     * 获取动态
     */
    ProjectDynamicDTO getProjectDynamic(QueryDynamicDTO query);

    /**
     * 记录项目动态
     */
    void addDynamic(DynamicDO dynamic);
    void addDynamic(List<DynamicDO> dynamicList);

    /**
     * 通用的添加动态方法
     */
    <T> void addDynamic(T origin,T target,String projectId,String companyId,String userId,String companyUserId);
    /**
     * 处理难以获取某些参数的情况，保持兼容性
     */
    <T> void addDynamic(T origin,T target,String projectId, String companyId,String userId);
    <T> void addDynamic(T origin,T target,String companyId,String userId);
    <T> void addDynamic(T origin,T target,String companyUserId);
    <T> void addDynamic(T origin,T target);
    /**
     * 方便使用的添加删除方法
     */
    <T> void addCreateDynamic(T target,String projectId, String companyId,String userId,String companyUserId);
    <T> void addCreateDynamic(T target,String projectId, String companyId,String userId);
    <T> void addCreateDynamic(T target,String companyId,String userId);
    <T> void addCreateDynamic(T target,String companyUserId);
    <T> void addCreateDynamic(T target);
    <T> void addDeleteDynamic(T origin,String projectId, String companyId,String userId,String companyUserId);
    <T> void addDeleteDynamic(T origin,String projectId, String companyId,String userId);
    <T> void addDeleteDynamic(T origin,String companyId,String userId);
    <T> void addDeleteDynamic(T origin,String companyUserId);
    <T> void addDeleteDynamic(T origin);
}
