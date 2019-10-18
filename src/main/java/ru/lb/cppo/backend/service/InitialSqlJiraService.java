package ru.lb.cppo.backend.service;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Stateless;
import java.io.File;
import java.net.URL;
import java.util.Enumeration;

@Stateless
@SuppressWarnings("unchecked")
public class InitialSqlJiraService {

    private static final Logger log4jLogger = Logger.getLogger(InitialSqlJiraService.class);

    @Resource
    EJBContext context;

    @PostConstruct
    @PostActivate
    public void openConnection(){
        log4jLogger.log(Level.INFO, "create InitialSqlJiraService");
        try {

        } catch (Exception sqle) {
            log4jLogger.log(Level.ERROR, "Error create" + sqle.getMessage());
        }
    }

    @PrePassivate
    @PreDestroy
    public void cleanup () {
        try {
            log4jLogger.log(Level.INFO, "destroy InitialSqlJiraService");

        } catch ( Exception sqle) {
            log4jLogger.log(Level.ERROR, "Error destroy" + sqle.getMessage());
        }
    }

    public void preparedSqlExecute()  {

        try {
            Enumeration<URL> urls = getClass().getClassLoader().getResources("/sql/");
            while (urls.hasMoreElements()){
                URL url = urls.nextElement();
                File file = new File(url.toURI());
                log4jLogger.log(Level.INFO, "SQL uri : " + file.getName());
                File fileNa = new File(url.getFile());
                log4jLogger.log(Level.INFO, "SQL file : " + file.getName());
            }
        } catch (Exception ex) {
            log4jLogger.log(Level.ERROR, "Error InitialSqlJiraService " + ex.getMessage());
        }
    }
}
