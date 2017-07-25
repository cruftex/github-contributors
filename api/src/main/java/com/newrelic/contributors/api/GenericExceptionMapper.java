package com.newrelic.contributors.api;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Collections;

@Singleton
@Provider
public class GenericExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(final WebApplicationException exception) {
        return Response.status(exception.getResponse().getStatus())
                .entity(Collections.singletonMap("message", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

}
