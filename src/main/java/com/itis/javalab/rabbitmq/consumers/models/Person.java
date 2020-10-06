package com.itis.javalab.rabbitmq.consumers.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Person {
    private String name;
    private String patronymic;
    private String surname;
    private String passport;
    private String mail;

    private Date date;
}
