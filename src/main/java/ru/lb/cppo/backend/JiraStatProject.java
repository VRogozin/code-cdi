package ru.lb.cppo.backend;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class JiraStatProject implements Serializable {
    private BigDecimal projectID;
    private String projectMnemonic;
    private String projectName;
    private String projectShortCode;

    public JiraStatProject(BigDecimal projectID, String projectMnemonic, String projectName, String projectShortCode) {
        this.projectID = projectID;
        this.projectMnemonic = projectMnemonic;
        this.projectName = projectName;
        this.projectShortCode = projectShortCode;
    }

    public BigDecimal getProjectID() {
        return projectID;
    }

    public void setProjectID(BigDecimal projectID) {
        this.projectID = projectID;
    }

    public String getProjectMnemonic() {
        return projectMnemonic;
    }

    public void setProjectMnemonic(String projectMnemonic) {
        this.projectMnemonic = projectMnemonic;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectShortCode() {
        return projectShortCode;
    }

    public void setProjectShortCode(String projectShortCode) {
        this.projectShortCode = projectShortCode;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);
        return "JiraStatProject{" +
                "projectID=" + df.format(projectID) +
                ", projectMnemonic='" + projectMnemonic + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectShortCode='" + projectShortCode + '\'' +
                '}';
    }

}
