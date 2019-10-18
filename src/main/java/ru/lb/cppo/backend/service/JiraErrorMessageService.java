package ru.lb.cppo.backend.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.JiraErrorMessage;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Stateless
public class JiraErrorMessageService {

    private static final Logger log4jLogger = Logger.getLogger(JiraErrorMessageService.class);

    @EJB
    private JiraDBService jrrs;

    public List<JiraErrorMessage> findAll() {

        List<JiraErrorMessage> jiraErrorMessageList = new ArrayList<JiraErrorMessage>();
        try {
            String queryStr = new StringBuilder()
            .append("select isf.ProjectCode, isf.VersionID, isf.ProjectVersion, isf.ProjectName, ")
            .append("isf.IssueNum, je.IssueID, je.ErrCode, je.ErrMessage ")
            .append("from tmp_cppo_JiraWarning je, tmp_cppo_issuefilled isf ")
            .append("where je.IssueID=isf.IssueID ")
            .append("order by isf.ProjectCode, isf.ProjectVersion, je.IssueID ")
            .toString();

            ResultSet rs = jrrs.executeSQLString(queryStr);
            while (rs.next()) {
                jiraErrorMessageList.add(new JiraErrorMessage(
                        rs.getString("ProjectCode"),
                        rs.getBigDecimal("VersionID"),
                        rs.getString("ProjectVersion"),
                        rs.getString("ProjectName"),
                        rs.getString("IssueNum"),
                        rs.getBigDecimal("IssueID"),
                        rs.getString("ErrCode"),
                        rs.getString("ErrMessage"))
                );
            }
        } catch (SQLException ex) {
            log4jLogger.log(Level.ERROR, "Error load error message"+ ex.getMessage());
            return Collections.emptyList();
        }
        return jiraErrorMessageList;
    }

}
