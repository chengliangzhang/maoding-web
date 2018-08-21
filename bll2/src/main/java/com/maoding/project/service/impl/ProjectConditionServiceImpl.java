package com.maoding.project.service.impl;

import com.maoding.core.base.dto.CoreQueryDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.constant.ProjectConst;
import com.maoding.core.util.ObjectUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.core.util.StringUtils;
import com.maoding.project.dao.ProjectConditionDao;
import com.maoding.project.dto.OptionalTitleDTO;
import com.maoding.project.dto.OptionalTitleGroupDTO;
import com.maoding.project.dto.OptionalTitleSelectedDTO;
import com.maoding.project.dto.ProjectConditionDTO;
import com.maoding.project.entity.ProjectConditionEntity;
import com.maoding.project.service.ProjectConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
    public OptionalTitleSelectedDTO listOptionalTitle(CoreQueryDTO query) {
        List<OptionalTitleGroupDTO> optionalTitleGroupList = new ArrayList<OptionalTitleGroupDTO>(){
            {
                add(new OptionalTitleGroupDTO("项目基本信息",new ArrayList<OptionalTitleDTO>(){
                    {
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_BASIC_CREATE_DATE,"立项时间"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_BASIC_NAME,"项目名称"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_BASIC_STATUS,"项目状态"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_BASIC_NO,"项目编号"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_BASIC_PART_A,"甲方"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_BASIC_PART_B,"乙方"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_BASIC_SIGN_DATE,"合同签订"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_BASIC_TYPE,"项目类型"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_BASIC_CREATE_COMPANY,"立项组织"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_BASIC_BUILDS,"功能分类"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_BASIC_ADDRESS,"项目地点"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_BASIC_RELATION_COMPANY,"合作组织"));
                    }
                }));
                add(new OptionalTitleGroupDTO("项目成员信息",new ArrayList<OptionalTitleDTO>(){
                    {
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_MEMBER_RESPONSE,"经营负责人"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_MEMBER_RESPONSE_ASSISTANT,"经营助理"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_MEMBER_DESIGN,"设计负责人"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_MEMBER_DESIGN_ASSISTANT,"设计助理"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_MEMBER_TASK_LEADER,"任务负责人"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_MEMBER_TASK_DESIGNER,"设计人员"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_MEMBER_TASK_CHECKER,"校对人员"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_MEMBER_TASK_AUDITOR,"审核人员"));
                    }
                }));
                add(new OptionalTitleGroupDTO("项目收入情况",new ArrayList<OptionalTitleDTO>(){
                    {
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_COST_CONTRACT,"合同计划收款"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_COST_CONTRACT_REAL,"合同到账信息"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_COST_TECHNICAL_GAIN,"技术审查费计划收款"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_COST_TECHNICAL_GAIN_REAL,"技术审查费到账金额"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_COST_COOPERATE_GAIN,"合作设计费计划收款"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_COST_COOPERATE_GAIN_REAL,"合作设计费到账金额"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_COST_OTHER_GAIN,"其他收入计划收款"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_COST_OTHER_GAIN_REAL,"其他收入到账金额"));
                    }
                }));
                add(new OptionalTitleGroupDTO("项目支出情况",new ArrayList<OptionalTitleDTO>(){
                    {
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_COST_TECHNICAL_PAY,"技术审查费计划付款"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_COST_TECHNICAL_PAY_REAL,"技术审查费付款金额"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_COST_COOPERATE_PAY,"合作设计费计划付款"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_COST_COOPERATE_PAY_REAL,"合作设计费付款金额"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_COST_OTHER_PAY,"其他支出计划付款"));
                        add(new OptionalTitleDTO(ProjectConst.TITLE_PROJECT_COST_OTHER_PAY_REAL,"其他支出付款金额"));
                    }
                }));
            }
        };

        //设置所有的被选中状态为"0"
        optionalTitleGroupList.forEach(group-> group.getOptionalTitleList().forEach(title->title.setIsSelected("0")));

        //查询当前的标题选项状态
        Map<String,Object> conditionQuery = new HashMap<>();
        //过滤出当前组织
        conditionQuery.put("companyId",query.getCurrentCompanyId());
        //过滤出当前用户
        conditionQuery.put("userId",query.getAccountId());
        //过滤出有效的
        conditionQuery.put("status","0");

        List<ProjectConditionDTO> list = selProjectConditionList(conditionQuery);

        //更新被选中状态，并生成已选择列表
        List<OptionalTitleDTO> selectedTitleList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(list)){
            //设置查找出的项的被选中状态为"1"
            for (ProjectConditionDTO condition : list){
                String[] codeArray = condition.getCode().split(",");
                for (String code : codeArray) {
                    for (OptionalTitleGroupDTO optionalTitleGroup : optionalTitleGroupList) {
                        boolean found = false;
                        for (OptionalTitleDTO optionalTitle : optionalTitleGroup.getOptionalTitleList()) {
                            if (StringUtils.contains(code, optionalTitle.getCode())) {
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
        }

        //设置返回值
        OptionalTitleSelectedDTO result = new OptionalTitleSelectedDTO();
        result.setOptionalTitleGroupList(optionalTitleGroupList);
        result.setSelectedTitleList(selectedTitleList);
        return result;
    }
}
