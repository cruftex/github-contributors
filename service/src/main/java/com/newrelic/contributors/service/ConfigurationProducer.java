package com.newrelic.contributors.service;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationProducer {

    @Produces
    @Singleton
    public Properties configuration() throws IOException {

        Properties config = new Properties();
        config.load(ConfigurationProducer.class.getClassLoader()
                .getResourceAsStream("contributors.properties"));

        return config;
    }

}
