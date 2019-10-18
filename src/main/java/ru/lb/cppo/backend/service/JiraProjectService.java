package ru.lb.cppo.backend.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.JiraProject;
import ru.lb.cppo.backend.pojo.PlainJiraProject;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class JiraProjectService {

    private static final Logger log4jLogger = Logger.getLogger(JiraProjectService.class);

    private String insertString="insert into tmp_cppo_stat_project(PID,PMNEM, PCode, SmallCode) "
            + "values (\':projectId\', \':projectName\', \':projectDescription\', \':shortCode\');";

    @EJB
    private JiraDBService jrrs;

    public List<PlainJiraProject> findAll()  {

        List<PlainJiraProject> jiraP = new ArrayList<PlainJiraProject>();
        try {

            jrrs.createInitialTable();
            String queryStr = new StringBuilder()
                    .append("select id, ProjectMnem, pname, ")
                    .append(" ifNULL(SmallCode,'') smallcode, isLive from ( ")
                    .append(" select project_l.*, tcsp.SmallCode from ( ")
                    .append(" select id, pkey ProjectMnem, pname , ifnull(isLive,false) isLive ")
                    .append(" from ( select v_project.id, v_project.pkey, v_project.pname, project_live.isLive ")
                    .append(" from ( select p.id, p.pkey, p.pname from project p ) v_project ")
                    .append(" left outer join ( select distinct p.id, p.pkey, p.pname, true islive ")
                    .append(" from project p, jiraissue ji, customfieldvalue cfv, customfield cf, ")
                    .append(" customfieldoption cfo where cf.cfname='Код проекта' and ji.PROJECT=p.id ")
                    .append(" and ji.created> STR_TO_DATE('01,1,2017','%d,%m,%Y') and cfv.ISSUE=ji.ID ")
                    .append(" and cf.ID=cfv.CUSTOMFIELD and cf.ID=cfo.CUSTOMFIELD and cfo.ID=cfv.STRINGVALUE ")
                    .append(" ) project_live on v_project.id=project_live.id ) v_project_live ")
                    .append(" ) project_l left outer join tmp_cppo_stat_project tcsp on project_l.id=tcsp.pid ")
                    .append(" ) project_finaly ")
                    .append(" order by isLive desc, ProjectMnem; ").toString();
            ResultSet rs = jrrs.executeSQLString(queryStr);
            while(rs.next()) {
                jiraP.add(new PlainJiraProject(
                                rs.getBigDecimal("id"),
                                rs.getString("ProjectMnem"),
                                rs.getString("pname"),
                                rs.getString("smallCode"),
                                rs.getBoolean("isLive")
                        )
                );
            }
        } catch (Exception ex) {
            log4jLogger.log(Level.ERROR, "Error fillAll project " + ex.getMessage());
        }
        return jiraP;
    }

    public List<PlainJiraProject> findAllLive()  {

        List<PlainJiraProject> jiraP = new ArrayList<PlainJiraProject>();
        try {
            String queryStr = new StringBuilder()
                    .append("select distinct p.id, p.pkey ProjectMnem, p.pname, true islive ")
                    .append("from project p, jiraissue ji, customfieldvalue cfv, customfield cf, ")
                    .append("customfieldoption cfo where cf.cfname='Код проекта' and ji.PROJECT=p.id ")
                    .append("and ji.created> STR_TO_DATE('01,1,2017','%d,%m,%Y') and cfv.ISSUE=ji.ID ")
                    .append("and cf.ID=cfv.CUSTOMFIELD and cf.ID=cfo.CUSTOMFIELD and cfo.ID=cfv.STRINGVALUE ")
                    .append("order by isLive desc, ProjectMnem ").toString();
            ResultSet rs = jrrs.executeSQLString(queryStr);
            while(rs.next()) {
                jiraP.add(new PlainJiraProject(
                                rs.getBigDecimal("id"),
                                rs.getString("ProjectMnem"),
                                rs.getString("pname"),
                                rs.getBoolean("isLive")
                        )
                );
            }
        } catch (Exception ex) {
            log4jLogger.log(Level.ERROR, "Error fillAllLive project " + ex.getMessage());
        }
        return jiraP;
    }

    public Boolean storeRow(JiraProject jpRow) throws SQLException {
        if (jpRow != null) {
            HashMap<String,String> hashmap = new HashMap<>();
            hashmap.put("projectId", checkNullString(jpRow.getProjectIdAsString()));
            hashmap.put("projectName",checkNullString(jpRow.getProjectName()));
            hashmap.put("projectDescription",checkNullString(jpRow.getProjectDescription()));
            hashmap.put("shortCode",checkNullString(jpRow.getShortCode()));
//            log4jLogger.log(Level.INFO,"hasmap |"+ hashmap.toString()+"|");
            return jrrs.insertSQLString(convertString(insertString,hashmap));
        } else {
            return false;
        }
    }

    private String checkNullString(String source){
        String outputString="";
        if (source != null) {
            return source;
        } else {
            return outputString;
        }
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
