package ru.lb.cppo.backend.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

@Entity
public class ProjectCode implements Serializable {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigDecimal codeId;
    private String codeName;
    private String codeDescription;
    private String smallCode;
    private Boolean isActive;

    public ProjectCode() {
        this.isActive=true;
    }

    public ProjectCode(BigDecimal id, String codeName) {
        this.codeId = id;
        this.codeName = codeName;
        this.isActive=true;
    }

    public ProjectCode(BigDecimal codeId, String codeName, String codeDescription, Boolean isActive) {
        this.codeId = codeId;
        this.codeName = codeName;
        this.codeDescription = codeDescription;
        this.isActive = isActive;
    }

    public Boolean isPersisted() {
        return getCodeId() !=null;
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

    public String getSmallCode() { return smallCode; }

    public void setSmallCode(String smallCode) { this.smallCode = smallCode; }

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectCode that = (ProjectCode) o;

        return codeId.equals(that.codeId);
    }

    @Override
    public int hashCode() {
        int result = codeId.hashCode();
        result = 31 * result + codeName.hashCode();
        return result;
    }
}
