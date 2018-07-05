package com.maoding.role.dto;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/1/5 10:54
 * 描    述 : 项目总览获取权限
 */
public enum ProjectUserPermissionEnum {
    ORG_MANAGER("sys_enterprise_logout", "11"),
    PROJECT_MANAGER("project_manager", "51"),
    DESIGN_MANAGER("design_manager", "52"),
    SUPER_PROJECT_EDIT("super_project_edit", "53"),
    PROJECT_EDIT("project_edit", "20"),
    PROJECT_CHARGE_MANAGER("project_charge_manage", "49"),
    FINANCE_BACK_FEE("finance_back_fee", "402"),
    SYS_ROLE_PERMISSION("sys_role_permission", "8"),
    PROJECT_OVERVIEW("project_overview", "56");


    private String name;
    private String code;

    ProjectUserPermissionEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
