package ru.lb.cppo.backend.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.ProjectCodePlan;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
public class ProjectCodePlanService {

    private static final Logger log4jLogger = Logger.getLogger(ProjectCodePlanService.class);
    private String insertString= "insert into tmp_cppo_PlanCode(ProjectCode,VersionID,ProjectVersion,WorkRole,PlanOrig,"
            + "VersionStatus) values (\':code\', \':versionID\', \':version\', \':role\', :plan, \':status\');";

    @EJB
    private JiraDBService jrrs;

    public List<ProjectCodePlan> findAll() {
        List<ProjectCodePlan> projectCodePlans = new ArrayList<ProjectCodePlan>();
        try {
            String queryStr = new StringBuilder()
            .append(" select t11.ProjectCode, t11.VersionID, t11.WorkRole, t11.ProjectVersion, ")
            .append(" t11.PlanOrig, t11.FactOrig, v_version.VersionStatus ")
            .append(" from ( select t5.ProjectCode, t5.VersionID, t5.WorkRole, t5.ProjectVersion, ")
            .append(" ifnull(t6.PlanOrig, 0.00) PlanOrig, ifnull(t6.FactOrig, 0.00) FactOrig ")
            .append(" from tmp_cppo_VersionAndRole t5  left outer join ")
            .append(" ( select t1.ProjectCode, t1.VersionID, t1.WorkRole, t1.ProjectVersion, ")
            .append(" round(sum(t1.PlanOrig),2) as PlanOrig, round(sum(t1.FactOrig),2) as FactOrig ")
            .append(" from ( select ProjectCode, VersionID, WorkRole, ProjectVersion, 0.00 as PlanOrig, ")
            .append(" round(sum(TimeSpn)) FactOrig from tmp_cppo_ProjectExecution group by 1,2,3 ")
            .append(" union all select ProjectCode, VersionID, WorkRole, ProjectVersion, PlanOrig, ")
            .append(" 0.00 as FactOrig from tmp_cppo_PlanCode ) t1 group by 1,2,3 order by 1,2,3 ) t6 ")
            .append(" on (t5.ProjectCode=t6.ProjectCode and t5.VersionID=t6.VersionID  ")
            .append(" and t5.WorkRole = t6.WorkRole) order by 1,2,3 ) t11 left outer join  ")
            .append(" ( select pv.id, if (pv.archived='true' ,'Archived', if (pv.released='true' ")
            .append(" ,'Released', if(pv.startdate<now() , 'Started','Planned'))) as versionStatus ")
            .append(" from projectversion pv ) v_version on t11.VersionID=v_version.id order by 1,2,3; ")
            .toString();

            ResultSet rs = jrrs.executeSQLString(queryStr);
            while (rs.next()) {
                projectCodePlans.add(new ProjectCodePlan(
                        rs.getString("ProjectCode"),
                        rs.getBigDecimal("VersionID"),
                        rs.getString("ProjectVersion"),
                        rs.getString("WorkRole"),
                        rs.getBigDecimal("PlanOrig"),
                        rs.getBigDecimal("FactOrig"),
                        rs.getString("VersionStatus" )
                        )
                );
            }
        } catch (SQLException ex) {
            projectCodePlans=Collections.emptyList();
            log4jLogger.log(Level.ERROR, "Error get all rows " + ex.getMessage());
        }
        return projectCodePlans;
    }

    public List<ProjectCodePlan> getActiveRow() {
        List<ProjectCodePlan> pcpList = findAll();
        //* todo проверка версии
        pcpList.stream()
                .filter(projectCodePlan -> projectCodePlan.getVersionID().compareTo(BigDecimal.ZERO)!=0)
                .distinct()
                .collect(Collectors.toList());
        if (pcpList.isEmpty()) {
            return Collections.emptyList();
        }
        return pcpList;
    }

    public Boolean storeRow(ProjectCodePlan pcpRow) throws SQLException {

        HashMap<String,String> hashmap = new HashMap<>();
        hashmap.put("code",pcpRow.getProjectCode());
        if (pcpRow.getVersionID()!= null) {
            hashmap.put("versionID" , pcpRow.getVersionID().toString());
        } else {
            hashmap.put("versionID" , BigDecimal.ZERO.toString());
        }
        hashmap.put("version",pcpRow.getProjectVersion());
        hashmap.put("role",pcpRow.getWorkRole());
        hashmap.put("plan",pcpRow.getPlanOrig().toString());
        hashmap.put("status",pcpRow.getVersionStatus().toString());
        return jrrs.insertSQLString(convertString(insertString,hashmap));
    }

    private String convertString(String source, HashMap<String,String> valueMap) {

        String workString=source;
        for (Map.Entry entry : valueMap.entrySet()) {
            workString = workString.replaceAll(":"+entry.getKey().toString(),entry.getValue().toString());
        }
//        log4jLogger.log(Level.INFO, "Replaced String" + workString);
        return workString;
    }

}
