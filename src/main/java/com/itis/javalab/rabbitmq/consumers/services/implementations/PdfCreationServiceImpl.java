package com.itis.javalab.rabbitmq.consumers.services.implementations;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.font.FontProvider;
import com.itis.javalab.rabbitmq.consumers.models.Person;
import com.itis.javalab.rabbitmq.consumers.services.interfaces.FileNameCreator;
import com.itis.javalab.rabbitmq.consumers.services.interfaces.PdfCreatorService;
import com.itis.javalab.rabbitmq.consumers.services.interfaces.TemplateProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.itis.javalab.rabbitmq.consumers.utils.CreationService.getParagraphByTextAligmentAndMargin;

@Service
@Profile({"dismiss","academ"})
public class PdfCreationServiceImpl implements PdfCreatorService {
    private final String prefix;
    private final String basicFileAddress;
    private final String templateName;

    private final FileNameCreator fileNameCreator;
    private final TemplateProcessor templateProcessor;

    public PdfCreationServiceImpl(@Value("${prefix}") String prefix,
                                  @Value("${file.address}") String basicFileAddress,
                                  @Value("${template.name}") String templateName,
                                  FileNameCreator fileNameCreator,
                                  TemplateProcessor templateProcessor) {
        this.prefix = prefix;
        this.basicFileAddress = basicFileAddress;
        this.templateName = templateName;
        this.fileNameCreator = fileNameCreator;
        this.templateProcessor = templateProcessor;
    }


    @Override
    public String createPfd(Person person) throws IOException {
        String resultFilePath = basicFileAddress + File.separator + fileNameCreator.getFileName(person, prefix);
        Map<String, Object> parametersMap = getParameters(person);
        String html = templateProcessor.getStringFromTemplateByNameAndParameters(templateName, parametersMap);
        ConverterProperties converterProperties = new ConverterProperties();
        FontProvider dfp = new DefaultFontProvider(true, false, false);
        dfp.addFont("/static/font.ttf", "CP1251");
        converterProperties.setFontProvider(dfp);
        PdfWriter pdfWriter = new PdfWriter(resultFilePath);
        Document document = HtmlConverter.convertToDocument(html, pdfWriter, converterProperties);
        document.close();
        return resultFilePath;
    }

    private Map<String, Object> getParameters(Person person) {
        Map<String, Object> parametersMap = new HashMap<>();
        parametersMap.put("fullName", getFullName(person));
        parametersMap.put("passportSeries", person.getPassport());
        parametersMap.put("passportDate", getDateRightFormat(person.getDate()));
        parametersMap.put("lastMainPartDate", getNowDateRightFormat(FormatStyle.LONG));
        parametersMap.put("endDateMainPart", getNowDateRightFormat(FormatStyle.SHORT));
        return parametersMap;

    }

    private String getFullName(Person person) {
        return person.getSurname() + " " + person.getName() + " " + person.getPatronymic();
    }


    private String getNowDateRightFormat(FormatStyle style) {
        return DateTimeFormatter.ofLocalizedDate(style)
                .withLocale(new Locale("ru"))
                .format(LocalDate.now());
    }


    private String getDateRightFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }

}
