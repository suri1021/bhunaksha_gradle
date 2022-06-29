package in.nic.lrisd.bhunakshav6.bhunakshacommon.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.dao.KhasramapDAO;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.Khasramap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class KhasramapServiceImpl implements  KhasramapService {

    @Autowired
    private KhasramapDAO khasramapDAO;

    @Override
    @Transactional
    public List<Khasramap> findAllByBhucode(String state, String bhucode) {
        return khasramapDAO.findAllByBhucode(state, bhucode);
    }

    @Override
    @Transactional
    public Khasramap findByBhucodeKide(String state, String bhucode, String kide) {
        return khasramapDAO.findByBhucodeKide(state, bhucode, kide);
    }

    @Override
    @Transactional
    public Khasramap findById(String id) {
        return khasramapDAO.findById(id);
    }

    @Override
    public Khasramap findByBhucodeId(String state, String bhucode, String id) {
        return khasramapDAO.findByBhucodeId(state, bhucode, id);
    }

    @Override
    public Double getArea(String state, String bhucode, String id) {
       return khasramapDAO.getArea(state,bhucode,id);
    }

    @Override
    public Map getExtent(String state, String dbSchema, String bhucode, String id) {
        return khasramapDAO.getExtent(state, dbSchema, bhucode, id);
    }

    @Override
    public Map findBYInnerPointMapData(String state, String bhucode, String pointText) {
        return khasramapDAO.findBYInnerPointMapData(state, bhucode, pointText);
    }

    @Override
    public Map findMapDataCentroidByBhucodeId(String state, String bhucode, String kide, String id) {
        return  khasramapDAO.findMapDataCentroidByBhucodeId(state, bhucode, kide, id);
    }

    @Override
    public Khasramap findByIntersectionPoint(String state, String bhucode, String ptText) {
        return khasramapDAO.findByIntersectionPoint(state, bhucode, ptText);
    }

    @Override
    public Timestamp findMaxLastUpdatedByBhucode(String state, String bhucode) {
        return khasramapDAO.findMaxLastUpdatedByBhucode(state, bhucode);
    }

    @Override
    public List<Map<String, String>> fetchPlotMaps(String stateCode, String gisCode, String srs, String destSrs, String plotIds, String plotNos, String bboxText) {
        List<String> plotIdsList = (StringUtils.isEmpty(plotIds)) ? null : Arrays.asList(plotIds.split(","));
        List<String> plotNosList = (StringUtils.isEmpty(plotNos)) ? null : Arrays.asList(plotNos.split(","));

        return khasramapDAO.fetchPlotMaps(stateCode, gisCode, srs, destSrs, plotIdsList, plotNosList, bboxText);
    }

    @Override
    public Map getExtentGisCode(String stateCode, String gisCode) {
        return khasramapDAO.getExtentGisCode(stateCode, gisCode);
    }

    @Override
    public Map getExtentVsrNo(String stateCode, String gisCode) {
        return khasramapDAO.getExtentVsrNo(stateCode, gisCode);
    }
}