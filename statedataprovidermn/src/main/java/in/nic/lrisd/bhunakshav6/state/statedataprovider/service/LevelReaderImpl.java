package in.nic.lrisd.bhunakshav6.state.statedataprovider.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.dao.LevelReaderDAO;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.CodeValueObj;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.service.LevelReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelReaderImpl implements LevelReader {

    @Autowired
    private LevelReaderDAO levelReaderDAO;

    @Override
    public List<CodeValueObj> fetchListForLevel(int level, String... code) {
        return levelReaderDAO.fetchListForLevel(level, code);
    }

    @Override
    public String fetchGisInfo(String gisCode) {
        return null;
    }

    @Override
    public CodeValueObj fetchLevelValue(int vsrLevelCount, String[] gis_codes) {
        return null;
    }
}