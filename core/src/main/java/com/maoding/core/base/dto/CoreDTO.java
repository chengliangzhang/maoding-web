package com.maoding.core.base.dto;

import java.io.Serializable;

/**
 * 深圳市设计同道技术有限公司
 * @author : 张成亮
 * @date   : 2018/7/27
 * @package: CoreDTO
 * @description : 元素基类
 */
public class CoreDTO implements Serializable,Cloneable {
    /** 显示元素编号 */
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
