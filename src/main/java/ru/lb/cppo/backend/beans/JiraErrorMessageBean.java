package ru.lb.cppo.backend.beans;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.JiraErrorMessage;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Stateless
public class JiraErrorMessageBean {
    private static final Logger log4jLogger = Logger.getLogger(JiraErrorMessageBean.class);

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

    private boolean isPersistedJEM(JiraErrorMessage jiraErrorMessage) {
        JiraErrorMessage existingRow = entityManager.getReference(
                JiraErrorMessage.class, jiraErrorMessage.getId());
        return jiraErrorMessage.equals(existingRow);
    }

    public void storeRow(JiraErrorMessage jiraErrorMessage) {
        if (jiraErrorMessage.isPersisted()) {
//            if (!isPersistedJEM(jiraErrorMessage)) {
// todo add log message
//                throw new RuntimeException(
//                        "Changing JiraErrorMessage row is not permitted");
//            }
            entityManager.merge(jiraErrorMessage);
        } else {
            entityManager.persist(jiraErrorMessage);
        }
        entityManager.flush();
    }

    public void flush(){
        if(entityManager.isOpen()){
            entityManager.flush();
            entityManager.clear();
        }
    }

    public void add(JiraErrorMessage jiraErrorMessage){
        entityManager.persist(jiraErrorMessage);
        entityManager.flush();
    }

    public void removeRow(JiraErrorMessage jiraErrorMessage) {
        jiraErrorMessage = entityManager.getReference(JiraErrorMessage.class, jiraErrorMessage.getId());
        entityManager.remove(jiraErrorMessage);
        entityManager.flush();
    }

    public void deleteAllRows() {
        Query query = entityManager.createQuery("DELETE FROM JiraErrorMessage ");
        try {
            query.executeUpdate();
        } catch (Exception sqle) {
            log4jLogger.log(Level.ERROR, "Error delete all rows" + sqle.getMessage());
        }
    }

    public List<JiraErrorMessage> getAllRow() {
        CriteriaQuery<JiraErrorMessage> cq = entityManager.getCriteriaBuilder()
                .createQuery(JiraErrorMessage.class);
        cq.select(cq.from(JiraErrorMessage.class));
        try {
            return entityManager.createQuery(cq).getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "*-* Failed execute Query getAllRow " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Collection<JiraErrorMessage> getFilteredJiraErrorMessages(String codeProject,String version ) {

        log4jLogger.log(Level.INFO, "#getFilteredJiraErrorMessages# "
                + " Код проекта |"+codeProject+"|"
                + " Версия |"+version+"|");

        StringBuilder sb = new StringBuilder("SELECT c FROM JiraErrorMessage c ");
        boolean flagWhere=false;
        if (codeProject !=null && !codeProject.isEmpty()) {
            sb.append("WHERE c.projectCode =:codeProject ");
            flagWhere=true;
        }
        if (version !=null && !version.isEmpty()) {
            if (!flagWhere) {
                sb.append(" WHERE ");
                flagWhere=true;
            } else {
                sb.append(" and ");
            }
            sb.append(" c.projectVersion =:version ");
        }
        sb.append(" order by c.projectCode, c.projectVersion ");
        log4jLogger.log(Level.INFO, "***FilteredJiraErrorMessage*** Запрос:|" + sb.toString()+"|");
        TypedQuery<JiraErrorMessage> query = entityManager.createQuery(
                sb.toString(),JiraErrorMessage.class);
        if (codeProject !=null && !codeProject.isEmpty()) {
            query.setParameter("codeProject", codeProject);
        }
        if (version !=null && !version.isEmpty()) {
            query.setParameter("version", version);
        }

        try {
            return query.getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "*-* FilteredJiraErrorMessage нет данных " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Collection<JiraErrorMessage> getFilteredJiraErrorMessages(String codeProject,BigDecimal versionID ) {

        log4jLogger.log(Level.INFO, "#getFilteredJiraErrorMessages# "
                + " Код проекта |"+codeProject+"|"
                + " ID Версии |"+versionID.toString()+"|");

        StringBuilder sb = new StringBuilder("SELECT c FROM JiraErrorMessage c ");
        boolean flagWhere=false;
        if (codeProject !=null && !codeProject.isEmpty()) {
            sb.append("WHERE c.projectCode =:codeProject ");
            flagWhere=true;
        }
        if (versionID !=null && versionID.compareTo(BigDecimal.ZERO)!=0) {
            if (!flagWhere) {
                sb.append(" WHERE ");
                flagWhere=true;
            } else {
                sb.append(" and ");
            }
            sb.append(" c.versionID =:versionid ");
        }
        sb.append(" order by c.projectCode, c.projectVersion ");
        log4jLogger.log(Level.INFO, "***FilteredJiraErrorMessage*** Запрос:|" + sb.toString()+"|");
        TypedQuery<JiraErrorMessage> query = entityManager.createQuery(
                sb.toString(),JiraErrorMessage.class);
        if (codeProject !=null && !codeProject.isEmpty()) {
            query.setParameter("codeProject", codeProject);
        }
        if (versionID !=null && versionID.compareTo(BigDecimal.ZERO)!=0) {
            query.setParameter("versionid", versionID);
        }

        try {
            return query.getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "*-* FilteredJiraErrorMessage нет данных " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public JiraErrorMessage getJiraErrorMessageById(BigDecimal rowId) {
        TypedQuery<JiraErrorMessage> query = entityManager.createQuery(
                "SELECT c FROM JiraErrorMessage c WHERE c.id = :rowId",
                JiraErrorMessage.class);
        query.setParameter("rowId", rowId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
