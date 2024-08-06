package cn.comradexy.sdk.registry;

import cn.comradexy.util.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * 注册中心接口
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-07-23
 * @Description: 注册中心接口
 */
public interface IRegistry {
    /**
     * 上报所有线程池信息
     *
     * @param threadPoolEntities 线程池列表
     */
    void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolEntities);

    /**
     * 上报单个线程池的配置参数
     *
     * @param threadPoolConfigEntity 线程池配置参数
     */
    void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity);
}
