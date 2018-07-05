package com.maoding.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/2/23 10:04
 * 描    述 : 报销费用excel导入
 */
public interface ExpenseConst {
    final String EXP_DATE = "申请时间";
    final String EXP_AMOUNT = "金额";
    final String EXP_TYPE = "费用类型";
    final String EXP_USER_ID = "申请人";
    final String AUDIT_PERSON = " 审批人";
    final String EXP_ALLNAME = "收支类型";
    final String EXP_USE = "用途说明";
    final String ALLOCATION_USER_ID = "支出方";//拨款人id
    final String PROJECT_ID = "关联项目";
    final String EXPENSE_TYPE = "1";
    final String COST_TYPE = "2";

    /**
     * 状态转换
     */
    final Map<String, String> TYPE_MAPPER = new HashMap<String, String>() {
        {
            put("报销申请", EXPENSE_TYPE);
            put("费用申请", COST_TYPE);
        }
    };

}
