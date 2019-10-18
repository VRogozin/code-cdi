package ru.lb.cppo.backend.pojo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.text.DecimalFormat;


public class PlainProjectCode {

    private BigDecimal codeId;
    private String codeName;
    private String codeDescription;
    private Boolean isActive;

    public PlainProjectCode() {
        this.isActive=true;
    }

    public PlainProjectCode(BigDecimal id, String codeName) {
        this.codeId = id;
        this.codeName = codeName;
        this.isActive=true;
    }

    public PlainProjectCode(BigDecimal codeId, String codeName, String codeDescription, Boolean isActive) {
        this.codeId = codeId;
        this.codeName = codeName;
        this.codeDescription = codeDescription;
        this.isActive = isActive;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public BigDecimal getCodeId() {
        return codeId;
    }

    public void setCodeID(BigDecimal codeId) {
        this.codeId = codeId;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getCodeDescription() {
        return codeDescription;
    }

    public void setCodeDescription(String codeDescription) {
        this.codeDescription = codeDescription;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);

        return getCodeName() + " | " + df.format(getCodeId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof PlainProjectCode))
            return false;

        PlainProjectCode that = (PlainProjectCode) o;

        return new EqualsBuilder().append(getCodeId(), that.getCodeId()).append(getCodeName(), that.getCodeName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getCodeId()).append(getCodeName()).toHashCode();
    }
}
