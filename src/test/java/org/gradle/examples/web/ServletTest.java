package org.gradle.examples.web;

import static org.junit.Assert.*;

import org.junit.*;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;

import org.gradle.examples.web.ServletTesterUtils;

public class ServletTest
{
	@Test
	public void testGetRequest() throws Exception {
		System.out.println("Starting");
		ServletTester servletTester = new ServletTester();
		servletTester.addServlet(org.gradle.examples.web.Servlet.class, "/test");
		servletTester.start();
		
		System.out.println("Sending request");
		
		HttpTester request = new HttpTester(); 
    	request.setMethod("GET");
    	request.setURI("/test");
    	request.setVersion("HTTP/1.0");
		
		HttpTester response = new HttpTester();
		response.parse(servletTester.getResponses(request.generate()));
		
		System.out.println("Handling response");
		assertEquals("hello, world",response.getContent());
	}
	/*
	@Test public void testNull() throws Exception {
		assertEquals("OK","OK");
	}*/
}