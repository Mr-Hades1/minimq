package com.singularityfold.core;

import com.singularityfold.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @author Mr_Hades
 * @date 2022-03-26 10:37
 */
@Slf4j(topic = "QueueManager")
public class QueueManager {
    private static MessageQueue[] queues;

    private static HashMap<String, MessageQueue> queueMap;

    private static boolean inited = false;

    /**
     * 队列管理器初始化
     *
     * @param queueNum    队列的数量
     * @param bindingKeys 对应于每个queue的bindingKey
     * @param queueNames  对应于每个queue的名字，name不可重复
     * @throws RuntimeException 如果queueNum和bindingKeys的长度不对应，抛出异常
     */
    public static void init(int queueNum, String[] bindingKeys, String[] queueNames) throws RuntimeException {
        if (inited)
            return;
        synchronized (QueueManager.class) {
            // double check lock，先做并发控制，方便以后扩展
            if (inited)
                return;
            if (!(bindingKeys.length == queueNum)) {
                log.error("The length of bindingKeys not equal to queueNum.");
                throw new RuntimeException("The length of bindingKeys not equal to queueNum.");
            }

            queues = new MessageQueue[queueNum];
            queueMap = new HashMap<>();
            // 保证名称不能有重复的，如果有的话，将原名做稍微修改
            HashMap<String, Integer> chosenNames = new HashMap<>();
            for (int i = 0; i < queueNum; i++) {
                if (queueNames == null || i >= queueNames.length || queueNames[i] == null)
                    queues[i] = new MessageQueue(bindingKeys[i], "queue_" + i);
                else {
                    String name = queueNames[i];
                    if (chosenNames.containsKey(name)) {
                        Integer old = chosenNames.get(name);
                        String newName = name + old;
                        queues[i] = new MessageQueue(bindingKeys[i], newName);
                        log.warn("A duplicated queue queueNames {} is modified to {}", name, newName);
                        chosenNames.put(name, old + 1);
                        queueMap.put(newName, queues[i]);
                    } else {
                        queues[i] = new MessageQueue(bindingKeys[i], name);
                        chosenNames.put(name, 1);
                        queueMap.put(name, queues[i]);
                    }
                }

            }
            log.info("{} queues are ready.", queueNum);
            inited = true;
        }
    }

    /**
     * 队列管理器初始化
     *
     * @param queueNum    队列的数量
     * @param bindingKeys 对应于每个queue的bindingKey
     * @throws RuntimeException 如果queueNum和bindingKeys的长度不对应，抛出异常
     */
    public static void init(int queueNum, String[] bindingKeys) throws RuntimeException {
        init(queueNum, bindingKeys, null);
    }


    // 供外部访问，是否初始化
    public static boolean isInited() {
        return inited;
    }

    // 放入一条消息到消息队列中
    public static void put(String message, String routingKey) {
        if (!inited) {
//            log.error("Please init the QueueManager first.");
            throw new RuntimeException("QueueManager not initiated.");
        }
        for (int i = 0; i < queues.length; i++) {
            String bindingKey = queues[i].getBindingKey();
            if (KeyUtil.routingKeyCompare(routingKey, bindingKey)) {
                try {
                    queues[i].put(message);
                } catch (InterruptedException e) {
                    // 忽略打断
                }
            }
        }
    }

    public static MessageQueue getQueue(int index) {
        if (!inited) {
//            log.error("Please init the QueueManager first.");
            throw new RuntimeException("QueueManager not initiated.");
        }
        return queues[index];
    }

    public boolean containsQueue(String name){
        if (!inited) {
//            log.error("Please init the QueueManager first.");
            throw new RuntimeException("QueueManager not initiated.");
        }
        return queueMap.containsKey(name);
    }

    /**
     * 唤醒在某个queue上等待的线程
     *
     * @param queueName 队列名
     */
    public static void signal(String queueName) {
        for (Thread worker : queueMap.get(queueName).workers) {
            worker.interrupt();
        }
    }

}
