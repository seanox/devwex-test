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
public class ListenerTest_Put extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  The creation of directories is responded with status 201 and the
     *  location.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_01() throws Exception {
        
        String request;
        String response;
        
        request = "Delete /put_test_1\\ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        TestHttpUtils.sendRequest("127.0.0.1:8085", request);

        request = "Head /put_test_1\\ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8085", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_404));

        request = "Put /put_test_1\\ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8085", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_201));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8085/put_test_1/")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_201)); 
    } 
    
    /** 
     *  TestCase for aceptance.
     *  The creation of directories without Content-length is responded with
     *  status 201 and the location.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_02() throws Exception {
        
        String request;
        String response;
        
        request = "Delete /put_test_2\\ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        TestHttpUtils.sendRequest("127.0.0.1:8085", request);

        request = "Head /put_test_2\\ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8085", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_404));

        request = "Put /put_test_2 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8085", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_201));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8085/put_test_2")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_201)); 
    }
    
    /** 
     *  TestCase for aceptance.
     *  The creation of directories with invalid characters in the name is
     *  responded with status 424.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_03() throws Exception {
        
        String request = "Put /put_test_2.:::2 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8085", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_424));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_424)); 
    }
    
    /** 
     *  TestCase for aceptance.
     *  The creation of directories that alrady exists is responded with status
     *  201 and the location.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_04() throws Exception {
        
        String request;
        String response;
        
        request = "Head /put_test_2\\ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8085", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        
        request = "Put /put_test_1/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8085", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_201));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8085/put_test_1/")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_201)); 
    }
    
    /** 
     *  TestCase for aceptance.
     *  The creation of directories without a slash at the end is responded with status
     *  201 and the location with a slash at the end.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_05() throws Exception {
        
        String request= "Put /put_test_1 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8085", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_302));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8085/put_test_1/")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_302)); 
    }
}