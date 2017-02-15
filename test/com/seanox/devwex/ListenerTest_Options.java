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
import com.seanox.test.utils.Pattern;

/**
 *  TestCases for {@link com.seanox.devwex.Listener}.
 */
public class ListenerTest_Options extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  For method {@code OPTIONS}, The target is not checked. Whether exits or
     *  not, the request is responded with status 200, {@code Allow} and
     *  without content details. {@code OPTIONS} is only a request about the
     *  supported HTTP methods.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_01() throws Exception {

        String request = "OPTIONS / HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));      
    } 
    
    /** 
     *  TestCase for aceptance.
     *  For method {@code OPTIONS}, The target is not checked. Whether exits or
     *  not, the request is responded with status 200, {@code Allow} and
     *  without content details. {@code OPTIONS} is only a request about the
     *  supported HTTP methods.
     *  @throws Exception
     */ 
    @Test
    public void testAceptance_02() throws Exception {

        String request = "OPTIONS /test_a HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));      
    } 
    
    /** 
     *  TestCase for aceptance.
     *  For method {@code OPTIONS}, The target is not checked. Whether exits or
     *  not, the request is responded with status 200, {@code Allow} and
     *  without content details. {@code OPTIONS} is only a request about the
     *  supported HTTP methods.
     *  @throws Exception
     */ 
    @Test
    public void testAceptance_03() throws Exception {

        String request = "OPTIONS /test_ax HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));      
    } 

    /** 
     *  TestCase for aceptance.
     *  For method {@code OPTIONS}, The target is not checked. Whether exits or
     *  not, the request is responded with status 200, {@code Allow} and
     *  without content details. {@code OPTIONS} is only a request about the
     *  supported HTTP methods.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_04() throws Exception {

        String request = "OPTIONS /method_file.txt HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));      
    }
    
    /** 
     *  TestCase for aceptance.
     *  For method {@code OPTIONS}, The target is not checked. Whether exits or
     *  not, the request is responded with status 200, {@code Allow} and
     *  without content details. {@code OPTIONS} is only a request about the
     *  supported HTTP methods.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_05() throws Exception {

        String request = "OPTIONS /method_file.txt/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));      
    } 
    
    /** 
     *  TestCase for aceptance.
     *  For method {@code OPTIONS}, the request header
     *  {@code If-Modified-Since} is ignored. Whether correct or invalid, the
     *  request is responded with status 200, {@code Allow} and without content
     *  details.
     *  @throws Exception
     */
    @Test
    public void testAceptance_06() throws Exception {

        String request;
        String response;
        
        request = "HEAD /method_file.txt HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));       
        String lastModified = TestHttpUtils.getResponseHeaderValue(response, HeaderField.LAST_MODIFIED);
        
        request = "OPTIONS /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: " + lastModified + "\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));    
    } 
    
    /** 
     *  TestCase for aceptance.
     *  For method {@code OPTIONS}, the request header
     *  {@code If-Modified-Since} is ignored. Whether correct or invalid, the
     *  request is responded with status 200, {@code Allow} and without content
     *  details.
     *  @throws Exception
     */
    @Test
    public void testAceptance_07() throws Exception {
        
        String request = "OPTIONS /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: Sat, 01 Jan 2000 00:00:00 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));     
    } 
    
    /** 
     *  TestCase for aceptance.
     *  For method {@code OPTIONS}, the request header
     *  {@code If-Modified-Since} is ignored. Whether correct or invalid, the
     *  request is responded with status 200, {@code Allow} and without content
     *  details.
     *  @throws Exception
     */
    @Test
    public void testAceptance_08() throws Exception {

        String request;
        String response;
        
        request = "HEAD /method_file.txt HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));       
        String lastModified = TestHttpUtils.getResponseHeaderValue(response, HeaderField.LAST_MODIFIED);
        String contentLength = TestHttpUtils.getResponseHeaderValue(response, HeaderField.CONTENT_LENGTH);
        
        request = "OPTIONS /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: " + lastModified + "; xxx; length=" + contentLength + "\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));    
    } 
    
    /** 
     *  TestCase for aceptance.
     *  For method {@code OPTIONS}, the request header
     *  {@code If-Modified-Since} is ignored. Whether correct or invalid, the
     *  request is responded with status 200, {@code Allow} and without content
     *  details.
     *  @throws Exception
     */
    @Test
    public void testAceptance_09() throws Exception {
        
        String request;
        String response;
        
        request = "HEAD /method_file.txt HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));       
        String lastModified = TestHttpUtils.getResponseHeaderValue(response, HeaderField.LAST_MODIFIED);
        String contentLength = TestHttpUtils.getResponseHeaderValue(response, HeaderField.CONTENT_LENGTH);
        
        request = "OPTIONS /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: " + lastModified + "; xxx; length=1" + contentLength + "\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));     
    }
    
    /** 
     *  TestCase for aceptance.
     *  For method {@code OPTIONS}, the target (also directories and
     *  independent of default) is not checked. Whether exits or not, the
     *  request is responded with status 200, {@code Allow} and without content
     *  details. {@code OPTIONS} is only a request about the supported HTTP
     *  methods. Header fields: {@code Range}, {@code If-Modified-Since} and
     *  {@code If-UnModified-Since}
     *  are ignored. 
     *  @throws Exception
     */     
    @Test
    public void testAceptance_11() throws Exception {
        
        String request = "OPTIONS /test_a/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));     
    } 
    
    /** 
     *  TestCase for aceptance.
     *  For method {@code OPTIONS}, the target (also directories) is not
     *  checked. Whether exits or not, the request is responded with status
     *  200, {@code Allow} and without content details. {@code OPTIONS} is only
     *  a request about the supported HTTP methods. Header fields:
     *  {@code Range}, {@code If-Modified-Since} and {@code If-UnModified-Since}
     *  are ignored. 
     *  @throws Exception
     */    
    @Test
    public void testAceptance_12() throws Exception {
        
        String request = "OPTIONS /test_d/ HTTP/1.0\r\n"
                + "If-Modified-Since: Mon, 11 Jan 2004 19:11:58 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));     
    } 
    
    /** 
     *  TestCase for aceptance.
     *  For method {@code OPTIONS}, the request for a forbidden target is
     *  responded with status 403. 
     *  @throws Exception
     */     
    @Test
    public void testAceptance_13() throws Exception {
        
        String request = "OPTIONS /forbidden HTTP/1.0\r\n"
                + "If-Modified-Since: Mon, 11 Jan 2004 19:11:58 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_403));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_403));     
    } 
    
    /** 
     *  TestCase for aceptance.
     *  For method {@code OPTIONS}, the target (also with an absolute paths) is
     *  not checked. Whether exits or not, the request is responded with status
     *  200, {@code Allow} and without content details. {@code OPTIONS} is only
     *  a request about the supported HTTP methods. 
     *  @throws Exception
     */ 
    @Test
    public void testAceptance_14() throws Exception {
        
        String request = "OPTIONS /absolute HTTP/1.0\r\n"
                + "If-Modified-Since: Mon, 11 Jan 2004 19:11:58 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));      
    } 
    
    /** 
     *  TestCase for aceptance.
     *  For method {@code OPTIONS}, the target (also with an absolute paths) is
     *  not checked. Whether exits or not, the request is responded with status
     *  200, {@code Allow} and without content details. {@code OPTIONS} is only
     *  a request about the supported HTTP methods. 
     *  @throws Exception
     */    
    @Test
    public void testAceptance_15() throws Exception {
        
        String request = "OPTIONS /absolutexxx HTTP/1.0\r\n"
                + "If-Modified-Since: Mon, 11 Jan 2004 19:11:58 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));      
    } 
    
    /**
     *  TestCase for aceptance.
     *  For the CGI, the method {@code OPTIONS} are responded by the CGI.
     *  In the test case, {@code OPTIONS} is for the CGI not allowed and the
     *  request is responded with status 403, also if the CGI not exists.
     *  @throws Exception
     */
    @Test
    public void testAceptance_16() throws Exception {
        
        String request = "OPTIONS /test.method.php HTTP/1.0\r\n"
                + "If-Modified-Since: Mon, 11 Jan 2004 19:11:58 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_403));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_403));     
    } 
    
    /**
     *  TestCase for aceptance.
     *  For the CGI, the method {@code OPTIONS} are responded by the CGI.
     *  In the test case, {@code OPTIONS} is for the CGI allowed and the
     *  request is responded with status 200.
     *  @throws Exception
     */
    @Test
    public void testAceptance_17() throws Exception {
        
        String request = "oPTIONS /method.jsx HTTP/1.0\r\n"
                + "Host: vHb\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_ALLOW_DIFFUSE));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.matches("(?si)^.*hallo.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));     
    }
    
    /**
     *  TestCase for aceptance.
     *  For the CGI, the method {@code OPTIONS} are responded by the CGI.
     *  In the test case, {@code OPTIONS} is for the CGI not allowed and the
     *  request is responded with status 403.
     *  @throws Exception
     */
    @Test
    public void testAceptance_18() throws Exception {
        
        String request = "oPTIONS /method.jsx HTTP/1.0\r\n"
                + "Host: vHe\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_403));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_403));    
    }
    
    private static void assertAceptance_19(int count, String uri, String start, String end) throws Exception {
        
        if (start != null && start.isEmpty())
            start = null;
        if (end != null && end.isEmpty())
            end = null;
        
        boolean case0 = start == null && end == null;
        boolean case1 = start == null || end == null;
        
        String request = "Options " + uri + " HTTP/1.0\r\n"
                + "Host: vHa\r\n";
        if (!case0) {
            request += "Range: bYteS = " + (start != null ? start : "");
            if (!case1)
                request += count % 2 == 0 ? "-" : " - ";
            request += end != null ? end : "";
            request += "\r\n";
        }
        
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request + "\r\n"));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_RANGE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));  
    }
    
    /** 
     *  TestCase for aceptance.
     *  For method {@code OPTIONS}, the target is not checked. Whether exits or
     *  not, the request is responded with status 200, {@code Allow} and
     *  without content details. {@code OPTIONS} is only a request about the
     *  supported HTTP methods. Header fields: {@code Range},
     *  {@code If-Modified-Since} and {@code If-UnModified-Since} are ignored. 
     *  @throws Exception
     */
    @Test
    public void testAceptance_19() throws Exception {
        
        for (String uri : new String[] {"/partial_content.txt", "/partial_content-nix.txt", "/"}) {

            int count = 0;
        
            ListenerTest_Options.assertAceptance_19(count++, uri, "0",      "0");
            ListenerTest_Options.assertAceptance_19(count++, uri, "0",      "1");    
            ListenerTest_Options.assertAceptance_19(count++, uri, "0",      "127");
            ListenerTest_Options.assertAceptance_19(count++, uri, "0",      "65535");
            ListenerTest_Options.assertAceptance_19(count++, uri, "1",      "0");
            ListenerTest_Options.assertAceptance_19(count++, uri, "1",      "1");
            ListenerTest_Options.assertAceptance_19(count++, uri, "1",      "127");    
            ListenerTest_Options.assertAceptance_19(count++, uri, "1",      "65535");
            ListenerTest_Options.assertAceptance_19(count++, uri, "127",    "256");
            ListenerTest_Options.assertAceptance_19(count++, uri, "256",    "127");
    
            ListenerTest_Options.assertAceptance_19(count++, uri, "127",    "0");
            ListenerTest_Options.assertAceptance_19(count++, uri, "127",    "1");
            ListenerTest_Options.assertAceptance_19(count++, uri, "65535",  "0");
            ListenerTest_Options.assertAceptance_19(count++, uri, "65535",  "1");
            ListenerTest_Options.assertAceptance_19(count++, uri, "256",    "65535");
            ListenerTest_Options.assertAceptance_19(count++, uri, "65535",  "256");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-256",   "127");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-127",   "256");
            ListenerTest_Options.assertAceptance_19(count++, uri, "256",    "-127");
            ListenerTest_Options.assertAceptance_19(count++, uri, "127",    "-256");
    
            ListenerTest_Options.assertAceptance_19(count++, uri, "0",      "A");
            ListenerTest_Options.assertAceptance_19(count++, uri, "1",      "A");
            ListenerTest_Options.assertAceptance_19(count++, uri, "256",    "B");
            ListenerTest_Options.assertAceptance_19(count++, uri, "65535",  "C");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-0",     "A");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-1",     "A");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-256",   "B");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-65535", "C");
            ListenerTest_Options.assertAceptance_19(count++, uri, "A",      "0");
            ListenerTest_Options.assertAceptance_19(count++, uri, "A",      "1");
    
            ListenerTest_Options.assertAceptance_19(count++, uri, "B",      "256");
            ListenerTest_Options.assertAceptance_19(count++, uri, "C",      "65535");
            ListenerTest_Options.assertAceptance_19(count++, uri, "A",      "-0");
            ListenerTest_Options.assertAceptance_19(count++, uri, "A",      "-1");
            ListenerTest_Options.assertAceptance_19(count++, uri, "B",      "-256");
            ListenerTest_Options.assertAceptance_19(count++, uri, "C",      "-65535");
            ListenerTest_Options.assertAceptance_19(count++, uri, "0",      "");
            ListenerTest_Options.assertAceptance_19(count++, uri, "256",    "");
            ListenerTest_Options.assertAceptance_19(count++, uri, "65535",  "");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-0",     "");
    
            ListenerTest_Options.assertAceptance_19(count++, uri, "-1",     "");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-256",   "");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-65535", "");
            ListenerTest_Options.assertAceptance_19(count++, uri, null,     "0");
            ListenerTest_Options.assertAceptance_19(count++, uri, null,     "256");
            ListenerTest_Options.assertAceptance_19(count++, uri, null,     "65535");
            ListenerTest_Options.assertAceptance_19(count++, uri, null,     "A");
            ListenerTest_Options.assertAceptance_19(count++, uri, null,      null);
            ListenerTest_Options.assertAceptance_19(count++, uri, "",       "0");
            ListenerTest_Options.assertAceptance_19(count++, uri, "",       "256");
           
            ListenerTest_Options.assertAceptance_19(count++, uri, "",       "65535");
            ListenerTest_Options.assertAceptance_19(count++, uri, "",       "-0");
            ListenerTest_Options.assertAceptance_19(count++, uri, "",       "-1");
            ListenerTest_Options.assertAceptance_19(count++, uri, "",       "-256");
            ListenerTest_Options.assertAceptance_19(count++, uri, "",       "-65535");
            ListenerTest_Options.assertAceptance_19(count++, uri, "0",      " ");
            ListenerTest_Options.assertAceptance_19(count++, uri, "1",      " ");
            ListenerTest_Options.assertAceptance_19(count++, uri, "256",    " ");
            ListenerTest_Options.assertAceptance_19(count++, uri, "65535",  " ");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-0",     " ");
            
            ListenerTest_Options.assertAceptance_19(count++, uri, "-1",     " ");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-256",   " ");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-65535", " ");
            ListenerTest_Options.assertAceptance_19(count++, uri, " ",      "0");
            ListenerTest_Options.assertAceptance_19(count++, uri, " ",      "1");
            ListenerTest_Options.assertAceptance_19(count++, uri, " ",      "256");
            ListenerTest_Options.assertAceptance_19(count++, uri, " ",      "65535");
            ListenerTest_Options.assertAceptance_19(count++, uri, " ",      "-0");
            ListenerTest_Options.assertAceptance_19(count++, uri, " ",      "-1");
            ListenerTest_Options.assertAceptance_19(count++, uri, " ",      "-256");
            
            ListenerTest_Options.assertAceptance_19(count++, uri, " ",      "-65535");
            ListenerTest_Options.assertAceptance_19(count++, uri, "0",      "-");
            ListenerTest_Options.assertAceptance_19(count++, uri, "1",      "-");
            ListenerTest_Options.assertAceptance_19(count++, uri, "256",    "-");
            ListenerTest_Options.assertAceptance_19(count++, uri, "65535",  "-");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-0",     "-");    
            ListenerTest_Options.assertAceptance_19(count++, uri, "-1",     "-");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-256",   "-");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-65535", "-");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-",      "0");
    
            ListenerTest_Options.assertAceptance_19(count++, uri, "-",      "256");    
            ListenerTest_Options.assertAceptance_19(count++, uri, "-",      "65535");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-",      "-0");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-",      "-1");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-",      "-256");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-",      "-65535");
            ListenerTest_Options.assertAceptance_19(count++, uri, "0",      ";");
            ListenerTest_Options.assertAceptance_19(count++, uri, "1",      ";");    
            ListenerTest_Options.assertAceptance_19(count++, uri, "256",    ";");
            ListenerTest_Options.assertAceptance_19(count++, uri, "65535",  ";");
    
            ListenerTest_Options.assertAceptance_19(count++, uri, "-0",     ";");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-1",     ";");
            ListenerTest_Options.assertAceptance_19(count++, uri, "-256",   ";");    
            ListenerTest_Options.assertAceptance_19(count++, uri, "-65535", ";");
            ListenerTest_Options.assertAceptance_19(count++, uri, ";",      "0");
            ListenerTest_Options.assertAceptance_19(count++, uri, ";",      "1");
            ListenerTest_Options.assertAceptance_19(count++, uri, ";",      "256");
            ListenerTest_Options.assertAceptance_19(count++, uri, ";",      "65535");    
            ListenerTest_Options.assertAceptance_19(count++, uri, ";",      "-0");
            ListenerTest_Options.assertAceptance_19(count++, uri, ";",      "-1");
            
            ListenerTest_Options.assertAceptance_19(count++, uri, ";",      "-256");
            ListenerTest_Options.assertAceptance_19(count++, uri, ";",      "-65535");
            ListenerTest_Options.assertAceptance_19(count++, uri, "1",      "");
            ListenerTest_Options.assertAceptance_19(count++, uri, "",       "1");
        }
    }
    
    private static void assertAceptance_22(String request) throws Exception {
    
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_RANGE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));  
    }
    
    /** 
     *  TestCase for aceptance.
     *  For method {@code OPTIONS}, the target is not checked. Whether exits or
     *  not, the request is responded with status 200, {@code Allow} and
     *  without content details. {@code OPTIONS} is only a request about the
     *  supported HTTP methods. Header fields: {@code Range},
     *  {@code If-Modified-Since} and {@code If-UnModified-Since} are ignored. 
     *  @throws Exception
     */     
    @Test
    public void testAceptance_22() throws Exception {
        
        String request;
        String response;
        
        String[] uris = new String[] {"/partial_content.txt", "/partial_content_empty.txt", "/"};
        for (String uri : uris) {

            String range = "";
            if (uri.equals(uris[0]))
                range = "Range: bytes=2-10\r\n";
            if (uri.equals(uris[1]))
                range = "Range: bytes=0-0\r\n";    
        
            request = "HEAD " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "\r\n";
            response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));       
            String lastModified = TestHttpUtils.getResponseHeaderValue(response, HeaderField.LAST_MODIFIED);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n";
            ListenerTest_Options.assertAceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n";
            ListenerTest_Options.assertAceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n";
            ListenerTest_Options.assertAceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n";
            ListenerTest_Options.assertAceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-UnModified-Since: " + lastModified + "\r\n\r\n";
            ListenerTest_Options.assertAceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n"
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n";
            ListenerTest_Options.assertAceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-UnModified-Since: " + lastModified + "\r\n"
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n";
            ListenerTest_Options.assertAceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-UnModified-Since: " + lastModified + "\r\n"
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n";
            ListenerTest_Options.assertAceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n"
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n";
            ListenerTest_Options.assertAceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n";
            ListenerTest_Options.assertAceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n";
            ListenerTest_Options.assertAceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n";
            ListenerTest_Options.assertAceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: " + lastModified + "\r\n\r\n";
            ListenerTest_Options.assertAceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n"
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n";
            ListenerTest_Options.assertAceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: " + lastModified + "\r\n"
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n";
            ListenerTest_Options.assertAceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: " + lastModified + "\r\n"
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n";
            ListenerTest_Options.assertAceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n"
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n";
            ListenerTest_Options.assertAceptance_22(request);
        }
    }
}