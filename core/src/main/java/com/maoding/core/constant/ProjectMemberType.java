package com.maoding.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wuwq on 2017/2/15.
 */
public class ProjectMemberType {

    /**
     * 项目创建人
     */
    public static final Integer PROJECT_CREATOR = 0;

    /**
     * 经营负责人
     */
    public static final Integer PROJECT_OPERATOR_MANAGER = 1;


    /**
     * 设计负责人
     */
    public static final Integer PROJECT_DESIGNER_MANAGER = 2;

    /**
     * 任务负责人
     */
    public static final Integer PROJECT_TASK_RESPONSIBLE = 3;

    /**
     * 设计人
     */
    public static final Integer PROJECT_DESIGNER = 4;

    /**
     * 校对人
     */
    public static final Integer PROJECT_PROOFREADER = 5;

    /**
     * 审核人
     */
    public static final Integer PROJECT_AUDITOR = 6;

    /**
     * 经营负责人(助理）
     */
    public static final Integer PROJECT_OPERATOR_MANAGER_ASSISTANT = 7;

    /**
     * 设计负责人(助理）
     */
    public static final Integer PROJECT_DESIGNER_MANAGER_ASSISTANT = 8;


    public static final Map<String,String> projectMemberRole = new HashMap<String,String>(){
        {
            put("0","项目立项人");
            put("1","经营负责人");
            put("2","设计负责人");
            put("3","任务负责人");
            put("4","设计人");
            put("5","校对人");
            put("6","审核人");
            put("7","经营助理");
            put("8","设计助理");
        }

    };

}
