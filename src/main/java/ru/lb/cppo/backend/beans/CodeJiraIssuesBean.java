package ru.lb.cppo.backend.beans;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.CodeJiraIssues;

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
import java.util.Collection;

@Stateless
public class CodeJiraIssuesBean {
    private static final Logger log4jLogger = Logger.getLogger(CodeJiraIssuesBean.class);

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

    private boolean isPersistedCJIB(CodeJiraIssues codeJiraIssues) {
        CodeJiraIssues existingRow = entityManager.getReference(
                CodeJiraIssues.class, codeJiraIssues.getId());
        return codeJiraIssues.equals(existingRow);
    }

    public void storeRow(CodeJiraIssues codeJiraIssues) {
        if (codeJiraIssues.isPersisted()) {
            entityManager.merge(codeJiraIssues);
        } else {
            entityManager.persist(codeJiraIssues);
        }
        entityManager.flush();
    }

    public void flush(){
        if(entityManager.isOpen()){
            entityManager.flush();
            entityManager.clear();
        }
    }

    public void add(CodeJiraIssues codeJiraIssues){
        entityManager.persist(codeJiraIssues);
        entityManager.flush();
    }

    public void removeRow(CodeJiraIssues codeJiraIssues) {
        codeJiraIssues = entityManager.getReference(CodeJiraIssues.class, codeJiraIssues.getId());
        entityManager.remove(codeJiraIssues);
        entityManager.flush();
    }

    public Collection<CodeJiraIssues> getAllRow() {
        CriteriaQuery<CodeJiraIssues> cq = entityManager.getCriteriaBuilder()
                .createQuery(CodeJiraIssues.class);
        cq.select(cq.from(CodeJiraIssues.class));
        return entityManager.createQuery(cq).getResultList();
    }

    public CodeJiraIssues getRowById(BigDecimal rowId) {
        TypedQuery<CodeJiraIssues> query = entityManager.createQuery(
                "SELECT c FROM CodeJiraIssues c WHERE c.id = :rowId",
                CodeJiraIssues.class);
        query.setParameter("rowId", rowId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "No result " + e.getMessage());
            return null;
        }
    }

    public Collection<CodeJiraIssues> getJiraIssuesByCode(String codeProject) {
        TypedQuery<CodeJiraIssues> query = entityManager.createQuery(
                "SELECT c FROM CodeJiraIssues c WHERE c.ProjectCode = :codeProject",
                CodeJiraIssues.class);
        query.setParameter("codeProject", codeProject);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "*-* JiraIssuesByCode нет данных " + e.getMessage());
            return null;
        }
    }

    public Collection<CodeJiraIssues> getFilteredJiraIssues(String codeProject,String version, String role ) {

        StringBuilder sb = new StringBuilder("SELECT c FROM CodeJiraIssues c ");
        boolean flagWhere=false;
        if (codeProject !=null && !codeProject.isEmpty()) {
            sb.append("WHERE c.ProjectCode =:codeProject ");
            flagWhere=true;
        }
        if (version !=null && !version.isEmpty()) {
            if (!flagWhere) {
                sb.append(" WHERE ");
                flagWhere=true;
            } else {
                sb.append(" and ");
            }
            sb.append(" c.ProjectVersion =:version ");
        }
        if (role !=null && !role.isEmpty()) {
            if (!flagWhere) {
                sb.append(" WHERE ");
            } else {
                sb.append(" and ");
            }
            sb.append(" c.WorkRole =:role ");
        }
        sb.append(" order by c.ProjectCode, c.ProjectVersion, c.WorkRole ");
        log4jLogger.log(Level.INFO, "*** FilteredJiraIssues запрос:|" + sb.toString()+"|");
        TypedQuery<CodeJiraIssues> query = entityManager.createQuery(
               sb.toString(),CodeJiraIssues.class);
        if (codeProject !=null && !codeProject.isEmpty()) {
            query.setParameter("codeProject", codeProject);
        }
        if (version !=null && !version.isEmpty()) {
            query.setParameter("version", version);
        }
        if (role !=null && !role.isEmpty()) {
            query.setParameter("role", role);
        }

        try {
            return query.getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "*-* FilteredJiraIssues нет данных " + e.getMessage());
            return null;
        }
    }

    public Collection<CodeJiraIssues> getFilteredJiraIssues(String codeProject,BigDecimal versionID, String role ) {

        StringBuilder sb = new StringBuilder("SELECT c FROM CodeJiraIssues c ");
        boolean flagWhere=false;
        if (codeProject !=null && !codeProject.isEmpty()) {
            sb.append("WHERE c.ProjectCode =:codeProject ");
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
        if (role !=null && !role.isEmpty()) {
            if (!flagWhere) {
                sb.append(" WHERE ");
            } else {
                sb.append(" and ");
            }
            sb.append(" c.WorkRole =:role ");
        }
        sb.append(" order by c.ProjectCode, c.ProjectVersion, c.WorkRole ");
        log4jLogger.log(Level.INFO, "*** FilteredJiraIssues запрос:|" + sb.toString()+"|");
        TypedQuery<CodeJiraIssues> query = entityManager.createQuery(
                sb.toString(),CodeJiraIssues.class);
        if (codeProject !=null && !codeProject.isEmpty()) {
            query.setParameter("codeProject", codeProject);
        }
        if (versionID !=null && versionID.compareTo(BigDecimal.ZERO)!=0) {
            query.setParameter("versionid", versionID);
        }
        if (role !=null && !role.isEmpty()) {
            query.setParameter("role", role);
        }

        try {
            return query.getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "*-* FilteredJiraIssues нет данных " + e.getMessage());
            return null;
        }
    }

    public void deleteAllRows() {
        Query query = entityManager.createNativeQuery("DELETE FROM code_issue_prepared");
        try {
            query.executeUpdate();
        } catch (Exception sqle) {
            log4jLogger.log(Level.ERROR, "Error delete all rows" + sqle.getMessage());
        }
    }

}
