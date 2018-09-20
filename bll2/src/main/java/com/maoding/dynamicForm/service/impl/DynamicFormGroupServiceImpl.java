package com.maoding.dynamicForm.service.impl;

import com.maoding.commonModule.dto.AuditCopyDataDTO;
import com.maoding.commonModule.service.AuditCopyService;
import com.maoding.core.base.service.NewBaseService;
import com.maoding.core.util.BeanUtils;
import com.maoding.core.util.DigitUtils;
import com.maoding.core.util.ObjectUtils;
import com.maoding.core.util.StringUtils;
import com.maoding.dynamicForm.dao.DynamicFormDao;
import com.maoding.dynamicForm.dao.DynamicFormGroupDao;
import com.maoding.dynamicForm.dto.FormDTO;
import com.maoding.dynamicForm.dto.FormGroupDTO;
import com.maoding.dynamicForm.dto.FormGroupEditDTO;
import com.maoding.dynamicForm.dto.FormGroupQueryDTO;
import com.maoding.dynamicForm.entity.DynamicFormEntity;
import com.maoding.dynamicForm.entity.DynamicFormGroupEntity;
import com.maoding.dynamicForm.service.DynamicFormGroupService;
import com.maoding.process.entity.ProcessTypeEntity;
import com.maoding.process.service.ProcessTypeService;
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
    private ProcessTypeService processTypeService;

    @Autowired
    private AuditCopyService auditCopyService;

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
                updatedEntity.setSeq(request.getSeq());
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
            updatedEntity.setSeq(dynamicFormGroupDao.selectMaxSeq(request.getCurrentCompanyId())+1);
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
        updateDynamicFormType(request);
        return dynamicFormGroupDao.updateById(updatedEntity);
    }

    @Override
    public DynamicFormGroupEntity getOtherDynamicFormGroup(String companyId) {
        //通过companyId 和 is_edit=0  和 group_name=其他模板  查询出对应的type_id
        FormGroupDTO dto = new FormGroupDTO();
        dto.setCompanyId(companyId);
        dto.setIsEdit(0);
        dto.setGroupName("其他模板");
        return dynamicFormGroupDao.selectTypeId(dto);
    }

    //如果组删除，会被分配到其他模板（等同于未分组），并且状态是不可编辑
    private void updateDynamicFormType(FormGroupEditDTO entity) throws Exception{

        //通过ID查询该分组所属的companyId和typeId。
        DynamicFormGroupEntity dynamicFormGroupEntity = dynamicFormGroupDao.selectById(entity.getId());
        FormGroupDTO formGroupDTO = new FormGroupDTO();
        formGroupDTO.setCompanyId(dynamicFormGroupEntity.getCompanyId());
        formGroupDTO.setId(dynamicFormGroupEntity.getId());


        //通过companyId 和 is_edit=0  和 group_name=其他模板  查询出对应的type_id
        FormGroupDTO dto = new FormGroupDTO();
        dto.setCompanyId(entity.getCurrentCompanyId());
        dto.setIsEdit(0);
        dto.setGroupName("其他模板");
        DynamicFormGroupEntity formGroup= dynamicFormGroupDao.selectTypeId(dto);

        processTypeService.updateDynamicFormType(entity.getId(),formGroup.getId());
    }

    @Override
    public void initDynamicFormGroup(String companyId) throws Exception {
        if(!this.dynamicFormGroupDao.isInitFormGroup(companyId)){
            List<DynamicFormGroupEntity> list = dynamicFormGroupDao.listDefaultFormGroup();
            for(DynamicFormGroupEntity group:list){
                String formType = group.getId();
                //1.插入group
                group.initEntity();
                group.setDeleted(0);
                group.setCompanyId(companyId);
                group.setSeq(dynamicFormGroupDao.selectMaxSeq(companyId)+1);
                dynamicFormGroupDao.insert(group);
                //2.查询form
                List<DynamicFormEntity> formList = dynamicFormDao.listDynamicFormByType(formType);
                for(DynamicFormEntity form:formList){
                    //插入综合表的记录
                    ProcessTypeEntity processType = new ProcessTypeEntity();
                    processType.setTargetType(form.getId());
                    processType.setFormId(form.getId());
                    processType.setFormType(group.getId());
                    processType.setCompanyId(companyId);
                    processTypeService.saveProcessType(processType);
                }
            }
        }
    }

    /**
     * 作者：FYT
     * 日期：2018/9/19
     * 描述：查询当前公司的分组
     * */
    @Override
    public List<DynamicFormGroupEntity> listFormGroupByCompanyId(FormGroupEditDTO dto) throws Exception{
        return dynamicFormGroupDao.listFormGroupByCompanyId(dto.getCurrentCompanyId());
    }

    /**
     * 描述       查询公司分组
     * 日期       2018/9/19
     *
     * @param query 分组查询条件
     * @author 张成亮
     */
    @Override
    public List<FormGroupDTO> listFormGroup(FormGroupQueryDTO query) {
        List<FormGroupDTO> groupList = dynamicFormGroupDao.listFormGroup(query);
        if (ObjectUtils.isNotEmpty(groupList) && (DigitUtils.isTrue(query.getIsIncludeForm()))){
            groupList.forEach(group->{
                List<FormDTO> formList = group.getFormList();
                if (ObjectUtils.isNotEmpty(formList)){
                    formList.forEach(form->{
                        if (DigitUtils.isTrue(query.getNeedCC())) {
                            List<AuditCopyDataDTO> list = auditCopyService.listAuditCopy(form.getId());
                            form.setCopyList(list);
                        }

                    });
                }
            });
        }
        return groupList;
    }
}
