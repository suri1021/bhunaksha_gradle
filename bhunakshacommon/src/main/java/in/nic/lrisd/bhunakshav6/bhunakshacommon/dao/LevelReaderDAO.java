package in.nic.lrisd.bhunakshav6.bhunakshacommon.dao;

import java.util.List;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.CodeValueObj;

public interface LevelReaderDAO {
    public List<CodeValueObj> fetchListForLevel(int level, String... code);

    public CodeValueObj fetchLevelValue(int level, String... code);

    public String fetchVsrInfo(String vsrNo);

    public String fetchGisInfo(String giscode);
}