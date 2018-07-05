package com.maoding.core.util;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.client.codec.Codec;
import org.redisson.codec.CodecProvider;
import org.redisson.config.Config;
import org.redisson.liveobject.provider.ResolverProvider;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Component
public class RedissonUtils implements InitializingBean, DisposableBean, RedissonClient {

    private String configFile;
    private Config config;
    private RedissonClient client;

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        if (StringUtils.isNotBlank(configFile)) {
            File file = resolver.getResource(configFile).getFile();
            config = Config.fromJSON(file);
        }
    }

    public void createClient() {
        if (client == null)
            client = Redisson.create(config);
    }

    @Override
    public void destroy() throws Exception {
        if (client != null) {
            synchronized (this) {
                if (client != null) {
                    client.shutdown();
                }
            }
        }
    }


    //------------------------------ 实现 Redisson 接口 ----------------------------------------------------------------------------------
    @Override
    public RBinaryStream getBinaryStream(String s) {
        return client.getBinaryStream(s);
    }

    @Override
    public <V> RGeo<V> getGeo(String s) {
        return client.getGeo(s);
    }

    @Override
    public <V> RGeo<V> getGeo(String s, Codec codec) {
        return client.getGeo(s, codec);
    }

    @Override
    public <V> RSetCache<V> getSetCache(String s) {
        return client.getSetCache(s);
    }

    @Override
    public <V> RSetCache<V> getSetCache(String s, Codec codec) {
        return client.getSetCache(s, codec);
    }

    @Override
    public <K, V> RMapCache<K, V> getMapCache(String s, Codec codec) {
        return client.getMapCache(s, codec);
    }

    @Override
    public <K, V> RMapCache<K, V> getMapCache(String s) {
        return client.getMapCache(s);
    }

    @Override
    public <V> RBucket<V> getBucket(String s) {
        return client.getBucket(s);
    }

    @Override
    public <V> RBucket<V> getBucket(String s, Codec codec) {
        return client.getBucket(s, codec);
    }

    @Override
    public RBuckets getBuckets() {
        return client.getBuckets();
    }

    @Override
    public RBuckets getBuckets(Codec codec) {
        return client.getBuckets(codec);
    }

    @Override
    public <V> RHyperLogLog<V> getHyperLogLog(String s) {
        return client.getHyperLogLog(s);
    }

    @Override
    public <V> RHyperLogLog<V> getHyperLogLog(String s, Codec codec) {
        return client.getHyperLogLog(s, codec);
    }

    @Override
    public <V> RList<V> getList(String s) {
        return client.getList(s);
    }

    @Override
    public <V> RList<V> getList(String s, Codec codec) {
        return client.getList(s, codec);
    }

    @Override
    public <K, V> RListMultimap<K, V> getListMultimap(String s) {
        return client.getListMultimap(s);
    }

    @Override
    public <K, V> RListMultimap<K, V> getListMultimap(String s, Codec codec) {
        return client.getListMultimap(s, codec);
    }

    @Override
    public <K, V> RListMultimapCache<K, V> getListMultimapCache(String s) {
        return client.getListMultimapCache(s);
    }

    @Override
    public <K, V> RListMultimapCache<K, V> getListMultimapCache(String s, Codec codec) {
        return client.getListMultimapCache(s, codec);
    }

    @Override
    public <K, V> RLocalCachedMap<K, V> getLocalCachedMap(String s, LocalCachedMapOptions localCachedMapOptions) {
        return client.getLocalCachedMap(s, localCachedMapOptions);
    }

    @Override
    public <K, V> RLocalCachedMap<K, V> getLocalCachedMap(String s, Codec codec, LocalCachedMapOptions localCachedMapOptions) {
        return client.getLocalCachedMap(s, codec, localCachedMapOptions);
    }

    @Override
    public <K, V> RMap<K, V> getMap(String s) {
        return client.getMap(s);
    }

    @Override
    public <K, V> RMap<K, V> getMap(String s, Codec codec) {
        return client.getMap(s, codec);
    }

    @Override
    public <K, V> RSetMultimap<K, V> getSetMultimap(String s) {
        return client.getSetMultimap(s);
    }

    @Override
    public <K, V> RSetMultimap<K, V> getSetMultimap(String s, Codec codec) {
        return client.getSetMultimap(s, codec);
    }

    @Override
    public <K, V> RSetMultimapCache<K, V> getSetMultimapCache(String s) {
        return client.getSetMultimapCache(s);
    }

    @Override
    public <K, V> RSetMultimapCache<K, V> getSetMultimapCache(String s, Codec codec) {
        return client.getSetMultimapCache(s, codec);
    }

    @Override
    public RSemaphore getSemaphore(String s) {
        return client.getSemaphore(s);
    }

    @Override
    public RPermitExpirableSemaphore getPermitExpirableSemaphore(String s) {
        return client.getPermitExpirableSemaphore(s);
    }

    @Override
    public RLock getLock(String s) {
        return client.getLock(s);
    }

    @Override
    public RLock getFairLock(String s) {
        return client.getFairLock(s);
    }

    @Override
    public RReadWriteLock getReadWriteLock(String s) {
        return client.getReadWriteLock(s);
    }

    @Override
    public <V> RSet<V> getSet(String s) {
        return client.getSet(s);
    }

    @Override
    public <V> RSet<V> getSet(String s, Codec codec) {
        return client.getSet(s, codec);
    }

    @Override
    public <V> RSortedSet<V> getSortedSet(String s) {
        return client.getSortedSet(s);
    }

    @Override
    public <V> RSortedSet<V> getSortedSet(String s, Codec codec) {
        return getSortedSet(s, codec);
    }

    @Override
    public <V> RScoredSortedSet<V> getScoredSortedSet(String s) {
        return client.getScoredSortedSet(s);
    }

    @Override
    public <V> RScoredSortedSet<V> getScoredSortedSet(String s, Codec codec) {
        return client.getScoredSortedSet(s, codec);
    }

    @Override
    public RLexSortedSet getLexSortedSet(String s) {
        return client.getLexSortedSet(s);
    }

    @Override
    public <M> RTopic<M> getTopic(String s) {
        return client.getTopic(s);
    }

    @Override
    public <M> RTopic<M> getTopic(String s, Codec codec) {
        return client.getTopic(s, codec);
    }

    @Override
    public <M> RPatternTopic<M> getPatternTopic(String s) {
        return client.getPatternTopic(s);
    }

    @Override
    public <M> RPatternTopic<M> getPatternTopic(String s, Codec codec) {
        return client.getPatternTopic(s, codec);
    }

    @Override
    public <V> RBlockingFairQueue<V> getBlockingFairQueue(String s) {
        return client.getBlockingFairQueue(s);
    }

    @Override
    public <V> RBlockingFairQueue<V> getBlockingFairQueue(String s, Codec codec) {
        return client.getBlockingFairQueue(s, codec);
    }

    @Override
    public <V> RQueue<V> getQueue(String s) {
        return client.getQueue(s);
    }

    @Override
    public <V> RDelayedQueue<V> getDelayedQueue(RQueue<V> rQueue) {
        return client.getDelayedQueue(rQueue);
    }

    @Override
    public <V> RQueue<V> getQueue(String s, Codec codec) {
        return client.getQueue(s, codec);
    }

    @Override
    public <V> RBlockingQueue<V> getBlockingQueue(String s) {
        return client.getBlockingQueue(s);
    }

    @Override
    public <V> RBlockingQueue<V> getBlockingQueue(String s, Codec codec) {
        return client.getBlockingQueue(s, codec);
    }

    @Override
    public <V> RBoundedBlockingQueue<V> getBoundedBlockingQueue(String s) {
        return client.getBoundedBlockingQueue(s);
    }

    @Override
    public <V> RBoundedBlockingQueue<V> getBoundedBlockingQueue(String s, Codec codec) {
        return client.getBoundedBlockingQueue(s, codec);
    }

    @Override
    public <V> RDeque<V> getDeque(String s) {
        return client.getDeque(s);
    }

    @Override
    public <V> RDeque<V> getDeque(String s, Codec codec) {
        return client.getDeque(s, codec);
    }

    @Override
    public <V> RBlockingDeque<V> getBlockingDeque(String s) {
        return client.getBlockingDeque(s);
    }

    @Override
    public <V> RBlockingDeque<V> getBlockingDeque(String s, Codec codec) {
        return client.getBlockingDeque(s, codec);
    }

    @Override
    public RAtomicLong getAtomicLong(String s) {
        return client.getAtomicLong(s);
    }

    @Override
    public RAtomicDouble getAtomicDouble(String s) {
        return client.getAtomicDouble(s);
    }

    @Override
    public RCountDownLatch getCountDownLatch(String s) {
        return client.getCountDownLatch(s);
    }

    @Override
    public RBitSet getBitSet(String s) {
        return client.getBitSet(s);
    }

    @Override
    public <V> RBloomFilter<V> getBloomFilter(String s) {
        return client.getBloomFilter(s);
    }

    @Override
    public <V> RBloomFilter<V> getBloomFilter(String s, Codec codec) {
        return client.getBloomFilter(s, codec);
    }

    @Override
    public RScript getScript() {
        return client.getScript();
    }

    @Override
    public RScheduledExecutorService getExecutorService(String s) {
        return client.getExecutorService(s);
    }

    @Override
    public RScheduledExecutorService getExecutorService(Codec codec, String s) {
        return client.getExecutorService(codec, s);
    }

    @Override
    public RRemoteService getRemoteService() {
        return client.getRemoteService();
    }

    @Override
    public RRemoteService getRemoteService(Codec codec) {
        return client.getRemoteService(codec);
    }

    @Override
    public RRemoteService getRemoteService(String s) {
        return client.getRemoteService(s);
    }

    @Override
    public RRemoteService getRemoteService(String s, Codec codec) {
        return client.getRemoteService(s, codec);
    }

    @Override
    public RBatch createBatch() {
        return client.createBatch();
    }

    @Override
    public RKeys getKeys() {
        return client.getKeys();
    }

    @Override
    public RLiveObjectService getLiveObjectService() {
        return client.getLiveObjectService();
    }

    @Override
    public void shutdown() {
        client.shutdown();
    }

    @Override
    public void shutdown(long l, long l1, TimeUnit timeUnit) {
        client.shutdown(l, l1, timeUnit);
    }

    @Override
    public Config getConfig() {
        return client.getConfig();
    }

    @Override
    public CodecProvider getCodecProvider() {
        return client.getCodecProvider();
    }

    @Override
    public ResolverProvider getResolverProvider() {
        return client.getResolverProvider();
    }

    @Override
    public NodesGroup<Node> getNodesGroup() {
        return client.getNodesGroup();
    }

    @Override
    public ClusterNodesGroup getClusterNodesGroup() {
        return client.getClusterNodesGroup();
    }

    @Override
    public boolean isShutdown() {
        return client.isShutdown();
    }

    @Override
    public boolean isShuttingDown() {
        return client.isShuttingDown();
    }
}
