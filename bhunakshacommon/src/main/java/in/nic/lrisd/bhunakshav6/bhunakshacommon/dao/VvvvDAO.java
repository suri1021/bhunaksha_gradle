package in.nic.lrisd.bhunakshav6.bhunakshacommon.dao;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.Vvvv;

import java.util.List;
import java.util.Map;

public interface VvvvDAO {
    public List<Vvvv> findAll();

    public Vvvv findByBhucode(String bhucode);

    public List<Vvvv> findByPattern(String pattern);

    public List<Vvvv> findByVsrno(String vsrno);

    public List<Vvvv> findByPointInterSection(String ptText, String srs);

    public List<Map<String, String>> getGisCodesAsList(List gisCodes, List skipGisCodes, List vsrNos, List skipCodes, String cqlFilter, String bboxText, String srs);

    public Map getExtentGiscode(String gisCode, int srs);

    public Map getExtentVsrNo(String vsrNo, int srs);

    Map getExtentSrs(int srs);
}
