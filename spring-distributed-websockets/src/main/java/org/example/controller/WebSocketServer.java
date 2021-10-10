package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ServerEndpoint("/chat/groups/{gid}")
public class WebSocketServer {

    private String gid;
    private Session session;
    private final static ConcurrentHashMap<String, List<Session>> groupCache = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("gid") String gid) {
        this.gid = gid;
        this.session = session;
        if (groupCache.containsKey(gid)) {
            groupCache.get(gid).add(this.session);
        } else {
            List<Session> sessions = new ArrayList<>();
            sessions.add(this.session);
            groupCache.put(gid, sessions);
        }
        log.info("open connection: {}-{}, current online: {}", gid, session, groupCache.get(gid).size());
    }

    @OnClose
    public void onClose() {
        List<Session> sessions = groupCache.get(this.gid);
        if (!CollectionUtils.isEmpty(sessions)) {
            sessions.remove(this.session);
        }
        log.info("close connection, current online: {}", groupCache.get(gid).size());
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
        groupCache.get(gid).stream().filter(s -> s != session).forEach(s -> {
            try {
                RemoteEndpoint.Basic remote = s.getBasicRemote();
                remote.sendText(message);
            } catch (IOException e) {
                log.error("send message failed, {}", e.getMessage());
            }
        });
    }
}
