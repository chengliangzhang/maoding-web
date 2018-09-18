package com.maoding.dynamicForm.service.impl;

import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.util.BeanUtils;
import com.maoding.core.util.StringUtils;
import com.maoding.dynamicForm.dao.DynamicFormDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldDao;
import com.maoding.dynamicForm.dao.DynamicFormGroupDao;
import com.maoding.dynamicForm.dto.DynamicFormFieldDTO;
import com.maoding.dynamicForm.dto.FormFieldQueryDTO;
import com.maoding.dynamicForm.dto.FormGroupDTO;
import com.maoding.dynamicForm.dto.FormGroupEditDTO;
import com.maoding.dynamicForm.entity.DynamicFormEntity;
import com.maoding.dynamicForm.entity.DynamicFormFieldEntity;
import com.maoding.dynamicForm.entity.DynamicFormGroupEntity;
import com.maoding.dynamicForm.service.DynamicFormGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("dynamicFormGroupService")
public class DynamicFormGroupServiceImpl extends NewBaseService implements DynamicFormGroupService {


    @Autowired
    private DynamicFormGroupDao dynamicFormGroupDao;

    @Autowired
    private DynamicFormDao dynamicFormDao;

    @Autowired
    private DynamicFormFieldDao dynamicFormFieldDao;

    /**
     * 描述       添加及更改动态窗口群组
     * 日期       2018/9/14
     *
     * @param request
     * @author 张成亮
     */
    @Override
    public FormGroupDTO saveDynamicFormGroup(FormGroupEditDTO request) {
        DynamicFormGroupEntity updatedEntity = null;
        //如果entity内的id不为空,则从数据库内读取，如果为空，则新增，如果不为空，则更改
        if (StringUtils.isNotEmpty(request.getId())){
            updatedEntity = dynamicFormGroupDao.selectById(request.getId());
            if (updatedEntity != null) {
                //修改
                BeanUtils.copyProperties(request, updatedEntity);
                updatedEntity.setCompanyId(request.getCurrentCompanyId());
                updatedEntity.setGroupName(request.getName());
                updatedEntity.setUpdateBy(request.getAccountId());
                updatedEntity.resetUpdateDate();
                dynamicFormGroupDao.updateById(updatedEntity);
            }
        }

        //如果entity的id为空，或者数据库内没有此记录，则新增记录
        if (updatedEntity == null) {
            updatedEntity = BeanUtils.createFrom(request,DynamicFormGroupEntity.class);
            updatedEntity.initEntity();
            updatedEntity.setDeleted(0);
            updatedEntity.setCompanyId(request.getCurrentCompanyId());
            updatedEntity.setGroupName(request.getName());
            updatedEntity.setCreateBy(request.getAccountId());
            dynamicFormGroupDao.insert(updatedEntity);
        }

        FormGroupDTO group = BeanUtils.createFrom(updatedEntity,FormGroupDTO.class);
        group.setName(updatedEntity.getGroupName());
        return group;
    }

    @Override
    public int deleteDynamicFormGroup(FormGroupEditDTO request) throws Exception {
        DynamicFormGroupEntity updatedEntity = new DynamicFormGroupEntity();
        updatedEntity.setId(request.getId());
        updatedEntity.setDeleted(1);
        return dynamicFormGroupDao.updateById(updatedEntity);
    }

    @Override
    public void initDynamicFormGroup(String companyId) throws Exception {
        if(!this.dynamicFormGroupDao.isInitFormGroup(companyId)){

            List<DynamicFormGroupEntity> list = dynamicFormGroupDao.listDefaultFormGroup();
            list.stream().forEach(group->{
                String formType = group.getId();
                //1.插入group
                group.initEntity();
                group.setDeleted(0);
                group.setCompanyId(companyId);
                dynamicFormGroupDao.insert(group);
                //2.查询form
                List<DynamicFormEntity> formList = dynamicFormDao.listDynamicFormByType(formType);
                formList.stream().forEach(form->{
                    String formId = form.getId();
                    //插入 form
                    form.initEntity();
                    form.setCompanyId(companyId);
                    form.setFormType(group.getId());
                    form.setDeleted(0);
                    form.setStatus(1);
                    dynamicFormDao.insert(form);
                    //3.查询field
                    FormFieldQueryDTO fieldQuery = new FormFieldQueryDTO();
                    fieldQuery.setFormId(formId);
                    List<DynamicFormFieldDTO> fieldList = dynamicFormFieldDao.listFormFieldByFormId(fieldQuery);
                    //4.处理字段控件
                    initDynamicFormField(fieldList,form);
                });
            });
        }
    }

    void initDynamicFormField(List<DynamicFormFieldDTO> fieldList,DynamicFormEntity form) {
        fieldList.stream().forEach(field->{
            DynamicFormFieldEntity fieldEntity = insertField(field,form,null);
            field.getDetailFieldList().stream().forEach(child->{
                insertField(field,form,fieldEntity.getId());
            });

        });
    }

    DynamicFormFieldEntity  insertField(DynamicFormFieldDTO field,DynamicFormEntity form,String pid){
        DynamicFormFieldEntity fieldEntity = BeanUtils.createFrom(field,DynamicFormFieldEntity.class);
        fieldEntity.initEntity();
        fieldEntity.setDeleted(0);
        fieldEntity.setFieldPid(pid);
        fieldEntity.setFormId(form.getId());
        dynamicFormFieldDao.insert(fieldEntity);
        return fieldEntity;
    }
}
