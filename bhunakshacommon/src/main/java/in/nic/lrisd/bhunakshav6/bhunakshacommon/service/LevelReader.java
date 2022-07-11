package in.nic.lrisd.bhunakshav6.bhunakshacommon.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.CodeValueObj;

import java.util.List;

public interface LevelReaderService {
    public List<CodeValueObj> fetchListForLevel(int level, String... code);

    String fetchGisInfo(String gisCode);

    CodeValueObj fetchLevelValue(int vsrLevelCount, String[] gis_codes);
}
