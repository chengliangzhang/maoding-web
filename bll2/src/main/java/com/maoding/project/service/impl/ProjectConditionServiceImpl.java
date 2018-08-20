package com.maoding.project.service.impl;

import com.maoding.core.base.dto.CoreQueryDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.util.StringUtil;
import com.maoding.project.dao.ProjectConditionDao;
import com.maoding.project.dto.OptionalTitleDTO;
import com.maoding.project.dto.OptionalTitleGroupDTO;
import com.maoding.project.dto.ProjectConditionDTO;
import com.maoding.project.entity.ProjectConditionEntity;
import com.maoding.project.service.ProjectConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
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

    private String notReturnString = "";
    @Override
    public List<ProjectConditionDTO> selProjectConditionList(Map<String, Object> param) {
        List<ProjectConditionDTO> list = projectConditionDao.selProjectConditionList(param);
        for(ProjectConditionDTO c:list){
            List<String> list1 = new ArrayList<>();
            String[] codes = c.getCode().split(",");
            list1.addAll(Arrays.asList(codes));
            if (!CollectionUtils.isEmpty(list1)){
                c.setCode(org.apache.commons.lang3.StringUtils.join(list1,","));
            }else {
                c.setCode("");
            }
        }
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
    public List<OptionalTitleGroupDTO> listOptionalTitle(CoreQueryDTO query) {
        final List<OptionalTitleGroupDTO> optionalTitleList = new ArrayList<OptionalTitleGroupDTO>(){
            {
                add(new OptionalTitleGroupDTO("项目基本信息",new ArrayList<OptionalTitleDTO>(){
                    {
                        add(new OptionalTitleDTO("projectCreateDate","立项时间"));
                        add(new OptionalTitleDTO("projectName","项目名称"));
                        add(new OptionalTitleDTO("status","项目状态"));
                        add(new OptionalTitleDTO("projectNo","项目编号"));
                        add(new OptionalTitleDTO("partyA","甲方"));
                        add(new OptionalTitleDTO("partyB","乙方"));
                        add(new OptionalTitleDTO("signDate","合同签订"));
                        add(new OptionalTitleDTO("projectType","项目类型"));
                        add(new OptionalTitleDTO("createCompany","立项组织"));
                        add(new OptionalTitleDTO("buildName","功能分类"));
                        add(new OptionalTitleDTO("address","项目地点"));
                        add(new OptionalTitleDTO("relationCompany","合作组织"));
                    }
                }));
                add(new OptionalTitleGroupDTO("项目成员信息",new ArrayList<OptionalTitleDTO>(){
                    {
                        add(new OptionalTitleDTO("busPersonInCharge","经营负责人"));
                        add(new OptionalTitleDTO("busPersonInChargeAssistant","经营助理"));
                        add(new OptionalTitleDTO("designPersonInCharge","设计负责人"));
                        add(new OptionalTitleDTO("designPersonInChargeAssistant","设计助理"));
                        add(new OptionalTitleDTO("taskLeader","任务负责人"));
                        add(new OptionalTitleDTO("designer","设计人员"));
                        add(new OptionalTitleDTO("checker","校对人员"));
                        add(new OptionalTitleDTO("auditor","审核人员"));
                    }
                }));
                add(new OptionalTitleGroupDTO("项目收入情况",new ArrayList<OptionalTitleDTO>(){
                    {
                        add(new OptionalTitleDTO("contractPlan","合同计划收款"));
                        add(new OptionalTitleDTO("contractReal","合同到账信息"));
                        add(new OptionalTitleDTO("technicalGainPlan","技术审查费计划收款"));
                        add(new OptionalTitleDTO("technicalGainReal","技术审查费到账金额"));
                        add(new OptionalTitleDTO("cooperateGainPlan","合作设计费计划收款"));
                        add(new OptionalTitleDTO("cooperateGainReal","合作设计费到账金额"));
                        add(new OptionalTitleDTO("otherGainPlan","其他收入计划收款"));
                        add(new OptionalTitleDTO("otherGainPlan","其他收入到账金额"));
                    }
                }));
                add(new OptionalTitleGroupDTO("项目支出情况",new ArrayList<OptionalTitleDTO>(){
                    {
                        add(new OptionalTitleDTO("technicalPayPlan","技术审查费计划付款"));
                        add(new OptionalTitleDTO("technicalPayReal","技术审查费付款金额"));
                        add(new OptionalTitleDTO("cooperatePayPlan","合作设计费计划付款"));
                        add(new OptionalTitleDTO("cooperatePayReal","合作设计费付款金额"));
                        add(new OptionalTitleDTO("otherPayPlan","其他支出计划付款"));
                        add(new OptionalTitleDTO("otherPayPlan","其他支出付款金额"));
                    }
                }));
            }
        };

        return optionalTitleList;
    }
}
