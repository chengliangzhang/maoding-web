package com.maoding.message.dto;

import com.maoding.core.base.dto.BaseDTO;
import java.math.BigDecimal;
import java.util.Date;


public class SendMessageDTO extends BaseDTO{

    /**
     *消息标题
     */
    private String messageTitle;

    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * 消息类型
     */
    private int messageType;

    /**
     * 公司ID
     */
    private String companyId;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 项目Id
     */
    private String projectId;

    /**
     * targetId
     */
    private String targetId;

    private String param1;

    private String param2;
    /**
     * 状态
     */
    private String status;

    /**
     * 创建人companyUserId
     */
    private String sendCompanyId;

    private String nodeId;//触发消息的任务节点id

    private Integer messageType1;

    private Integer messageType2;

    private String content1;

    private String content2;

    /*********************************/

    private String createByName;

    private String sendCompanyName;

    private String projectName;

    private String pointName;

    private String costTypeName;

    private BigDecimal costFee;

    private String costAction;

    private Date startDateFrom;
    private Date endDateFrom;

    private Date startDateTo;
    private Date endDateTo;

    public SendMessageDTO(){}


    public SendMessageDTO(String projectId, String companyId, String userId, String accountId, String currentCompanyId, Integer messageType, String targetId,String param1,String content1,String content2){
        this.projectId = projectId;
        this.companyId = companyId;
        this.userId = userId;
        this.sendCompanyId = currentCompanyId;
        this.messageType = messageType;
        this.setAccountId(accountId);
        this.targetId = targetId;
        this.param1 = param1;
        this.content1 = content1;
        this.content2 = content2;
    }

    public SendMessageDTO(String projectId, String companyId, String userId, String accountId, String currentCompanyId, Integer messageType1, Integer messageType2, String targetId, String nodeId,String param1){
        this.projectId = projectId;
        this.companyId = companyId;
        this.userId = userId;
        this.sendCompanyId = currentCompanyId;
        this.messageType1 = messageType1;
        this.messageType2 = messageType2;
        this.setAccountId(accountId);
        this.targetId = targetId;
        this.param1 = param1;
        this.nodeId = nodeId;
    }

    public SendMessageDTO(String projectId, String companyId, String userId, String accountId, String currentCompanyId, Integer messageType1, Integer messageType2, String targetId, String nodeId,String param1,String content1){
        this.projectId = projectId;
        this.companyId = companyId;
        this.userId = userId;
        this.sendCompanyId = currentCompanyId;
        this.messageType1 = messageType1;
        this.messageType2 = messageType2;
        this.setAccountId(accountId);
        this.targetId = targetId;
        this.param1 = param1;
        this.nodeId = nodeId;
        this.content1 = content1;
    }

    public SendMessageDTO(String projectId, String companyId, String accountId, String currentCompanyId,Integer messageType){
        this.projectId = projectId;
        this.companyId = companyId;
        this.sendCompanyId = currentCompanyId;
        this.setAccountId(accountId);
        this.messageType = messageType;
    }
    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle == null ? null : messageTitle.trim();
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent == null ? null : messageContent.trim();
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId == null ? null : targetId.trim();
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1 == null ? null : param1.trim();
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2 == null ? null : param2.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSendCompanyId() {
        return sendCompanyId;
    }

    public void setSendCompanyId(String sendCompanyId) {
        this.sendCompanyId = sendCompanyId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getMessageType1() {
        return messageType1;
    }

    public void setMessageType1(Integer messageType1) {
        this.messageType1 = messageType1;
    }

    public Integer getMessageType2() {
        return messageType2;
    }

    public void setMessageType2(Integer messageType2) {
        this.messageType2 = messageType2;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getSendCompanyName() {
        return sendCompanyName;
    }

    public void setSendCompanyName(String sendCompanyName) {
        this.sendCompanyName = sendCompanyName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getCostTypeName() {
        return costTypeName;
    }

    public void setCostTypeName(String costTypeName) {
        this.costTypeName = costTypeName;
    }

    public BigDecimal getCostFee() {
        return costFee;
    }

    public void setCostFee(BigDecimal costFee) {
        this.costFee = costFee;
    }

    public String getCostAction() {
        return costAction;
    }

    public void setCostAction(String costAction) {
        this.costAction = costAction;
    }

    public Date getStartDateFrom() {
        return startDateFrom;
    }

    public void setStartDateFrom(Date startDateFrom) {
        this.startDateFrom = startDateFrom;
    }

    public Date getEndDateFrom() {
        return endDateFrom;
    }

    public void setEndDateFrom(Date endDateFrom) {
        this.endDateFrom = endDateFrom;
    }

    public Date getStartDateTo() {
        return startDateTo;
    }

    public void setStartDateTo(Date startDateTo) {
        this.startDateTo = startDateTo;
    }

    public Date getEndDateTo() {
        return endDateTo;
    }

    public void setEndDateTo(Date endDateTo) {
        this.endDateTo = endDateTo;
    }
}
