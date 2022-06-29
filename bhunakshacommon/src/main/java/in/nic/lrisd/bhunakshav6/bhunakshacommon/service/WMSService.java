package in.nic.lrisd.bhunakshav6.bhunakshacommon.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.bean.WMSParms;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class WMSService {

    @Autowired
    WMSLayerBuilder layerBuilder;

    MapContent map = new MapContent();

    public byte[] getWmsImage(WMSParms wmsParms) {

        Graphics2D gr = null;
        ByteArrayOutputStream baos = null;
        try {
            List<Layer> layerList = layerBuilder.createLayers(wmsParms);
            if (layerList == null) return null;

            for (Layer layer : layerList) {

                if (layer == null) {
                    continue;
                }

                map.addLayer(layer);
            }

            GTRenderer renderer = new StreamingRenderer();
            Rectangle imageBounds = new Rectangle(0, 0, wmsParms.getWidth(), wmsParms.getHeight());
            BufferedImage image = null;
            if (wmsParms.isTransparent()) {
                image = new BufferedImage(imageBounds.width, imageBounds.height, BufferedImage.TYPE_INT_ARGB);
                gr = image.createGraphics();
                gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0));
            } else {
                image = new BufferedImage(imageBounds.width, imageBounds.height, BufferedImage.TYPE_INT_RGB);
                gr = image.createGraphics();
                gr.setPaint(wmsParms.getBgcolor());
            }
            gr.fill(imageBounds);
            renderer.setMapContent(map);
            renderer.paint(gr, imageBounds, wmsParms.getBbox());

            //out = response.getOutputStream();
            baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageData = baos.toByteArray();
            return imageData;
        } catch (IOException e) {
            Logger.getLogger(WMSService.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e) {
            Logger.getLogger(WMSService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } finally {
            map.dispose();

            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (gr != null) {
                gr.dispose();
//                gr = null;
            }

        }
        return null;
    }
}
