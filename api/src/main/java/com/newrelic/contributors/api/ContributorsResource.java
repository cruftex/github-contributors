package com.newrelic.contributors.api;

import com.newrelic.contributors.domain.Location;
import com.newrelic.contributors.domain.Top;
import com.newrelic.contributors.service.ContributorsService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RequestScoped
@Path("contributors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ContributorsResource {

    @Inject
    private ContributorsService service;

    @GET
    public List getTopContributors(
            @Location @QueryParam("location") String location,
            @Top @QueryParam("top") @DefaultValue("50") int top) {

        return service.findTopContributors(location, top);
    }

}
