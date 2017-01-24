/**
 *  LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 *  im Folgenden Seanox Software Solutions oder kurz Seanox genannt. Diese
 *  Software unterliegt der Version 2 der GNU General Public License.
 *
 *  Devwex, Advanced Server Development
 *  Copyright (C) 2017 Seanox Software Solutions
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of version 2 of the GNU General Public License as published
 *  by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 *  more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.seanox.devwex;

import org.junit.Assert;
import org.junit.Test;

/**
 *  TestCases for {@link com.seanox.devwex.Listener}.
 */
public class ListenerTest_Status extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  Request of a forbidden url.
     *  Template for status 403 is not dedicated and is contained in status-4xx.html.
     *  The response must contains 'Template: status-4xx.html'.
     *  @throws Exception
     */
    @Test
    public void testAceptance_1() throws Exception {
        
        String request = "Get /forbidden.html HTTP/1.0\r\n"
                + "Host: vHo";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8085", request + "\r\n\r\n"));

        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("Template: status-4xx.html"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Request of a faulty CGI application.
     *  Template for status 502 is not dedicated and is contained in status.html.
     *  The response must contains 'Template: status.html'.
     *  @throws Exception
     */
    @Test
    public void testAceptance_2() throws Exception {
        
        String request = "Get /error.cgi HTTP/1.0\r\n"
                + "Host: vHo";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8085", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 502\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("Template: status.html"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Request of a not exsting url.
     *  Template for status 404 is dedicated.
     *  The response must contains 'Template: status-404.html'.
     *  @throws Exception
     */
    @Test
    public void testAceptance_3() throws Exception {
        
        String request = "Get /not_found.html HTTP/1.0\r\n"
                + "Host: vHo";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8085", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 404\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("Template: status-404.html"));
    }    
}