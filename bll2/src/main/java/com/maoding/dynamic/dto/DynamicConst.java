package com.maoding.dynamic.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chengliang.zhang on 2017/6/5.
 */
public interface DynamicConst {
    /*****************项目动态动态类型******************/
    //历史遗留项目动态
    Integer DYNAMIC_TYPE_ADD_PROJECT = 1;//创建项目
    Integer DYNAMIC_TYPE_CHANGE_PROJECT = 2;//更新项目基本信息
    Integer DYNAMIC_TYPE_ADD_ISSUE_TASK = 3;//签发任务
    Integer DYNAMIC_TYPE_ADD_TASK = 4;//新建子任务
    Integer DYNAMIC_TYPE_TASKPERSON = 5;//安排生产任务参与人员
    Integer DYNAMIC_TYPE_CONTRACTTOSECTION = 6;//合同到账确认
    Integer DYNAMIC_TYPE_CONTRACTNODE = 7;//新增合同回款节点
    Integer DYNAMIC_TYPE_INITIATEDSECTION = 8;//合同回款发起

    //新增项目动态
    //---------基本信息类----
    Integer DYNAMIC_TYPE_DEL_PROJECT = 9;//删除项目
    //---------任务类----------
    Integer DYNAMIC_TYPE_CHANGE_ISSUE_TASK = 10;//更改签发任务
    Integer DYNAMIC_TYPE_CHANGE_TASK = 11;//更改生产任务
    Integer DYNAMIC_TYPE_DEL_ISSUE_TASK = 12;//删除签发任务
    Integer DYNAMIC_TYPE_DEL_TASK = 13;//删除生产任务
    //--------款项类-----------
    Integer DYNAMIC_TYPE_CHANGE_FEE = 14;//修改总金额
    Integer DYNAMIC_TYPE_ADD_CONTRACT_POINT = 15;//新增合同收款节点
    Integer DYNAMIC_TYPE_ADD_DESIGN_POINT = 16;//新增技术审查费收款节点
    Integer DYNAMIC_TYPE_ADD_COOPERATOR_POINT = 17;//新增合作设计费付款节点
    Integer DYNAMIC_TYPE_ADD_OTHER_DEBIT_POINT = 18;//新增其他收支收款节点
    Integer DYNAMIC_TYPE_ADD_OTHER_PAY_POINT = 19;//新增其他收支付款节点
    Integer DYNAMIC_TYPE_CHANGE_CONTRACT_POINT = 20;//更改合同收款节点
    Integer DYNAMIC_TYPE_CHANGE_DESIGN_POINT = 21;//更改技术审查费收款节点
    Integer DYNAMIC_TYPE_CHANGE_COOPERATOR_POINT = 22;//更改合作设计费付款节点
    Integer DYNAMIC_TYPE_CHANGE_OTHER_DEBIT_POINT = 23;//更改其他收支收款节点
    Integer DYNAMIC_TYPE_CHANGE_OTHER_PAY_POINT = 24;//更改其他收支付款节点
    Integer DYNAMIC_TYPE_DEL_CONTRACT_POINT = 25;//删除合同收款节点
    Integer DYNAMIC_TYPE_DEL_DESIGN_POINT = 26;//删除技术审查费收款节点
    Integer DYNAMIC_TYPE_DEL_COOPERATOR_POINT = 27;//删除合作设计费付款节点
    Integer DYNAMIC_TYPE_DEL_OTHER_DEBIT_POINT = 28;//删除其他收支收款节点
    Integer DYNAMIC_TYPE_DEL_OTHER_PAY_POINT = 29;//删除其他收支付款节点

    //-----文件类------
    Integer DYNAMIC_TYPE_UPLOAD_FILE = 30;//上传文件
    Integer DYNAMIC_TYPE_UPDATE_FILE = 31;//修改文件
    Integer DYNAMIC_TYPE_DELETE_FILE = 32;//删除文件
    Integer DYNAMIC_TYPE_CREATE_DIRECTORY = 36;//创建目录
    Integer DYNAMIC_TYPE_UPDATE_DIRECTORY = 37;//修改目录
    Integer DYNAMIC_TYPE_DELETE_DIRECTORY = 38;//删除目录

    //----阶段类-------
    Integer DYNAMIC_TYPE_ADD_PHASE_TASK = 33;//添加设计内容
    Integer DYNAMIC_TYPE_CHANGE_PHASE_TASK = 34;//修改设计内容
    Integer DYNAMIC_TYPE_DEL_PHASE_TASK = 35;//删除设计内容

