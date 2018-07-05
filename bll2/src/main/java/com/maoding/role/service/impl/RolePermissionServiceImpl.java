package com.maoding.role.service.impl;

import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.StringUtil;
import com.maoding.role.dao.RolePermissionDao;
import com.maoding.role.dto.SaveRolePermissionDTO;
import com.maoding.role.entity.RolePermissionEntity;
import com.maoding.role.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：RolePermissionService
 * 类描述：角色视图表（dao）
 * 作    者：wrb
 * 日    期：2016年11月2日-上午11:38:47
 */
@Service("rolePermissionService")
public class RolePermissionServiceImpl extends GenericService<RolePermissionEntity> implements RolePermissionService {

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    public AjaxMessage saveRolePermission(SaveRolePermissionDTO dto) throws Exception{

        String companyId=dto.getCurrentCompanyId();
        String createBy=dto.getAccountId();

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("roleId", dto.getRoleId());
        map.put("companyId", companyId);
        rolePermissionDao.deleteByRoleId(map);//先删除权限与角色之间的关系

        for(int i=0;i<dto.getPermissionIds().size();i++){
            RolePermissionEntity rolePermission =new RolePermissionEntity();
            rolePermission.setId(StringUtil.buildUUID());
            rolePermission.setPermissionId(dto.getPermissionIds().get(i));
            rolePermission.setCompanyId(companyId);
            rolePermission.setRoleId(dto.getRoleId());
            rolePermission.setCreateBy(createBy);
            rolePermissionDao.insert(rolePermission);
        }

        return new AjaxMessage().setCode("0").setInfo("保存成功");
    }

}