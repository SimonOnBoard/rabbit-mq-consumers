package com.itis.javalab.rabbitmq.consumers.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itis.javalab.rabbitmq.consumers.models.Person;
import com.itis.javalab.rabbitmq.consumers.services.interfaces.BasicConsumer;
import com.itis.javalab.rabbitmq.consumers.services.interfaces.PdfCreatorService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class BasicConsumerImpl implements BasicConsumer {
    private final ConnectionFactory connectionFactory;
    private final String exchangeName;
    private final String exchangeType;
    private Connection connection;
    private final PdfCreatorService pdfCreatorService;
    private final ObjectMapper objectMapper;


    @PostConstruct
    private void init(){
        try {
            connection = connectionFactory.newConnection();
            consume();
        } catch (IOException | TimeoutException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void consume() throws IOException {
        Channel channel = connection.createChannel();
        channel.basicQos(3);
        channel.exchangeDeclare(exchangeName, exchangeType);
        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, exchangeName, "");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            pdfCreatorService.createPfd(objectMapper.readValue(message, Person.class));
        };
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });
    }
}
