package ru.lb.cppo.ui;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import ru.lb.cppo.backend.data.ProjectCode;

public class ProjectCodeGrid extends Grid<ProjectCode> {
    public ProjectCodeGrid() {

        CheckBox isShadowCheck = new CheckBox();
        TextField descEditor = new TextField();

        addColumn(ProjectCode::getCodeName).setCaption("Код проекта").setExpandRatio(2);
        addColumn(ProjectCode::getCodeDescription).setCaption("Описание кода проекта").setExpandRatio(2)
        .setEditorComponent(descEditor, ProjectCode::setCodeDescription);
        addColumn(ProjectCode::getActive).setCaption("Включен").setExpandRatio(2)
        .setEditorComponent(isShadowCheck, ProjectCode::setActive);

        getEditor().setEnabled(true);
    }
}
