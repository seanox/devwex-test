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
     *  {@code /authentication/a/b/d/e/e [acc:usr-e:pwd-e:Section-E]}
     *  The authentication is missing and the request is responded with status
     *  401.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_10() throws Exception {
        
        String request = "GET /authentication/a/b/d/e/e/ HTTP/1.0\r\n"
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
     *  {@code /authentication/a/b/d/e/e [acc:usr-e:pwd-e:Section-E]}
     *  With {@code acc:usr-e:pwd-e} the authentication is correct and the request
     *  is responded with status 200.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_11() throws Exception {
        
        String request = "GET /authentication/a/b/d/e/e/ HTTP/1.0\r\n"
               + "Host: vHa\r\n"
               + "Authorization: Basic dXNyLWU6cHdkLWU=";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-e\" \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a/b/d/e/e [acc:usr-e:pwd-e:Section-E]}
     *  With {@code acc:usr-e:pwd-e} the authentication is correct and the
     *  request is responded with status 302, because requested is a directory.
     *  @throws Exception
     */
    @Test
    public void testAceptance_12() throws Exception {
        
        String request;
        String response;
        String accessLog;
        
        request = "GET /authentication/a/b/d/e/e HTTP/1.0\r\n"
               + "Host: vHa\r\n"
               + "Authorization: Basic " + TestUtils.encodeBase64("usr-e:pwd-e");
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 302\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?s).*\r\nLocation: http://vHa:8080/authentication/a/b/d/e/e/\r\n.*$"));

        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-e\" \\[[^]]+\\]\\s\"[^\"]+\"\\s302\\s\\d+\\s-\\s-$"));
        
        request = "GET /authentication/a/b/d/e/e/ HTTP/1.0\r\n"
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
    
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic realm=\"Section-E\"\r\n"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nContent-Length: \\d+\r\n.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nContent-Type: text/html\r\n.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
         
        request = "GET /authentication/a/b/d/e/e/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + TestUtils.encodeBase64("usr-e:pwd-e");
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));

        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-e\" \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }
    
    private static void assertAceptance_13(String path, String status, int auth) throws Exception {
        
        String request = "GET " + path + " HTTP/1.0\r\n"
                + "Host: vHa\r\n";
        if (auth == 1)
            request += "Authorization: Basic " + TestUtils.encodeBase64("usr-e:pwd-e");
        if (auth == 2)
            request += "Authorization: Basic " + TestUtils.encodeBase64("usr-a:pwd-a");
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
      
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 " + status + "\\s+\\w+.*$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  The access to @{code /authentication/a/b/d/e/*} is defined in
     *  combination with option @{code [C]}. ACC has the higher priority and
     *  must take precedence over the option @{code [C]}.
     *  All request with ACC are responded with status 401.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_13() throws Exception {
        
        ListenerTest_AuthenticationBasic.assertAceptance_13("/authentication/a/b/c/f", "401", 0);
        ListenerTest_AuthenticationBasic.assertAceptance_13("/authentication/a/b/c/g", "401", 0);
        ListenerTest_AuthenticationBasic.assertAceptance_13("/authentication/a/b/c/h", "401", 0);
        ListenerTest_AuthenticationBasic.assertAceptance_13("/authentication/a/b/c/i", "401", 0);

        ListenerTest_AuthenticationBasic.assertAceptance_13("/authentication/a/b/c/f", "403", 1);
        ListenerTest_AuthenticationBasic.assertAceptance_13("/authentication/a/b/c/g", "403", 1);
        ListenerTest_AuthenticationBasic.assertAceptance_13("/authentication/a/b/c/h", "403", 1);
        ListenerTest_AuthenticationBasic.assertAceptance_13("/authentication/a/b/c/i", "403", 1);

        ListenerTest_AuthenticationBasic.assertAceptance_13("/authentication/a/b/c/f", "401", 2);
        ListenerTest_AuthenticationBasic.assertAceptance_13("/authentication/a/b/c/g", "401", 2);
        ListenerTest_AuthenticationBasic.assertAceptance_13("/authentication/a/b/c/h", "401", 2);
        ListenerTest_AuthenticationBasic.assertAceptance_13("/authentication/a/b/c/i", "401", 2);
    }
    
    private static void assertAceptance_14(String path, String realm, int auth) throws Exception {
        
        String request = "GET " + path + " HTTP/1.0\r\n"
                + "Host: vHa\r\n";
        if (auth == 1)
            request += "Authorization: Basic " + TestUtils.encodeBase64("usr-e:pwd-e");
        if (auth == 2)
            request += "Authorization: Basic " + TestUtils.encodeBase64("usr-a:pwd-a");
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic realm=\"" + realm + "\"\r\n"));        
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  The realm can be specified differently. Spaces at the beginning and
     *  end, as well the quotation mark are to be suppressed in the HTTP
     *  header. Without a realm, a clean HTTP header with realm must be sent.
     *  @throws Exception
     */   
    @Test
    public void testAceptance_14() throws Exception {
        
        ListenerTest_AuthenticationBasic.assertAceptance_14("/authentication/r/a/", "", 0);
        ListenerTest_AuthenticationBasic.assertAceptance_14("/authentication/r/b/", "", 0);
        ListenerTest_AuthenticationBasic.assertAceptance_14("/authentication/r/c/", "", 0);
        ListenerTest_AuthenticationBasic.assertAceptance_14("/authentication/r/d/", "", 0);
        ListenerTest_AuthenticationBasic.assertAceptance_14("/authentication/r/e/", "", 0);
        ListenerTest_AuthenticationBasic.assertAceptance_14("/authentication/r/f/", "x", 0);
        ListenerTest_AuthenticationBasic.assertAceptance_14("/authentication/r/g/", "x", 0);
        ListenerTest_AuthenticationBasic.assertAceptance_14("/authentication/r/h/", "x", 0);
        ListenerTest_AuthenticationBasic.assertAceptance_14("/authentication/r/i/", "x", 0);
        ListenerTest_AuthenticationBasic.assertAceptance_14("/authentication/r/j/", "x", 0);
        ListenerTest_AuthenticationBasic.assertAceptance_14("/authentication/r/k/", "", 0);        
    }
    
    private static void assertAceptance_15(String path, String realm, String auth) throws Exception {
        
        String request = "GET " + path + " HTTP/1.0\r\n"
                + "Host: vHa\r\n";
        if (auth != null && !auth.trim().isEmpty())
            request += "Authorization: Basic " + TestUtils.encodeBase64(auth);
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        if (realm != null)
            Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic realm=\"" + realm + "\"\r\n"));      
        else
            Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  Users and Realm are separated by the colon when encrypting. Below are
     *  tested different combinations in the definition.   
     *  @throws Exception
     */    
    @Test
    public void testAceptance_15() throws Exception {
        
        ListenerTest_AuthenticationBasic.assertAceptance_15("/authentication/s/a/", "", null);
        ListenerTest_AuthenticationBasic.assertAceptance_15("/authentication/s/a/", "", null);
        ListenerTest_AuthenticationBasic.assertAceptance_15("/authentication/s/b/", "sb", null);
        ListenerTest_AuthenticationBasic.assertAceptance_15("/authentication/s/b/", "sb", null);

        ListenerTest_AuthenticationBasic.assertAceptance_15("/authentication/s/a/", "", "usrSa1:");
        ListenerTest_AuthenticationBasic.assertAceptance_15("/authentication/s/a/", null, "usrSa2:");
        ListenerTest_AuthenticationBasic.assertAceptance_15("/authentication/s/b/", "sb", "usrSb1:");
        ListenerTest_AuthenticationBasic.assertAceptance_15("/authentication/s/b/", null, "usrSb2:");        
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a [acc:usr-a:pwd-a:Section-A]}
     *  With {@code acc:usr-a:pwd-a} the authentication is correct and the
     *  request is responded with status 404, because the request does not
     *  exit. In the access-log, the user is also logged because authorization
     *  was given. 
     *  @throws Exception
     */    
    @Test
    public void testAceptance_16() throws Exception {
        
        String request = "GET /authentication/a/xxx HTTP/1.0\r\n"
               + "Host: vHa\r\n"
               + "Authorization: Basic dXNyLWE6cHdkLWE=";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 404\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-a\" \\[[^]]+\\]\\s\"[^\"]+\"\\s404\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a [acc:usr-a:pwd-a:Section-A]}
     *  The authentication is missing and the request is responded with status
     *  401. Even if the file or directory does not exist.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_17() throws Exception {
        
        String request = "GET /authentication/a/xxx HTTP/1.0\r\n"
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
     *  {@code /authentication/a [acc:usr-a:pwd-a:Section-A]}
     *  The authentication is missing and the request is responded with status
     *  401.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_18() throws Exception {
        
        String request;
        String response;
        String accessLog;
        
        request = "GET /authentication/a/ HTTP/1.0\r\n"
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic realm=\"Section-A\"\r\n"));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
        
        request = "GET /authentication/a/ HTTP/1.0\r\n"
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
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