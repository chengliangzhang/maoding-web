package com.maoding.dynamic.dao.impl;

import com.maoding.core.util.DateUtils;
import com.maoding.core.util.StringUtil;
import com.maoding.dynamic.dao.ZInfoDAO;
import com.maoding.dynamic.dto.*;
import com.maoding.project.entity.ProjectDesignContentEntity;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.project.entity.ProjectProcessNodeEntity;
import com.maoding.projectcost.entity.ProjectCostPaymentDetailEntity;
import com.maoding.projectcost.entity.ProjectCostPointDetailEntity;
import com.maoding.projectcost.entity.ProjectCostPointEntity;
import com.maoding.task.dto.TaskWithFullNameDTO;
import com.maoding.task.entity.ProjectProcessTimeEntity;
import com.maoding.task.entity.ProjectTaskEntity;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chengliang.zhang on 2017/6/8.
 */
@Service("zInfoDAO")
public class ZInfoDAOImpl implements ZInfoDAO {
    @Autowired
    protected SqlSessionTemplate sqlSession;

    private ZInfoDAO dao = null;

    @Override
    public String getUserNameByUserId(String userId) {
        String name = dao.getUserNameByUserId(userId);
        return (name != null) ? name : "";
    }

    @Override
    public String getUserNameByCompanyUserId(String companyUserId) {
        String name = dao.getUserNameByCompanyUserId(companyUserId);
        return (name != null) ? name : "";
    }

    @Override
    public String getUserIdByCompanyUserId(String companyUserId) {
        return dao.getUserIdByCompanyUserId(companyUserId);
    }

    @Override
    public String getCompanyUserIdByCompanyIdAndUserId(@Param("companyId") String companyId, @Param("userId") String userId) {
        Map<String,String> query = new HashMap<>();
        query.put("companyId",companyId);
        query.put("userId",userId);
        return getCompanyUserIdByCompanyIdAndUserId(query);
    }

    @Override
    public String getCompanyUserIdByCompanyIdAndUserId(Map<String, String> query) {
        return dao.getCompanyUserIdByCompanyIdAndUserId(query);
    }

    @Override
    public String getProjectNameByProjectId(String projectId) {
        String name = dao.getProjectNameByProjectId(projectId);
        return (name != null) ? name : "";
    }

    @Override
    public String getCompanyNameByTaskId(String taskId) {
        String name = dao.getCompanyNameByTaskId(taskId);
        return (name != null) ? name : "";
    }

    @Override
    public String getCompanyIdByTaskId(String taskId) {
        return dao.getCompanyIdByTaskId(taskId);
    }

    @Override
    public String getProduceTaskFullNameByTask(ProjectTaskEntity task) {
        return dao.getProduceTaskFullNameByTask(task);
    }

    @Override
    public String getProduceTaskNameByTask(ProjectTaskEntity task) {
        return dao.getProduceTaskNameByTask(task);
    }

    @Override
    public String getProduceTaskNameWithLeaderByTask(ProjectTaskEntity task) {
        return dao.getProduceTaskNameWithLeaderByTask(task);
    }

    @Override
    public String getProduceTaskNameWithMembersByTask(ProjectTaskEntity task) {
        return dao.getProduceTaskNameWithMembersByTask(task);
    }

    @Override
    public String getPhaseTaskFullNameByTask(ProjectTaskEntity task) {
        return dao.getPhaseTaskFullNameByTask(task);
    }

    @Override
    public String getPhaseTaskNameByTask(ProjectTaskEntity task) {
        return dao.getPhaseTaskNameByTask(task);
    }

    @Override
    public String getPhaseTaskFullNameByDesignContent(ProjectDesignContentEntity designContent) {
        return dao.getPhaseTaskFullNameByDesignContent(designContent);
    }

    @Override
    public String getPhaseTaskNameByDesignContent(ProjectDesignContentEntity designContent) {
        return dao.getPhaseTaskNameByDesignContent(designContent);
    }

    @Override
    public String getIssueTaskFullNameByTask(ProjectTaskEntity task) {
        return dao.getIssueTaskFullNameByTask(task);
    }

