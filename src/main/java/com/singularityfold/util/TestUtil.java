package com.singularityfold.util;

import com.singularityfold.netIO.MessageHandler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Mr_Hades
 * @date 2022-04-17 10:48
 */
public class TestUtil {
    public static void getUsersPerSecond() {
        // 设置定时器输出每秒客户端的数量
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int size = MessageHandler.clients.size();
                if (size != 0)
                    System.out.println("current channel number: " + size);
            }
        };
        timer.schedule(timerTask, 1000L, 1000L);
    }
}
