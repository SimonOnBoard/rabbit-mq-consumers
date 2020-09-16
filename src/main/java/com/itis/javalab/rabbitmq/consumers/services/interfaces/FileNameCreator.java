package com.itis.javalab.rabbitmq.consumers.services.interfaces;

import com.itis.javalab.rabbitmq.consumers.models.Person;

public interface FileNameCreator {
    String getFileName(Person person, String prefix);
}
