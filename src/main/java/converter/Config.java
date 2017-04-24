package converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 保存系统配置信息
 */
public class Config {
    /**
     * 应用根目录
     */
    private static String basePath;
    /**
     * 存放上传文件的临时目录
     */
    private static String uploadPath;
    /**
     * 存放文件格式转换后新文件的目录
     */
    private static String convertPath;
    /**
     * 允许的文件扩展名
     */
    private static List<String> allowedExtensions = new ArrayList<>();

    /**
     * 系统根目录。
     * <p>例如:/home/tomcat/webapps/ROOT/</p>
     * @return 系统根目录
     */
    public static String getBasePath() {
        return basePath;
    }

    static void setBasePath(String path) {
        basePath = path;
    }

    /**
     * @return true 允许的文件扩展名
     */
    public static boolean isAllowedExtensions(String extension) {
        if(allowedExtensions.isEmpty()){
            String[] extensions = new String[]{"jpg","png","gif","doc","docx","xls","xlsx","ppt","pptx","pdf","txt"};
            allowedExtensions.addAll(Arrays.asList(extensions));
        }
        return allowedExtensions.contains(extension);
    }

    /**
     *
     * @return 存放文件格式转换后新文件的目录
     */
    public static String getConvertPath() {
        return convertPath;
    }

    static void setConvertPath(String path) {
        Config.convertPath = path;
    }

    /**
     *
     * @return 存放上传文件的临时目录
     */
    public static String getUploadPath() {
        return uploadPath;
    }

    static void setUploadPath(String path) {
        uploadPath = path;
    }

}
