package com.maoding.deliver.service;

import com.maoding.core.base.service.BaseService;
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
public interface DeliverService extends BaseService<DeliverEntity> {
    /**
     * @author  张成亮
     * @date    2018/7/18
     * @description     查询交付任务
     * @param   query 交付任务查询条件
     * @return  交付任务列表
     **/
    List<DeliverDTO> listDeliver(MyTaskQueryDTO query);

}
