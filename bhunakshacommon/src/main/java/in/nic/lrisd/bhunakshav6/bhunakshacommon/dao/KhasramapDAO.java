package in.nic.lrisd.bhunakshav6.bhunakshacommon.dao;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.Khasramap;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface KhasramapDAO {
    public List<Khasramap> findAllByBhucode(String state, String bhucode);

    public Khasramap findByBhucodeKide(String state, String bhucode, String kide);

    public Khasramap findById(String id);

    public Khasramap findByBhucodeId(String state, String bhucode, String id);

    public Khasramap findByIntersectionPoint(String state, String bhucode, String ptText);

    public Double getArea(String state, String bhucode, String id);

    public Map getExtent(String state, String dbSchema, String bhucode, String id);

    public Map findBYInnerPointMapData(String state, String bhucode, String pointText);

    public Map findMapDataCentroidByBhucodeId(String state, String bhucode, String kide, String id);

    public Timestamp findMaxLastUpdatedByBhucode(String state, String bhucode);

    List fetchPlotMaps(String state, String bhucode, String sourceSrs, String destSrs, List plotIds, List plotNos, String bboxText);

    Map getExtentGisCode(String state, String bhucode);

    Map getExtentVsrNo(String state, String vsrNo);
}
