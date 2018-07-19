package com.maoding.core.constant;

import java.util.HashMap;
import java.util.Map;

public class ProcessConst {


    //合同回款
    public static final Integer PROCESS_TYPE_CONTRACT = 2;

    //技术审查费收款
    public static final Integer PROCESS_TYPE_RECEIVE_TECHNICAL  = 3;

    //技术审查费付款
    public static final Integer PROCESS_TYPE_PAY_TECHNICAL  = 4;

    //合作设计费收款
    public static final Integer PROCESS_TYPE_RECEIVE_COOPERATIVE  = 5;

    //技术审查费付款
    public static final Integer PROCESS_TYPE_PAY_COOPERATIVE  = 6;


    public static final Integer START_STATUS = 1;
    public static final Integer STOP_STATUS = 0;

    //所有组织
    public static final Integer COMPANY_TYPE_ALL = 0;

    //分机组织
    public static final Integer COMPANY_TYPE_SUB = 1;

    //合伙人
    public static final Integer COMPANY_TYPE_PARTNER = 2;

    //单个组织
    public static final Integer COMPANY_TYPE_SINGLE = 3;

    //流程对应的类型
    public static final Map<String,String> PROCESS_NAME_MAP = new HashMap<String,String>(){
        {
            put("2","合同回款流程");
            put("3","技术审查费收款流程");
            put("4","技术审查费付款流程");
            put("5","合作设计费收款流程");
            put("6","技术审查费付款流程");
        }

    };

    //发票类型
    public static final String CONDITION_INVOICE = "1";

    //应收状态
    public static final String CONDITION_RECEIVE_ABLE = "2";

    //应付状态
    public static final String CONDITION_PAY_ABLE = "3";

    //已收状态
    public static final String CONDITION_RECEIVE = "4";

    //已付状态
    public static final String CONDITION_PAY = "5";

    //同步
    public static final String CONDITION_SYNC = "6";

    public static final Integer IS_SELECTED = 1;

    //财务确认发票信息
    public static final Integer NODE_TYPE_FINANCE_INVOICE_CONFIRM = 4;
}
