package com.maoding.org.entity;

import com.maoding.core.base.entity.BaseEntity;
/**
 * 深圳市设计同道技术有限公司
 * 类    名：DepartEntity
 * 类描述：部门基类
 * 作    者：TangY
 * 日    期：2016年7月8日-上午10:31:58
 */
public class DepartEntity extends BaseEntity implements java.io.Serializable {
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
     * 记录当前部门级别链（上一级ID-当前级别ID）
     */
    private String departPath;
    
    /**
     * 排序
     */
    private int departSeq;

    /**
     * 0：部门，1：分支机构
     */
    private String orgType;
  
    /**
     * 0:可用，1不可用（删除）
     */
    private String status;

    /**
     *部门独立性(0,非独立，1独立) 
     */
    private String departType;


    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName == null ? null : departName.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid == null ? null : pid.trim();
    }

    public String getDepartLevel() {
        return departLevel;
    }

    public void setDepartLevel(String departLevel) {
        this.departLevel = departLevel == null ? null : departLevel.trim();
    }

    public String getDepartPath() {
        return departPath;
    }

    public void setDepartPath(String departPath) {
        this.departPath = departPath == null ? null : departPath.trim();
    }

    public int getDepartSeq() {
        return departSeq;
    }

    public void setDepartSeq(int departSeq) {
        this.departSeq = departSeq;
    }

    public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getDepartType() {
        return departType;
    }

    public void setDepartType(String departType) {
        this.departType = departType == null ? null : departType.trim();
    }
}