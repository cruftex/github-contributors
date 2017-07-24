package com.newrelic.contributors.api;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

public class ContributorsApplication extends ResourceConfig {

    public static void main(String[] args) throws Exception {

        ResourceConfig config = new ResourceConfig();
        config.setApplicationName("GitHub Contributors");
        config.packages("com.newrelic.contributors.api");
        config.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");

        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }

}