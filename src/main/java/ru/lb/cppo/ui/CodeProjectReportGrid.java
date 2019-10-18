package ru.lb.cppo.ui;

import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.ProgressBarRenderer;
import elemental.json.JsonValue;
import ru.lb.cppo.backend.data.ReportRow;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CodeProjectReportGrid extends Grid<ReportRow> {
    private static final String STYLE_ALIGN_RIGHT = "v-align-right";

    public CodeProjectReportGrid() {

        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        addColumn(ReportRow::getProjectCode).setCaption("Код проекта").setExpandRatio(2);
        addColumn(this::checkVersionStatus, new HtmlRenderer())
                .setCaption("Версия").setComparator((p1, p2) -> {
            return p1.getProjectVersion().compareTo(p2.getProjectVersion());
        });
        addColumn(ReportRow::getWorkRole).setCaption("Роль").setExpandRatio(2);
        addColumn(reportRow->decimalFormat.format(reportRow.getTimeOrig()) + " ч")
                .setCaption("Первоначальная оценка").setComparator((rr1, rr2) -> {
            return rr1.getTimeOrig().compareTo(rr2.getTimeOrig());
        }).setStyleGenerator(reportRow -> STYLE_ALIGN_RIGHT);
        addColumn(reportRow->decimalFormat.format(reportRow.getTimeSpn()) + " ч")
                .setCaption("Текущие трудозатраты").setComparator((rr1, rr2) -> {
            return rr1.getTimeSpn().compareTo(rr2.getTimeSpn());
        }).setStyleGenerator(reportRow -> STYLE_ALIGN_RIGHT);
        addColumn(reportRow->decimalFormat.format(reportRow.getTimeEst()) + " ч")
                .setCaption("Оставшиеся трудозатраты").setComparator((rr1, rr2) -> {
            return rr1.getTimeEst().compareTo(rr2.getTimeEst());
        }).setStyleGenerator(reportRow -> STYLE_ALIGN_RIGHT);
        addColumn(ReportRow::getProzentRealizeAsDouble, new ProgressBarRenderer(){
            @Override
            public JsonValue encode(Double value) {
                if (value != null) {
                    value = value/ 100.0;
                }
                return super.encode(value);
            }
        }).setCaption("% исполнения");
        addColumn(reportRow->decimalFormat.format(reportRow.getProzentRealize()) + " %")
                .setCaption("Процент исполнения").setComparator((rr1, rr2) -> {
            return rr1.getProzentRealize().compareTo(rr2.getProzentRealize());
        }).setStyleGenerator(reportRow -> STYLE_ALIGN_RIGHT);
        addColumn(reportRow->decimalFormat.format(reportRow.getDeltaRealize()) + " ч")
                .setCaption("Отклонение от плана").setComparator((rr1, rr2) -> {
            return rr1.getDeltaRealize().compareTo(rr2.getDeltaRealize());
        }).setStyleGenerator(reportRow -> STYLE_ALIGN_RIGHT);
        addColumn(this::htmlFormatProzentDelta, new HtmlRenderer())
                .setCaption("Отклонение от плана (%)").setComparator((p1, p2) -> {
            return p1.getProzentDelta().compareTo(p2.getProzentDelta());
        }).setStyleGenerator(reportRow -> STYLE_ALIGN_RIGHT);
        setSizeFull();
        setFrozenColumnCount(3);
//- редактирование
//        getEditor().setEnabled(true);
    }

    private String htmlFormatProzentDelta(ReportRow reportRow) {
        BigDecimal deltaProzent = reportRow.getProzentDelta();

        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

        String color = "";
        if (deltaProzent.intValue()<=0) {
            color = "#197de1;";
        } else {
            if (deltaProzent.intValue() <= 10) {
                color = "#d18100;";
            } else {
                color = "#ed473b;";
            }
        }

        String iconCode = "<span style=\"color:" + color
                + "\">"
                + decimalFormat.format(deltaProzent)
                + " %</span>";
        return iconCode;
    }

    private String checkVersionStatus(ReportRow reportRow){
        String color="Planned";

        switch (reportRow.getVersionStatus()){
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
                + reportRow.getProjectVersion()
                + "</span>";
        return versionString;
    }

}
