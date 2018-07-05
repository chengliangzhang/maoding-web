package com.maoding.system.entity;


import com.maoding.core.base.entity.BaseEntity;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：VersionEntity
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2015年12月30日-上午11:25:10
 */
public class VersionEntity extends BaseEntity {
    private static final long serialVersionUID = -627840644442909419L;
    private String platform;
    private String versioncode;
    private String versionname;
    private String updateurl;
    private String minsdkversion;
    private String versiondesc;

    private String mandatoryUpdate;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(String versioncode) {
        this.versioncode = versioncode;
    }

    public String getVersionname() {
        return versionname;
    }

    public void setVersionname(String versionname) {
        this.versionname = versionname;
    }

    public String getUpdateurl() {
        return updateurl;
    }

    public void setUpdateurl(String updateurl) {
        this.updateurl = updateurl;
    }

    public String getVersiondesc() {
        return versiondesc;
    }

    public void setVersiondesc(String versiondesc) {
        this.versiondesc = versiondesc;
    }

    public String getMinsdkversion() {
        return minsdkversion;
    }

    public void setMinsdkversion(String minsdkversion) {
        this.minsdkversion = minsdkversion;
    }

    public String getMandatoryUpdate() {
        return mandatoryUpdate;
    }

    public void setMandatoryUpdate(String mandatoryUpdate) {
        this.mandatoryUpdate = mandatoryUpdate;
    }
}
