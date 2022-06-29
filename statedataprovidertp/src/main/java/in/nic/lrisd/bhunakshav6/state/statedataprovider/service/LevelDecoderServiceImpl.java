package in.nic.lrisd.bhunakshav6.state.statedataprovider.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.entity.CodeValueObj;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.service.LevelDecoderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class LevelDecoderServiceImpl implements LevelDecoderService {

    @Override
    public int getVsrLevelCount() {
        return 5;
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
                return "Subdivision";
            case 3:
                return "Ri";
            case 4:
                return "Tehsil";
            case 5:
                return "Village";
            case 6:
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
                return "Subdivision";
            case 3:
                return "Ri";
            case 4:
                return "Tehsil";
            case 5:
                return "Village";
            case 6:
                return "Sheet No";
            default:
                return "";
        }
    }

    @Override
    public String createVsrno(String... levelCodes) throws Exception {
        if (levelCodes.length < getVsrLevelCount()) {
            throw new Exception("Invalid number of parameters in code");
        } else {
            String vsrNo = levelCodes[0] + levelCodes[1] + levelCodes[2] + levelCodes[3] + levelCodes[4];

            return vsrNo;
        }
    }

    @Override
    public String createGisCode(String... levelCodes) throws Exception {

        if (levelCodes.length < (getVsrLevelCount() + getMapLevelCount())) {
            Logger.getLogger(LevelDecoderServiceImpl.class.getName()).log(Level.SEVERE, "Invalid parameters to create gisCode", "Invalid parameters to create gisCode");
        } else {
            return levelCodes[0] + levelCodes[1] + levelCodes[2] + levelCodes[3] + levelCodes[4] + (levelCodes[5] == null ? "" : levelCodes[5]);
        }
        return null;
    }

    @Override
    public String[] levelsFromGisCode(String gisCode) throws Exception {
        try {
            if (gisCode.toCharArray().length >= 21) {
                String[] levels = new String[6];
                levels[0] = gisCode.substring(0, 3);
                levels[1] = gisCode.substring(3, 7);
                levels[2] = gisCode.substring(7, 10);
                levels[3] = gisCode.substring(10, 15);
                levels[4] = gisCode.substring(15,21);
                levels[5] = gisCode.substring(21);
                return levels;
            }

        } catch (Exception ex) {
            Logger.getLogger(LevelDecoderServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String[] levelsFromVsrno(String vsrNo) {
        try {
            if (vsrNo.toCharArray().length == 21) {
                String[] levels = new String[5];
                levels[0] = vsrNo.substring(0, 3);
                levels[1] = vsrNo.substring(3, 7);
                levels[2] = vsrNo.substring(7, 10);
                levels[3] = vsrNo.substring(10, 15);
                levels[4] = vsrNo.substring(15, 21);
                // levels[5] = vsrNo.substring(21);
                return levels;
            }
        } catch (Exception ex) {
            Logger.getLogger(LevelDecoderServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean isVsrnoValid(String vsrNo) {

        if (vsrNo != null && !vsrNo.equals(""))
        {
            if (vsrNo.length() == 21) return true;
        }

        return false;
    }

    @Override
    public boolean isGisCodeValid(String gisCode)
    {

        if (gisCode != null && !gisCode.equals(""))
        {
            if (gisCode.length() >= 21) return true;
        }

        return false;

    }

    @Override
    public boolean isValid(String vsrno, String gisCode) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
