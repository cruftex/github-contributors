package com.newrelic.contributors.service;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Singleton
public class CacheProducer {

    @Inject
    private GitHubCacheLoader loader;

    @Inject
    private Properties configuration;

    @Produces
    @Singleton
    @SuppressWarnings("unchecked")
    public javax.cache.Cache<String, List> cache() {

        final Optional<Cache<String, List>> cache =
                Optional.ofNullable(Caching.getCache("contributors",
                        String.class, List.class));

        return cache.orElseGet(() -> {
            final int seconds = Integer.parseInt(configuration.getProperty("cache.expiry.seconds"));
            final Duration expiry = new Duration(TimeUnit.SECONDS, seconds);

            final MutableConfiguration config = new MutableConfiguration()
                    .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(expiry))
                    .setCacheLoaderFactory(new FactoryBuilder.SingletonFactory<>(loader))
                    .setTypes(String.class, List.class)
                    .setReadThrough(true);

            return Caching.getCachingProvider().getCacheManager()
                    .createCache("contributors", config);
        });
    }

}
