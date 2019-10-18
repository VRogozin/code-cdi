package ru.lb.cppo.backend.beans;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.CodeAccounting;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class CodeAccountingBean {
    private static final Logger log4jLogger = Logger.getLogger(CodeAccountingBean.class);

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

    private boolean isPersistedCA(CodeAccounting codeAccounting) {
        CodeAccounting existingRow = entityManager.getReference(
                CodeAccounting.class, codeAccounting.getId());
        return codeAccounting.equals(existingRow);
    }

    public void storeRow(CodeAccounting codeAccounting) {
        if (codeAccounting.isPersisted()) {
//            if (!isPersistedCA(codeAccounting)) {
//                throw new RuntimeException(
//                        "CodeAccounting | Changing reports row is not permitted");
//            }
            entityManager.merge(codeAccounting);
        } else {
            entityManager.persist(codeAccounting);
        }
        entityManager.flush();
    }

    public void add(CodeAccounting codeAccounting){
        entityManager.persist(codeAccounting);
        entityManager.flush();
    }

    public void flush(){
        if(entityManager.isOpen()){
            entityManager.flush();
            entityManager.clear();
        }
    }

    public void removeRow(CodeAccounting codeAccounting) {
        codeAccounting = entityManager.getReference(CodeAccounting.class, codeAccounting.getId());
        entityManager.remove(codeAccounting);
        entityManager.flush();
    }

    public Collection<CodeAccounting> getAllRow() {
        CriteriaQuery<CodeAccounting> cq = entityManager.getCriteriaBuilder()
                .createQuery(CodeAccounting.class);
        cq.select(cq.from(CodeAccounting.class));
        return entityManager.createQuery(cq).getResultList();
    }

    public Collection<CodeAccounting> getCodeAccountingByCode(String codeProject) {
        TypedQuery<CodeAccounting> query = entityManager.createQuery(
                "SELECT c FROM CodeAccounting c WHERE c.projectCode = :codeProject",
                CodeAccounting.class);
        query.setParameter("codeProject", codeProject);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "CodeAccounting | нет данных " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Collection<CodeAccounting> getFilteredCodeAccounting(String codeProject,String version ) {

        StringBuilder sb = new StringBuilder("SELECT c FROM CodeAccounting c ");
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

        if (!flagWhere) {
            sb.append(" WHERE ");
        } else {
            sb.append(" and ");
        }
        sb.append(" c.isActive =true ");

        sb.append(" order by c.projectCode, c.projectVersion ");
        log4jLogger.log(Level.INFO, "*** Filtered CodeAccounting : запрос|" + sb.toString()+"|");
        TypedQuery<CodeAccounting> query = entityManager.createQuery(
               sb.toString(),CodeAccounting.class);
        if (codeProject !=null && !codeProject.isEmpty()) {
            query.setParameter("codeProject", codeProject);
        }
        if (version !=null && !version.isEmpty()) {
            query.setParameter("version", version);
        }

        try {
            return query.getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "*** CodeAccounting | нет данных " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Collection<CodeAccounting> getFilteredCodeAccounting(String codeProject,BigDecimal versionID ) {

        StringBuilder sb = new StringBuilder("SELECT c FROM CodeAccounting c ");
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

        if (!flagWhere) {
            sb.append(" WHERE ");
        } else {
            sb.append(" and ");
        }
        sb.append(" c.isActive =true ");

        sb.append(" order by c.projectCode, c.projectVersion ");
        log4jLogger.log(Level.INFO, "*** Filtered CodeAccounting : запрос|" + sb.toString()+"|");
        TypedQuery<CodeAccounting> query = entityManager.createQuery(
                sb.toString(),CodeAccounting.class);
        if (codeProject !=null && !codeProject.isEmpty()) {
            query.setParameter("codeProject", codeProject);
        }
        if (versionID !=null && versionID.compareTo(BigDecimal.ZERO)!=0) {
            query.setParameter("versionid", versionID);
        }

        try {
            return query.getResultList();
        } catch (NoResultException e) {
            log4jLogger.log(Level.ERROR, "*** CodeAccounting | нет данных " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void deleteAllRows() {
        Query query = entityManager.createNativeQuery("DELETE FROM CodeAccounting");
        try {
            query.executeUpdate();
        } catch (Exception sqle) {
            log4jLogger.log(Level.ERROR, "CodeAccounting | Error delete all rows" + sqle.getMessage());
        }
    }

    public List<PlainCodeVersion> getCodeVersionAsList() {

//        log4jLogger.log(Level.INFO, "!---- export Project Code Plans string ---------------!");
        Collection<CodeAccounting> codeAccountingList;
        codeAccountingList = getAllRow();

        List<PlainCodeVersion> plainCodeVersionList = new ArrayList<PlainCodeVersion>();

        Iterator<CodeAccounting> codeAccountingIterator = codeAccountingList.iterator();
        Set<String> uniqueValues = new HashSet<String>();

        try {
            while(codeAccountingIterator.hasNext()) {
                CodeAccounting codeAccounting = codeAccountingIterator.next();
                if (codeAccounting.getVersionID() == null){
                    codeAccounting.setVersionID(BigDecimal.ONE.negate());
                }
                PlainCodeVersion plainCodeVersion =
                        new PlainCodeVersion(codeAccounting.getProjectCode()
                                , codeAccounting.getVersionID()
                                , codeAccounting.getProjectVersion());
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
