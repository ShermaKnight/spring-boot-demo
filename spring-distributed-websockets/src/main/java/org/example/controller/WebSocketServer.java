package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ServerEndpoint("/api/websocket/{sid}")
public class WebSocketServer {

    private String sid;
    private Session session;
    private CopyOnWriteArraySet<WebSocketServer> sets = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.sid = sid;
        this.session = session;
        sets.add(this);
        log.info("open connection: {}, current online: {}", sid, sets.size());
    }

    @OnClose
    public void onClose() {
        sets.remove(this);
        log.info("close connection, current online: {}", sets.size());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        this.session = session;
        log.error("connection error");
        throwable.printStackTrace();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        this.session = session;
        log.info("receive message: {}", message);
        try {
            RemoteEndpoint.Basic remote = this.session.getBasicRemote();
            for (int i = 1; i <= 10; i++) {
                TimeUnit.SECONDS.sleep(i);
                remote.sendText("处理程序" + (i * 10) + "%");
            }
        } catch (IOException | InterruptedException e) {
            log.error("send message failed, {}", e.getMessage());
        }
    }
}
