package com.itis.javalab.rabbitmq.consumers.configs;

import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@PropertySource({"classpath:application.properties", "classpath:application-${spring.profiles.active}.properties"})
public class ApplicationContext {
    private final Environment environment;

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(){
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("classpath:/templates"); //defines the classpath location of the freemarker templates
        freeMarkerConfigurer.setDefaultEncoding("UTF-8"); // Default encoding of the template files
        return freeMarkerConfigurer;
    }

    @Bean
    public freemarker.template.Configuration configuration(){
        return freeMarkerConfigurer().getConfiguration();
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

}
