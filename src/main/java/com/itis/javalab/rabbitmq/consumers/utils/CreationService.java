package com.itis.javalab.rabbitmq.consumers.utils;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

public class CreationService {
    public static Text getCRLFTextObject(){
        return new Text("\n");
    }

    public static Paragraph getParagraphByTextAligmentAndMargin(Text text, TextAlignment alignment, int size) {
        return new Paragraph(text).setTextAlignment(alignment).setMarginLeft(size);
    }

    public static Paragraph getParagraphByTextAligmentAndMargin(String text, TextAlignment alignment, int size) {
        return getParagraphByTextAligmentAndMargin(new Text(text), alignment, size);
    }
}
