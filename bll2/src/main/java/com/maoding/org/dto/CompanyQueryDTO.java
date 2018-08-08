package com.maoding.org.dto;

import com.maoding.core.base.dto.CoreQueryDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/8
 * 类名: com.maoding.org.dto.CompanyQueryDTO
 * 作者: 张成亮
 * 描述: 组织查询条件
 **/
public class CompanyQueryDTO extends CoreQueryDTO {
    /** id: 公司编号 */

    /** 收付款类型 */
    private Integer feeType;

    /** 项目编号 */
    private String projectId;

    /** 0/null-查询收款，1-查询付款 */
    private String isPay;

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getFeeType() {
        return feeType;
    }

    public void setFeeType(Integer feeType) {
        this.feeType = feeType;
    }
}
