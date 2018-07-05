package com.maoding.statistic.dto;

import java.math.BigDecimal;

public class ProfitStatementDataDTO {

    private String expTypeParentName;

    private String expTypeName;

    private BigDecimal januaryData ;

    private BigDecimal februaryData ;

    private BigDecimal marchData ;

    private BigDecimal aprilData ;

    private BigDecimal mayData ;

    private BigDecimal juneData ;

    private BigDecimal julyData ;

    private BigDecimal augustData ;

    private BigDecimal septemberData ;

    private BigDecimal octoberData ;

    private BigDecimal novemberData ;

    private BigDecimal decemberData ;

    private BigDecimal totalData ;

    public ProfitStatementDataDTO(String expTypeParentName,String expTypeName){
        this.expTypeParentName  = expTypeParentName;
        this.expTypeName = expTypeName;
        januaryData = new BigDecimal("0");
        februaryData = new BigDecimal("0");
        marchData = new BigDecimal("0");
        aprilData = new BigDecimal("0");
        mayData = new BigDecimal("0");
        juneData = new BigDecimal("0");
        julyData = new BigDecimal("0");
        augustData = new BigDecimal("0");
        septemberData = new BigDecimal("0");
        octoberData = new BigDecimal("0");
        novemberData = new BigDecimal("0");
        decemberData = new BigDecimal("0");
        totalData = new BigDecimal("0");
    }

    public String getExpTypeParentName() {
        return expTypeParentName;
    }

    public void setExpTypeParentName(String expTypeParentName) {
        this.expTypeParentName = expTypeParentName;
    }

    public String getExpTypeName() {
        return expTypeName;
    }

    public void setExpTypeName(String expTypeName) {
        this.expTypeName = expTypeName;
    }

    public BigDecimal getJanuaryData() {
        return januaryData;
    }

    public void setJanuaryData(BigDecimal januaryData) {
        this.januaryData = januaryData;
    }

    public BigDecimal getFebruaryData() {
        return februaryData;
    }

    public void setFebruaryData(BigDecimal februaryData) {
        this.februaryData = februaryData;
    }

    public BigDecimal getMarchData() {
        return marchData;
    }

    public void setMarchData(BigDecimal marchData) {
        this.marchData = marchData;
    }

    public BigDecimal getAprilData() {
        return aprilData;
    }

    public void setAprilData(BigDecimal aprilData) {
        this.aprilData = aprilData;
    }

    public BigDecimal getMayData() {
        return mayData;
    }

    public void setMayData(BigDecimal mayData) {
        this.mayData = mayData;
    }

    public BigDecimal getJuneData() {
        return juneData;
    }

    public void setJuneData(BigDecimal juneData) {
        this.juneData = juneData;
    }

    public BigDecimal getJulyData() {
        return julyData;
    }

    public void setJulyData(BigDecimal julyData) {
        this.julyData = julyData;
    }

    public BigDecimal getAugustData() {
        return augustData;
    }

    public void setAugustData(BigDecimal augustData) {
        this.augustData = augustData;
    }

    public BigDecimal getSeptemberData() {
        return septemberData;
    }

    public void setSeptemberData(BigDecimal septemberData) {
        this.septemberData = septemberData;
    }

    public BigDecimal getOctoberData() {
        return octoberData;
    }

    public void setOctoberData(BigDecimal octoberData) {
        this.octoberData = octoberData;
    }

    public BigDecimal getNovemberData() {
        return novemberData;
    }

    public void setNovemberData(BigDecimal novemberData) {
        this.novemberData = novemberData;
    }

    public BigDecimal getDecemberData() {
        return decemberData;
    }

    public void setDecemberData(BigDecimal decemberData) {
        this.decemberData = decemberData;
    }

    public BigDecimal getTotalData() {
        totalData = januaryData.add(februaryData).add(marchData).add(aprilData).add(mayData)
                .add(juneData).add(julyData).add(augustData).add(septemberData)
        .add(octoberData).add(novemberData).add(decemberData);
        return totalData;
    }

    public void setTotalData(BigDecimal totalData) {
        this.totalData = totalData;
    }

    public void addObject(ProfitStatementDataDTO dto){
        januaryData = januaryData.add(dto.getJanuaryData());
        februaryData = februaryData.add(dto.getFebruaryData());
        marchData = marchData.add(dto.getMarchData());
        aprilData = aprilData.add(dto.getAprilData());
        mayData = mayData.add(dto.getMayData());
        juneData = juneData.add(dto.getJuneData());
        julyData = julyData.add(dto.getJulyData());
        augustData = augustData.add(dto.getAugustData());
        septemberData = septemberData.add(dto.getSeptemberData());
        octoberData = octoberData.add(dto.getOctoberData());
        novemberData = novemberData.add(dto.getNovemberData());
        decemberData = decemberData.add(dto.getDecemberData());
    }

    public void subtractObject(ProfitStatementDataDTO dto){
        januaryData = januaryData.subtract(dto.getJanuaryData());
        februaryData = februaryData.subtract(dto.getFebruaryData());
        marchData = marchData.subtract(dto.getMarchData());
        aprilData = aprilData.subtract(dto.getAprilData());
        mayData = mayData.subtract(dto.getMayData());
        juneData = juneData.subtract(dto.getJuneData());
        julyData = julyData.subtract(dto.getJulyData());
        augustData = augustData.subtract(dto.getAugustData());
        septemberData = septemberData.subtract(dto.getSeptemberData());
        octoberData = octoberData.subtract(dto.getOctoberData());
        novemberData = novemberData.subtract(dto.getNovemberData());
        decemberData = decemberData.subtract(dto.getDecemberData());
    }
}
