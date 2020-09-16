package com.itis.javalab.rabbitmq.consumers.configs;

import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationContext {
    private final Environment environment;

    @Bean
    public String exchangeName() {
        return environment.getProperty("exchange.name");
    }

    @Bean
    public String exchangeType() {
        return environment.getProperty("exchange.type");
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        return connectionFactory;
    }

    @Bean
    public BufferedReader scanner() {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    @Bean
    public DateFormat dateFormat() {
        return new SimpleDateFormat("dd.MM.yyyy");
    }

    @Bean(name = "basicFileAddress")
    public String basicFileAddress() {
        return environment.getProperty("file.address");
    }

    @Bean(name = "firstPartOfAcademicLeaveText")
    public List<String> firstPartOfAcademicLeaveText() {
        List<String> list = new ArrayList<>();
        list.add("Ректору ФГАОУ ВО");
        list.add("«Казанский (Приволжский)");
        list.add("федеральный университет»");
        list.add("И.Р. Гафурову");
        return list;
    }

    @Bean(name = "secondPartOfAcademicLeaveText")
    public List<String> secondPartOfAcademicLeaveText() {
        List<String> list = new ArrayList<>();
        list.add("(ФИО студента, курс, форма обучения, ");
        list.add("специализация/ направление, факультет/институт,");
        list.add("образовательная организация)");
        list.add("(адрес, телефон, адрес электронной почты)");
        return list;
    }

    @Bean(name = "mainPartOfAcademicLeaveText")
    public List<String> mainPartOfAcademicLeaveText(){
        List<String> strings = new ArrayList<>();
        strings.add("Заявление");
        strings.add("Прошу Вас предоставить мне академический  отпуск по");
        strings.add("(семейным обстоятельствам  (по состоянию здоровья  и  другим  исключительным случаям))");
        return strings;
    }

    @Bean(name = "signatureAcademicLeaveText")
    public List<String> signatureAcademicLeaveText(){
        List<String> strings = new ArrayList<>();
        strings.add("(подпись)");
        strings.add("(ФИО)");
        strings.add("(дата)");
        return strings;
    }

    @Bean(name = "startPartOfDismissDocument")
    public List<String> startPartOfDismiss(){
        List<String> strings = new ArrayList<>();
        strings.add("Генеральному директору");
        strings.add("ООО \"Светлое будущее\" ");
        strings.add("Петрову П.П");
        return strings;
    }

    @Bean(name = "mainPartOfDismissDocument")
    public List<String> mainPartOfDissmiss(){
        List<String> strings = new ArrayList<>();
        strings.add("Заявление");
        strings.add("об увольнении по собственному желанию");
        return strings;
    }
}
