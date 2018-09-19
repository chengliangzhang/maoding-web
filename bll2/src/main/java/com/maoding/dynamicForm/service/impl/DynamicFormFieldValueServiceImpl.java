package com.maoding.dynamicForm.service.impl;

import com.maoding.attach.dto.FileDataDTO;
import com.maoding.commonModule.dto.QueryCopyRecordDTO;
import com.maoding.commonModule.service.CopyRecordService;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.constant.NetFileType;
import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.core.util.StringUtils;
import com.maoding.dynamicForm.dao.DynamicFormDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldDao;
import com.maoding.dynamicForm.dao.DynamicFormFieldValueDao;
import com.maoding.dynamicForm.dto.DynamicFormFieldValueDTO;
import com.maoding.dynamicForm.dto.FormFieldQueryDTO;
import com.maoding.dynamicForm.dto.SaveDynamicAuditDTO;
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

    @Autowired
    private ProjectSkyDriverService projectSkyDriverService;

    @Autowired
    private CopyRecordService copyRecordService;

    @Autowired
    private ProcessService processService;

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
        mainEntity.setType(dto.getType());
        mainEntity.setCompanyId(dto.getCurrentCompanyId());
        mainEntity.setCompanyUserId(dto.getCurrentCompanyUserId());

        String mainId = mainEntity.getId();
        //todo 2.添加页面上的fieldId 对应的 value
        for(DynamicFormFieldValueDTO valueDTO: dto.getFieldList()){
            DynamicFormFieldValueEntity dynamicFormFieldValueEntity = new DynamicFormFieldValueEntity();
            dynamicFormFieldValueEntity.setMainId(mainEntity.getId());
            dynamicFormFieldValueEntity.initEntity();
            dynamicFormFieldValueEntity.setFieldId(valueDTO.getId());
            dynamicFormFieldValueEntity.setFieldValue((String)valueDTO.getFieldValue());
            dynamicFormFieldValueDao.insert(dynamicFormFieldValueEntity);
        }

        //todo 3.添加明细的List对应value
        long time = 1000;
        Map<String,List<DynamicFormFieldValueDTO>> detailList = dto.getDetailList();
        if(StringUtil.isNullOrEmpty(detailList)){
            DynamicFormFieldValueEntity detailFieldValue = new DynamicFormFieldValueEntity();
            Set<String> keys = detailList.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                //todo 3.1 先设置好主记录数据
                detailFieldValue.initEntity();
                //此处重新设置创建时间，为了明细数据，按时间有序排列
                detailFieldValue.setCreateDate(DateUtils.getDate(DateUtils.getDate().getTime()+time));
                time = time+100;
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
                    childDetailFieldValue.setFieldValue((String)valueDTO.getFieldValue());
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
                dynamicAudit.getDetailList().put(field.getFieldId(),new ArrayList<>());
                Map<String,List<DynamicFormFieldValueDTO>> detailList = new HashMap<>();
                //查询明细
                dto.setFieldPid(field.getFieldId());
                List<DynamicFormFieldValueDTO> detailFieldList = dynamicFormFieldValueDao.listFormFieldValueByFormId(dto);
                DynamicFormFieldValueDTO detail = new DynamicFormFieldValueDTO();
                detail.setFieldId(field.getFieldId());
                detailFieldList.stream().forEach(fDetail->{
                    String key = fDetail.getFieldValuePid();
                    if(key==null){
                        key = "1";
                    }
                    if(detailList.containsKey(key)){
                        detailList.get(key).add(fDetail);
                    }else {
                        List<DynamicFormFieldValueDTO> detailMap = new ArrayList<>();
                        detailMap.add(fDetail);
                        detailList.put(key,detailMap);
                    }
                    detail.getDetailFieldList().add(fDetail);
                });

                Set<String> keys = detailList.keySet();
                Iterator<String> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    DynamicFormFieldValueDTO detail3 = new DynamicFormFieldValueDTO();
                    detail3.setId(key);
                    detail.setDetailFieldList(detailList.get(key));
                    dynamicAudit.getDetailList().get(field.getFieldId()).add(detail3);
                }
            }else {
                dynamicAudit.getFieldList().add(field);
            }
        });

        return dynamicAudit;
    }
}
