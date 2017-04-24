package implByPOI;

import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.IURIResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by zzt on 17/4/7.
 */
public class ExtractorAndResolver extends FileImageExtractor implements IURIResolver {

    public ExtractorAndResolver(File baseDir) {
        super(baseDir);
    }

    @Override
    public void extract(String imagePath, byte[] imageData) throws IOException {
        super.extract(Paths.get(imagePath).getFileName().toString(), imageData);
    }

    @Override
    public String resolve(String uri) {
        return Paths.get(uri).getFileName().toString();
    }


}
