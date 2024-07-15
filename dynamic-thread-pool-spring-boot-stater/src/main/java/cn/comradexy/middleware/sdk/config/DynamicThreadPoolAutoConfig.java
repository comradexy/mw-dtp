package cn.comradexy.middleware.sdk.config;

import cn.comradexy.middleware.sdk.domain.DynamicThreadPoolService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class DynamicThreadPoolAutoConfig {
    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolAutoConfig.class);

    @Bean("dynamicThreadPoolService")
    public DynamicThreadPoolService dynamicThreadPoolService(ApplicationContext applicationContext,
                                                             Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        String appName = applicationContext.getEnvironment().getProperty("spring.application.name");
        // 如果没有配置应用名，则使用缺省名，同时打印警告日志
        if (StringUtils.isBlank(appName)) {
            appName = "缺省";
            logger.warn("应用未配置 spring.application.name, 无法获取到应用名称！");
        }

        return new DynamicThreadPoolService(appName, threadPoolExecutorMap);
    }
}
