package ru.lb.cppo.backend;

import ru.lb.cppo.backend.data.VersionStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;

public class JiraProjectExecution implements Serializable {
    private String projectCode;
    private BigDecimal projectID;
    private String projectMnem;
    private BigDecimal versionID;
    private String projectVersion;
    private String workRole;
    private BigDecimal timeOrig;
    private BigDecimal timeSpn;
    private BigDecimal timeEst;
    private BigDecimal prozentRealize;
    private BigDecimal deltaRealize;
    private BigDecimal prozentDelta;
    private VersionStatusEnum versionStatus;

    public JiraProjectExecution() {
    }

    public JiraProjectExecution(String projectCode, BigDecimal projectID, String projectMnem, BigDecimal versionID,
            String projectVersion, String workRole, BigDecimal timeOrig, BigDecimal timeSpn, BigDecimal timeEst,
            BigDecimal prozentRealize, BigDecimal deltaRealize, BigDecimal prozentDelta,
            VersionStatusEnum versionStatus) {
        this.projectCode = projectCode;
        this.projectID = projectID;
        this.projectMnem = projectMnem;
        this.versionID = versionID;
        this.projectVersion = projectVersion;
        this.workRole = workRole;
        this.timeOrig = timeOrig;
        this.timeSpn = timeSpn;
        this.timeEst = timeEst;
        this.prozentRealize = prozentRealize;
        this.deltaRealize = deltaRealize;
        this.prozentDelta = prozentDelta;
        this.versionStatus = versionStatus;
    }

    public JiraProjectExecution(String projectCode, BigDecimal projectID, String projectMnem, BigDecimal versionID,
            String projectVersion, String workRole, BigDecimal timeOrig, BigDecimal timeSpn, BigDecimal timeEst,
            BigDecimal prozentRealize, BigDecimal deltaRealize, BigDecimal prozentDelta,
            String versionStatus) {
        this.projectCode = projectCode;
        this.projectID = projectID;
        this.projectMnem = projectMnem;
        this.versionID = versionID;
        this.projectVersion = projectVersion;
        this.workRole = workRole;
        this.timeOrig = timeOrig;
        this.timeSpn = timeSpn;
        this.timeEst = timeEst;
        this.prozentRealize = prozentRealize;
        this.deltaRealize = deltaRealize;
        this.prozentDelta = prozentDelta;
        this.versionStatus = VersionStatusEnum.fromString(versionStatus);
    }

    public JiraProjectExecution(String projectCode, String projectVersion, String workRole, BigDecimal timeOrig, BigDecimal timeSpn, BigDecimal timeEst, BigDecimal prozentRealize, BigDecimal deltaRealize, BigDecimal prozentDelta, String versionStatus) {
        this.projectCode = projectCode;
        this.projectVersion = projectVersion;
        this.workRole = workRole;
        this.timeOrig = timeOrig;
        this.timeSpn = timeSpn;
        this.timeEst = timeEst;
        this.prozentRealize = prozentRealize;
        this.deltaRealize = deltaRealize;
        this.prozentDelta = prozentDelta;
        this.versionStatus = VersionStatusEnum.fromString(versionStatus);
    }

    public JiraProjectExecution(String projectCode, String projectVersion, String workRole, BigDecimal timeOrig, BigDecimal timeSpn, BigDecimal timeEst, BigDecimal prozentRealize, BigDecimal deltaRealize, BigDecimal prozentDelta, VersionStatusEnum versionStatus) {
        this.projectCode = projectCode;
        this.projectVersion = projectVersion;
        this.workRole = workRole;
        this.timeOrig = timeOrig;
        this.timeSpn = timeSpn;
        this.timeEst = timeEst;
        this.prozentRealize = prozentRealize;
        this.deltaRealize = deltaRealize;
        this.prozentDelta = prozentDelta;
        this.versionStatus = versionStatus;
    }


    public JiraProjectExecution(String projectCode, String projectVersion, String workRole, BigDecimal timeOrig, BigDecimal timeSpn, BigDecimal timeEst) {
        this.projectCode = projectCode;
        this.projectVersion = projectVersion;
        this.workRole = workRole;
        this.timeOrig = timeOrig;
        this.timeSpn = timeSpn;
        this.timeEst = timeEst;
        this.versionStatus=VersionStatusEnum.PLANNED;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public BigDecimal getProjectID() {
        return projectID;
    }

    public void setProjectID(BigDecimal projectID) {
        this.projectID = projectID;
    }

    public String getProjectMnem() {
        return projectMnem;
    }

    public void setProjectMnem(String projectMnem) {
        this.projectMnem = projectMnem;
    }

    public BigDecimal getVersionID() {
        return versionID;
    }

    public void setVersionID(BigDecimal versionID) {
        this.versionID = versionID;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    public String getWorkRole() {
        return workRole;
    }

    public void setWorkRole(String workRole) {
        this.workRole = workRole;
    }

    public BigDecimal getTimeOrig() {
        return timeOrig;
    }

    public void setTimeOrig(BigDecimal timeOrig) {
        this.timeOrig = timeOrig;
    }

    public BigDecimal getTimeSpn() { return timeSpn;  }

    public void setTimeSpn(BigDecimal timeSpn) {
        this.timeSpn = timeSpn;
    }

    public BigDecimal getTimeEst() {
        return timeEst;
    }

    public void setTimeEst(BigDecimal timeEst) {
        this.timeEst = timeEst;
    }

    public BigDecimal getProzentRealize() {
        return prozentRealize;
    }

    public void setProzentRealize(BigDecimal prozentRealize) {
        this.prozentRealize = prozentRealize;
    }

    public BigDecimal getDeltaRealize() {
        return deltaRealize;
    }

    public void setDeltaRealize(BigDecimal deltaRealize) {
        this.deltaRealize = deltaRealize;
    }

    public BigDecimal getProzentDelta() {
        return prozentDelta;
    }

    public void setProzentDelta(BigDecimal prozentDelta) {
        this.prozentDelta = prozentDelta;
    }

    public VersionStatusEnum getVersionStatus() {
        return versionStatus;
    }

    public void setVersionStatus(VersionStatusEnum versionStatus) {
        this.versionStatus = versionStatus;
    }

    public void setVersionStatus(String versionStatus) {
        this.versionStatus = VersionStatusEnum.fromString(versionStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JiraProjectExecution that = (JiraProjectExecution) o;

        if (!projectCode.equals(that.projectCode)) return false;
        if (!projectVersion.equals(that.projectVersion)) return false;
        if (!workRole.equals(that.workRole)) return false;
        if (timeOrig != null ? !timeOrig.equals(that.timeOrig) : that.timeOrig != null) return false;
        if (timeSpn != null ? !timeSpn.equals(that.timeSpn) : that.timeSpn != null) return false;
        return timeEst != null ? timeEst.equals(that.timeEst) : that.timeEst == null;
    }

    @Override
    public int hashCode() {
        int result = projectCode.hashCode();
        result = 31 * result + projectVersion.hashCode();
        result = 31 * result + workRole.hashCode();
        result = 31 * result + (timeOrig != null ? timeOrig.hashCode() : 0);
        result = 31 * result + (timeSpn != null ? timeSpn.hashCode() : 0);
        result = 31 * result + (timeEst != null ? timeEst.hashCode() : 0);
        return result;
    }
}
