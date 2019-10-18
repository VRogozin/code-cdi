package ru.lb.cppo.backend.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Stateless
@SuppressWarnings("unchecked")
public class JiraDBService {

    private static final Logger log4jLogger = Logger.getLogger(JiraDBService.class);

    private static final String defaultProjectList ="10680,11880,12180,13781,13980,15680,15780,15781,15782,16180";

    @Resource(name = "mysqlFinal", lookup = "java:/MySqlDS", type = DataSource.class)
    private DataSource datasource;
    private Connection connection;

    @PostConstruct
    @PostActivate
    public void openConnection() {
        log4jLogger.log(Level.INFO, "create JiraReportService");
        try {
            connection = datasource.getConnection();
        } catch (Exception sqle) {
            log4jLogger.log(Level.ERROR, "Error create" + sqle.getMessage());
        }
    }

    @PrePassivate
    @PreDestroy
    public void cleanup() {
        try {
            log4jLogger.log(Level.INFO, "destroy JiraReportService");
            connection.close();
            connection = null;
        } catch (SQLException sqle) {
            log4jLogger.log(Level.ERROR, "Error destroy" + sqle.getMessage());
        }
    }

    public String getDefaultProjectList(){
        return defaultProjectList;
    }

    public ResultSet executeSQLString(String sqlString) {
        ResultSet rs = null;
        String eMessage="";
        try {
            eMessage="Connection close";
            if (connection.isClosed()) {
                openConnection();
                eMessage="";
            }
            eMessage="CreateStatement";
            Statement stmt = connection.createStatement();
            eMessage="Statement execute"+sqlString;
            rs = stmt.executeQuery(sqlString);
        } catch (SQLException e) {
            log4jLogger.log(Level.ERROR, eMessage + e.getMessage());
        }
        return rs;
    }

    public Boolean insertSQLString(String sqlString) {
        Boolean resultFlag = false;
        String eMessage="";
        try {
            eMessage="Connection close";
            if (connection.isClosed()) {
                openConnection();
                eMessage="";
            }
            eMessage="CreateStatement";
            Statement stmt = connection.createStatement();
            eMessage="Statement execute"+sqlString;
            if (!sqlString.trim().equals("")) {
                int cntRow=stmt.executeUpdate(normalizeString(sqlString));
                resultFlag=true;
            }
        } catch (SQLException e) {
            log4jLogger.log(Level.ERROR, eMessage + e.getMessage());
        }
        return resultFlag;
    }

    public void initialSQL() {
        try {
            if (connection.isClosed()) {
                openConnection();
            }
        } catch (SQLException e) {
            log4jLogger.log(Level.ERROR, "InitialSQL Connection isClosed failed :" + e.getMessage());
        }
        deleteInitialTable();
        createInitialTable();
        populateDB("SQL/00_TempTableFilled.sql");
        populateDB("SQL/10_createJiraIssueFilled.sql");
        populateDB("SQL/20_isSearchParents2.sql");
        populateDB("SQL/30_JiraCollapsedToTable.sql");
        populateDB("SQL/40_createTotalCodeVersion.sql");
        populateDB("SQL/50_createProcentCodeVersion.sql");
        populateDB("SQL/60_SplitOtherCPPORecord.sql");
        populateDB("SQL/70_createIssueCorrected.sql");
        populateDB("SQL/80_CreateIssuePreparedTable.sql");
        populateDB("SQL/100_JiraReportsCollapsedHard.sql");

        populateDB("SQL/900_UserRolesActiveProject.sql");
        populateDB("SQL/910_VersionAndRole.sql");

        populateDB("SQL/999_CheckJiraHealth.sql");
        populateDB("SQL/999_JiraHealthCostAfterRelease.sql");
    }

    public void createInitialTable() {
        try {
            if (connection.isClosed()) {
                openConnection();
            }
        } catch (SQLException e) {
            log4jLogger.log(Level.ERROR, "CreateInitialTable. Connection isClosed failed :" + e.getMessage());
        }
        populateDB("SQL/00_TempTableCreate.sql");
    }

    public void deleteInitialTable() {
        try {
            if (connection.isClosed()) {
                openConnection();
            }
        } catch (SQLException e) {
            log4jLogger.log(Level.ERROR, "DeleteInitialTable. Connection isClosed failed :" + e.getMessage());
        }
        populateDB("SQL/00_TempTableDelete.sql");
    }

    public void fillTemporarySQL(HashMap<String,String> valueMap) {
        try {
            if (connection.isClosed()) {
                openConnection();
            }
        } catch (SQLException e) {
            log4jLogger.log(Level.ERROR, "fill TemporarySQL Connection isClosed failed :" + e.getMessage());
        }
        populateDB("SQL/10m_createJiraIssueFilled.sql", valueMap,false);
        populateDB("SQL/20_isSearchParents2.sql");
        populateDB("SQL/30_JiraCollapsedToTable.sql");
        populateDB("SQL/40_createTotalCodeVersion.sql");
        populateDB("SQL/50_createProcentCodeVersion.sql");
        populateDB("SQL/60_SplitOtherCPPORecord.sql");
        populateDB("SQL/70_createIssueCorrected.sql");
        populateDB("SQL/80_CreateIssuePreparedTable.sql");
        populateDB("SQL/100_JiraReportsCollapsedHard.sql");

        populateDB("SQL/900_UserRolesActiveProject.sql");
        populateDB("SQL/910m_VersionAndRole.sql", valueMap, false);

        populateDB("SQL/999_CheckJiraHealth.sql");
        populateDB("SQL/999m_JiraHealthCostAfterRelease.sql", valueMap, false);
    }

