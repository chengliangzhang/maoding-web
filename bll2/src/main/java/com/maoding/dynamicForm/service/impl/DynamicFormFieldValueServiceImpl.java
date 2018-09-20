package com.maoding.dynamicForm.service.impl;

import com.maoding.attach.dto.FileDataDTO;
import com.maoding.commonModule.dto.QueryCopyRecordDTO;
import com.maoding.commonModule.service.CopyRecordService;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.constant.NetFileType;
import com.maoding.core.util.StringUtils;
import com.maoding.core.util.TraceUtils;
import com.maoding.dynamicForm.dao.DynamicFormDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldValueDao;
import com.maoding.dynamicForm.dto.DynamicFormFieldValueDTO;
import com.maoding.dynamicForm.dto.FormFieldQueryDTO;
import com.maoding.dynamicForm.dto.SaveDynamicAuditDTO;
import com.maoding.dynamicForm.entity.DynamicFormEntity;
import com.maoding.dynamicForm.entity.DynamicFormFieldSelectableValueEntity;
import com.maoding.dynamicForm.entity.DynamicFormFieldValueEntity;
import com.maoding.dynamicForm.service.DynamicFormFieldValueService;
import com.maoding.financial.dto.AuditDTO;
import com.maoding.financial.dto.AuditEditDTO;
import com.maoding.financial.entity.ExpMainEntity;
import com.maoding.financial.service.ExpMainService;
import com.maoding.org.dto.CompanyUserDataDTO;
import com.maoding.process.service.ProcessService;
import com.maoding.project.service.ProjectSkyDriverService;
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
    private DynamicFormDao dynamicFormDao;

    @Autowired
    private DynamicFormFieldDao dynamicFormFieldDao;

    @Autowired
    private DynamicFormFieldValueDao dynamicFormFieldValueDao;

    @Autowired
    private ExpMainService expMainService;

    @Autowired
    private ProjectSkyDriverService projectSkyDriverService;

    @Autowired
    private CopyRecordService copyRecordService;

    @Autowired
    private ProcessService processService;

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
        return 1;
    }


    private DynamicFormFieldValueEntity saveDynamicFormFieldValue(DynamicFormFieldValueDTO valueDTO){
        DynamicFormFieldValueEntity dynamicFormFieldValueEntity = new DynamicFormFieldValueEntity();
        dynamicFormFieldValueEntity.initEntity();
        dynamicFormFieldValueEntity.setFieldValue((String)valueDTO.getFieldValue());
        dynamicFormFieldValueDao.insert(dynamicFormFieldValueEntity);
        return dynamicFormFieldValueEntity;
    }

    @Override
    public Map<String,Object> initDynamicAudit(FormFieldQueryDTO dto) throws Exception {

        String id = dto.getId();
        String conditionValue = null;
        List<AuditDTO> auditList = null;
        List<CompanyUserDataDTO> ccCompanyUserList = null;
        List<FileDataDTO> expAttachList = null;
        Map<String,Object> result = new HashMap<>();
        //1.查询模板+数据
        List<DynamicFormFieldValueDTO> fieldList = dynamicFormFieldValueDao.listFormFieldValueByFormId(dto);
        SaveDynamicAuditDTO dynamicAudit = this.handleDynamicFormField(fieldList,dto);
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

        }
        //3.查询流程 返回流程标识，给前端控制是否要给审批人，以及按钮显示的控制
        Map<String,Object> processData = processService.getCurrentTaskUser(new AuditEditDTO(id,null,null),auditList,conditionValue);

        //4. 查询表单名称
        DynamicFormEntity formEntity = dynamicFormDao.selectById(dto.getFormId());
        TraceUtils.check(formEntity != null,"~没有查询到表单");
        if (formEntity != null){
            result.put("formName",formEntity.getFormName());
        }

        result.put("dynamicAudit",dynamicAudit);
        result.put("expAttachList",expAttachList);
        result.put("ccCompanyUserList",ccCompanyUserList);
        result.putAll(processData);
        return result;
    }

    private SaveDynamicAuditDTO handleDynamicFormField(List<DynamicFormFieldValueDTO> fieldList,FormFieldQueryDTO dto){
        SaveDynamicAuditDTO dynamicAudit = new SaveDynamicAuditDTO();
        fieldList.stream().forEach(field->{
            if(field.getFieldType()==9){
                List<List<DynamicFormFieldValueDTO>> detailFieldValueList = new ArrayList<>();
                dto.setFieldPid(field.getFieldId());
                List<DynamicFormFieldValueDTO> detailList = dynamicFormFieldValueDao.listFormFieldValueByFormId(dto);
                Map<String,List<DynamicFormFieldValueDTO>> detailMap = new HashMap<>();
                for(DynamicFormFieldValueDTO val:detailList){
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
        });
        dynamicAudit.setFieldList(fieldList);
        return dynamicAudit;
    }
}
