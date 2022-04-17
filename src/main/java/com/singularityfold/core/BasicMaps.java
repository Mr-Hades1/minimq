package com.singularityfold.core;

import com.singularityfold.netIO.MessageHandler;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用来管理消息队列中的基础映射关系
 * 1. producer的channelId与自己的键，方便netty的nio线程将消息放到对应的队列中
 * 2. 队列的序号与channelId，方便分发线程进行转发到对应的订阅consumer
 *
 * @author Mr_Hades
 * @date 2022-03-26 11:37
 */
public class BasicMaps {


    /*
        队列的名称与channelId，方便分发线程进行转发到对应的订阅consumer
        写线程有多个，即nio线程组，consumer会动态订阅queue
        读线程有多个，为worker线程组，会从中获取订阅信息
     */
    public static ConcurrentHashMap<String, List<ChannelId>> queueConsumerMap = new ConcurrentHashMap<>();

    /*
        用于记录和管理所有客户端的channel，可以自动移除已经断开的会话
        此处记录一个对象引用副本
     */
    public static ChannelGroup clients = MessageHandler.clients;

}
