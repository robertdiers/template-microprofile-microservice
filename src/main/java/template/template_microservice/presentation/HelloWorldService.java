package template.template_microservice.presentation;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import template.template_microservice.business.HelloWorldBA;
import template.template_microservice.util.ApplicationLogger;

/**
 * example REST end point
 * @author robert.diers
 * @project Application
 * copyright: (c) Accenture 2019, all rights reserved
 */
@Path("/helloworld")
public class HelloWorldService {
	
	@Inject
	HelloWorldBA helloWorldBA;
	
	//default constructor
	public HelloWorldService() {}
	
	//junit constructor
	public HelloWorldService(HelloWorldBA helloWorldBA) {
		this.helloWorldBA = helloWorldBA;
	}
	
	@GET
    @Path("/sayhello")
	@Produces(MediaType.TEXT_PLAIN)
	public Response sayHello() {
		
		try {
			//call business layer
			String message = helloWorldBA.sayHello();
			
			//return response
			return Response.ok(message).build();
		} catch (Exception e) {
			ApplicationLogger.log(e);
			return Response.status(500).type("text/plain").entity(e.getMessage()).build();
		}
	}

}
