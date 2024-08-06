package cn.comradexy.middleware.controller;

import cn.comradexy.middleware.model.vo.Result;
import cn.comradexy.middleware.entity.ThreadPoolConfigEntity;
import cn.comradexy.middleware.valobj.RegistryEnumVO;
import com.alibaba.fastjson.JSON;
import org.redisson.api.RList;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理端controller
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-06
 * @Description: 管理端controller
 */
@RestController
//@CrossOrigin("*")
@RequestMapping("/dtp/api/")
public class DynamicThreadPoolController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    public RedissonClient redissonClient;

    /**
     * 查询线程池数据
     */
    @GetMapping(value = "query_thread_pool_list")
    public Result<List<ThreadPoolConfigEntity>> queryThreadPoolList() {
        try {
            RList<ThreadPoolConfigEntity> cacheList =
                    redissonClient.getList(RegistryEnumVO.THREAD_POOL_CONFIG_LIST_KEY.getKey());
            return Result.success(cacheList.readAll());
        } catch (Exception e) {
            logger.error("查询线程池数据异常", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询线程池配置
     */
    @GetMapping(value = "query_thread_pool_config")
    public Result<ThreadPoolConfigEntity> queryThreadPoolConfig(@RequestParam String appName,
                                                                @RequestParam String threadPoolName) {
        try {
            String cacheKey = RegistryEnumVO.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY.getKey() +
                    RegistryEnumVO.CONNECTOR.getKey() +
                    appName +
                    RegistryEnumVO.CONNECTOR.getKey() +
                    threadPoolName;
            ThreadPoolConfigEntity threadPoolConfigEntity =
                    redissonClient.<ThreadPoolConfigEntity>getBucket(cacheKey).get();
            return Result.success(threadPoolConfigEntity);
        } catch (Exception e) {
            logger.error("查询线程池配置异常", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改线程池配置
     */
    @PostMapping(value = "update_thread_pool_config")
    public Result<Boolean> updateThreadPoolConfig(@RequestBody ThreadPoolConfigEntity request) {
        try {
            logger.info("修改线程池配置开始 {} {} {}", request.getAppName(), request.getThreadPoolName(),
                    JSON.toJSONString(request));
            RTopic topic = redissonClient.getTopic(RegistryEnumVO.DYNAMIC_THREAD_POOL_REDIS_TOPIC.getKey() +
                    RegistryEnumVO.CONNECTOR.getKey() +
                    request.getAppName());
            topic.publish(request);
            logger.info("修改线程池配置完成 {} {}", request.getAppName(), request.getThreadPoolName());
            return Result.success(true);
        } catch (Exception e) {
            logger.error("修改线程池配置异常 {}", JSON.toJSONString(request), e);
            return Result.error(e.getMessage());
        }
    }

}
