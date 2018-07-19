package com.maoding.deliver.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.deliver.dto.DeliverDTO;
import com.maoding.deliver.entity.DeliverEntity;
import com.maoding.mytask.dto.MyTaskQueryDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/18
 * @description :
 */
public interface DeliverDao extends BaseDao<DeliverEntity> {
    /**
     * @author  张成亮
     * @date    2018/7/18
     * @description     查询交付任务
     * @param   query 交付任务查询条件
     * @return  交付任务列表
     **/
    List<DeliverDTO> listDeliver(MyTaskQueryDTO query);

    /**
     * @author  张成亮
     * @date    2018/7/19
     * @description     删除交付任务，同时把所有分配给其他人的确认任务和执行任务一并删除
     * @param   request 删除申请
     **/
    void fakeDeleteDeliver(BaseDTO request);
}
