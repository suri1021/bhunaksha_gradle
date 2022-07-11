package in.nic.lrisd.bhunakshav6.state.statedataprovider.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.bean.BhunakshaUser;
import in.nic.lrisd.bhunakshav6.bhunakshacommon.service.StateDataProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StateDataProviderImpl implements StateDataProvider {
    @Override
    public boolean validateDB() {
        return false;
    }

    @Override
    public String[] getAllOwnerKhasras(Object object, String vsrNo, String plotNo) {
        return new String[0];
    }

    @Override
    public String getStateName() {
        return null;
    }

    @Override
    public BhunakshaUser login(String user, String pass, String[] levels) {
        return null;
    }

    @Override
    public String getStateCode() {
        return "16";
    }

    @Override
    public Map getRORReportMap(BhunakshaUser user, String vsrNo, String gisCode, String plotNo) {
        return null;
    }

    @Override
    public String getPlotInfo(BhunakshaUser user, String vsrNo, String plotNo, String gisCode) {
        return null;
    }

    @Override
    public String getPlotInfo_default(BhunakshaUser user, String vsrNo, String plotNo, String gisCode) {
        return null;
    }

    @Override
    public String getPlotInfo_occupants(BhunakshaUser user, String vsrNo, String plotNo, String gisCode) {
        return null;
    }

    @Override
    public String getTalukaVillage(BhunakshaUser user, String vsrNo, String plotNo, String gisCode) {
        return null;
    }

    @Override
    public BhunakshaUser login(String user, String pass, String[] levels, String salt) {
        return null;
    }

    @Override
    public String getRORPlotArea(String vsrNo, String plotNo) {
        return null;
    }

    @Override
    public double getPlotAreaSquareMeter(String vsrNo, String plotNo) {
        return 0;
    }

    @Override
    public Double reverseFormatedPlotArea(String formatedArea) {
        return null;
    }

    @Override
    public ArrayList<String> getPlotsInVsr(String vsrNo) {
        return null;
    }

    @Override
    public ArrayList<String> getPlotsNotInList(String vsrNo, ArrayList<String> plotsList) {
        return null;
    }

    @Override
    public ArrayList<String> getRORVillageList(String[] level) {
        return null;
    }

    @Override
    public int getPlotsCount(String vsrNo) {
        return 0;
    }

    @Override
    public int getAreaSum(String vsrNo) {
        return 0;
    }

    @Override
    public String validateDivision(String gisCode, String plotNo, String actualPlotNo) {
        return null;
    }

    @Override
    public int getLoginLevel() {
        return 0;
    }

    @Override
    public String getPlotInfoLinks(String gisCode, String plotNo) {
        return null;
    }

    @Override
    public double getScaleFactor(String gisCode) {
        return 0;
    }

    @Override
    public String getScaleForPlotImage(String gisCode, String scale) {
        return null;
    }

    @Override
    public List<Map<String, Object>> levelRights(String module, String user, String gisCode) {
        return null;
    }

    @Override
    public ArrayList<String> getVillagesList(String[] levels) {
        return null;
    }

    @Override
    public ArrayList<String> getPlotsAreaListInVsr(String vsrNo) {
        return null;
    }

    @Override
    public ArrayList<String> getKideForTheme(String query, String giscode, boolean isThemeLabel) {
        return null;
    }

    @Override
    public String concatLevels(String levels) {
        return null;
    }

    @Override
    public String getForwardSendbackGroup(BhunakshaUser user, String forwardSendback, String gisCode, String plotNo) {
        return null;
    }

    @Override
    public boolean updateMutationDetails(BhunakshaUser user, String gisCode, String plotNo, String operationType, String divId) {
        return false;
    }

    @Override
    public boolean hasVillageRight(BhunakshaUser user, String gisCode, String plotNo) {
        return false;
    }

    @Override
    public void getRORTransactionList(BhunakshaUser user, String gisCode) {

    }

    @Override
    public boolean isPlotNoValid(String gisCode, String plotNo) {
        return false;
    }

    @Override
    public String getExtraInfoForSplit(String vsrno, String plotNo, String mutationNo) {
        return null;
    }

    @Override
    public boolean checkMergePlotNo(String plotNo, String newPlotNo) {
        return false;
    }

    @Override
    public boolean isMutationNoValid(String mutationNo) {
        return false;
    }

    @Override
    public String[] loginSSO(String gid) {
        return new String[0];
    }

    @Override
    public String GetNextMutationNo(String giscode) {
        return null;
    }
}
