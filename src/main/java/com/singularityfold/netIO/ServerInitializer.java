package com.singularityfold.netIO;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author Mr_Hades
 * @date 2022-03-25 21:17
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        //websocket 基于http协议，所需要的http 编解码器
        pipeline.addLast(new HttpServerCodec());
        // 对数据流进行分块
        pipeline.addLast(new ChunkedWriteHandler());
        //对httpMessage 进行聚合处理，聚合成request或 response
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));
        // 简单处理，忽略心跳
        // 解析WebSocket帧的结构
        pipeline.addLast(new WebSocketServerProtocolHandler("/"));

        //自定义的handler
        pipeline.addLast(new MessageHandler());
    }
}
