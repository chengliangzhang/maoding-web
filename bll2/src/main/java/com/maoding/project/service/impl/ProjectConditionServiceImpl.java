package com.maoding.project.service.impl;

import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.util.*;
import com.maoding.invoice.dao.InvoiceDao;
import com.maoding.invoice.dto.InvoiceFilterQueryDTO;
import com.maoding.project.dao.ProjectConditionDao;
import com.maoding.project.dao.ProjectDao;
import com.maoding.project.dto.*;
import com.maoding.project.entity.ProjectConditionEntity;
import com.maoding.project.service.ProjectConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/1/25 17:21
 * 描    述 :
 */
@Service("projectConditionService")
public class ProjectConditionServiceImpl extends GenericService<ProjectConditionEntity> implements ProjectConditionService {
    @Autowired
    private ProjectConditionDao projectConditionDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private InvoiceDao invoiceDao;

    private String notReturnString = "busPersonInCharge,busPersonInChargeAssistant,designPersonInCharge,designPersonInChargeAssistant";
    @Override
    public List<ProjectConditionDTO> selProjectConditionList(Map<String, Object> param) {
        List<ProjectConditionDTO> list = projectConditionDao.selProjectConditionList(param);

        //排除notReturnString
//        for(ProjectConditionDTO c:list){
//            List<String> list1 = new ArrayList<>();
//            String[] codes = c.getCode().split(",");
//            for(String code:codes){
//                if(!notReturnString.contains(code)){
//                    list1.add(code);
//                }
//            }
//            if (!CollectionUtils.isEmpty(list1)){
//                c.setCode(org.apache.commons.lang3.StringUtils.join(list1,","));
//            }else {
//                c.setCode("");
//            }
//        }
        return list;
    }

    @Override
    public int insertProjectCondition(Map<String, Object> param) {
        ProjectConditionEntity entity = new ProjectConditionEntity();
        List<ProjectConditionDTO> conditionDTOS = projectConditionDao.selProjectConditionList(param);
        int status = 0;
        if (0 < conditionDTOS.size()) {
            entity.setId(conditionDTOS.get(0).getId());
            entity.setCode(param.get("code").toString());
            entity.setUpdateBy(null != param.get("userId") ? (String) param.get("userId") : "");
            status = projectConditionDao.updateById(entity);
        } else {
            entity.setId(StringUtil.buildUUID());
            entity.setCompanyId(null != param.get("companyId") ? (String) param.get("companyId") : "");
            entity.setUserId(null != param.get("userId") ? (String) param.get("userId") : "");
            entity.setCode(null != param.get("code") ? (String) param.get("code") : "");
            entity.setType(null != param.get("type") ? Integer.parseInt(param.get("type").toString()) : 0);
            entity.setStatus(0);
            entity.setCreateBy(null != param.get("userId") ? (String) param.get("userId") : "");
            status = projectConditionDao.insert(entity);
        }
        return status;
    }

    /**
     * 描述     查询可选标题
     * 日期     2018/8/20
     *
     * @param query 查询条件
     *              accountId 查询用户编号，默认为当前用户编号
     * @return 可选择标题栏列表，两层列表，包含是否选中标志
     * @author 张成亮
     **/
    @Override
    public OptionalTitleSelectedDTO listOptionalTitle(TitleQueryDTO query) {
        TraceUtils.check(query.getType() != null,log,"!type不能为空");

        //从数据库内读取所有的可选标题栏
        List<OptionalTitleGroupDTO> optionalTitleGroupList = projectConditionDao.listOptionalTitleGroup(query);

        List<TitleColumnDTO> list = listTitle(query);

        //更新被选中状态，并生成已选择列表
        List<OptionalTitleDTO> selectedTitleList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(list)){
            //设置查找出的项的被选中状态为"1"
            for (TitleColumnDTO title : list){
                for (OptionalTitleGroupDTO optionalTitleGroup : optionalTitleGroupList) {
                    boolean found = false;
                    for (OptionalTitleDTO optionalTitle : optionalTitleGroup.getOptionalTitleList()) {
                        if (StringUtils.isSame(title.getCode(), optionalTitle.getCode())) {
                            optionalTitle.setIsSelected("1");
                            selectedTitleList.add(optionalTitle);
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        break;
                    }
                }
            }
        }

        //设置返回值
        OptionalTitleSelectedDTO result = new OptionalTitleSelectedDTO();
        result.setOptionalTitleGroupList(optionalTitleGroupList);
        result.setSelectedTitleList(selectedTitleList);
        return result;
    }

