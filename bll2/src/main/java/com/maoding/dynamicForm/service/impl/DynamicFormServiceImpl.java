package com.maoding.dynamicForm.service.impl;

import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.dynamicForm.dao.DynamicFormDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldSelectableValueDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldValueDao;
import com.maoding.dynamicForm.dto.DynamicFormFieldDTO;
import com.maoding.dynamicForm.dto.SaveDynamicFormDTO;
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
     * 描述：保存审核表单样式
     * @return
     * @throws Exception
     */
    @Override
    public int insertDynamicForm(SaveDynamicFormDTO dto) throws Exception {

        DynamicFormEntity dynamicFormEntity = new DynamicFormEntity();
        DynamicFormFieldEntity dynamicFormFieldEntity = new DynamicFormFieldEntity();
        DynamicFormFieldSelectableValueEntity dynamicFormFieldSelectableValueEntity = new DynamicFormFieldSelectableValueEntity();

//        for(DynamicFormFieldDTO dto:dtoList){
//            dynamicFormEntity.initEntity();
//            dynamicFormEntity.setFormName(dto.getFormName());
//            dynamicFormEntity.setCompanyId(dto.getCompanyId());
//            dynamicFormEntity.setFormType(dto.getFormType());
//            dynamicFormEntity.setSeq(dto.getSeq());
//            dynamicFormEntity.setStatus(dto.getStatus());
//            dynamicFormEntity.setDeleted(dto.getDeleted());
//
//            dynamicFormFieldEntity.initEntity();
//            dynamicFormFieldEntity.setFormId(dynamicFormEntity.getId());
//            dynamicFormFieldEntity.setFieldPid(dto.getFieldPid());
//            dynamicFormFieldEntity.setFieldTitle(dto.getFieldTitle());
//            dynamicFormFieldEntity.setFieldType(dto.getFieldType());
//            dynamicFormFieldEntity.setFieldUnit(dto.getFieldUnit());
//            dynamicFormFieldEntity.setFieldTooltip(dto.getFieldTooltip());
//            dynamicFormFieldEntity.setFieldDefaultValue(dto.getFieldDefault_value());
//            dynamicFormFieldEntity.setFieldSelectValueType(dto.getFieldSelect_value_type());
//            dynamicFormFieldEntity.setSeqX(dto.getSeqX());
//            dynamicFormFieldEntity.setSeqY(dto.getSeqY());
//            dynamicFormFieldEntity.setRequiredType(dto.getRequiredType());
//            dynamicFormFieldEntity.setDeleted(dto.getDeleted());
//
//            dynamicFormFieldSelectableValueEntity.setFieldId(dynamicFormFieldEntity.getId());
//            dynamicFormFieldSelectableValueEntity.setSelectableValue(dto.getSelectableValue());
//            dynamicFormFieldSelectableValueEntity.setSelectableName(dto.getSelectableName());
//            dynamicFormFieldSelectableValueEntity.setSeq(dto.getSeq());
//            dynamicFormFieldSelectableValueEntity.setDeleted(dto.getDeleted());
//            //todo 保存
//            int i1 = dynamicFormDao.insert(dynamicFormEntity);
//            int i2 = dynamicFormFieldDao.insert(dynamicFormFieldEntity);
//            int i3 = dynamicFormFieldSelectableValueDao.insert(dynamicFormFieldSelectableValueEntity);
//
//            return i1==12&&i2==i3&&i3==1?1:0;
//        }
        return 1;
    }

    public AjaxMessage savetDynamicForm(List<DynamicFormFieldDTO> dtoList) throws Exception {
        return new AjaxMessage().setCode("0").setInfo("保存成功").setData(null);
    }
}
