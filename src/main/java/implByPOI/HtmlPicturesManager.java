package implByPOI;

import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;

public class HtmlPicturesManager implements PicturesManager {
    private Logger log = LoggerFactory.getLogger(HtmlPicturesManager.class);
    private String baseDir;

    /**
     * @param baseDir 存放图片的文件夹名称
     *
     */
    HtmlPicturesManager(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public String savePicture(byte[] content, PictureType pictureType, String name, float widthInches, float heightInches) {
        try (FileOutputStream out = new FileOutputStream(new File(baseDir, name))) {
            out.write(content);
            out.flush();
        } catch (Exception e) {
            log.error("保存图片出错", e);
        }
        return name;
    }

}
