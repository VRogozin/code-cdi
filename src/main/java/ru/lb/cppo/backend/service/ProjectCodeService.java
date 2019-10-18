package ru.lb.cppo.backend.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.pojo.PlainProjectCode;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ProjectCodeService {

    private static final Logger log4jLogger = Logger.getLogger(ProjectCodeService.class);

    @EJB
    private JiraDBService jrrs;

    public List<PlainProjectCode> findAll()  {

        List<PlainProjectCode> codeP = new ArrayList<PlainProjectCode>();
        try {

            String queryStr = new StringBuilder()
            .append("select distinct id codeID, codeProject codeName, if((pkey='none'),false, true) isLive ")
            .append("from ( select cp_all.id, cp_all.codeProject, ifNULL(cp_live.pkey,'none') pkey ")
            .append("from ( select distinct cfo.id, cfo.customvalue codeProject ")
            .append("from customfield cf, customfieldoption cfo where cf.cfname='Код проекта' ")
            .append("and cf.ID=cfo.CUSTOMFIELD order by codeProject) cp_all ")
            .append("left outer join ( select distinct p.pkey, cfo.id, cfo.customvalue codeProject ")
            .append("from project p, jiraissue ji, customfieldvalue cfv, customfield cf, ")
            .append("customfieldoption cfo where cf.cfname='Код проекта' and ji.PROJECT=p.id ")
            .append("and ji.created> STR_TO_DATE('01,1,2017','%d,%m,%Y') and cfv.ISSUE=ji.ID ")
            .append("and cf.ID=cfv.CUSTOMFIELD and cf.ID=cfo.CUSTOMFIELD and cfo.ID=cfv.STRINGVALUE ")
            .append(") cp_live on cp_all.id=cp_live.id ) code_project order by isLive desc, codeName;").toString();

            ResultSet rs = jrrs.executeSQLString(queryStr);
            while(rs.next()) {
                codeP.add(new PlainProjectCode(
                            rs.getBigDecimal("codeID"),
                            rs.getString("codeName"),
                            "",
                            rs.getBoolean("isLive")
                            ));
            }
        } catch (Exception ex) {
            log4jLogger.log(Level.ERROR, "Error fillAll " + ex.getMessage());
        }
        return codeP;
    }

    public PlainProjectCode findByName(String codeName)  {

        PlainProjectCode codeP = null;
        try {
            String queryStr = new StringBuilder()
                    .append("select distinct cfo.id codeID, cfo.customvalue codeName ")
                    .append("from Customfield cf, Customfieldoption cfo ")
                    .append("where cf.cfname='Код проекта' and cf.ID=cfo.CUSTOMFIELD ")
                    .append("and cfo.customvalue='")
                    .append(codeName)
                    .append("';")
                    .toString();
            ResultSet rs = jrrs.executeSQLString(queryStr);
            while(rs.next()) {
                codeP = new PlainProjectCode(rs.getBigDecimal("codeID"),rs.getString("codeName"));
            }
        } catch (Exception ex) {
            log4jLogger.log(Level.ERROR, "Error find by Code " + ex.getMessage());
        }
        return codeP;
    }

}
