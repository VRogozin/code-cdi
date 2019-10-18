package ru.lb.cppo.backend.data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="code_project_report")
public class ReportRow extends AbstractEntity {
    private static final long serialVersionUID = 16577896789623L;

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

	public ReportRow(){
    }

    public ReportRow(String projectCode, String projectVersion, String workRole){
        this.projectCode=projectCode;
        this.projectVersion=projectVersion;
        this.workRole=workRole;
        this.versionStatus=VersionStatusEnum.PLANNED;
    }

    public ReportRow(String projectCode, BigDecimal projectID, String projectMnem, BigDecimal versionID,
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

    public ReportRow(String projectCode, BigDecimal projectID, String projectMnem, BigDecimal versionID,
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

    public ReportRow(String projectCode, String projectVersion, String workRole, BigDecimal timeOrig, BigDecimal timeSpn, BigDecimal timeEst, BigDecimal prozentRealize, BigDecimal deltaRealize, BigDecimal prozentDelta, String versionStatus) {
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

    public ReportRow(String projectCode, String projectVersion, String workRole, BigDecimal timeOrig, BigDecimal timeSpn, BigDecimal timeEst, BigDecimal prozentRealize, BigDecimal deltaRealize, BigDecimal prozentDelta, VersionStatusEnum versionStatus) {
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

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public BigDecimal getProjectID() { return projectID; }

    public void setProjectID(BigDecimal projectID) { this.projectID = projectID; }

    public String getProjectMnem() { return projectMnem; }

    public void setProjectMnem(String projectMnem) { this.projectMnem = projectMnem; }

    public BigDecimal getVersionID() { return versionID; }

    public void setVersionID(BigDecimal versionID) { this.versionID = versionID; }

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

    public BigDecimal getTimeEst() {
        return timeEst;
    }

    public void setTimeEst(BigDecimal timeEst) {
        this.timeEst = timeEst;
    }

    public BigDecimal getTimeSpn() {
        return timeSpn;
    }

    public void setTimeSpn(BigDecimal timeSpn) {
        this.timeSpn = timeSpn;
    }

    public BigDecimal getProzentRealize() {
        return prozentRealize;
    }
    public Double getProzentRealizeAsDouble() {return prozentRealize.doubleValue();}

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
    public Double getProzentDeltaAsDouble() {
        return prozentDelta.doubleValue();
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

}
