package ru.lb.cppo.backend.pojo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class SprProjectVersion {

    private BigDecimal versionId;
    private String versionName;
    private String projectName;
    private String projectDescription;

    public SprProjectVersion() {
    }

    public SprProjectVersion(BigDecimal versionId, String versionName, String projectName,
            String projectDescription) {
        this.versionId = versionId;
        this.versionName = versionName;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
    }

    public BigDecimal getVersionId() {
        return versionId;
    }
    public String getVersionIdAsString() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(0);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);
        return df.format(versionId);
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

    @Override
    public String toString(){
        return new StringBuilder()
                .append("ID :").append(versionId.toString())
                .append(": Версия :").append(versionName)
                .append(": Проект :").append(projectName)
                .append(": Описание :").append(projectDescription)
                .append(":")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof SprProjectVersion))
            return false;

        SprProjectVersion that = (SprProjectVersion) o;

        return new EqualsBuilder().append(getVersionIdAsString(), that.getVersionIdAsString())
                .append(getVersionName(), that.getVersionName()).append(getProjectName(), that.getProjectName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getVersionIdAsString()).append(getVersionName()).append(getProjectName())
                .toHashCode();
    }
}
