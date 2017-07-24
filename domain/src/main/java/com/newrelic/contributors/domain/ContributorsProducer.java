package com.newrelic.contributors.domain;

import io.github.resilience4j.cache.Cache;
import io.github.resilience4j.retry.RetryConfig;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.List;

@Singleton
public class ContributorsProducer {

    @Produces
    @Singleton
    public static RetryConfig retryConfig() {
        // Config could be taken from properties, etc.
        return RetryConfig.ofDefaults();
    }

    @Produces
    @Singleton
    public static Client client() {
        // Config could be taken from properties, etc.
        return ClientBuilder.newClient();
    }

    @Produces
    public static Logger logger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    @Produces
    @Singleton
    @SuppressWarnings("unchecked")
    public static Cache<String, List<String>> cache() {

        final CacheConfiguration config = new CacheConfiguration("contributors");
        config.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.FIVE_MINUTES));
        config.setCacheMode(CacheMode.LOCAL); // Could be partitioned for maximum scalability
        config.setAtomicityMode(CacheAtomicityMode.ATOMIC);

        final IgniteCache<String, List<String>> igniteCache = Ignition.ignite().getOrCreateCache(config);
        final Cache<String, List<String>> cache = Cache.of(igniteCache);

        final Logger logger = LoggerFactory.getLogger("contributors-cache");
        cache.getEventPublisher().onEvent(e -> logger.debug(e.toString()));

        return cache;
    }

}
