package com.example.doormanage.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    private final WebSocketHandler gameSocketHandler;

    public WebsocketConfig(WebSocketHandler gameSocketHandler) {
        this.gameSocketHandler = gameSocketHandler;
    }
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(gameSocketHandler, "/user").setAllowedOrigins("*").addInterceptors(new ClientSocketHandshakeInterceptor());
    }
}