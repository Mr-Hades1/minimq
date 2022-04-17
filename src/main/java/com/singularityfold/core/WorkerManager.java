package com.singularityfold.core;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 创建并管理work线程，负责将消息分发给consumer
 *
 * @author Mr_Hades
 * @date 2022-03-26 10:15
 */
@Slf4j(topic = "WorkerManager")
public class WorkerManager {
    // 固定数量线程数，方便后续扩展和管理
    static private Thread[] threads;

    static private boolean inited = false;

    // 初始化方法，进行线程的创建
    public static void init(int threadNum) {
        if (inited)
            return;
        synchronized (WorkerManager.class) {
            // double check lock
            if (inited)
                return;
            threads = new Thread[threadNum];
            for (int i = 0; i < threadNum; i++) {
                Thread thread = new Thread(new Task(i), "worker-" + i);
                // 将thread添加到每个queue的worker线程中
                QueueManager.getQueue(i).workers.add(thread);
                thread.setDaemon(true); // 设置为当前线程的守护线程，随着主程序的终止而被杀死
                thread.start();
                threads[i] = thread;
            }
            log.info("{} worker threads are started.", threadNum);
            inited = true;
        }
    }

    public static boolean isInited() {
        return inited;
    }

    /**
     * 任务对象，持续取queue中的消息并将其转发到对应的channel中
     * 两种情况会阻塞
     * 1. queue中无元素
     * 2. queue没有consumer来绑定
     */
    private static class Task implements Runnable {

        private Logger log = LoggerFactory.getLogger(Task.class);

        private MessageQueue queue;

        @Override
        public void run() {
            log.debug("worker thread of queue {} is working", queue.getName());
            String message;
            while (true) {
                try {
                    message = queue.take(); // 阻塞获取消息
                    List<ChannelId> channelIds;
                    while ((channelIds = BasicMaps.queueConsumerMap.get(queue.getName())) == null ||
                            channelIds.isEmpty()) {
                        try {
                            Thread.sleep(Long.MAX_VALUE);
                            log.debug("no consumers, sleeping...");
                        } catch (InterruptedException e) {
                            //以防有人捣乱
                            log.debug("interrupted...");
                        }
                    }
                    for (ChannelId channelId : channelIds) {
                        Channel channel = BasicMaps.clients.find(channelId);
                        channel.writeAndFlush(
                                new TextWebSocketFrame(JSON.toJSONString(message))
                        ); // 发送消息
                    }
                } catch (InterruptedException e) {
                    // 不做异常处理，继续阻塞获取消息
                }
            }
        }


        // 绑定的工作队列的序号
        public Task(int queueIndex) {
            this.queue = QueueManager.getQueue(queueIndex);
        }
    }

}
