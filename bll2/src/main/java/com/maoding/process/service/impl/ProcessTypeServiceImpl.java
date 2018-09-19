package com.maoding.process.service.impl;

import com.maoding.commonModule.dto.SaveAuditCopyDTO;
import com.maoding.commonModule.service.AuditCopyService;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.constant.ProcessTypeConst;
import com.maoding.core.util.BeanUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.core.util.StringUtils;
import com.maoding.core.util.TraceUtils;
import com.maoding.dynamicForm.dto.FormGroupDTO;
import com.maoding.dynamicForm.dto.SaveDynamicFormDTO;
import com.maoding.process.dao.ProcessTypeDao;
import com.maoding.process.entity.ProcessTypeEntity;
import com.maoding.process.service.ProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("processTypeService")
public class ProcessTypeServiceImpl extends NewBaseService implements ProcessTypeService{

    @Autowired
    private AuditCopyService auditCopyService;

    @Autowired
    private ProcessTypeDao processTypeDao;

    @Override
    public int saveAuditCopy(SaveAuditCopyDTO dto) throws Exception {
        if(dto.getNoticeType()!=null){
            ProcessTypeEntity processType = new ProcessTypeEntity();
            processType.setId(dto.getTargetId());
            processType.setNoticeType(dto.getNoticeType());
            processTypeDao.updateById(processType);
        }
        return this.auditCopyService.saveAuditCopy(dto);
    }

    @Override
    public int saveProcessType(ProcessTypeEntity processTypeEntity) throws Exception {
        processTypeEntity.initEntity();
        processTypeEntity.setDeleted(0);
        SaveDynamicFormDTO saveDynamicFormDTO = new SaveDynamicFormDTO();
        saveDynamicFormDTO.setId(processTypeEntity.getFormId());
        saveDynamicFormDTO.setCurrentCompanyId(processTypeEntity.getCompanyId());

        processTypeEntity.setSeq(this.processTypeDao.selectMaxSeq(saveDynamicFormDTO.getCurrentCompanyId()));
        processTypeEntity.setStatus(ProcessTypeConst.STATUS_PROCESS_START);//默认启用
        if(processTypeEntity.getTargetType()==null) {
            processTypeEntity.setTargetType(processTypeEntity.getFormId());
        }
        if(processTypeEntity.getType()==null){
            processTypeEntity.setType(ProcessTypeConst.TYPE_NOT_PROCESS);
        }
        return processTypeDao.insert(processTypeEntity);
    }

    /**
     * 作者：FYT
     * 日期：2018/9/14
     * 描述：动态审批表 启用/停用
     */
    @Override
    public int updateStatusDynamicForm(SaveDynamicFormDTO dto) throws Exception {
        //1.查询，根据dto中的id，和当前组织去查询ProcessTypeEntity（targetType = dto.id,companyId = dto.currentCompanyId)
        ProcessTypeEntity processTypeEntity = processTypeDao.selectById(dto.getId());
        if(!StringUtil.isNullOrEmpty(processTypeEntity.getStatus())){
            if(0==processTypeEntity.getStatus() && 0==dto.getStatus()){
                processTypeEntity.setStatus(1);
                return processTypeDao.updateById(processTypeEntity);
            }
            if(1==processTypeEntity.getStatus() && 1==dto.getStatus()){
                processTypeEntity.setStatus(0);
                return processTypeDao.updateById(processTypeEntity);
            }
        }
        return 0;
    }


    /**
     * 作者：FYT
     * 日期：2018/9/14
     * 描述：动态表单模板 逻辑删除
     * @return
     * @throws Exception
     */
    @Override
    public int deleteDynamicForm(SaveDynamicFormDTO dto) throws Exception {
        TraceUtils.check(StringUtils.isNotEmpty(dto.getId()),"!id不能为空");
        ProcessTypeEntity processTypeEntity = BeanUtils.createFrom(dto,ProcessTypeEntity.class);
        processTypeEntity.setDeleted(1);
        return processTypeDao.updateById(processTypeEntity);
    }

    /**
     * 作者：FYT
     * 日期：2018/9/18
     * 描述：查询所有属于该分组的动态审批表
     * @return
     * @throws Exception
     */
    @Override
    public List<ProcessTypeEntity> selectByCompanyIdFormType(FormGroupDTO formGroupDTO) throws Exception{
        return processTypeDao.selectByCompanyIdFormType(formGroupDTO);
    }

    /**
     * 作者：FYT
     * 日期：2018/9/18
     * 描述：将没有分组的动态审批表，设置FormType = 4
     * @return
     * @throws Exception
     */
    @Override
    public int updateDynamicFormType(ProcessTypeEntity processTypeEntity) throws Exception{
        return processTypeDao.updateById(processTypeEntity);
    }

}
