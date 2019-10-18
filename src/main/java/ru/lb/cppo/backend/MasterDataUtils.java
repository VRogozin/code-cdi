package ru.lb.cppo.backend;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.beans.*;
import ru.lb.cppo.backend.data.*;
import ru.lb.cppo.backend.pojo.*;
import ru.lb.cppo.backend.service.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Stateless
public class MasterDataUtils {
    private static final Logger log4jLogger = Logger.getLogger(MasterDataUtils.class);
    private long flushRows=25;
    private Date loadMySQLDate;

    @Inject
    private JiraDBService jiraDBService;
    @Inject
    private JiraProjectExecutionService jiraProjectExecutionService;
    @Inject
    private CodeJiraIssuesService codeJiraIssuesService;
    @Inject
    private ProjectCodePlanService projectCodePlanService;
    @Inject
    private JiraErrorMessageService jiraErrorMessageService;
    @Inject
    private JiraProjectService jiraProjectService;
    @Inject
    private ProjectCodeService projectCodeService;
    @Inject
    private ProjectUserRoleService projectUserRoleService;
    @Inject
    private ProjectVersionService projectVersionService;

    @Inject
    private ReportRowBean reportRowBean;
    @Inject
    private CodeJiraIssuesBean codeJiraIssuesBean;
    @Inject
    private ProjectCodePlanBean projectCodePlanBean;
    @Inject
    private JiraErrorMessageBean jiraErrorMessageBean;
    @Inject
    private JiraProjectBean jiraProjectBean;
    @Inject
    private ProjectCodeBean projectCodeBean;
    @Inject
    private ProjectUserRoleBean projectUserRoleBean;
    @Inject
    private ProjectVersionBean projectVersionBean;
    @Inject
    private CodeAccountingBean codeAccountingBean;

    public MasterDataUtils() {
    }

    public Date getLoadMySQLDate() {
        return loadMySQLDate;
    }

    public String getLoadMySQLDateAsString() {
        String strDate="недавно ...";
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        if ( loadMySQLDate !=null ) {
            strDate = sdfDate.format(loadMySQLDate);
        }
        return strDate;
    }

    public void setLoadMySQLDate(Date loadMySQLDate) {
        this.loadMySQLDate = loadMySQLDate;
    }

//    @Shedule(second="0", minute="10", hour="5", dayOfMonth="*", month="*", year="*")
    /**
     * Загрузка данных основных экранов
     */
    public void fillPGBase() {
        log4jLogger.log(Level.INFO, "---------------------------------"
                + " FillPGBase ---------------------------------");

        // loadJiraProjectExecutions
        loadJiraProjectExecutions();
        //loadCodeJiraIssues
        loadCodeJiraIssues();
        // loadProjectCodePlan
        loadProjectCodePlan();
        // load JiraErroMessage from table
        loadJiraErrorMessage();
        setLoadMySQLDate(new Date());
        log4jLogger.log(Level.INFO, "---------------------------------"
                + " /FillPGBase ---------------------------------");

    }

