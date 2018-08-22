package com.maoding.activiti.dto;

import java.util.ArrayList;
import java.util.List;

public class UserTaskDTO {

    private String min;

    private String max;

    List<String> assignList = new ArrayList<>();//暂时只考虑assign

    List<String> candidateUserList = new ArrayList<>();

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

    public List<String> getAssignList() {
        return assignList;
    }

    public void setAssignList(List<String> assignList) {
        this.assignList = assignList;
    }

    public List<String> getCandidateUserList() {
        return candidateUserList;
    }

    public void setCandidateUserList(List<String> candidateUserList) {
        this.candidateUserList = candidateUserList;
    }
}
