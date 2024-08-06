package cn.comradexy.middleware.sdk.trigger.listener;

import cn.comradexy.middleware.entity.ThreadPoolConfigEntity;
import cn.comradexy.middleware.sdk.domain.IDynamicThreadPoolService;
import cn.comradexy.middleware.sdk.registry.IRegistry;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 动态线程池变更监听
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-02
 * @Description: 动态线程池变更监听
 */
public class ThreadPoolConfigAdjustListener implements MessageListener<ThreadPoolConfigEntity> {
    private final Logger logger = LoggerFactory.getLogger(ThreadPoolConfigAdjustListener.class);

    private final IDynamicThreadPoolService dynamicThreadPoolService;

    private final IRegistry registry;

    public ThreadPoolConfigAdjustListener(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.registry = registry;
    }

    /**
     * 消费者的消费逻辑
     *
     * @param charSequence           消息主题/频道
     * @param threadPoolConfigEntity 消息内容
     */
    @Override
    public void onMessage(CharSequence charSequence, ThreadPoolConfigEntity threadPoolConfigEntity) {
        logger.info("动态线程池，调整线程池配置。线程池名称:{} 核心线程数:{} 最大线程数:{}",
                threadPoolConfigEntity.getThreadPoolName(),
                threadPoolConfigEntity.getCorePoolSize(),
                threadPoolConfigEntity.getMaximumPoolSize());
        // 更新线程池配置
        dynamicThreadPoolService.updateThreadPoolConfig(threadPoolConfigEntity);

        // 线程池配置变更后，需要重新上报线程池信息
        // 重新上报所有线程池信息
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();
        registry.reportThreadPool(threadPoolConfigEntities);
        // 重新上报当前线程池信息
        ThreadPoolConfigEntity threadPoolConfigEntityCurrent =
                dynamicThreadPoolService.queryThreadPoolConfigByName(threadPoolConfigEntity.getThreadPoolName());
        registry.reportThreadPoolConfigParameter(threadPoolConfigEntityCurrent);
    }
}

