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
public class ListenerTest_Gateway extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  Method {@code HEAD} was not defined for the CGI and the request is
     *  responded with status 403. For a method {@code HEAD} the server status
     *  is without content.
     *  @throws Exception
     */
    @Test
    public void testAceptance_01() throws Exception {
        
        String request = "HEAD /cgi_module.con HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s403\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  The file extension {@code *.con }" was defined as CGI with the module
     *  {@code ConnectorA}. Thus the module respponded the request with status
     *  001.
     *  @throws Exception
     */
    @Test
    public void testAceptance_02() throws Exception {
        
        String request = "GET /cgi_module.con HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 001 Test ok\\s+\\w+.*$"));
        Assert.assertTrue(response.matches("(?s)^.*\r\nModul: ConnectorA\r\n.*$"));
        Assert.assertTrue(response.matches("(?s)^.*\r\nModultype: 7\r\n.*$"));
        Assert.assertTrue(response.matches("(?s)^.*\r\nOpts: ConnectorA \\[pa=1\\] \\[M\\]\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s1\\s\\d+\\s-\\s-$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  The file extension {@code *.con }" was defined as CGI with the module
     *  {@code ConnectorA}. Thus the module respponded the request with status
     *  001.
     *  @throws Exception
     */
    @Test
    public void testAceptance_03() throws Exception {
        
        String request = "GET /cgi_module.con/1 HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 404\\s+\\w+.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nContent-Type: \\w+.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nContent-Length: \\d+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s404\\s\\d+\\s-\\s-$"));      
    } 
    
    /** 
     *  TestCase for aceptance.
     *  The CGI script responds the request with {@code HTTP/1.1 123 Test ...}.
     *  So must also the response header contain {@code HTTP/1.0 123 Test ...}
     *  and be logged with status 123.
     *  @throws Exception
     */
    @Test
    public void testAceptance_05() throws Exception {
        
        String request = "GET /cgi_header_status1.bsh HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 123 Test\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nServer: Seanox-Devwex.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s123\\s\\d+\\s-\\s-$"));      
    } 
    
    /** 
     *  TestCase for aceptance.
     *  The CGI script responds the request with {@code HTTP/1.1 401 Test ...}.
     *  The status text is individual but is respondedd with the server
     *  standard {@code HTTP/1.0 401 Authorization Required}.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_99() throws Exception {
        
        String request = "GET /cgi_header_statusc.bsh HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401 Authorization Required\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nServer: Seanox-Devwex.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));      
    }    
}