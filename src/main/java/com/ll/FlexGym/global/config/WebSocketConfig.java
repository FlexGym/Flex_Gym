package com.ll.FlexGym.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // 메시지브로커를 등록하는 코드
        config.setApplicationDestinationPrefixes("/app"); // 도착 경로에 대한 prefix를 설정
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")   //SockJS 연결 주소
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("http://localhost:8080")
                .setAllowedOrigins("https://www.colikeprdo.work")// spring stomp CORS 설정하기
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                        Principal principal = request.getPrincipal();
                        if (principal != null) {
                            // WebSocket 세션에 인증 정보를 넣어둡니다.
                            attributes.put("PRINCIPAL", principal);
                        }
                        return principal;
                    }
                })
                .withSockJS()
                .setInterceptors(new HttpSessionHandshakeInterceptor());
        // 주소 : ws://localhost:8080/ws
    }
}