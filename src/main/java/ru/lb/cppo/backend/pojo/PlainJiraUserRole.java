package ru.lb.cppo.backend.pojo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class PlainJiraUserRole {
    private String juserName;
    private String juserDispName;
    private String juserRole;

    public PlainJiraUserRole(String juserName, String juserDispName, String juserRole) {
        this.juserName = juserName;
        this.juserDispName = juserDispName;
        this.juserRole = juserRole;
    }

    public String getJuserDispName() {
        return juserDispName;
    }

    public void setJuserDispName(String juserDispName) {
        this.juserDispName = juserDispName;
    }

    public String getJuserRole() {
        return juserRole;
    }

    public void setJuserRole(String juserRole) {
        this.juserRole = juserRole;
    }

    public String getJuserName() {
        return juserName;
    }

    public void setJuserName(String juserName) {
        this.juserName = juserName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof PlainJiraUserRole))
            return false;

        PlainJiraUserRole that = (PlainJiraUserRole) o;

        return new EqualsBuilder().append(getJuserName(), that.getJuserName())
                .append(getJuserDispName(), that.getJuserDispName()).append(getJuserRole(), that.getJuserRole())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getJuserName()).append(getJuserDispName()).append(getJuserRole())
                .toHashCode();
    }
}
