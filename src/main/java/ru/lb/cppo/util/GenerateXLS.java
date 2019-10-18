package ru.lb.cppo.util;

import com.vaadin.server.Page;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import ru.lb.cppo.backend.data.CodeJiraIssues;
import ru.lb.cppo.backend.data.JiraErrorMessage;
import ru.lb.cppo.backend.data.ReportRow;

import java.io.*;
import java.util.ArrayList;

public class GenerateXLS {
    private static final Logger log4jLogger = Logger.getLogger(GenerateXLS.class);

    public GenerateXLS() {

    }

    public File generateMasterReport(ArrayList<ReportRow> reportsrow) {
        log4jLogger.log(Level.INFO, "Generate XLS Master Report");

        String reportFileName = "MasterReport-"+ Page.getCurrent().getWebBrowser().getCurrentDate().toString();
        try {
            File repFile = File.createTempFile(reportFileName, ".xlsx");
            try(InputStream is = getClass().getClassLoader().getResourceAsStream("XLSTemplate/template/MasterReportTemplate.xlsx")) {
                try {
                    OutputStream os = new FileOutputStream(repFile);
                    Context context = new Context();
                    context.putVar("reportsrows", reportsrow);
                    JxlsHelper.getInstance().processTemplateAtCell(is, os, context,"Report!A1");
                    os.flush();
                    os.close();
                    is.close();

                    return repFile;

                } catch (Exception e) {
                    log4jLogger.log(Level.ERROR, "Error on processing XLS Master Report "+ e.getMessage());
                }
            } catch (Exception e) {
                log4jLogger.log(Level.ERROR, "Error on read template file for generate XLS Master Report "+ e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public File generateMultiPageReport(ArrayList<ReportRow> reportsrow, ArrayList<CodeJiraIssues> codeJiraIssues) {
        log4jLogger.log(Level.INFO, "Generate XLS multipage report");

        String reportFileName = "MPReport-"+ Page.getCurrent().getWebBrowser().getCurrentDate().toString();
        try {
            File repFile = File.createTempFile(reportFileName, ".xlsx");
            try(InputStream is = getClass().getClassLoader().getResourceAsStream("XLSTemplate/template/MultiPageTemplate.xlsx")) {
                try {
                    OutputStream os = new FileOutputStream(repFile);
                    Context context = new Context();
                    context.putVar("reportsrows", reportsrow);
                    context.putVar("codeJiraIssuesList", codeJiraIssues);
                    log4jLogger.log(Level.INFO, "Process template file");
                    JxlsHelper.getInstance().processTemplate(is, os, context);
                    log4jLogger.log(Level.INFO, "write file");

                    os.flush();
                    os.close();
                    is.close();

                    return repFile;

                } catch (Exception e) {
                    log4jLogger.log(Level.ERROR, "Error on processing XLS multipage report "+ e.getMessage());
                }
            } catch (Exception e) {
                log4jLogger.log(Level.ERROR, "Error on read template file for generate XLS Master Report "+ e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public File generateErrorReport(ArrayList<JiraErrorMessage> errMsgRow) {
        File repFile;
        InputStream is;

        log4jLogger.log(Level.INFO, "Generate XLS Error Message Report");
        String reportFileName = "ErrMsgReport-" + Page.getCurrent().getWebBrowser().getCurrentDate().toString();
        try {
            repFile = File.createTempFile(reportFileName, ".xlsx");
        } catch (Exception e) {
            log4jLogger.log(Level.ERROR, "Error on create ouput file for Error Message Report " + e.getMessage());
            return null;
        }

        try {
            is = getClass().getClassLoader().getResourceAsStream("XLSTemplate/template/ErrMsgReportTemplate.xlsx");
        } catch (Exception e) {
            log4jLogger.log(Level.ERROR, "Error load template file for Error Message Report " + e.getMessage());
            return null;
        }

        try {
             OutputStream os = new FileOutputStream(repFile);
             Context context = new Context();
             context.putVar("errMsgRowList" , errMsgRow);
             JxlsHelper.getInstance().processTemplateAtCell(is, os, context, "Report!A1");
             os.flush();
             os.close();
             is.close();

        } catch (Exception e) {
            log4jLogger.log(Level.ERROR, "Error on processing XLS Error Message Report " + e.getMessage());
            return null;
        }

        return repFile;

    }

}
