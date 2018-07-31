package com.maoding.core.base.dto;

/**
 * 深圳市设计同道技术有限公司
 * @author : 张成亮
 * @date   : 2018/7/27
 * @package: CoreEditDTO
 * @description : 前端发送给后台的用于修改的信息
 *      将修改所有通过查找条件查找到的元素
 *      派生类内定义的字段（即除了id，isSelected，accountId等字段之外的字段）是要修改到的值，如果某字段为空，则保持原值，不做修改
 *
 *      在调用save方法时
 *          如果isSelected不为0
 *              如果id为空，则插入新记录
 *              否则，id不为空，则判断是否存在编号相同的元素，
 *                  不存在则新增元素，编号为相应id
 *                  存在则修改编号为此id的元素
 *          否则，isSelected为0
 *              如果id不为空，则删除此编号元素
 *              如果id为空，则无需做任何动作
 *
 *      accountId为修改者编号，对应数据库内createBy、updateBy等字段
 */
public abstract class CoreEditDTO extends BaseDTO {
    /** id:要编辑的元素编号，如果为空则表明需要增加，且编号自动生成 */

    /** 元素是否被选中 */
    private String isSelected;

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }
}