    private void populateDB(String sqlFile){
        HashMap<String,String> valueMap = new HashMap<>();
        valueMap.put("projectList",defaultProjectList);
        populateDB(sqlFile, valueMap,false);
    }

    private void populateDB(String sqlFile, boolean showSql){
        HashMap<String,String> valueMap = new HashMap<>();
        valueMap.put("projectList",defaultProjectList);
        populateDB(sqlFile, valueMap,showSql);
    }

    private void populateDB(String sqlFile, HashMap<String,String> valueMap, boolean showSql){
        try {

            final long start = System.nanoTime();

            InputStream sqlStream = getClass().getClassLoader().getResourceAsStream(sqlFile);
            if(sqlStream != null) {
                executeScript(sqlStream, valueMap, showSql);
            }
            final long total = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start);
            log4jLogger.log(Level.INFO, "Run "+sqlFile+". Time spent " + total);

        } catch (Exception ex) {
            log4jLogger.log(Level.ERROR, "Error read resources" + ex.getMessage());
        }
    }

    public void executeScript(InputStream sqlStream) {

        executeScript(sqlStream,false);
    }

    public void executeScript(InputStream sqlStream, boolean showSql) {
        String s = new String();
        StringBuffer sb = new StringBuffer();

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(sqlStream));

            while ((s = br.readLine()) != null) {
                sb.append(s).append("\n");
            }
            br.close();

            // here is our splitter ! We use ";" as a delimiter for each request
            // then we are sure to have well formed statements
            String[] inst = sb.toString().split(";");

            Statement st = connection.createStatement();

            for (int i = 0; i < inst.length; i++) {
                // we ensure that there is no spaces before or after the request string
                // in order to not execute empty statements

                if (!inst[i].trim().equals("")) {

                    if (showSql) {
                        log4jLogger.log(Level.INFO, "SQL statement : " + inst[i]);
                    }
                    st.execute(normalizeString(inst[i]));
                }
            }
        } catch (Exception e) {
            log4jLogger.log(Level.ERROR, "Error execute queries " + e.getMessage());
        }

    }

    public void executeScript(InputStream sqlStream, HashMap<String,String> valueMap, boolean showSql) {
        String s = new String();
        StringBuffer sb = new StringBuffer();

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(sqlStream));

            while ((s = br.readLine()) != null) {
                sb.append(s).append("\n");
            }
            br.close();

            // here is our splitter ! We use ";" as a delimiter for each request
            // then we are sure to have well formed statements
            String[] inst = sb.toString().split(";");

            Statement st = connection.createStatement();

            for (int i = 0; i < inst.length; i++) {
                // we ensure that there is no spaces before or after the request string
                // in order to not execute empty statements

                if (!inst[i].trim().equals("")) {

                    String preparedString =normalizeString(inst[i],valueMap);

                    if (showSql) {
                        log4jLogger.log(Level.INFO, "SQL with prepared value statement : " + preparedString);
                    }
                    st.execute(preparedString);
                }
            }
        } catch (Exception e) {
            log4jLogger.log(Level.ERROR, "Error execute queries " + e.getMessage());
        }

    }

    /**
     * Преобразование строки SQL вызова
     * @param sourceStr - исходная строка
     * @return outputStr - преобразованная строка
     */
    private String normalizeString(String sourceStr) {

        String outputStr=sourceStr.replaceAll("\\Q--\\E(.*)\\n","\n");
        outputStr=outputStr.replaceAll("\r"," ");
        outputStr=outputStr.replaceAll("\t"," ");
        outputStr= StringUtils.normalizeSpace(outputStr);
        // String filter=inst[i].replaceAll("\\Q/*\\E(.|\\n)*?\\Q*/\\E","").trim();

        return outputStr;
    }

    private String normalizeString(String sourceStr, HashMap<String,String> valueMap) {

        String outputStr;

        if (valueMap.isEmpty()) {
            valueMap = new HashMap<>();
            valueMap.put("projectList",defaultProjectList);
            log4jLogger.log(Level.TRACE, "Use default projectList ");
        } else {
            log4jLogger.log(Level.TRACE, "Use projectList from parameters");
        }
        outputStr=convertString(sourceStr,valueMap);

        outputStr=outputStr.replaceAll("\\Q--\\E(.*)\\n","\n");
        outputStr=outputStr.replaceAll("\r"," ");
        outputStr=outputStr.replaceAll("\t"," ");
        outputStr= StringUtils.normalizeSpace(outputStr);


        return outputStr;
    }

    private String convertString(String source, HashMap<String,String> valueMap) {

        String workString=source;
        for (Map.Entry entry : valueMap.entrySet()) {
            workString = workString.replaceAll(":"+entry.getKey().toString(),entry.getValue().toString());
        }
        log4jLogger.log(Level.TRACE, "Replaced String" + workString);
        return workString;
    }


}
