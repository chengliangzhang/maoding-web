package com.maoding.commonModule.dao;

import com.maoding.commonModule.dto.ContentDTO;
import com.maoding.commonModule.dto.TemplateQueryDTO;
import com.maoding.commonModule.entity.CustomConstEntity;
import com.maoding.core.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * 日    期 : 2018/6/26 12:50
 * 描    述 :
 */
@Repository
public interface ConstDao extends BaseDao<CustomConstEntity> {
    /**
     * @description 查询模板内容
     * @author  张成亮
     * @date    2018/7/4 20:06
     * @param   query 查询过滤条件
     * @return  模板包含的组件列表
     **/
    List<ContentDTO> listTemplateContent(TemplateQueryDTO query);
}
