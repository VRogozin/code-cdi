package ru.lb.cppo.backend.beans;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.ProjectCode;

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

@Stateless
public class ProjectCodeBean {
    private static final Logger log4jLogger = Logger.getLogger(ProjectCodeBean.class);

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

    private boolean isPersistedCode(ProjectCode projectCode) {
        ProjectCode existingRow = entityManager.getReference(
                ProjectCode.class, projectCode.getCodeId());
        return projectCode.equals(existingRow);
    }

    public void storeRow(ProjectCode projectCode) {
        if (projectCode.isPersisted()) {
//            if (!isPersistedCode(projectCode)) {
// todo add log message
//                throw new RuntimeException(
//                        "Changing ProjectCode row is not permitted");
//            }
            entityManager.merge(projectCode);
        } else {
            entityManager.persist(projectCode);
        }
        entityManager.flush();
    }

    public void add(ProjectCode projectCode) {
        entityManager.persist(projectCode);
        entityManager.flush();
    }


    public void removeRow(ProjectCode projectCode) {
        projectCode = entityManager.getReference(ProjectCode.class, projectCode.getCodeId());
        entityManager.remove(projectCode);
        entityManager.flush();
    }

    public void deleteAllRows() {
        detachAllRows();
        Query query = entityManager.createQuery("DELETE FROM ProjectCode ");
        try {
            query.executeUpdate();
        } catch (Exception sqle) {
            log4jLogger.log(Level.ERROR, "Error delete all rows" + sqle.getMessage());
        }
    }

    public void detachAllRows() {
        List<ProjectCode> pcList = getAllRow();
        Iterator<ProjectCode> projectCodeIterator = pcList.iterator();
        while (projectCodeIterator.hasNext()) {
            ProjectCode v_projectCode = projectCodeIterator.next();
            entityManager.detach(v_projectCode);
        }
        log4jLogger.log(Level.INFO, "Rows detached");
    }

    public List<ProjectCode> getAllRow() {
        CriteriaQuery<ProjectCode> cq = entityManager.getCriteriaBuilder()
                .createQuery(ProjectCode.class);
        cq.select(cq.from(ProjectCode.class));
        try {
            return entityManager.createQuery(cq).getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "*-* Failed execute Query getAllRow " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public ProjectCode getProjectCodeById(BigDecimal rowId) {
        TypedQuery<ProjectCode> query = entityManager.createQuery(
                "SELECT c FROM ProjectCode c WHERE c.codeId = :rowId",
                ProjectCode.class);
        query.setParameter("rowId", rowId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
