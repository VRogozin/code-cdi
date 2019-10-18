package ru.lb.cppo.backend.beans;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.ProjectUserRole;

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
import java.util.List;

@Stateless
public class ProjectUserRoleBean {

    private static final Logger log4jLogger = Logger.getLogger(ProjectUserRoleBean.class);

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

    private boolean isPersistedRole(ProjectUserRole projectUserRole) {
        ProjectUserRole existingRow = entityManager.getReference(
                ProjectUserRole.class, projectUserRole.getId());
        return projectUserRole.equals(existingRow);
    }

    public void storeRow(ProjectUserRole projectUserRole) {
        if (projectUserRole.isPersisted()) {
//            if (!isPersistedRole(projectUserRole)) {
// todo add log message
//                throw new RuntimeException(
//                        "Changing ProjectUserRole row is not permitted");
//            }
            entityManager.merge(projectUserRole);
        } else {
            entityManager.persist(projectUserRole);
        }
        entityManager.flush();
    }

    public void removeRow(ProjectUserRole projectUserRole) {
        projectUserRole = entityManager.getReference(ProjectUserRole.class, projectUserRole.getId());
        entityManager.remove(projectUserRole);
        entityManager.flush();
    }

    public void deleteAllRows() {
        Query query = entityManager.createQuery("DELETE FROM ProjectUserRole ");
        try {
            query.executeUpdate();
        } catch (Exception sqle) {
            log4jLogger.log(Level.ERROR, "Error delete all rows" + sqle.getMessage());
        }
    }

    public List<ProjectUserRole> getAllRow() {
        CriteriaQuery<ProjectUserRole> cq = entityManager.getCriteriaBuilder()
                .createQuery(ProjectUserRole.class);
        cq.select(cq.from(ProjectUserRole.class));
        try {
            return entityManager.createQuery(cq).getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "*-* Failed execute Query getAllRow " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<ProjectUserRole> getActiveRow() {
        Boolean flag=true;
        TypedQuery<ProjectUserRole> query = entityManager.createQuery(
                "SELECT c FROM ProjectUserRole c WHERE c.isActive = :flag",
                ProjectUserRole.class);
        query.setParameter("flag", flag);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public ProjectUserRole getProjectUserRoleById(BigDecimal rowId) {
        TypedQuery<ProjectUserRole> query = entityManager.createQuery(
                "SELECT c FROM ProjectUserRole c WHERE c.id = :rowId",
                ProjectUserRole.class);
        query.setParameter("rowId", rowId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
