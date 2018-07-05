package com.maoding.project.dto;

/**
 * Created by Chengliang.zhang on 2017/8/14.
 * 地址
 */
public class LocationDTO {
    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** 县或区或镇 */
    private String county;

    /** 详细地址 */
    private String detailAddress;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }
}
