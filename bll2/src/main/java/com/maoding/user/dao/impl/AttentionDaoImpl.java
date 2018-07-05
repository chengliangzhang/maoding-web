package com.maoding.user.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.user.dao.AttentionDao;
import com.maoding.user.entity.AttentionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;


@Service("attentionDao")
public class AttentionDaoImpl extends GenericDao<AttentionEntity> implements AttentionDao {
    protected final Logger log= LoggerFactory.getLogger(getClass());
    public AttentionEntity getAttentionEntity(Map<String,Object> paraMap){
        List<AttentionEntity> list = this.sqlSession.selectList("AttentionEntityMapper.selectByParam",paraMap);
        if(!CollectionUtils.isEmpty(list)){
            if(list.size()>1){
                //存在错误数据
                try{
                   // log.error(getClass()+"：存在错误数据，参数："+param.);
                }catch (Exception e){

                }
            }
            return list.get(0);
        }
        return null;
    }

}
