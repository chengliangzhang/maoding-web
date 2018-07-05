package com.maoding.org.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DepartRoleDTO
 * 类描述：组织人员列表，部门，权限信息）
 * 作    者：MaoSF
 * 日    期：2016年7月9日-下午4:20:44
 */
public class DepartRoleDTO extends BaseDTO{

	 /**
     * 部门名称
     */
    private String departName;

    /**
     * 部门下面的权限
     */
    private String role;

    /**
     * role的code(用,隔开）
     */
    private String code;


	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}


	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
