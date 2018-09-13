package com.maoding.dynamicForm.service.impl;

import com.maoding.core.base.service.GenericService;
import com.maoding.core.util.BeanUtils;
import com.maoding.dynamicForm.dao.DynamicFormDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldSelectableValueDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldValueDao;
import com.maoding.dynamicForm.dto.*;
import com.maoding.dynamicForm.entity.DynamicFormEntity;
import com.maoding.dynamicForm.entity.DynamicFormFieldEntity;
import com.maoding.dynamicForm.entity.DynamicFormFieldSelectableValueEntity;
import com.maoding.dynamicForm.service.DynamicFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 动态表单自定义的数据层接口
 */
@Service("dynamicFormService")
public class DynamicFormServiceImpl extends GenericService<DynamicFormEntity> implements DynamicFormService {

    @Autowired
    private DynamicFormDao dynamicFormDao;
    @Autowired
    private DynamicFormFieldDao dynamicFormFieldDao;
    @Autowired
    private DynamicFormFieldSelectableValueDao dynamicFormFieldSelectableValueDao;
    @Autowired
    private DynamicFormFieldValueDao dynamicFormFieldValueDao;


    /**
     * 作者：FYT
     * 日期：2018/9/13
     * 描述：保存审核表单模板
     * @return
     * @throws Exception
     */
    @Override
    public int insertDynamicForm(SaveDynamicFormDTO dto) throws Exception {
        //保存主表(外层审核表)
        DynamicFormEntity dynamicFormEntity = new DynamicFormEntity();
        dynamicFormEntity.initEntity();
        dynamicFormEntity.setCompanyId(dto.getCompanyId());
        dynamicFormEntity.setFormName(dto.getFormName());
        dynamicFormEntity.setFormType(dto.getFormType());
        dynamicFormEntity.setStatus(dto.getStatus());
        dynamicFormEntity.setDeleted(0);
        int c = dynamicFormDao.insert(dynamicFormEntity);
        //保存主表的子表（控件表）
        for (DynamicFormFieldDTO formFieldDTO:  dto.getFieldList()){
            formFieldDTO.setFormId(dynamicFormEntity.getId());
            formFieldDTO.setId(saveDynamicFormField(formFieldDTO));
            //保存明细表
            for(DynamicFormFieldDTO formFieldDTO2: formFieldDTO.getDetailFieldList()) {
                formFieldDTO2.setFormId(dynamicFormEntity.getId());
                formFieldDTO2.setFieldPid(formFieldDTO.getId());
                saveDynamicFormField(formFieldDTO2);
            }
        }
        return c;
    }

    //保存审核表单模板:复制对象抽取的方法
    private String saveDynamicFormField(DynamicFormFieldDTO formFieldDTO){
        //将DTO对象复制到entity
        DynamicFormFieldEntity dynamicFormFieldEntity = BeanUtils.createFrom(formFieldDTO,DynamicFormFieldEntity.class);
        //补充entity缺失值
        dynamicFormFieldEntity.initEntity();
        dynamicFormFieldEntity.setDeleted(0);
        //添加到数据库
        dynamicFormFieldDao.insert(dynamicFormFieldEntity);
        //控件相同属性多个值，即遍历添加
        List<DynamicFormFieldSelectedValueDTO> dynamicFormFieldSelectedValueList = formFieldDTO.getFieldSelectedValueList();
        int seq = 1;
        for (DynamicFormFieldSelectedValueDTO valueDTO: dynamicFormFieldSelectedValueList){
            DynamicFormFieldSelectableValueEntity dynamicFormFieldSelectableValueEntity = BeanUtils.createFrom(valueDTO,DynamicFormFieldSelectableValueEntity.class);
            dynamicFormFieldSelectableValueEntity.initEntity();
            dynamicFormFieldSelectableValueEntity.setFieldId(dynamicFormFieldEntity.getId());
            dynamicFormFieldSelectableValueEntity.setDeleted(0);
            dynamicFormFieldSelectableValueEntity.setSeq(seq++);
            dynamicFormFieldSelectableValueDao.insert(dynamicFormFieldSelectableValueEntity);
        }

        return dynamicFormFieldEntity.getId();
    }

    /**
     * 描述       获取动态窗口组件位置、标题等信息
     * 日期       2018/9/13
     * @author   张成亮
     **/
    @Override
    public FormDetailDTO getFormDetail(FormDetailQueryDTO query) {
        return null;
    }
}
