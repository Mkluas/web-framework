package cn.mklaus.framework.extend;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.*;

/**
 * @author Mklaus
 * @date 2018-01-29 下午3:14
 */
public abstract class AbstractEventQueue<T extends ApplicationEvent> implements ApplicationListener<T>, InitializingBean {

    private LinkedBlockingQueue<T> queue;

    @Async
    @Override
    public void onApplicationEvent(T event) {
        this.queue.offer(event);
    }

    @Override
    public void afterPropertiesSet() {
        this.queue = new LinkedBlockingQueue<>();

        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        singleThreadPool.execute(() -> {
            while (true) {
                try {
                    T event = this.queue.take();
                    this.handleEvent(event);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        singleThreadPool.shutdown();
    }

    /**
     * 处理事件
     * @param event 从阻塞队列获取的事件
     */
    protected abstract void handleEvent(T event);

}
