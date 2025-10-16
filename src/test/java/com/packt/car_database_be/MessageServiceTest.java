package com.packt.car_database_be;

import com.packt.car_database_be.service.MessageService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageServiceTest {

    @Test
    public void testAddMessage() {

        MessageService messageService = new MessageService();
        String message = "Hello, world!";
        messageService.addMessage(message);
        assertEquals(message, messageService.getMessage(0));
    }
}
