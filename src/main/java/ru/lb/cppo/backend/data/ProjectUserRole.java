package ru.lb.cppo.backend.data;

import javax.persistence.Entity;

@Entity
public class ProjectUserRole extends AbstractEntity{
    private String juserName;
    private String userDispName;
    private String roleName;
    private Boolean isActive;

    public ProjectUserRole() {
        this.isActive=true;
    }

    public ProjectUserRole(String juserName, String userDispName, String roleName, Boolean isActive) {
        this.juserName = juserName;
        this.userDispName = userDispName;
        this.roleName = roleName;
        if (roleName.compareTo("-")==0) {
            this.isActive = false;
        } else {
            this.isActive = isActive;
        }
    }

    public ProjectUserRole(String juserName, String userDispName, String roleName) {
        this.juserName = juserName;
        this.userDispName = userDispName;
        this.roleName = roleName;
        if (roleName.compareTo("-")==0) {
            this.isActive = false;
        } else {
            this.isActive = true;
        }
    }

    public String getJuserName() {
        return juserName;
    }

    public void setJuserName(String juserName) {
        this.juserName = juserName;
    }

    public String getUserDispName() {
        return userDispName;
    }

    public void setUserDispName(String userDispName) {
        this.userDispName = userDispName;
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
