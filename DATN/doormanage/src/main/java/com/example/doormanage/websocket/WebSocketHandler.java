package com.example.doormanage.websocket;

import com.example.doormanage.entity.ISession;
import com.example.doormanage.entity.SpringWSSession;
import com.example.doormanage.manage.SessionManage;
import com.google.common.base.Strings;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException, JSONException {

        String payload = message.getPayload();
        logger.info(payload);
        session.sendMessage(new TextMessage("Hi " + payload + " how may we help you?"));
    }
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object > map = session.getAttributes();
        String module = (String)map.get("module");
        if(Strings.isNullOrEmpty(module)){
            logger.error("module is not set, disconnect this connection ");
            session.close(new CloseStatus(4014 , "module is not set, disconnect this connection" ));
            return;
        }
        else {
            if(module.equals("android")|| module.equals("ai_module"))
            logger.info("Get module" + module);
            ISession client = new SpringWSSession(session);
            SessionManage.addSession("", client, module);
        }
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Connection Close");
        super.afterConnectionClosed(session, status);
        SessionManage.closeSession(session.getId(), 0, "" );
    }
}