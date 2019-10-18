package ru.lb.cppo.backend.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class PlainWorkLog {

    private BigDecimal workLogID;
    private BigDecimal issueID;
    private String author;
    private String updateAuthor;
    private String workLogBody;
    private Date created;
    private Date updated;
    private Date startDate;
    private BigDecimal timeWorked;
    private BigDecimal timeHours;

    public PlainWorkLog() {
    }

    public PlainWorkLog(BigDecimal workLogID, BigDecimal issueID, String author, String updateAuthor, String workLogBody, Date created, Date updated, Date startDate, BigDecimal timeWorked) {
        this.workLogID = workLogID;
        this.issueID = issueID;
        this.author = author;
        this.updateAuthor = updateAuthor;
        this.workLogBody = workLogBody;
        this.created = created;
        this.updated = updated;
        this.startDate = startDate;
        this.timeWorked = timeWorked;
        this.timeHours = timeWorked.divide(BigDecimal.valueOf(3600));
    }

    public BigDecimal getWorkLogID() {
        return workLogID;
    }

    public void setWorkLogID(BigDecimal workLogID) {
        this.workLogID = workLogID;
    }

    public BigDecimal getIssueID() {
        return issueID;
    }

    public void setIssueID(BigDecimal issueID) {
        this.issueID = issueID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUpdateAuthor() {
        return updateAuthor;
    }

    public void setUpdateAuthor(String updateAuthor) {
        this.updateAuthor = updateAuthor;
    }

    public String getWorkLogBody() {
        return workLogBody;
    }

    public void setWorkLogBody(String workLogBody) {
        this.workLogBody = workLogBody;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getTimeWorked() {
        return timeWorked;
    }

    public void setTimeWorked(BigDecimal timeWorked) {
        this.timeWorked = timeWorked;
    }

    public BigDecimal getTimeHours() {
        return timeHours;
    }

    public void setTimeHours(BigDecimal timeHours) {
        this.timeHours = timeHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlainWorkLog that = (PlainWorkLog) o;
        return Objects.equals(workLogID, that.workLogID);
    }

    @Override
    public int hashCode() {

        return Objects.hash(workLogID);
    }
}

