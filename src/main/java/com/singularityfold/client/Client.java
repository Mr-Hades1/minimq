package com.singularityfold.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.function.Consumer;

/**
 * java的WebSocketClient可以自动发送心跳包ping，
 * 而netty的WebSocketServerProtocolHandler可以自动发送心跳包pong
 * @author Mr_Hades
 * @date 2022-03-27 11:05
 */
public class Client extends WebSocketClient {

    // client的名称
    private String name;

    // client 类型，0代表consumer，1代表producer
    private int type;

    // 供外部类用来处理接受消息的接口函数
    private Consumer<String> onMessageAction;

    // 同一个包内访问
    Client(URI serverUri, String name, int type) {
        super(serverUri);
        this.name = name;
        this.type = type;
    }

    public void setOnMessageAction(Consumer<String> onMessageAction) {
        this.onMessageAction = onMessageAction;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Client " + name + " connects successfully!");
    }

    @Override
    public void onMessage(String message) {
//        System.out.println("Client " + name + " received message: " + message);
        if (onMessageAction != null)
            onMessageAction.accept(message);

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed. code: " + code + ", reason: " + reason + ", remote: " + remote);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Connection error: " + ex.getMessage());
    }


}
