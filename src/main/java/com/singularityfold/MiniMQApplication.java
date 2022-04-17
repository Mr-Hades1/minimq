package com.singularityfold;

import com.singularityfold.netIO.MQServer;

/**
 * @author Mr_Hades
 * @date 2022-03-26 13:57
 */
public class MiniMQApplication {
    public static void main(String[] args) {
        // 启动服务器
        MQServer.getInstance().start();
    }
}
