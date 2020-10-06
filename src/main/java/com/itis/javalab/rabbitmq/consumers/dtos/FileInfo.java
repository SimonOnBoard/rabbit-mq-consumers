package com.itis.javalab.rabbitmq.consumers.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.itis.javalab.rabbitmq.consumers.serializers.FileInfoSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.plaf.BorderUIResource;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonSerialize(using = FileInfoSerializer.class)
public class FileInfo {
    private String filePath;
    private String name;
    private Long size;
    private String mail;
}
