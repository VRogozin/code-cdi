package ru.lb.cppo.backend.data;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class JiraErrorMessage extends AbstractEntity {

    private String projectCode;
    private BigDecimal versionID;
    private String projectVersion;
    private String projectName;
    private String issueNum;
    private BigDecimal issueID;
    private String errorCode;
    private String errMessage;

    public JiraErrorMessage() {
    }

    public JiraErrorMessage(String projectCode
            , BigDecimal versionID
            , String projectVersion
            , String projectName
            , String issueNum
            , BigDecimal issueID
            , String errorCode
            , String errMessage) {
        this.projectCode = projectCode;
        this.versionID=versionID;
        this.projectVersion = projectVersion;
        this.projectName = projectName;
        this.issueNum = issueNum;
        this.issueID = issueID;
        this.errorCode = errorCode;
        this.errMessage = errMessage;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public BigDecimal getVersionID() { return versionID; }

    public void setVersionID(BigDecimal versionID) { this.versionID = versionID; }

    public String getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getIssueNum() {
        return issueNum;
    }

    public void setIssueNum(String issueNum) {
        this.issueNum = issueNum;
    }

    public BigDecimal getIssueID() {
        return issueID;
    }

    public void setIssueID(BigDecimal issueID) {
        this.issueID = issueID;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

}
