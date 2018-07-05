package com.maoding.statistic.dto;

import java.io.Serializable;
import java.util.List;

public class ColumnarDataDTO implements Serializable {

    private String companyId;

    private String label;

    private String backgroundColor;

    private String borderColor;

    private String pointBackgroundColor;

    private String pointBorderColor;

    private double[] data;

    public ColumnarDataDTO (int colSize,String color){
        this.backgroundColor = color;
        this.borderColor = color;
        this.pointBackgroundColor = color;
        this.pointBorderColor = color;
        data = new double[colSize];
        for(int i = 0;i<colSize;i++){
            data[i] = 0;
        }
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public String getPointBackgroundColor() {
        return pointBackgroundColor;
    }

    public void setPointBackgroundColor(String pointBackgroundColor) {
        this.pointBackgroundColor = pointBackgroundColor;
    }

    public String getPointBorderColor() {
        return pointBorderColor;
    }

    public void setPointBorderColor(String pointBorderColor) {
        this.pointBorderColor = pointBorderColor;
    }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }
}
