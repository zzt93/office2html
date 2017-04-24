package implByPOI;

import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.IURIResolver;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

/**
 * Created by zzt on 17/3/30.
 */
public class EmbedImgResolver extends FileImageExtractor implements IURIResolver {
    private byte[] picture;

    EmbedImgResolver(String baseDir) {
        super(new File(baseDir));
    }

    @Override
    public void extract(String imagePath, byte[] imageData) throws IOException {
        this.picture = imageData;
    }

    @Override
    public String resolve(String uri) {
        StringBuilder sb = new StringBuilder(picture.length + PoiEmbedImgHtmlConverter.EMBED_IMG_SRC_PREFIEX.length())
                .append(PoiEmbedImgHtmlConverter.EMBED_IMG_SRC_PREFIEX)
                .append(Base64.getEncoder().encodeToString(picture));
        return sb.toString();
    }
}
