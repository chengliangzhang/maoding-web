package com.maoding.org.dto;

import com.maoding.core.base.dto.CoreShowDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/8
 * 类名: com.maoding.org.dto.CompanySimpleDTO
 * 作者: 张成亮
 * 描述: 简单化的用于显示的组织信息
 **/
public class CompanySimpleDTO extends CoreShowDTO {
    /** id: 组织编号 */

    /**
     * 纳税识别号
     */
    private String taxNumber;

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }
}
