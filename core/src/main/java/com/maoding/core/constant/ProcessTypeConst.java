package com.maoding.core.constant;

/**
 * 流程类型定义
 */
public interface ProcessTypeConst {

    /*****流程类型中的key的定义方式：companyId_流程类型_type(companyId:组织id，流程类型：为以下定义的类型，type：1=自由流程，2=固定流程，3=分条件流程）)******/

    /**
     * 报销类型
     */
    public static final String  EXPENSE_PROCESS_TYPE = "expense";

    /**
     * 费用申请类型
     */
    public static final String  COST_APPLY_PROCESS_TYPE = "costApply";

    /**
     * 请假类型
     */
    public static final String  LEAVE_PROCESS_TYPE = "leave";

    /**
     * 出差类型
     */
    public static final String   ON_BUSINESS_PROCESS_TYPE = "onBusiness";


    /**
     * 自由流程
     */
    public static final Integer  FREE_PROCESS_TYPE = 1;

    /**
     * 固定流程
     */
    public static final Integer  FIXED_PROCESS_TYPE = 2;

    /**
     * 分条件流程
     */
    public static final Integer CONDITION_PROCESS_TYPE = 3;
}
