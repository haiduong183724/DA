package com.example.doormanage.websocket;

import com.example.doormanage.utils.URLQueryUtils;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

public class ClientSocketHandshakeInterceptor implements HandshakeInterceptor {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        URI uri = request.getURI();
        Map<String, String> params = URLQueryUtils.getParams(uri);
        String module = params.get("module");
        if(Strings.isNullOrEmpty(module)){
            logger.info("param module is empty");
            module = "";
        }
        map.put("module", module);
        logger.info("connection websocket uri: {}", uri);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }

}
