package template.template_microservice.persistence;

import template.template_microservice.util.SimpleConfiguration;

/**
 * example persistence implementation
 * @author robert.diers
 * @project Application
 * copyright: (c) Accenture 2019, all rights reserved
 */
public class HelloWorldBEImpl implements HelloWorldBE {

	@Override
	public String sayHello() {
		
		//here we should access database and interfaces
		return SimpleConfiguration.getPropertyWithoutNull("helloworld");
	}

}
