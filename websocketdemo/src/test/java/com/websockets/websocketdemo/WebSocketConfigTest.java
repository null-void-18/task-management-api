package com.websockets.websocketdemo;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

@SpringBootTest
class WebSocketConfigTest {

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Test
    void testRegisterStompEndpoints() {
        // Mock the StompEndpointRegistry
        StompEndpointRegistry registry = mock(StompEndpointRegistry.class);
        var endpointRegistration = mock(StompWebSocketEndpointRegistration.class);

        when(registry.addEndpoint("/ws")).thenReturn(endpointRegistration);
        when(endpointRegistration.setAllowedOriginPatterns("*")).thenReturn(endpointRegistration);

        // Call the method
        webSocketConfig.registerStompEndpoints(registry);

        // Verify the interactions
        verify(registry).addEndpoint("/ws");
        verify(endpointRegistration).setAllowedOriginPatterns("*");
        verify(endpointRegistration).withSockJS();
    }

    @Test
    void testConfigureMessageBroker() {
        // Mock the MessageBrokerRegistry
        MessageBrokerRegistry registry = mock(MessageBrokerRegistry.class);

        // Call the method
        webSocketConfig.configureMessageBroker(registry);

        // Verify the interactions
        verify(registry).enableSimpleBroker("/topic");
        verify(registry).setApplicationDestinationPrefixes("/app");
    }
}