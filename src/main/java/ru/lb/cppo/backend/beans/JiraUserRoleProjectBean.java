package ru.lb.cppo.backend.beans;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.JiraUserRoleProject;

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
import java.util.List;

@Stateless
public class JiraUserRoleProjectBean {
    private static final Logger log4jLogger = Logger.getLogger(JiraUserRoleProjectBean.class);

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

    private boolean isPersistedJiraUserRoleProject(JiraUserRoleProject jurpRow) {
        JiraUserRoleProject existingJiraUserRoleProject = entityManager.getReference(
                JiraUserRoleProject.class, jurpRow.getId());
        return jurpRow.equals(existingJiraUserRoleProject);
    }

    public void storeRow(JiraUserRoleProject jurpRow) {
        if (jurpRow.isPersisted()) {
//            if (!isPersistedJiraUserRoleProject(jurpRow)) {
//                throw new RuntimeException(
//                        "Changing jirauserrole row is not permitted");
//            }
            entityManager.merge(jurpRow);
        } else {
            entityManager.persist(jurpRow);
        }
        entityManager.flush();
    }

    public Collection<JiraUserRoleProject> getAllRow() {
        CriteriaQuery<JiraUserRoleProject> cq = entityManager.getCriteriaBuilder()
                .createQuery(JiraUserRoleProject.class);
        cq.select(cq.from(JiraUserRoleProject.class));
        List<JiraUserRoleProject> resultList = entityManager.createQuery(cq)
                .getResultList();

        return resultList;
    }

    public void removeRow(JiraUserRoleProject jurpRow) {
        jurpRow = entityManager.getReference(JiraUserRoleProject.class, jurpRow.getId());
        entityManager.remove(jurpRow);
        entityManager.flush();
    }

    public JiraUserRoleProject getRowById(BigDecimal rowId) {
        TypedQuery<JiraUserRoleProject> query = entityManager.createQuery(
                "SELECT c FROM JiraUserRoleProject c WHERE c.id = :rowId",
                JiraUserRoleProject.class);
        query.setParameter("rowId", rowId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void deleteAllRows() {
        Query query = entityManager.createNativeQuery("DELETE FROM JiraUserRoleProject");
        try {
            query.executeUpdate();
        } catch (Exception sqle) {
            log4jLogger.log(Level.ERROR, "Error delete all rows" + sqle.getMessage());
        }

    }

}
