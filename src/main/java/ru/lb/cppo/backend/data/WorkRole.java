package ru.lb.cppo.backend.data;

import javax.persistence.Entity;

@Entity
public class WorkRole extends AbstractEntity {
    private String roleName;
    private Boolean isActive =true;

    public WorkRole() {
    }

    public WorkRole(String roleName) { this.roleName = roleName;
    }

    public WorkRole(String roleName, Boolean isActive) {
        this.roleName = roleName;
        this.isActive = isActive;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
