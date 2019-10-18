package ru.lb.cppo.backend.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.ProjectUserRole;
import ru.lb.cppo.backend.pojo.PlainJiraUserRole;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ProjectUserRoleService {

    private static final Logger log4jLogger = Logger.getLogger(ProjectUserRoleService.class);
    private String insertString="insert into tmp_cppo_user_role(USER_KEY,USER_ROLE) "
            + "values (\':juserName\', \':juserRole\');";

    @EJB
    private JiraDBService jrrs;

    public List<PlainJiraUserRole> findAll()  {

        List<PlainJiraUserRole> jUserRole = new ArrayList<PlainJiraUserRole>();
        try {

            String queryStr = new StringBuilder()
            .append("select usrrl.userName, usrrl.displayName, usrrl.UserRole from ")
            .append("( select userName, displayName, ifnull(USER_ROLE,'-') UserRole ")
            .append("from ( select distinct userName, displayName ")
            .append("from tmp_cppo_userRoleProject ) uroleproject left outer join ")
            .append("tmp_cppo_user_role urole on urole.user_key=uroleproject.username ) usrrl;").toString();

            ResultSet rs = jrrs.executeSQLString(queryStr);
            while(rs.next()) {
                jUserRole.add(new PlainJiraUserRole(
                            rs.getString("userName"),
                            rs.getString("displayName"),
                            rs.getString("userRole")
                            ));
            }
        } catch (Exception ex) {
            log4jLogger.log(Level.ERROR, "Error findAll " + ex.getMessage());
        }
        return jUserRole;
    }

    public Boolean storeRow(ProjectUserRole purRow) throws SQLException {

        HashMap<String,String> hashmap = new HashMap<>();
        hashmap.put("juserName",purRow.getJuserName());
        hashmap.put("juserRole",purRow.getRoleName());
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
