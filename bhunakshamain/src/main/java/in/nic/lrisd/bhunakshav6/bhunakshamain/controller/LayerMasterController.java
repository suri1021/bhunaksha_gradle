package in.nic.lrisd.bhunakshav6.bhunakshamain.controller;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.LayerMaster;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.service.LayerMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/layermaster")
public class LayerMasterController {

    @Autowired
    private LayerMasterService layerMasterService;

    @GetMapping("")
    public List<LayerMaster> findAll() {
        return layerMasterService.findAll();
    }
}
