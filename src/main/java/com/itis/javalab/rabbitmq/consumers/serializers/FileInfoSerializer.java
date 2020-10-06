package com.itis.javalab.rabbitmq.consumers.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.itis.javalab.rabbitmq.consumers.dtos.FileInfo;
import org.bouncycastle.jce.provider.PKIXCertPathBuilderSpi;

import java.io.IOException;

public class FileInfoSerializer extends StdSerializer<FileInfo> {

    public FileInfoSerializer() {
        this(null);
    }

    public FileInfoSerializer(Class<FileInfo> t) {
        super(t);
    }

    @Override
    public void serialize(FileInfo fileInfo, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("mail", fileInfo.getMail());
        jsonGenerator.writeStringField("path", fileInfo.getFilePath());
        jsonGenerator.writeStringField("name", fileInfo.getName());
        jsonGenerator.writeNumberField("size", fileInfo.getSize());
        jsonGenerator.writeEndObject();
    }
}