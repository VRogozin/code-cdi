package ru.lb.cppo.backend.data;

import javax.persistence.Entity;

@Entity
public class JiraUserRoleProject extends AbstractEntity {
    private String juserName;
    private String juserKey;
    private String juserDispName;
    private String projectName;
    private String juserRole;

    public JiraUserRoleProject(){
    }

    public JiraUserRoleProject(String juserName, String juserKey, String juserDispName, String projectName,
            String juserRole) {
        this.juserName = juserName;
        this.juserKey = juserKey;
        this.juserDispName = juserDispName;
        this.projectName = projectName;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getJuserKey() {
        return juserKey;
    }

    public void setJuserKey(String juserKey) {
        this.juserKey = juserKey;
    }

    public String getJuserName() {
        return juserName;
    }

    public void setJuserName(String juserName) {
        this.juserName = juserName;
    }

}
