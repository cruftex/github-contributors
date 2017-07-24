package com.newrelic.contributors.domain;

import com.jayway.jsonpath.JsonPath;
import io.github.resilience4j.cache.Cache;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import io.vavr.control.Try;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.MemoryConfiguration;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Singleton
public class ContributorsService {

    @Inject
    private Logger logger;

    @Inject
    private Client client;

    @Inject
    private RetryConfig retryConfig;

    @Inject
    private Cache<String, List<String>> cache;

    private final JsonPath path = JsonPath.compile("$.items[*].login");

    @SuppressWarnings("unchecked")
    public List<String> findTopContributors(final String location, final int top) {

        final Retry retry = Retry.of("GitHub - User Search", retryConfig);
        retry.getEventPublisher().onEvent(e -> logger.debug(e.toString()));

        final CheckedFunction0<List<String>> retriedSupplier =
                Retry.decorateCheckedSupplier(retry, () -> retrieveTopContributors(location));

        final CheckedFunction1<String, List<String>> cachedSupplier =
                Cache.decorateCheckedSupplier(cache, retriedSupplier);

        return Try.of(() -> cachedSupplier.apply(location))
                .onFailure(t -> logger.warn(t.getMessage()))
                .getOrElse(Collections.emptyList());
    }

    private List<String> retrieveTopContributors(String location) throws IOException {
        final Response response = client.target("https://api.github.com/search/users")
                .queryParam("q", "location:" + location)
                .queryParam("sort", "repositories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        return path.read(response.readEntity(InputStream.class));
    }

    @PostConstruct
    private void postConstruct() {
        // Should be enabled when connected to a server
        final String clientMode = System.getProperty("com.newrelic.cache.clientMode");
        Ignition.setClientMode(Boolean.getBoolean(clientMode));
        Ignition.start(new IgniteConfiguration()
                .setMemoryConfiguration(new MemoryConfiguration()));
    }

    @PreDestroy
    private void preDestroy() {
        Ignition.stop(true);
        client.close();
    }

}
