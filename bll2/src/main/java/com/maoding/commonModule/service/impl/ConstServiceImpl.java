package com.maoding.commonModule.service.impl;

import com.maoding.commonModule.dao.ConstDao;
import com.maoding.commonModule.dto.ContentDTO;
import com.maoding.commonModule.dto.TemplateQueryDTO;
import com.maoding.commonModule.dto.UpdateConstDTO;
import com.maoding.commonModule.entity.CustomConstEntity;
import com.maoding.commonModule.service.ConstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * 日    期 : 2018/6/26 14:11
 * 描    述 :
 */
@Service("constService")
public class ConstServiceImpl implements ConstService {

    @Autowired
    private ConstDao constDao;

    /**
     * @param request 申请插入的记录的基本数据
     * @return
     * @description 添加常量
     * @author 张成亮
     * @date 2018/6/26 12:47
     * @return 插入记录的id
     **/
    @Override
    public String insertConst(UpdateConstDTO request) {
        CustomConstEntity entity = new CustomConstEntity();
        entity.initEntity();
        entity.setProjectId(request.getProjectId());
        entity.setClassicId(request.getClassicId());
        entity.setCodeId((short)0);
        entity.setTitle(request.getTitle());
        entity.setDeleted(false);
        constDao.insert(entity);
        return entity.getId();
    }

    /**
     * @param request
     * @return
     * @description 添加常量
     * @author 张成亮
     * @date 2018/6/26 12:47
     **/
    @Override
    public void updateConst(UpdateConstDTO request) {
        CustomConstEntity entity = new CustomConstEntity();
        entity.setId(request.getId());
        entity.setDeleted(false);
        entity.resetUpdateDate();
        entity.setTitle(request.getTitle());
        constDao.updateById(entity);
    }

    /**
     * @param id 要删除的常量的唯一编号
     * @return
     * @description 删除常量
     * @author 张成亮
     * @date 2018/6/26 12:47
     **/
    @Override
    public void deleteConst(String id) {
        CustomConstEntity entity = new CustomConstEntity();
        entity.setId(id);
        entity.setDeleted(true);
        constDao.updateById(entity);
    }

    /**
     * @param query 过滤条件
     * @return 符合过滤条件的模板内容
     * @author 张成亮
     * @date 2018/7/4
     * @description 查询模板内容
     **/
    @Override
    public List<ContentDTO> listTemplateContent(TemplateQueryDTO query) {
        return constDao.listTemplateContent(query);
    }
}
