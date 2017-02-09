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

import com.seanox.devwex.TestHttpUtils.HeaderField;
import com.seanox.test.utils.Codec;

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
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
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
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
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
                + "Authorization: Basic dXNyLWE6cHdkLWE=\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
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
                + "Authorization: Basic dXNyLWE6cHdkLWE=\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
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
                + "Authorization: Basic dXNyLWI6cHdkLWI=\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
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
                + "Authorization: Basic dXNyLWI6cHdkLWI=\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
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
                + "Authorization: Basic dXNyLWE6cHdkLWE=\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
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
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
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
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
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
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
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
                + "Authorization: Basic dXNyLWU6cHdkLWU=\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
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
                + "Authorization: Basic " + Codec.encodeBase64("usr-e:pwd-e") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
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
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
    
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
                + "Authorization: Basic " + Codec.encodeBase64("usr-e:pwd-e") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));

        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-e\" \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }
    
    private static void assertAceptance_13(String uri, String status, int auth) throws Exception {
        
        String request = "GET " + uri + " HTTP/1.0\r\n"
                + "Host: vHa\r\n";
        if (auth == 1)
            request += "Authorization: Basic " + Codec.encodeBase64("usr-e:pwd-e") + "\r\n";
        if (auth == 2)
            request += "Authorization: Basic " + Codec.encodeBase64("usr-a:pwd-a") + "\r\n";
        request += "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
      
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
    
    private static void assertAceptance_14(String uri, String realm, int auth) throws Exception {
        
        String request = "GET " + uri + " HTTP/1.0\r\n"
                + "Host: vHa\r\n";
        if (auth == 1)
            request += "Authorization: Basic " + Codec.encodeBase64("usr-e:pwd-e") + "\r\n";
        if (auth == 2)
            request += "Authorization: Basic " + Codec.encodeBase64("usr-a:pwd-a") + "\r\n";
        request += "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
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
    
    private static void assertAceptance_15(String uri, String realm, String auth) throws Exception {
        
        String request = "GET " + uri + " HTTP/1.0\r\n"
                + "Host: vHa\r\n";
        if (auth != null && !auth.trim().isEmpty())
            request += "Authorization: Basic " + Codec.encodeBase64(auth) + "\r\n";
        request += "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
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
                + "Authorization: Basic dXNyLWE6cHdkLWE=\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
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
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic realm=\"Section-A\"\r\n"));  
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  Test for Basic Authentication:
     *  VHA defines {@code /authentication/a [acc:usr-a:pwd-a:Section-A]}.
     *  VHF extends VHA, overwrites {@code /authentication/a
     *  [acc:usr-a:pwd-a:Section-A] [D]}. VHA must refuse the authorization via
     *  DIGEST. VHA must refuse the authorization via BASIC.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_18() throws Exception {
        
        String request;
        String response;
        String accessLog;
        
        request = "GET /authentication/a/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic "));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
        
        request = "GET /authentication/a/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequestDigestAuthorisation("127.0.0.1:8080", request, "usr-a", "pwd-a"));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic "));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
        
        request = "GET /authentication/a/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-a:pwd-a") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-a\" \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
        
        request = "GET /authentication/a/ HTTP/1.0\r\n"
                + "Host: vHf\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Digest "));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
        
        request = "GET /authentication/a/ HTTP/1.0\r\n"
                + "Host: vHf\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-a:pwd-a") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Digest "));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
        
        request = "GET /authentication/a/ HTTP/1.0\r\n"
                + "Host: vHf\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequestDigestAuthorisation("127.0.0.1:8080", request, "usr-a", "pwd-a"));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-a\" \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a/b/d/e/e/lock [C]}
     *  The URI requires an authorization and has been redirected, because a
     *  directory with no slash at the end was requested. The requests must be
     *  responded with status 401 and 302.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_19() throws Exception {
        
        String request;
        String response;
        String accessLog;
        
        request = "GET /authentication/a/b/d/e/e HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic "));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));  
        
        request = "GET /authentication/a/b/d/e/e HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-e:pwd-e") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 302\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-e\" \\[[^]]+\\]\\s\"[^\"]+\"\\s302\\s\\d+\\s-\\s-$"));
        
        request = "HEAD /authentication/a/b/d/e/e HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic "));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));  
        
        request = "HEAD /authentication/a/b/d/e/e HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-e:pwd-e") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 302\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-e\" \\[[^]]+\\]\\s\"[^\"]+\"\\s302\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a/b/d/e/e/lock [C]}
     *  The URI requires an authorization and the target does not exits. The
     *  requests must be responded with status 401 and 404.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_20() throws Exception {
        
        String request;
        String response;
        String accessLog;
        
        request = "GET /authentication/a/b/d/e/e/nix HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic "));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
        
        request = "GET /authentication/a/b/d/e/e/nix HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-e:pwd-e") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 404\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-e\" \\[[^]]+\\]\\s\"[^\"]+\"\\s404\\s\\d+\\s-\\s-$"));
        
        request = "HEAD /authentication/a/b/d/e/e/nix HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic "));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
        
        request = "HEAD /authentication/a/b/d/e/e/nix HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-e:pwd-e") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 404\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-e\" \\[[^]]+\\]\\s\"[^\"]+\"\\s404\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a/b/d/e/e/lock [C]}
     *  The URI requires an authorization and has been forbidden. The requests
     *  must be responded with status 401 and 403.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_21() throws Exception {
        
        String request;
        String response;
        String accessLog;
        
        request = "GET /authentication/a/b/d/e/e/lock HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic "));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
        
        request = "GET /authentication/a/b/d/e/e/lock HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-e:pwd-e") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-e\" \\[[^]]+\\]\\s\"[^\"]+\"\\s403\\s\\d+\\s-\\s-$"));
        
        request = "HEAD /authentication/a/b/d/e/e/lock HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic "));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
        
        request = "HEAD /authentication/a/b/d/e/e/lock HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-e:pwd-e") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-e\" \\[[^]]+\\]\\s\"[^\"]+\"\\s403\\s\\d+\\s-\\s-$"));
    }

    private static void assertAceptance_22_1(String... args) throws Exception {
        
        String uri = null;
        if (args.length > 0)
            uri = args[0];
        String authorisation = null;
        if (args.length > 1)
            authorisation = args[1];
        String login = null;
        if (args.length > 2)
            login = args[2];

        String request = "GET " + uri + " HTTP/1.0\r\n"
                + "Host: vHa\r\n";
        if (login != null)
            request += "Authorization: Basic " + Codec.encodeBase64(login) + "\r\n";
        request += "\r\n";

        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        if (authorisation != null)
            Assert.assertTrue(TestHttpUtils.getResponseHeaderValue(response, HeaderField.WWW_AUTHENTICATE).startsWith(authorisation + " "));
        else
            Assert.assertFalse(TestHttpUtils.exitsResponseHeader(response, HeaderField.WWW_AUTHENTICATE));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        if (authorisation != null)
            Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
        else
            Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s302\\s\\d+\\s-\\s-$"));
    }

    private static void assertAceptance_22_2(String... args) throws Exception {
        
        String uri = null;
        if (args.length > 0)
            uri = args[0];
        String method = null;
        if (args.length > 1)
            method = args[1];
        String user = null;
        if (args.length > 2)
            user = args[2];        
        String password = null;
        if (args.length > 3)
            password = args[3]; 

        String request;
        String response;
        
        response = null;
        if (method == null || method.trim().isEmpty()) {
            request = "GET " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "\r\n";
            response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        } else if (("Basic").equalsIgnoreCase(method)) {
            request = "GET " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "Authorization: Basic " + Codec.encodeBase64(user + ":" + password) + "\r\n"
                    + "\r\n";
            response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        } else if (("Digest").equalsIgnoreCase(method)) {
            request = "GET " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "\r\n";
            response = new String(TestHttpUtils.sendRequestDigestAuthorisation("127.0.0.1:8080", request, user, password));
        } else Assert.fail("Unsupported authentication method: '" + method + "'");
        
        Assert.assertNotNull(response);
        Assert.assertFalse(response.trim().isEmpty());
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 302\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));

        if (user == null || user.trim().isEmpty())
            user = "-";
        else user = "\"" + user + "\""; 

        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\\Q" + user + "\\E \\[[^]]+\\]\\s\"[^\"]+\"\\s302\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  The directory structure {@code /o1} uses a mix of different
     *  authorizations. The correct use and response is checked.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_22() throws Exception {
        
        ListenerTest_AuthenticationBasic.assertAceptance_22_1("/o1", null);
        ListenerTest_AuthenticationBasic.assertAceptance_22_1("/o1/o2", null);
        ListenerTest_AuthenticationBasic.assertAceptance_22_1("/o1/o2/o3", null);
        ListenerTest_AuthenticationBasic.assertAceptance_22_1("/o1/o2/o3/b1", "Basic");
        ListenerTest_AuthenticationBasic.assertAceptance_22_1("/o1/o2/o3/b1/b2", "Basic");
        ListenerTest_AuthenticationBasic.assertAceptance_22_1("/o1/o2/o3/b1/b2/b3", "Basic");
        ListenerTest_AuthenticationBasic.assertAceptance_22_1("/o1/o2/o3/b1/b2/b3/n1", null);
        ListenerTest_AuthenticationBasic.assertAceptance_22_1("/o1/o2/o3/b1/b2/b3/n1/n2", null);
        ListenerTest_AuthenticationBasic.assertAceptance_22_1("/o1/o2/o3/b1/b2/b3/n1/n2/n3", null);
        ListenerTest_AuthenticationBasic.assertAceptance_22_1("/o1/o2/o3/b1/b2/b3/n1/n2/n3/d1", "Digest");
        ListenerTest_AuthenticationBasic.assertAceptance_22_1("/o1/o2/o3/b1/b2/b3/n1/n2/n3/d1/d2", "Digest");
        ListenerTest_AuthenticationBasic.assertAceptance_22_1("/o1/o2/o3/b1/b2/b3/n1/n2/n3/d1/d2/d3", "Digest");

        ListenerTest_AuthenticationBasic.assertAceptance_22_2("/o1");
        ListenerTest_AuthenticationBasic.assertAceptance_22_2("/o1/o2");
        ListenerTest_AuthenticationBasic.assertAceptance_22_2("/o1/o2/o3");
        ListenerTest_AuthenticationBasic.assertAceptance_22_2("/o1/o2/o3/b1", "Basic", "usr-b", "pwd-b");
        ListenerTest_AuthenticationBasic.assertAceptance_22_2("/o1/o2/o3/b1/b2", "Basic", "usr-b", "pwd-b");
        ListenerTest_AuthenticationBasic.assertAceptance_22_2("/o1/o2/o3/b1/b2/b3", "Basic", "usr-b", "pwd-b");
        ListenerTest_AuthenticationBasic.assertAceptance_22_2("/o1/o2/o3/b1/b2/b3/n1");
        ListenerTest_AuthenticationBasic.assertAceptance_22_2("/o1/o2/o3/b1/b2/b3/n1/n2");
        ListenerTest_AuthenticationBasic.assertAceptance_22_2("/o1/o2/o3/b1/b2/b3/n1/n2/n3");
        ListenerTest_AuthenticationBasic.assertAceptance_22_2("/o1/o2/o3/b1/b2/b3/n1/n2/n3/d1", "Digest", "usr-d", "pwd-d");
        ListenerTest_AuthenticationBasic.assertAceptance_22_2("/o1/o2/o3/b1/b2/b3/n1/n2/n3/d1/d2", "Digest", "usr-d", "pwd-d");
        ListenerTest_AuthenticationBasic.assertAceptance_22_2("/o1/o2/o3/b1/b2/b3/n1/n2/n3/d1/d2/d3", "Digest", "usr-d", "pwd-d");        
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a [acc:usr-a:pwd-a:Section-A]}
     *  The authentication is incorrect and the request is responded with
     *  status 401.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_23() throws Exception {
        
        String request = "GET /authentication/a/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-a:pwd-b") + "\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
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
     *  The authentication is incorrect and the request is responded with
     *  status 401.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_24() throws Exception {
        
        String request = "GET /authentication/a/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-b:pwd-a") + "\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic realm=\"Section-A\"\r\n"));  
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/bvc [Acc:group:c] [realm:sb] [C]}
     *  The URI requires an authorization and has been forbidden. The requests
     *  must be responded with status 401 and 403.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_25() throws Exception {
        
        String request;
        String response;
        String accessLog;
        
        request = "GET /authentication/bvc HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-a:pwd-a") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-a\" \\[[^]]+\\]\\s\"[^\"]+\"\\s403\\s\\d+\\s-\\s-$"));
        
        request = "GET /authentication/bvc HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-ax:pwd-ax") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));  
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate:"));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/bvr > http://www.heise.de [Acc:group:c] [R]}
     *  The URI requires an authorization and has been redirected. The requests
     *  must be responded with status 401 and 302.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_26() throws Exception {
        
        String request;
        String response;
        String accessLog;
        
        request = "GET /authentication/bvr HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-a:pwd-a") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 302\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-a\" \\[[^]]+\\]\\s\"[^\"]+\"\\s302\\s\\d+\\s-\\s-$"));
        
        request = "GET /authentication/bvr HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-ax:pwd-ax") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));  
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate:"));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/bvv > ./stage/documents_vh_A/test_a [Acc:group:c]}
     *  The URI requires an authorization and referenced a existing virtual
     *  path. The requests must be responded with status 401 and 200.
     *  @throws Exception
     */  
    @Test
    public void testAceptance_27() throws Exception {
        
        String request;
        String response;
        String accessLog;
        
        request = "GET /authentication/bvv/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-a:pwd-a") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-a\" \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
        
        request = "GET /authentication/bvv/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-as:pwd-ax") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate:"));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
        
        request = "GET /authentication/bvv HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-as:pwd-ax") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate:"));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
    }

    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/bvm > ConnectorA [v:xx=123] [m] [Acc:group:c]}
     *  The URI requires an authorization and referenced a modul. The requests 
     *  must be responded with status 401 and 001.
     *  @throws Exception
     */  
    @Test
    public void testAceptance_28() throws Exception {
        
        String request;
        String response;
        String accessLog;
        
        request = "GET /authentication/bvm/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-a:pwd-a") + "\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 001\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));  
        Assert.assertTrue(response.matches("(?s)^.*\\[v\\:xx=123\\] \\[m\\].*$"));
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s\"usr-a\" \\[[^]]+\\]\\s\"[^\"]+\"\\s1\\s\\d+\\s-\\s-$"));
        
        request = "GET /authentication/bvm HTTP/1.0\r\n"
           + "Host: vHa\r\n"
           + "Authorization: Basic " + Codec.encodeBase64("usr-ax:pwd-ax") + "\r\n"
           + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate:"));  
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
    }
    
    private static void assertAceptance_29(String uri, String user, String password, boolean authorisation) throws Exception {
        
        String request = "GET " + uri + " HTTP/1.0\r\n"
                + "Host: vHf\r\n"
                + "Authorization: Basic " + Codec.encodeBase64(user + ":" + password) + "\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));

         if (authorisation)
             Assert.assertTrue(TestHttpUtils.exitsResponseHeader(response, HeaderField.WWW_AUTHENTICATE));
         else
             Assert.assertFalse(TestHttpUtils.exitsResponseHeader(response, HeaderField.WWW_AUTHENTICATE));
         
         Thread.sleep(250);
         String accessLog = TestUtils.getAccessLogTail();
         if (authorisation)
             Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
         else
             Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s.*? \\[[^]]+\\]\\s\"[^\"]+\"\\s404\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  The directory structure {@code /authentication} uses a mix of different
     *  authorizations with different options and options . The correct use and
     *  response is checked.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_29() throws Exception {
        
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg0", "usr-a", "pwd-a", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg0", "usr-b", "pwd-b", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg0", "usr-e", "pwd-e", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg0", "usr-d", "pwd-d", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg0", "usr-x", "pwd-x", false);

        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg1", "usr-a", "pwd-a", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg1", "usr-b", "pwd-b", true);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg1", "usr-e", "pwd-e", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg1", "usr-d", "pwd-d", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg1", "usr-x", "pwd-x", true);

        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg2", "usr-a", "pwd-a", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg2", "usr-b", "pwd-b", true);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg2", "usr-e", "pwd-e", true);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg2", "usr-d", "pwd-d", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg2", "usr-x", "pwd-x", true);

        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg3", "usr-a", "pwd-a", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg3", "usr-b", "pwd-b", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg3", "usr-e", "pwd-e", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg3", "usr-d", "pwd-d", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg3", "usr-x", "pwd-x", false);

        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg4", "usr-a", "pwd-a", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg4", "usr-b", "pwd-b", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg4", "usr-e", "pwd-e", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg4", "usr-d", "pwd-d", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg4", "usr-x", "pwd-x", false);

        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg5", "usr-a", "pwd-a", true);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg5", "usr-b", "pwd-b", true);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg5", "usr-e", "pwd-e", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg5", "usr-d", "pwd-d", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg5", "usr-x", "pwd-x", true);

        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg6", "usr-a", "pwd-a", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg6", "usr-b", "pwd-b", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg6", "usr-e", "pwd-e", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg6", "usr-d", "pwd-d", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg6", "usr-x", "pwd-x", false);

        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg7", "usr-a", "pwd-a", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg7", "usr-b", "pwd-b", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg7", "usr-e", "pwd-e", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg7", "usr-d", "pwd-d", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg7", "usr-x", "pwd-x", false);

        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg8", "usr-a", "pwd-a", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg8", "usr-b", "pwd-b", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg8", "usr-e", "pwd-e", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg8", "usr-d", "pwd-d", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg8", "usr-x", "pwd-x", false);

        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg9", "usr-a", "pwd-a", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg9", "usr-b", "pwd-b", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg9", "usr-e", "pwd-e", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg9", "usr-d", "pwd-d", false);
        ListenerTest_AuthenticationBasic.assertAceptance_29("/authentication/bg9", "usr-x", "pwd-x", false);
    }
    
    private static void assertAceptance_30(String uri, String method) throws Exception {
        
        String request = "GET " + uri + " HTTP/1.0\r\n"
                + "Host: vHf\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
         
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: " + method + " "));  
         
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  The change of Autorisierungart must be working. In the following, the
     *  correct function is checked. The request must be responded with 401 and
     *  the correct {@code WWW-Authenticate}.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_30() throws Exception {
        
        ListenerTest_AuthenticationBasic.assertAceptance_30("/authentication/bdb/a", "Basic");
        ListenerTest_AuthenticationBasic.assertAceptance_30("/authentication/bdb/a/b", "Digest");
        ListenerTest_AuthenticationBasic.assertAceptance_30("/authentication/bdb/a/b/c", "Basic");
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
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
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
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Basic realm=\"Section-BEC\"\r\n"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
    }     
}