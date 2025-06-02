package com.websockets.websocketdemo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ChatControllerTest {

    private final ChatController chatController = new ChatController();

    @Test
    void testSend() throws Exception {
        // Create a sample ChatMessage
        ChatMessage inputMessage = new ChatMessage("Alice", "Hello, World!", null);

        // Call the send method
        ChatMessage outputMessage = chatController.send(inputMessage);

        // Verify the sender and content remain unchanged
        assertEquals("Alice", outputMessage.getSender());
        assertEquals("Hello, World!", outputMessage.getContent());

        // Verify the timestamp is set correctly (non-null and formatted)
        String expectedFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        assertEquals(expectedFormat, outputMessage.getTimestamp());
    }
}