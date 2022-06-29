package in.nic.lrisd.bhunakshav6.bhunakshamain.controller;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.bean.WMSParms;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.service.WMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/WMS")
public class WMSController {

    @Autowired
    private WMSService wmsService;

    @GetMapping(value = "", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getWMSImage (HttpServletRequest request) throws IOException {
        byte[] imageData = wmsService.getWmsImage(new WMSParms(request));

        if (imageData != null) {
            return ResponseEntity.ok().body(imageData);
        }
        else
            return ResponseEntity.noContent().build();
    }
}
