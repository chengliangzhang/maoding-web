package com.maoding.conllaboration.service;

import com.maoding.conllaboration.entity.CollaborationEntity;
import com.maoding.core.base.service.BaseService;

/**
 * Created by Idccapp21 on 2017/2/8.
 */


public interface CollaborationService extends BaseService<CollaborationEntity> {

    /**
     * 推送同步指令CU（触发条件：组织增删改、组织人员增删改）
     */
    void pushSyncCMD_CU(String companyId);

    /**
     * 推送同步指令PU（触发条件：项目增删改）
     */
    void pushSyncCMD_PU(String projectId);

    /**
     * 推送同步指令PT（触发条件：阶段变动（PT0）、签发变动（PT1）、生产变动（PT2））
     */
    void pushSyncCMD_PT(String projectId, String taskPath, String syncCmd);
}
