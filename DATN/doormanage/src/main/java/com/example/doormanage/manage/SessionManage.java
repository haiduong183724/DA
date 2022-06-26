package com.example.doormanage.manage;

import com.example.doormanage.entity.ISession;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class SessionManage {

    /**
     * 20s
     */
    private static final int timeOut = 20000;

    private static final Logger logger = LoggerFactory.getLogger(SessionManage.class);

    /**
     * Key = userId
     */
    private static final ConcurrentHashMap<String, List<ISession>> userSessionMap = new ConcurrentHashMap<>();
    /**
     * Key = walletId
     */
    /**
     * Key = sessionId
     */
    private static final ConcurrentHashMap<String, ISession> SESSIONS = new ConcurrentHashMap<>();

    private static final ReentrantLock LOCK = new ReentrantLock(true);
    private static final ReentrantLock LOCK_WALLET = new ReentrantLock(true);

    private SessionManage() {

    }

    public boolean checkUserOnline(ISession session) {
        return SESSIONS.contains(session);
    }

    public static List<String> getConnectedUserIds() {

        List<String> userIds = new ArrayList<>();
        for (Map.Entry<String, List<ISession>> entry : userSessionMap.entrySet()) {
            userIds.add(entry.getKey());
        }
        return userIds;
    }

    public static ISession getISessionBySessionId(String sessionId) {
        if (Strings.isNullOrEmpty(sessionId)) return null;

        for (Map.Entry<String, List<ISession>> entry : userSessionMap.entrySet()) {
            for (ISession iSession : entry.getValue()) {
                if (sessionId.equals(iSession.getId())) {
                    return iSession;
                }
            }
        }

        return null;
    }


    public static int sendToSession(String sessionId, String msg){

        if (Strings.isNullOrEmpty(sessionId)){
            return -1;
        }

        ISession iSession = SESSIONS.get(sessionId);
        if (iSession == null){
            return -1;
        }

        int count = 0;
        if (iSession.send(msg)){
            count++;
        }

        return count;

    }

    public static void sendAll(String msg) {
        if (SESSIONS.isEmpty()) {
            logger.info("no client to send message");
            return;
        }
        long count = 0;
        for (ISession session : SESSIONS.values()) {
            boolean sent = session.send(msg);
            if (sent) {
                count++;
            }
        }
        logger.debug("Total sent: {}", count);
    }

    public static void addSession(String userId, ISession session, String type ) {
        if(type.equals("android")) {
            SESSIONS.put(session.getId(), session);
        }
        if (!Strings.isNullOrEmpty(userId)) {
            LOCK.lock();
            try {
                _addSessionToMapConn(userSessionMap, userId, session);
            } finally {
                LOCK.unlock();
            }
        }
    }

    private static void _addSessionToMapConn(Map<String, List<ISession>> mapConn, String key, ISession session) {
        List<ISession> sessions = mapConn.get(key);
        if (sessions == null) {
            sessions = new ArrayList<>();
            sessions.add(session);
            mapConn.put(key, sessions);
            return;
        }
        sessions.add(session);
    }


    private static void _removeClosedConnection(Map<String, List<ISession>> mapConn, final Set<String> removed) {
        for (Map.Entry<String, List<ISession>> entry : mapConn.entrySet()) {
            List<ISession> sessions = entry.getValue();
            if (sessions == null || sessions.isEmpty()) {
                mapConn.remove(entry.getKey());
                continue;
            }
            sessions.removeIf(s -> removed.contains(s.getId()));
            if (sessions.isEmpty()){
                mapConn.remove(entry.getKey());
            }
        }
    }

    private static void _removeClosedConnection(Map<String, List<ISession>> mapConn, final String removed) {
        for (Map.Entry<String, List<ISession>> entry : mapConn.entrySet()) {
            List<ISession> sessions = entry.getValue();
            if (sessions == null || sessions.isEmpty()) {
                mapConn.remove(entry.getKey());
                continue;
            }
            sessions.removeIf(s -> removed.equals(s.getId()));
            if (sessions.isEmpty()){
                mapConn.remove(entry.getKey());
            }
        }
    }

    public static void closeSession(String id, Integer code, String message) {
        if (Strings.isNullOrEmpty(id) || code == null || Strings.isNullOrEmpty(message)) {
            return;
        }

        ISession session;

        LOCK.lock();
        try {
            session = SESSIONS.remove(id);
            logger.info("remove session id: " + id + " successfully!");
            _removeClosedConnection(userSessionMap, id);
        } finally {
            LOCK.unlock();
        }
        if (session != null) {
            session.close(code, message);
        }

    }
}
