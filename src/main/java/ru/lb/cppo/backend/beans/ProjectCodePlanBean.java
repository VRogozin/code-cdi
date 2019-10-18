package ru.lb.cppo.backend.beans;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.ProjectCodePlan;
import ru.lb.cppo.backend.pojo.PlainCodeVersion;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class ProjectCodePlanBean {
    private static final Logger log4jLogger = Logger.getLogger(ProjectCodePlanBean.class);

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

    private boolean isPersistedCodePlan(ProjectCodePlan projectCodePlan) {
        ProjectCodePlan existingRow = entityManager.getReference(
                ProjectCodePlan.class, projectCodePlan.getId());
        return projectCodePlan.equals(existingRow);
    }

    public void storeRow(ProjectCodePlan projectCodePlan) {
        if (projectCodePlan.isPersisted()) {
//            if (!isPersistedCodePlan(projectCodePlan)) {
//                throw new RuntimeException(
//                        "Changing reports row is not permitted");
//            }
            entityManager.merge(projectCodePlan);
        } else {
            entityManager.persist(projectCodePlan);
        }
        entityManager.flush();
    }

    public void flush(){
        if(entityManager.isOpen()){
            entityManager.flush();
            entityManager.clear();
        }
    }

    public List<ProjectCodePlan> getAllRow() {
        CriteriaQuery<ProjectCodePlan> cq = entityManager.getCriteriaBuilder()
                .createQuery(ProjectCodePlan.class);
        log4jLogger.log(Level.INFO, "*** Query getAllRow |");
        cq.select(cq.from(ProjectCodePlan.class));
        try {
            return entityManager.createQuery(cq).getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "*-* Failed execute Query getAllRow " + e.getMessage());
            return null;
        }
    }

    public Collection<ProjectCodePlan> getFilteredRow(String codeProject, String version, String role) {
        Boolean flagWhere = false;

        StringBuilder sb = new StringBuilder("SELECT c FROM ProjectCodePlan c ");
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
                flagWhere=true;
            } else {
                sb.append(" and ");
            }
            sb.append(" c.workRole =:role ");
        }
        sb.append(" order by c.projectCode, c.projectVersion,c.workRole ");
        log4jLogger.log(Level.INFO, "*** Query FilteredRow |" + sb.toString()+"|");
        TypedQuery<ProjectCodePlan> query = entityManager.createQuery(
                sb.toString(),ProjectCodePlan.class);
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
            log4jLogger.log(Level.ERROR, "*-* filteredRow - нет данных " + e.getMessage());
            return null;
        }
    }

    public List<String> getCodeList(){

        List<String> strArr = new ArrayList<String>();

        StringBuilder sb = new StringBuilder("SELECT DISTINCT c.projectCode FROM ProjectCodePlan c ")
                            .append(" order by c.projectCode ");

        log4jLogger.log(Level.INFO, "*** Query CodeList |" + sb.toString()+"|");
        Query queryCode =   entityManager.createQuery(sb.toString());
        try {
            strArr=queryCode.getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "*-* CodeList нет данных " + e.getMessage());
        }
        if (strArr==null) {
            strArr.add("");
        }
        return strArr;
    }

    public List<String> getRoleListByCodeVersion(String codeProject, String version){

        Boolean flagWhere=false;
        List<String> strArr = new ArrayList<String>();

        StringBuilder sb = new StringBuilder("SELECT DISTINCT c.workRole FROM ProjectCodePlan c ");
        if (codeProject !=null && !codeProject.isEmpty()) {
            sb.append(" WHERE c.projectCode =:codeProject ");
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
        sb.append(" order by c.workRole ");

        log4jLogger.log(Level.INFO, "*** Query RoleList |" + sb.toString()+"|");
        Query queryRole =   entityManager.createQuery(sb.toString());
        if (codeProject !=null && !codeProject.isEmpty()) {
            queryRole.setParameter("codeProject", codeProject);
        }
        if (version !=null && !version.isEmpty()) {
            queryRole.setParameter("version", version);
        }

        try {
            strArr=queryRole.getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "*-* RoleList нет данных " + e.getMessage());
        }
        if (strArr==null) {
            strArr.add("");
        }
        return strArr;
    }

    public void removeRow(ProjectCodePlan projectCodePlan) {
        projectCodePlan = entityManager.getReference(ProjectCodePlan.class, projectCodePlan.getId());
        entityManager.remove(projectCodePlan);
        entityManager.flush();
    }

    public void deleteAllRows() {
        Query query = entityManager.createQuery("DELETE FROM ProjectCodePlan ");
        try {
            query.executeUpdate();
        } catch (Exception sqle) {
            log4jLogger.log(Level.ERROR, "Error delete all rows" + sqle.getMessage());
        }
    }

    public List<PlainCodeVersion> getCodeVersionAsList() {

        log4jLogger.log(Level.INFO, "!---- export Project Code Plans string ---------------!");

        List<ProjectCodePlan> projectCodePlans;
        projectCodePlans = getAllRow();

        List<PlainCodeVersion> plainCodeVersionList = new ArrayList<PlainCodeVersion>();

        Iterator<ProjectCodePlan> projectCodePlanIterator = projectCodePlans.iterator();
        Set<String> uniqueValues = new HashSet<String>();

        try {
            while(projectCodePlanIterator.hasNext()) {
                ProjectCodePlan projectCodePlan = projectCodePlanIterator.next();
                PlainCodeVersion plainCodeVersion =
                        new PlainCodeVersion(projectCodePlan.getProjectCode()
                                , projectCodePlan.getVersionID()
                                , projectCodePlan.getProjectVersion());
                if (uniqueValues.add(plainCodeVersion.toString())) {
                    plainCodeVersionList.add(plainCodeVersion);
                }
            }
        } catch (Exception e) {
            log4jLogger.log(Level.ERROR, "Code&Version error :" + e.getMessage());
        }
        return plainCodeVersionList.stream().distinct().collect(Collectors.toList());
    }
}
