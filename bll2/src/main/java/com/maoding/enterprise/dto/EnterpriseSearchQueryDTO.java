package com.maoding.enterprise.dto;


public class EnterpriseSearchQueryDTO  {

    private String enterpriseId;

    private String name;

    private int size;

    private String companyId;

    boolean isSave;
    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        if(size==0){
            size=10;
        }
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }
}
