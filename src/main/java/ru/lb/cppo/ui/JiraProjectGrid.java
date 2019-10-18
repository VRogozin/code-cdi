package ru.lb.cppo.ui;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import ru.lb.cppo.backend.data.JiraProject;

public class JiraProjectGrid extends Grid<JiraProject> {
    public JiraProjectGrid() {

        CheckBox isShadowCheck = new CheckBox();
        TextField descEditor = new TextField();
        TextField shortCodeEditor = new TextField();

        addColumn(JiraProject::getProjectId).setCaption("ИД проекта").setExpandRatio(2);
        addColumn(JiraProject::getProjectName).setCaption("Шифр проекта").setExpandRatio(2);
        addColumn(JiraProject::getProjectDescription).setCaption("Описание проекта").setExpandRatio(2)
                .setEditorComponent(descEditor, JiraProject::setProjectDescription);;
        addColumn(JiraProject::getShortCode).setCaption("Краткий код").setExpandRatio(1)
                .setEditorComponent(shortCodeEditor, JiraProject::setShortCode);
        addColumn(JiraProject::getActive).setCaption("Включен").setExpandRatio(2)
                .setEditorComponent(isShadowCheck, JiraProject::setActive);

        getEditor().setEnabled(true);
    }
}
