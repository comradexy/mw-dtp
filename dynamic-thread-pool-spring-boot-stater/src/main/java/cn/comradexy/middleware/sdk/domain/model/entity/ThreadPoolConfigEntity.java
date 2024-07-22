package cn.comradexy.middleware.sdk.domain.model.entity;


import lombok.*;

/**
 * 线程池配置实体对象
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ThreadPoolConfigEntity {

    /**
     * 应用名称
     */
    @NonNull
    private String appName;

    /**
     * 线程池名称
     */
    @NonNull
    private String threadPoolName;

    /**
     * 核心线程数
     */
    private int corePoolSize;

    /**
     * 最大线程数
     */
    private int maximumPoolSize;

    /**
     * 当前活跃线程数
     */
    private int activeCount;

    /**
     * 当前池中线程数
     */
    private int poolSize;

    /**
     * 队列类型
     */
    private String queueType;

    /**
     * 当前队列任务数
     */
    private int queueSize;

    /**
     * 队列剩余任务数
     */
    private int remainingCapacity;
}