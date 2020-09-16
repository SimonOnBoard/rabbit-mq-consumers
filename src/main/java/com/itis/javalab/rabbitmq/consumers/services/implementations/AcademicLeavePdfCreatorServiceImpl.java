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
import java.util.List;

import static com.itis.javalab.rabbitmq.consumers.utils.CreationService.getCRLFTextObject;
import static com.itis.javalab.rabbitmq.consumers.utils.CreationService.getParagraphByTextAligmentAndMargin;

@Service
@Profile("academ")
public class AcademicLeavePdfCreatorServiceImpl implements PdfCreatorService {
    private final String prefix = "academic_leave";

    public AcademicLeavePdfCreatorServiceImpl(FileNameCreator fileNameCreator,
                                              String basicFileAddress,
                                              @Qualifier("firstPartOfAcademicLeaveText") List<String> firstPartOfAcademicLeaveText,
                                              @Qualifier("secondPartOfAcademicLeaveText") List<String> secondPartOfAcademicLeaveText,
                                              @Qualifier("mainPartOfAcademicLeaveText") List<String> mainPartStrings,
                                              @Qualifier("signatureAcademicLeaveText") List<String> signatureAcademicLeaveText) {
        this.fileNameCreator = fileNameCreator;
        this.basicFileAddress = basicFileAddress;
        this.firstPartOfAcademicLeaveText = firstPartOfAcademicLeaveText;
        this.secondPartOfAcademicLeaveText = secondPartOfAcademicLeaveText;
        this.mainPartStrings = mainPartStrings;
        this.signatureAcademicLeaveText = signatureAcademicLeaveText;
    }

    private PdfFont font;
    private final FileNameCreator fileNameCreator;
    private final String basicFileAddress;
    private final List<String> firstPartOfAcademicLeaveText;
    private final List<String> secondPartOfAcademicLeaveText;
    private final List<String> mainPartStrings;
    private final List<String> signatureAcademicLeaveText;

    @Override
    public void createPfd(Person person) throws IOException {
        font = PdfFontFactory.createFont(
                "src/main/resources/static/font.ttf", "CP1251", true);

        String resultFilePath = basicFileAddress + File.separator + fileNameCreator.getFileName(person, prefix);

        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(resultFilePath));
        Document document = new Document(pdfDocument);
        document.setFont(font);
        addStart(document, person);
        addMainPart(document);
        addSignature(document);
        document.close();
    }

    private void addSignature(Document document) {
        Paragraph emptyLinesParagraph = getParagraphByTextAligmentAndMargin("", TextAlignment.CENTER, 0);
        for(int i = 0; i < 10; i++){
            emptyLinesParagraph.add(getCRLFTextObject());
        }
        document.add(emptyLinesParagraph);

        Paragraph signature = getParagraphByTextAligmentAndMargin("", TextAlignment.CENTER, 0);
        for (int i = 0; i < signatureAcademicLeaveText.size(); i++) {
            signature.add(new Text("                   " + signatureAcademicLeaveText.get(i) + "                     ").setTextRise(-10).setUnderline().setFontSize(8))
                    .add(new Text("                     "));
        }
        document.add(signature);
    }

    private void addMainPart(Document document) {
        document.add(getParagraphByTextAligmentAndMargin(mainPartStrings.get(0), TextAlignment.CENTER, 0).setFontSize(12));
        document.add(getParagraphByTextAligmentAndMargin(mainPartStrings.get(1), TextAlignment.LEFT, 0).setMarginLeft(10));
        Text emptyStringUnderlined = new Text("________________________________________________________________________________________________________________________________");
        document.add(new Paragraph(emptyStringUnderlined).setMultipliedLeading(1.0f).add(getCRLFTextObject()).add(new Text("                   " + mainPartStrings.get(2)).setTextAlignment(TextAlignment.CENTER)).setFontSize(8));
        Text text = new Text("            (дата)             ").setTextRise(-10).setUnderline().setFontSize(8);
        document.add(new Paragraph(new Text("c ")).add(text).add("  по ").add(text).add(" .").add(getCRLFTextObject()));
    }

    private void addStart(Document document, Person person) {
        Paragraph firstStartParagraph = new Paragraph();
        firstStartParagraph.setTextAlignment(TextAlignment.LEFT).setMarginLeft(300).setFontSize(15);
        for (int i = 0; i < firstPartOfAcademicLeaveText.size(); i++) {
            firstStartParagraph.add(firstPartOfAcademicLeaveText.get(i));
            firstStartParagraph.add(getCRLFTextObject());
        }
        document.add(firstStartParagraph);


        Text fullName = createTextObjectFromFullName(person);
        Paragraph secondStartParagraph = getParagraphByTextAligmentAndMargin(fullName, TextAlignment.LEFT, 300).setMultipliedLeading(1.0f).add(getCRLFTextObject());
        for (int i = 0; i < secondPartOfAcademicLeaveText.size() - 1; i++) {
            secondStartParagraph.add(new Text(secondPartOfAcademicLeaveText.get(i)).setFontSize(8));
            secondStartParagraph.add(getCRLFTextObject());
        }
        document.add(secondStartParagraph);


        //conclusion of start paragraphs
        Text text = new Text("_________________________________");
        document.add(getParagraphByTextAligmentAndMargin(text, TextAlignment.LEFT, 300).add(getCRLFTextObject()));
        document.add(getParagraphByTextAligmentAndMargin(text, TextAlignment.LEFT, 300).setMultipliedLeading(1.0f)
                .add(getCRLFTextObject())
                .add(new Text(secondPartOfAcademicLeaveText.get(secondPartOfAcademicLeaveText.size() - 1)).setFontSize(8))
                .add(getCRLFTextObject()).add(getCRLFTextObject()));
    }





    private Text createTextObjectFromFullName(Person person) {
        Text result = new Text("от " + getFullName(person)).setFontSize(12).setUnderline();

        if (result.getText().length() < 35) {
            int count = 35 - result.getText().length() + 1;
            String x = " ";
            for (int i = 0; i < count - 1; i++) {
                x += " ";
            }
            result = new Text(result.getText() + x).setUnderline();
        }

        return result;
    }

    private String getFullName(Person person) {
        return person.getName() + " " + person.getPatronymic() + " " + person.getSurname();
    }
}
