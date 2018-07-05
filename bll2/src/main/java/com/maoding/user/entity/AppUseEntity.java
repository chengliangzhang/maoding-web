package com.maoding.user.entity;

import com.maoding.core.base.entity.BaseEntity;
import com.maoding.core.util.StringUtil;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：AppUseEntity
 * 类描述：AppUseEntity
 * 作    者：MaoSF
 * 日    期：2016年7月6日-下午4:46:50
 */
public class AppUseEntity extends BaseEntity implements java.io.Serializable{
	


    private String areaStructure;


    private String plotRatio;

    private String coverageRate;



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