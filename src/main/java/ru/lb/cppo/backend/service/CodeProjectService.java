package ru.lb.cppo.backend.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.CodeProject;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class CodeProjectService {

    private static final Logger log4jLogger = Logger.getLogger(CodeProjectService.class);

    @EJB
    private JiraDBService jrrs;

    public List<CodeProject> findAll()  {

        List<CodeProject> codeP = new ArrayList<>();
        try {
            String queryStr = new StringBuilder()
                    .append("select distinct cfo.id codeID, cfo.customvalue codeName ")
                    .append("from Customfield cf, Customfieldoption cfo ")
                    .append("where cf.cfname='Код проекта' and cf.ID=cfo.CUSTOMFIELD ").toString();
            ResultSet rs = jrrs.executeSQLString(queryStr);
            while(rs.next()) {
                codeP.add(new CodeProject(rs.getBigDecimal("codeID"),rs.getString("codeName")));
            }
        } catch (Exception ex) {
            log4jLogger.log(Level.ERROR, "Error fillAll " + ex.getMessage());
        }
        return codeP;
    }

    public CodeProject findByName(String codeName)  {

        CodeProject codeP = null;
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
                codeP = new CodeProject(rs.getBigDecimal("codeID"),rs.getString("codeName"));
            }
        } catch (Exception ex) {
            log4jLogger.log(Level.ERROR, "Error find by Code " + ex.getMessage());
        }
        return codeP;
    }


}
