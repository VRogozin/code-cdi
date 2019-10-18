package ru.lb.cppo.backend.pojo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import ru.lb.cppo.backend.data.VersionStatusEnum;

import java.math.BigDecimal;
import java.util.Date;

public class PlainProjectVersion {

    private BigDecimal versionId;
    private String versionName;
    private BigDecimal jproject;
    private Date versionBegin;
    private Date releaseDate;
    private VersionStatusEnum status = VersionStatusEnum.PLANNED;
    private Boolean isActive;

    public PlainProjectVersion() {
        this.isActive=true;
    }

    public PlainProjectVersion(BigDecimal versionId, String versionName, BigDecimal jproject) {
        this.versionId = versionId;
        this.versionName = versionName;
        this.jproject = jproject;
        this.isActive=true;
    }

    public PlainProjectVersion(BigDecimal versionId, String versionName, BigDecimal jproject, Date versionBegin, Date releaseDate,  VersionStatusEnum status) {
        this.versionId = versionId;
        this.versionName = versionName;
        this.jproject = jproject;
        this.versionBegin = versionBegin;
        this.releaseDate = releaseDate;
        this.status = status;
        this.isActive=true;
    }

    public PlainProjectVersion(BigDecimal versionId, String versionName, BigDecimal jproject, Date versionBegin, Date releaseDate,  String status) {
        this.versionId = versionId;
        this.versionName = versionName;
        this.jproject = jproject;
        this.versionBegin = versionBegin;
        this.releaseDate = releaseDate;
        this.status = VersionStatusEnum.fromString(status);
        this.isActive=true;
    }

    public PlainProjectVersion(BigDecimal versionId, String versionName, BigDecimal jproject, Date versionBegin,
            Date releaseDate, VersionStatusEnum status, Boolean isActive) {
        this.versionId = versionId;
        this.versionName = versionName;
        this.jproject = jproject;
        this.versionBegin = versionBegin;
        this.releaseDate = releaseDate;
        this.status = status;
        this.isActive = isActive;
    }

    public PlainProjectVersion(BigDecimal versionId, String versionName, BigDecimal jproject, Date versionBegin,
            Date releaseDate, String status, Boolean isActive) {
        this.versionId = versionId;
        this.versionName = versionName;
        this.jproject = jproject;
        this.versionBegin = versionBegin;
        this.releaseDate = releaseDate;
        this.status = VersionStatusEnum.fromString(status);
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
        if (this == o)
            return true;

        if (!(o instanceof PlainProjectVersion))
            return false;

        PlainProjectVersion that = (PlainProjectVersion) o;

        return new EqualsBuilder().append(getVersionId(), that.getVersionId())
                .append(getVersionName(), that.getVersionName()).append(getJproject(), that.getJproject()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getVersionId()).append(getVersionName()).append(getJproject())
                .toHashCode();
    }
}
