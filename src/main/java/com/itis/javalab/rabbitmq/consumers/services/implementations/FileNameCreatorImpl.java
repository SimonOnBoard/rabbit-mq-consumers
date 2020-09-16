package com.itis.javalab.rabbitmq.consumers.services.implementations;

import com.itis.javalab.rabbitmq.consumers.models.Person;
import com.itis.javalab.rabbitmq.consumers.services.interfaces.FileNameCreator;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class FileNameCreatorImpl implements FileNameCreator {
    @Override
    public String getFileName(Person person, String prefix) {
        String fileName = "";
        fileName += prefix + "_" + Instant.now().toEpochMilli() + "_" + person.getName() + "_" + person.getSurname() + "_" + person.getPassport() + ".pdf";
        return fileName;
    }
}
