package cn.comradexy.sdk.registry.redis;

import cn.comradexy.util.entity.ThreadPoolConfigEntity;
import cn.comradexy.util.valobj.RegistryEnumVO;
import cn.comradexy.sdk.registry.IRegistry;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Redis 注册中心
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-07-23
 * @Description: Redis 注册中心
 */
public class RedisRegistry implements IRegistry {
    private final Logger logger = LoggerFactory.getLogger(RedisRegistry.class);

    private final RedissonClient redissonClient;

    public RedisRegistry(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolConfigEntities) {
        // 对threadPoolConfigEntities内的数据进行去重
        // 使用HashSet并结合@EqualsAndHashCode(of = {"appName", "threadPoolName"})去重
        Set<ThreadPoolConfigEntity> threadPoolConfigEntitySet = new HashSet<>(threadPoolConfigEntities);
        threadPoolConfigEntities = new ArrayList<>(threadPoolConfigEntitySet);

        RList<ThreadPoolConfigEntity> list =
                redissonClient.getList(RegistryEnumVO.THREAD_POOL_CONFIG_LIST_KEY.getKey());
        if (null == list) {
            logger.error("Redis 注册中心，获取缓存对象失败。{}", RegistryEnumVO.THREAD_POOL_CONFIG_LIST_KEY.getKey());
            return;
        }
        list.delete();
        list.addAll(threadPoolConfigEntities);
        logger.info("Redis 注册中心，上报线程池信息：{}", threadPoolConfigEntities);
    }

    @Override
    public void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity) {
        String cacheKey = RegistryEnumVO.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY.getKey() +
                RegistryEnumVO.CONNECTOR.getKey() + threadPoolConfigEntity.getAppName() +
                RegistryEnumVO.CONNECTOR.getKey() + threadPoolConfigEntity.getThreadPoolName();
        RBucket<ThreadPoolConfigEntity> bucket = redissonClient.getBucket(cacheKey);
        if (null == bucket) {
            logger.error("Redis 注册中心，获取缓存对象失败。{}", cacheKey);
            return;
        }
        // 缓存30天
        bucket.set(threadPoolConfigEntity, Duration.ofDays(30));
        logger.info("Redis 注册中心，上报线程池配置：{}", threadPoolConfigEntity);
    }

}
