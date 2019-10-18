package ru.lb.cppo.backend.data;

import ru.lb.cppo.util.BigDecimalConverter;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

@Entity
public class CodeAccounting extends AbstractEntity {
    private String projectCode;
    private BigDecimal versionID;
    private String projectVersion;
    private String accDescription;
    private BigDecimal cleanCost;
    private BigDecimal riskCost;
    private BigDecimal adminCost;
    private BigDecimal guaranteeCost;
    private BigDecimal totalCost;
    private Date dateCreated;
    private Date dateModified;
    private Boolean isActive;

    public CodeAccounting() {
    }

    public CodeAccounting(String projectCode, String projectVersion) {
        this.projectCode = projectCode;
        this.projectVersion = projectVersion;
        this.cleanCost= BigDecimal.ZERO;
        this.riskCost=BigDecimal.ZERO;
        this.adminCost=BigDecimal.ZERO;
        this.guaranteeCost=BigDecimal.ZERO;
        this.totalCost=BigDecimal.ZERO;
        this.isActive=true;
    }

    public CodeAccounting(String projectCode, BigDecimal versionID, String projectVersion) {
        this.projectCode = projectCode;
        this.versionID = versionID;
        this.projectVersion = projectVersion;
        this.cleanCost= BigDecimal.ZERO;
        this.riskCost=BigDecimal.ZERO;
        this.adminCost=BigDecimal.ZERO;
        this.guaranteeCost=BigDecimal.ZERO;
        this.totalCost=BigDecimal.ZERO;
        this.isActive=true;
    }

    public CodeAccounting(String projectCode, BigDecimal versionID,
            String projectVersion, String accDescription, BigDecimal cleanCost, BigDecimal riskCost,
            BigDecimal adminCost, BigDecimal guaranteeCost, BigDecimal totalCost, Date dateCreated, Date dateModified,
            Boolean isActive) {
        this.projectCode = projectCode;
        this.versionID = versionID;
        this.projectVersion = projectVersion;
        this.accDescription = accDescription;
        this.cleanCost = cleanCost;
        this.riskCost = riskCost;
        this.adminCost = adminCost;
        this.guaranteeCost = guaranteeCost;
        this.totalCost = totalCost;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.isActive = isActive;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public BigDecimal getVersionID() {
        return versionID;
    }

    public void setVersionID(BigDecimal versionID) {
        this.versionID = versionID;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    public String getAccDescription() {
        return accDescription;
    }

    public void setAccDescription(String accDescription) {
        this.accDescription = accDescription;
    }

    public BigDecimal getCleanCost() {
        return cleanCost;
    }

    public void setCleanCost(BigDecimal cleanCost) {
        this.cleanCost = cleanCost;
    }

    public void setCleanCost(String cleanCost) {
        try {
            this.cleanCost = new BigDecimalConverter().stringToDecimal(cleanCost);
        } catch (ParseException e) {
        }
    }

    public BigDecimal getRiskCost() {
        return riskCost;
    }

    public void setRiskCost(BigDecimal riskCost) {
        this.riskCost = riskCost;
    }

    public void setRiskCost(String riskCost) {
        try {
            this.riskCost = new BigDecimalConverter().stringToDecimal(riskCost);
        } catch (ParseException e) {
        }
    }

    public BigDecimal getAdminCost() {
        return adminCost;
    }

    public void setAdminCost(BigDecimal adminCost) {
        this.adminCost = adminCost;
    }

    public void setAdminCost(String adminCost) {
        try {
            this.adminCost = new BigDecimalConverter().stringToDecimal(adminCost);
        } catch (ParseException e) {
        }
    }

    public BigDecimal getGuaranteeCost() {
        return guaranteeCost;
    }

    public void setGuaranteeCost(BigDecimal guaranteeCost) {
        this.guaranteeCost = guaranteeCost;
    }
    public void setGuaranteeCost(String guaranteeCost) {
        try {
            this.guaranteeCost = new BigDecimalConverter().stringToDecimal(guaranteeCost);
        } catch (ParseException e) {
        }
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void setTotalCost(String totalCost) {
        try {
            this.totalCost = new BigDecimalConverter().stringToDecimal(totalCost);
        } catch (ParseException e) {
        }
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

}