package com.maoding.dynamicForm.service.impl;

import com.maoding.commonModule.dao.ConstDao;
import com.maoding.commonModule.dto.WidgetDTO;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.util.*;
import com.maoding.dynamicForm.dao.*;
import com.maoding.dynamicForm.dto.*;
import com.maoding.dynamicForm.entity.DynamicFormEntity;
import com.maoding.dynamicForm.entity.DynamicFormFieldEntity;
import com.maoding.dynamicForm.entity.DynamicFormFieldSelectableValueEntity;
import com.maoding.dynamicForm.entity.DynamicFormGroupEntity;
import com.maoding.dynamicForm.service.DynamicFormService;
import com.maoding.process.dao.ProcessTypeDao;
import com.maoding.process.entity.ProcessTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 动态表单自定义的数据层接口
 */
@Service("dynamicFormService")
public class DynamicFormServiceImpl extends NewBaseService implements DynamicFormService {

    @Autowired
    private DynamicFormDao dynamicFormDao;

    @Autowired
    private DynamicFormFieldDao dynamicFormFieldDao;

    @Autowired
    private DynamicFormFieldSelectableValueDao dynamicFormFieldSelectableValueDao;

    @Autowired
    private DynamicFormFieldValueDao dynamicFormFieldValueDao;

    @Autowired
    private DynamicFormGroupDao dynamicFormGroupDao;

    @Autowired
    private ProcessTypeDao processTypeDao;

    @Autowired
    private ConstDao constDao;

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
        FormDTO form = changeForm(dto);
        DynamicFormEntity dynamicFormEntity = BeanUtils.createFrom(form,DynamicFormEntity.class);
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

        //将模板保存到ProcessType与之关联，在页面（后台管理-审批管理 中显示）
        ProcessTypeEntity processTypeEntity = processTypeDao.selectByTargetType(dto);
        if(StringUtil.isNullOrEmpty(processTypeEntity)){
            processTypeEntity.initEntity();
            processTypeEntity.setCompanyId(dto.getCompanyId());
            processTypeEntity.setTargetType(dynamicFormEntity.getId());//业务类型
            processTypeEntity.setType(1);//流程类型
            processTypeEntity.setStatus(StringUtil.isNullOrEmpty(dto.getStatus())?0:dto.getStatus());//业务类型
            processTypeEntity.setDeleted(0);
            processTypeEntity.setFormId(dynamicFormEntity.getId());
            processTypeEntity.setSeq(processTypeDao.selectMaxSeq(dto.getCurrentCompanyId())+1);
            processTypeDao.insert(processTypeEntity);
        }else{
            processTypeEntity.setTargetType(dto.getFormType());//业务类型
            processTypeEntity.setFormId(dynamicFormEntity.getId());
            processTypeDao.updateById(processTypeEntity);
        }
        return 1;
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

         //保存主表(外层审核表)
         FormDTO form = changeForm(dto);
         DynamicFormEntity dynamicFormEntity = BeanUtils.createFrom(form,DynamicFormEntity.class);
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
         return 1;
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

    /**
     * 描述       添加及更改动态窗口模板信息
     * 日期       2018/9/14
     *
     * @param request
     * @author 张成亮
     */
    @Override
    public FormDTO changeForm(SaveDynamicFormDTO request) {
        DynamicFormEntity updatedEntity = null;
        //如果entity内的id不为空,则从数据库内读取，如果为空，则新增，如果不为空，则更改
        if (StringUtils.isNotEmpty(request.getId())){
            updatedEntity = dynamicFormDao.selectById(request.getId());
            if (updatedEntity != null) {
                //修改
                BeanUtils.copyProperties(request, updatedEntity);
                updatedEntity.setCompanyId(request.getCurrentCompanyId());
                updatedEntity.setUpdateBy(request.getAccountId());
                updatedEntity.resetUpdateDate();
                updatedEntity.setDeleted(0);
                dynamicFormDao.updateById(updatedEntity);
            }
        }

        //如果entity的id为空，或者数据库内没有此记录，则新增记录
        if (updatedEntity == null) {
            updatedEntity = BeanUtils.createFrom(request,DynamicFormEntity.class);
            updatedEntity.initEntity();
            updatedEntity.setCompanyId(request.getCurrentCompanyId());
            updatedEntity.setCreateBy(request.getAccountId());
            updatedEntity.setStatus(DigitUtils.parseInt(request.getStatus()));
            updatedEntity.setDeleted(0);

            //对数据进行检查
            TraceUtils.check(ObjectUtils.isNotEmpty(updatedEntity.getFormType()),"!formType不能为空");
            dynamicFormDao.insert(updatedEntity);
        }

        FormDTO form = BeanUtils.createFrom(updatedEntity,FormDTO.class);
        form.setName(updatedEntity.getFormName());
        return form;
    }

