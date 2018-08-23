package com.maoding.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.CoreDTO;

import java.util.Date;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/23
 * 类名: com.maoding.project.dto.ProjectVariableDTO
 * 作者: 张成亮
 * 描述:
 **/
public class ProjectVariableDTO extends CoreDTO {
    /** id:项目编号 **/

    /** 项目编号 */
    private String projectNo;
    /** 项目名称 */
    private String projectName;
    /** 立项组织 **/
    private String createCompany;
    /** 立项时间 **/
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date projectCreateDate;
    /** 合同签订 **/
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String signDate;
    /** 项目状态 **/
    private String status;
    /** 功能分类 **/
    private String buildName;
    /** 项目地点 **/
    private String address;
    /** 甲方 **/
    private String partyA;
    /** 乙方 **/
    private String partyB;
    /** 合作组织 **/
    private String relationCompany;
    /** 项目类型 **/
    private String projectType;
    /** 经营负责人 **/
    private String busPersonInCharge;
    /** 经营助理 **/
    private String busPersonInChargeAssistant;
    /** 设计负责人 **/
    private String designPersonInCharge;
    /** 设计助理 **/
    private String designPersonInChargeAssistant;
    /** 任务负责人 **/
    private String taskLeader;
    /** 设计人员 **/
    private String designer;
    /** 校对人员 **/
    private String checker;
    /** 审核人员 **/
    private String auditor;
    /** 合同计划收款 **/
    private double contract;
    /** 合同到账信息 **/
    private double contractReal;
    /** 技术审查费计划收款 **/
    private double technicalGain;
    /** 技术审查费到账金额 **/
    private double technicalGainReal;
    /** 合作设计费计划收款 **/
    private double cooperateGain;
    /** 合作设计费到账金额 **/
    private double cooperateGainReal;
    /** 其他收入计划收款 **/
    private double otherGain;
    /** 其他收入到账金额 **/
    private double otherGainReal;
    /** 技术审查费计划付款 **/
    private double technicalPay;
    /** 技术审查费付款金额 **/
    private double technicalPayReal;
    /** 合作设计费计划付款 **/
    private double cooperatePay;
    /** 合作设计费付款金额 **/
    private double cooperatePayReal;
    /** 其他支出计划付款 **/
    private double otherPay;
    /** 其他支出付款金额 **/
    private double otherPayReal;

}
