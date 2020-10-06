package com.itis.javalab.rabbitmq.consumers.services.interfaces;

public interface ExchangeSenderService {
    void sendMessage(String filePath, String mail);
}