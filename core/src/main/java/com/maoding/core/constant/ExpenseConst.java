package com.maoding.core.constant;

import com.maoding.core.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/2/23 10:04
 * 描    述 : 报销费用excel导入
 */
public interface ExpenseConst {
     String EXP_DATE = "申请时间";
     String EXP_AMOUNT = "金额";
     String EXP_TYPE = "费用类型";
     String EXP_USER_ID = "申请人";
     String AUDIT_PERSON = " 审批人";
     String EXP_ALLNAME = "收支类型";
     String EXP_USE = "用途说明";
     String ALLOCATION_USER_ID = "支出方";//拨款人id
     String PROJECT_ID = "关联项目";
     String EXPENSE_TYPE = "1";
     String COST_TYPE = "2";

    /**
     * 状态转换
     */
     Map<String, String> TYPE_MAPPER = new HashMap<String, String>() {
        {
            put("报销申请", EXPENSE_TYPE);
            put("费用申请", COST_TYPE);
        }
    };


    /**
     * 报销类型
     */
    String  TYPE_EXPENSE = "1";

    /**
     * 费用申请类型
     */
    String  TYPE_COST_APPLY = "2";

    /**
     * 请假类型
     */
    String  TYPE_LEAVE = "3";

    /**
     * 出差类型
     */
    String  TYPE_ON_BUSINESS = "4";

//    /**
//     * 项目立项
//     */
//    String TYPE_PROJECT_SET_UP = "projectSetUp";

    /**
     * 项目费用申请
     */
    String TYPE_PROJECT_PAY_APPLY = "5";


    static String getTypeName(String typeValue) {
        String typeName = null;
        if(StringUtils.isEmpty(typeValue)){
            return "";
        }
        switch (typeValue) {
            case "1":
                typeName = "年假";
                break;
            case "2":
                typeName = "事假";
                break;
            case "3":
                typeName = "病假";
                break;
            case "4":
                typeName = "调休假";
                break;
            case "5":
                typeName = "婚假";
                break;
            case "6":
                typeName = "产假";
                break;
            case "7":
                typeName = "陪产假";
                break;
            case "8":
                typeName = "丧假";
                break;
            case "9":
                typeName = "其他";
                break;
            case "10":
                typeName = "出差";
                break;
            default:
                typeName = typeValue;
        }
        return typeName;
    }
}
