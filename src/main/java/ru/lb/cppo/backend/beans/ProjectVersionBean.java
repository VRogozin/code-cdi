package ru.lb.cppo.backend.beans;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.ProjectVersion;

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
public class ProjectVersionBean {
    private static final Logger log4jLogger = Logger.getLogger(ProjectVersionBean.class);

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

    private boolean isPersistedVersion(ProjectVersion projectVersion) {
        ProjectVersion existingRow = entityManager.getReference(
                ProjectVersion.class, projectVersion.getVersionId());
        return projectVersion.equals(existingRow);
    }

    public void storeRow(ProjectVersion projectVersion) {
        if (projectVersion.isPersisted()) {
//            if (!isPersistedVersion(projectVersion)) {
// todo add log message
//                throw new RuntimeException(
//                        "Changing ProjectVersion row is not permitted" );
//            }
            entityManager.merge(projectVersion);
        } else {
            entityManager.persist(projectVersion);
        }
        entityManager.flush();
    }
/*
    public void add(ProjectVersion projectVersion) {
            if (!entityManager.contains(projectVersion)) {
                entityManager.persist(projectVersion);
                entityManager.flush();
            } else {
                log4jLogger.log(Level.INFO,
                        "Already exsists " + projectVersion.getVersionName());
            }
    }
*/
    public void removeRow(ProjectVersion projectVersion) {
        projectVersion = entityManager.getReference(ProjectVersion.class, projectVersion.getVersionId());
        entityManager.remove(projectVersion);
        entityManager.flush();
    }

    public void detachAllRows() {
        List<ProjectVersion> pvList = getAllRow();
        Iterator<ProjectVersion> projectVersionIterator = pvList.iterator();
        while (projectVersionIterator.hasNext()) {
            ProjectVersion projectVersion = projectVersionIterator.next();
            entityManager.detach(projectVersion);
        }
        log4jLogger.log(Level.INFO, "Rows detached");
    }

    public void deleteAllRows() {
        detachAllRows();
        Query query = entityManager.createQuery("DELETE FROM ProjectVersion ");
        try {
            query.executeUpdate();
        } catch (Exception sqle) {
            log4jLogger.log(Level.ERROR, "Error delete all rows" + sqle.getMessage());
        }
        entityManager.flush();
    }

    public List<ProjectVersion> getAllRow() {
        CriteriaQuery<ProjectVersion> cq = entityManager.getCriteriaBuilder()
                .createQuery(ProjectVersion.class);
        cq.select(cq.from(ProjectVersion.class));
        try {
            return entityManager.createQuery(cq).getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "*-* Failed execute Query getAllRow " + e.getMessage());
            return null;
        }
    }

    public List<ProjectVersion> getActiveRow() {
        List<ProjectVersion> pvList = getAllRow();
        pvList.stream().filter(projectVersion -> projectVersion.getActive()).distinct().collect(Collectors.toList());
        if (pvList.isEmpty()) {
            return Collections.emptyList();
        }
        return pvList;
    }

    public ProjectVersion getProjectVersionById(BigDecimal rowId) {
        TypedQuery<ProjectVersion> query = entityManager.createQuery(
                "SELECT c FROM ProjectVersion c WHERE c.versionId = :rowId",
                ProjectVersion.class);
        query.setParameter("rowId", rowId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
