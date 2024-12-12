package com.example.ticketing.config;

import com.example.ticketing.handler.TicketWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(ticketWebSocketHandler(), "/ticket-status")
                .setAllowedOrigins("*"); // Allow all origins during development
    }

    @Bean
    public TicketWebSocketHandler ticketWebSocketHandler() {
        return new TicketWebSocketHandler();
    }
}