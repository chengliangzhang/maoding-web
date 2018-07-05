package com.maoding.user.dto;


/**
 * 深圳市设计同道技术有限公司
 * 类    名：AppUseDTO
 * 类描述：用户注册DTO
 * 作    者：MaoSF
 * 日    期：2016年7月7日-上午9:50:37
 */
@SuppressWarnings("serial")
public class AppUseDTO  {

	private String id;

	private String areaStructure;


	private String plotRatio;

	private String coverageRate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAreaStructure() {
		return areaStructure;
	}

	public void setAreaStructure(String areaStructure) {
		this.areaStructure = areaStructure;
	}

	public String getPlotRatio() {
		return plotRatio;
	}

	public void setPlotRatio(String plotRatio) {
		this.plotRatio = plotRatio;
	}

	public String getCoverageRate() {
		return coverageRate;
	}

	public void setCoverageRate(String coverageRate) {
		this.coverageRate = coverageRate;
	}
}
