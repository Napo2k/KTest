package org.gradle.examples.web;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;

import org.gradle.examples.web.ServletTesterUtils;

public class ServletTest
{
	@Test
	public void getRequestTest() throws Exception {
		ServletTester servletTester = ServletTesterUtils.createServletTester();
		ServletTesterUtils.initServlet(servletTester, "/", org.gradle.examples.web.Servlet.class, "/validUrl");
		
		HttpTester response = ServletTesterUtils.makeRequest(servletTester, "/validUrl");
		
		assertEquals(200,response.getStatus());
		assertEquals("hello, world",response.getContent());
	}
}