package com.maoding.deliver.service.impl;

import com.maoding.core.base.service.GenericService;
import com.maoding.deliver.dto.DeliverDTO;
import com.maoding.deliver.entity.DeliverEntity;
import com.maoding.deliver.service.DeliverService;
import com.maoding.mytask.dto.MyTaskQueryDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/18
 * @description :
 */
public class DeliverServiceImpl extends GenericService<DeliverEntity> implements DeliverService {
    /**
     * @param query 交付任务查询条件
     * @return 交付任务列表
     * @author 张成亮
     * @date 2018/7/18
     * @description 查询交付任务
     **/
    @Override
    public List<DeliverDTO> listDeliver(MyTaskQueryDTO query) {
        return null;
    }
}
