package com.maoding.commonModule.dao.impl;

import com.maoding.commonModule.dao.ConstDao;
import com.maoding.commonModule.dto.ContentDTO;
import com.maoding.commonModule.dto.TemplateQueryDTO;
import com.maoding.commonModule.dto.WidgetDTO;
import com.maoding.commonModule.entity.CustomConstEntity;
import com.maoding.core.base.dao.GenericDao;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * 日    期 : 2018/6/26 14:12
 * 描    述 :
 */
@Service("constDao")
public class ConstDaoImpl extends GenericDao<CustomConstEntity> implements ConstDao {
    @Override
    public List<ContentDTO> listTemplateContent(TemplateQueryDTO query) {
        return this.sqlSession.selectList("CustomConstEntityMapper.listTemplateContent", query);
    }

    /**
     * 描述       查询动态表单可以使用的控件信息
     * 日期       2018/9/18
     * @author 张成亮
     */
    @Override
    public List<WidgetDTO> listWidget() {
        return sqlSession.selectList("CustomConstEntityMapper.listWidget");
    }

}
