package in.nic.lrisd.bhunakshav6.bhunakshamain.controller;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.bean.CommonUtil;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.SymbolMaster;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.service.SymbolServletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController("SymbolServlet")
public class SymbolServlet {

    @Autowired
    private SymbolServletService symbolServletService;

    @GetMapping("/{code}")
    public ResponseEntity<byte[]> getSymbolServlet(@PathVariable String code, HttpServletRequest request,
                                                   HttpServletResponse response) {
        SymbolMaster symbolMaster = symbolServletService.getSymbolImage(code);

        if (symbolMaster == null) return ResponseEntity.noContent().build();
        response.setContentType(symbolMaster.getImageType());
        byte[] imageData = CommonUtil.convertToBase64Image(symbolMaster.getImage());

        if (imageData != null) {
            return ResponseEntity.ok().body(imageData);
        }
        else
            return ResponseEntity.noContent().build();
    }
}
