package ru.lb.cppo.ui;

import java.io.Serializable;

public class SettingsLogic implements Serializable {
    private SettingsView view;

    public SettingsLogic(SettingsView settingsView) {
        view = settingsView;
    }
    public void init(){
/*
        if (!MyUI.get().getAccessControl().isUserInRole("admin")) {
            view.setNewProductEnabled(false);
        }
*/
    }
}
