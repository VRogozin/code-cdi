package ru.lb.cppo.backend.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import ru.lb.cppo.util.BigDecimalConverter;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.text.ParseException;

@Entity
public class ProjectCodePlan extends AbstractEntity{
    private String projectCode;
    private BigDecimal versionID;
    private String projectVersion;
    private String workRole;
    private BigDecimal planOrig;
    private BigDecimal factOrig;
    private VersionStatusEnum versionStatus;

    public ProjectCodePlan() {
    }

    public ProjectCodePlan(String projectCode, String projectVersion, String workRole, BigDecimal planOrig) {
        this.projectCode = projectCode;
        this.projectVersion = projectVersion;
        this.workRole = workRole;
        this.planOrig = planOrig;
        this.factOrig=BigDecimal.ZERO;
        this.versionStatus=VersionStatusEnum.PLANNED;
    }

    public ProjectCodePlan(String projectCode, String projectVersion, String workRole, BigDecimal planOrig, BigDecimal factOrig, String versionStatus) {
        this.projectCode = projectCode;
        this.projectVersion = projectVersion;
        this.workRole = workRole;
        this.planOrig = planOrig;
        this.factOrig = factOrig;
        this.versionStatus=VersionStatusEnum.fromString(versionStatus);
    }

    public ProjectCodePlan(String projectCode, BigDecimal versionID, String projectVersion, String workRole, BigDecimal planOrig, BigDecimal factOrig, String versionStatus) {
        this.projectCode = projectCode;
        this.versionID=versionID;
        this.projectVersion = projectVersion;
        this.workRole = workRole;
        this.planOrig = planOrig;
        this.factOrig = factOrig;
        this.versionStatus=VersionStatusEnum.fromString(versionStatus);
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public BigDecimal getVersionID() { return versionID; }

    public void setVersionID(BigDecimal versionID) { this.versionID = versionID; }

    public void setVersionID(String versionID) {
        try {
            this.versionID = new BigDecimalConverter().stringToDecimal(versionID);
        } catch (ParseException e) {
        }
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

    public BigDecimal getPlanOrig() {
        return planOrig;
    }

    public void setPlanOrig(BigDecimal planOrig) {
        this.planOrig = planOrig;
    }

    public void setPlanOrig(String planOrig) {
        try {
            this.planOrig = new BigDecimalConverter().stringToDecimal(planOrig);
        } catch (ParseException e) {
        }
    }

    public BigDecimal getFactOrig() {
        return factOrig;
    }

    public void setFactOrig(BigDecimal factOrig) {
        this.factOrig=factOrig;
    }

    public VersionStatusEnum getVersionStatus() { return versionStatus; }

    public void setVersionStatus(VersionStatusEnum versionStatus) {
        this.versionStatus = versionStatus;
    }

    public void setVersionStatus(String versionStatus) {
        this.versionStatus = VersionStatusEnum.fromString(versionStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof ProjectCodePlan))
            return false;

        ProjectCodePlan that = (ProjectCodePlan) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(getProjectCode(), that.getProjectCode())
                .append(getVersionID(), that.getVersionID()).append(getWorkRole(), that.getWorkRole()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(getProjectCode()).append(getVersionID())
                .append(getWorkRole()).toHashCode();
    }
}
