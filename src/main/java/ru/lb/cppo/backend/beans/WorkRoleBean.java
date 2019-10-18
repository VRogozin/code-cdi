package ru.lb.cppo.backend.beans;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.WorkRole;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.math.BigDecimal;
import java.util.List;

@Stateless
public class WorkRoleBean {
    private static final Logger log4jLogger = Logger.getLogger(WorkRoleBean.class);

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

    private boolean isPersistedWorkRole(WorkRole workRole) {
        WorkRole existingRow = entityManager.getReference(
                WorkRole.class, workRole.getId());
        return workRole.equals(existingRow);
    }

    public void storeRow(WorkRole workRole) {
        if (workRole.isPersisted()) {
//            if (!isPersistedWorkRole(workRole)) {
// todo add log message
//                throw new RuntimeException(
//                        "Changing WorkRole row is not permitted");
//            }
            entityManager.merge(workRole);
        } else {
            entityManager.persist(workRole);
        }
        entityManager.flush();
    }

    public void removeRow(WorkRole workRole) {
        workRole = entityManager.getReference(WorkRole.class, workRole.getId());
        entityManager.remove(workRole);
        entityManager.flush();
    }

    public void deleteAllRows() {
        Query query = entityManager.createQuery("DELETE FROM WorkRole ");
        try {
            query.executeUpdate();
        } catch (Exception sqle) {
            log4jLogger.log(Level.ERROR, "Error delete all rows" + sqle.getMessage());
        }
    }

    public List<WorkRole> getAllRow() {
        CriteriaQuery<WorkRole> cq = entityManager.getCriteriaBuilder()
                .createQuery(WorkRole.class);
        cq.select(cq.from(WorkRole.class));
        try {
            return entityManager.createQuery(cq).getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "*-* Failed execute Query getAllRow " + e.getMessage());
            return null;
        }
    }

    public WorkRole getWorkRoleById(BigDecimal rowId) {
        TypedQuery<WorkRole> query = entityManager.createQuery(
                "SELECT c FROM WorkRole c WHERE c.id = :rowId",
                WorkRole.class);
        query.setParameter("rowId", rowId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
