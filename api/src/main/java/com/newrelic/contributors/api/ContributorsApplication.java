package com.newrelic.contributors.api;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * JAX-RS application defined as a CDI bean.
 */
public class ContributorsApplication extends Application {

    private static final Set<Class<?>> appClasses = new HashSet<>();

    static {
        appClasses.add(ContributorsResource.class);
        appClasses.add(GenericExceptionMapper.class);
        appClasses.add(ConstraintViolationMapper.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return appClasses;
    }

}
