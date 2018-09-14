package com.maoding.dynamicForm.service.impl;

import com.maoding.core.base.service.GenericService;
import com.maoding.core.util.*;
import com.maoding.core.util.StringUtil;
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

import javax.validation.constraints.NotNull;
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
    public int insertDynamicForm (SaveDynamicFormDTO dto) throws Exception{

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
     * 作者：FYT
     * 日期：2018/9/14
     * 描述：保存/修改审核表单模板(暂时未启用接口)
     * @return
     * @throws Exception
     */
    public int saveAndUpdateDynamicFormField(SaveDynamicFormDTO dto){

         DynamicFormEntity dynamicFormEntity = new DynamicFormEntity();
         //保存主表(外层审核表)
         if(StringUtil.isNullOrEmpty(dto.getId())){
             dynamicFormEntity.initEntity();
             dynamicFormEntity.setCompanyId(dto.getCompanyId());
             dynamicFormEntity.setDeleted(0);
         }else{
             dynamicFormEntity.setId(dto.getId());
         }
         dynamicFormEntity.setFormName(dto.getFormName());
         dynamicFormEntity.setFormType(dto.getFormType());
         dynamicFormEntity.setStatus(dto.getStatus());
         int c = dynamicFormDao.insert(dynamicFormEntity);
         //保存主表的子表（控件表）
         for (DynamicFormFieldDTO formFieldDTO:  dto.getFieldList()){
             formFieldDTO.setFormId(dynamicFormEntity.getId());
             formFieldDTO.setId(saveAndUpdateDynamicFormField(formFieldDTO,dto.getId(),dynamicFormEntity.getId()));
             //保存明细表
             for(DynamicFormFieldDTO formFieldDTO2: formFieldDTO.getDetailFieldList()) {
                 formFieldDTO2.setFormId(dynamicFormEntity.getId());
                 formFieldDTO2.setFieldPid(formFieldDTO.getId());
                     saveAndUpdateDynamicFormField(formFieldDTO2,dto.getId(),dynamicFormEntity.getId());
             }
         }
         return c;
     }

    private String saveAndUpdateDynamicFormField(DynamicFormFieldDTO formFieldDTO,String dtoId,String entityId){
        //将DTO对象复制到entity
        DynamicFormFieldEntity dynamicFormFieldEntity = BeanUtils.createFrom(formFieldDTO,DynamicFormFieldEntity.class);
        //如果之前有设置，则逻辑删除后再重新添加
        if (dtoId.equals(entityId)) {
            dynamicFormFieldEntity.setDeleted(1);
            dynamicFormFieldDao.updateById(dynamicFormFieldEntity);
        }

        //补充entity缺失值
        dynamicFormFieldEntity.initEntity();
        dynamicFormFieldEntity.setDeleted(0);
        //添加到数据库
        dynamicFormFieldDao.insert(dynamicFormFieldEntity);

        //控件相同属性多个值，即遍历添加
        List<DynamicFormFieldSelectedValueDTO> dynamicFormFieldSelectedValueList = formFieldDTO.getFieldSelectedValueList();
        //如果之前有设置，则逻辑删除后再重新添加
        if (dtoId.equals(entityId)){
            for (DynamicFormFieldSelectedValueDTO valueDTO: dynamicFormFieldSelectedValueList){
                DynamicFormFieldSelectableValueEntity dynamicFormFieldSelectableValueEntity = BeanUtils.createFrom(valueDTO,DynamicFormFieldSelectableValueEntity.class);
                dynamicFormFieldSelectableValueEntity.setDeleted(1);
                dynamicFormFieldSelectableValueDao.updateById(dynamicFormFieldSelectableValueEntity);
            }
        }
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
     * 作者：FYT
     * 日期：2018/9/14
     * 描述：审批表 启用/停用
     * @return
     * @throws Exception
     */
    @Override
    public int startOrStopDynamicForm(SaveDynamicFormDTO dto) throws Exception {
        //设置动态表单状态为1启用 0停用
        DynamicFormEntity dynamicFormEntity = new DynamicFormEntity();
        dynamicFormEntity.setId(dto.getId());
        dynamicFormEntity.setStatus(dto.getStatus());
        return dynamicFormDao.updateById(dynamicFormEntity);
    }

    /**
     * 作者：FYT
     * 日期：2018/9/14
     * 描述：审批表 删除
     * @return
     * @throws Exception
     */
    @Override
    public int deleteDynamicForm(SaveDynamicFormDTO dto) throws Exception {
        DynamicFormEntity dynamicFormEntity = new DynamicFormEntity();
        dynamicFormEntity.setId(dto.getId());
        dynamicFormEntity.setDeleted(1);
        return dynamicFormDao.updateById(dynamicFormEntity);
    }

    /**
     * 描述       查询动态窗口模板
     * 日期       2018/9/13
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public List<FormDTO> listForm(@NotNull FormQueryDTO query) {
        return dynamicFormDao.listForm(query);
    }

    /**
     * 描述       获取动态窗口组件位置、标题等信息
     * 日期       2018/9/13
     * @author   张成亮
     **/
    @Override
    public FormDTO getFormDetail(@NotNull FormQueryDTO query) {
        List<FormDTO> formList = listForm(query);
        TraceUtils.check((ObjectUtils.isNotEmpty(formList) && formList.size() == 1),"~动态表单查询结果错误");
        FormDTO form = ObjectUtils.getFirst(formList);
        return (form != null) ? getFormDetail(form) : null;
    }

    /**
     * 描述       补充动态窗口模板的控件的位置、标题、类型等信息
     * 日期       2018/9/13
     *
     * @param form
     * @author 张成亮
     */
    @Override
    public FormDTO getFormDetail(@NotNull FormDTO form) {
        FormFieldQueryDTO fieldQuery = new FormFieldQueryDTO();
        TraceUtils.check(StringUtils.isNotEmpty(form.getId()),"!form.id不能为空");
        fieldQuery.setFormId(form.getId());
        List<DynamicFormFieldDTO> fieldList = dynamicFormFieldDao.listFormField(fieldQuery);
        if (ObjectUtils.isNotEmpty(fieldList)){
            fieldList.forEach(field->{
                if (DigitUtils.parseInt(field.getRequiredType()) != 0){
                    FormFieldOptionalQueryDTO optionalQuery = new FormFieldOptionalQueryDTO();
                    optionalQuery.setFieldId(field.getId());
                    field.setFieldSelectedValueList(dynamicFormFieldSelectableValueDao.listOptional(optionalQuery));
                }
            });
        }
        form.setFieldList(fieldList);
        return form;
    }
}