    private void loadJiraProjectExecutions(){

        final long start = System.nanoTime();
        final long total;

        log4jLogger.log(Level.INFO, " || load JiraProjectExecutions ");

        List<JiraProjectExecution> jiraProjectExecutions = jiraProjectExecutionService.findAllRows();
        // TODO: 24.11.2017   проверка наличия данных
        if (jiraProjectExecutions.size()>0 && !jiraProjectExecutions.isEmpty()) {
            reportRowBean.deleteAllRows();
            Iterator<JiraProjectExecution> itr = jiraProjectExecutions.iterator();
            long rowCnt=0;
            while (itr.hasNext()) {
                JiraProjectExecution jirarow = itr.next();
                ReportRow reportRow =
                        new ReportRow(  jirarow.getProjectCode(),
                                jirarow.getProjectID(),
                                jirarow.getProjectMnem(),
                                jirarow.getVersionID(),
                                jirarow.getProjectVersion(),
                                jirarow.getWorkRole(),
                                jirarow.getTimeOrig(),
                                jirarow.getTimeSpn(),
                                jirarow.getTimeEst(),
                                jirarow.getProzentRealize(),
                                jirarow.getDeltaRealize(),
                                jirarow.getProzentDelta(),
                                jirarow.getVersionStatus());
                reportRowBean.storeRow(reportRow);
                if (rowCnt > 0 && rowCnt % flushRows == 0) {
                    reportRowBean.flush();
                    rowCnt=0;
                }
                rowCnt++;
            }
            log4jLogger.log(Level.INFO, " - Загрузка выполнена.");
        } else {
            log4jLogger.log(Level.ERROR, " - Ошибка получения списка JiraProjectExecution:");
        }
        total = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start);
        log4jLogger.log(Level.INFO, " Time spent " + total);
        log4jLogger.log(Level.INFO, "---------------------------------");
    }

    private void loadCodeJiraIssues(){

        final long start = System.nanoTime();
        final long total;

        log4jLogger.log(Level.INFO, " || load CodeJiraIssues ");
        List<CodeJiraIssues> codeJiraIssues = codeJiraIssuesService.findAll();
        if (codeJiraIssues.size()>0 && !codeJiraIssues.isEmpty()) {
            codeJiraIssuesBean.deleteAllRows();
            Iterator<CodeJiraIssues> cjiIterator = codeJiraIssues.iterator();
            long rowCnt=0;
            while (cjiIterator.hasNext()) {
                CodeJiraIssues codeJIRow = new CodeJiraIssues();
                codeJIRow = cjiIterator.next();
                codeJiraIssuesBean.storeRow(codeJIRow);

                if (rowCnt > 0 && rowCnt % flushRows == 0) {
                    codeJiraIssuesBean.flush();
                    rowCnt=0;
                }
                rowCnt++;
            }
            log4jLogger.log(Level.INFO, " - Загрузка выполнена.");
        } else {
            log4jLogger.log(Level.ERROR, " - Ошибка получения списка CodeJiraIssues:");
        }
        total = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start);
        log4jLogger.log(Level.INFO, " Time spent " + total);
        log4jLogger.log(Level.INFO, "---------------------------------");

    }

    private void loadProjectCodePlan(){

        final long start = System.nanoTime();
        final long total;

        log4jLogger.log(Level.INFO, " || load ProjectCodePlan ");
        List<ProjectCodePlan> projectCodePlans = projectCodePlanService.getActiveRow();
        if (projectCodePlans.size()>0 && !projectCodePlans.isEmpty()) {
            projectCodePlanBean.deleteAllRows();
            Iterator<ProjectCodePlan> projectCodePlanIterator = projectCodePlans.iterator();
            long rowCnt=0;
            while (projectCodePlanIterator.hasNext()) {
                ProjectCodePlan codePlan = new ProjectCodePlan();
                codePlan = projectCodePlanIterator.next();
                projectCodePlanBean.storeRow(codePlan);
                if (rowCnt > 0 && rowCnt % flushRows == 0) {
                    projectCodePlanBean.flush();
                    rowCnt=0;
                }
                rowCnt++;
            }
            projectCodePlanBean.flush();
            log4jLogger.log(Level.INFO, " - Загрузка выполнена.");
        } else {
            log4jLogger.log(Level.ERROR, " -  Ошибка получения списка ProjectCodePlan:");
        }
        total = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start);
        log4jLogger.log(Level.INFO, " Time spent " + total);
        log4jLogger.log(Level.INFO, "---------------------------------");

    }

    private void loadJiraErrorMessage(){

        final long start = System.nanoTime();
        final long total;

        log4jLogger.log(Level.INFO, " || Загрузка JiraErrorMessage ");
        // JiraErrorMessage
        List<JiraErrorMessage> jiraErrorMessages = jiraErrorMessageService.findAll();
        if (!jiraErrorMessages.isEmpty()) {
            jiraErrorMessageBean.deleteAllRows();
            Iterator<JiraErrorMessage> itrJEM = jiraErrorMessages.iterator();
            long rowCnt=0;
            while (itrJEM.hasNext()) {
                JiraErrorMessage jiraErr = new JiraErrorMessage();
                jiraErr = itrJEM.next();
                jiraErrorMessageBean.storeRow(jiraErr);

                if (rowCnt > 0 && rowCnt % flushRows == 0) {
                    jiraErrorMessageBean.flush();
                    rowCnt=0;
                }
                rowCnt++;
            }
            jiraErrorMessageBean.flush();
            log4jLogger.log(Level.INFO, " - Загрузка выполнена.");
        } else {
            log4jLogger.log(Level.ERROR, " - Ошибка получения списка JiraErrorMessage:");
        }

        total = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start);
        log4jLogger.log(Level.INFO, " Time spent " + total);
        log4jLogger.log(Level.INFO, "---------------------------------");

    }

    /**
     * загрузка справочных данных
     */
    public void fillSettings() {
        log4jLogger.log(Level.INFO, "Settings : Load Jira Project :");
        loadJiraProject();
        log4jLogger.log(Level.INFO, "Settings : Load Project Code:");
        loadProjectCode();
        log4jLogger.log(Level.INFO, "Settings : Load Project Version:");
        loadProjectVersion();
        log4jLogger.log(Level.INFO, "Settings : Load User Role :");
        loadProjectUserRole();
        log4jLogger.log(Level.INFO, "Settings : Load Accounting :");
        loadAccounting();
    }

    /**
     * Загрузка данных проектов Jira
     */
    private void loadJiraProject() {
        List<PlainJiraProject> jProjects = jiraProjectService.findAll();
        if (!jProjects.isEmpty() && jProjects.size()>0) {
            jiraProjectBean.deleteAllRows();
            Iterator<PlainJiraProject> itrJProject = jProjects.iterator();
            while (itrJProject.hasNext()) {
                PlainJiraProject plainJiraProject = itrJProject.next();
                JiraProject jiraProject = new JiraProject(plainJiraProject.getProjectId(), plainJiraProject.getProjectName(),
                        plainJiraProject.getProjectDescription(), plainJiraProject.getSmallCode(), plainJiraProject.getActive());
                jiraProjectBean.storeRow(jiraProject);
            }
        } else {
            log4jLogger.log(Level.ERROR, "Ошибка получения списка PlainJiraProject:");
        }
    }

    /**
     * Загрузка данных Кодов проектов
     */
    private void loadProjectCode() {
        List<PlainProjectCode> projectCodes = projectCodeService.findAll();
        if (!projectCodes.isEmpty() && projectCodes.size() > 0) {
            projectCodeBean.deleteAllRows();
            Iterator<PlainProjectCode> ppCode = projectCodes.iterator();
            while (ppCode.hasNext()) {
                PlainProjectCode codeProject = ppCode.next();
                ProjectCode projectCode = new ProjectCode(codeProject.getCodeId(), codeProject.getCodeName(),
                        codeProject.getCodeDescription(), codeProject.getActive());
                projectCodeBean.storeRow(projectCode);
            }
        } else {
            log4jLogger.log(Level.ERROR, "Ошибка получения списка PlainProjectCode:");
        }
    }

    /**
     * Загрузка Версий проектов Jira
     */
    private void loadProjectVersion() {
        List<PlainProjectVersion> projectVersions = projectVersionService.findAll();
        if (!projectVersions.isEmpty() && projectVersions.size()>0) {
            projectVersionBean.deleteAllRows();
            Iterator<PlainProjectVersion> ppVer = projectVersions.iterator();
            while (ppVer.hasNext()) {
                PlainProjectVersion ppVersion = ppVer.next();
                ProjectVersion projectVersion =
                        new ProjectVersion(ppVersion.getVersionId(), ppVersion.getVersionName(), ppVersion.getJproject(),
                                ppVersion.getVersionBegin(), ppVersion.getReleaseDate(), ppVersion.getStatus(), ppVersion.getActive());
                projectVersionBean.storeRow(projectVersion);
            }
        } else {
            log4jLogger.log(Level.ERROR, "Ошибка получения списка PlainProjectVersion:");
        }
    }

    /**
     * Загрузка Пользователей и их ролей в проекте
     */
    private void loadProjectUserRole() {
        List<PlainJiraUserRole> plainJiraUserRoleList = projectUserRoleService.findAll();
        if (!plainJiraUserRoleList.isEmpty() && plainJiraUserRoleList.size()>0) {
            projectUserRoleBean.deleteAllRows();
            Iterator<PlainJiraUserRole> plainJiraUserRoleIterator = plainJiraUserRoleList.iterator();
            while (plainJiraUserRoleIterator.hasNext()) {
                PlainJiraUserRole plainJiraUserRole = plainJiraUserRoleIterator.next();
                ProjectUserRole projectUserRole =
                        new ProjectUserRole(plainJiraUserRole.getJuserName(), plainJiraUserRole.getJuserDispName(),
                                plainJiraUserRole.getJuserRole());
                projectUserRoleBean.storeRow(projectUserRole);
            }
        } else {
            log4jLogger.log(Level.ERROR, "Ошибка получения списка PlainJiraUserRole:");
        }
    }

    /**
     * Загрузка уникальных записей для добавления финансовых проектов
     */
    private List<PlainCodeVersion> getAdditionalUniqueCodeVersion (){

        List<PlainCodeVersion> outputList =new ArrayList<PlainCodeVersion>();

        List<PlainCodeVersion> plainCodeVersionsFromAccounting = codeAccountingBean.getCodeVersionAsList();
        List<PlainCodeVersion> plainCodeVersions = projectCodePlanBean.getCodeVersionAsList();

//        log4jLogger.log(Level.INFO, "!!! -- codeVersionfromCodeAccounting -- !!!");
//        plainCodeVersionsFromAccounting.stream().forEachOrdered(p->log4jLogger.log(Level.INFO, p.toString()));
//        log4jLogger.log(Level.INFO, "!!! -- codeVersionfromCodePlan -- !!!");
//        plainCodeVersions.stream().forEachOrdered(p->log4jLogger.log(Level.INFO, p.toString()));
//        log4jLogger.log(Level.INFO, "!!! ------------------ !!!");

        Iterator<PlainCodeVersion> pCVIterator=plainCodeVersions.iterator();
        Iterator<PlainCodeVersion> pCVFAIterator=plainCodeVersionsFromAccounting.iterator();

        Set<String> uniqueValues = new HashSet<String>();
        try {
            while(pCVFAIterator.hasNext()) {
                PlainCodeVersion pcvFA = pCVFAIterator.next();
                if (!uniqueValues.add(pcvFA.toString())) {
                    log4jLogger.log(Level.ERROR, "Error !!! Not unique value :" + pcvFA.toString());
                }
            }
//            log4jLogger.log(Level.INFO, "Unique value list count :" + uniqueValues.size());

            while(pCVIterator.hasNext()) {
                PlainCodeVersion pcv = pCVIterator.next();
                if (uniqueValues.add(pcv.toString())) {
                    outputList.add(pcv);
//                    log4jLogger.log(Level.INFO, "Add unique value :" + pcv.toString());
                }
            }
        } catch (Exception e) {
            log4jLogger.log(Level.ERROR, "Code Version new value List  error :" + e.getMessage());
        }
//        log4jLogger.log(Level.INFO, "Unique value updated list count :" + uniqueValues.size());

        if (outputList.isEmpty()) {
            return Collections.EMPTY_LIST;
        } else {
//            log4jLogger.log(Level.INFO, "!!! -- unique value -- !!!");
//            outputList.stream().forEachOrdered(p->log4jLogger.log(Level.INFO, p.toString()));
//            log4jLogger.log(Level.INFO, "!!! ------------------ !!!");
            return outputList;
        }
    }

    /**
     * Загрузка записей для финансовых проектов
     */
    private void loadAccounting() {

        final long start = System.nanoTime();
        final long total;

        log4jLogger.log(Level.INFO, "---------------------------------"
                + " MasterDataUtils / loadAccounting ");

        List<PlainCodeVersion> plainCodeVersions = getAdditionalUniqueCodeVersion();
        log4jLogger.log(Level.INFO, "Additional rows for CodeAccounting :"+plainCodeVersions.size());
        Iterator<PlainCodeVersion> plainCodeVersionIterator = plainCodeVersions.iterator();
        long rowCnt=0;
        while(plainCodeVersionIterator.hasNext()) {
            PlainCodeVersion plainCodeVersion = plainCodeVersionIterator.next();
            CodeAccounting codeAccounting = new CodeAccounting(
                    plainCodeVersion.getProjectCode(),
                    plainCodeVersion.getVersionID(),
                    plainCodeVersion.getProjectVersion());
            codeAccountingBean.storeRow(codeAccounting);
            if (rowCnt > 0 && rowCnt % flushRows == 0) {
                codeAccountingBean.flush();
                rowCnt=0;
            }
            rowCnt++;
        }
        codeAccountingBean.flush();

        total = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start);
        log4jLogger.log(Level.INFO, " Time spent " + total);
        log4jLogger.log(Level.INFO, "---------------------------------");

    }

    /**
     * Расчет данных для отчета. Выгрузка стартовых прарметров для расчета.
     */
    public void exportWorkSettings(){
        log4jLogger.log(Level.INFO, "!=====================================================!");
        log4jLogger.log(Level.INFO, "------------- Create initial Table -------------------");
        jiraDBService.deleteInitialTable();
        jiraDBService.createInitialTable();
        log4jLogger.log(Level.INFO, "!-----------------------------------------------------!");
        log4jLogger.log(Level.INFO, "Jira Project :" + jiraProjectBean.getProjectQueryString() );
        exportProjects();
        exportUserRoles();
        exportCPPlan();
        log4jLogger.log(Level.INFO, "!=====================================================!");
        HashMap<String,String> valueMap = new HashMap<>();
        valueMap.put("projectList",jiraProjectBean.getProjectQueryString());
        jiraDBService.fillTemporarySQL(valueMap);
    }

    /**
     * Выгрузка данных по проектам подлежащим обработке
     */
    private void exportProjects() {

        log4jLogger.log(Level.INFO, "!---- export Project ---------------------------------!");

        List<JiraProject> jiraProjects;
        jiraProjects = jiraProjectBean.getActiveRow();

        if (!jiraProjects.isEmpty()) {
            Iterator<JiraProject> jiraProjectIterator = jiraProjects.iterator();
            while (jiraProjectIterator.hasNext()) {
                JiraProject jiraProject = new JiraProject();
                jiraProject = jiraProjectIterator.next();
                try {
//                    log4jLogger.log(Level.INFO, "Export Project :" + jiraProject.getProjectIdAsString() + " " + jiraProject.getProjectName()
//                            + " : " + jiraProjectService.storeRow(jiraProject) + ":");
                    jiraProjectService.storeRow(jiraProject);
                } catch (SQLException e) {
                    log4jLogger.log(Level.ERROR, "Export Project. Failed export to MySQL :" + e.getMessage());
                }
            }
        } else {
            log4jLogger.log(Level.ERROR, "Export Project. Список проектов пуст. :");
        }
    }

    /**
     * Выгрузка данных по плановым затратам проектов подлежащих обработке
     */
    private void exportCPPlan() {

        log4jLogger.log(Level.INFO, "!---- export Project Code Plans ----------------------!");

        List<ProjectCodePlan> projectCodePlans;
        projectCodePlans = projectCodePlanBean.getAllRow();

        Iterator<ProjectCodePlan> projectCodePlanIterator = projectCodePlans.iterator();
        while(projectCodePlanIterator.hasNext()) {
            ProjectCodePlan projectCodePlan = new ProjectCodePlan();
            projectCodePlan = projectCodePlanIterator.next();
            try {
//                log4jLogger.log(Level.INFO, "Export ProjectCodePlan :" +
//                        projectCodePlan.getProjectCode() + projectCodePlan.getProjectVersion() +
//                        " : " + projectCodePlanService.storeRow(projectCodePlan) +":");
                projectCodePlanService.storeRow(projectCodePlan);
            } catch (SQLException e) {
                log4jLogger.log(Level.ERROR, "Export CPPlan. Failed export to MySQL :" + e.getMessage());
            }
        }
    }

    /**
     * Выгрузка данных по списку ролей подлежащим обработке
     */
    private void exportUserRoles() {

        log4jLogger.log(Level.INFO, "!---- export User Roles ------------------------------!");

        List<ProjectUserRole> projectUserRoles;
        projectUserRoles = projectUserRoleBean.getActiveRow();

        Iterator<ProjectUserRole> projectUserRoleIterator = projectUserRoles.iterator();
        while(projectUserRoleIterator.hasNext()) {
            ProjectUserRole projectUserRole = new ProjectUserRole();
            projectUserRole = projectUserRoleIterator.next();
            try {
//                log4jLogger.log(Level.INFO, "Export ProjectUserRole :" +
//                        projectUserRole.getJuserName() + " - " + projectUserRole.getRoleName() +
//                        " : " + projectUserRoleService.storeRow(projectUserRole) +":");
                projectUserRoleService.storeRow(projectUserRole);
            } catch (SQLException e) {
                log4jLogger.log(Level.ERROR, "Export ProjectUserRole. Failed export to MySQL :" + e.getMessage());
            }
        }
    }

}
