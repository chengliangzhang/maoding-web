package com.maoding.core.base.dto;


public class QueryDTO extends BaseDTO {

    private String companyId;

    protected Integer pageIndex;

    protected Integer pageSize;

    /**
     * 申请的起始记录
     */
    protected Integer startPage;
    /**
     * 申请的页记录个数
     */
    protected Integer endPage;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStartPage() {
        if(pageIndex!=null && pageSize!=null){
            startPage = pageIndex * pageSize;
        }
        return startPage;
    }

    public void setStartPage(Integer startPage) {
        this.startPage = startPage;
    }

    public Integer getEndPage() {
        endPage = pageSize;
        return endPage;
    }

    public void setEndPage(Integer endPage) {
        this.endPage = endPage;
    }
}
