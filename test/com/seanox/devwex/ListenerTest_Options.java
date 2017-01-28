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
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nAllow: AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"OPTIONS [^\"]+\"\\s200\\s\\d+\\s-\\s-$"));      
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
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nAllow: AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"OPTIONS [^\"]+\"\\s200\\s\\d+\\s-\\s-$"));      
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
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nAllow: AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"OPTIONS [^\"]+\"\\s200\\s\\d+\\s-\\s-$"));      
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
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nAllow: AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"OPTIONS [^\"]+\"\\s200\\s\\d+\\s-\\s-$"));      
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
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nAllow: AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"OPTIONS [^\"]+\"\\s200\\s\\d+\\s-\\s-$"));      
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
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));       
        String lastModified = TestHttpUtils.getResponseHeaderValue(response, HeaderField.LAST_MODIFIED);
        
        request = "OPTIONS /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: " + lastModified + "\r\n"
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));   
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nAllow: AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"OPTIONS [^\"]+\"\\s200\\s\\d+\\s-\\s-$"));    
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
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));   
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nAllow: AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"OPTIONS [^\"]+\"\\s200\\s\\d+\\s-\\s-$"));     
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
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));       
        String lastModified = TestHttpUtils.getResponseHeaderValue(response, HeaderField.LAST_MODIFIED);
        String contentLength = TestHttpUtils.getResponseHeaderValue(response, HeaderField.CONTENT_LENGTH);
        
        
        request = "OPTIONS /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: " + lastModified + "; xxx; length=" + contentLength + "\r\n"
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));   
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nAllow: AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"OPTIONS [^\"]+\"\\s200\\s\\d+\\s-\\s-$"));    
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
        
        String request = "OPTIONS /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: Sat, 01 Jan 2000 00:00:00 GMT; xxx; length=123\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));   
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nAllow: AAA, BBB, XXX, GET, POST, XPOST, CCCC, HEAD, DELETE, PUT, OPTIONS\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"OPTIONS [^\"]+\"\\s200\\s\\d+\\s-\\s-$"));     
    } 
}