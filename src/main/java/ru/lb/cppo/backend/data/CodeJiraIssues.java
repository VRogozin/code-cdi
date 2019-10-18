package ru.lb.cppo.backend.data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="code_issue_prepared")
public class CodeJiraIssues extends AbstractEntity {
    private String RecordType;
    private BigDecimal IssueID;
    private BigDecimal ProjectID;
    private String ProjectCode;
    private String ProjectMnem;
    private String ProjectName;
    private BigDecimal versionID;
    private String ProjectVersion;
    private VersionStatusEnum versionStatus;
    private String PriorityStr;
    private String jissuetype;
    private String IssueNum;
    private String jIssueStatus;
    private String Asigned;
    private BigDecimal TimeOrig;
    private BigDecimal TimeSpn;
    private BigDecimal TimeEst;
    private String BuildNum;
    private String URL;
    private String WorkRole;
    private String ProjectPrev;
    private String ProjectSubst;

    public CodeJiraIssues() {
    }

    public CodeJiraIssues(String recordType
            , BigDecimal issueID
            , BigDecimal projectID
            , String projectCode
            , String projectMnem
            , String projectName
            , BigDecimal versionID
            , String projectVersion
            , String versionStatus
            , String priorityStr
            , String jissuetype
            , String issueNum
            , String jIssueStatus
            , String asigned
            , BigDecimal timeOrig
            , BigDecimal timeSpn
            , BigDecimal timeEst
            , String buildNum
            , String URL
            , String workRole
            , String projectPrev
            , String projectSubst) {
        this.RecordType = recordType;
        this.IssueID = issueID;
        this.ProjectID = projectID;
        this.ProjectCode = projectCode;
        this.ProjectMnem = projectMnem;
        this.ProjectName = projectName;
        this.versionID=versionID;
        this.ProjectVersion = projectVersion;
        this.versionStatus=VersionStatusEnum.fromString(versionStatus);
        this.PriorityStr = priorityStr;
        this.jissuetype = jissuetype;
        this.IssueNum = issueNum;
        this.jIssueStatus = jIssueStatus;
        this.Asigned = asigned;
        this.TimeOrig = timeOrig;
        this.TimeSpn = timeSpn;
        this.TimeEst = timeEst;
        this.BuildNum = buildNum;
        this.URL = URL;
        this.WorkRole = workRole;
        this.ProjectPrev = projectPrev;
        this.ProjectSubst = projectSubst;
    }

    public String getRecordType() {
        return RecordType;
    }

    public void setRecordType(String recordType) {
        RecordType = recordType;
    }

    public BigDecimal getIssueID() {
        return IssueID;
    }

    public void setIssueID(BigDecimal issueID) {
        IssueID = issueID;
    }

    public BigDecimal getProjectID() {
        return ProjectID;
    }

    public void setProjectID(BigDecimal projectID) {
        ProjectID = projectID;
    }

    public String getProjectCode() {
        return ProjectCode;
    }

    public void setProjectCode(String projectCode) {
        this.ProjectCode = projectCode;
    }

    public String getProjectMnem() {
        return ProjectMnem;
    }

    public void setProjectMnem(String projectMnem) {
        ProjectMnem = projectMnem;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public BigDecimal getVersionID() {
        return versionID;
    }

    public void setVersionID(BigDecimal versionID) {
        this.versionID = versionID;
    }

    public String getProjectVersion() {
        return ProjectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        ProjectVersion = projectVersion;
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

    public String getPriorityStr() {
        return PriorityStr;
    }

    public void setPriorityStr(String priorityStr) {
        PriorityStr = priorityStr;
    }

    public String getJissuetype() {
        return jissuetype;
    }

    public void setJissuetype(String jissuetype) {
        this.jissuetype = jissuetype;
    }

    public String getIssueNum() {
        return IssueNum;
    }

    public void setIssueNum(String issueNum) {
        IssueNum = issueNum;
    }

    public String getjIssueStatus() {
        return jIssueStatus;
    }

    public void setjIssueStatus(String jIssueStatus) {
        this.jIssueStatus = jIssueStatus;
    }

    public String getAsigned() {
        return Asigned;
    }

    public void setAsigned(String asigned) {
        Asigned = asigned;
    }

    public BigDecimal getTimeOrig() {
        return TimeOrig;
    }

    public void setTimeOrig(BigDecimal timeOrig) {
        TimeOrig = timeOrig;
    }

    public BigDecimal getTimeEst() {
        return TimeEst;
    }

    public void setTimeEst(BigDecimal timeEst) {
        TimeEst = timeEst;
    }

    public BigDecimal getTimeSpn() { return TimeSpn; }

    public void setTimeSpn(BigDecimal timeSpn) {
        TimeSpn = timeSpn;
    }

    public String getBuildNum() {
        return BuildNum;
    }

    public void setBuildNum(String buildNum) {
        BuildNum = buildNum;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getWorkRole() {
        return WorkRole;
    }

    public void setWorkRole(String workRole) {
        WorkRole = workRole;
    }

    public String getProjectPrev() {
        return ProjectPrev;
    }

    public void setProjectPrev(String projectPrev) {
        ProjectPrev = projectPrev;
    }

    public String getProjectSubst() {
        return ProjectSubst;
    }

    public void setProjectSubst(String projectSubst) {
        ProjectSubst = projectSubst;
    }
}
