package ru.lb.cppo.backend.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.CodeJiraIssues;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class CodeJiraIssuesService {

    private static final Logger log4jLogger = Logger.getLogger(CodeJiraIssuesService.class);

    @EJB
    private JiraDBService jrrs;

    public List<CodeJiraIssues> findAll()  {

        List<CodeJiraIssues> codeP = new ArrayList<CodeJiraIssues>();
        try {
            String queryStr = new StringBuilder()
                    .append("select RecordType, IssueID, ProjectID, ProjectCode, ProjectMnem, ")
                    .append("ProjectName, VersionID, ProjectVersion, VersionStatus, PriorityStr, jissuetype, ")
                    .append("IssueNum, jIssueStatus, Asigned, TimeOrig, TimeSpn, TimeEst, BuildNum, ")
                    .append("URL, WorkRole, ProjectPrev, ProjectSubst ")
                    .append("from tmp_cppo_issuePrepared;")
                    .toString();

            ResultSet rs = jrrs.executeSQLString(queryStr);
            while(rs.next()) {
                codeP.add(new CodeJiraIssues(
                rs.getString( "RecordType"),
                rs.getBigDecimal("IssueID"),
                rs.getBigDecimal("ProjectID"),
                rs.getString( "ProjectCode"),
                rs.getString( "ProjectMnem"),
                rs.getString( "ProjectName"),
                rs.getBigDecimal("VersionID"),
                rs.getString( "ProjectVersion"),
                rs.getString( "VersionStatus"),
                rs.getString( "PriorityStr"),
                rs.getString( "jissuetype"),
                rs.getString( "IssueNum"),
                rs.getString( "jIssueStatus"),
                rs.getString( "Asigned"),
                rs.getBigDecimal("TimeOrig"),
                rs.getBigDecimal("TimeSpn"),
                rs.getBigDecimal("TimeEst"),
                rs.getString( "BuildNum"),
                rs.getString( "URL"),
                rs.getString( "WorkRole"),
                rs.getString( "ProjectPrev"),
                rs.getString( "ProjectSubst")));
            }
        } catch (Exception ex) {
            log4jLogger.log(Level.ERROR, "Error fillAll " + ex.getMessage());
        }
        return codeP;
    }

}
