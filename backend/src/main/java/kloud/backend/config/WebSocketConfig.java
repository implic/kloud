package kloud.backend.config;

import kloud.backend.controller.ws.SpringWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SpringWebSocketHandler(), "/api/pod/shell")
                .setAllowedOrigins()
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*"); // 添加允许跨域访问

    }

    @Bean
    public TextWebSocketHandler webSocketHandler() {
        return new SpringWebSocketHandler();
    }

}