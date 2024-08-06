package cn.comradexy.middleware.sdk.domain;

import cn.comradexy.middleware.entity.ThreadPoolConfigEntity;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 动态线程池服务
 *
 * @Author: ComradeXY
 * @CreateTime: 2021-07-15
 * @Description: 动态线程池服务
 */
public class DynamicThreadPoolService implements IDynamicThreadPoolService {
    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolService.class);

    private final String appName;
    private final Map<String, ThreadPoolExecutor> threadPoolExecutorMap;

    public DynamicThreadPoolService(String appName, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        this.appName = appName;
        this.threadPoolExecutorMap = threadPoolExecutorMap;
    }

    @Override
    public List<ThreadPoolConfigEntity> queryThreadPoolList() {
        Set<String> threadPoolNames = threadPoolExecutorMap.keySet();
        List<ThreadPoolConfigEntity> threadPoolVOs = new ArrayList<>(threadPoolNames.size());
        for (String threadPoolName : threadPoolNames) {
            ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolName);
            ThreadPoolConfigEntity threadPoolConfigVO = ThreadPoolConfigEntity.builder()
                    .appName(appName)
                    .threadPoolName(threadPoolName)
                    .corePoolSize(threadPoolExecutor.getCorePoolSize())
                    .maximumPoolSize(threadPoolExecutor.getMaximumPoolSize())
                    .activeCount(threadPoolExecutor.getActiveCount())
                    .poolSize(threadPoolExecutor.getPoolSize())
                    .queueType(threadPoolExecutor.getQueue().getClass().getSimpleName())
                    .queueSize(threadPoolExecutor.getQueue().size())
                    .remainingCapacity(threadPoolExecutor.getQueue().remainingCapacity())
                    .build();
            threadPoolVOs.add(threadPoolConfigVO);
        }
        return threadPoolVOs;
    }

    @Override
    public ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName) {
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolName);
        if (null == threadPoolExecutor) return new ThreadPoolConfigEntity(appName, threadPoolName);

        // 线程池配置数据
        ThreadPoolConfigEntity threadPoolConfigVO = ThreadPoolConfigEntity.builder()
                .appName(appName)
                .threadPoolName(threadPoolName)
                .corePoolSize(threadPoolExecutor.getCorePoolSize())
                .maximumPoolSize(threadPoolExecutor.getMaximumPoolSize())
                .activeCount(threadPoolExecutor.getActiveCount())
                .poolSize(threadPoolExecutor.getPoolSize())
                .queueType(threadPoolExecutor.getQueue().getClass().getSimpleName())
                .queueSize(threadPoolExecutor.getQueue().size())
                .remainingCapacity(threadPoolExecutor.getQueue().remainingCapacity())
                .build();

        if (logger.isDebugEnabled()) {
            logger.info("动态线程池，配置查询 应用名:{} 线程名:{} 池化配置:{}", appName, threadPoolName,
                    JSON.toJSONString(threadPoolConfigVO));
        }

        return threadPoolConfigVO;
    }

    @Override
    public void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity) {
        // 校验应用名
        if (null == threadPoolConfigEntity || !appName.equals(threadPoolConfigEntity.getAppName())) return;

        // 判断线程池是否存在
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolConfigEntity.getThreadPoolName());
        if (null == threadPoolExecutor) return;

        // 设置参数：调整核心线程数和最大线程数
        // TODO: 目前只支持调整核心线程数和最大线程数，后续可以扩展
        threadPoolExecutor.setCorePoolSize(threadPoolConfigEntity.getCorePoolSize());
        threadPoolExecutor.setMaximumPoolSize(threadPoolConfigEntity.getMaximumPoolSize());
    }
}
