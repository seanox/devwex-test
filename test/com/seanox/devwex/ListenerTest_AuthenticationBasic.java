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
public class ListenerTest_AuthenticationBasic extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a [acc:usr-a:pwd-a:Section-A]}
     *  Without {@code usr-a:pwd-a} the request is responded with status 401.
     *  @throws Exception
     */
    @Test
    public void testAceptance_01() throws Exception {
        
        String request = "GET /authentication/a/ HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic realm=\"Section-A\"\r\n"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a/b/d [acc:none]}
     *  With {@code [acc:none]} no authentication is required and the request
     *  is responded with status 200.
     *  @throws Exception
     */
    @Test
    public void testAceptance_02() throws Exception {
        
        String request = "GET /authentication/a/b/d/ HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a [acc:usr-a:pwd-a:Section-A]}
     *  With {@code usr-a:pwd-a} the authentication is correct and the request
     *  is responded with status 200.
     *  @throws Exception
     */
    @Test
    public void testAceptance_03() throws Exception {
        
        String request = "GET /authentication/a/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic dXNyLWE6cHdkLWE=";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-a\" \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a/b [acc:usr-b:pwd-b:Section-B]}
     *  With {@code usr-a:pwd-a} the authentication is not correct and the
     *  request is responded with status 401.
     *  @throws Exception
     */
    @Test
    public void testAceptance_04() throws Exception {
        
        String request = "GET /authentication/a/b/ HTTP/1.0\r\n"
               + "Host: vHa\r\n"
               + "Authorization: Basic dXNyLWE6cHdkLWE=";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic realm=\"Section-B\"\r\n"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a/b [acc:usr-b:pwd-b:Section-B]}
     *  With {@code usr-b:pwd-b} the authentication is correct and the request
     *  is responded with status 200.
     *  @throws Exception
     */
    @Test
    public void testAceptance_05() throws Exception {
        
        String request = "GET /authentication/a/b/ HTTP/1.0\r\n"
               + "Host: vHa\r\n"
               + "Authorization: Basic dXNyLWI6cHdkLWI=";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-b\" \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a [acc:usr-a:pwd-a:Section-A]}
     *  With {@code usr-b:pwd-b} the authentication is not correct and the
     *  request is responded with status 401.
     *  @throws Exception
     */
    @Test
    public void testAceptance_06() throws Exception {
        
        String request = "GET /authentication/a/ HTTP/1.0\r\n"
               + "Host: vHa\r\n"
               + "Authorization: Basic dXNyLWI6cHdkLWI=";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic realm=\"Section-A\"\r\n"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a/b/c [acc:usr-a:pwd-a:Section-A2]}
     *  With {@code usr-a:pwd-a} the authentication is correct and the request
     *  is responded with status 200.
     *  @throws Exception
     */
    @Test
    public void testAceptance_07() throws Exception {
        
        String request = "GET /authentication/a/b/c/ HTTP/1.0\r\n"
               + "Host: vHa\r\n"
               + "Authorization: Basic dXNyLWE6cHdkLWE=";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-a\" \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }   
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a/b/c [acc:usr-a:pwd-a:Section-A2]}
     *  The authentication is missing and the request is responded with status
     *  401.
     *  @throws Exception
     */
    @Test
    public void testAceptance_08() throws Exception {
        
        String request = "GET /authentication/a/b/c/ HTTP/1.0\r\n"
               + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic realm=\"Section-A2\"\r\n"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a/b/d/e [acc:usr-e:pwd-e:Section-E]}
     *  The authentication is missing and the request is responded with status
     *  401.
     *  @throws Exception
     */
    @Test
    public void testAceptance_09() throws Exception {
        
        String request = "GET /authentication/a/b/d/e/ HTTP/1.0\r\n"
               + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic realm=\"Section-E\"\r\n"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
    }    
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a/b/e [acC:group:BE[realm:Section-BE}
     *  With a corrupt acc rule the authentication is ignored and the request
     *  is responded with status 200.
     *  @throws Exception
     */
    @Test
    public void testAceptance_98() throws Exception {
        
        String request = "GET /authentication/a/b/e/ HTTP/1.0\r\n"
               + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a/b/e/c [acC:group:BEC][realm:Section-BEC}
     *  With a correct acc rule after a corrupt acc rule, the authentication is
     *  requierd and the request is responded with status 401.
     *  @throws Exception
     */
    @Test
    public void testAceptance_99() throws Exception {
        
        String request = "GET /authentication/a/b/e/c/ HTTP/1.0\r\n"
               + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic realm=\"Section-BEC\"\r\n"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
    }     
}