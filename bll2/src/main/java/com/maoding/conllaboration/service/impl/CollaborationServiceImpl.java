package com.maoding.conllaboration.service.impl;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Sets;
import com.maoding.conllaboration.RKey;
import com.maoding.conllaboration.SyncCmd;
import com.maoding.conllaboration.entity.CollaborationEntity;
import com.maoding.conllaboration.service.CollaborationService;
import com.maoding.core.base.service.GenericService;
import com.maoding.core.util.RedissonUtils;
import com.maoding.core.util.StringUtils;
import com.maoding.org.dao.CompanyDao;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Idccapp21 on 2017/2/8.
 */
@Service("collaborationService")
public class CollaborationServiceImpl extends GenericService<CollaborationEntity> implements CollaborationService {

    private static final Logger log = LoggerFactory.getLogger(CollaborationServiceImpl.class);

    @Qualifier("redissonUtils_corp")
    @Autowired
    private RedissonUtils redissonUtils;

    @Autowired
    private CompanyDao companyDao;


    private List<String> listMatchEndpoint(String[] companyIds) {
        Set<String> endpoints = Sets.newHashSet();
        //读取协同团队
        RReadWriteLock cLock = redissonUtils.getReadWriteLock(RKey.LOCK_CORP_EP_C);
        try {

            RLock r = cLock.readLock();
            r.lock(5, TimeUnit.SECONDS);
            RSet<String> set_companies = redissonUtils.getSet(RKey.CORP_EP_C);
            Set<String> companies = set_companies.readAll();
            r.unlock();

            //匹配团队
            companies.forEach(c -> {
                String[] splits = StringUtils.split(c, ":");
                if (splits.length == 2 && Arrays.stream(companyIds).anyMatch(id -> id.equalsIgnoreCase(splits[1]))) {
                    endpoints.add(splits[0]);
                }
            });
        } catch (Exception ex) {
            log.error("getMatchesByCompanyId 发生异常", ex);
        }

        return Lists.newArrayList(endpoints);
    }

    private List<String> listCompanyIdByProjectId(String projectId) {
        List<String> companyIds = companyDao.listCompanyIdByProjectId(projectId);
        /*ProjectEntity project = projectDao.selectById(projectId);
        String companyId = project.getCompanyId();
        if (!companyIds.contains(companyId))
            companyIds.add(companyId);*/
        return companyIds;
    }

    /**
     * 推送同步指令CU（触发条件：组织增删改、组织人员增删改）
     */
    @Override
    public void pushSyncCMD_CU(String companyId) {
        if (StringUtils.isBlank(companyId)) {
            log.error("pushSyncCMD_CU 的参数 companyId 不能为空");
            return;
        }

        String change = SyncCmd.CU + ":" + companyId;
        CompletableFuture.runAsync(() -> {
            List<String> endpoints = listMatchEndpoint(new String[]{companyId});
            if (endpoints == null || endpoints.size() == 0)
                return;

            //写入变更
            endpoints.forEach(ep -> {
                String lockKey=String.format(RKey.LOCK_CORP_EP_SYNC_C_PATTERN, ep).toUpperCase();
                String key=String.format(RKey.CORP_EP_SYNC_C_PATTERN, ep).toUpperCase();
                try {
                    RReadWriteLock cLock = redissonUtils.getReadWriteLock(lockKey);
                    RLock r = cLock.readLock();
                    r.lock(5, TimeUnit.SECONDS);

                    RSet<String> changes = redissonUtils.getSet(key);
                    changes.add(change);
                    r.unlock();
                    log.info("端点 {} 添加协同变更: {}", ep, change);
                } catch (Exception ex) {
                    log.error("pushSyncCMD_CU 发生异常", ex);
                }
            });
        });
    }

