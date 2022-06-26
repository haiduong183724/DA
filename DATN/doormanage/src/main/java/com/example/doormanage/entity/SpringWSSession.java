package com.example.doormanage.entity;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class SpringWSSession implements ISession {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final WebSocketSession client;
    private final String id;
    private boolean ssClosed = false;
    private long lastTime;

    public SpringWSSession(WebSocketSession client) {
        this.client = client;
        this.id = client.getId();
        this.lastTime = System.currentTimeMillis();
    }

    public String getId() {
        return this.id;
    }


    @Override
    public boolean send(Object data) {
        // send từ Server -> Client
        // Client nhận msg, cập nhật lastTime để giữ kết nối.
        // -> Giờ làm tiếp phần Client send lên thì cũng phải giữ kết nối.
        logger.info("send: {}", data);
        if (client != null && client.isOpen() && !ssClosed) {
            try {
                String text = (String) data;
                client.sendMessage(new TextMessage(text));
                return true;
            } catch (Exception ex) {
                return false;
            }
        }

        try {
            if (client != null) {
                client.close();
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    @Override
    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    public long getLastTime() {
        return this.lastTime;
    }

    @Override
    public synchronized void disconnect() {
        if (this.ssClosed) {
            return;
        }
        this.ssClosed = true;
        if (client != null) {
            try {
                client.close();
            } catch (Throwable e) {
                logger.error("Ex: ", e);
            }
        }
    }

    @Override
    public void disconnect(Integer code, String message) {
        if (this.ssClosed || code == null || Strings.isNullOrEmpty(message)) {
            return;
        }
        this.ssClosed = true;
        if (client != null) {
            try {
                client.close(new CloseStatus(code, message));
            } catch (Throwable e) {
                logger.error("Ex: ", e);
            }
        }
    }

    @Override
    public void close() {
        disconnect();
    }

    @Override
    public void close(Integer code, String message) {
        disconnect(code, message);
    }

    @Override
    public boolean isOpen() {
        return !ssClosed && client != null && client.isOpen();
    }

}
