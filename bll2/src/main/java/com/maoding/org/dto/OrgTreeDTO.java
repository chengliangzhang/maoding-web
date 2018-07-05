package com.maoding.org.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DepartDTO
 * 类描述：部门 DTO
 * 作    者：TangY
 * 日    期：2016年7月8日-上午10:39:23
 */
public class OrgTreeDTO extends BaseDTO{
	private String relationType;

	private String realId;

	/**
	 * 公司id
     */
	private String companyId;
	 /**
     * 部门名称
     */
    private String text;

     /**
      * 公司ID
      */
    private Map<String,Object> state = new HashMap<String,Object>();

    /**
     * 子节点
     */
    private List<OrgTreeDTO> children = new ArrayList<OrgTreeDTO>();

	/**
	 * 当前节点对应的实体
     */
	private  Object treeEntity;

    /**
     * 类型图标
     */
    private String type;

	private  String pid;//父id（分支机构、事业合伙人中使用，用于删除事业合伙人 pid==currentCompanyId时可以删除）

	/**
	 * 是否是当前分支机构
	 */
	private int isCurrentSubCompany;

	private Map<String,String> li_attr;

	public String getRealId() {
		return realId;
	}

	public void setRealId(String realId) {
		this.realId = realId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Map<String, Object> getState() {
		return state;
	}

	public void setState(Map<String, Object> state) {
		this.state = state;
	}

	public List<OrgTreeDTO> getChildren() {
		return children;
	}

	public void setChildren(List<OrgTreeDTO> children) {
		this.children = children;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getTreeEntity() {
		return treeEntity;
	}

	public void setTreeEntity(Object treeEntity) {
		this.treeEntity = treeEntity;
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

	/**
	 * 根据id获取子节点
	 * @param id
	 * @return
     */
	public OrgTreeDTO getChildById(String id){
		for(OrgTreeDTO tree:children){
			if(tree.getId().equals(id)){
				return tree;
			}
		}
		return null;
	}

	public int getIsCurrentSubCompany() {
		return isCurrentSubCompany;
	}

	public void setIsCurrentSubCompany(int isCurrentSubCompany) {
		this.isCurrentSubCompany = isCurrentSubCompany;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public Map<String, String> getLi_attr() {
		if(li_attr==null){
			li_attr = new HashMap<>();
		}
		li_attr.put("id",this.realId);
		li_attr.put("relationType",this.relationType);
		return li_attr;
	}

	public void setLi_attr(Map<String, String> li_attr) {
		this.li_attr = li_attr;
	}
}
