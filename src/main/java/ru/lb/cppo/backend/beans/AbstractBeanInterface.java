package ru.lb.cppo.backend.beans;

import ru.lb.cppo.backend.data.AbstractEntity;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public interface AbstractBeanInterface<T extends AbstractEntity> {
    @PostConstruct
    void init();

    @PreDestroy
    void cleanup();

    boolean isPersisted(T t);

    void storeRow(T t);

    void flush();

    void add(T t);

    void removeRow(T t);
}
