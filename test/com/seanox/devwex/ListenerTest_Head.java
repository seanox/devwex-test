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
public class ListenerTest_Head extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  Directories are responded with status 200 and without Content-Length.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_01() throws Exception {
        
        String request = "HEAD / HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Directories without a slah at the end, are responded with status 302
     *  and a redirect.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_02() throws Exception {
        
        String request = "HEAD /test_a HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
    
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_302));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8080/test_a/")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
    
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_302));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Directories these not exists, are responded with status 404.
     *  @throws Exception
     */   
    @Test
    public void testAceptance_03() throws Exception {
        
        String request = "HEAD /test_ax HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
    
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_404));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Files are responded with status 200, Content-Length, Content-Type and
     *  LastModified.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_04() throws Exception {
        
        String request = "HEAD /method_file.txt HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Files with a slah at the end, are responded with status 302 and a
     *  redirect.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_05() throws Exception {
        
        String request = "HEAD /method_file.txt/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_302));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8080/method_file.txt")));

        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_302));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Requests with a correct If-Modified-Since are responded with status
     *  304.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_06() throws Exception {
        
        String request;
        String response;        
        
        request = "Get /documents/commons/lastmodified.jsx HTTP/1.0\r\n"
                + "File: ../../documents_vh_A/method_file.txt\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        String lastModified = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        
        request = "HEAD /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: " + lastModified + " GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_304));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LOCATION_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_304));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Requests with a incorrect If-Modified-Since are responded with status
     *  200.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_07() throws Exception {
        
        String request = "HEAD /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: Mon, 19 Jan 2004 16:58:55 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Requests with a correct If-Modified-Since (incl. length) are responded
     *  with status 304.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_08() throws Exception {

        String request;
        String response;        
        
        request = "Get /documents/commons/lastmodified.jsx HTTP/1.0\r\n"
                + "File: ../../documents_vh_A/method_file.txt\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        String lastModified = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");

        request = "HEAD /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: " + lastModified + " GMT; xxx; length=15\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_304));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LOCATION_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_304));        
    }
    
    /** 
     *  TestCase for aceptance.
     *  Requests with a If-Modified-Since (correct date, invalid length) are
     *  responded with status 200.
     *  @throws Exception
     */   
    @Test
    public void testAceptance_09() throws Exception {

        String request;
        String response;        
        
        request = "Get /documents/commons/lastmodified.jsx HTTP/1.0\r\n"
                + "File: ../../documents_vh_A/method_file.txt\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        String lastModified = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");

        request = "HEAD /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: " + lastModified + " GMT; xxx; length=20\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));    
    }
}