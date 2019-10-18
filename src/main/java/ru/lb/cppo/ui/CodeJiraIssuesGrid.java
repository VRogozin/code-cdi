package ru.lb.cppo.ui;

import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.NumberRenderer;
import ru.lb.cppo.backend.data.CodeJiraIssues;

import java.text.DecimalFormat;

public class CodeJiraIssuesGrid extends Grid<CodeJiraIssues> {

    public CodeJiraIssuesGrid() {

        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        addColumn(CodeJiraIssues::getProjectCode).setCaption("Код проекта");
        addColumn(CodeJiraIssues::getWorkRole).setCaption("Роль");
//        addColumn(CodeJiraIssues::getProjectVersion).setCaption("Версия");
        addColumn(this::checkVersionStatus, new HtmlRenderer())
                .setCaption("Версия").setComparator((p1, p2) -> {
            return p1.getProjectVersion().compareTo(p2.getProjectVersion());
        });
        addColumn(CodeJiraIssues::getProjectName).setCaption("Наименование проекта");
        addColumn(CodeJiraIssues::getPriorityStr).setCaption("Приоритет");
        addColumn(CodeJiraIssues::getJissuetype).setCaption("Тип задачи");
//        addColumn(CodeJiraIssues::getIssueNum).setCaption("Номер задачи");
        addColumn(this::htmlFormatURL, new HtmlRenderer()).setCaption("Номер задачи").setComparator((u1,u2) -> {
            return u1.getIssueNum().compareTo(u2.getIssueNum());
        });
        addColumn(CodeJiraIssues::getjIssueStatus).setCaption("Статус задачи");
        addColumn(CodeJiraIssues::getAsigned).setCaption("Исполнитель");
        addColumn(codeji->decimalFormat.format(codeji.getTimeOrig()) + " ч")
                        .setCaption("Первоначальная оценка").setComparator((cj1, cj2) -> {
            return cj1.getTimeOrig().compareTo(cj2.getTimeOrig());
        }).setStyleGenerator(codeji -> "v-align-right");
//        addColumn(CodeJiraIssues::getTimeSpn).setCaption("Затрачено времени").setStyleGenerator(codeji -> "v-align-right");
        addColumn(codeji->decimalFormat.format(codeji.getTimeSpn()) + " ч")
                .setCaption("Затрачено времени").setComparator((cj1, cj2) -> {
            return cj1.getTimeSpn().compareTo(cj2.getTimeSpn());
        }).setStyleGenerator(codeji -> "v-align-right");
//        addColumn(CodeJiraIssues::getTimeEst).setCaption("Осталось времени").setStyleGenerator(codeji -> "v-align-right");
        addColumn(codeji->decimalFormat.format(codeji.getTimeEst()) + " ч")
                .setCaption("Осталось времени").setComparator((cj1, cj2) -> {
            return cj1.getTimeEst().compareTo(cj2.getTimeEst());
        }).setStyleGenerator(codeji -> "v-align-right");
        addColumn(CodeJiraIssues::getBuildNum).setCaption("Номер сборки");
/*
        addColumn(this::htmlFormatURL, new HtmlRenderer()).setCaption("URL").setComparator((u1,u2) -> {
            return u1.getURL().compareTo(u2.getURL());
                });
*/
        addColumn(CodeJiraIssues::getRecordType).setCaption("Тип записи");
        addColumn(CodeJiraIssues::getId, new NumberRenderer()).setCaption("Id").setHidden(true);
        addColumn(CodeJiraIssues::getIssueID).setCaption("ID задачи").setHidden(true);
        addColumn(CodeJiraIssues::getProjectID).setCaption("ID проекта").setHidden(true);
        addColumn(CodeJiraIssues::getProjectMnem).setCaption("Мнемоника проекта").setHidden(true);
        addColumn(CodeJiraIssues::getProjectPrev).setCaption("Заголовок кода проекта").setHidden(true);
        addColumn(CodeJiraIssues::getProjectSubst).setCaption("Остаток кода проекта").setHidden(true);

        setSizeFull();
        setFrozenColumnCount(3);
    }

    private String htmlFormatURL(CodeJiraIssues codeJiraIssues) {
        String issuesURL = codeJiraIssues.getURL();
        String issueNum = codeJiraIssues.getIssueNum();

        String urlCode ="<a class=\"v-link\" href=\""+ issuesURL+ "\" target=\"blank\">"+issueNum+"</a>";

        return urlCode;
    }

    private String checkVersionStatus(CodeJiraIssues codeJiraIssues){
        String color="Planned";

        switch (codeJiraIssues.getVersionStatus()){
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
                + codeJiraIssues.getProjectVersion()
                + "</span>";
        return versionString;
    }

}
