package ru.lb.cppo.backend.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.JiraStatProject;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class JiraStatProjectService {

    private static final Logger log4jLogger = Logger.getLogger(JiraStatProjectService.class);

    @EJB
    private JiraDBService jrrs;

    public List<JiraStatProject> findAll() {

        List<JiraStatProject> jiraStatProjectsList = new ArrayList<JiraStatProject>();
        try {
            String queryStr = new StringBuilder()
                    .append("select PID, PMNEM, PCode, SmallCode ")
                    .append("from tmp_cppo_stat_project").toString();

            ResultSet rs = jrrs.executeSQLString(queryStr);
            while (rs.next()) {
                jiraStatProjectsList.add(new JiraStatProject(
                        rs.getBigDecimal("PID"),
                        rs.getString("PMNEM"),
                        rs.getString("PCode"),
                        rs.getString("SmallCode"))
                );
            }
        } catch (SQLException ex) {
            log4jLogger.log(Level.ERROR, "Error JiraStatProject FindAll " + ex.getMessage());
        }
        return jiraStatProjectsList;
    }

}
