package com.maoding.org.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DepartDTO
 * 类描述：部门 DTO
 * 作    者：TangY
 * 日    期：2016年7月8日-上午10:39:23
 */
public class DepartDTO extends BaseDTO{

	 /**
     * 部门名称
     */
    private String departName;

     /**
      * 公司ID
      */
    private String companyId;

    /**
     * 父部门id
     */
    private String pid;
   
    /**
     * 部门层级
     */
    private String departLevel;
    
    /**
     * 排序
     */
    private int departSeq;
    
    /**
     *部门独立性(0,非独立，1独立) 
     */
    private String departType;
    
    /**
     * 父部分的departPath
     */
    private String departPath;
    
    /**
     * 0：部门，1：分支机构
     */
    private String orgType;
    
    private DepartDTO parentDepart;//父部门
    
    /**
     * 类型图标
     */
    private String type;

	/**
	 * 职位
	 */
	private String serverStation;

	public String getServerStation() {
		return serverStation;
	}

	public void setServerStation(String serverStation) {
		this.serverStation = serverStation;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getDepartLevel() {
		return departLevel;
	}

	public void setDepartLevel(String departLevel) {
		this.departLevel = departLevel;
	}

	public String getDepartType() {
		return departType;
	}

	public void setDepartType(String departType) {
		this.departType = departType;
	}

	public int getDepartSeq() {
		return departSeq;
	}

	public void setDepartSeq(int departSeq) {
		this.departSeq = departSeq;
	}


	public String getDepartPath() {
		return departPath;
	}

	public void setDepartPath(String departPath) {
		this.departPath = departPath;
	}


	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public DepartDTO getParentDepart() {
		return parentDepart;
	}

	public void setParentDepart(DepartDTO parentDepart) {
		this.parentDepart = parentDepart;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    
    
}
