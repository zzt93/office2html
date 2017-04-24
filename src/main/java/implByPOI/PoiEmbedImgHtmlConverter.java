package implByPOI;

import converter.IHtmlConverter;
import exception.FileConversionFormatException;
import com.google.common.collect.Lists;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * 使用POI作为转换器实现转换功能
 */
public class PoiEmbedImgHtmlConverter implements IHtmlConverter {
    static final String EMBED_IMG_SRC_PREFIEX = "data:;base64,";
    private static Logger log = LoggerFactory.getLogger(PoiEmbedImgHtmlConverter.class);

    @Override
    public void docToHtml(InputStream in, Path outDir, String fileName) {
        try {
            HWPFDocument wordDocument = new HWPFDocument(in);
            // inline image, no need to set pictureManager
            WordToHtmlConverter wordToHtmlConverter = new EmbedImgHtmlConverter();
            wordToHtmlConverter.processDocument(wordDocument);
            DOMSource domSource = new DOMSource(wordToHtmlConverter.getDocument());
            // then transform html
            Transformer serializer = TransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "no");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, new StreamResult(outDir.toFile()));
        } catch (Exception e) {
            log.error("将doc文件转换为html时出错", e);
            throw new FileConversionFormatException("将doc文件转换为html时出错!", e);
        }
    }

    @Override
    public void xlsxToHtml(InputStream in, Path outDir, String targetFileName) {

    }

    @Override
    public void xlsToHtml(InputStream in, Path outDir, String targetFileName) {

    }

    @Override
    public void docxToHtml(InputStream in, Path outDir, String targetFileName) {
        try (OutputStream out = new FileOutputStream(targetFileName)) {
            XWPFDocument document = new XWPFDocument(in);
            String baseDir = Paths.get(targetFileName).getParent().toString();
            EmbedImgResolver resolver = new EmbedImgResolver(baseDir);
            XHTMLOptions options = XHTMLOptions.create()
                    .URIResolver(resolver);
            options.setExtractor(resolver);

            XHTMLConverter.getInstance().convert(document, out, options);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<String> getSupportedExtensionsForHtml() {
        return Lists.newArrayList("doc", "docx");
    }




}