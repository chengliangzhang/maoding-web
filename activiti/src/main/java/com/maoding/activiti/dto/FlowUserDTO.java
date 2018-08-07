package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreShowDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/8/6
 * 类名: com.maoding.activiti.dto.GroupDTO
 * 作者: 张成亮
 * 描述: 流程用户信息
 **/
public class FlowUserDTO extends CoreShowDTO {
    /** id: activiti user id - company user id */

    /** 账号头像 */
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public FlowUserDTO(){}
    public FlowUserDTO(String id, String name, String imgUrl){
        setId(id);
        setName(name);
        setImgUrl(imgUrl);
    }
}
