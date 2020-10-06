package com.itis.javalab.rabbitmq.consumers.services.interfaces;

import com.itis.javalab.rabbitmq.consumers.models.Person;

import java.io.IOException;

public interface PdfCreatorService {
    String createPfd(Person person) throws IOException;
}
