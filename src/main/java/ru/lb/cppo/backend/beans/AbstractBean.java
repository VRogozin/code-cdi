package ru.lb.cppo.backend.beans;

import org.apache.log4j.Logger;
import ru.lb.cppo.backend.data.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

public abstract class AbstractBean<T extends AbstractEntity> implements AbstractBeanInterface<T> {

    private Logger log4jLogger = Logger.getLogger(this.getClass());

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    @PersistenceContext(unitName = "jirareport")
    private EntityManager entityManager;

    @Override
    public void init(){
        entityManager=entityManagerFactory.createEntityManager();
    }

    @Override
    public void cleanup() {
        if(entityManager.isOpen()){
            entityManager.close();
        }
    }

    @Override
    public boolean isPersisted(T t) {
        T existingRow = entityManager.getReference((Class<T>) t.getClass(), t.getId());
        return t.equals(existingRow);
    }

    @Override
    public void flush() {
        if(entityManager.isOpen()){
            entityManager.flush();
            entityManager.clear();
        }
    }

    @Override
    public void storeRow(T t) {
        if (t.isPersisted()) {
            if (!isPersisted(t)) {
                throw new RuntimeException(
                        "Changing " +t.getClass().getName() +
                        " reports row is not permitted" );
            }
            entityManager.merge(t);
        } else {
            entityManager.persist(t);
        }
        entityManager.flush();
    }

    @Override
    public void add(T t) {
        entityManager.persist(t);
        entityManager.flush();
    }

    @Override
    public void removeRow(T t) {
        t = entityManager.getReference((Class<T>) t.getClass(), t.getId());
        entityManager.remove(t);
    }

}