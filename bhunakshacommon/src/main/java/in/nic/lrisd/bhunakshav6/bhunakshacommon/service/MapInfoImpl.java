package in.nic.lrisd.bhunakshav6.bhunakshacommon.service;

import com.google.gson.Gson;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.AppSettings;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.Khasramap;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.Vvvv;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.referencing.operation.MathTransformFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MapInfoImpl implements MapInfo {

    @Autowired
    private LevelDecoder levelDecoder;

    @Autowired
    private KhasramapService khasramapService;

    @Autowired
    private  VvvvService vvvvService;

    @Autowired
    private StateDataProvider stateDataProvider;

    @Autowired
    private MapDataUtil mapDataUtil;

    @Autowired
    private AppSettingsService appSettingsService;

    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

    WKTReader reader = new WKTReader(geometryFactory);


    @Override
    public Map getExtent(String gisCode, String plotId) throws Exception {
        return null;
    }

    @Override
    public Map getFMBExtentGeoref(int srs, String vsrNo, String gisCode, String vsrLevels, String gisLevels) throws Exception {
        return null;
    }

    @Override
    public List<Map<String, String>> getPlotId(String gisCode, String plotno) throws Exception {
        return null;
    }

    @Override
    public Map getExtentGeoref(int srs, String gisCode, String plotId) throws Exception {
        return null;
    }

    @Override
    public Map getMapPlots(String gisCode) throws Exception {
        return null;
    }

    @Override
    public Map getVVVVExtentGeoref(int srs, String vsrNo, String gisCode, String vsrLevels, String gisLevels) throws Exception {

        if (StringUtils.isEmpty(vsrNo) && (! StringUtils.isEmpty(vsrLevels))) {
            vsrNo = levelDecoder.createVsrno(vsrLevels.split(","));
        }
        if (StringUtils.isEmpty(gisCode) && (! StringUtils.isEmpty(gisLevels))) {
            gisCode = levelDecoder.createGisCode(gisLevels.split(","));
        }
        if ((StringUtils.isEmpty(vsrNo) || !levelDecoder.isVsrnoValid(vsrNo)) && (StringUtils.isEmpty(gisCode)
                || !levelDecoder.isGisCodeValid(gisCode)) && srs == 0) {
            return null;
        }

        if (! StringUtils.isEmpty(gisCode)) {
            if (srs != 0) {
                return vvvvService.getExtentGisCode(gisCode, srs);
            }
            else {
                Map ext = khasramapService.getExtentGisCode(stateDataProvider.getStateCode(), gisCode);
                ext.put("gisCode", gisCode);
                ext.put("scaleFactor", mapDataUtil.getScaleFactor(gisCode));
                ext.put("attribution", mapDataUtil.getAttributionForMap("public", gisCode));
                return ext;
            }
        }
        else if(! StringUtils.isEmpty(vsrNo)) {
            if (srs != 0) {
                return vvvvService.getExtentVsrNo(vsrNo, srs);
            }
            else {
                return khasramapService.getExtentVsrNo(stateDataProvider.getStateCode(), vsrNo);
            }
        }
        else if (srs != 0) {
            return vvvvService.getExtentSrs(srs);
        }

        return null;
    }

    @Override
    public Map getScaleFactor(String gisCode) throws Exception {
        Map ext = new HashMap();

        Vvvv vvvv = vvvvService.findByBhucode(gisCode);
        if (vvvv != null) {
            ext.put("scaleFactor", vvvv.getMapscale());
        }

        return ext;
    }

    @Override
    public Map getPlotAtPosition(String gisCode, double x, double y) {
        Map ext = new HashMap();
        Point pt = geometryFactory.createPoint(new Coordinate(x, y));
        Khasramap khasramap = khasramapService.findByIntersectionPoint(stateDataProvider.getStateCode(), gisCode, pt.toText());
        if (khasramap == null) return null;

        ext.put("id", khasramap.getId());
        ext.put("kide", khasramap.getKide());
        Gson gson = new Gson();
        if (khasramap.getAttributes_json() != null) {
            Map attrs = gson.fromJson(khasramap.getAttributes_json().toString(), HashMap.class);
            ext.put("attributes_json", attrs);
        }
        else {
            ext.put("attributes_json", "");
        }

        return ext;
    }

    @Override
    public Map getPlotAtXYGeoref(int srs, double x, double y, String pVsrno, String pGisCode, String skipCodes) {
        MathTransformFactory mf = ReferencingFactoryFinder.getMathTransformFactory(null);
        Point pt = geometryFactory.createPoint(new Coordinate(x, y));
        AppSettings appSettings = appSettingsService.findByCode(Constants.PLOT_AT_XY_EXTRA_FILTER_CQL.code);
        String cql = "";
        if (appSettings != null && appSettings.getCode() != null
                                        && WMSLayerBuilder.isValidFilter(appSettings.getCode())) {
            cql = appSettings.getCode().replaceAll("(?i)strsubstring", "substring");
        }

        
        return null;
    }

    @Override
    public Map getPlotInfo(String gisCode, String plotId, String plotNo, String attrs) {
        return null;
    }

    @Override
    public Map<String, String> getAreaFormat() {
        return null;
    }

    @Override
    public Map<String, String> getAreaUnitsFormat() {
        return null;
    }

    @Override
    public Map<String, String> getPlotsAndLayers(String gisCode) {
        return null;
    }

    @Override
    public String getVillageGeoJSonservice(String gisCode, String key, String id) {
        return null;
    }

    @Override
    public String getVillageGeoJSon(String giscode, String noPlotInfo, String noCompress) {
        return null;
    }

    @Override
    public String getVillagePointGeoJSon(String giscode, String noPlotInfo, String noCompress) {
        return null;
    }

    @Override
    public String getVillageLineGeoJSon(String giscode, String noPlotInfo, String noCompress) {
        return null;
    }

    @Override
    public String getVillagePolygonGeoJSon(String giscode, String noPlotInfo, String noCompress) {
        return null;
    }

    @Override
    public String getVillageGeoJSonLayers(String giscode, String noPlotInfo, String noCompress) {
        return null;
    }

    @Override
    public String getPlotsGeoJSon(String giscode, String noPlotInfo, String noCompress, String selectedPlots, boolean transformRequired) {
        return null;
    }

    @Override
    public List<Map<String, String>> getAllKideInVsr(String gisCode) {
        return null;
    }

    @Override
    public Map comments(String sign, String user, String password, String bhucode, String plotNo, String comment) {
        return null;
    }

    @Override
    public String updateComments(String token, String updates) {
        return null;
    }

    @Override
    public List<Map<String, Object>> getComments() {
        return null;
    }

    @Override
    public List<Map<String, Object>> getVVVVData(String token, String loginLevel) {
        return null;
    }

    @Override
    public String updateScale(String sign, String updatedScale) {
        return null;
    }

    @Override
    public String transformGeom(List<String> source, List<String> dest, String geomText) {
        return null;
    }

    @Override
    public String getGisCode(String plotNo, String currentGisCode) {
        return null;
    }

    @Override
    public String getPageSizes() {
        return null;
    }

    @Override
    public String getPlotInfo(String mutationNo, String vsrNo, String plotNo) {
        return null;
    }

    @Override
    public Map getPlotInfo(String gisCode, String plotNo) {
        Map info = new HashMap();

        Khasramap khasramap = khasramapService.findByBhucodeKide(stateDataProvider.getStateCode(), gisCode, plotNo);
        if (khasramap != null) {
            Point point = getCentroid(khasramap.getWkbGeometry());
            info.put("center_x", point.getX());
            info.put("center_y", point.getY());
            info.put("plotNo", plotNo);
            info.put("gisCode", gisCode);

        }
        return info;
    }

    @Override
    public Map<String, List<Map<String, String>>> getAllLayersEdition(String giscode, String kide) {
        return null;
    }

    @Override
    public String getCompressdata(String plotId) {
        return null;
    }

    @Override
    public Map<String, List<Map<String, String>>> getLayers(String giscode, String code) {
        return null;
    }

    @Override
    public ResponseEntity getMap(String gisCode, String plotNo, int height, int width, String derivedLayerIds, String selectedLayerTypes, String ScaleTextField, String mapType, String sampleCopy) {
        return null;
    }

    @Override
    public BufferedImage getPlotImage(String gisCode, String plotNo, int height, int width, String derivedLayerIds, String selectedLayerTypes, String ScaleTextField, String mapType, String sampleCopy, boolean isSingle) {
        return null;
    }

    @Override
    public double getSize(double size) {
        return 0;
    }

    @Override
    public double getImageMatrixSize(double size) {
        return 0;
    }

    @Override
    public String getCRS(String gisCode) {
        return null;
    }

    @Override
    public String isValidGisCode(String gisCode) {
        return null;
    }

    @Override
    public String getPointsfromPNIU(String pniu, String gisCode) {
        return null;
    }


    @Override
    public Point getCentroid(String geometryText) {
        if (StringUtils.isBlank(geometryText)) {
            return null;
        }
        else {
            try {
                reader.read(geometryText).getCentroid();
            } catch (ParseException e){
                return null;
            }
        }

        return  null;
    }
}
