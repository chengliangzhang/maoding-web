package com.maoding.dynamicForm.service.impl;

import com.maoding.core.base.service.GenericService;
import com.maoding.core.util.BeanUtils;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.dynamicForm.dao.DynamicFormDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldSelectableValueDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldValueDao;
import com.maoding.dynamicForm.dto.*;
import com.maoding.dynamicForm.entity.DynamicFormEntity;
import com.maoding.dynamicForm.entity.DynamicFormFieldSelectableValueEntity;
import com.maoding.dynamicForm.entity.DynamicFormFieldValueEntity;
import com.maoding.dynamicForm.service.DynamicFormFieldValueService;
import com.maoding.financial.entity.ExpMainEntity;
import com.maoding.financial.service.ExpMainService;
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
    private ExpMainService expMainService;

    /**
     * 作者：FYT
     * 日期：2018/9/13
     * 描述：保存审核表单内容
     * @return
     * @throws Exception
     */
    @Override
    public int saveAuditDetail(SaveDynamicAuditDTO dto) throws Exception {

        //todo 1.添加审批表单主表的记录
        ExpMainEntity mainEntity = new ExpMainEntity();
        mainEntity.initEntity();
        mainEntity.setExpDate(DateUtils.getDate());
        mainEntity.setApproveStatus("0");
        mainEntity.setCompanyId(dto.getCurrentCompanyId());
        mainEntity.setCompanyUserId(dto.getCurrentCompanyUserId());

        String mainId = mainEntity.getId();
        //todo 2.添加页面上的fieldId 对应的 value
        for(DynamicFormFieldValueDTO valueDTO: dto.getFiledList()){
            DynamicFormFieldValueEntity dynamicFormFieldValueEntity = new DynamicFormFieldValueEntity();
            dynamicFormFieldValueEntity.setMainId(mainEntity.getId());
            dynamicFormFieldValueEntity.initEntity();
            dynamicFormFieldValueEntity.setFieldId(valueDTO.getId());
            dynamicFormFieldValueEntity.setFieldValue(valueDTO.getFieldValue());
            dynamicFormFieldValueDao.insert(dynamicFormFieldValueEntity);
        }

        //todo 3.添加明细的List对应value
        Map<String,List<DynamicFormFieldValueDTO>> detailList = dto.getDetailList();
        if(StringUtil.isNullOrEmpty(detailList)){
            DynamicFormFieldValueEntity detailFieldValue = new DynamicFormFieldValueEntity();
            Set<String> keys = detailList.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                //todo 3.1 先设置好主记录数据
                detailFieldValue.initEntity();
                detailFieldValue.setFieldId(key);
                detailFieldValue.setMainId(mainId);
                double filedValue = 0;
                boolean isStatistic = false;
                //todo 3.2 保存明细中每个控件的值
                for (DynamicFormFieldValueDTO valueDTO : detailList.get(key)) {
                    DynamicFormFieldValueEntity childDetailFieldValue = new DynamicFormFieldValueEntity();
                    childDetailFieldValue.initEntity();
                    childDetailFieldValue.setMainId(mainId);
                    childDetailFieldValue.setFieldValuePid(detailFieldValue.getId());//通过pid一样，判断出是同一条明细中的数据
                    childDetailFieldValue.setFieldId(valueDTO.getId());
                    childDetailFieldValue.setFieldValue(valueDTO.getFieldValue());
                    dynamicFormFieldValueDao.insert(childDetailFieldValue);
                    if(valueDTO.getIsStatistics()==1 && valueDTO.getRequiredType()==1){
                        isStatistic = true;
                        filedValue += Double.parseDouble(childDetailFieldValue.getFieldValue());
                    }
                }

                //如果参与统计，则把值累加到主记录上去
                if(isStatistic){
                    detailFieldValue.setFieldValue(filedValue+"");
                }
                //todo 3.3 保存明细主记录
                dynamicFormFieldValueDao.insert(detailFieldValue);
            }
        }
        //todo 4.插入主表记录，因为，在插入主表记录的时候，要启动流程，分条件流程还需要携带条件值，所有放在最后插入
        this.expMainService.saveExpMain(mainEntity,dto);
        return 1;
    }

}
