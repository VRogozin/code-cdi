package ru.lb.cppo.ui;

class SettingsView extends SettingsFormDesign {

    private SettingsLogic viewLogic;
    public SettingsView(SettingsLogic settingsLogic) {
        super();
        addStyleName("product-form");
        viewLogic = settingsLogic;
    }

}
