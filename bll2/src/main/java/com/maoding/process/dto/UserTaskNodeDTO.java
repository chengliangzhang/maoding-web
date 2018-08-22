package com.maoding.process.dto;

import com.maoding.org.dto.CompanyUserAppDTO;

import java.util.ArrayList;
import java.util.List;

public class UserTaskNodeDTO {

    private String min;

    private String max;

    private List<CompanyUserAppDTO> userList = new ArrayList<>();

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public List<CompanyUserAppDTO> getUserList() {
        return userList;
    }

    public void setUserList(List<CompanyUserAppDTO> userList) {
        this.userList = userList;
    }
}