    /**
     * 描述     保存标题设置
     * 日期     2018/8/23
     *
     * @param request 保存请求
     * @author 张成亮
     **/
    @Override
    public void changeOptionalTitle(TitleEditDTO request) {
        TraceUtils.check(request.getType() != null,log,"!type不能为空");

        if (ObjectUtils.isNotEmpty(request.getTitleCodeList())){
            StringBuilder sb = new StringBuilder();
            for(String code : request.getTitleCodeList()){
                if (sb.length() > 0){
                    sb.append(",");
                }
                sb.append(code);
            }
            TitleQueryDTO query = BeanUtils.createFrom(request,TitleQueryDTO.class);
            List<TitleColumnDTO> list = listTitle(query);
            String id = getTitleId(list);
            if (StringUtils.isNotEmpty(id)){
                ProjectConditionEntity entity = new ProjectConditionEntity();
                entity.setId(id);
                entity.resetUpdateDate();
                entity.setUpdateBy(request.getAccountId());
                entity.setCompanyId(request.getCurrentCompanyId());
                entity.setUserId(request.getAccountId());
                entity.setStatus(0);
                entity.setType(request.getType());
                entity.setCode(sb.toString());
                projectConditionDao.updateById(entity);
            } else {
                ProjectConditionEntity entity = new ProjectConditionEntity();
                entity.initEntity();
                entity.setCreateBy(request.getAccountId());
                entity.setCompanyId(request.getCurrentCompanyId());
                entity.setUserId(request.getAccountId());
                entity.setStatus(0);
                entity.setType(request.getType());
                entity.setCode(sb.toString());
                projectConditionDao.insert(entity);
            }
        } else {
            TitleQueryDTO query = BeanUtils.createFrom(request,TitleQueryDTO.class);
            List<TitleColumnDTO> list = listTitle(query);
            String id = getTitleId(list);
            if (StringUtils.isNotEmpty(id)){
                projectConditionDao.deleteById(id);
            }
        }
    }

    //获取原有标题栏设置的编号
    private String getTitleId(List<TitleColumnDTO> list){
        String id = null;
        if (ObjectUtils.isNotEmpty(list)){
            for (TitleColumnDTO title : list) {
                if (title.getCanBeHide() == 1){
                    id = StringUtils.left(title.getTitleId(),32);
                }
            }
        }
        return id;
    }

    /**
     * 描述       查询标题设置
     * 日期       2018/8/23
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public List<TitleColumnDTO> listTitle(TitleQueryDTO query) {
        final int TITLE_TYPE_PROJECT = 0;
        final int TITLE_TYPE_PROJECT_OVERVIEW = 1;
        final int TITLE_TYPE_INVOICE = 2;

        if (query.getType() == null){
            query.setType(TITLE_TYPE_PROJECT);
        }
        int type = DigitUtils.parseInt(query.getType());
        List<TitleColumnDTO> titleList = projectConditionDao.listTitle(query);
        if (ObjectUtils.isEmpty(titleList)){
            titleList = projectConditionDao.listDefaultTitle(query);
        }

        if (isNeedGetFilterList(titleList,query)){
            titleList.forEach(title->{
                if (title.getHasList() == 1){
                    List<CoreShowDTO> filterList = null;
                    if ((type == TITLE_TYPE_PROJECT) || (type == TITLE_TYPE_PROJECT_OVERVIEW)) {
                        ProjectFilterQueryDTO filterQuery = BeanUtils.createFrom(query,ProjectFilterQueryDTO.class);
                        filterQuery.setTitleCode(title.getCode());
                        filterList = projectDao.getProjectFilterList(filterQuery);
                    } else if (type == TITLE_TYPE_INVOICE) {
                        InvoiceFilterQueryDTO filterQuery = BeanUtils.createFrom(query,InvoiceFilterQueryDTO.class);
                        filterQuery.setTitleCode(title.getCode());
                        filterList = invoiceDao.getInvoiceFilterList(filterQuery);
                    }
                    if (isValid(filterList)) {
                        title.setFilterList(filterList);
                    }
                }
            });
        }

        return titleList;
    }

    //判断是否需要读取列表过滤器
    private boolean isNeedGetFilterList(List<TitleColumnDTO> titleList, TitleQueryDTO query){
        return ObjectUtils.isNotEmpty(titleList)
                && ((query.getWithList() == null) || (query.getWithList() == 1));
    }

    //判断查询出的列表是否有效
    private boolean isValid(List<CoreShowDTO> filterList){
        return ObjectUtils.isNotEmpty(filterList)
                && (ObjectUtils.getFirst(filterList) != null);
    }
}
