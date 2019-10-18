package ru.lb.cppo.ui;

import com.vaadin.ui.Grid;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import ru.lb.cppo.backend.data.CodeAccounting;

import java.text.DecimalFormat;

public class CodeAccountingGrid extends Grid<CodeAccounting> {

    public CodeAccountingGrid() {

        final DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);

//        TextField cleanCostEditor = new TextField();
//        TextField riskCostEditor = new TextField();
        TextField adminCostEditor = new TextField();
//        TextField guaranteeCostEditor = new TextField();
        TextField totalCostEditor = new TextField();
        TextArea codeDescEditor = new TextArea();

        addColumn(CodeAccounting::getProjectCode).setCaption("Код проекта");

        addColumn(CodeAccounting::getProjectVersion).setCaption("Версия");
/*
        addColumn(codeAcc->decimalFormat.format((BigDecimal)codeAcc.getCleanCost()))
                .setCaption("Чистые затраты")
                .setComparator((ca1, ca2) -> {
                    return ca1.getCleanCost().compareTo(ca2.getCleanCost());
                })
                .setEditorComponent(cleanCostEditor, CodeAccounting::setCleanCost)
                .setStyleGenerator(codeAcc -> "v-align-right")
                .setHidden(true);

        addColumn(codeAcc->decimalFormat.format(codeAcc.getRiskCost()))
                .setCaption("Риски")
                .setComparator((ca1, ca2) -> {
                    return ca1.getRiskCost().compareTo(ca2.getRiskCost());
                })
                .setEditorComponent(riskCostEditor,CodeAccounting::setRiskCost)
                .setStyleGenerator(codeAcc -> "v-align-right")
                .setHidden(true);

        addColumn(codeAcc->decimalFormat.format(codeAcc.getGuaranteeCost()))
                .setCaption("Гарантийные обязательства")
                .setComparator((ca1, ca2) -> {
                    return ca1.getGuaranteeCost().compareTo(ca2.getGuaranteeCost());
                })
                .setEditorComponent(guaranteeCostEditor,CodeAccounting::setGuaranteeCost)
                .setStyleGenerator(codeAcc -> "v-align-right")
                .setHidden(true);
*/
        addColumn(codeAcc->decimalFormat.format(codeAcc.getTotalCost()))
                .setCaption("Финансовая оценка")
                .setComparator((ca1, ca2) -> {
                    return ca1.getTotalCost().compareTo(ca2.getTotalCost());
                })
                .setEditorComponent(totalCostEditor,CodeAccounting::setTotalCost)
                .setStyleGenerator(codeAcc -> "v-align-right");

        addColumn(codeAcc->decimalFormat.format(codeAcc.getAdminCost()))
                .setCaption("Административные расходы")
                .setComparator((ca1, ca2) -> {
                    return ca1.getAdminCost().compareTo(ca2.getAdminCost());
                })
                .setEditorComponent(adminCostEditor,CodeAccounting::setAdminCost)
                .setStyleGenerator(codeAcc -> "v-align-right");

        codeDescEditor.setRows(1);
        addColumn(CodeAccounting::getAccDescription)
                .setCaption("Описание")
                .setEditorComponent(codeDescEditor, CodeAccounting::setAccDescription);

        setSizeFull();
        setFrozenColumnCount(2);
        getEditor().setEnabled(false);

    }
}
