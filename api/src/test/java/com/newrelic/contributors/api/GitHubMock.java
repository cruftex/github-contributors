package com.newrelic.contributors.api;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RequestScoped
@Path("search")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GitHubMock {

    private GitHubMock delegate;

    public GitHubMock() {
    }

    public GitHubMock(final GitHubMock delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("users")
    public List users(@QueryParam("location") String location,
                      @QueryParam("q") String query,
                      @QueryParam("sort") String sort) {
        return delegate.users(location, query, sort);
    }

}
