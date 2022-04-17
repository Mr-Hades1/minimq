package com.singularityfold.client;

import com.alibaba.fastjson.JSON;
import com.singularityfold.pojo.Message;
import com.singularityfold.util.DateUtil;

import java.net.URI;

/**
 * 封装producer客户端，装饰器模式，仅暴露出必要的接口
 *
 * @author Mr_Hades
 * @date 2022-03-27 11:06
 */
public class Producer {

    private Client client;
    private String routingKey;

    public Producer(URI serverUri, String name) {
        this.client = new Client(serverUri, name, 1);
        try {
            client.connectBlocking();
        } catch (InterruptedException e) {
            //
        }
    }

    /**
     * 发送一条消息，包含routingKey
     *
     * @param message    消息，为json字符串格式
     * @param routingKey routingKey
     */
    public void send(String message, String routingKey) {
        if (this.routingKey == null)
            this.routingKey = routingKey;
        Message packaged = new Message(1, message, routingKey, DateUtil.getLocalTime());
        client.send(JSON.toJSONString(packaged));
    }

    /**
     * 根据默认routingKey来进行消息发送
     *
     * @param massage 消息
     */
    public void send(String massage) {
        if (routingKey == null){
            System.out.println("Please set a default routing key.");
            return;
        }

        send(massage, this.routingKey);
    }

    /**
     * 设置默认的routingKey
     *
     * @param key routingKey
     */
    public void setDefaultRoutingKey(String key) {
        this.routingKey = key;
    }

}
