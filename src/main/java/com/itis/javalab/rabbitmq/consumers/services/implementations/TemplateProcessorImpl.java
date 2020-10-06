package com.itis.javalab.rabbitmq.consumers.services.implementations;

import com.itis.javalab.rabbitmq.consumers.services.interfaces.TemplateProcessor;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Map;


@Service
public class TemplateProcessorImpl implements TemplateProcessor {
    private final Configuration configuration;

    public TemplateProcessorImpl(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getStringFromTemplateByNameAndParameters(String name, Map<String, Object> parameters) {
        try {
            Template template = configuration.getTemplate(name);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, parameters);
        } catch (IOException | TemplateException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
