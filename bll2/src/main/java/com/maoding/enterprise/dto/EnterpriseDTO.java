package com.maoding.enterprise.dto;

import com.maoding.core.base.dto.BaseIdObject;

public class EnterpriseDTO extends BaseIdObject{

    private String corpname;

    private String taxNumber;

    public String getCorpname() {
        return corpname;
    }

    public void setCorpname(String corpname) {
        this.corpname = corpname;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }
}
