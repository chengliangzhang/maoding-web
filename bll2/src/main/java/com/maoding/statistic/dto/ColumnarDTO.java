package com.maoding.statistic.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ColumnarDTO implements Serializable {


    /**
     * 字段命名根据图形js对应的名称命名的
     */
    private String[] labels;

    private ColumnarDataDTO datasets[];

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public ColumnarDataDTO[] getDatasets() {
        return datasets;
    }

    public void setDatasets(ColumnarDataDTO[] datasets) {
        this.datasets = datasets;
    }
}
