package com.singularityfold.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 定义消息协议格式
 * 1. 来自consumer的订阅注册消息，消息格式为：
 *  <pre><code>
 *      {
 *          type: 0,
 *          extend: ["queue_name1","queue_name1"]  // 订阅的队列名称
 *      }
 *  </code></pre>
 * 2. 来自producer的普通消息，消息格式为：
 *  <pre><code>
 *      {
 *          type: 1,
 *          content: "消息内容",
 *          extend: "com.xhades.top"  // routingKey
 *      }
 *  </code></pre>

 * @author Mr_Hades
 * @date 2022-03-25 21:53
 */
@Data
@Builder
@AllArgsConstructor
public class Message {

    /*
     消息类型，分为以下几种
        0: 来自consumer的订阅注册消息
        1: 来自producer的普通消息
     */
    private int type;

    /*
        消息正文，可以为null
     */
    private String content;

    /*
        若type为0，则extend为来自consumer的queueName，指定与哪个queue相连接
        若type为1，则extend为来自producer的routingKey
     */
    private String extend;

    /*
        消息发送的时间
     */
    private String datetime;

}
