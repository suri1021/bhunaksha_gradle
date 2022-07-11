package in.nic.lrisd.bhunakshav6.bhunakshacommon.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.CodeValueObj;

import java.util.ArrayList;
import java.util.List;

public interface LevelDecoder {

    public int getVsrLevelCount() throws Exception;
    public int getMapLevelCount() throws Exception;
    public String getLevelNameEn(int level) throws Exception;

    public List<String> getAllLevelLablesEn();
    public String getLevelNameLn(int level) throws Exception;
    public String createVsrno(String... levelCodes) throws Exception;
    public String createGisCode(String... levelCodes) throws Exception;
    public String[] levelsFromGisCode(String gisCode) throws Exception;
    public String[] levelsFromVsrno(String vsrno) throws Exception;
    public boolean isVsrnoValid(String vsrno);
    public boolean isGisCodeValid(String gisCode);
    public boolean isValid(String vsrno, String gisCode);
    public ArrayList<CodeValueObj> getPossibleInputAtLevel(int level);
}
