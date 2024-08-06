package cn.comradexy;

import cn.comradexy.sdk.domain.DynamicThreadPoolService;
import cn.comradexy.util.entity.ThreadPoolConfigEntity;
import cn.comradexy.sdk.registry.IRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
    private final Logger logger = LoggerFactory.getLogger(AppTest.class);
    @Resource
    private IRegistry registry;
    @Resource
    private DynamicThreadPoolService dynamicThreadPoolService;
    @Resource
    private RTopic dynamicThreadPoolRedisTopic;

    @Test
    public void registryTest() {
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();
        List<ThreadPoolConfigEntity> threadPoolConfigEntities2 = dynamicThreadPoolService.queryThreadPoolList();
        threadPoolConfigEntities.addAll(threadPoolConfigEntities2);
        logger.info("动态线程池，上报线程池信息：{}", threadPoolConfigEntities);
        registry.reportThreadPool(threadPoolConfigEntities);
    }

    @Test
    public void redisTopicTest() throws InterruptedException {
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();
        ThreadPoolConfigEntity threadPoolConfigEntity = threadPoolConfigEntities.get(0);
        logger.info("调整前 -- 线程池名称:{} 核心线程数:{} 最大线程数:{}",
                threadPoolConfigEntity.getThreadPoolName(),
                threadPoolConfigEntity.getCorePoolSize(),
                threadPoolConfigEntity.getMaximumPoolSize());

        threadPoolConfigEntity.setCorePoolSize(10);
        threadPoolConfigEntity.setMaximumPoolSize(20);
        dynamicThreadPoolRedisTopic.publish(threadPoolConfigEntity); // 发布消息，异步

        logger.info("等待消息消费完成, 5秒后再查询线程池配置");
        new CountDownLatch(1).await(5, TimeUnit.SECONDS); // 等待5秒，等待消息消费完成
        logger.info("调整后 -- 线程池名称:{} 核心线程数:{} 最大线程数:{}",
                threadPoolConfigEntity.getThreadPoolName(),
                threadPoolConfigEntity.getCorePoolSize(),
                threadPoolConfigEntity.getMaximumPoolSize());
    }
}
