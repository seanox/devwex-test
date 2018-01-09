/**
 *  LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 *  im Folgenden Seanox Software Solutions oder kurz Seanox genannt.
 *  Diese Software unterliegt der Version 2 der GNU General Public License.
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

import com.seanox.test.utils.HttpUtils;
import com.seanox.test.utils.HttpUtils.HeaderField;
import com.seanox.test.utils.OutputFacadeStream;
import com.seanox.test.utils.Pattern;

/**
 *  Test cases for {@link com.seanox.devwex.Worker}.<br>
 *  <br>
 *  WorkerTest_Options 5.1 20171231<br>
 *  Copyright (C) 2017 Seanox Software Solutions<br>
 *  All rights reserved.
 *
 *  @author  Seanox Software Solutions
 *  @version 5.1 20171231
 */
public class WorkerTest_Options extends AbstractTest {
    
    /** 
     *  Test case for acceptance.
     *  For method {@code OPTIONS}, The target is not checked. Whether exits or
     *  not, the request is responded with status 200, {@code Allow} and
     *  without content details. {@code OPTIONS} is only a request about the
     *  supported HTTP methods.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_01() throws Exception {

        String request = "OPTIONS / HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));      
    } 
    
    /** 
     *  Test case for acceptance.
     *  For method {@code OPTIONS}, The target is not checked. Whether exits or
     *  not, the request is responded with status 200, {@code Allow} and
     *  without content details. {@code OPTIONS} is only a request about the
     *  supported HTTP methods.
     *  @throws Exception
     */ 
    @Test
    public void testAcceptance_02() throws Exception {

        String request = "OPTIONS /test_a HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));      
    } 
    
    /** 
     *  Test case for acceptance.
     *  For method {@code OPTIONS}, The target is not checked. Whether exits or
     *  not, the request is responded with status 200, {@code Allow} and
     *  without content details. {@code OPTIONS} is only a request about the
     *  supported HTTP methods.
     *  @throws Exception
     */ 
    @Test
    public void testAcceptance_03() throws Exception {

        String request = "OPTIONS /test_ax HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));      
    } 

