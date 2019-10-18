package ru.lb.cppo.backend.pojo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;

public class PlainJiraProject {

    private BigDecimal projectId;
    private String projectName;
    private String projectDescription;
    private String smallCode;
    private Boolean isActive;

    public PlainJiraProject(BigDecimal projectId, String projectName, String projectDescription, Boolean isActive) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.isActive=isActive;
    }

    public PlainJiraProject(BigDecimal projectId, String projectName, String projectDescription, String smallCode, Boolean isActive) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.smallCode=smallCode;
        this.isActive=isActive;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public BigDecimal getProjectId() {
        return projectId;
    }

    public void setProjectId(BigDecimal projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getSmallCode() { return smallCode; }

    public void setSmallCode(String smallCode) { this.smallCode = smallCode; }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof PlainJiraProject))
            return false;

        PlainJiraProject that = (PlainJiraProject) o;

        return new EqualsBuilder().append(getProjectId(), that.getProjectId())
                .append(getProjectName(), that.getProjectName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getProjectId()).append(getProjectName()).toHashCode();
    }
}
