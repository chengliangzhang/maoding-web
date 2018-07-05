package com.maoding.commonModule.service;

import com.maoding.commonModule.dto.ContentDTO;
import com.maoding.commonModule.dto.TemplateQueryDTO;
import com.maoding.commonModule.dto.UpdateConstDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * 日    期 : 2018/6/26 12:47
 * 描    述 :
 */
public interface ConstService {
    short CONST_TYPE_BUILT_TYPE = 33;

    /**
     * @description 添加常量
     * @author  张成亮
     * @date    2018/6/26 12:47
     * @param
     * @return
     **/
    String insertConst(UpdateConstDTO request);

    /**
     * @description 添加常量
     * @author  张成亮
     * @date    2018/6/26 12:47
     * @param
     * @return
     **/
    void updateConst(UpdateConstDTO request);

    /**
     * @description 更改常量
     * @author  张成亮
     * @date    2018/6/26 12:47
     * @param
     * @return
     **/
    void deleteConst(String id);

    /**
     * @author  张成亮
     * @date    2018/7/4
     * @description     查询模板内容
     * @param   query 过滤条件
     * @return  符合过滤条件的模板内容
     **/
    List<ContentDTO> listTemplateContent(TemplateQueryDTO query);
}
