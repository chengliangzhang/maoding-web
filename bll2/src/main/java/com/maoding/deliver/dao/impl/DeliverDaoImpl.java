package com.maoding.deliver.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.core.base.dto.BaseDTO;
import com.maoding.deliver.dao.DeliverDao;
import com.maoding.deliver.dto.DeliverDTO;
import com.maoding.deliver.entity.DeliverEntity;
import com.maoding.mytask.dto.MyTaskQueryDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/18
 * @description :
 */
@Service("deliverDao")
public class DeliverDaoImpl extends GenericDao<DeliverEntity> implements DeliverDao {
    /**
     * @param query 交付任务查询条件
     * @return 交付任务列表
     * @author 张成亮
     * @date 2018/7/18
     * @description 查询交付任务
     **/
    @Override
    public List<DeliverDTO> listDeliver(MyTaskQueryDTO query) {
        return sqlSession.selectList("DeliverEntityMapper.listDeliver",query);
    }

    /**
     * @param request 删除申请
     * @author 张成亮
     * @date 2018/7/19
     * @description 删除交付任务，同时把所有分配给其他人的确认任务和执行任务一并删除
     **/
    @Override
    public void fakeDeleteDeliver(BaseDTO request) {
        sqlSession.update("DeliverEntityMapper.fakeDeleteDeliver",request);
    }
}
