package ru.lb.cppo.backend.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class ProjectVersion implements Serializable {
    @Id
    private BigDecimal versionId;
    private String versionName;
    private BigDecimal jproject;
    private Date versionBegin;
    private Date releaseDate;
    private VersionStatusEnum status = VersionStatusEnum.PLANNED;
    private Boolean isActive;

    public ProjectVersion() {
        this.isActive=true;
    }

    public ProjectVersion(BigDecimal versionId, String versionName, BigDecimal jproject) {
        this.versionId = versionId;
        this.versionName = versionName;
        this.jproject = jproject;
        this.isActive=true;
    }

    public ProjectVersion(BigDecimal versionId, String versionName, BigDecimal jproject, Date versionBegin, Date releaseDate,  VersionStatusEnum status) {
        this.versionId = versionId;
        this.versionName = versionName;
        this.jproject = jproject;
        this.versionBegin = versionBegin;
        this.releaseDate = releaseDate;
        this.status = status;
        this.isActive=true;
    }

    public ProjectVersion(BigDecimal versionId, String versionName, BigDecimal jproject, Date versionBegin, Date releaseDate, String status) {
        this.versionId = versionId;
        this.versionName = versionName;
        this.jproject = jproject;
        this.versionBegin = versionBegin;
        this.releaseDate = releaseDate;
        this.status = VersionStatusEnum.fromString(status);
        this.isActive=true;
    }

    public ProjectVersion(BigDecimal versionId, String versionName, BigDecimal jproject, Date versionBegin,
            Date releaseDate, VersionStatusEnum status, Boolean isActive) {
        this.versionId = versionId;
        this.versionName = versionName;
        this.jproject = jproject;
        this.versionBegin = versionBegin;
        this.releaseDate = releaseDate;
        if (status == null) {status=VersionStatusEnum.PLANNED; }
        this.status = status;
        this.isActive = isActive;
    }

    public boolean isPersisted() {
        return getVersionId() != null;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public VersionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(VersionStatusEnum status) {
        this.status = status;
    }

    public BigDecimal getVersionId() {
        return versionId;
    }

    public void setVersionId(BigDecimal versionId) {
        this.versionId = versionId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public BigDecimal getJproject() {
        return jproject;
    }

    public void setJproject(BigDecimal jproject) {
        this.jproject = jproject;
    }

    public Date getVersionBegin() {
        return versionBegin;
    }

    public void setVersionBegin(Date versionBegin) {
        this.versionBegin = versionBegin;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectVersion that = (ProjectVersion) o;

        return versionId.equals(that.versionId) && versionName.equals(that.versionName) && (jproject != null ? jproject.equals(that.jproject) : that.jproject == null);
    }

    @Override
    public int hashCode() {
        int result = versionId.hashCode();
        result = 31 * result + versionName.hashCode();
        result = 31 * result + (jproject != null ? jproject.hashCode() : 0);
        return result;
    }
}
