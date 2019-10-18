package ru.lb.cppo.backend.pojo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * PlainCodeVersion - класс для передачи данных
 */
public class PlainCodeVersion {

    private String projectCode;
    private BigDecimal versionID;
    private String projectVersion;

    public PlainCodeVersion() {
    }

    public PlainCodeVersion(String projectCode, BigDecimal versionID,  String projectVersion) {
        this.projectCode = projectCode;
        this.versionID=versionID;
        this.projectVersion = projectVersion;
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

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(0);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);

        return "PlainCodeVersion{"+ projectCode + ':' + df.format(versionID) + ":" + projectVersion + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof PlainCodeVersion))
            return false;

        PlainCodeVersion that = (PlainCodeVersion) o;

        return new EqualsBuilder().append(getProjectCode(), that.getProjectCode())
                .append(getProjectVersion(), that.getProjectVersion()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getProjectCode()).append(getProjectVersion()).toHashCode();
    }
}
