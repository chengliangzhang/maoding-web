package com.maoding.financial.dto;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.financial.entity.ExpCategoryEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ExpTypeDTO
 * 类描述：变动支出类型Bean（用于新增，数据封装）
 * 作    者：MaoSF
 * 日    期：2015年12月7日-上午11:13:40
 */
public class ExpTypeOutDTO extends BaseDTO {

    private List<ExpTypeDTO> expTypeDTOList =new ArrayList<ExpTypeDTO>();

    private List<ExpCategoryEntity> deleteExpTypeList =new ArrayList<ExpCategoryEntity>();

    public List<ExpTypeDTO> getExpTypeDTOList() {
        return expTypeDTOList;
    }

    public void setExpTypeDTOList(List<ExpTypeDTO> expTypeDTOList) {
        this.expTypeDTOList = expTypeDTOList;
    }

    public List<ExpCategoryEntity> getDeleteExpTypeList() {
        return deleteExpTypeList;
    }

    public void setDeleteExpTypeList(List<ExpCategoryEntity> deleteExpTypeList) {
        this.deleteExpTypeList = deleteExpTypeList;
    }
}
