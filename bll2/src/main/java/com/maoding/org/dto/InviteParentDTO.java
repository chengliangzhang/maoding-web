package com.maoding.org.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：邀请事业合伙人DTO
 * 类描述：部门 DTO
 * 作    者：MaoSF
 * 日    期：2017年4月1日-上午10:39:23
 */
public class InviteParentDTO extends BaseDTO {

	/**
	 * 1:分支机构邀请，2：事业合伙人邀请,3:外部合作
     */
	private String type;
	/**
	 * 项目ID（外部合作必传参数）
     */
	private String projectId;

    private List<String> cellphoneList = new ArrayList<String>();

	public List<String> getCellphoneList() {
		return cellphoneList;
	}

	public void setCellphoneList(List<String> cellphoneList) {
		this.cellphoneList = cellphoneList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
