package com.newrelic.contributors.api;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.jboss.weld.environment.se.Weld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class Main {

    private static final  Logger logger = LoggerFactory.getLogger(Main.class.getName());

    private static final URI BASE_URI = URI.create("http://localhost:8080/");
    private static final String ROOT_PATH = "contributors?location=barcelona";

    public static void main(String[] args) {
        try {
            System.out.println("New Relic - GitHub Contributors App");

            final Weld weld = new Weld();
            weld.initialize();

            final ResourceConfig resourceConfig = createJaxRsApp();
            final HttpServer server = GrizzlyHttpServerFactory
                    .createHttpServer(BASE_URI, resourceConfig, true);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                server.shutdownNow();
                weld.shutdown();
            }));

            logger.info("Application started.\nTry out {}{}\n" +
                    "Stop the application using CTRL+C", BASE_URI, ROOT_PATH);

            Thread.currentThread().join();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Create JAX-RS application. The same one is used also in the tests.
     *
     * @return Jersey's resource configuration of the Weld application.
     */
    static ResourceConfig createJaxRsApp() {
        return ResourceConfig.forApplicationClass(ContributorsApplication.class);
    }

}
