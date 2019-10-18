package ru.lb.cppo.backend.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@Entity
public class JiraProject implements Serializable {

    private static final long serialVersionUID = 165727656751L;

    @Id
    private BigDecimal projectId;
    private String projectName;
    private String projectDescription;
    private String shortCode;
    private Boolean isActive;

    public JiraProject() {
        this.isActive=true;
    }

    public JiraProject(BigDecimal projectId, String projectName, String projectDescription) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.isActive=true;
    }

    public JiraProject(BigDecimal projectId, String projectName, String projectDescription, Boolean isActive) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.isActive=isActive;
    }

    public JiraProject(BigDecimal projectId, String projectName, String projectDescription, String shortCode, Boolean isActive) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.shortCode=shortCode;
        this.isActive=isActive;
    }

    public JiraProject(BigDecimal projectId, String projectName) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.isActive=true;
    }

    public Boolean isPersisted() {
        return getProjectId() !=null;
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

    public String getProjectIdAsString() {
        final DecimalFormat decimalFormat = new DecimalFormat("#");
        return decimalFormat.format(getProjectId());
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

    public String getShortCode() { return shortCode; }

    public void setShortCode(String shortCode) { this.shortCode = shortCode;  }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JiraProject that = (JiraProject) o;
        return projectId.equals(that.projectId);
    }

    @Override
    public int hashCode() {
        return projectId.hashCode();
    }
}
