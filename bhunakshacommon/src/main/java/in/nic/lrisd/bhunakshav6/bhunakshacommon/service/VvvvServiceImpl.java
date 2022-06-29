package in.nic.lrisd.bhunakshav6.bhunakshacommon.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.dao.VvvvDAO;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.Vvvv;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class VvvvServiceImpl implements VvvvService {

    @Autowired
    private VvvvDAO vvvvDAO;

    @Override
    public List<Vvvv> findAll() {
        return vvvvDAO.findAll();
    }

    @Override
    public Vvvv findByBhucode(String bhucode) {
        return vvvvDAO.findByBhucode(bhucode);
    }

    @Override
    public List<Vvvv> findByPattern(String pattern) {
        return vvvvDAO.findByPattern(pattern);
    }

    @Override
    public List<Vvvv> findByVsrno(String vsrno) {
        return vvvvDAO.findByVsrno(vsrno);
    }

    @Override
    public List<Vvvv> findByPointInterSection(String ptText, String srs) {
        return vvvvDAO.findByPointInterSection(ptText, srs);
    }

    @Override
    public List<Map<String, String>> getGisCodesAsList(String gisCodes, String skipGisCodes, String vsrNos, String skipCodes, String cqlFilter, String bboxText, String srs) {
        List<String> gisCodeList = (StringUtils.isEmpty(gisCodes)) ? null : Arrays.asList(gisCodes.split(","));
        List<String> skipGisCodesList = (StringUtils.isEmpty(skipGisCodes)) ? null : Arrays.asList(skipGisCodes.split(","));
        List<String> vsrNosList = (StringUtils.isEmpty(vsrNos)) ? null : Arrays.asList(vsrNos.split(","));
        List<String> skipCodesList = (StringUtils.isEmpty(skipCodes)) ? null : Arrays.asList(skipCodes.split(","));

        return vvvvDAO.getGisCodesAsList(gisCodeList, skipGisCodesList, vsrNosList, skipCodesList, cqlFilter, bboxText, srs);
    }

    @Override
    public Map getExtentGisCode(String gisCode, int srs) {
        return vvvvDAO.getExtentGiscode(gisCode, srs);
    }

    @Override
    public Map getExtentVsrNo(String vsrNo, int srs) {
        return vvvvDAO.getExtentVsrNo(vsrNo, srs);
    }

    @Override
    public Map getExtentSrs(int srs) {
        return vvvvDAO.getExtentSrs(srs);
    }
}