package implByPOI;

import converter.IHtmlConverter;
import exception.FileConversionFormatException;
import com.google.common.collect.Lists;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.converter.ExcelToHtmlUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * 使用POI作为转换器实现转换功能
 */
public class PoiHtmlConvert implements IHtmlConverter {
    private static Logger log = LoggerFactory.getLogger(PoiHtmlConvert.class);
    private List<String> supportedExtensionsForHtmlConverter = Lists.newArrayList("doc", "docx");


    @Override
    public void docToHtml(InputStream in, Path outDir, String fileName) {
        try {
            HWPFDocument wordDocument = new HWPFDocument(in);
            // save image first
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            wordToHtmlConverter.setPicturesManager(new HtmlPicturesManager(outDir.toString()));
            wordToHtmlConverter.processDocument(wordDocument);
            DOMSource domSource = new DOMSource(wordToHtmlConverter.getDocument());
            // then transform html
            Transformer serializer = TransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "no");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, new StreamResult(outFile(outDir, fileName)));
        } catch (Exception e) {
            log.error("将doc文件转换为html时出错", e);
            throw new FileConversionFormatException("将doc文件转换为html时出错!", e);
        }
    }

    /**
     * img will automatically store under `baseDir/` + `WORD_MEDIA`
     * {@link javax.xml.transform.URIResolver}: used to set img/@src, default resolver will also append `WORD_MEDIA`
     * {@link org.apache.poi.xwpf.converter.core.IImageExtractor}: used to extract image file
     *
     * @see javax.xml.transform.URIResolver
     * @see org.apache.poi.xwpf.converter.core.IImageExtractor
     * @see org.apache.poi.xwpf.converter.core.XWPFDocumentVisitor#WORD_MEDIA
     */
    @Override
    public void docxToHtml(InputStream in, Path outDir, String targetFileName) {
        try (
                OutputStream out = new FileOutputStream(outFile(outDir, targetFileName));
        ) {
            XWPFDocument document = new XWPFDocument(in);

            XHTMLOptions options = XHTMLOptions.create();
            ExtractorAndResolver extractor = new ExtractorAndResolver(outDir.toFile());
            options.setExtractor(extractor);
            options.URIResolver(extractor);

            XHTMLConverter.getInstance().convert(document, out, options);
        } catch (Exception e) {
            log.error("将docx文件转换为html时出错", e);
            throw new FileConversionFormatException("将docx文件转换为html时出错!", e);
        }
    }

    @Override
    public void xlsxToHtml(InputStream in, Path outDir, String targetFileName) {

    }

    @Override
    public void xlsToHtml(InputStream in, Path outDir, String targetFileName) {
        try {
            Document doc = process(in);
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(outFile(outDir, targetFileName));
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "no");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
        } catch (Exception e) {
            log.error("将xls文件转换为html时出错", e);
            throw new FileConversionFormatException("将xls文件转换为html时出错!", e);
        }
    }

    private Document process(InputStream in) throws ParserConfigurationException, IOException {
        final HSSFWorkbook workbook = new HSSFWorkbook(in);
        ExcelToHtmlConverter excelToHtmlConverter = new ExcelToHtmlConverter(
                XMLHelper.getDocumentBuilderFactory().newDocumentBuilder()
                        .newDocument());
        excelToHtmlConverter.processWorkbook(workbook);
        Document doc = excelToHtmlConverter.getDocument();
        workbook.close();
        return doc;
    }


    @Override
    public List<String> getSupportedExtensionsForHtml() {
        return supportedExtensionsForHtmlConverter;
    }

}
