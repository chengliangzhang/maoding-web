package com.maoding.excelExport.dto;

public class ExcelDataDTO {

    private Object data;

    private int color;

    public ExcelDataDTO(){}

    public ExcelDataDTO(Object data,int color){
        this.data = data;
        this.color = color;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
