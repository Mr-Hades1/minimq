package com.singularityfold.netIO;

import com.alibaba.fastjson.JSON;
import com.singularityfold.core.BasicMaps;
import com.singularityfold.core.QueueManager;
import com.singularityfold.pojo.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Mr_Hades
 * @date 2022-03-25 21:45
 */
@Slf4j(topic = "MessageHandler")
public class MessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //用于记录和管理所有客户端的channel，可以自动移除已经断开的会话
    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 获取客户端所传输的消息
        String data = msg.text();
        // 获取当前通话channel
        Channel channel = ctx.channel();
        clients.add(channel); // 将其纳入管理
        Message message;
        try {
            // 解析出消息类型
            message = JSON.parseObject(data, Message.class);
            // 来自consumer的订阅注册消息
            if (message.getType() == 0) {

                // TODO 完成覆盖绑定的效果
                List<String> queueNames = (List<String>) JSON.parseObject(message.getExtend(), List.class);
                ConcurrentHashMap<String, List<ChannelId>> map = BasicMaps.queueConsumerMap;
                for (String queueName : queueNames) {
                    // 该queue此前未被任何consumer注册
                    if (!map.containsKey(queueName)) {
                        ArrayList<ChannelId> list = new ArrayList<>();
                        list.add(channel.id());
                        map.put(queueName, list);
                    } else {
                        map.get(queueName).add(channel.id());
                    }
                    QueueManager.signal(queueName);
                }


            }
            // 来自producer的普通消息
            else if (message.getType() == 1) {
                String content = message.getContent();
                String routingKey = message.getExtend();
                QueueManager.put(content, routingKey);

            } else {
                throw new Exception();
            }

        } catch (Exception e) {
            // 消息格式有误
            log.debug("{}消息格式有误", data);
            channel.writeAndFlush(
                    new TextWebSocketFrame("消息格式有误")
            ).addListener(future -> {
                channel.close();
                log.debug("成功移除channel");
            });

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.debug(cause.getMessage());
    }
}