    /** 
     *  Test case for acceptance.
     *  For method {@code OPTIONS}, The target is not checked. Whether exits or
     *  not, the request is responded with status 200, {@code Allow} and
     *  without content details. {@code OPTIONS} is only a request about the
     *  supported HTTP methods.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_04() throws Exception {

        String request = "OPTIONS /method_file.txt HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));      
    }
    
    /** 
     *  Test case for acceptance.
     *  For method {@code OPTIONS}, The target is not checked. Whether exits or
     *  not, the request is responded with status 200, {@code Allow} and
     *  without content details. {@code OPTIONS} is only a request about the
     *  supported HTTP methods.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_05() throws Exception {

        String request = "OPTIONS /method_file.txt/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));      
    } 
    
    /** 
     *  Test case for acceptance.
     *  For method {@code OPTIONS}, the request header
     *  {@code If-Modified-Since} is ignored. Whether correct or invalid, the
     *  request is responded with status 200, {@code Allow} and without content
     *  details.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_06() throws Exception {

        String request;
        String response;
        
        request = "HEAD /method_file.txt HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));       
        String lastModified = HttpUtils.getResponseHeaderValue(response, HeaderField.LAST_MODIFIED);
        
        request = "OPTIONS /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: " + lastModified + "\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCaptureTail();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));    
    } 
    
    /** 
     *  Test case for acceptance.
     *  For method {@code OPTIONS}, the request header
     *  {@code If-Modified-Since} is ignored. Whether correct or invalid, the
     *  request is responded with status 200, {@code Allow} and without content
     *  details.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_07() throws Exception {
        
        String request = "OPTIONS /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: Sat, 01 Jan 2000 00:00:00 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));     
    } 
    
    /** 
     *  Test case for acceptance.
     *  For method {@code OPTIONS}, the request header
     *  {@code If-Modified-Since} is ignored. Whether correct or invalid, the
     *  request is responded with status 200, {@code Allow} and without content
     *  details.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_08() throws Exception {

        String request;
        String response;
        
        request = "HEAD /method_file.txt HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));       
        String lastModified = HttpUtils.getResponseHeaderValue(response, HeaderField.LAST_MODIFIED);
        String contentLength = HttpUtils.getResponseHeaderValue(response, HeaderField.CONTENT_LENGTH);
        
        request = "OPTIONS /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: " + lastModified + "; xxx; length=" + contentLength + "\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCaptureTail();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));    
    } 
    
    /** 
     *  Test case for acceptance.
     *  For method {@code OPTIONS}, the request header
     *  {@code If-Modified-Since} is ignored. Whether correct or invalid, the
     *  request is responded with status 200, {@code Allow} and without content
     *  details.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_09() throws Exception {
        
        String request;
        String response;
        
        request = "HEAD /method_file.txt HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));       
        String lastModified = HttpUtils.getResponseHeaderValue(response, HeaderField.LAST_MODIFIED);
        String contentLength = HttpUtils.getResponseHeaderValue(response, HeaderField.CONTENT_LENGTH);
        
        request = "OPTIONS /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: " + lastModified + "; xxx; length=1" + contentLength + "\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));    
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCaptureTail();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));     
    }
    
    /** 
     *  Test case for acceptance.
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
    public void testAcceptance_11() throws Exception {
        
        String request = "OPTIONS /test_a/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));     
    } 
    
    /** 
     *  Test case for acceptance.
     *  For method {@code OPTIONS}, the target (also directories) is not
     *  checked. Whether exits or not, the request is responded with status
     *  200, {@code Allow} and without content details. {@code OPTIONS} is only
     *  a request about the supported HTTP methods. Header fields:
     *  {@code Range}, {@code If-Modified-Since} and {@code If-UnModified-Since}
     *  are ignored. 
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_12() throws Exception {
        
        String request = "OPTIONS /test_d/ HTTP/1.0\r\n"
                + "If-Modified-Since: Mon, 11 Jan 2004 19:11:58 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));     
    } 
    
    /** 
     *  Test case for acceptance.
     *  For method {@code OPTIONS}, the request for a forbidden target is
     *  responded with status 403. 
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_13() throws Exception {
        
        String request = "OPTIONS /forbidden HTTP/1.0\r\n"
                + "If-Modified-Since: Mon, 11 Jan 2004 19:11:58 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_403));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_403));     
    } 
    
    /** 
     *  Test case for acceptance.
     *  For method {@code OPTIONS}, the target (also with an absolute paths) is
     *  not checked. Whether exits or not, the request is responded with status
     *  200, {@code Allow} and without content details. {@code OPTIONS} is only
     *  a request about the supported HTTP methods. 
     *  @throws Exception
     */ 
    @Test
    public void testAcceptance_14() throws Exception {
        
        String request = "OPTIONS /absolute HTTP/1.0\r\n"
                + "If-Modified-Since: Mon, 11 Jan 2004 19:11:58 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));      
    } 
    
