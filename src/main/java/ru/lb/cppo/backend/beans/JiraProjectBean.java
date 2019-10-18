package ru.lb.cppo.backend.beans;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.JiraProject;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class JiraProjectBean {
    private static final Logger log4jLogger = Logger.getLogger(JiraProjectBean.class);
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext(unitName = "jirareport")
    private EntityManager entityManager;

    @PostConstruct
    private void init(){
        entityManager=entityManagerFactory.createEntityManager();
    }

    @PreDestroy
    private void cleanup(){
        if(entityManager.isOpen()){
            entityManager.close();
        }
    }

    private boolean isPersistedJP(JiraProject jiraProject) {
        JiraProject existingRow = entityManager.getReference(
                JiraProject.class, jiraProject.getProjectId());
/*        log4jLogger.log(Level.INFO,
                "Equals JiraProject row for " +
                        jiraProject.getProjectDescription() +
                        "(" + jiraProject.getProjectId().toString() + ") " + jiraProject.equals(existingRow));
*/        return jiraProject.equals(existingRow);
    }

    public void storeRow(JiraProject jiraProject) {
        if (jiraProject.isPersisted()) {
//            if (!isPersistedJP(jiraProject)) {
// todo add log message
//                throw new RuntimeException(
//                        "Changing JiraProject row is not permitted");
//            }
//            log4jLogger.log(Level.INFO,
//                    "Merge " + jiraProject.getProjectDescription());
            entityManager.merge(jiraProject);
        } else {
/*            log4jLogger.log(Level.INFO,
                    "Persist " + jiraProject.getProjectDescription());
*/            entityManager.persist(jiraProject);
        }
        entityManager.flush();
    }
/*
    public void add(JiraProject jiraProject) {
            log4jLogger.log(Level.INFO,
                    "Persist " + jiraProject.getProjectDescription());

        if (!entityManager.contains(jiraProject)) {
            entityManager.persist(jiraProject);
            entityManager.flush();
        } else {
            log4jLogger.log(Level.INFO,
                    "Already exsists " + jiraProject.getProjectDescription());
        }
    }
*/
    public void removeRow(JiraProject jiraProject) {
        jiraProject = entityManager.getReference(JiraProject.class, jiraProject.getProjectId());
        entityManager.remove(jiraProject);
        entityManager.flush();
    }

    public void detachAllRows() {
        List<JiraProject> jpList = getAllRow();
        Iterator<JiraProject> jiraProjectIterator = jpList.iterator();
        while (jiraProjectIterator.hasNext()) {
            JiraProject v_jiraProject = jiraProjectIterator.next();
            entityManager.detach(v_jiraProject);
        }
        log4jLogger.log(Level.INFO, "Rows detached");
    }

    public void deleteAllRows() {
        detachAllRows();
        Query query = entityManager.createQuery("DELETE FROM JiraProject ");
        try {
            query.executeUpdate();
        } catch (Exception sqle) {
            log4jLogger.log(Level.ERROR, "Error delete all rows" + sqle.getMessage());
        }
        entityManager.flush();
    }

    public List<JiraProject> getAllRow() {
        CriteriaQuery<JiraProject> cq = entityManager.getCriteriaBuilder()
                .createQuery(JiraProject.class);
        cq.select(cq.from(JiraProject.class));
        try {
            return entityManager.createQuery(cq).getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "*-* Failed execute Query getAllRow " + e.getMessage());
            return null;
        }
    }

    public List<JiraProject> getActiveRow() {

        Boolean flag=true;

        TypedQuery<JiraProject> query = entityManager.createQuery(
                "SELECT c FROM JiraProject c WHERE c.isActive = :flag",
                JiraProject.class);
        query.setParameter("flag", flag);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "*-* Failed execute Query getActiveRow " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public JiraProject getJiraProjectById(BigDecimal rowId) {
        TypedQuery<JiraProject> query = entityManager.createQuery(
                "SELECT c FROM JiraProject c WHERE c.projectId = :rowId",
                JiraProject.class);
        query.setParameter("rowId", rowId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public String getProjectQueryString(){
//        getAllRow().stream().map(jiraProject -> jiraProject.getProjectName()).reduce(String::concat).toString();

        return getAllRow().stream().filter(jiraProject-> jiraProject.getActive())
                .map(JiraProject::getProjectIdAsString)
                .collect(Collectors.joining(",","","")).toString();
    }

}
