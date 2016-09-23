package org.gradle.examples.web;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.*;
import javax.servlet.http.*;
import org.junit.*;
import org.mockito.Mockito;

public class ServletTest extends Mockito {
	
    @Test
	public void testGetRequest() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        
        when(response.getWriter()).thenReturn(writer);
        
        new Servlet().doGet(request, response);
        
        writer.flush();
        assertEquals("hello, world\r\n", sw.toString());
    }
}
