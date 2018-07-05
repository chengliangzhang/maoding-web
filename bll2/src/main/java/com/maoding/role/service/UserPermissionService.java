package com.maoding.role.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.role.dto.SaveUserPermission2DTO;
import com.maoding.role.dto.SaveUserPermissionDTO;
import com.maoding.role.entity.UserPermissionEntity;

import java.util.Map;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：UserPermissionService
 * 类描述：前台角色权限表（dao）
 * 作    者：wrb
 * 日    期：2016年11月2日-上午11:38:47
 */
public interface UserPermissionService extends BaseService<UserPermissionEntity> {


    /**
     * 方法描述：
     * 作者：MaoSF
     * 日期：2017/6/13
     */
    void saveUserPermission(UserPermissionEntity entity);

    /**
     * 方法描述：保存用户权限关联数据
     * 作者：MaoSF
     * 日期：2016/11/2
     * @param:
     * @return:
     */
    public AjaxMessage saveUserPermission(SaveUserPermissionDTO dto) throws Exception;


    /**
     * 方法描述：保存用户权限关联数据（从权限中选择人员）
     * 作者：MaoSF
     * 日期：2016/11/2
     * @param:
     * @return:
     */
    public AjaxMessage saveUserPermission2(SaveUserPermission2DTO dto) throws Exception;

    /**
     * 方法描述：删除用户权限关联数据（从权限中删除人员）
     * 作者：MaoSF
     * 日期：2016/11/2
     * @param:
     * @return:
     */
    public AjaxMessage deleteUserPermission2(Map<String,Object> map) throws Exception;



    /**
     * 方法描述：批量删除用户权限关联数据
     * 作者：MaoSF
     * 日期：2016/11/2
     * @param:
     * @return:
     */
    public AjaxMessage deleteUserPermissionOfBatch(SaveUserPermissionDTO dto) throws Exception;


}