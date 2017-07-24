package com.newrelic.contributors.api;

import com.newrelic.contributors.domain.ContributorsService;
import com.newrelic.contributors.domain.Location;
import com.newrelic.contributors.domain.Top;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("contributors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ContributorsResource {

    @Inject
    private ContributorsService service;

    @GET
    public List<String> getTopContributors(
            @Location @QueryParam("location") String location,
            @Top @QueryParam("top") int top) {

        return service.findTopContributors(location, top);
    }

}
