package com.maoding.project.dto;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/24
 * 类名: com.maoding.project.dto.ProjectNeedInfoDTO
 * 作者: 张成亮
 * 描述:
 **/
public class ProjectNeedInfoDTO {
    /** 需要填充基本信息 **/
    private boolean needFillBasic;

    /** 需要填充功能分类信息 **/
    private boolean needFillFunction;

    /** 需要填充合作者信息 **/
    private boolean needFillMember;

    /** 需要填充费用信息 **/
    private boolean needFillFee;

    public boolean isNeedFillBasic() {
        return needFillBasic;
    }

    public void setNeedFillBasic(boolean needFillBasic) {
        this.needFillBasic = needFillBasic;
    }

    public boolean isNeedFillFunction() {
        return needFillFunction;
    }

    public void setNeedFillFunction(boolean needFillFunction) {
        this.needFillFunction = needFillFunction;
    }

    public boolean isNeedFillMember() {
        return needFillMember;
    }

    public void setNeedFillMember(boolean needFillMember) {
        this.needFillMember = needFillMember;
    }

    public boolean isNeedFillFee() {
        return needFillFee;
    }

    public void setNeedFillFee(boolean needFillFee) {
        this.needFillFee = needFillFee;
    }
}
