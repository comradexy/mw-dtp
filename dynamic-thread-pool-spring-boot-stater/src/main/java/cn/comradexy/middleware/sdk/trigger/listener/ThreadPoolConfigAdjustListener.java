package cn.comradexy.middleware.sdk.trigger.listener;

import cn.comradexy.middleware.sdk.domain.model.entity.ThreadPoolConfigEntity;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 动态线程池变更监听
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-02
 * @Description: 动态线程池变更监听
 */
public class ThreadPoolConfigAdjustListener implements MessageListener<ThreadPoolConfigEntity> {
    private final Logger logger= LoggerFactory.getLogger(ThreadPoolConfigAdjustListener.class);

    @Override
    public void onMessage(CharSequence charSequence, ThreadPoolConfigEntity threadPoolConfigEntity) {
        // TODO: 线程池配置变更监听

    }
}
