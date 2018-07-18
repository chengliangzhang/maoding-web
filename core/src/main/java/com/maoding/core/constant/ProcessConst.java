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

    public static final Integer COMPANY_TYPE_ALL = 0;//所有组织

    public static final Integer COMPANY_TYPE_SUB = 1;//分机组织

    public static final Integer COMPANY_TYPE_PARTNER = 2;//合伙人

    public static final Integer COMPANY_TYPE_SINGLE = 3;//单个组织


    public static final Map<String,String> PROCESS_NAME_MAP = new HashMap<String,String>(){
        {
            put("2","合同回款流程");
            put("3","技术审查费收款流程");
            put("4","技术审查费付款流程");
            put("5","合作设计费收款流程");
            put("6","技术审查费付款流程");
        }

    };

}
