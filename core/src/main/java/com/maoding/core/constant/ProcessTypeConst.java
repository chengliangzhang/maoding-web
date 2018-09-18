package com.maoding.core.constant;

/**
 * 流程类型定义
 */
public interface ProcessTypeConst {

    /*****流程类型中的key的定义方式：companyId_流程类型_type(companyId:组织id，流程类型：为以下定义的类型，type：1=自由流程，2=固定流程，3=分条件流程）)******/

    /**
     * 报销类型
     */
    String  PROCESS_TYPE_EXPENSE = "expense";

    /**
     * 费用申请类型
     */
    String  PROCESS_TYPE_COST_APPLY = "costApply";

    /**
     * 请假类型
     */
    String  PROCESS_TYPE_LEAVE = "leave";

    /**
     * 出差类型
     */
    String  PROCESS_TYPE_ON_BUSINESS = "onBusiness";

    /**
     * 项目立项
     */
    String PROCESS_TYPE_PROJECT_SET_UP = "projectSetUp";

    /**
     * 项目费用申请
     */
    String PROCESS_TYPE_PROJECT_PAY_APPLY = "projectPayApply";

    /**
     * 默认流程
     */
    String PROCESS_TYPE_FREE="free_process";

    /**
     * 无流程
     */
    Integer TYPE_NOT_PROCESS = 0;

    /**
     * 自由流程
     */
      Integer TYPE_FREE = 1;

    /**
     * 固定流程
     */
      Integer TYPE_FIXED = 2;

    /**
     * 分条件流程
     */
      Integer PROCESS_TYPE_CONDITION = 3;
	  
    /**
     * 通过
     */
    String PASS = "1";

    /**
     * 不通过
     */
    String NOT_PASS = "0";

    /** 流程定义内默认流程路径名称 */
    String DEFAULT_FLOW_TASK_KEY = "defaultFlow";

    /** 流程定义内默认启动节点编号 */
    String FLOW_ELEMENT_KEY_START = "start";

    /** 流程定义内默认结束节点编号 */
    String FLOW_ELEMENT_KEY_END = "end";

    /** 流程定义编号分隔符 */
    String ID_SPLIT = "_";

    /** 流程定义编号前缀 */
    String ID_PREFIX_PROCESS = "p" + ID_SPLIT;

    /** 流程定义内用户任务编号前缀 */
    String ID_PREFIX_TASK = "t" + ID_SPLIT;


    /**
     * 无流程
     */
    Integer STATUS_PROCESS_START = 1;

    /**
     * 自由流程
     */
    Integer STATUS_PROCESS_STOP = 0;
}
