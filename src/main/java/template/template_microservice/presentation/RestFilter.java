package template.template_microservice.presentation;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import template.template_microservice.util.ApplicationLogger;
import template.template_microservice.util.ThreadExposer;

/**
 * REST service wrapper
 * @author robert.diers
 * @project Application
 * copyright: (c) Accenture 2019, all rights reserved
 */
@Provider
@PreMatching
public class RestFilter implements ContainerRequestFilter, ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext ctx) throws IOException {	
		
		try {
			
			//caller			
			String caller = ctx.getHeaderString("X-FORWARDED-FOR");
			if (caller == null || caller.length() < 1) caller = ctx.getHeaderString("ORWARDED");
			if (caller == null || caller.length() < 1) caller = ctx.getHeaderString("HOST");
			
			// authorization here - you might want to override the caller?			

			// expose data to thread
			ThreadExposer.exposeCaller(caller);
			ThreadExposer.exposeStartTime(System.currentTimeMillis());
	
		} catch (Exception e) {
			ApplicationLogger.log(e, e.getMessage());
			throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN)
    				.entity("Couldn't retrieve authentication").build());
		}		
	}

	@Override
	public void filter(ContainerRequestContext ctx, ContainerResponseContext ctx2) throws IOException {
		
		String uri = ""+ctx.getUriInfo().getRequestUri();		
		Long start = ThreadExposer.getExposedStartTime();
		Long end = System.currentTimeMillis();
		
		//log execution time
		ApplicationLogger.log(ThreadExposer.getExposedCaller() + " - "+(end-start)+" ms: "+uri);
	}
  
}