package in.nic.lrisd.bhunakshav6.state.statedataprovider.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.CodeValueObj;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.service.LevelDecoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class LevelDecoderImpl implements LevelDecoder {

    @Override
    public int getVsrLevelCount() {
        return 4;
    }

    @Override
    public int getMapLevelCount() {
        return 1;  //To be made 4 after testing other map types in MH
    }

    @Override
    public String getLevelNameLn(int level) {
        switch (level) {
            case 1:
                return "District";
            case 2:
                return "Sub Division";
            case 3:
                return "Circle";
            case 4:
                return "Village";
            case 5:
                return "Sheet No";
            default:
                return "";
        }
    }

    @Override
    public String getLevelNameEn(int level) {
        switch (level) {
            case 1:
                return "District";
            case 2:
                return "Sub Division";
            case 3:
                return "Circle";
            case 4:
                return "Village";
            case 5:
                return "Sheet No";
            default:
                return "";
        }
    }

    @Override
    public List<String> getAllLevelLablesEn() {
        return Stream.of("District", "Sub Divison", "Circle", "Village", "Sheet No").collect(Collectors.toList());
    }

    @Override
    public String createVsrno(String... levelCodes) throws Exception {
        if (levelCodes.length < getVsrLevelCount()) {
            throw new Exception("Invalid number of parameters in code");
        } else {
            String vsrNo = levelCodes[0] + levelCodes[1] + levelCodes[2] + levelCodes[3];

            return vsrNo;
        }
    }

    @Override
    public String createGisCode(String... levelCodes) throws Exception {

        if (levelCodes.length < (getVsrLevelCount() + getMapLevelCount())) {
            Logger.getLogger(LevelDecoderImpl.class.getName()).log(Level.SEVERE, "Invalid parameters to create gisCode", "Invalid parameters to create gisCode");
        } else {
            return levelCodes[0] + "" + levelCodes[1] + "" + levelCodes[2] + levelCodes[3] + levelCodes[4];
        }
        return null;
    }

    @Override
    public String[] levelsFromGisCode(String gisCode) throws Exception {
        try {
            if (gisCode.toCharArray().length >= 10) {
                String[] levels = new String[5];
                levels[0] = gisCode.substring(0, 2);
                levels[1] = gisCode.substring(2, 4);
                levels[2] = gisCode.substring(4,7);
                levels[3] = gisCode.substring(7,10);
                levels[4] = gisCode.substring(10);
                return levels;
            }

        } catch (Exception ex) {
            Logger.getLogger(LevelDecoderImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String[] levelsFromVsrno(String vsrno) {
        try {
            if (vsrno.toCharArray().length == 10) {
                String[] levels = new String[4];
                levels[0] = vsrno.substring(0, 2);
                levels[1] = vsrno.substring(2, 4);
                levels[2] = vsrno.substring(4,7);
                levels[3] = vsrno.substring(7,10);
                return levels;
            }
        } catch (Exception ex) {
            Logger.getLogger(LevelDecoderImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean isVsrnoValid(String vsrno) {
        if (vsrno != null && !vsrno.equals("")) {
            if (vsrno.length() == 10) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isGisCodeValid(String gisCode)
    {
        if (gisCode != null && !gisCode.equals("")) {
            if (gisCode.length() >= 10) {
                return true;
            }
        }

        return false;

    }

    @Override
    public boolean isValid(String vsrno, String gisCode) {
        if (isVsrnoValid(vsrno) == true && isGisCodeValid(gisCode) == true) {
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<CodeValueObj> getPossibleInputAtLevel(int level) {
        return null;

    }
}
