package com.maoding.conllaboration.service;

import com.maoding.conllaboration.entity.CompanyDiskEntity;
import com.maoding.core.base.service.BaseService;

/**
 * Created by Idccapp21 on 2017/2/8.
 */


public interface CompanyDiskService extends BaseService<CompanyDiskEntity> {

    /**
     * 初始化组织空间
     */
    void initDisk(String companyId);
}
