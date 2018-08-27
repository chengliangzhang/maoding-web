package com.maoding.project.dto;

import com.maoding.core.base.dto.CoreDTO;
import com.maoding.core.base.dto.CoreShowDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/23
 * 类名: com.maoding.project.dto.TitleColumnDTO
 * 作者: 张成亮
 * 描述:
 **/
public class TitleColumnDTO extends CoreDTO {
    /** id: 标题编号 **/
    
    /** 标题关键字 **/
    private String code;

    /** 标题名称 **/
    private String name;

    /** 字段类型：1-字符串,2-日期,3-金额（万元）,4-金额（元） **/
    private int type;

    /** 过滤器类型：0-无过滤器，1-字符串，2-单选列表，3-多选列表，4-时间，5-地址 **/
    private int filterType;

    /** 是否可隐藏,0-不可隐藏,1-可隐藏 **/
    private short canBeHide;

    /** 是否可排序,0-不可排序,1-可以排序 **/
    private short canBeOrder;

    /** 过滤器是列表型：0-不是,1-是 **/
    private short hasList;

    /** 合作组织：0-不是，1-是 **/
    private short isRelationCompany;

    /** 任务负责人：0-不是，1-是 **/
    private short isTaskLeader;

    /** 设计：0-不是，1-是 **/
    private short isDesigner;

    /** 校验：0-不是，1-是 **/
    private short isChecker;

    /** 审核：0-不是，1-是 **/
    private short isAuditor;

    /** 合同款：0-不是，1-是 **/
    private short isContractFee;

    /** 技术审查费：0-不是，1-是 **/
    private short isTechnicalFee;

    /** 合作设计费收款：0-不是，1-是 **/
    private short isCooperateGainFee;

    /** 其他收款：0-不是，1-是 **/
    private short isOtherGainFee;

    /** 合作设计费付款：0-不是，1-是 **/
    private short isCooperatePayFee;

    /** 其他付款：0-不是，1-是 **/
    private short isOtherPayFee;

    /** 过滤器，仅对列表型过滤器有效 **/
    private List<CoreShowDTO> filterList;

    public short getCanBeHide() {
        return canBeHide;
    }

    public void setCanBeHide(short canBeHide) {
        this.canBeHide = canBeHide;
    }

    public short getCanBeOrder() {
        return canBeOrder;
    }

    public void setCanBeOrder(short canBeOrder) {
        this.canBeOrder = canBeOrder;
    }

    public short getHasList() {
        return hasList;
    }

    public void setHasList(short hasList) {
        this.hasList = hasList;
    }

    public short getIsRelationCompany() {
        return isRelationCompany;
    }

    public void setIsRelationCompany(short isRelationCompany) {
        this.isRelationCompany = isRelationCompany;
    }

    public short getIsTaskLeader() {
        return isTaskLeader;
    }

    public void setIsTaskLeader(short isTaskLeader) {
        this.isTaskLeader = isTaskLeader;
    }

    public short getIsDesigner() {
        return isDesigner;
    }

    public void setIsDesigner(short isDesigner) {
        this.isDesigner = isDesigner;
    }

    public short getIsChecker() {
        return isChecker;
    }

    public void setIsChecker(short isChecker) {
        this.isChecker = isChecker;
    }

    public short getIsAuditor() {
        return isAuditor;
    }

    public void setIsAuditor(short isAuditor) {
        this.isAuditor = isAuditor;
    }

    public short getIsContractFee() {
        return isContractFee;
    }

    public void setIsContractFee(short isContractFee) {
        this.isContractFee = isContractFee;
    }

    public short getIsTechnicalFee() {
        return isTechnicalFee;
    }

    public void setIsTechnicalFee(short isTechnicalFee) {
        this.isTechnicalFee = isTechnicalFee;
    }

    public short getIsCooperateGainFee() {
        return isCooperateGainFee;
    }

    public void setIsCooperateGainFee(short isCooperateGainFee) {
        this.isCooperateGainFee = isCooperateGainFee;
    }

    public short getIsOtherGainFee() {
        return isOtherGainFee;
    }

    public void setIsOtherGainFee(short isOtherGainFee) {
        this.isOtherGainFee = isOtherGainFee;
    }

    public short getIsCooperatePayFee() {
        return isCooperatePayFee;
    }

    public void setIsCooperatePayFee(short isCooperatePayFee) {
        this.isCooperatePayFee = isCooperatePayFee;
    }

    public short getIsOtherPayFee() {
        return isOtherPayFee;
    }

    public void setIsOtherPayFee(short isOtherPayFee) {
        this.isOtherPayFee = isOtherPayFee;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFilterType() {
        return filterType;
    }

    public void setFilterType(int filterType) {
        this.filterType = filterType;
    }

    public List<CoreShowDTO> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<CoreShowDTO> filterList) {
        this.filterList = filterList;
    }
}
