package com.maoding.core.base.entity;

import java.util.Date;
import java.util.UUID;

public abstract class BaseEntity implements java.io.Serializable  {

	private String id;
	private String 	createBy;
	private String 	updateBy;
	private Date 	createDate;
	private Date 	updateDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * 方法描述：设置实体4个基本属性
	 * 作   者：LY
	 * 日   期：2016/7/22 17:20
	 * @param
	 * @return
	 *
	*/
	public void set4Base(String createBy, String updateBy, Date createDate, Date updateDate){
		this.setCreateBy(createBy);
		this.setUpdateBy(updateBy);
		this.setCreateDate(createDate);
		this.setUpdateDate(updateDate);
	}

	/** 初始化实体 */
	public void initEntity() {
		resetId();
		resetCreateDate();
		resetUpdateDate();
	}

	/** 重置主键Id为新的UUID */
	public void resetId() {
		this.id = UUID.randomUUID().toString().replaceAll("-", "");
	}

	/** 重置创建时间为当前时间 */
	public void resetCreateDate() {
		this.createDate = new Date();
	}

	/** 重置更新时间为当前时间 */
	public void resetUpdateDate() {
		this.updateDate = new Date();
	}
}
