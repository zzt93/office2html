package converter;


import exception.FileConversionFormatException;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 将其它格式的文件转换为html
 * <br>
 * <a href="http://stackoverflow.com/questions/227236/convert-word-doc-to-html-programmatically-in-java">
 * multiple lib to convert doc to html</a>
 */
public interface IHtmlConverter {


    default File outFile(Path outDir, String fileName) {
        return Paths.get(outDir.toString(), fileName).toFile();
    }

    default void toHtml(String extension, InputStream in, Path outDir, String targetFileName) {
        switch (extension) {
            case "doc":
                docToHtml(in, outDir, targetFileName);
                break;
            case "docx":
                docxToHtml(in, outDir, targetFileName);
                break;
            case "xls":
                xlsToHtml(in, outDir, targetFileName);
                break;
            case "xlsx":
                xlsxToHtml(in, outDir, targetFileName);
                break;
            default:
                throw new FileConversionFormatException("不支持的文件类型:" + extension);
        }
    }

    void xlsxToHtml(InputStream in, Path outDir, String targetFileName);

    void xlsToHtml(InputStream in, Path outDir, String targetFileName);

    void docxToHtml(InputStream in, Path outDir, String targetFileName);

    /**
     * doc文件转换为html文件
     *
     * @param in       源文件的文件名
     * @param outDir   目标文件的文件名
     * @param targetFileName
     */
    void docToHtml(InputStream in, Path outDir, String targetFileName);


    /**
     * @return 获得Html转换器支持的文件后缀名, 不区分大小写，""表示没有扩展名的文件
     */
    List<String> getSupportedExtensionsForHtml();

    /**
     * 判断转换器是否支持一个扩展名
     *
     * @param extension 文件扩展名
     * @return true 支持，false 不支持
     */
    default boolean isSupportedExtensionsForHtml(String extension) {
        List<String> list = getSupportedExtensionsForHtml();
        return list != null && list.stream().anyMatch(item -> item.equalsIgnoreCase(extension));
    }


}
