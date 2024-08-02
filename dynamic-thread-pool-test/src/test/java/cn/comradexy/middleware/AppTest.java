package cn.comradexy.middleware;

import cn.comradexy.middleware.sdk.domain.DynamicThreadPoolService;
import cn.comradexy.middleware.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.comradexy.middleware.sdk.registry.IRegistry;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class AppTest {
    private final Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Autowired
    private IRegistry registry;

    @Autowired
    DynamicThreadPoolService dynamicThreadPoolService;

    @Test
    public void test() {
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();
        List<ThreadPoolConfigEntity> threadPoolConfigEntities2 = dynamicThreadPoolService.queryThreadPoolList();
        threadPoolConfigEntities.addAll(threadPoolConfigEntities2);
        logger.info("动态线程池，上报线程池信息：{}", threadPoolConfigEntities);
        registry.reportThreadPool(threadPoolConfigEntities);
    }
}
