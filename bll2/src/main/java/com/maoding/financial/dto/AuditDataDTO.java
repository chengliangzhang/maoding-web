package com.maoding.financial.dto;

import com.maoding.core.util.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuditDataDTO {

    private String id;

    private String companyUserId;
    /**
     * 编号
     */
    private String expNo;

    /**
     * 类型：1=报销申请，2=费用申请，3:请假，4：出差
     */
    private Integer type;

    /**
     * 报销、费用的金额
     */
    private BigDecimal amount;

     /**
     * 请假的类型（1：年假，2：事假，3：病假，4：调休假，5：婚假，6：产假，7：陪产假，8：丧假，9：其他，10：出差）
     */
    private String leaveType;

    /**
     * 请假的类型
     */
    private String leaveTypeName;

    /**
     * 出差，请假的时长
     */
    private String leaveTime;

    /**
     * 出差，请假的开始时间
     */
    private Date startTime;

    /**
     * 出差，请假的结束时间
     */
    private Date endTime;

    /**
     * 发起申请的用户姓名
     */
    private String userName;

    /**
     * 出差地
     */
    private String address;

    /**
     * 版本控制字段
     */
    private Integer versionNum;

    private String approveStatus;

    /**
     * 审批状态名称(0:待审核，1:同意，2，退回,3:撤回,4:删除,5.审批中）
     */
    private String approveStatusName;

    public String getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(String companyUserId) {
        this.companyUserId = companyUserId;
    }

    /**
     * 费用部分
     */
    List<CostDetailDTO> costList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpNo() {
        return expNo;
    }

    public void setExpNo(String expNo) {
        this.expNo = expNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(Integer versionNum) {
        this.versionNum = versionNum;
    }

    public List<CostDetailDTO> getCostList() {
        return costList;
    }

    public void setCostList(List<CostDetailDTO> costList) {
        this.costList = costList;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getApproveStatusName() {
        if ("0".equals(approveStatus)) {
            return "待审批";
        } else if ("1".equals(approveStatus)) {
            return "已审批";
        } else if ("2".equals(approveStatus)) {
            return "退回";
        } else if ("3".equals(approveStatus)) {
            return "撤回";
        } else if ("4".equals(approveStatus)) {
            return "删除";
        } else if ("5".equals(approveStatus)) {
            return "审批中";
        }else if ("6".equals(approveStatus)) {
            return "已拨款";
        }
        return null;
    }

    public void setApproveStatusName(String approveStatusName) {
        this.approveStatusName = approveStatusName;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getLeaveTypeName() {
        if(StringUtil.isNullOrEmpty(this.leaveType)){
            return "其他";
        }
        switch (this.leaveType){
            case "1":
                return "年假";
            case "2":
                return "事假";
            case "3":
                return "病假";
            case "4":
                return "调休假";
            case "5":
                return "婚假";
            case "6":
                return "产假";
            case "7":
                return "陪产假";
            case "8":
                return "丧假";
            case "9":
                return "其他";
            case "10":
                return "出差";
            default:
                return "其他";
        }
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
    }
}
