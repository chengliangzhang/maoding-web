package com.maoding.mytask.dto;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.invoice.dto.InvoiceEditDTO;

import java.util.List;

public class HandleMyTaskDTO extends BaseDTO {

    private String result;

    private String status;

    private String paidDate;

    //完成情况
    private String completion;


    private String projectId;

    private String taskId;

    private String currentCompanyUserId;

    /** skyDrive内存储的文件编号 */
    private List<String> fileIdList;

    /**
     * 发票信息
     */
    private InvoiceEditDTO invoiceData;

    public List<String> getFileIdList() {
        return fileIdList;
    }

    public void setFileIdList(List<String> fileIdList) {
        this.fileIdList = fileIdList;
    }

    public HandleMyTaskDTO(){

    }

    public HandleMyTaskDTO(String id,String result,String status,String accountId){
        this.setId(id);
        this.setResult(result);
        this.setStatus(status);
        this.setAccountId(accountId);
    }

    public HandleMyTaskDTO(String id,String result,String status,String accountId,String paidDate,String completion){
        this.setId(id);
        this.setResult(result);
        this.setStatus(status);
        this.setAccountId(accountId);
        this.paidDate = paidDate;
        this.completion = completion;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public String getCompletion() {
        return completion;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCurrentCompanyUserId() {
        return currentCompanyUserId;
    }

    public void setCurrentCompanyUserId(String currentCompanyUserId) {
        this.currentCompanyUserId = currentCompanyUserId;
    }

    public InvoiceEditDTO getInvoiceData() {
        return invoiceData;
    }

    public void setInvoiceData(InvoiceEditDTO invoiceData) {
        this.invoiceData = invoiceData;
    }
}
