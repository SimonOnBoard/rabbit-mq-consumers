package com.itis.javalab.rabbitmq.consumers.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itis.javalab.rabbitmq.consumers.dtos.FileInfo;
import com.itis.javalab.rabbitmq.consumers.services.interfaces.ExchangeSenderService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@Service
public class ExchangeSenderServiceImpl implements ExchangeSenderService {
    private final ConnectionFactory connectionFactory;
    private final String exchangeName;
    private final String exchangeType;
    private Connection connection;
    private final ObjectMapper objectMapper;
    private ClassLoader classLoader;

    public ExchangeSenderServiceImpl(ConnectionFactory connectionFactory,
                                     @Value("${past.exchange.name}") String exchangeName,
                                     @Value("${past.exchange.type}") String exchangeType, ObjectMapper objectMapper) {
        this.connectionFactory = connectionFactory;
        this.exchangeName = exchangeName;
        this.exchangeType = exchangeType;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void setNewConnection() {
        try {
            classLoader = getClass().getClassLoader();
            connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, exchangeType);
        } catch (IOException | TimeoutException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void sendMessage(String filePath, String mail) {
        try (Channel channel = connection.createChannel()) {
            String data = getFileInfo(filePath, mail);
            channel.basicPublish(exchangeName, "", null, data.getBytes());
        } catch (TimeoutException | IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private String getFileInfo(String filePath, String mail) throws JsonProcessingException {
        File file = new File(filePath);
        if (!file.exists()) throw new IllegalStateException("File not exists");
        FileInfo fileInfo = FileInfo.builder().filePath(filePath).name(file.getName())
                .size(file.getTotalSpace())
                .mail(mail).build();
        return objectMapper.writeValueAsString(fileInfo);
    }
}