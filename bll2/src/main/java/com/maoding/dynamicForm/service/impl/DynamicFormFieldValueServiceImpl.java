package com.maoding.dynamicForm.service.impl;

import com.maoding.core.base.service.GenericService;
import com.maoding.core.util.StringUtil;
import com.maoding.dynamicForm.dao.DynamicFormDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldSelectableValueDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldValueDao;
import com.maoding.dynamicForm.dto.*;
import com.maoding.dynamicForm.entity.DynamicFormFieldSelectableValueEntity;
import com.maoding.dynamicForm.entity.DynamicFormFieldValueEntity;
import com.maoding.dynamicForm.service.DynamicFormFieldValueService;
import com.maoding.financial.entity.ExpMainEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 动态表单自定义的数据层接口- 自定义数据字段- 可选择提供
 */
@Service("dynamicFormFieldSelectableValueService")
public class DynamicFormFieldValueServiceImpl extends GenericService<DynamicFormFieldSelectableValueEntity> implements DynamicFormFieldValueService {

    @Autowired
    private DynamicFormDao dynamicFormDao;
    @Autowired
    private DynamicFormFieldDao dynamicFormFieldDao;
    @Autowired
    private DynamicFormFieldValueDao dynamicFormFieldValueDao;
    @Autowired
    private DynamicFormFieldSelectableValueDao dynamicFormFieldSelectableValueDao;
    /**
     * 作者：FYT
     * 日期：2018/9/13
     * 描述：保存审核表单内容
     * @return
     * @throws Exception
     */
    @Override
    public int saveAuditDetail(SaveDynamicAuditDTO dto) throws Exception {

        //todo 1.添加页面上的fieldId 对应的 value
        DynamicFormFieldValueEntity dynamicFormFieldValueEntity = new DynamicFormFieldValueEntity();
        ExpMainEntity mainEntity = new ExpMainEntity();
        mainEntity.initEntity();
        dynamicFormFieldValueEntity.setMainId(mainEntity.getId());
        for(DynamicFormFieldValueDTO valueDTO: dto.getFiledList()){
            dynamicFormFieldValueEntity.initEntity();
            dynamicFormFieldValueEntity.setFieldId(valueDTO.getId());
            dynamicFormFieldValueEntity.setFieldValue(valueDTO.getFieldValue());
            dynamicFormFieldValueDao.insert(dynamicFormFieldValueEntity);
        }
        //todo 2.添加明细的List对应value
        Map<String,List<DynamicFormFieldValueDTO>> detailList = dto.getDetailList();
        if(StringUtil.isNullOrEmpty(detailList)){
            DynamicFormFieldValueEntity dynamicFormFieldValueEntity2 = new DynamicFormFieldValueEntity();
            Set<String> keys = detailList.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                List<DynamicFormFieldValueDTO> fieldValueDTOList = detailList.get(key);
                for (DynamicFormFieldValueDTO valueDTO : fieldValueDTOList) {
                    dynamicFormFieldValueEntity2.initEntity();
                    dynamicFormFieldValueEntity2.setFieldId(valueDTO.getId());
                    dynamicFormFieldValueEntity2.setFieldValue(valueDTO.getFieldValue());
                    dynamicFormFieldValueEntity2.setFieldValue(valueDTO.getFieldValue());
                    dynamicFormFieldValueDao.insert(dynamicFormFieldValueEntity2);
                }
            }
        }
        return 1;
    }

}
