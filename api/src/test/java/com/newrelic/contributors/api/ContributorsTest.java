package com.newrelic.contributors.api;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.jboss.weld.environment.se.Weld;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;

public class ContributorsTest extends JerseyTest {

    @Rule
    public WireMockRule rule = new WireMockRule(options().port(8888));

    private Weld weld;

    @Override
    public void setUp() throws Exception {
        weld = new Weld();
        weld.initialize();

        rule.stubFor(get(urlEqualTo("/search/users?q=location%3Abarcelona&" +
                "sort=repositories&per_page=150&page=1"))
                //.withQueryParam("q", equalTo("location%3Abarcelona"))
                //.withQueryParam("sort", equalTo("repositories"))
                .withHeader("Accept", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "  \"total_count\": 8651,\n" +
                                "  \"incomplete_results\": false,\n" +
                                "  \"items\": [\n" +
                                "    {\n" +
                                "      \"login\": \"kristianmandrup\"\n" +
                                "    }\n" +
                                "}")));
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        weld.shutdown();
        super.tearDown();
    }

    @Override
    protected ResourceConfig configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        return Main.createJaxRsApp();
    }

    @Test
    public void testContributors() throws InterruptedException {

        final Response response = target().path("contributors")
                .queryParam("location", "barcelona")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertEquals(200, response.getStatus());
        assertEquals(String.format("Hello %s", "Josef"), response.readEntity(String.class));
    }

}
