package com.singularityfold.core;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Mr_Hades
 * @date 2022-03-26 15:57
 */
public class MessageQueue extends LinkedBlockingQueue<String> {

    // 每个queue的bindingKey，同一个包下才能够访问
    private final String bindingKey;

    // 为每个queue起一个具有分辨性的名字，同一个包下才能够访问
    private final String name;

    // 在当前queue上工作的所有的worker线程，只允许同包访问
    final ArrayList<Thread> workers = new ArrayList<>();

    public String getBindingKey() {
        return bindingKey;
    }

    public String getName() {
        return name;
    }

    public MessageQueue(String bindingKey, String name) {
        super();
        this.bindingKey = bindingKey;
        this.name = name;
    }

    @Override
    public String toString() {
        return "MessageQueue{" +
                "bindingKey='" + bindingKey + '\'' +
                ", name='" + name + '\'' +
                ", elements=" + super.toString() +
                '}';
    }
}
