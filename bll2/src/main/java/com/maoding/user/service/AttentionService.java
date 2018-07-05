package com.maoding.user.service;


import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.user.dto.AttentionDTO;
import com.maoding.user.entity.AttentionEntity;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：AttentionDaoImpl
 * 类描述：关注Dao
 * 作    者：wrb
 * 日    期：2017年01月06日-下午16:38:05
 */
public interface AttentionService extends BaseService<AttentionEntity> {

    /**
     * 添加项目关注记录
     * @param dto
     * @return
     */
    public AjaxMessage addAttention(AttentionDTO dto) throws Exception;

    /**
     * 添加项目关注记录
     * @param dto
     * @return
     */
    public AjaxMessage delAttention(String id) throws Exception;

}
