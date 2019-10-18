package ru.lb.cppo.backend.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.pojo.PlainProjectVersion;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ProjectVersionService {

    private static final Logger log4jLogger = Logger.getLogger(ProjectVersionService.class);

    @EJB
    private JiraDBService jrrs;

    public List<PlainProjectVersion> findAll()  {

        List<PlainProjectVersion> codeP = new ArrayList<PlainProjectVersion>();
        try {
            String queryStr = new StringBuilder()
            .append("select distinct na.SINK_NODE_ID version_id , p.id projectID ")
            .append(" , pv.vname , if (pv.archived='true' ,'Archived', ")
            .append(" if (pv.released='true','Released', ")
            .append(" if(pv.startdate<now() , 'Started','Planned'))) as ")
            .append(" versionStatus, pv.startdate , pv.releasedate , true isLive ")
            .append(" from project p, jiraissue ji, nodeassociation na , projectversion pv ")
            .append(" where p.id in ( select distinct p.id from project p, ")
            .append(" jiraissue ji, customfieldvalue cfv, customfield cf, customfieldoption cfo ")
            .append(" where cf.cfname='Код проекта' and ji.PROJECT=p.id ")
            .append(" and ji.created> STR_TO_DATE('01,1,2017','%d,%m,%Y') ")
            .append(" and cfv.ISSUE=ji.ID and cf.ID=cfv.CUSTOMFIELD ")
            .append(" and cf.ID=cfo.CUSTOMFIELD and cfo.ID=cfv.STRINGVALUE) ")
            .append(" and ji.PROJECT=p.id and ji.id=na.SOURCE_NODE_ID ")
            .append(" and ji.created>STR_TO_DATE('01,1,2017','%d,%m,%Y') ")
            .append(" and na.SOURCE_NODE_ENTITY='Issue' ")
            .append(" and na.ASSOCIATION_TYPE='IssueFixVersion' ")
            .append(" and pv.id=na.SINK_NODE_ID; ").toString();

            ResultSet rs = jrrs.executeSQLString(queryStr);
            while(rs.next()) {
                codeP.add(new PlainProjectVersion(
                            rs.getBigDecimal("version_id"),
                            rs.getString("vname"),
                            rs.getBigDecimal("projectID"),
                            rs.getDate("startdate"),
                            rs.getDate("releasedate"),
                            rs.getString("versionStatus"),
                            rs.getBoolean("isLive")
                            ));
            }
        } catch (Exception ex) {
            log4jLogger.log(Level.ERROR, "Error get list version " + ex.getMessage());
        }
        return codeP;
    }

}
