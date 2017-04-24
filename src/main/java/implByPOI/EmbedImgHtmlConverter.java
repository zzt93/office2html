package implByPOI;

import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Base64;

/**
 * Created by xmc1993 on 17/3/21.
 */
public class EmbedImgHtmlConverter extends WordToHtmlConverter {

    EmbedImgHtmlConverter() throws ParserConfigurationException {
        super(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
    }

    @Override
    protected void processImageWithoutPicturesManager(Element currentBlock, boolean inlined, Picture picture) {
        Element imgNode = currentBlock.getOwnerDocument().createElement("img");// 创建img标签
        StringBuilder sb = new StringBuilder(picture.getSize() + PoiEmbedImgHtmlConverter.EMBED_IMG_SRC_PREFIEX.length())
                .append(PoiEmbedImgHtmlConverter.EMBED_IMG_SRC_PREFIEX)
                .append(Base64.getEncoder().encodeToString(picture.getRawContent()));
        imgNode.setAttribute("src", sb.toString());
        currentBlock.appendChild(imgNode);
    }
}
