package in.nic.lrisd.bhunakshav6.bhunakshacommon.bean;

import javax.validation.constraints.Pattern;
import java.util.Date;

public class BhunakshaUser {

    private String userId;
    @Pattern(regexp = ".[^<>/&\\'\"]*")
    private String userName;
    private String group;
    private boolean loged = false;
    private String token;
    private boolean admin = false;
    private String extraInfo;
    private String logedLevel;
    private String errMsg = "";
    private boolean pwdReset = false;
    private String eMail;
    private String contactNumber;
    private String id;
    private String villageCodeList;
    private Date lastUpdated;
    private String userGroup;
    private String password;
    private String loginLevel;
    private boolean dscExists = false;
    private String dscStatus;
    private boolean serviceRunning = false;
    private int loginFailedCount = 0;
    private String uId;

    public int getLoginFailedCount () {
        return loginFailedCount;
    }

    public void setLoginFailedCount(int loginFailedCount) {
        this.loginFailedCount = loginFailedCount;
    }

    public boolean isServiceRunning() {
        return serviceRunning;
    }

    public void setServiceRunningStatus(boolean serviceRunning) {
        this.serviceRunning = serviceRunning;
    }

    public BhunakshaUser() {

    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isLoged() {
        return loged;
    }

    public void setLoged(boolean loged) {
        this.loged = loged;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getLogedLevel() {
        return logedLevel;
    }

    public void setLogedLevel(String logedLevel) {
        this.logedLevel = logedLevel;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public boolean ispwdReset() {
        return pwdReset;
    }

    public void setpwdReset(boolean pwdReset) {
        this.pwdReset = pwdReset;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVillageCodeList() {
        return villageCodeList;
    }

    public void setVillageCodeList(String villageCodeList) {
        this.villageCodeList = villageCodeList;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginLevel() {
        return loginLevel;
    }

    public void setLoginLevel(String loginLevel) {
        this.loginLevel = loginLevel;
    }

    public boolean getdscExists() {
        return dscExists;
    }

    public void setdscExists(boolean dsc) {
        this.dscExists = dsc;
    }

    public String getdscStatus() {
        return dscStatus;
    }

    public void setdscStatus(String dsc) {
        this.dscStatus = dsc;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
