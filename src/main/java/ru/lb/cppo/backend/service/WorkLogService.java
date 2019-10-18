package ru.lb.cppo.backend.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.beans.JiraProjectBean;
import ru.lb.cppo.backend.pojo.PlainWorkLog;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class WorkLogService {

    private static final Logger log4jLogger = Logger.getLogger(WorkLogService.class);

    @EJB
    private JiraDBService jrrs;
    private JiraProjectBean jiraProjectBean;


    public List<PlainWorkLog> findAll()  {

        List<PlainWorkLog> workLogP = new ArrayList<PlainWorkLog>();
        try {

            String queryStr = new StringBuilder()
                    .append("select t.ID, t.issueid, t.AUTHOR, t.UPDATEAUTHOR, t.worklogbody, t.created, t.UPDATED, ")
                    .append("t.STARTDATE, t.timeworked, t.timeworked/3600 tchasy from ( select distinct ")
                    .append("ji.ID issue_id from project p, jiraissue ji where p.id in (:projectList) ")
                    .append("and ji.PROJECT=p.id and ji.created> STR_TO_DATE(':startDate','%d,%m,%Y')) ")
                    .append("ji_issues, worklog t where t.startdate>STR_TO_DATE(':startWorkLogDate','%d,%m,%Y') ")
                    .append(" and ji_issues.issue_id=t.issueid; ").toString();


            ResultSet rs = jrrs.executeSQLString(queryStr);
            while(rs.next()) {
                workLogP.add(new PlainWorkLog(
                        rs.getBigDecimal("id"),
                        rs.getBigDecimal("issueID"),
                        rs.getString("author"),
                        rs.getString("updateAuthor"),
                        rs.getString("workLogBody"),
                        rs.getDate("created"),
                        rs.getDate("updated"),
                        rs.getDate("startDate"),
                        rs.getBigDecimal("timeWorked")
                        )
                );
            }
        } catch (Exception ex) {
            log4jLogger.log(Level.ERROR, "Error fillAll worklog " + ex.getMessage());
        }
        return workLogP;
    }

    private HashMap<String,String> getParametersHashMap(){

          HashMap<String,String> valueMap = new HashMap<>();
          valueMap.put("projectList", jiraProjectBean.getProjectQueryString());

          return valueMap;
    }

    private String convertString(String source, HashMap<String,String> valueMap) {

        String workString=source;
        for (Map.Entry entry : valueMap.entrySet()) {
            workString = workString.replaceAll(":"+entry.getKey().toString(),entry.getValue().toString());
        }
  //      log4jLogger.log(Level.INFO, "Replaced String |" + workString +"|");
        return workString;
    }

}
