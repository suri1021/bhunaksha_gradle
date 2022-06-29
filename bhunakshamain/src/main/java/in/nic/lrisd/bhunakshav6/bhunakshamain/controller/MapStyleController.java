package in.nic.lrisd.bhunakshav6.bhunakshamain.controller;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.MapStyles;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.service.MapStylesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mapstyle")
public class MapStyleController {

    @Autowired
    private MapStylesService mapStylesService;

    @GetMapping("")
    public List<MapStyles> getstyes() {
        return mapStylesService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<MapStyles> getstyle(@PathVariable String id) {
        return mapStylesService.findByid(id);
    }
}