    @Override
    public String getIssueTaskNameByTask(ProjectTaskEntity task) {
        return dao.getIssueTaskNameByTask(task);
    }

    @Override
    public String getIssueTaskNameWithToCompanyByTask(ProjectTaskEntity task) {
        return dao.getIssueTaskNameWithToCompanyByTask(task);
    }

    @Override
    public String getIssueTaskNameWithPlanByTask(ProjectTaskEntity task) {
        return dao.getIssueTaskNameWithPlanByTask(task);
    }

    @Override
    public String getPeriodByTaskId(String taskId) {
        return dao.getPeriodByTaskId(taskId);
    }

    @Override
    public ProjectProcessTimeEntity getTimeByTaskId(String taskId) {
        if (taskId == null) return null;
        return dao.getTimeByTaskId(taskId);
    }

    @Override
    public String getPeriodByTime(ProjectProcessTimeEntity time) {
        if (time == null) return "";
        StringBuilder s = new StringBuilder();
        s.append((time.getStartTime() != null) ? DateUtils.date2Str(time.getStartTime(),DateUtils.date_sdf2):"");
        s.append(" — ");
        s.append((time.getEndTime() != null) ? DateUtils.date2Str(time.getEndTime(),DateUtils.date_sdf2):"");
        return s.toString();
    }

    @Override
    public String getPeriodByDesignContentId(String designContentId) {
        return dao.getPeriodByDesignContentId(designContentId);
    }

    @Override
    public String getProjectAddressByProject(ProjectEntity project) {
        if (project == null) return null;
        String s = null;
        if (!StringUtil.isNullOrEmpty(project.getProvince())){
            s = ((s == null) ? "" : (s + " ")) + project.getProvince();
        }
        if (!StringUtil.isNullOrEmpty(project.getCity())){
            s = ((s == null) ? "" : (s + " ")) + project.getCity();
        }
        if (!StringUtil.isNullOrEmpty(project.getCounty())){
            s = ((s == null) ? "" : (s + " ")) + project.getCounty();
        }
        if (!StringUtil.isNullOrEmpty(project.getDetailAddress())){
            s = ((s == null) ? "" : (s + " ")) + project.getDetailAddress();
        }
        return s;
    }

    @Override
    public String getProjectBuiltFloorByProject(ProjectEntity project) {
        if (project == null) return null;
        StringBuilder s = new StringBuilder();
        if (!StringUtil.isNullOrEmpty(project.getBuiltFloorUp())){
            s.append("地上" + project.getBuiltFloorUp() + "层");
        }
        if (!StringUtil.isNullOrEmpty(project.getBuiltFloorDown())){
            s.append("地下" + project.getBuiltFloorDown() + "层");
        }
        return s.toString();
    }

    @Override
    public String getProjectStatusNameByStatus(String status) {
        if (status == null) return null;
        String name = ("0".equals(status)) ? "进行中" : "已完成";
        return name;
    }

    @Override
    public String getContractCompanyNameByCompanyId(String companyId) {
        return dao.getContractCompanyNameByCompanyId(companyId);
    }

    @Override
    public String getCompanyNameByCompanyId(String companyId) {
        if (companyId == null) return null;
        return dao.getCompanyNameByCompanyId(companyId);
    }

    @Override
    public String getMembersByTaskId(String id) {
        if (id == null) return null;
        return dao.getMembersByTaskId(id);
    }

    @Override
    public TaskWithFullNameDTO getTaskByTaskId(String id) {
        if (id == null) return null;
        return dao.getTaskByTaskId(id);
    }

    @Override
    public TaskWithFullNameDTO getTaskByTask(ProjectTaskEntity task) {
        if (task == null) return null;
        return dao.getTaskByTask(task);
    }

    @Override
    public TaskWithFullNameDTO getTaskByTime(ProjectProcessTimeEntity time) {
        if (time == null) return null;
        return dao.getTaskByTime(time);
    }