    /**
     * 推送同步指令PU（触发条件：项目增删改）
     */
    @Override
    public void pushSyncCMD_PU(String projectId) {
        if (StringUtils.isBlank(projectId)) {
            log.error("pushSyncCMD_PU 的参数 projectId 不能为空");
            return;
        }

        List<String> companyIds = listCompanyIdByProjectId(projectId);

        CompletableFuture.runAsync(() -> {
            List<String> endpoints = listMatchEndpoint(companyIds.toArray(new String[companyIds.size()]));
            if (endpoints == null || endpoints.size() == 0)
                return;

            //写入变更
            endpoints.forEach(ep -> {
                try {
                    String lockKey=String.format(RKey.LOCK_CORP_EP_SYNC_P_ID_PATTERN, ep, projectId).toUpperCase();
                    String key=String.format(RKey.CORP_EP_SYNC_P_ID_PATTERN, ep, projectId).toUpperCase();
                    RReadWriteLock cLock = redissonUtils.getReadWriteLock(lockKey);
                    RLock r = cLock.readLock();
                    r.lock(5, TimeUnit.SECONDS);
                    RSet<String> changes = redissonUtils.getSet(key);
                    changes.add(SyncCmd.PU);
                    r.unlock();
                    log.info("端点 {} 项目 {} 添加协同变更: {}", ep,projectId, SyncCmd.PU);
                } catch (Exception ex) {
                    log.error("pushSyncCMD_PU 发生异常#1", ex);
                }

                try {
                    String lockKey=String.format(RKey.LOCK_CORP_EP_SYNC_P_PATTERN, ep).toUpperCase();
                    String key=String.format(RKey.CORP_EP_SYNC_P_PATTERN, ep).toUpperCase();
                    RReadWriteLock cLock = redissonUtils.getReadWriteLock(lockKey);
                    RLock r = cLock.readLock();
                    r.lock(5, TimeUnit.SECONDS);
                    RSet<String> changes = redissonUtils.getSet(key);
                    changes.add(projectId);
                    r.unlock();
                } catch (Exception ex) {
                    log.error("pushSyncCMD_PU 发生异常#2", ex);
                }
            });
        });
    }

    /**
     * 推送同步指令PT（触发条件：阶段变动（PT0）、签发变动（PT1）、生产变动（PT2））
     */
    @Override
    public void pushSyncCMD_PT(String projectId, String taskPath, String syncCmd) {
        if (StringUtils.isBlank(projectId) || StringUtils.isBlank(taskPath)) {
            log.error("pushSyncCMD_PT 的参数 projectId 和 taskPath 不能为空");
            return;
        }

        List<String> companyIds = listCompanyIdByProjectId(projectId);

        String rootNodeId = getRootNodeIdByTaskPath(taskPath);

        CompletableFuture.runAsync(() -> {
            List<String> endpoints = listMatchEndpoint(companyIds.toArray(new String[companyIds.size()]));
            if (endpoints == null || endpoints.size() == 0)
                return;

            //写入变更
            endpoints.forEach(ep -> {
                try {
                    String lockKey=String.format(RKey.LOCK_CORP_EP_SYNC_P_ID_PATTERN, ep, projectId).toUpperCase();
                    String key=String.format(RKey.CORP_EP_SYNC_P_ID_PATTERN, ep, projectId).toUpperCase();
                    RReadWriteLock cLock = redissonUtils.getReadWriteLock(lockKey);
                    RLock r = cLock.readLock();
                    r.lock(5, TimeUnit.SECONDS);
                    RSet<String> changes = redissonUtils.getSet(key);
                    changes.add(syncCmd + ":" +  rootNodeId);

                    r.unlock();
                    log.info("端点 {} 项目 {} 添加协同变更: {}", ep,projectId, syncCmd + ":" +  rootNodeId);
                } catch (Exception ex) {
                    log.error("pushSyncCMD_PT 发生异常#1", ex);
                }

                try {
                    String lockKey=String.format(RKey.LOCK_CORP_EP_SYNC_P_PATTERN, ep).toUpperCase();
                    String key=String.format(RKey.CORP_EP_SYNC_P_PATTERN, ep).toUpperCase();
                    RReadWriteLock cLock = redissonUtils.getReadWriteLock(lockKey);
                    RLock r = cLock.readLock();
                    r.lock(5, TimeUnit.SECONDS);
                    RSet<String> changes = redissonUtils.getSet(key);
                    changes.add(projectId);
                    r.unlock();
                } catch (Exception ex) {
                    log.error("pushSyncCMD_PT 发生异常#2", ex);
                }
            });
        });
    }

    /**
     * 根据TaskPath截取根节点ID
     */
    private String getRootNodeIdByTaskPath(String taskPath) {
        int index = taskPath.indexOf("-");
        if (index == -1)
            return taskPath;
        return taskPath.substring(0, index);
    }
}
