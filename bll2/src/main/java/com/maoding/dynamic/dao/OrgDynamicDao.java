package com.maoding.dynamic.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.dynamic.entity.OrgDynamicEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Idccapp21 on 2017/2/23.
 */
public interface OrgDynamicDao extends BaseDao<OrgDynamicEntity> {


    /**
     * 根据参数查询动态
     * @param param
     * @return
     */
    public List<OrgDynamicEntity> getOrgDynamicByParam(Map<String,Object> param);

    /**
     * 查找组织动态，包含发给自己的组织动态
     * @param param 同getOrgDynamicByParam的param参数
     * @return 同getOrgDynamicByParam的返回值
     */
    List<OrgDynamicEntity> listOrgDynamicByParam(Map<String,Object> param);

    /**
     * 获取记录总数
     */
    int getLastQueryCount();

    /**
     * 根据参数查询动态
     * @param param
     * @return
     */
    public List<OrgDynamicEntity> getLastOrgDynamicByParam(Map<String,Object> param);

    /**
     * 根据参数查询动态数量
     * @param param
     * @return
     */
    public int getOrgDynamicCountByParam(Map<String,Object> param);

    /**
     * 方法描述：更改fileld2的值，用于项目删除
     * 作者：MaoSF
     * 日期：2017/3/29
     * @param:
     * @return:
     */
    int updatefield2ByTargetId(String targetId);
}
