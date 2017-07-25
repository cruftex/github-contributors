package com.newrelic.contributors.api;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.jboss.weld.environment.se.Weld;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class ContributorsTest extends JerseyTest {

    private Weld weld;

    private GitHubMock gitHub;

    @Override
    public void setUp() throws Exception {
        weld = new Weld();
        weld.initialize();
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

        this.gitHub = Mockito.mock(GitHubMock.class);
        return Main.createJaxRsApp();
        //  .register(new GitHubMock(gitHub));
    }

    @Test
    public void testContributors() {
        final Response response = target().path("contributors")
                .queryParam("location", "barcelona")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertEquals(200, response.getStatus());
        assertEquals(String.format("Hello %s", "Josef"), response.readEntity(String.class));
    }

}
