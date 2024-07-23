package cn.comradexy.middleware.sdk.registry.redis;

import cn.comradexy.middleware.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.comradexy.middleware.sdk.registry.IRegistry;
import org.redisson.api.RedissonClient;

import java.util.List;

/**
 * Redis 注册中心
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-07-23
 * @Description: Redis 注册中心
 */
public class RedisRegistry implements IRegistry {
    private final RedissonClient redissonClient;

    public RedisRegistry(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolEntities) {
        // TODO: 上报所有线程池信息
    }

    @Override
    public void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity) {
        // TODO: 上报单个线程池的配置参数
    }

}
