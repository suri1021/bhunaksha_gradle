package in.nic.lrisd.bhunakshav6.bhunakshacommon.service;

import in.nic.lrisd.bhunakshav6.bhunakshacommon.bean.BhunakshaUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface StateDataProviderService {

    public static final String FORWARD = "F";
    public static final String SENDBACK = "S";
    public static final String APPROVE = "A";

    public abstract boolean validateDB();

    public String[] getAllOwnerKhasras(Object object, String vsrNo, String plotNo);

    public String getStateName();

    public BhunakshaUser login(String user, String pass, String[] levels);

   // public ContributedTab[] getContributedTabs();

    public String getStateCode();

    public abstract Map getRORReportMap(BhunakshaUser user, String vsrNo, String gisCode, String plotNo);

    public abstract String getPlotInfo(BhunakshaUser user, String vsrNo, String plotNo, String gisCode);

    public abstract String getPlotInfo_default(BhunakshaUser user, String vsrNo, String plotNo, String gisCode);

    public abstract String getPlotInfo_occupants(BhunakshaUser user, String vsrNo, String plotNo, String gisCode);

    public abstract String getTalukaVillage(BhunakshaUser user, String vsrNo, String plotNo, String gisCode);

    public BhunakshaUser login(String user, String pass, String[] levels, String salt);

    public abstract String getRORPlotArea(String vsrNo, String plotNo);

    public abstract double getPlotAreaSquareMeter(String vsrNo, String plotNo);

    public Double reverseFormatedPlotArea(String formatedArea);

    public abstract ArrayList<String> getPlotsInVsr(String vsrNo);

    public abstract ArrayList<String> getPlotsNotInList(String vsrNo, ArrayList<String> plotsList);

    public ArrayList<String> getRORVillageList(String[] level);

    public int getPlotsCount(String vsrNo);

    public int getAreaSum(String vsrNo);

    public abstract String validateDivision(String gisCode, String plotNo, String actualPlotNo);
    public int getLoginLevel();
    public abstract String getPlotInfoLinks(String gisCode, String plotNo);

    public abstract double getScaleFactor(String gisCode);

    public abstract String getScaleForPlotImage(String gisCode, String scale);

    public List<Map<String, Object>> levelRights(String module, String user, String gisCode);

    public abstract ArrayList<String> getVillagesList(String[] levels);

    public abstract ArrayList<String> getPlotsAreaListInVsr(String vsrNo);

    public abstract ArrayList<String> getKideForTheme(String query, String giscode, boolean isThemeLabel);

    public abstract String concatLevels(String levels);

    public abstract String getForwardSendbackGroup(BhunakshaUser user, String forwardSendback, String gisCode, String plotNo);

    public abstract boolean updateMutationDetails(BhunakshaUser user, String gisCode, String plotNo, String operationType,String divId);

    public abstract boolean hasVillageRight(BhunakshaUser user, String gisCode, String plotNo);

    public abstract void getRORTransactionList(BhunakshaUser user, String gisCode);

    public boolean isPlotNoValid(String gisCode, String plotNo);

    public abstract String getExtraInfoForSplit(String vsrno,String plotNo,String mutationNo);
    public abstract boolean checkMergePlotNo(String plotNo, String newPlotNo);

    public boolean isMutationNoValid(String mutationNo);

    String [] loginSSO(String gid);

    String GetNextMutationNo(String giscode);
}
