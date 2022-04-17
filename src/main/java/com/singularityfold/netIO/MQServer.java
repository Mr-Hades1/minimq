package com.singularityfold.netIO;

/**
 * @author Mr_Hades
 * @date 2022-3-25 21:45
 */

import com.singularityfold.util.BannerUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j(topic = "MQServer")
public class MQServer {

    private static class SingletonWSServer {
        static final MQServer instance = new MQServer();
    }

    public static MQServer getInstance() {
        return SingletonWSServer.instance;
    }

    private EventLoopGroup mainGroup;
    private EventLoopGroup subGroup;
    private ServerBootstrap server;
    private ChannelFuture future;

    public MQServer() {
        mainGroup = new NioEventLoopGroup();
        subGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(mainGroup, subGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerInitializer());
    }

    public void start() {
        try {

            BannerUtil.loadBanner("banner.txt");
            Class.forName("com.singularityfold.config.Config"); // 初始化配置

            Channel channel = server.bind(8888).sync().channel();
            log.info("Server starts successfully!");
            channel.closeFuture().sync();

        } catch (Exception e) {
            log.error("server error", e);
            e.printStackTrace();
        } finally {
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }
}