    //----- 通用类 -----
    Integer DYNAMIC_TYPE_CREATE_OBJECT = 40;//创建对象
    Integer DYNAMIC_TYPE_UPDATE_OBJECT = 41;//修改对象
    Integer DYNAMIC_TYPE_DELETE_OBJECT = 42;//删除对象
    Integer DYNAMIC_TYPE_CREATE_CONTRACT_COST = 50; //新增款项总金额
    Integer DYNAMIC_TYPE_UPDATE_CONTRACT_COST = 51; //修改款项总金额
    Integer DYNAMIC_TYPE_DELETE_CONTRACT_COST = 52; //删除款项总金额
    Integer DYNAMIC_TYPE_CREATE_POINT_COST = 60; //添加合同回款节点
    Integer DYNAMIC_TYPE_UPDATE_POINT_COST = 61; //修改合同回款节点
    Integer DYNAMIC_TYPE_DELETE_POINT_COST = 62; //删除合同回款节点
    Integer DYNAMIC_TYPE_CREATE_POINT_COST_RECEIPT = 63; //添加收款节点
    Integer DYNAMIC_TYPE_UPDATE_POINT_COST_RECEIPT = 64; //修改收款节点
    Integer DYNAMIC_TYPE_DELETE_POINT_COST_RECEIPT = 65; //删除收款节点
    Integer DYNAMIC_TYPE_CREATE_POINT_COST_PAY = 66; //添加付款节点
    Integer DYNAMIC_TYPE_UPDATE_POINT_COST_PAY = 67; //修改付款节点
    Integer DYNAMIC_TYPE_DELETE_POINT_COST_PAY = 68; //删除付款节点
    Integer DYNAMIC_TYPE_CREATE_DETAIL_COST = 70; //发起合同回款
    Integer DYNAMIC_TYPE_UPDATE_DETAIL_COST = 71; //修改合同回款
    Integer DYNAMIC_TYPE_DELETE_DETAIL_COST = 72; //删除合同回款
    Integer DYNAMIC_TYPE_CREATE_DETAIL_COST_RECEIPT = 73; //发起收款
    Integer DYNAMIC_TYPE_UPDATE_DETAIL_COST_RECEIPT = 74; //修改收款
    Integer DYNAMIC_TYPE_DELETE_DETAIL_COST_RECEIPT = 75; //删除收款
    Integer DYNAMIC_TYPE_CREATE_DETAIL_COST_PAY = 76; //发起付款
    Integer DYNAMIC_TYPE_UPDATE_DETAIL_COST_PAY = 77; //修改付款
    Integer DYNAMIC_TYPE_DELETE_DETAIL_COST_PAY = 78; //删除付款
    Integer DYNAMIC_TYPE_CREATE_PAY_COST = 80; //确认到账
    Integer DYNAMIC_TYPE_UPDATE_PAY_COST = 81; //确认到账
    Integer DYNAMIC_TYPE_DELETE_PAY_COST = 82; //确认到账
    Integer DYNAMIC_TYPE_CREATE_PAY_COST_PAY = 83; //确认支付
    Integer DYNAMIC_TYPE_UPDATE_PAY_COST_PAY = 84; //确认支付
    Integer DYNAMIC_TYPE_DELETE_PAY_COST_PAY = 85; //确认支付
    Integer DYNAMIC_TYPE_CREATE_RESPONSIBLE = 90; //指定负责人
    Integer DYNAMIC_TYPE_UPDATE_RESPONSIBLE = 91; //更改负责人
    Integer DYNAMIC_TYPE_DELETE_RESPONSIBLE = 92; //删除负责人
    Integer DYNAMIC_TYPE_FINISH_TASK = 100; //完成任务
    Integer DYNAMIC_TYPE_REACTIVE_TASK = 101; //激活任务

    /******* 被操作的信息类型 **********/
    Integer TARGET_TYPE_SKY_DRIVE = 1; //操作网盘数据表：maoding_web_project_sky_drive
    Integer TARGET_TYPE_PROJECT = 2; //操作项目表：maoding_web_project
    Integer TARGET_TYPE_PROJECT_MANAGER = 3; //操作项目负责人表：maoding_web_project_manager
    Integer TARGET_TYPE_PROJECT_TASK = 4; //操作任务表：maoding_web_project_task
    Integer TARGET_TYPE_DESIGN_CONTENT = 5; //设计内容表：maoding_web_project_design_content
    Integer TARGET_TYPE_COST = 6; //总金额表：maoding_web_project_cost
    Integer TARGET_TYPE_COST_POINT = 7; //费用节点表：maoding_web_project_cost_point
    Integer TARGET_TYPE_COST_DETAIL = 8; //费用细项表：maoding_web_project_cost_point_detail
    Integer TARGET_TYPE_COST_PAY = 9; //收支金额表：maoding_web_project_cost_payment_detail
    Integer TARGET_TYPE_PROJECT_MEMBER = 10; //负责人表：maoding_web_project_cost_payment_detail
    Integer TARGET_TYPE_PROCESS_NODE = 11; //设校审节点表：maoding_web_project_process_node

    /******* 格式相关 ***************/
    String SEPARATOR = " ;"; //分隔符

    /******* 动态类型转换 **************/
    Map<Integer,String> TYPE_STRING = new HashMap<Integer,String>(){
        {
            put(DYNAMIC_TYPE_UPLOAD_FILE,"上传文件");
            put(DYNAMIC_TYPE_UPDATE_FILE,"修改文件");
            put(DYNAMIC_TYPE_DELETE_FILE,"删除文件");
            put(DYNAMIC_TYPE_CREATE_DIRECTORY,"创建目录");
            put(DYNAMIC_TYPE_UPDATE_DIRECTORY,"修改目录");
            put(DYNAMIC_TYPE_DELETE_DIRECTORY,"删除目录");
        }
    };
}
