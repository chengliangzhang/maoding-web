package com.maoding.statistic.dto;

import java.util.ArrayList;
import java.util.List;

public class ProfitStatementTableDTO {

    private String key;

    private int size;

    private String code;

    private int flag ;//颜色表示：1:灰色的底

    private int arrowsFlag;//箭头表示 1：有箭头，其他无箭头


    private List<ProfitStatementDataDTO> list = new ArrayList<>();

    public ProfitStatementTableDTO(){

    }
    public ProfitStatementTableDTO(String key,String code){
        this.key = key;
        this.code = code;
    }
    public ProfitStatementTableDTO(String key, List<ProfitStatementDataDTO> list){
        this.key = key;
        this.list = list;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSize() {
        size = list.size();
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<ProfitStatementDataDTO> getList() {
        return list;
    }

    public void setList(List<ProfitStatementDataDTO> list) {
        this.list = list;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getArrowsFlag() {
        return arrowsFlag;
    }

    public void setArrowsFlag(int arrowsFlag) {
        this.arrowsFlag = arrowsFlag;
    }
}
