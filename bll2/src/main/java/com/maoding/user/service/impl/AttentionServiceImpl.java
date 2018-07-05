package com.maoding.user.service.impl;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.core.util.StringUtil;
import com.maoding.user.dao.AttentionDao;
import com.maoding.user.dto.AttentionDTO;
import com.maoding.user.entity.AttentionEntity;
import com.maoding.user.service.AttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service("attentionService")
public class AttentionServiceImpl extends GenericService<AttentionEntity> implements AttentionService {

    @Autowired
    private AttentionDao attentionDao;

    @Override
    public AjaxMessage addAttention(AttentionDTO dto) throws Exception{

        Map<String,Object> map = new HashMap<>();
        map.put("targetId",dto.getTargetId());
        map.put("type",dto.getType());
        map.put("companyId",dto.getCompanyId());
        map.put("companyUserId",dto.getCompanyUserId());
        if(attentionDao.getAttentionEntity(map)!=null){
            return AjaxMessage.succeed("关注成功");
        }
        dto.setId(StringUtil.buildUUID());
        AttentionEntity attentionEntity = new AttentionEntity();
        BaseDTO.copyFields(dto, attentionEntity);

        int res = attentionDao.insert(attentionEntity);
        if(res==1){
            return new AjaxMessage().setCode("0").setData(dto.getId());
        }else {
            return new AjaxMessage().setCode("1").setInfo("关注失败，请重新刷新请求！");
        }
    }

    @Override
    public AjaxMessage delAttention(String id) throws Exception {
        int res = attentionDao.deleteById(id);
        if(res==1){
            return new AjaxMessage().setCode("0");
        }else {
            return new AjaxMessage().setCode("1").setInfo("关注失败，请重新刷新请求！");
        }
    }
}
