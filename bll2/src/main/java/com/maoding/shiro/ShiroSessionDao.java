package com.maoding.shiro;

/**
 * Created by Wuwq on 2016/11/14.
 */

import com.beust.jcommander.internal.Lists;
import com.maoding.core.util.RedissonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.joda.time.DateTime;
import org.redisson.api.RMapCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ShiroSessionDao extends CachingSessionDAO {

    private static final Logger logger = LoggerFactory.getLogger(ShiroSessionDao.class);
    // 保存到Redis中key的前缀 prefix+sessionId
    private String prefix;
    // 设置会话的超时秒杀
    private int timeoutSeconds;
    private Boolean enableRedisCache;
    @Qualifier("redissonUtils_session")
    @Autowired
    private RedissonUtils redissonUtils;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public Boolean getEnableRedisCache() {
        return enableRedisCache;
    }

    public void setEnableRedisCache(Boolean enableRedisCache) {
        this.enableRedisCache = enableRedisCache;
    }

    /**
     * 重写CachingSessionDAO中readSession方法，如果Session中没有登陆信息就调用doReadSession方法从Redis中重读
     */
    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        Session cachedSession = null;
        cachedSession = super.getCachedSession(sessionId);
        if (!enableRedisCache)
            return cachedSession;
        else {
            //if(cachedSession ==null || cachedSession.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null){
            if (cachedSession == null) {
                cachedSession = this.doReadSession(sessionId);
                if (cachedSession == null) {
                    throw new UnknownSessionException();
                } else {
                    ((ShiroSession) cachedSession).setNeedUpdate(true);
                    super.update(cachedSession);
                    //((ShiroSession)cachedSession).setNeedUpdate(false);
                }
            }
            /*else if(cachedSession !=null)
                logger.debug("Ehcache 读取 session：{}, IP：{}",sessionId,cachedSession.getHost());*/
            return cachedSession;
        }
    }

    /**
     * 从Redis中读取Session
     *
     * @param sessionId 会话ID
     *
     * @return ShiroSession
     */
    @Override
    public Session doReadSession(Serializable sessionId) {
        Session session = null;
        try {
            RMapCache<String, String> map = redissonUtils.getMapCache(prefix);
            String value = map.getOrDefault(sessionId.toString(), null);
            if (StringUtils.isNotBlank(value)) {
                session = SerializeUtils.deserializeFromString(value);
                logger.debug("Redis 读取 session：{}, IP：{}", sessionId, session.getHost());
            }
        } catch (Exception e) {
            logger.error("Redis 读取 session 失败", e);
        }

        return session;
    }

    /**
     * 如DefaultSessionManager在创建完session后会调用该方法；
     * 如保存到关系数据库/文件系统/NoSQL数据库；即可以实现会话的持久化；
     * 返回会话ID；主要此处返回的ID.equals(session.getId())；
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        assignSessionId(session, sessionId);
        if (!enableRedisCache)
            return sessionId;

        try {
            RMapCache<String, String> map = redissonUtils.getMapCache(prefix);

            map.put(sessionId.toString(), SerializeUtils.serializeToString((ShiroSession) session), timeoutSeconds, TimeUnit.SECONDS);

            logger.debug("Redis 创建 session：{}, IP：{}", sessionId, session.getHost());
        } catch (Exception e) {
            logger.error("Redis 创建 session 失败", e);
        }
        return sessionId;
    }

    /**
     * 更新会话；如更新会话最后访问时间/停止会话/设置超时时间/设置移除属性等会调用
     */
    @Override
    protected void doUpdate(Session session) {
        if (session instanceof ValidatingSession || ((ValidatingSession) session).isValid()) {
            if (enableRedisCache) {
                try {
                    if (session instanceof ShiroSession) {
                        ShiroSession ss = (ShiroSession) session;
                        if (ss.isNeedUpdate()) {
                            try {
                                ss.setNeedUpdate(false);
                                ss.setLastAccessTime(DateTime.now().toDate());
                                RMapCache<String, String> map = redissonUtils.getMapCache(prefix);
                                map.put(ss.getId().toString(), SerializeUtils.serializeToString(ss), timeoutSeconds, TimeUnit.SECONDS);
                                logger.debug("Redis 更新 Session：{}, IP：{}", session.getId(), session.getHost());
                            } catch (Exception e) {
                                throw e;
                            }
                        }
                    } else if (session instanceof Serializable) {
                        RMapCache<String, String> map = redissonUtils.getMapCache(prefix);
                        map.put(session.getId().toString(), SerializeUtils.serializeToString((Serializable) session), timeoutSeconds, TimeUnit.SECONDS);
                        logger.debug("ID {} classname {} 作为非ShiroSession对象被更新, ", session.getId(), session.getClass().getName());
                    } else {
                        logger.debug("ID {} classname {} 不能被序列化 更新失败", session.getId(), session.getClass().getName());
                    }
                } catch (Exception e) {
                    logger.error("Redis 更新 session 失败", e);
                }
            }
        }
    }

    /**
     * 删除会话；当会话过期/会话停止（如用户退出时）会调用
     */
    @Override
    protected void doDelete(Session session) {
        try {
            RMapCache<String, String> map = redissonUtils.getMapCache(prefix);
            map.remove(session.getId().toString());
            logger.debug("Redis 删除 Session：{}, IP：{}", session.getId(), session.getHost());
        } catch (Exception e) {
            logger.error("Redis 删除 session 失败", e);
        }
    }

    @Override
    protected void cache(Session session, Serializable sessionId) {
        super.cache(session, sessionId);
    }

    /**
     * 删除cache中缓存的Session
     */
    public void uncache(Serializable sessionId) {
        try {
            Session session = super.getCachedSession(sessionId);
            super.uncache(session);
        } catch (Exception e) {
            logger.error("uncache Session 失败", e);
        }
    }

    /**
     * 获取当前所有活跃用户，如果用户量多此方法影响性能
     */
    @Override
    public Collection<Session> getActiveSessions() {
        try {
            RMapCache<String, String> map = redissonUtils.getMapCache(prefix);
            if (map == null || map.size() == 0) {
                return null;
            }
            List<String> valueList = Lists.newArrayList(map.values());
            return SerializeUtils.deserializeFromStringController(valueList);
        } catch (Exception e) {
            logger.error("Redis 统计 session 失败", e);
        }
        return null;
    }

    /**
     * 返回本机Ehcache中Session
     */
    public Collection<Session> getEhCacheActiveSessions() {
        return super.getActiveSessions();
    }
}