package ru.lb.cppo.ui;

import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.HtmlRenderer;
import ru.lb.cppo.backend.data.ProjectCodePlan;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ProjectCodePlanGrid extends Grid<ProjectCodePlan> {
    public ProjectCodePlanGrid() {

        Locale.setDefault(new Locale("ru", "RU"));
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance();

        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        TextField planOrigEditor = new TextField();

        addColumn(ProjectCodePlan::getProjectCode).setCaption("Код проекта").setExpandRatio(2);
//        addColumn(ReportRow::getProjectVersion).setCaption("Версия").setExpandRatio(2);
        addColumn(this::checkVersionStatus, new HtmlRenderer())
                .setCaption("Версия").setComparator((p1, p2) -> {
            return p1.getProjectVersion().compareTo(p2.getProjectVersion());
        });
        addColumn(ProjectCodePlan::getWorkRole).setCaption("Роль").setExpandRatio(2);

        addColumn(projectCodePlanRow->decimalFormat.format(projectCodePlanRow.getPlanOrig()))
                .setCaption("Плановые затраты (ч)").setComparator((rr1, rr2) -> {
            return rr1.getPlanOrig().compareTo(rr2.getPlanOrig());
        }).setStyleGenerator(reportRow -> "v-align-right")
        .setEditorComponent(planOrigEditor, ProjectCodePlan::setPlanOrig);

        addColumn(projectCodePlanRow->decimalFormat.format(projectCodePlanRow.getFactOrig()))
                .setCaption("Фактические затраты (ч)").setComparator((rr1, rr2) -> {
            return rr1.getFactOrig().compareTo(rr2.getFactOrig());
        }).setStyleGenerator(reportRow -> "v-align-right");

        getEditor().setEnabled(true);
    }

    private String checkVersionStatus(ProjectCodePlan codeRow){
        String color="Planned";

        switch (codeRow.getVersionStatus()){
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
                + codeRow.getProjectVersion()
                + "</span>";
        return versionString;
    }

}