    @Override
    public ZCostDTO getCostByPoint(ProjectCostPointEntity point) {
        if (point == null) return null;
        ZCostDTO cost = new ZCostDTO();
        if (cost == null) return null;
        cost.setId(point.getId());
        cost.setIdType(DynamicConst.TARGET_TYPE_COST_POINT);
        cost.setPointName(point.getFeeDescription());
        cost.setPointRate(new BigDecimal((point.getFeeProportion() != null)?point.getFeeProportion():"0"));
        cost.setPointCost(point.getFee());
        cost.setPointType(Integer.parseInt((point.getType() != null)?point.getType():"0"));
        if (cost.getPointType() == 1) {
            cost.setPointTypeName("合同回款");
        } else if (cost.getPointType() == 2){
            cost.setPointTypeName("技术审查费");
        } else if (cost.getPointType() == 3){
            cost.setPointTypeName("合作设计费");
        } else {
            cost.setPointTypeName("其他收支");
        }
        cost.setFullName(getCostFullName(cost));
        return cost;
    }

    @Override
    public ZCostDTO getCostByDetail(ProjectCostPointDetailEntity detail) {
        if (detail == null) return null;
        ZCostDTO result = dao.getCostByDetail(detail);
        if (result == null) return null;
        result.setIdType(DynamicConst.TARGET_TYPE_COST_DETAIL);
        result.setFullName(getCostFullName(result));
        return result;
    }

    @Override
    public ZCostDTO getCostByPay(ProjectCostPaymentDetailEntity pay) {
        if (pay == null) return null;
        ZCostDTO result = dao.getCostByPay(pay);
        if (result == null) return null;
        result.setIdType(DynamicConst.TARGET_TYPE_COST_PAY);
        result.setFullName(getCostFullName(result));
        return result;
    }

    private String getCostFullName(ZCostDTO cost){
        if (cost == null) return null;
        StringBuilder s = new StringBuilder();
        s.append((cost.getDetailCost() != null) ? "\"" : "");
        s.append((cost.getPointName() != null) ? cost.getPointName() : "");
        if (cost.getPointRate() != null) {
            s.append(" 比例：").append(String.valueOf(cost.getPointRate().setScale(5, 2).doubleValue())).append("%");
        }
        s.append(" 金额：").append((cost.getPointCost() != null) ?
                String.valueOf(cost.getPointCost().setScale(20, 6).doubleValue()) : "0").append("万");
        if (cost.getDetailCost() != null) {
            s.append("\" ");
            if (cost.getPointType() == 1){
                s.append("回款");
            } else if ((cost.getPointType() == 2) || (cost.getPointType() == 3) || (cost.getPointType() == 5)){
                s.append("收款");
            } else if (cost.getPointType() == 4) {
                s.append("付款");
            }
            s.append("金额").append(String.valueOf(cost.getDetailCost().setScale(20, 6).doubleValue())).append("万");
            if (cost.getPaidDate() != null){
                if ((cost.getPaidUserName() != null) || (cost.getPayUserName() != null)){
                    s.append(" ").append((cost.getPaidUserName() != null) ? cost.getPaidUserName() : cost.getPayUserName());
                }
                if (cost.getPaidCost() != null) {
                    s.append("确认到账").append(String.valueOf(cost.getPaidCost().setScale(20, 6).doubleValue())).append("万");
                }
                if (cost.getPaidDate() != null) {
                    s.append(" 到账日期：" + DateUtils.date2Str(cost.getPaidDate(), DateUtils.date_sdf2));
                }
            }
        }
        return s.toString();
    }

    @Override
    public ZProjectDTO getProjectByProject(ProjectEntity project) {
        if (project == null) return null;
        return dao.getProjectByProject(project);
    }

    @Override
    public ZProcessNodeDTO getProcessNodeByProcessNode(ProjectProcessNodeEntity node) {
        if (node == null) return null;
        return dao.getProcessNodeByProcessNode(node);
    }

    @Override
    public Integer getLastQueryCount() {
        return sqlSession.selectOne("CommonMapper.getLastQueryCount");
    }

    @PostConstruct
    public void init(){
        dao = sqlSession.getMapper(ZInfoDAO.class);
    }
}
