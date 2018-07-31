package com.maoding.core.base.dto;

import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * @author : 张成亮
 * @date   : 2018/7/27
 * @package: CoreEditDTO
 * @description : 前端发送给后台的用于修改的信息
 *      将修改所有通过查找条件查找到的元素
 *      派生类内定义的字段（即除了id，idList，accountId等字段之外的字段）是要修改到的值，如果某字段为空，则保持原值，不做修改
 *
 *      在调用save方法时，如果id及idList为空，则插入新记录
 *      如果id或idList不为空，则判断是否存在编号相同的元素，
 *          不存在则新增元素，编号为相应id
 *          存在则修改编号为此id的元素
 *
 *      在调用delete方法时，根据查询条件内id或idList进行查找
 *      delete方法应当设置元素删除标志，而不是直接删除
 *
 *      accountId为修改者编号，对应数据库内createBy、updateBy等字段
 */
public abstract class CoreEditDTO extends CoreDTO {
    /** 要编辑多个元素时，目标元素编号列表 */
    private List<String> idList;
    /** 修改者编号 */
    private String accountId;

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
