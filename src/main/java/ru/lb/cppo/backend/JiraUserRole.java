package ru.lb.cppo.backend;

import java.io.Serializable;

public class JiraUserRole implements Serializable {
    private static final long serialVersionUID = -497454270457295613L;
    private String juserDispName;
    private String juserRole;
    private String juserKey;
    private String juserName;

    public JiraUserRole(String juserDispName, String juserRole, String juserKey, String juserName) {
        this.juserDispName = juserDispName;
        this.juserRole = juserRole;
        this.juserKey = juserKey;
        this.juserName = juserName;
    }

    public JiraUserRole(String juserName, String juserRole) {
        this.juserDispName = "";
        this.juserRole = juserRole;
        this.juserKey = "";
        this.juserName = juserName;
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
