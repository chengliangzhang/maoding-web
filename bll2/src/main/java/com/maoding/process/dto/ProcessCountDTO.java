package com.maoding.process.dto;

public class ProcessCountDTO {

    private Integer contractReceiveCount;

    private Integer technicalReceiveCount;

    private Integer technicalPayCount;

    private Integer cooperativeReceiveCount;

    private Integer cooperativePayCount;


    public Integer getContractReceiveCount() {
        return contractReceiveCount;
    }

    public void setContractReceiveCount(Integer contractReceiveCount) {
        this.contractReceiveCount = contractReceiveCount;
    }

    public Integer getTechnicalReceiveCount() {
        return technicalReceiveCount;
    }

    public void setTechnicalReceiveCount(Integer technicalReceiveCount) {
        this.technicalReceiveCount = technicalReceiveCount;
    }

    public Integer getTechnicalPayCount() {
        return technicalPayCount;
    }

    public void setTechnicalPayCount(Integer technicalPayCount) {
        this.technicalPayCount = technicalPayCount;
    }

    public Integer getCooperativeReceiveCount() {
        return cooperativeReceiveCount;
    }

    public void setCooperativeReceiveCount(Integer cooperativeReceiveCount) {
        this.cooperativeReceiveCount = cooperativeReceiveCount;
    }

    public Integer getCooperativePayCount() {
        return cooperativePayCount;
    }

    public void setCooperativePayCount(Integer cooperativePayCount) {
        this.cooperativePayCount = cooperativePayCount;
    }
}
