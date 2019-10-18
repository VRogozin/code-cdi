package ru.lb.cppo.ui;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.HtmlRenderer;
import ru.lb.cppo.backend.data.ProjectVersion;

public class ProjectVersionGrid extends Grid<ProjectVersion> {
    public ProjectVersionGrid() {

        CheckBox isShadowCheck = new CheckBox();

        addColumn(ProjectVersion::getJproject).setCaption("ИД проекта").setExpandRatio(2);

//        addColumn(ReportRow::getProjectVersion).setCaption("Версия").setExpandRatio(2);
        addColumn(this::checkVersionStatus, new HtmlRenderer())
                .setCaption("Версия").setComparator((p1, p2) -> {
            return p1.getVersionName().compareTo(p2.getVersionName());
        });
        addColumn(ProjectVersion::getStatus).setCaption("Статус версии").setExpandRatio(2);
        addColumn(ProjectVersion::getVersionBegin).setCaption("дата старта").setExpandRatio(2);
        addColumn(ProjectVersion::getReleaseDate).setCaption("дата релиза").setExpandRatio(2);
        addColumn(ProjectVersion::getActive).setCaption("Включен").setExpandRatio(1)
        .setEditorComponent(isShadowCheck, ProjectVersion::setActive);

        getEditor().setEnabled(true);
    }

    private String checkVersionStatus(ProjectVersion codeRow){
        String color="#34495e";

        switch (codeRow.getStatus()){
            case ARCHIVED:
                color="#7f8c8d";
                break;
            case PLANNED:
                color="#34495e";
                break;
            case RELEASED:
                color="#3498db";
                break;
            case STARTED:
                color="#2ecc71";
                break;
        }

        String versionString = "<span style=\"color: "
                + color
                + "\">"
                + codeRow.getVersionName()
                + "</span>";
        return versionString;
    }

}
