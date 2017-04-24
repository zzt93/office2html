package converter;

import com.google.common.collect.Lists;
import implByPOI.PoiHtmlConvert;
import util.FileTool;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * 将一种文件格式转换为其它格式
 */
public class FileConverter {
    private static final List<IHtmlConverter> htmlConverterList =
            Collections.unmodifiableList(Lists.newArrayList(new PoiHtmlConvert()));

    private static final HashMap<String, IHtmlConverter> mappings = new HashMap<>();

    static {
        for (IHtmlConverter iHtmlConverter : htmlConverterList) {
            for (String s : iHtmlConverter.getSupportedExtensionsForHtml()) {
                mappings.put(s, iHtmlConverter);
            }
        }
    }

    private final String fileExtension;

    private IHtmlConverter converter;

    private FileConverter(String fileExtension, IHtmlConverter iHtmlConverter) {
        this.fileExtension = fileExtension;
        converter = iHtmlConverter;
    }

    public static Optional<FileConverter> getInstance(String fileName) {
        String fileExtension = FileTool.getFileExtension(fileName);
        if (mappings.containsKey(fileExtension)) {
            return Optional.of(new FileConverter(fileExtension, mappings.get(fileName)));
        }
        return Optional.empty();
    }

    public void toHtml(InputStream in, Path outDir, String fileName) {
        converter.toHtml(fileExtension, in, outDir, fileName);
    }
}
