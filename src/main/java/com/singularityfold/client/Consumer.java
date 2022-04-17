package com.singularityfold.client;

import com.alibaba.fastjson.JSON;
import com.singularityfold.pojo.Message;
import com.singularityfold.util.DateUtil;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 封装consumer客户端，装饰器模式，仅暴露出必要的接口，方便以后扩展
 *
 * @author Mr_Hades
 * @date 2022-03-27 11:06
 */
public class Consumer {

    private List<String> registerInfo = new ArrayList<>();
    private Client client;

    public Consumer(URI serverUri, String name) {
        this.client = new Client(serverUri, name, 0);
        try {
            client.connectBlocking();
        } catch (InterruptedException e) {
            //
        }
    }

    /**
     * 为该Client注册绑定一系列新的queue
     *
     * @param queueNames queue的名称
     * @param append     true表示追加绑定，false表示覆盖绑定
     */
    public void register(List<String> queueNames, boolean append) {
        if (!append) {
            registerInfo.clear();
        }
        registerInfo.addAll(queueNames);

        Message packaged = new Message(0, null, JSON.toJSONString(queueNames), DateUtil.getLocalTime());
        client.send(JSON.toJSONString(packaged));

    }

    /**
     * 为该Client注册绑定一个新的queue
     *
     * @param queueName queue的名称
     * @param append    true表示追加绑定，false表示覆盖绑定
     */
    public void register(String queueName, boolean append) {
        register(List.of(queueName), append);
    }


    /**
     * 函数式编程，自定义消息处理方式，由被调用方提供方法参数
     *
     * @param action 自定义接口函数
     */
    public void onMessage(java.util.function.Consumer<String> action) {
        client.setOnMessageAction(action);
    }

    /**
     * 获取注册绑定信息
     *
     * @return registerInfo
     */
    public List<String> getRegisterInfo() {
        return Collections.unmodifiableList(registerInfo);
    }

}
