package ru.lb.cppo.backend.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.JiraUserRole;
import ru.lb.cppo.backend.pojo.PlainJiraUserRole;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class JiraUserRoleService {

    private static final Logger log4jLogger = Logger.getLogger(JiraUserRoleService.class);

    @EJB
    private JiraDBService jrrs;

    public List<JiraUserRole> findAll() {
        List<JiraUserRole> jiraUserRolesList = new ArrayList<JiraUserRole>();
        try {
            String queryStr = new StringBuilder()
                    .append("select trim(cu.display_name) juserDispName, tmpur.user_role juserRole, ")
                    .append("au.user_key juserKey, cu.lower_user_name juserName ")
                    .append("from tmp_cppo_user_role tmpur, app_user au, cwd_user cu ")
                    .append("where tmpur.user_key = au.lower_user_name and ")
                    .append("au.lower_user_name=cu.lower_user_name ").toString();

            ResultSet rs = jrrs.executeSQLString(queryStr);
            while (rs.next()) {
                jiraUserRolesList.add(new JiraUserRole(
                        rs.getString("juserDispName"),
                        rs.getString("juserRole"),
                        rs.getString("juserKey"),
                        rs.getString("juserName"))
                );
            }
        } catch (SQLException ex) {
            log4jLogger.log(Level.ERROR, "Error findAll |"+ ex.getMessage());
        }
        return jiraUserRolesList;
    }

    public BigDecimal getCountNotResponsed() {
        BigDecimal cnt = BigDecimal.ZERO;
        try {
            String queryStr = new StringBuilder()
                    .append("select count(*) cnt ")
                    .append("from ( select usrrl.userName, usrrl.UserRole from ")
                    .append("( select userName, ifnull(USER_ROLE,'-') UserRole ")
                    .append("from ( select distinct userName from tmp_cppo_userRoleProject ")
                    .append(") uroleproject left outer join tmp_cppo_user_role urole ")
                    .append("on urole.user_key=uroleproject.username ) usrrl ) aaa ")
                    .append("where aaa.UserRole='-'; ").toString();
            ResultSet rs = jrrs.executeSQLString(queryStr);
            while (rs.next()) {
                cnt=rs.getBigDecimal("cnt");
            }
        } catch (SQLException ex) {
            log4jLogger.log(Level.ERROR, "Error getCountNotResponsed |"+ ex.getMessage());
        }
        return cnt;
    }

    public List<PlainJiraUserRole> getActiveUserRoles() {
        List<PlainJiraUserRole> pjuList = new ArrayList<PlainJiraUserRole>();
        try {
            String queryStr = new StringBuilder()
                    .append("select usrrl.userName, usrrl.displayName, usrrl.UserRole from ")
                    .append("( select userName, displayName , ifnull(USER_ROLE,'-') UserRole ")
                    .append("from ( select distinct userName, displayName from tmp_cppo_userRoleProject ")
                    .append(") uroleproject left outer join tmp_cppo_user_role urole ")
                    .append("on urole.user_key=uroleproject.username ) usrrl; ").toString();
            ResultSet rs = jrrs.executeSQLString(queryStr);
            while (rs.next()) {
                    pjuList.add(new PlainJiraUserRole(
                    rs.getString("userName"),
                    rs.getString("displayName"),
                    rs.getString("UserRole"))
                );
            }
        } catch (SQLException ex) {
            log4jLogger.log(Level.ERROR, "Error getActiveUserRoles |"+ ex.getMessage());
        }
        return pjuList;
    }

    public BigDecimal getCountUsers() {
        BigDecimal cnt = BigDecimal.ZERO;
        try {
            String queryStr = new StringBuilder()
                    .append("select count(*) cnt from tmp_cppo_userRoleProject; ").toString();
            ResultSet rs = jrrs.executeSQLString(queryStr);
            while (rs.next()) {
                cnt=rs.getBigDecimal("cnt");
            }
        } catch (SQLException ex) {
            log4jLogger.log(Level.ERROR, "Error count in tmp_cppo_userRoleProject |"+ ex.getMessage());
        }

        return cnt;
    }

}
