package com.maoding.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chengliang.zhang on 2017/7/19.
 */
public interface ProjectConst {
    final String PROJECT_NO = "项目编号";
    final String PROJECT_NAME = "项目名称";
    final String PROJECT_COMPANY_NAME = "立项组织";
    final String PROJECT_CREATOR_NAME = "立项人";
    final String PROJECT_CREATE_DATE = "立项日期";
    final String PROJECT_CONTRACT_DATE = "合同签订日期";
    final String PROJECT_PROVINCE = "省/直辖市";
    final String PROJECT_CITY = "市/直辖市区";
    final String PROJECT_COUNTY = "区/县";
    final String PROJECT_DETAIL_ADDRESS = "详细地址";
    final String PROJECT_STATUS = "项目状态";
    final String PROJECT_A_NAME = "甲方";
    final String PROJECT_B_NAME = "乙方";
    final String PROJECT_DESIGN_CONTENT_1 = "设计任务（一）";
    final String PROJECT_START_DATE_1 = "开始时间（一）";
    final String PROJECT_END_DATE_1 = "结束时间（一）";
    final String PROJECT_DESIGN_ORGANIZATION_1 = "设计组织（一）";
    final String PROJECT_FINISH_DATE_1 = "完成时间（一）";
    final String PROJECT_DESIGN_CONTENT_2 = "设计任务（二）";
    final String PROJECT_START_DATE_2 = "开始时间（二）";
    final String PROJECT_END_DATE_2 = "结束时间（二）";
    final String PROJECT_DESIGN_ORGANIZATION_2 = "设计组织（二）";
    final String PROJECT_FINISH_DATE_2 = "完成时间（二）";
    final String PROJECT_DESIGN_CONTENT_3 = "设计任务（三）";
    final String PROJECT_START_DATE_3 = "开始时间（三）";
    final String PROJECT_END_DATE_3 = "结束时间（三）";
    final String PROJECT_DESIGN_ORGANIZATION_3 = "设计组织（三）";
    final String PROJECT_FINISH_DATE_3 = "完成时间（三）";
    final String PROJECT_DESIGN_CONTENT_4 = "设计任务（四）";
    final String PROJECT_START_DATE_4 = "开始时间（四）";
    final String PROJECT_END_DATE_4 = "结束时间（四）";
    final String PROJECT_DESIGN_ORGANIZATION_4 = "设计组织（四）";
    final String PROJECT_FINISH_DATE_4 = "完成时间（四）";
    final String PROJECT_DESIGN_CONTENT_5 = "设计任务（五）";
    final String PROJECT_START_DATE_5 = "开始时间（五）";
    final String PROJECT_END_DATE_5 = "结束时间（五）";
    final String PROJECT_DESIGN_ORGANIZATION_5 = "设计组织（五）";
    final String PROJECT_FINISH_DATE_5 = "完成时间（五）";

    final String PROJECT_STATUS_RUNNING = "0";
    final String PROJECT_STATUS_PAUSE = "1";
    final String PROJECT_STATUS_FINISHED = "2";
    final String PROJECT_STATUS_ABORT = "3";

    /** 状态转换 */
    final Map<String,String> STATUS_MAPPER = new HashMap<String,String>(){
        {
            put("进行中", PROJECT_STATUS_RUNNING);
            put("已暂停",PROJECT_STATUS_PAUSE);
            put("已完成",PROJECT_STATUS_FINISHED);
            put("已终止", PROJECT_STATUS_ABORT);
        }
    };

    /** 项目成员类型 */
    final Integer MEMBER_TYPE_CREATOR = 0;
    final Integer MEMBER_TYPE_MANAGER = 1;
    final Integer MEMBER_TYPE_DESIGN = 2;
    final Integer MEMBER_TYPE_TASK_LEADER = 3;
    final Integer MEMBER_TYPE_TASK_DESIGN = 4;
    final Integer MEMBER_TYPE_TASK_CHECK = 5;
    final Integer MEMBER_TYPE_TASK_AUDIT = 6;

    /** 项目负责人类型 */
    final Map<Integer,String> PERMISSION_MAPPER = new HashMap<Integer,String>(){
        {
            put(MEMBER_TYPE_MANAGER,"51");
            put(MEMBER_TYPE_DESIGN,"52");
        }
    };
}