    /** 
     *  Test case for acceptance.
     *  For method {@code OPTIONS}, the target (also with an absolute paths) is
     *  not checked. Whether exits or not, the request is responded with status
     *  200, {@code Allow} and without content details. {@code OPTIONS} is only
     *  a request about the supported HTTP methods. 
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_15() throws Exception {
        
        String request = "OPTIONS /absolutexxx HTTP/1.0\r\n"
                + "If-Modified-Since: Mon, 11 Jan 2004 19:11:58 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));      
    } 
    
    /**
     *  Test case for acceptance.
     *  For the CGI, the method {@code OPTIONS} are responded by the CGI.
     *  In the test case, {@code OPTIONS} is for the CGI not allowed and the
     *  request is responded with status 403, also if the CGI not exists.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_16() throws Exception {
        
        String request = "OPTIONS /method.php HTTP/1.0\r\n"
                + "If-Modified-Since: Mon, 11 Jan 2004 19:11:58 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_403));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_403));     
    } 
    
    /**
     *  Test case for acceptance.
     *  For the CGI, the method {@code OPTIONS} are responded by the CGI.
     *  In the test case, {@code OPTIONS} is for the CGI allowed and the
     *  request is responded with status 200.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_17() throws Exception {
        
        String request = "oPTIONS /method.jsx HTTP/1.0\r\n"
                + "Host: vHb\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_ALLOW_DIFFUSE));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.matches("(?si)^.*hallo.*$"));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));     
    }
    
    /**
     *  Test case for acceptance.
     *  For the CGI, the method {@code OPTIONS} are responded by the CGI.
     *  In the test case, {@code OPTIONS} is for the CGI not allowed and the
     *  request is responded with status 403.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_18() throws Exception {
        
        String request = "oPTIONS /method.jsx HTTP/1.0\r\n"
                + "Host: vHe\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_403));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_403));    
    }
    
    private static void assertAcceptance_19(int count, String path, String start, String end) throws Exception {
        
        try (OutputFacadeStream.Capture capture = AbstractSuite.accessStream.capture()) {
                    
            if (start != null
                    && start.contains("-")
                    && end == null)
                end = "";
            if (end != null
                    && end.contains("-")
                    && start == null)
                start = "";
    
            String request = "Options " + path + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n";
            if (start != null || end != null) {
                request += "Range: bYteS = " + (start != null ? start : "");
                if (start != null && end != null)
                    request += count % 2 == 0 ? "-" : " - ";
                request += end != null ? end : "";
                request += "\r\n";
            }
            
            if (start != null && start.contains(";"))
                end = null;
            
            String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request + "\r\n"));
            
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
            Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
            Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_RANGE_DIFFUSE));
            Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
            Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
           
            Thread.sleep(AbstractTest.SLEEP);
            String accessLog = capture.toString().trim();
            Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));
        }
    }
    
    /** 
     *  Test case for acceptance.
     *  For method {@code OPTIONS}, the target is not checked. Whether exits or
     *  not, the request is responded with status 200, {@code Allow} and
     *  without content details. {@code OPTIONS} is only a request about the
     *  supported HTTP methods. Header fields: {@code Range},
     *  {@code If-Modified-Since} and {@code If-UnModified-Since} are ignored. 
     *  @throws Exception
     */
    @Test
    public void testAcceptance_19() throws Exception {

        for (String path : new String[] {"/partial_content.txt", "/partial_content_empty.txt",
                "/partial_content-nix.txt", "/"}) {

            int count = 0;
        
            WorkerTest_Options.assertAcceptance_19(++count, path, "0",      "0");
            WorkerTest_Options.assertAcceptance_19(++count, path, "0",      "1");    
            WorkerTest_Options.assertAcceptance_19(++count, path, "0",      "127");
            WorkerTest_Options.assertAcceptance_19(++count, path, "0",      "65535");
            WorkerTest_Options.assertAcceptance_19(++count, path, "1",      "0");
            WorkerTest_Options.assertAcceptance_19(++count, path, "1",      "1");
            WorkerTest_Options.assertAcceptance_19(++count, path, "1",      "127");    
            WorkerTest_Options.assertAcceptance_19(++count, path, "1",      "65535");
            WorkerTest_Options.assertAcceptance_19(++count, path, "127",    "256");
            WorkerTest_Options.assertAcceptance_19(++count, path, "256",    "127");
    
            WorkerTest_Options.assertAcceptance_19(++count, path, "127",    "0");
            WorkerTest_Options.assertAcceptance_19(++count, path, "127",    "1");
            WorkerTest_Options.assertAcceptance_19(++count, path, "65535",  "0");
            WorkerTest_Options.assertAcceptance_19(++count, path, "65535",  "1");
            WorkerTest_Options.assertAcceptance_19(++count, path, "256",    "65535");
            WorkerTest_Options.assertAcceptance_19(++count, path, "65535",  "256");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-256",   "127");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-127",   "256");
            WorkerTest_Options.assertAcceptance_19(++count, path, "256",    "-127");
            WorkerTest_Options.assertAcceptance_19(++count, path, "127",    "-256");
    
            WorkerTest_Options.assertAcceptance_19(++count, path, "0",      "A");
            WorkerTest_Options.assertAcceptance_19(++count, path, "1",      "A");
            WorkerTest_Options.assertAcceptance_19(++count, path, "256",    "B");
            WorkerTest_Options.assertAcceptance_19(++count, path, "65535",  "C");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-0",     "A");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-1",     "A");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-256",   "B");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-65535", "C");
            WorkerTest_Options.assertAcceptance_19(++count, path, "A",      "0");
            WorkerTest_Options.assertAcceptance_19(++count, path, "A",      "1");
    
            WorkerTest_Options.assertAcceptance_19(++count, path, "B",      "256");
            WorkerTest_Options.assertAcceptance_19(++count, path, "C",      "65535");
            WorkerTest_Options.assertAcceptance_19(++count, path, "A",      "-0");
            WorkerTest_Options.assertAcceptance_19(++count, path, "A",      "-1");
            WorkerTest_Options.assertAcceptance_19(++count, path, "B",      "-256");
            WorkerTest_Options.assertAcceptance_19(++count, path, "C",      "-65535");
            WorkerTest_Options.assertAcceptance_19(++count, path, "0",      "");
            WorkerTest_Options.assertAcceptance_19(++count, path, "256",    "");
            WorkerTest_Options.assertAcceptance_19(++count, path, "65535",  "");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-0",     "");
    
            WorkerTest_Options.assertAcceptance_19(++count, path, "-1",     "");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-256",   "");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-65535", "");
            WorkerTest_Options.assertAcceptance_19(++count, path, null,     "0");
            WorkerTest_Options.assertAcceptance_19(++count, path, null,     "256");
            WorkerTest_Options.assertAcceptance_19(++count, path, null,     "65535");
            WorkerTest_Options.assertAcceptance_19(++count, path, null,     "A");
            WorkerTest_Options.assertAcceptance_19(++count, path, null,     null);
            WorkerTest_Options.assertAcceptance_19(++count, path, "",       "0");
            WorkerTest_Options.assertAcceptance_19(++count, path, "",       "256");
           
            WorkerTest_Options.assertAcceptance_19(++count, path, "",       "65535");
            WorkerTest_Options.assertAcceptance_19(++count, path, "",       "-0");
            WorkerTest_Options.assertAcceptance_19(++count, path, "",       "-1");
            WorkerTest_Options.assertAcceptance_19(++count, path, "",       "-256");
            WorkerTest_Options.assertAcceptance_19(++count, path, "",       "-65535");
            WorkerTest_Options.assertAcceptance_19(++count, path, "0",      " ");
            WorkerTest_Options.assertAcceptance_19(++count, path, "1",      " ");
            WorkerTest_Options.assertAcceptance_19(++count, path, "256",    " ");
            WorkerTest_Options.assertAcceptance_19(++count, path, "65535",  " ");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-0",     " ");
            
            WorkerTest_Options.assertAcceptance_19(++count, path, null,     "-0");
            WorkerTest_Options.assertAcceptance_19(++count, path, null,     "-1");
            WorkerTest_Options.assertAcceptance_19(++count, path, null,     "-256");
            WorkerTest_Options.assertAcceptance_19(++count, path, null,     "-65535");
            WorkerTest_Options.assertAcceptance_19(++count, path, "0",      null);
            WorkerTest_Options.assertAcceptance_19(++count, path, "1",      null);
            WorkerTest_Options.assertAcceptance_19(++count, path, "256",    null);
            WorkerTest_Options.assertAcceptance_19(++count, path, "65535",  null);
            WorkerTest_Options.assertAcceptance_19(++count, path, "-0",     null);  
            WorkerTest_Options.assertAcceptance_19(++count, path, null,     "65535");
            WorkerTest_Options.assertAcceptance_19(++count, path, null,     "256");
            WorkerTest_Options.assertAcceptance_19(++count, path, null,     "127");            
            
            WorkerTest_Options.assertAcceptance_19(++count, path, "-1",     " ");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-256",   " ");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-65535", " ");
            WorkerTest_Options.assertAcceptance_19(++count, path, " ",      "0");
            WorkerTest_Options.assertAcceptance_19(++count, path, " ",      "1");
            WorkerTest_Options.assertAcceptance_19(++count, path, " ",      "256");
            WorkerTest_Options.assertAcceptance_19(++count, path, " ",      "65535");
            WorkerTest_Options.assertAcceptance_19(++count, path, " ",      "-0");
            WorkerTest_Options.assertAcceptance_19(++count, path, " ",      "-1");
            WorkerTest_Options.assertAcceptance_19(++count, path, " ",      "-256");
            
            WorkerTest_Options.assertAcceptance_19(++count, path, " ",      "-65535");
            WorkerTest_Options.assertAcceptance_19(++count, path, "0",      "-");
            WorkerTest_Options.assertAcceptance_19(++count, path, "1",      "-");
            WorkerTest_Options.assertAcceptance_19(++count, path, "256",    "-");
            WorkerTest_Options.assertAcceptance_19(++count, path, "65535",  "-");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-0",     "-");    
            WorkerTest_Options.assertAcceptance_19(++count, path, "-1",     "-");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-256",   "-");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-65535", "-");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-",      "0");
    
            WorkerTest_Options.assertAcceptance_19(++count, path, "-",      "256");    
            WorkerTest_Options.assertAcceptance_19(++count, path, "-",      "65535");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-",      "-0");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-",      "-1");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-",      "-256");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-",      "-65535");
            WorkerTest_Options.assertAcceptance_19(++count, path, "0",      ";");
            WorkerTest_Options.assertAcceptance_19(++count, path, "1",      ";");    
            WorkerTest_Options.assertAcceptance_19(++count, path, "256",    ";");
            WorkerTest_Options.assertAcceptance_19(++count, path, "65535",  ";");
            WorkerTest_Options.assertAcceptance_19(++count, path, "0;",     null);
            WorkerTest_Options.assertAcceptance_19(++count, path, "1;",     null);    
            WorkerTest_Options.assertAcceptance_19(++count, path, "256;",   null);
            WorkerTest_Options.assertAcceptance_19(++count, path, "65535;", null);            
    
            WorkerTest_Options.assertAcceptance_19(++count, path, "-0",     ";");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-1",     ";");
            WorkerTest_Options.assertAcceptance_19(++count, path, "-256",   ";");    
            WorkerTest_Options.assertAcceptance_19(++count, path, "-65535", ";");
            WorkerTest_Options.assertAcceptance_19(++count, path, ";",      "0");
            WorkerTest_Options.assertAcceptance_19(++count, path, ";",      "1");
            WorkerTest_Options.assertAcceptance_19(++count, path, ";",      "256");
            WorkerTest_Options.assertAcceptance_19(++count, path, ";",      "65535");    
            WorkerTest_Options.assertAcceptance_19(++count, path, ";",      "-0");
            WorkerTest_Options.assertAcceptance_19(++count, path, ";",      "-1");
            
            WorkerTest_Options.assertAcceptance_19(++count, path, ";",      "-256");
            WorkerTest_Options.assertAcceptance_19(++count, path, ";",      "-65535");
            WorkerTest_Options.assertAcceptance_19(++count, path, "1",      "");
            WorkerTest_Options.assertAcceptance_19(++count, path, "",       "1");
        }
    }
    
    private static void assertAcceptance_22(String request) throws Exception {
        
        try (OutputFacadeStream.Capture capture = AbstractSuite.accessStream.capture()) {
            
            String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
            
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
            Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
            Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_RANGE_DIFFUSE));
            Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
            Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW(("AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS").split(",\\s+"))));
            
            Thread.sleep(AbstractTest.SLEEP);
            String accessLog = capture.toString().trim();
            Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 0)));  
        }
    }
    
    /** 
     *  Test case for acceptance.
     *  For method {@code OPTIONS}, the target is not checked. Whether exits or
     *  not, the request is responded with status 200, {@code Allow} and
     *  without content details. {@code OPTIONS} is only a request about the
     *  supported HTTP methods. Header fields: {@code Range},
     *  {@code If-Modified-Since} and {@code If-UnModified-Since} are ignored. 
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_22() throws Exception {
        
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
            response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));       
            String lastModified = HttpUtils.getResponseHeaderValue(response, HeaderField.LAST_MODIFIED);
            
            Thread.sleep(AbstractTest.SLEEP);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n";
            WorkerTest_Options.assertAcceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n";
            WorkerTest_Options.assertAcceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n";
            WorkerTest_Options.assertAcceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n";
            WorkerTest_Options.assertAcceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-UnModified-Since: " + lastModified + "\r\n\r\n";
            WorkerTest_Options.assertAcceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n"
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n";
            WorkerTest_Options.assertAcceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-UnModified-Since: " + lastModified + "\r\n"
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n";
            WorkerTest_Options.assertAcceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-UnModified-Since: " + lastModified + "\r\n"
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n";
            WorkerTest_Options.assertAcceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + range
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n"
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n";
            WorkerTest_Options.assertAcceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n";
            WorkerTest_Options.assertAcceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n";
            WorkerTest_Options.assertAcceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n";
            WorkerTest_Options.assertAcceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: " + lastModified + "\r\n\r\n";
            WorkerTest_Options.assertAcceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n"
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n";
            WorkerTest_Options.assertAcceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: " + lastModified + "\r\n"
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n";
            WorkerTest_Options.assertAcceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: " + lastModified + "\r\n"
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n";
            WorkerTest_Options.assertAcceptance_22(request);
    
            request = "OPTIONS " + uri + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n"
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n";
            WorkerTest_Options.assertAcceptance_22(request);
        }
    }
}