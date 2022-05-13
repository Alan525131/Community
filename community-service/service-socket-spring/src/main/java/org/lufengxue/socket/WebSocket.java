package org.lufengxue.socket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : Allen
 * @date : 2022/05/12 16:45
 * @desc : 实时通信类
 */
@Slf4j
@Component
@ServerEndpoint("/ws/{user}")
public class WebSocket {



    /**
     * concurrent包的线程安全Map，用来存放每个客户端对应的MyWebSocket对象。
     */
    public static ConcurrentHashMap<String, Session> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 客户端user
     */
    private String user;

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("user") String user) throws IOException {
        log.info("user {} 建立连接, 当前在线人数为:{}", user, webSocketMap.size());
        this.user = user;
        this.session = session;
        webSocketMap.put(user, session);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("user") String user) {
        log.info("user {} 断开连接, 当前在线人数为:{}", user, webSocketMap.size());
        webSocketMap.remove(user);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.debug("收到客户端消息: {}", message);
//        session.getBasicRemote().sendText("大家好");
    }


    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("webSocket发生错误！user：{}", user, error);
    }

    /**
     * 向当前客户端发送消息
     *
     * @param message 字符串消息
     */
    @SneakyThrows
    public void sendMessage(String message) {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     */
    @SneakyThrows
    public static void sendAll(String message) {
        for (Session session : webSocketMap.values()) {
            //表示同步发送
            session.getBasicRemote().sendText("message");
            //表示异步发送
            //this.session.getAsyncRemote().sendText(message);
        }
    }
}
