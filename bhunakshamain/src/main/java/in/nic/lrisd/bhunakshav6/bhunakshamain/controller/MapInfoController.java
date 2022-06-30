package in.nic.lrisd.bhunakshav6.bhunakshamain.controller;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.service.MapInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mapinfo")
public class MapInfoController {

    @Autowired
    private MapInfoService mapInfoService;


    @RequestMapping(value = "/getVVVVExtentGeoref",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    @CrossOrigin(origins = "http://localhost:3000")
    public Map getVVVVExtentGeoref(@RequestParam("srs") int srs,
                                   @RequestParam(value = "vsrno", required = false) String vsrNo,
                                   @RequestParam(value = "giscode", required = false) String gisCode,
                                   @RequestParam(value = "vsrLevels", required = false) String vsrLevels,
                                   @RequestParam(value = "gisLevels", required = true) String gisLevels) throws Exception {

        return mapInfoService.getVVVVExtentGeoref(srs,vsrNo,gisCode,vsrLevels,gisLevels);
    }
}
