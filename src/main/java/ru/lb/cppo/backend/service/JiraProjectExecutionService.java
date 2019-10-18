package ru.lb.cppo.backend.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.JiraProjectExecution;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class JiraProjectExecutionService {

    private static final Logger log4jLogger = Logger.getLogger(JiraProjectExecutionService.class);

    @EJB
    private JiraDBService jrrs;

    public List<JiraProjectExecution> findAll() {

        List<JiraProjectExecution> JiraProjectExecutionsList = new ArrayList<JiraProjectExecution>();
        try {
            String queryStr = new StringBuilder()
                    .append("select ProjectCode, ProjectVersion, WorkRole, ")
                    .append("TimeOrig, TimeSpn, TimeEst, ProzentRealize, ")
                    .append("DeltaRealize, ProzentDelta, VersionStatus ")
                    .append("from tmp_cppo_ProjectExecution ").toString();

            ResultSet rs = jrrs.executeSQLString(queryStr);
            if(rs != null) {

                while (rs.next()) {
                    JiraProjectExecutionsList.add(new JiraProjectExecution(
                        rs.getString("ProjectCode"),
                        rs.getString("ProjectVersion"),
                        rs.getString("WorkRole"),
                        rs.getBigDecimal("TimeOrig"),
                        rs.getBigDecimal("TimeSpn"),
                        rs.getBigDecimal("TimeEst"),
                        rs.getBigDecimal("ProzentRealize"),
                        rs.getBigDecimal("DeltaRealize"),
                        rs.getBigDecimal("ProzentDelta"),
                        rs.getString("VersionStatus"))
                    );
                }
            }
        } catch (SQLException ex) {
            log4jLogger.log(Level.ERROR, "Error "+ getClass().getCanonicalName()+"|"+ ex.getMessage());
        }
        return JiraProjectExecutionsList;
    }

    public List<JiraProjectExecution> findAllRows() {

        List<JiraProjectExecution> JiraProjectExecutionsList = new ArrayList<JiraProjectExecution>();
        try {
            String queryStr = new StringBuilder()
                    .append("select ProjectCode, ProjectID, ProjectMnem, ")
                    .append("VersionID, ProjectVersion, WorkRole, ")
                    .append("TimeOrig, TimeSpn, TimeEst, ProzentRealize, ")
                    .append("DeltaRealize, ProzentDelta, VersionStatus ")
                    .append("from tmp_cppo_ProjectExecution ").toString();

            ResultSet rs = jrrs.executeSQLString(queryStr);
            if(rs != null) {

                while (rs.next()) {
                    JiraProjectExecutionsList.add(new JiraProjectExecution(
                            rs.getString("ProjectCode"),
                            rs.getBigDecimal("ProjectID"),
                            rs.getString("ProjectMnem"),
                            rs.getBigDecimal("VersionID"),
                            rs.getString("ProjectVersion"),
                            rs.getString("WorkRole"),
                            rs.getBigDecimal("TimeOrig"),
                            rs.getBigDecimal("TimeSpn"),
                            rs.getBigDecimal("TimeEst"),
                            rs.getBigDecimal("ProzentRealize"),
                            rs.getBigDecimal("DeltaRealize"),
                            rs.getBigDecimal("ProzentDelta"),
                            rs.getString("VersionStatus"))
                    );
                }
            }
        } catch (SQLException ex) {
            log4jLogger.log(Level.ERROR, "Error "+ getClass().getCanonicalName()+"|"+ ex.getMessage());
        }
        return JiraProjectExecutionsList;
    }



    public List<JiraProjectExecution> findByCode(String code) {

        List<JiraProjectExecution> JiraProjectExecutionsList = new ArrayList<JiraProjectExecution>();
        try {
            String queryStr = new StringBuilder()
                    .append("select ProjectCode, ProjectVersion, WorkRole, ")
                    .append("TimeOrig, TimeSpn, TimeEst, ProzentRealize, ")
                    .append("DeltaRealize, ProzentDelta, VersionStatus ")
                    .append("from tmp_cppo_ProjectExecution ")
                    .append("where ProjectCode = \'")
                    .append(code.trim())
                    .append("\'")
                    .toString();

            ResultSet rs = jrrs.executeSQLString(queryStr);
            if(rs != null) {

                while (rs.next()) {
                    JiraProjectExecutionsList.add(new JiraProjectExecution(
                            rs.getString("ProjectCode"),
                            rs.getString("ProjectVersion"),
                            rs.getString("WorkRole"),
                            rs.getBigDecimal("TimeOrig"),
                            rs.getBigDecimal("TimeSpn"),
                            rs.getBigDecimal("TimeEst"),
                            rs.getBigDecimal("ProzentRealize"),
                            rs.getBigDecimal("DeltaRealize"),
                            rs.getBigDecimal("ProzentDelta"),
                            rs.getString("VersionStatus"))
                    );
                }
            }
        } catch (SQLException ex) {
            log4jLogger.log(Level.ERROR, "Error "+ getClass().getCanonicalName()+"|"+ ex.getMessage());
        }
        return JiraProjectExecutionsList;
    }

}
