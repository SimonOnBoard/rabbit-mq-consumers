package com.itis.javalab.rabbitmq.consumers.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itis.javalab.rabbitmq.consumers.models.Person;
import com.itis.javalab.rabbitmq.consumers.services.interfaces.BasicConsumer;
import com.itis.javalab.rabbitmq.consumers.services.interfaces.ExchangeSenderService;
import com.itis.javalab.rabbitmq.consumers.services.interfaces.PdfCreatorService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class BasicConsumerImpl implements BasicConsumer {
    private final ConnectionFactory connectionFactory;
    private final PdfCreatorService pdfCreatorService;

    private final String exchangeName;

    private final String routingKey;


    private final ObjectMapper objectMapper;
    private final ExchangeSenderService exchangeSenderService;
    private Connection connection;

    public BasicConsumerImpl(ConnectionFactory connectionFactory,
                             PdfCreatorService pdfCreatorService,
                             @Value("${exchange.name}") String exchangeName,
                             @Value("${topic.key}") String routingKey,
                             ObjectMapper objectMapper,
                             ExchangeSenderService exchangeSenderService) {
        this.connectionFactory = connectionFactory;
        this.pdfCreatorService = pdfCreatorService;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.objectMapper = objectMapper;
        this.exchangeSenderService = exchangeSenderService;
    }


    @PostConstruct
    private void init() {
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
        channel.basicQos(1);
        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, exchangeName, routingKey);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'" + "   " + consumerTag + "   " + delivery.getEnvelope().getRoutingKey());
            System.out.println(delivery.getEnvelope().toString());
            Person person = objectMapper.readValue(message, Person.class);
            String resultPath = pdfCreatorService.createPfd(person);
            exchangeSenderService.sendMessage(resultPath, person.getMail());
        };
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> {
        });
    }
}
