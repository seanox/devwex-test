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

import com.seanox.test.utils.Pattern;

/**
 *  TestCases for {@link com.seanox.devwex.Listener}.
 */
public class ListenerTest_Locate extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  The determination of a URL must also work in the mix of UFT8 / MIME.
     *  The request is responded with status 200.
     *  @throws Exception
     */
    @Test
    public void testAceptance_01() throws Exception {
        
        String request = "GET /url_ma%c3%9f%FF_1.html HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("-3-"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  The determination of a URL must also work in the mix of UFT8 / MIME.
     *  The request is responded with status 200.
     *  @throws Exception
     */
    @Test
    public void testAceptance_02() throws Exception {
        
        String request = "GET /url_ma%df%c3%bf_1.html HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("-3-"));
    }  
    
    /** 
     *  TestCase for aceptance.
     *  The determination of a URL must also work in the mix of UFT8 / MIME.
     *  The name of the file exists only in plain text but the url will be decoded.
     *  The request is responded with status 404.
     *  @throws Exception
     */
    @Test
    public void testAceptance_03() throws Exception {
        
        String request = "GET /url_ma%DF%FF_2.html HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 404\\s+\\w+.*$"));
    }    
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [VIRTUAL:VHA:REF] /forbidden > .../forbidden [c]}
     *  The Access to the path is forbidden. The request must be responded with
     *  status 403.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_05() throws Exception {
        
        String request = "GET /forbidden HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s403\\s\\d+\\s-\\s-$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [VIRTUAL:VHA:REF] /forbidden > .../forbidden [c]}
     *  The Access to the parent directory is forbidden. The request for the
     *  sub-directory must be responded also with status 403.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_06() throws Exception {
        
        String request = "GET /forbidden/not_forbidden/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s403\\s\\d+\\s-\\s-$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [VIRTUAL:VHA:REF] /forbidden_2 > .../forbidden}
     *  Virtual paths have independent options. If access to a physical path is
     *  forbidden, a virtual path can allow the access. The request must be
     *  responded with status 200.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_07() throws Exception {
        
        String request = "GET /forbidden_2/not_forbidden/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [VIRTUAL:VHA:REF] /test_test > .../documents_vh_A/test_a [-]}
     *  Virtual paths have independent options. The request must be responded
     *  with status 200, because {@code /test_test} is not equals to the other
     *  {@code /test_test...}.
     *  @throws Exception
     */        
    @Test
    public void testAceptance_08() throws Exception {
        
        String request = "GET /test_test/test_a.html HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        
        String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.matches("(?s)^\\s*test_a\\.html\\s*$"));        
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }

    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [VIRTUAL:VHA:REF] /test_test_a > .../documents_vh_A/test_b [-]}
     *  Virtual paths have independent options. The request must be responded
     *  with status 200, because {@code /test_test_a} is not equals to the
     *  other {@code /test_test...}.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_09() throws Exception {
        
        String request = "GET /test_test_a/test_b.html HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        
        String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.matches("(?s)^\\s*test_b\\.html\\s*$"));        
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }  
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [VIRTUAL:VHA:REF] /test_test_b > .../documents_vh_A/test_c [-]}
     *  Virtual paths have independent options. The request must be responded
     *  with status 200, because {@code /test_test_b} is not equals to the
     *  other {@code /test_test...}.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_10() throws Exception {
        
        String request = "GET /test_test_b/test_c.html HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        
        String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.matches("(?s)^\\s*test_c\\.html\\s*$"));        
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }  
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [VIRTUAL:VHA:REF] /test_test/test > .../documents_vh_A/test_d}
     *  Virtual paths have independent options. The request must be responded
     *  with status 200, because {@code /test_test/test/} is not equals to the
     *  other {@code /test_test...}. Even if the physical path
     *  {@code /test_test/test} " exists, the virtual path is preferred.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_11() throws Exception {
        
        String request = "GET /test_test/test/test_d.html HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        
        String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.matches("(?s)^\\s*test_d\\.html\\s*$"));        
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [VIRTUAL:VHA:REF] /absolute > .../forbidden/absolute.html [a]}
     *  The virtual path is defined as 'absolute'. The request must be
     *  responded with status 200 and the content of
     *  {@code .../documents_vh_A/forbidden/absolute.html}.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_12() throws Exception {
        
        String request = "GET /absolute HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        
        String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.matches("(?s)^\\s*absolute\\.html\\s*$"));        
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [VIRTUAL:VHA:REF] /test.2absolut > .../cgi_environment.jsx [a]}
     *  The virtual path is defined as 'absolute'. The request must be
     *  responded with status 200 and the parameters {@code PATH_BASE},
     *  {@code PATH_TRANSLATED}, {@code PATH_ABSOLUTE} and {@code PATH_INFO}.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_13() throws Exception {
        
        String request = "GET /test.2absolutes?parameter=PATH_BASE,PATH_TRANSLATED,PATH_ABSOLUTE,PATH_INFO HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        
        String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        
        Assert.assertTrue(body.matches("(?s)^.*\r\nPATH_INFO=es\r\n.*$"));
        Assert.assertTrue(body.matches("(?s)^.*\r\nPATH_BASE=/test\\.2absolutes\r\n.*$"));
        Assert.assertTrue(body.matches("(?s)^.*\r\nPATH_TRANSLATED=[^\r\n]+[\\\\/]cgi_environment.jsx\r\n.*$"));
        Assert.assertTrue(body.matches("(?s)^.*\r\nPATH_ABSOLUTE=/test\\.2absolut\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [VIRTUAL:VHA:REF] /forbidden.html [c]}
     *  The forbid of individual files is possible. The request must be
     *  responded with status 403 and the correct location.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_14() throws Exception {
        
        String request = "GET /forbidden.html HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s403\\s\\d+\\s-\\s-$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [VIRTUAL:VHA:REF] /redirect > http://www.xXx.zzz/?a=2 [r]}
     *  The virtual path is defined as redirection. The request must be
     *  responded with status 302 and the correct location.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_15() throws Exception {
        
        String request = "GET /redirect HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1") + "\r\n";
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 302\\s+\\w+.*$"));
        Assert.assertTrue(header.matches("(?s)^.*\r\nLocation: http://www\\.xXx\\.zzz/\\?a=2\r\n.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sLast-Modified:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s302\\s\\d+\\s-\\s-$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [VIRTUAL:VHA:REF] /redirect > http://www.xXx.zzz/?a=2 [r]}
     *  The virtual path is defined as redirection. The request must be
     *  responded with status 302 and the correct location.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_16() throws Exception {
        
        String request = "GET /redirect?asss HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1") + "\r\n";
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 302\\s+\\w+.*$"));
        Assert.assertTrue(header.matches("(?s)^.*\r\nLocation: http://www\\.xXx\\.zzz/\\?a=2\\?asss\r\n.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sLast-Modified:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s302\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [VIRTUAL:VHA:REF] /redirect > http://www.xXx.zzz/?a=2 [r]}
     *  The virtual path is defined as redirection. The request must be
     *  responded with status 302 and the correct location.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_17() throws Exception {
        
        String request = "GET /redirect/test/a/b/c?asss HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1") + "\r\n";
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 302\\s+\\w+.*$"));
        Assert.assertTrue(header.matches("(?s)^.*\r\nLocation: http://www\\.xXx\\.zzz/\\?a=2/test/a/b/c\\?asss\r\n.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sLast-Modified:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s302\\s\\d+\\s-\\s-$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [VIRTUAL:VHA:REF] /redirect > http://www.xXx.zzz/?a=2 [r]}
     *  The virtual path is defined as redirection. The request must be
     *  responded with status 302 and the correct location.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_18() throws Exception {
        
        String request = "GET /redirect/test/a/b/c/?asss HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1") + "\r\n";
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 302\\s+\\w+.*$"));
        Assert.assertTrue(header.matches("(?s)^.*\r\nLocation: http://www\\.xXx\\.zzz/\\?a=2/test/a/b/c/\\?asss\r\n.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sLast-Modified:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s302\\s\\d+\\s-\\s-$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  The characters &gt; and &lt; are invalid.
     *  The request must be responded with status 404.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_19() throws Exception {
        
        String request = "GET /test_test/test/<>test_d.html HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 404\\s+\\w+.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s404\\s\\d+\\s-\\s-$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  The virtual path {@code /redirect} does not include
     *  {@code /redirectxxx}. The request must be responded with status 404.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_20() throws Exception {
        
        String request = "GET /redirectxxx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 404\\s+\\w+.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s404\\s\\d+\\s-\\s-$"));
    }     
    
    /** 
     *  TestCase for aceptance.
     *  The virtual path {@code /test_test} is a directory. Without a slash at
     *  the end, the request must be responded with status 302 and the correct
     *  location.
     *  @throws Exception
     */
    @Test
    public void testAceptance_21() throws Exception {
        
        String request = "GET \\test_test HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1") + "\r\n";
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 302\\s+\\w+.*$"));
        Assert.assertTrue(header.matches("(?s)^.*\r\nLocation: http://vHa:8080/test_test/\r\n.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sLast-Modified:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s302\\s\\d+\\s-\\s-$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  The virtual path {@code /url_ma��_3.html} is a file. With a slash at
     *  the end, the request must be responded with status 302 and the correct
     *  location.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_22() throws Exception {
        
        String request = "GET \\url_ma��_3.html/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1") + "\r\n";
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 302\\s+\\w+.*$"));
        Assert.assertTrue(header.matches("(?s)^.*\r\nLocation: http://vHa:8080/url_ma��_3.html\r\n.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(header.matches("(?s)^.*\\sLast-Modified:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s302\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  The virtual path {@code /test.modul} is defined as module. The request
     *  must be responded by ConnectorA with status 001.
     *  @throws Exception
     */
    @Test
    public void testAceptance_23() throws Exception {
        
        String request = "GET /test.modul HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1") + "\r\n";
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 001 Test ok\\s+\\w+.*$"));
        Assert.assertTrue(header.matches("(?s)^.*\r\nModul: ConnectorA\r\n.*$"));
        Assert.assertTrue(header.matches("(?s)^.*\r\nModultype: 7\r\n.*$"));
        Assert.assertTrue(header.matches("(?s)^.*\r\nOpts: ConnectorA \\[v:xx=123\\] \\[m\\]\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s1\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  The virtual path {@code /test.modul} is defined as module. Virtual
     *  paths of modusl are absolute. Even this request must be responded by
     *  ConnectorA with status 001.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_24() throws Exception {
        
        String request = "GET /test.modul123 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 001 Test ok\\s+\\w+.*$"));
        Assert.assertTrue(response.matches("(?s)^.*\r\nModul: ConnectorA\r\n.*$"));
        Assert.assertTrue(response.matches("(?s)^.*\r\nModultype: 7\r\n.*$"));
        Assert.assertTrue(response.matches("(?s)^.*\r\nOpts: ConnectorA \\[v:xx=123\\] \\[m\\]\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s1\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  The path {@code /cgi_environment.jsx.} with a dot at the end is not
     *  correct. The request must be responded  with status 404.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_25() throws Exception {
        
        String request = "GET \\cgi_environment.jsx. HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 404\\s+\\w+.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s404\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  The path {@code /cgi_environment.jsx} is  correct. The request must be
     *  responded  with status 200.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_26() throws Exception {
        
        String request = "GET \\cgi_environment.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  For Windows is a path case insensitive. Even with an incorect upercase
     *  letter in the path, the request must be responded  with status 200.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_27() throws Exception {
        
        if (!System.getProperty("os.name").toLowerCase().contains("win"))
            return;
        
        String request = "GET \\cgi_environmenT.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [VIRTUAL:VHA:REF] /lastmodified.jsx > .../lastmodified.jsx [A]}
     *  The virtual path is defined as 'absolute'. The request must be
     *  responded with status 200, event with a dot at the end.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_28() throws Exception {
        
        String request = "GET \\lastmodified.jsx. HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Breaking out of the DocRoot must not happen if the path contains masked
     *  characters.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_31() throws Exception {
        
        String request;
        String response;
        String header;
        String body1;
        String body2;
        String accessLog;

        request = "GET / HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        body1 = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));

        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));

        request = "GET /..\\..\\..\\\\..\\\\.. HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        body2 = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
        
        Assert.assertEquals(body1, body2);
    }
    
    /** 
     *  TestCase for aceptance.
     *  The virtual path {@code /x} is defined as 'absolute', but the server
     *  does not support the Get method. The request must be responded with
     *  status 405.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_36() throws Exception {
        
        String request = "GET /x/fs/ggg HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8091", request));
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 405\\s+\\w+.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s405\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  The virtual path {@code /x} is defined as 'absolute'. The request must
     *  be responded with status 405 and the content of the physical file.
     *  @throws Exception
     */  
    @Test
    public void testAceptance_37() throws Exception {
        
        String request = "GET /x/fs/ggg HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8092", request));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertTrue(response.matches("(?s)^.*\r\n\r\nHallo\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  The virtual path {@code /x/x} is defined as 'absolute' in the scope
     *  of {@code /x}. The request must be responded with status 200 and the
     *  file listing.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_38() throws Exception {
        
        String request = "GET /x/x/ HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8092", request));
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1") + "\r\n";
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertTrue(header.matches("(?s)^.*\r\nContent-Type: text/html\r\n.*$"));
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2") + "\r\n";
        Assert.assertTrue(body.matches("(?s)^.*\r\nindex of: /x/x\r\n.*$"));
        Assert.assertTrue(body.matches("(?s)^.*\r\nname:test\\.txt\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  In Windows XP, there was a bug with paths in the format of
     *  {@code / / /}. You can use illegal directories. The server must check
     *  the physical paths. The request must be responded with status 404.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_39() throws Exception {
        
        String request;
        
        for (int loop = 0; loop < 20; loop++) {
            request = "/";
            while (request.length() < ((loop +1) *2) +1)
                request += "%20/";
            request = "GET " + request + " HTTP/1.0\r\n"
                    + "\r\n";
            String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
            Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 404\\s+\\w+.*$"));
            
            Thread.sleep(250);
            String accessLog = TestUtils.getAccessLogTail();
            Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s404\\s\\d+\\s-\\s-$"));
        }
    }
    
    private static void assertAceptance_40(String uri, int status) throws Exception {

        String request = "GET /" + uri + " HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1") + "\r\n";
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 " + status + "\\s+\\w+.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s" + status + "\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  The various combinations of options for forwarding are checked.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_40() throws Exception {
        
        ListenerTest_Locate.assertAceptance_40("xaa",  403);
        ListenerTest_Locate.assertAceptance_40("xaax", 404);
        ListenerTest_Locate.assertAceptance_40("xab",  403);
        ListenerTest_Locate.assertAceptance_40("xabx", 404);
        ListenerTest_Locate.assertAceptance_40("xac",  403);
        ListenerTest_Locate.assertAceptance_40("xacx", 403);
        ListenerTest_Locate.assertAceptance_40("xad",  403);
        ListenerTest_Locate.assertAceptance_40("xadx", 403);

        ListenerTest_Locate.assertAceptance_40("xa1",  403);
        ListenerTest_Locate.assertAceptance_40("xa1x", 404);
        ListenerTest_Locate.assertAceptance_40("xa2",  403);
        ListenerTest_Locate.assertAceptance_40("xa2x", 403);
        ListenerTest_Locate.assertAceptance_40("xa3",  403);
        ListenerTest_Locate.assertAceptance_40("xa3x", 403);
        ListenerTest_Locate.assertAceptance_40("xa4",  403);
        ListenerTest_Locate.assertAceptance_40("xa4x", 404);
        ListenerTest_Locate.assertAceptance_40("xa5",  403);
        ListenerTest_Locate.assertAceptance_40("xa5x", 403);
        ListenerTest_Locate.assertAceptance_40("xa6",  403);
        ListenerTest_Locate.assertAceptance_40("xa6x", 403);
        ListenerTest_Locate.assertAceptance_40("xa7",  403);
        ListenerTest_Locate.assertAceptance_40("xa7x", 403);
        ListenerTest_Locate.assertAceptance_40("xa8",  403);
        ListenerTest_Locate.assertAceptance_40("xa8x", 403);

        ListenerTest_Locate.assertAceptance_40("xb1",  500);
        ListenerTest_Locate.assertAceptance_40("xb1x", 500);
        ListenerTest_Locate.assertAceptance_40("xb2",  500);
        ListenerTest_Locate.assertAceptance_40("xb2x", 500);
        ListenerTest_Locate.assertAceptance_40("xb3",  500);
        ListenerTest_Locate.assertAceptance_40("xb3x", 500);
        ListenerTest_Locate.assertAceptance_40("xb4",  500);
        ListenerTest_Locate.assertAceptance_40("xb4x", 500);

        ListenerTest_Locate.assertAceptance_40("xc1",  302);
        ListenerTest_Locate.assertAceptance_40("xc1x", 404);
        ListenerTest_Locate.assertAceptance_40("xc2",  302);
        ListenerTest_Locate.assertAceptance_40("xc2x", 302);
    }
    
    private static void assertAceptance_41(String uri, String value) throws Exception {
        
        String request = "GET /" + uri + " HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8083", request));
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1") + "\r\n";
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2") + "\r\n";
        Assert.assertTrue(body.matches("(?s)^.*\r\n\\Q" + value + "\\E\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
    }

    /** 
     *  TestCase for aceptance.
     *  The virtual path {@code /test.../xxx} is defined as 'absolute' for
     *  files and directories. The server must use the correct environment
     *  variable {@code SCRIPT_NAME} and the request must be responded with
     *  correct status and content.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_41() throws Exception {
        
        ListenerTest_Locate.assertAceptance_41("/test1/xxx/123", "/test1/xxx");
        ListenerTest_Locate.assertAceptance_41("/test2/xxx/123", "/test2/xxx/");
        ListenerTest_Locate.assertAceptance_41("/test3/xxx/123/", "/test3/xxx/index.jsx");
        ListenerTest_Locate.assertAceptance_41("/test4/xxx/123/", "/test4/xxx/index.jsx");        
    }
    
    /**
     *  TestCase for aceptance.
     *  The virtual path {@code /test.../xxx} is defined as 'absolute' for
     *  files and directories. The server must use the correct environment
     *  variable {@code SCRIPT_NAME} and the request must be responded with
     *  correct status and content.
     *  @throws Exception
     */
    @Test
    public void testAceptance_42() throws Exception {
        
        String request = "GET /m HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8091", request));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 405\\s+\\w+.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s405\\s\\d+\\s-\\s-$"));
    }
    
    /**
     *  TestCase for aceptance.
     *  The virtual path {@code /m/m} is defined as module, but the server does
     *  not support the GET method, but the options {@code [x]} says all
     *  methods are supported. The request must be responded by the module with
     *  status 001.
     *  @throws Exception
     */   
    @Test
    public void testAceptance_43() throws Exception {
        
        String request = "GET /m/m HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8091", request));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 001\\s+\\w+.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s1\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Breaking out of the DocRoot must not happen if the path contains masked
     *  special characters.
     *  @throws Exception
     */
    @Test
    public void testAceptance_94() throws Exception {
        
        String request;
        String response;
        
        request = "GET / HTTP/1.0\r\n\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8081", request));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        String header1 = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header1.trim().length() > 0);
        String body1 = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body1.length() > 0);  
        
        request = "GET /..%c0%af..%c0%af..%c0%af..%c0%af..%c0%af.. HTTP/1.0\r\n\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8081", request));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));  
        String header2 = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header2.trim().length() > 0);
        String body2 = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body2.length() > 0);        
        
        Assert.assertTrue(body1.length() == body2.length());
        Assert.assertTrue(body1.equals(body2));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Breaking out of the DocRoot must not happen if the path contains masked
     *  special characters.
     *  @throws Exception
     */
    @Test
    public void testAceptance_95() throws Exception {
        
        String request;
        String response;
        
        request = "GET / HTTP/1.0\r\n\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8081", request));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        String header1 = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header1.trim().length() > 0);
        String body1 = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body1.length() > 0);  
        
        request = "GET /commons/%2e%2e/%2e%2e%5c%2e%2e%c0%af%2f%2f%2e%2e%2f HTTP/1.0\r\n\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8081", request));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));  
        String header2 = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header2.trim().length() > 0);
        String body2 = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body2.length() > 0);        
        
        Assert.assertTrue(body1.length() == body2.length());
        Assert.assertTrue(body1.equals(body2));
    }  
    
    /** 
     *  TestCase for aceptance.
     *  Breaking out of the DocRoot must not happen if the path contains masked
     *  special characters.
     *  @throws Exception
     */
    @Test
    public void testAceptance_96() throws Exception {
        
        String request;
        String response;
        
        request = "GET / HTTP/1.0\r\n\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8081", request));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        String header1 = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header1.trim().length() > 0);
        String body1 = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body1.length() > 0);  
        
        request = "GET /xxx/%2e%2e/%2e%2e%5c%2e%2e%c0%af%2f%2f%2e%2e%2f HTTP/1.0\r\n\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8081", request));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));  
        String header2 = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header2.trim().length() > 0);
        String body2 = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body2.length() > 0);        
        
        Assert.assertTrue(body1.length() == body2.length());
        Assert.assertTrue(body1.equals(body2));
    }     
    
    /** 
     *  TestCase for aceptance.
     *  Breaking out of the DocRoot must not happen if the path contains masked
     *  special characters.
     *  @throws Exception
     */
    @Test
    public void testAceptance_97() throws Exception {
        
        String request;
        String response;
        
        request = "GET / HTTP/1.0\r\n\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8081", request));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        String header1 = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header1.trim().length() > 0);
        String body1 = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body1.length() > 0);  
        
        request = "GET /xxx/%2e%2e/%2e%2e%5c%2e%2e%%c0%af%2f%2f%2e%2e%2f HTTP/1.0\r\n\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8081", request));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));  
        String header2 = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header2.trim().length() > 0);
        String body2 = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body2.length() > 0);        
        
        Assert.assertTrue(body1.length() == body2.length());
        Assert.assertTrue(body1.equals(body2));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Breaking out of the DocRoot must not happen if the path contains masked
     *  special characters.
     *  @throws Exception
     */
    @Test
    public void testAceptance_98() throws Exception {
        
        String request;
        String response;
        
        for (int loop = 0; loop < 20; loop++) {

            request = "/";
            while (request.length() < ((loop +1) *2) +1)
                request += "%20/";

            request = "GET " + request + " HTTP/1.0\r\n\r\n";
            response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
            Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 404\\s+\\w+.*$"));
        }        
    }      
}