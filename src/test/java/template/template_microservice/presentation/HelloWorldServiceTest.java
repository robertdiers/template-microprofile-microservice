package template.template_microservice.presentation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import template.template_microservice.business.HelloWorldBA;
import template.template_microservice.business.HelloWorldBAImpl;
import template.template_microservice.persistence.HelloWorldBE;
import template.template_microservice.persistence.HelloWorldBEImpl;

/**
 * example REST end point test
 * @author robert.diers
 * @project Application
 * copyright: (c) Accenture 2019, all rights reserved
 */
public class HelloWorldServiceTest {
	
	@Test
    public void multiplicationOfZeroIntegersShouldReturnZero() {
		
		HelloWorldBE helloWorldBE = new HelloWorldBEImpl();
		HelloWorldBA helloWorldBA = new HelloWorldBAImpl(helloWorldBE);
		HelloWorldService service = new HelloWorldService(helloWorldBA); // MyClass is tested

        // assert statements
        assertEquals("Response should be 200", 200, service.sayHello().getStatus());
    }

}
