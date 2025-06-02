package com.websockets.websocketdemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ChatMessageTest {

    @Test
    void testConstructorAndGetters() {
        // Create a ChatMessage object using the constructor
        ChatMessage message = new ChatMessage("Alice", "Hello, World!", "2025-06-02T10:00:00");

        // Verify the values using getters
        assertEquals("Alice", message.getSender());
        assertEquals("Hello, World!", message.getContent());
        assertEquals("2025-06-02T10:00:00", message.getTimestamp());
    }

    @Test
    void testSetters() {
        // Create a ChatMessage object using the default constructor
        ChatMessage message = new ChatMessage();

        // Set values using setters
        message.setSender("Bob");
        message.setContent("Hi there!");
        message.setTimestamp("2025-06-02T11:00:00");

        // Verify the values using getters
        assertEquals("Bob", message.getSender());
        assertEquals("Hi there!", message.getContent());
        assertEquals("2025-06-02T11:00:00", message.getTimestamp());
    }
}