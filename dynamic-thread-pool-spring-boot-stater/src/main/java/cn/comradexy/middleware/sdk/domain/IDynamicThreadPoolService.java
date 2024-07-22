package cn.comradexy.middleware.sdk.domain;

import cn.comradexy.middleware.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * 动态线程池服务
 */
public interface IDynamicThreadPoolService {
    /**
     * 查询线程池列表
     *
     * @return 线程池列表
     */
    List<ThreadPoolConfigEntity> queryThreadPoolList();

    /**
     * 根据线程池名称查询线程池配置
     *
     * @param threadPoolName 线程池名称
     * @return 线程池配置
     */
    ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName);

    /**
     * 更新线程池配置
     *
     * @param threadPoolConfigEntity 线程池配置
     */
    void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity);

}