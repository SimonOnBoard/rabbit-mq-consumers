package com.itis.javalab.rabbitmq.consumers.services.implementations;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itis.javalab.rabbitmq.consumers.models.Person;
import com.itis.javalab.rabbitmq.consumers.services.interfaces.FileNameCreator;
import com.itis.javalab.rabbitmq.consumers.services.interfaces.PdfCreatorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.itis.javalab.rabbitmq.consumers.utils.CreationService.getCRLFTextObject;
import static com.itis.javalab.rabbitmq.consumers.utils.CreationService.getParagraphByTextAligmentAndMargin;

@Service
@Profile("dismiss")
public class DismissPdfCreatorImpl implements PdfCreatorService {
    private final String prefix = "dismiss";
    private PdfFont font;
    private final FileNameCreator fileNameCreator;
    private final String basicFileAddress;
    private final List<String> startPartOfDismissDocument;
    private final List<String> mainPartOfDismissDocument;

    public DismissPdfCreatorImpl(FileNameCreator fileNameCreator,
                                 String basicFileAddress,
                                 @Qualifier("startPartOfDismissDocument") List<String> startPartOfDismissDocument,
                                 @Qualifier("mainPartOfDismissDocument") List<String> mainPartOfDismissDocument) {
        this.fileNameCreator = fileNameCreator;
        this.basicFileAddress = basicFileAddress;
        this.startPartOfDismissDocument = startPartOfDismissDocument;
        this.mainPartOfDismissDocument = mainPartOfDismissDocument;
    }


    @Override
    public void createPfd(Person person) throws IOException {
        font = PdfFontFactory.createFont(
                "src/main/resources/static/font.ttf", "CP1251", true);

        String resultFilePath = basicFileAddress + File.separator + fileNameCreator.getFileName(person, prefix);

        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(resultFilePath));
        Document document = new Document(pdfDocument);
        document.setFont(font);
        addStart(document, person);
        addMainPart(document, person);
        document.close();
    }

    private void addMainPart(Document document, Person person) {
        Paragraph emptyLineParagraph = getParagraphByTextAligmentAndMargin("", TextAlignment.CENTER, 0);
        for (int i = 0; i < 3; i++) {
            emptyLineParagraph.add(getCRLFTextObject());
        }
        document.add(emptyLineParagraph);

        Paragraph mainPartFirstParagraph = getParagraphByTextAligmentAndMargin("", TextAlignment.CENTER, 0).setMultipliedLeading(1.0f);
        mainPartFirstParagraph.add(new Text(mainPartOfDismissDocument.get(0)).setFontSize(15)).add(getCRLFTextObject())
                .add(new Text(mainPartOfDismissDocument.get(1)).setFontSize(12)).add(getCRLFTextObject());
        document.add(mainPartFirstParagraph);

        Paragraph mainPartSecondParagraph = getParagraphByTextAligmentAndMargin("", TextAlignment.CENTER, 0).setFontSize(12);
        mainPartSecondParagraph.add("Я " + person.getSurname() + " " + person.getName() + " "
                + person.getPatronymic() + " пасспорт: " + person.getPassport()).add(getCRLFTextObject());
        mainPartSecondParagraph.add("выдан " + getDateRightFormat(person.getDate()) + " в соотсветствии со статьёй 80 Трудового кодекса").add(getCRLFTextObject());
        mainPartSecondParagraph.add("Российской Федерации прошу уволить меня по собственному желанию " + getNowDateRightFormat());

        document.add(mainPartSecondParagraph);
    }

    private String getNowDateRightFormat() {
        return null;
    }

    private String getDateRightFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }

    private void addStart(Document document, Person person) {
        Paragraph paragraph = getParagraphByTextAligmentAndMargin("", TextAlignment.LEFT, 300);
        for (int i = 0; i < startPartOfDismissDocument.size(); i++) {
            paragraph.add(startPartOfDismissDocument.get(i)).add(getCRLFTextObject());
        }
        paragraph.add("от " + person.getSurname() + " " + person.getName()).add(getCRLFTextObject()).add(person.getPatronymic());
        document.add(paragraph);
    }

}
