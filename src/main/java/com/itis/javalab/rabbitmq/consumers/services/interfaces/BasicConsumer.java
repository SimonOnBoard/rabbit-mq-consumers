package com.itis.javalab.rabbitmq.consumers.services.interfaces;

import java.io.IOException;

public interface BasicConsumer {
    void consume() throws IOException;
}
