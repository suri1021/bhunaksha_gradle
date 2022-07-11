package in.nic.lrisd.bhunakshav6.bhunakshamain.controller;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.service.MapInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/mapinfo")
public class MapInfoController {

    @Autowired
    private MapInfo mapInfo;


    @RequestMapping(value = "/getVVVVExtentGeoref",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    //@CrossOrigin(origins = "*")
    public Map getVVVVExtentGeoref(@RequestParam("srs") int srs,
                                   @RequestParam(value = "vsrno", required = false) String vsrNo,
                                   @RequestParam(value = "giscode", required = false) String gisCode,
                                   @RequestParam(value = "vsrLevels", required = false) String vsrLevels,
                                   @RequestParam(value = "gisLevels", required = true) String gisLevels) throws Exception {

        return mapInfo.getVVVVExtentGeoref(srs,vsrNo,gisCode,vsrLevels,gisLevels);
    }

    @RequestMapping(value =  "/getScaleFactor",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    //@CrossOrigin(origins = "*")
    public Map getScaleFactor(@RequestParam("giscode") String gisCode) throws Exception {
        return  mapInfo.getScaleFactor(gisCode);
    }

    @RequestMapping(value =  "/getPlotAtXY",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    //@CrossOrigin(origins = "*")
    public Map getPlotAtXY(@RequestParam("giscode") String gisCode, @RequestParam double x, @RequestParam double y) throws Exception {
        return  mapInfo.getPlotAtPosition(gisCode, x, y);
    }
}
