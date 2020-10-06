package com.itis.javalab.rabbitmq.consumers.services.interfaces;

import java.util.Map;

public interface TemplateProcessor {
    String getStringFromTemplateByNameAndParameters(String name, Map<String, Object> parameters);
}
