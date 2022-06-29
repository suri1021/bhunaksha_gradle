package in.nic.lrisd.bhunakshav6.bhunakshacommon.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.Khasramap;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface KhasramapService {
    public List<Khasramap> findAllByBhucode(String state, String bhucode);

    public Khasramap findByBhucodeKide(String state, String bhucode, String kide);

    public Khasramap findById(String id);

    public Khasramap findByBhucodeId(String state, String bhucode, String id);

    public Double getArea(String state, String bhucode, String id);

    public Map getExtent(String state, String dbSchema, String bhucode, String id);

    public Map findBYInnerPointMapData(String state, String bhucode, String pointText);

    public Map findMapDataCentroidByBhucodeId(String state, String bhucode, String kide, String id);

    public Khasramap findByIntersectionPoint(String state, String bhucode, String ptText);

    public Timestamp findMaxLastUpdatedByBhucode(String state, String bhucode);

    List<Map<String, String>> fetchPlotMaps(String stateCode, String gisCode, String srs, String destSrs, String plotIds, String plotNos, String bboxText);

    Map getExtentGisCode(String stateCode, String gisCode);

    Map getExtentVsrNo(String stateCode, String vsrNo);
}
