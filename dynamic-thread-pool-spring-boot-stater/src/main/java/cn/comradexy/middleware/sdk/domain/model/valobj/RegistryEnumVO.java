package cn.comradexy.middleware.sdk.domain.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 注册中心枚举类
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-07-24
 * @Description: 注册中心枚举类
 */
@AllArgsConstructor
@Getter
public enum RegistryEnumVO {
    THREAD_POOL_CONFIG_LIST_KEY("THREAD_POOL_CONFIG_LIST_KEY", "线程池配置列表"),
    THREAD_POOL_CONFIG_PARAMETER_LIST_KEY("THREAD_POOL_CONFIG_PARAMETER_LIST_KEY", "线程池配置参数"),
    DYNAMIC_THREAD_POOL_REDIS_TOPIC("DYNAMIC_THREAD_POOL_REDIS_TOPIC", "动态线程池监听主题配置"),
    CONNECTOR("_", "连接符");

    private final String key;
    private final String desc;
}
