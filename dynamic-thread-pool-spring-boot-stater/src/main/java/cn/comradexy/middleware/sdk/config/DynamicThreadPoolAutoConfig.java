package cn.comradexy.middleware.sdk.config;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class DynamicThreadPoolAutoConfig {
    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolAutoConfig.class);

    @Bean
    public String dynamicThreadPoolService(ApplicationContext applicationContext,
                                           Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        String appName = applicationContext.getEnvironment().getProperty("spring.application.name");
        // 如果没有配置应用名，则使用缺省名，同时打印警告日志
        if (StringUtils.isBlank(appName)) {
            appName = "缺省";
            logger.warn("缺少应用名配置：spring.application.name is empty");
            return "";
        }

        // 打印线程池信息
        Set<String> threadPoolExecutorKeys = threadPoolExecutorMap.keySet();
        List<ThreadPoolInfo> threadPoolInfoList = new ArrayList<>();
        for (String key : threadPoolExecutorKeys) {
            ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(key);
            ThreadPoolInfo threadPoolInfo = new ThreadPoolInfo(key,
                    threadPoolExecutor.getPoolSize(),
                    threadPoolExecutor.getCorePoolSize(),
                    threadPoolExecutor.getMaximumPoolSize(),
                    threadPoolExecutor.getActiveCount(),
                    threadPoolExecutor.getQueue().size());
            threadPoolInfoList.add(threadPoolInfo);
        }
        logger.info("{}-线程池信息：{}", appName, JSON.toJSONString(threadPoolInfoList));

        return "";
    }

    @Data
    @AllArgsConstructor
    class ThreadPoolInfo {
        private String threadPoolName;
        private int poolSize;
        private int corePoolSize;
        private int maximumPoolSize;
        private int activeCount;
        private int queueSize;
    }
}
