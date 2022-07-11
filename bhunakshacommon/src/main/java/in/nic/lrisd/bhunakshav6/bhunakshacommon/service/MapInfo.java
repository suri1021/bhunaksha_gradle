package in.nic.lrisd.bhunakshav6.bhunakshacommon.service;

import org.locationtech.jts.geom.Point;
import org.springframework.http.ResponseEntity;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public interface MapInfo {
    public Map getExtent(String gisCode, String plotId) throws Exception;
    public Map getFMBExtentGeoref(int srs,String vsrNo,String gisCode,String vsrLevels,String gisLevels) throws Exception;
    public List<Map<String, String>> getPlotId(String gisCode, String plotno) throws Exception;
    public Map getExtentGeoref(int srs, String gisCode, String plotId) throws Exception;
    public Map getMapPlots(String gisCode) throws Exception;
    public Map getVVVVExtentGeoref(int srs, String vsrNo, String gisCode, String vsrLevels, String gisLevels) throws Exception;
    public Map getScaleFactor(String gisCode) throws Exception;
    public Map getPlotAtPosition(String gisCode, double x, double y);
    public Map getPlotAtXYGeoref(int srs, double x, double y, String pVsrno, String pGisCode, String skipCodes);
    public Map getPlotInfo(String gisCode,String plotId,String plotNo, String attrs);
    public Map<String, String> getAreaFormat();
    public Map<String, String> getAreaUnitsFormat();
    public Map<String, String> getPlotsAndLayers(String gisCode);
    public String getVillageGeoJSonservice(String gisCode, String key, String id);
    public String getVillageGeoJSon(String giscode, String noPlotInfo, String noCompress);
    public String getVillagePointGeoJSon(String giscode, String noPlotInfo, String noCompress);
    public String getVillageLineGeoJSon(String giscode, String noPlotInfo, String noCompress);
    public String getVillagePolygonGeoJSon(String giscode, String noPlotInfo, String noCompress);
    public String getVillageGeoJSonLayers(String giscode, String noPlotInfo, String noCompress);
    public String getPlotsGeoJSon(String giscode, String noPlotInfo, String noCompress, String selectedPlots, boolean transformRequired);
    public List<Map<String, String>> getAllKideInVsr(String gisCode);
    public Map comments(String sign, String user, String password, String bhucode,String plotNo,String comment);
    public String updateComments(String token,String updates);
    public List<Map<String, Object>> getComments();
    public List<Map<String, Object>> getVVVVData(String token, String loginLevel);
    public String updateScale(String sign, String updatedScale);
    public String transformGeom(List<String> source, List<String> dest, String geomText);
    public String getGisCode(String plotNo, String currentGisCode);
    public String getPageSizes();
    public String getPlotInfo(String mutationNo, String vsrNo, String plotNo);

    Map getPlotInfo(String gisCode, String plotNo);

    public Map<String, List<Map<String, String>>> getAllLayersEdition(String giscode, String kide);
    public String getCompressdata(String plotId);
    public Map<String, List<Map<String, String>>> getLayers(String giscode, String code);
    public ResponseEntity getMap(String gisCode, String plotNo, int height, int width, String derivedLayerIds, String selectedLayerTypes,
                                 String ScaleTextField, String mapType, String sampleCopy);
    public BufferedImage getPlotImage(String gisCode, String plotNo, int height, int width, String derivedLayerIds, String selectedLayerTypes,
                                       String ScaleTextField, String mapType, String sampleCopy, boolean isSingle);
    public double getSize(double size);
    public double getImageMatrixSize(double size);
    public String getCRS(String gisCode);
    public String isValidGisCode(String gisCode);
    public String getPointsfromPNIU(String pniu,String gisCode);

    Point getCentroid(String geometryText);
}
