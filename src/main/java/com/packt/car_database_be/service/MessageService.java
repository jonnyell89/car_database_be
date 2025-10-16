package com.packt.car_database_be.service;

import java.util.ArrayList;
import java.util.List;

public class MessageService {

    private List<String> messages = new ArrayList<>();

    public MessageService() {};

    public List<String> getMessages() {
        return messages;
    }

    public String getMessage(int index) {
        if (index > messages.size()) throw new IndexOutOfBoundsException();
        return messages.get(index);
    }

    public void addMessage(String message) {
        messages.add(message);
    }
}
