package com.maoding.conllaboration.service.impl;

import com.maoding.conllaboration.dao.CompanyDiskDao;
import com.maoding.conllaboration.entity.CompanyDiskEntity;
import com.maoding.conllaboration.service.CompanyDiskService;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Idccapp21 on 2017/2/8.
 */
@Service("companyDiskService")
public class CompanyDiskServiceImpl extends GenericService<CompanyDiskEntity> implements CompanyDiskService {

    /**
     * 初始化组织空间
     */
    @Override
    public void initDisk(String companyId) {
        //TODO 虽然已经移植，先保留这个空实现的接口
    }
}
