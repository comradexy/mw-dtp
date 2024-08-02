package cn.comradexy.middleware.sdk.registry.redis;

import cn.comradexy.middleware.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.comradexy.middleware.sdk.domain.model.valobj.RegistryEnumVO;
import cn.comradexy.middleware.sdk.registry.IRegistry;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

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
        // TODO: 对threadPoolConfigEntities内的数据进行去重
        

        RList<ThreadPoolConfigEntity> list =
                redissonClient.getList(RegistryEnumVO.THREAD_POOL_CONFIG_LIST_KEY.getKey());
        if (null == list) {
            logger.error("Redis 注册中心，获取缓存对象失败。{}", RegistryEnumVO.THREAD_POOL_CONFIG_LIST_KEY.getKey());
            return;
        }
        list.delete();
        list.addAll(threadPoolConfigEntities);
    }

    @Override
    public void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity) {
        String cacheKey = RegistryEnumVO.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY.getKey() +
                "_" + threadPoolConfigEntity.getAppName() +
                "_" + threadPoolConfigEntity.getThreadPoolName();
        RBucket<ThreadPoolConfigEntity> bucket = redissonClient.getBucket(cacheKey);
        if (null == bucket) {
            logger.error("Redis 注册中心，获取缓存对象失败。{}", cacheKey);
            return;
        }
        // 缓存30天
        bucket.set(threadPoolConfigEntity, Duration.ofDays(30));
    }

}
