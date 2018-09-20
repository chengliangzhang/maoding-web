package com.maoding.dynamicForm.service.impl;

import com.maoding.attach.dto.FileDataDTO;
import com.maoding.commonModule.dto.QueryCopyRecordDTO;
import com.maoding.commonModule.dto.SaveCopyRecordDTO;
import com.maoding.commonModule.service.AuditCopyService;
import com.maoding.commonModule.service.CopyRecordService;
import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.constant.CopyTargetType;
import com.maoding.core.constant.NetFileType;
import com.maoding.core.constant.SystemParameters;
import com.maoding.core.util.BeanUtils;
import com.maoding.core.util.StringUtils;
import com.maoding.dynamicForm.dao.DynamicFormFieldValueDao;
import com.maoding.dynamicForm.dto.*;
import com.maoding.dynamicForm.entity.DynamicFormFieldSelectableValueEntity;
import com.maoding.dynamicForm.entity.DynamicFormFieldValueEntity;
import com.maoding.dynamicForm.service.DynamicFormFieldValueService;
import com.maoding.dynamicForm.service.DynamicFormService;
import com.maoding.exception.CustomException;
import com.maoding.financial.dto.AuditDTO;
import com.maoding.financial.dto.AuditEditDTO;
import com.maoding.financial.dto.ExpTypeDTO;
import com.maoding.financial.entity.ExpMainEntity;
import com.maoding.financial.service.ExpCategoryService;
import com.maoding.financial.service.ExpMainService;
import com.maoding.org.dto.CompanyUserDataDTO;
import com.maoding.process.dao.ProcessTypeDao;
import com.maoding.process.entity.ProcessTypeEntity;
import com.maoding.process.service.ProcessService;
import com.maoding.project.dto.ProjectDTO;
import com.maoding.project.service.ProjectSkyDriverService;
import com.maoding.system.entity.DataDictionaryEntity;
import com.maoding.system.service.DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态表单自定义的数据层接口- 自定义数据字段- 可选择提供
 */
@Service("dynamicFormFieldSelectableValueService")
public class DynamicFormFieldValueServiceImpl extends GenericService<DynamicFormFieldSelectableValueEntity> implements DynamicFormFieldValueService {

    @Autowired
    private AuditCopyService auditCopyService;

    @Autowired
    private DynamicFormService dynamicFormService;

    @Autowired
    private DynamicFormFieldValueDao dynamicFormFieldValueDao;

    @Autowired
    private ExpMainService expMainService;

    @Autowired
    private ExpCategoryService expCategoryService;

    @Autowired
    private ProjectSkyDriverService projectSkyDriverService;

    @Autowired
    private CopyRecordService copyRecordService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private ProcessTypeDao processTypeDao;


    private Integer detailType = 9;
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
        if(StringUtils.isNotEmpty(dto.getTargetId())){
            mainEntity.setId(dto.getTargetId());
        }
        mainEntity.setType(dto.getType());
        String mainId = mainEntity.getId();
        //todo 2.添加页面上的fieldId 对应的 value
        for(DynamicFormFieldValueDTO valueDTO: dto.getFieldList()) {
            valueDTO.setMainId(mainId);
            if (valueDTO.getFieldType() == detailType) {//如果是明细
                DynamicFormFieldValueEntity fieldValue = this.saveDynamicFormFieldValue(valueDTO);
                String fieldValuePid = fieldValue.getId();
                //todo 3.添加明细的List对应value
                int groupNum = 1;
                boolean isStatistic = false;
                double filedValue = 0;
                for(int i = 0;i<valueDTO.getDetailFieldList().size();i++,groupNum++){
                    for(DynamicFormFieldValueDTO detailDTO:valueDTO.getDetailFieldList().get(i)){
                        detailDTO.setMainId(mainId);
                        detailDTO.setGroupNum(groupNum);
                        detailDTO.setFieldValuePid(fieldValuePid);
                        this.saveDynamicFormFieldValue(detailDTO);
                        if(detailDTO.getIsStatistics()==1 && detailDTO.getRequiredType()==1){
                            isStatistic = true;
                            filedValue += Double.parseDouble(detailDTO.getFieldValue());
                        }
                    }
                }
                if(isStatistic){
                    fieldValue.setFieldValue(filedValue+"");
                    dynamicFormFieldValueDao.updateById(fieldValue);
                }
            }else {
                this.saveDynamicFormFieldValue(valueDTO);
            }
        }