    /**
     * 描述       添加及更改动态窗口控件信息
     * 日期       2018/9/14
     *
     * @param request
     * @author 张成亮
     */
    @Override
    public List<DynamicFormFieldDTO> changeFormDetail(SaveDynamicFormDTO request) {
        return null;
    }

    /**
     * 作者：FYT
     * 日期：2018/9/17
     * 描述：是否启用动态表单 isEdit： 1启用，0禁用
     */
    @Override
    public int setDynamicFormIsEdit(SaveDynamicFormDTO dto) throws Exception {
        //isEdit： 1启用，0禁用
        ProcessTypeEntity entity = BeanUtils.createFrom(dto, ProcessTypeEntity.class);
        entity.setDeleted(1);
        processTypeDao.updateById(entity);
        return 1;
    }


    /**
     * 描述       准备用于编辑的动态窗口
     * 要编辑的动态表单模板编号为空则返回空白动态表单信息，加载指定表单信息
     * 日期       2018/9/18
     *
     * @param request 目标表单模板的部分属性
     * @author 张成亮
     */
    @Override
    public FormWithOptionalDTO prepareFormToEdit(@NotNull FormEditDTO request) {
        //获取动态模板信息，并复制已设置的编辑信息到目标模板
        FormWithOptionalDTO form = new FormWithOptionalDTO();
        if (StringUtils.isNotEmpty(request.getId())) {
            FormQueryDTO query = new FormQueryDTO();
            query.setId(request.getId());
            FormDTO origForm = getFormDetail(query);
            BeanUtils.copyProperties(origForm, form);
        }
        BeanUtils.copyProperties(request, form);

        //读取可选控件信息
        List<WidgetDTO> widgetList = constDao.listWidget();
        form.setOptionalWidgetList(widgetList);

        //读取可选群组信息
        List<DynamicFormGroupEntity> groupList = dynamicFormGroupDao.listFormGroupByCompanyId(request.getCurrentCompanyId());
        form.setFormGroupList(BeanUtils.createListFrom(groupList,FormGroupDTO.class));

        return form;
    }



    /**
     * 作者：FYT
     * 日期：2018/9/17
     * 描述：后台管理-审批管理-操作，seq排序对调(交换seq值)
     */
    @Override
    public int setDynamicFormSeq(FormGroupEditDTO dto) throws Exception {
        //从数据库根据id查询seq
        ProcessTypeEntity entity1 = processTypeDao.selectById(dto.getDynamicFromId1());
        ProcessTypeEntity entity2 = processTypeDao.selectById(dto.getDynamicFromId2());

        //值交换
        exchangeValue(entity1,entity2);
        processTypeDao.updateById(entity1);
        processTypeDao.updateById(entity2);

        return 1;
    }


    private void exchangeValue(ProcessTypeEntity entity1,ProcessTypeEntity entity2){

        Integer dtoSeq = entity1.getSeq();
        Integer dto2Seq = entity2.getSeq();
        dtoSeq = dtoSeq^dto2Seq;
        dto2Seq = dtoSeq^dto2Seq;
        dtoSeq = dtoSeq^dto2Seq;
        entity1.setSeq(dtoSeq);
        entity2.setSeq(dto2Seq);
    }

    /**
     * 作者：FYT
     * 日期：2018/9/18
     * 描述：后台管理-审批管理-操作，对调分组seq排序对调(交换seq值) （如：行政审批 与 财务审批 位置对调）
     */
    @Override
    public int updateDynamicFormSeq(FormGroupEditDTO dto) throws Exception {
        //从数据库根据id查询seq
        DynamicFormGroupEntity entity1 = dynamicFormGroupDao.selectById(dto.getDynamicFromId1());
        DynamicFormGroupEntity entity2 = dynamicFormGroupDao.selectById(dto.getDynamicFromId2());

        //值交换
        exchangeValue(entity1,entity2);
        dynamicFormGroupDao.updateById(entity1);
        dynamicFormGroupDao.updateById(entity2);

        return 1;
    }
    private void exchangeValue(DynamicFormGroupEntity entity1,DynamicFormGroupEntity entity2){

        Integer dtoSeq = entity1.getSeq();
        Integer dto2Seq = entity2.getSeq();
        dtoSeq = dtoSeq^dto2Seq;
        dto2Seq = dtoSeq^dto2Seq;
        dtoSeq = dtoSeq^dto2Seq;
        entity1.setSeq(dtoSeq);
        entity2.setSeq(dto2Seq);
    }

}
