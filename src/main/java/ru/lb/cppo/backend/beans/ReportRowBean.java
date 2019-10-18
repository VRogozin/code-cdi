package ru.lb.cppo.backend.beans;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.ReportRow;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.*;

@Stateless
public class ReportRowBean {
    private static final Logger log4jLogger = Logger.getLogger(ReportRowBean.class);

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

    private boolean isPersistedReportRow(ReportRow reportRow) {
        ReportRow existingReportRow = entityManager.getReference(
                ReportRow.class, reportRow.getId());
        return reportRow.equals(existingReportRow);
    }

    public void storeRow(ReportRow reportRow) {
        if (reportRow.isPersisted()) {
//            if (!isPersistedReportRow(reportRow)) {
//                throw new RuntimeException(
//                        "Changing reports row is not permitted");
//            }
            entityManager.merge(reportRow);
        } else {
            entityManager.persist(reportRow);
        }
        entityManager.flush();
    }

    public Collection<ReportRow> getAllRow() {
        CriteriaQuery<ReportRow> cq = entityManager.getCriteriaBuilder()
                .createQuery(ReportRow.class);
        cq.select(cq.from(ReportRow.class));
        List<ReportRow> resultList = entityManager.createQuery(cq)
                .getResultList();

        return resultList;
    }

    public void removeRow(ReportRow reportRow) {
        reportRow = entityManager.getReference(ReportRow.class, reportRow.getId());
        entityManager.remove(reportRow);
        entityManager.flush();
    }

    public ReportRow getRowById(BigDecimal rowId) {
        TypedQuery<ReportRow> query = entityManager.createQuery(
                "SELECT c FROM ReportRow c WHERE c.id = :rowId",
                ReportRow.class);
        query.setParameter("rowId", rowId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Collection<ReportRow> getRowByCode(String codeProject) {
        TypedQuery<ReportRow> query = entityManager.createQuery(
                "SELECT c FROM ReportRow c WHERE c.projectCode = :codeProject",
                ReportRow.class);
        query.setParameter("codeProject", codeProject);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return emptyList();
        }
    }

    public void flush(){
        if(entityManager.isOpen()){
            entityManager.flush();
            entityManager.clear();
        }
    }

    public Collection<ReportRow> getFilteredReportRow(String codeProject, String version, String role ) {

        StringBuilder sb = new StringBuilder("SELECT c FROM ReportRow c ");
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

        if (role !=null && !role.isEmpty()) {
            if (!flagWhere) {
                sb.append(" WHERE ");
            } else {
                sb.append(" and ");
            }
            sb.append(" c.workRole =:role ");
        }
        sb.append(" order by c.projectCode, c.projectVersion, c.workRole ");

        log4jLogger.log(Level.INFO, "*** ReportRow запрос:|" + sb.toString()+"|");

        TypedQuery<ReportRow> query = entityManager.createQuery(
                sb.toString(),ReportRow.class);
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
            log4jLogger.log(Level.ERROR, "*-* FilteredReportRow нет данных " + e.getMessage());
            return emptyList();
        }
    }

    public Collection<ReportRow> getFilteredReportRow(String codeProject, BigDecimal versionID, String role ) {

        StringBuilder sb = new StringBuilder("SELECT c FROM ReportRow c ");
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

        if (role !=null && !role.isEmpty()) {
            if (!flagWhere) {
                sb.append(" WHERE ");
            } else {
                sb.append(" and ");
            }
            sb.append(" c.workRole =:role ");
        }
        sb.append(" order by c.projectCode, c.projectVersion, c.workRole ");

        log4jLogger.log(Level.INFO, "*** ReportRow запрос:|" + sb.toString()+"|");

        TypedQuery<ReportRow> query = entityManager.createQuery(
                sb.toString(),ReportRow.class);
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
            log4jLogger.log(Level.ERROR, "*-* FilteredReportRow нет данных " + e.getMessage());
            return emptyList();
        }
    }

    public void deleteAllRows() {
        Query query = entityManager.createNativeQuery("DELETE FROM code_project_report");
        try {
            query.executeUpdate();
        } catch (Exception sqle) {
            log4jLogger.log(Level.ERROR, "Error delete all rows" + sqle.getMessage());
        }

    }

}
