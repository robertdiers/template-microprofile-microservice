package template.template_microservice.business;

import javax.inject.Inject;

import template.template_microservice.persistence.HelloWorldBE;

/**
 * example business implementation
 * @author robert.diers
 * @project Application
 * copyright: (c) Accenture 2019, all rights reserved
 */
public class HelloWorldBAImpl implements HelloWorldBA {
	
	@Inject
	HelloWorldBE helloWorldBE;
	
	//default constructor
	public HelloWorldBAImpl() {}
	
	//junit constructor
	public HelloWorldBAImpl(HelloWorldBE helloWorldBE) {
		this.helloWorldBE = helloWorldBE;
	}

	@Override
	public String sayHello() {
		
		//here we should do all of our business stuff...
		return helloWorldBE.sayHello();
	}

}
