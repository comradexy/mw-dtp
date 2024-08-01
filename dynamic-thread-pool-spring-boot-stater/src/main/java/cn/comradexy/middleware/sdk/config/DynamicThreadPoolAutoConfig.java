package cn.comradexy.middleware.sdk.config;

import cn.comradexy.middleware.sdk.domain.DynamicThreadPoolService;
import cn.comradexy.middleware.sdk.domain.IDynamicThreadPoolService;
import cn.comradexy.middleware.sdk.registry.IRegistry;
import cn.comradexy.middleware.sdk.registry.redis.RedisRegistry;
import cn.comradexy.middleware.sdk.trigger.job.ThreadPoolDataReportJob;
import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 动态线程池自动配置
 *
 * @Author: ComradeXY
 * @CreateTime: 2021-07-15
 * @Description: 动态线程池自动配置
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties(DynamicThreadPoolAutoProperties.class)
public class DynamicThreadPoolAutoConfig {
    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolAutoConfig.class);

    @Bean("redissonClient")
    public RedissonClient redissonClient(DynamicThreadPoolAutoProperties properties) {
        Config config = new Config();
        // 根据需要可以设定编解码器；https://github.com/redisson/redisson/wiki/4.-%E6%95%B0%E6%8D%AE%E5%BA%8F%E5%88%97%E5%8C%96
        config.setCodec(JsonJacksonCodec.INSTANCE);

        config.useSingleServer()
                .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
                .setPassword(properties.getPassword())
                .setConnectionPoolSize(properties.getPoolSize())
                .setConnectionMinimumIdleSize(properties.getMinIdleSize())
                .setIdleConnectionTimeout(properties.getIdleTimeout())
                .setConnectTimeout(properties.getConnectTimeout())
                .setRetryAttempts(properties.getRetryAttempts())
                .setRetryInterval(properties.getRetryInterval())
                .setPingConnectionInterval(properties.getPingInterval())
                .setKeepAlive(properties.isKeepAlive())
        ;

        RedissonClient redissonClient = Redisson.create(config);

        logger.info("动态线程池，注册器（redis）链接初始化完成。{} {} {}", properties.getHost(), properties.getPoolSize(),
                !redissonClient.isShutdown());

        return redissonClient;
    }

    @Bean
    public IRegistry redisRegistry(RedissonClient redissonClient) {
        return new RedisRegistry(redissonClient);
    }

    @Bean
    public ThreadPoolDataReportJob threadPoolDataReportJob(IDynamicThreadPoolService dynamicThreadPoolService,
                                                           IRegistry registry) {
        return new ThreadPoolDataReportJob(dynamicThreadPoolService, registry);
    }

    @Bean("dynamicThreadPoolService")
    public DynamicThreadPoolService dynamicThreadPoolService(ApplicationContext applicationContext,
                                                             Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        String appName = applicationContext.getEnvironment().getProperty("spring.application.name");
        // 如果没有配置应用名，则使用缺省名，同时打印警告日志
        if (StringUtils.isBlank(appName)) {
            appName = "缺省";
            logger.warn("应用未配置 spring.application.name, 无法获取到应用名称！");
        }

        // TODO: 获取缓存数据，设置本地线程池配置


        return new DynamicThreadPoolService(appName, threadPoolExecutorMap);
    }
}
