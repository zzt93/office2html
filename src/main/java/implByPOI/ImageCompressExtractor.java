package implByPOI;

import org.apache.poi.xwpf.converter.core.FileImageExtractor;

import java.io.File;
import java.io.IOException;

/**
 * Created by zzt on 17/3/31.
 */
public class ImageCompressExtractor extends FileImageExtractor {
    public ImageCompressExtractor(File baseDir) {
        super(baseDir);

    }

    @Override
    public void extract(String imagePath, byte[] imageData) throws IOException {
        super.extract(imagePath, ImgCompression.compressImg(imageData));
    }
}
