package com.maoding.projectcost.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 项目费用详情
 * 类    名：ProjectCostDetailDTO
 * 类描述：费用收付款明显详情记录（支付方金额，收款方金额，发票信息等）
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:11:50
 */
public class ProjectCostPaymentDetailDataDTO {

    private String id;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 费用节点id
     */
    private String pointDetailId;

    /**
     * 金额
     */
    private BigDecimal fee;


    /**
     * 发票信息
     */
    private String invoice;


    private String type;

    /**
     * 收款日期
     */
    private String paidDate;

    /**
     * 付款日期
     */
    private String payDate;


    /**
     * 操作人
     */
    private String userName;

    /**
     * 操作人
     */
    private String userName2;

    /**
     * 操作人
     */
    private String userName3;

    private String field1;

    /**
     * 权限map:
     * 4.付款（技术审查费-确认付款款（经营负责人）），
     * 5.付款（技术审查费-确认付款款（企业负责人）），
     * 6.付款（合作设计费-付款确认（经营负责人）），
     * 7.付款（合作设计费-付款确认（企业负责人）），
     * 8.到款（技术审查费-确认到款），
     * 9.到款（合作设计费-到款确认）
     * 10.到款（合同回款-到款确认）
     * 根据map中的key中对应界面每个格子的权限操作,value对应任务的id。
    */
    private Map<String,Object> roleMap = new HashMap<String,Object>();


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPointDetailId() {
        return pointDetailId;
    }

    public void setPointDetailId(String pointDetailId) {
        this.pointDetailId = pointDetailId;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }


    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName2() {
        return userName2;
    }

    public void setUserName2(String userName2) {
        this.userName2 = userName2;
    }

    public String getUserName3() {
        return userName3;
    }

    public void setUserName3(String userName3) {
        this.userName3 = userName3;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public Map<String, Object> getRoleMap() {
        return roleMap;
    }

    public void setRoleMap(Map<String, Object> roleMap) {
        this.roleMap = roleMap;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }
}
