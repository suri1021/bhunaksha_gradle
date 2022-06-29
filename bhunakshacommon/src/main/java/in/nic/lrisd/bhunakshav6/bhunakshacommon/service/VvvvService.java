package in.nic.lrisd.bhunakshav6.bhunakshacommon.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.Vvvv;

import java.util.List;
import java.util.Map;

public interface VvvvService {
    public List<Vvvv> findAll();

    public Vvvv findByBhucode(String bhucode);

    public List<Vvvv> findByPattern(String pattern);

    public List<Vvvv> findByVsrno(String vsrno);

    public List<Vvvv> findByPointInterSection(String ptText, String srs);

    public List<Map<String, String>>  getGisCodesAsList(String gisCodes, String skipGisCodes, String vsrNos, String skipCodes, String cqlFilter, String bboxText, String srs);

    Map getExtentGisCode(String gisCode, int srs);

    Map getExtentVsrNo(String vsrNo, int srs);

    Map getExtentSrs(int srs);
}
