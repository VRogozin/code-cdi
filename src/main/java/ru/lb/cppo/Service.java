package ru.lb.cppo;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Service {

    public String sayHello(String name) {
        return "hello, " +name;
    }
}
