package com.maoding.dynamicForm.service.impl;

import com.maoding.commonModule.dao.ConstDao;
import com.maoding.commonModule.dto.AuditCopyDataDTO;
import com.maoding.commonModule.dto.WidgetDTO;
import com.maoding.commonModule.dto.WidgetPropertyDTO;
import com.maoding.commonModule.service.AuditCopyService;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.util.*;
import com.maoding.dynamicForm.dao.*;
import com.maoding.dynamicForm.dto.*;
import com.maoding.dynamicForm.entity.DynamicFormEntity;
import com.maoding.dynamicForm.entity.DynamicFormFieldEntity;
import com.maoding.dynamicForm.entity.DynamicFormFieldSelectableValueEntity;
import com.maoding.dynamicForm.entity.DynamicFormGroupEntity;
import com.maoding.dynamicForm.service.DynamicFormGroupService;
import com.maoding.dynamicForm.service.DynamicFormService;
import com.maoding.process.dao.ProcessTypeDao;
import com.maoding.process.entity.ProcessTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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
    private DynamicFormGroupDao dynamicFormGroupDao;

    @Autowired
    private DynamicFormGroupService dynamicFormGroupService;

    @Autowired
    private ProcessTypeDao processTypeDao;

    @Autowired
    private ConstDao constDao;

    @Autowired
    private AuditCopyService auditCopyService;

    /**
     * 作者：FYT
     * 日期：2018/9/13
     * 描述：保存审核表单模板
     * @return
     * @throws Exception
     */
    @Override
    public int insertDynamicForm (SaveDynamicFormDTO dto) throws Exception{
        //将模板保存到ProcessType与之关联，在页面（后台管理-审批管理 中显示）
        ProcessTypeEntity processTypeEntity = processTypeDao.selectByTargetType(dto);
        //保存主表(外层审核表)
        FormDTO form = createForm(dto);
        TraceUtils.check(form != null);
        String formId = form.getId();
        //保存主表的子表（控件表）
        int seq = 1;
        for (DynamicFormFieldDTO formFieldDTO:  dto.getFieldList()){
            formFieldDTO.setFormId(formId);
            formFieldDTO.setSeqY(seq++);
            formFieldDTO.setId(saveDynamicFormField(formFieldDTO));
            //保存明细表
            int seq2 = 1;
            for(DynamicFormFieldDTO formFieldDTO2: formFieldDTO.getDetailFieldList()) {
                formFieldDTO2.setFormId(formId);
                formFieldDTO2.setFieldPid(formFieldDTO.getId());
                formFieldDTO2.setSeqY(seq2++);
                saveDynamicFormField(formFieldDTO2);
            }
        }

        if(processTypeEntity==null){
            if(StringUtil.isNullOrEmpty(dto.getFormType())){
                //查询其他模板，插入到其他模板中
                dto.setFormType(dynamicFormGroupService.getOtherDynamicFormGroup(dto.getCurrentCompanyId()).getId());
            }
            processTypeEntity = new ProcessTypeEntity();
            processTypeEntity.initEntity();
            processTypeEntity.setCompanyId(dto.getCurrentCompanyId());
            processTypeEntity.setTargetType(formId);//业务类型
            processTypeEntity.setType(1);//流程类型
            processTypeEntity.setStatus(StringUtil.isNullOrEmpty(dto.getStatus())?0:dto.getStatus());//业务类型
            processTypeEntity.setDeleted(0);
            processTypeEntity.setFormType(dto.getFormType());
            processTypeEntity.setFormId(formId);
            processTypeEntity.setSeq(processTypeDao.selectMaxSeq(dto.getCurrentCompanyId())+1);
            processTypeDao.insert(processTypeEntity);
        }else{
            processTypeEntity.setFormType(dto.getFormType());//业务类型
            processTypeEntity.setFormId(formId);
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
     * 描述       查询动态窗口模板
     * 日期       2018/9/13
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public List<FormDTO> listForm(@NotNull FormQueryDTO query) {
        List<FormDTO> formList = dynamicFormDao.listForm(query);
        return formList;
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
                    field.setFieldSelectedValueList(listOptional(field.getId()));
                }
                field.getDetailFieldList().stream().forEach(child->{
                    if (DigitUtils.parseInt(child.getRequiredType()) != 0) {
                        child.setFieldSelectedValueList(listOptional(child.getId()));
                    }
                });
            });
        }
        form.setFieldList(fieldList);
        return form;
    }

    private List<DynamicFormFieldSelectedValueDTO> listOptional(String fieldId){
        FormFieldOptionalQueryDTO optionalQuery = new FormFieldOptionalQueryDTO();
        optionalQuery.setFieldId(fieldId);
        return dynamicFormFieldSelectableValueDao.listOptional(optionalQuery);
    }

    /**
     * 描述       添加及更改动态窗口模板信息 保存动态表单主表数据，只做新增，不做更新操作
     * 日期       2018/9/14
     *
     * @param request
     * @author 张成亮
     */
    public FormDTO createForm(SaveDynamicFormDTO request) {
        DynamicFormEntity updatedEntity = BeanUtils.createFrom(request,DynamicFormEntity.class);
        updatedEntity.initEntity();
        updatedEntity.setCompanyId(request.getCurrentCompanyId());
        updatedEntity.setCreateBy(request.getAccountId());
        updatedEntity.setStatus(DigitUtils.parseInt(request.getStatus()));
        updatedEntity.setDeleted(0);
        dynamicFormDao.insert(updatedEntity);

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

        //读取可选控件信息并转换为前端所需格式
        List<WidgetDTO> widgetList = constDao.listWidget();
        List<DynamicFormFieldDTO> widgetForJsList = convertWidgetList(widgetList);
        form.setOptionalWidgetList(widgetForJsList);

        //读取可选群组信息
        FormGroupQueryDTO groupQuery = new FormGroupQueryDTO();
        groupQuery.setCurrentCompanyId(request.getCurrentCompanyId());
        List<FormGroupDTO> groupList = dynamicFormGroupDao.listFormGroup(groupQuery);
        form.setFormGroupList(groupList);

        return form;
    }

    //转换可选控件为前端所需形式
    private List<DynamicFormFieldDTO> convertWidgetList(List<WidgetDTO> widgetList){
        List<DynamicFormFieldDTO> widgetForJsList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(widgetList)) {
            for (WidgetDTO widget : widgetList) {
                DynamicFormFieldDTO widgetForJs = BeanUtils.createFrom(widget, DynamicFormFieldDTO.class);
                List<WidgetPropertyDTO> propertyList = widget.getPropertyList();
                if (ObjectUtils.isNotEmpty(propertyList)) {
                    for (WidgetPropertyDTO widgetProperty : propertyList) {
                        if (canSaveByCode(widgetProperty)){
                            BeanUtils.setProperty(widgetForJs,widgetProperty.getName(),widgetProperty.getDefaultValue());
                        }
                    }
                }
                widgetForJsList.add(widgetForJs);
            }
        }
        return widgetForJsList;
    }

    //是否可以通过属性名称设置
    private boolean canSaveByCode(WidgetPropertyDTO property){
        return DigitUtils.isTrue(property.getCanSaveByCode())
                && StringUtils.isNotEmpty(property.getName())
                && StringUtils.isNotEmpty(property.getDefaultValue());
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