        //todo 4.插入主表记录，因为，在插入主表记录的时候，要启动流程，分条件流程还需要携带条件值，所有放在最后插入
        this.expMainService.saveExpMain(mainEntity,dto);
        //处理抄送人
        List<String> companyUserIdList = new ArrayList<>();
        dto.getCcCompanyUserList().stream().forEach(c->{
            companyUserIdList.add(c.getId());
        });
        this.expMainService.saveCopy(companyUserIdList,dto.getCurrentCompanyUserId(),mainId,mainId);
        return 1;
    }


    private DynamicFormFieldValueEntity saveDynamicFormFieldValue(DynamicFormFieldValueDTO valueDTO){
        DynamicFormFieldValueEntity dynamicFormFieldValueEntity = BeanUtils.createFrom(valueDTO,DynamicFormFieldValueEntity.class);
        dynamicFormFieldValueEntity.initEntity();
        dynamicFormFieldValueEntity.setFieldValue((String)valueDTO.getFieldValue());
        dynamicFormFieldValueDao.insert(dynamicFormFieldValueEntity);
        return dynamicFormFieldValueEntity;
    }


    @Override
    public Map<String,Object> initDynamicAudit(FormFieldQueryDTO dto) throws Exception {
        String id = dto.getId();
        if(StringUtils.isNotEmpty(id)){
            ExpMainEntity mainEntity = expMainService.selectById(dto.getId());
            dto.setFormId(mainEntity.getType());
        }
        ProcessTypeEntity processType = this.processTypeDao.getCurrentProcessTypeByFormId(dto.getCurrentCompanyId(),dto.getFormId());
        if(processType==null){
            throw new CustomException("参数错误");
        }
        String conditionValue = null;
        List<AuditDTO> auditList = null;
        List<CompanyUserDataDTO> ccCompanyUserList = null;
        List<FileDataDTO> expAttachList = null;
        Map<String,Object> result = new HashMap<>();
        //1.查询模板+数据
        List<DynamicFormFieldValueDTO> fieldList = dynamicFormFieldValueDao.listFormFieldValueByFormId(dto);
        SaveDynamicAuditDTO dynamicAudit = this.handleDynamicFormField(fieldList,dto);
        dynamicAudit.setType(dto.getFormId());
        //2.把数据匹配上
        if(StringUtils.isNotEmpty(id)){
            //2.3 查询附件
            Map<String,Object> param = new HashMap<>();
            param.put("targetId", id);
            param.put("type", NetFileType.EXPENSE_ATTACH);
            expAttachList = this.projectSkyDriverService.getAttachDataList(param);
            //4.查询知会人
            ccCompanyUserList = copyRecordService.getCopyRecode(new QueryCopyRecordDTO(id));
        }else {
            //查询模板中的知会人,待处理
            ccCompanyUserList = auditCopyService.listAuditCopyUser(dto.getCurrentCompanyId(),dto.getFormId());
        }
        //3.查询流程 返回流程标识，给前端控制是否要给审批人，以及按钮显示的控制
        AuditEditDTO audit = BeanUtils.createFrom(dto,AuditEditDTO.class);
        audit.setMainId(dto.getId());
        audit.setAuditType(processType.getTargetType());
        Map<String,Object> processData = processService.getCurrentTaskUser(audit,auditList,conditionValue);
        dynamicAudit.setExpAttachList(expAttachList);
        dynamicAudit.setCcCompanyUserList(ccCompanyUserList);
        result.put("dynamicAudit",dynamicAudit);
        result.putAll(processData);
        return result;
    }

    private SaveDynamicAuditDTO handleDynamicFormField(List<DynamicFormFieldValueDTO> fieldList,FormFieldQueryDTO dto) throws Exception{
        SaveDynamicAuditDTO dynamicAudit = new SaveDynamicAuditDTO();
        Map<String,Object> valueMap = new HashMap<>();
        for(DynamicFormFieldValueDTO field:fieldList){
            this.setSelectValue(field,dto,valueMap);
            if(field.getFieldType()==9){
                List<List<DynamicFormFieldValueDTO>> detailFieldValueList = new ArrayList<>();
                dto.setFieldPid(field.getFieldId());
                List<DynamicFormFieldValueDTO> detailList = dynamicFormFieldValueDao.listFormFieldValueByFormId(dto);
                Map<String,List<DynamicFormFieldValueDTO>> detailMap = new HashMap<>();
                for(DynamicFormFieldValueDTO val:detailList){
                    this.setSelectValue(val,dto,valueMap);
                    String key = val.getGroupNum()+"";
                    if(detailMap.containsKey(key)){
                        detailMap.get(key).add(val);
                    }else {
                        List<DynamicFormFieldValueDTO> detailList2 = new ArrayList<>();
                        detailList2.add(val);
                        detailMap.put(key,detailList2);
                        detailFieldValueList.add(detailList2);
                    }
                }
                field.setDetailFieldList(detailFieldValueList);
            }
        }
        dynamicAudit.setFieldList(fieldList);
        return dynamicAudit;
    }

    /**
     * valueMap 主要用于有候选值的，控件类型一样，并且所选择的候选类型一样，不是自定义的数据，以免多次查找
     */
    private void setSelectValue(DynamicFormFieldValueDTO field,FormFieldQueryDTO dto,Map<String,Object> valueMap) throws Exception{
        if(isNeedSelectValue(field) && field.getFieldSelectValueType()!=null){
            if("0".equals(field.getFieldSelectValueType())){//需要全部重新获取，不要put valueMap中
                List<CoreShowDTO>  selectList = this.getDynamicFormFieldSelectedList(field.getFieldId());
                field.setFieldSelectedValueList(selectList);
               // valueMap.put(key,selectList);
            }else {
                String key = field.getFieldType()+"_"+field.getFieldSelectValueType();
                if(valueMap.containsKey(key)){
                    field.setFieldSelectedValueList(valueMap.get(key));
                }
                if(field.getFieldType() == 6){
                    if("1".equals(field.getFieldSelectValueType()) || "2".equals(field.getFieldSelectValueType())){
                        List<Map<String,Object>>  selectList =  this.getExpList(dto);
                        field.setFieldSelectedValueList(selectList);
                        valueMap.put(key,selectList);
                    }
                    if("3".equals(field.getFieldSelectValueType())){
                        List<CoreShowDTO> selectList = this.getLeaveTypeList();
                        field.setFieldSelectedValueList(selectList);
                        valueMap.put(key,selectList);
                    }
                }
            }
        }

        if(field.getFieldType()==11){//todo 从审批表中获取数据,过后处理

        }
        if(field.getFieldType()==12){//从我的项目中获取
            String key = field.getFieldType()+"_"+field.getFieldSelectValueType();
            if(valueMap.containsKey(key)){
                field.setFieldSelectedValueList(valueMap.get(key));
            }else {
                List<CoreShowDTO> selectList = this.getProjectList(dto,field.getFieldSelectValueType());
                field.setFieldSelectedValueList(selectList);
                valueMap.put(key,selectList);
            }
        }
    }

    private List<Map<String,Object>> getExpList(FormFieldQueryDTO dto) throws Exception{
        List<ExpTypeDTO> selectList =  expCategoryService.getExpCategoryTypeList(dto.getCurrentCompanyId(),dto.getAccountId());
        List<Map<String,Object>> list = new ArrayList<>();
        selectList.stream().forEach(s->{
            Map<String,Object> map = new HashMap<>();
            map.put("id",s.getParent().getName());
            map.put("name",s.getParent().getName());
            List<CoreShowDTO> childList = new ArrayList<>();
            map.put("child",childList);
            s.getChild().stream().forEach(c->{
                childList.add(new CoreShowDTO(s.getParent().getName()+"-"+c.getName(),c.getName()));
            });
            list.add(map);
        });
        return list;
    }

    private List<CoreShowDTO> getProjectList(FormFieldQueryDTO dto,String fieldSelectValueType){
        List<ProjectDTO> selectList =  expMainService.getProjectListWS(dto.getCurrentCompanyId(),dto.getAccountId(),fieldSelectValueType);
        List<CoreShowDTO> list = new ArrayList<>();
        selectList.stream().forEach(s->{
            list.add(new CoreShowDTO(s.getId(),s.getProjectName()));
        });
        return list;
    }

    private List<CoreShowDTO> getLeaveTypeList(){
        List<CoreShowDTO> list = new ArrayList<>();
        List<DataDictionaryEntity> selectList = this.dataDictionaryService.getSubDataByCode(SystemParameters.LEAVE);
        selectList.stream().forEach(s->{
            list.add(new CoreShowDTO(s.getVl(),s.getName()));
        });
        return list;
    }

    private List<CoreShowDTO> getDynamicFormFieldSelectedList(String fieldId){
        List<CoreShowDTO> list = new ArrayList<>();
        List<DynamicFormFieldSelectedValueDTO>  selectList = dynamicFormService.listOptional(fieldId);
        selectList.stream().forEach(s->{
            if(s.getSelectableValue()==null){
                s.setSelectableValue(s.getSelectableName());
            }
            list.add(new CoreShowDTO(s.getSelectableValue(),s.getSelectableName()));
        });
        return list;
    }

    private boolean isNeedSelectValue(DynamicFormFieldBaseDTO field){
        if (field.getFieldType() == 6 || field.getFieldType() == 7 || field.getFieldType() == 8 ){
            return true;
        }
        return false;
    }
}
