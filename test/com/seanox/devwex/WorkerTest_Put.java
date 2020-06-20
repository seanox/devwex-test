/**
 * LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 * im Folgenden Seanox Software Solutions oder kurz Seanox genannt.
 * Diese Software unterliegt der Version 2 der GNU General Public License.
 *
 * Devwex, Advanced Server Development
 * Copyright (C) 2020 Seanox Software Solutions
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of version 2 of the GNU General Public License as published by the
 * Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.seanox.devwex;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.seanox.test.utils.Codec;
import com.seanox.test.utils.MockUtils;
import com.seanox.test.utils.Pattern;
import com.seanox.test.utils.StreamUtils;
import com.seanox.test.utils.Timing;

/**
 * Test cases for {@link com.seanox.devwex.Worker}.<br>
 * <br>
 * WorkerTest_Put 5.2 20200410<br>
 * Copyright (C) 2020 Seanox Software Solutions<br>
 * All rights reserved.
 *
 * @author  Seanox Software Solutions
 * @version 5.2.0 20200410
 */
public class WorkerTest_Put extends AbstractTest {
    
    /** 
     * Test case for acceptance.
     * The creation of directories is responded with status 201 and the location.
     * @throws Exception
     */     
    @Test
    public void testAcceptance_01() throws Exception {
        
        String request;
        String response;
        
        request = "Delete /put_test_1\\ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        this.sendRequest("127.0.0.1:8085", request);

        request = "Head /put_test_1\\ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_404));

        request = "Put /put_test_1\\ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_201));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8085/put_test_1/")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_201)); 
    } 
    
    /** 
     * Test case for acceptance.
     * The creation of directories without Content-length is responded with
     * status 201 and the location.
     * @throws Exception
     */      
    @Test
    public void testAcceptance_02() throws Exception {
        
        String request;
        String response;
        
        request = "Delete /put_test_2\\ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        this.sendRequest("127.0.0.1:8085", request);

        request = "Head /put_test_2\\ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_404));

        request = "Put /put_test_2 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_201));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8085/put_test_2")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_201)); 
    }
    
    /** 
     * Test case for acceptance.
     * The creation of directories with invalid characters in the name is
     * responded with status 424.
     * @throws Exception
     */      
    @Test
    public void testAcceptance_03() throws Exception {
        
        String request = "Put /put_test_2.:::2 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_424));
        
        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_424)); 
    }
    
    /** 
     * Test case for acceptance.
     * The creation of directories that alrady exists is responded with status 201
     * and the location.
     * @throws Exception
     */     
    @Test
    public void testAcceptance_04() throws Exception {
        
        String request;
        String response;
        
        request = "Head /put_test_2\\ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        
        request = "Put /put_test_1/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_201));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8085/put_test_1/")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_201)); 
    }
    
    /** 
     * Test case for acceptance.
     * The creation of directories without a slash at the end is responded with
     * status 201 and the location with a slash at the end.
     * @throws Exception
     */     
    @Test
    public void testAcceptance_05() throws Exception {
        
        String request= "Put /put_test_1 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_302));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8085/put_test_1/")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_302)); 
    }
    
    /** 
     * Test case for acceptance.
     * The creation of a directory with sub-directories is responded with status 201
     * and the location to the main directory.
     * @throws Exception
     */     
    @Test
    public void testAcceptance_06() throws Exception {
        
        String request = "Put /put_test_1/x1/x2/x3 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";

        String response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_201));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8085/put_test_1/x1/x2/x3")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_201)); 
    }
    
    /** 
     * Test case for acceptance.
     * The creation of a files is responded with status 201 and the location to
     * the file.
     * @throws Exception
     */     
    @Test
    public void testAcceptance_07() throws Exception {
        
        String request = "Put /put_test_1/test_file.1 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Length: 10\r\n"
                + "\r\n"
                + "1234567890";

        String response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_201));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8085/put_test_1/test_file.1")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_201)); 
    }
    
    /** 
     * Test case for acceptance.
     * The overwriting of an existing a files is responded with status 201 and
     * the location to the file.
     * @throws Exception
     */     
    @Test
    public void testAcceptance_08() throws Exception {
        
        String request;
        String response;
        
        request = "Head /put_test_1/test_file.1 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        
        request = "Put /put_test_1/test_file.1 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Length: 10\r\n"
                + "\r\n"
                + "1234567890";
        response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_201));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8085/put_test_1/test_file.1")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_201)); 
    }
    
    /** 
     * Test case for acceptance.
     * Overlength is ignored. Only as much data is written, as specified by
     * Content-Length.
     * @throws Exception
     */     
    @Test
    public void testAcceptance_09() throws Exception {
        
        String request;
        String response;
        
        request = "Put /put_test_1/test_file.2 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Length: 50\r\n"
                + "\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_201));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8085/put_test_1/test_file.2")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_201)); 

        request = "head /put_test_1/test_file.2 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH(50)));
    }
    
    /** 
     * Test case for acceptance.
     * Requests with an invalid Content-Length are responded with status 411.
     * @throws Exception
     */     
    @Test
    public void testAcceptance_10() throws Exception {
        
        String request;
        String response;
        
        request = "Put /put_test_1/test_file.3 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Length: x50\r\n"
                + "\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_411));

        request = "head /put_test_1/test_file.3 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_404));
    }
    
    /** 
     * Test case for acceptance.
     * Requests with an invalid Content-Length are responded with status 411.
     * @throws Exception
     */       
    @Test
    public void testAcceptance_11() throws Exception {
        
        String request;
        String response;
        
        request = "Put /put_test_1/test_file.3 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Length: -1\r\n"
                + "\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n"
                + "1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ--1234567890--ABCDEFGHIJKLMNOPQRSTUVWXYZ **\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_411));

        request = "head /put_test_1/test_file.3 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_404));
    }
    
    /** 
     * Test case for acceptance.
     * The creation of a file that already exists as a directory is responded
     * with status 302 and the location to the directory.
     * @throws Exception
     */     
    @Test
    public void testAcceptance_12() throws Exception {
        
        String request = "Put /put_test_1/x1 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Length: 10\r\n"
                + "\r\n"
                + "1234567890";
        String response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_302));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8085/put_test_1/x1/")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_302));
    }
    
    /** 
     * Test case for acceptance.
     * The creation of a directory that already exists as a file is responded
     * with status 302 and the location to the file.
     * @throws Exception
     */    
    @Test
    public void testAcceptance_13() throws Exception {
        
        String request = "Put /put_test_1/test_file.1/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_302));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8085/put_test_1/test_file.1")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_302));
    }
    
    /** 
     * Test case for acceptance.
     * PUT-requests to a CGI, are executed by the CGI application.
     * @throws Exception
     */     
    @Test
    public void testAcceptance_14() throws Exception {
        
        String request = "Put /method.jsx HTTP/1.0\r\n"
                + "Host: vHe\r\n"
                + "\r\n";
        String response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LOCATION_DIFFUSE));
        String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("hallo", body);
    }
    
    /** 
     * Test case for acceptance.
     * PUT-requests to a CGI, are executed by the CGI application.
     * The request is responded with status 405 if the PUT method not allowed
     * for the CGI.
     * @throws Exception
     */     
    @Test
    public void testAcceptance_15() throws Exception {
        
        String request = "Put /method.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_405));
    }
    
    /** 
     * Test case for acceptance.
     * PUT-requests to a module, are executed by the module (/test.module).
     * The path is absolute, therefore also /test.module123 is accepted.
     * @throws Exception
     */      
    @Test
    public void testAcceptance_17() throws Exception {
        
        String request = "Put /test.module123 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = this.sendRequest("127.0.0.1:8085", request);
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("001 Test ok")));
        Assert.assertTrue(response.matches("(?s)^.*\r\nModule: module.WorkerModule_A::Service\r\n.*$"));
        
        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("1")));        
    } 
    
    /** 
     * Test case for acceptance.
     * The path /test.xxx is absolute, therefore also /test.xxx123 is accepted.
     * @throws Exception
     */        
    @Test
    public void testAcceptance_18() throws Exception {
        
        String request;
        String response;
        
        request = "Delete /file.xxx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);

        request = "Head /file.xxx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_404));

        request = "Put /test.xxx123 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Length: 0\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_201));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8085/test.xxx123")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_201)); 
        
        request = "Get /test.xxx123 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));    
    }     
    
    /** 
     * Test case for acceptance.
     * PUT is supported for absolute path.
     * @throws Exception
     */      
    @Test
    public void testAcceptance_19() throws Exception {
        
        String request;
        String response;
        
        request = "Delete /file.xxx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Length: 0\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        
        request = "Head /file.xxx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_404));

        request = "Put /test.xxx HTTP/1.0\r\n"
            + "Host: vHa\r\n"
            + "Content-Length: 0\r\n"
            + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_201));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8085/test.xxx")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_201)); 

        request = "Get /test.xxx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
    } 
    
    /** 
     * Test case for acceptance.
     * PUT requests to a redirected url is responded with status 302 and the
     * location.
     * @throws Exception
     */      
    @Test
    public void testAcceptance_20() throws Exception {
        
        String request = "Put /redirect/a/b/c/file.xxx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Length: 0\r\n"
                + "\r\n";
        String response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_302));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://www.xXx.zzz/?a=2/a/b/c/file.xxx")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_302));
    } 
    
    /** 
     * Test case for acceptance.
     * PUT requests to a forbidden url is responded with status 403.
     * @throws Exception
     */      
    @Test
    public void testAcceptance_21() throws Exception {
        
        String request = "Put /forbidden/absolute-xxx.html HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Length: 0\r\n"
                + "\r\n";
        String response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_403));
    } 
    
    /** 
     * Test case for acceptance.
     * PUT requests in combination with an authentication must work.
     * In this case the authentication is not correct and the request is
     * responded with status 401.
     * @throws Exception
     */     
    @Test
    public void testAcceptance_22() throws Exception {
        
        String request = "Put /authentication/a/test.xxx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Length: 0\r\n"
                + "\r\n";
        String response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_401));
    } 
    
    /** 
     * Test case for acceptance.
     * PUT requests in combination with an authentication must work.
     * In this case the authentication is correct and the request is responded
     * with status 201.
     * @throws Exception
     */     
    @Test
    public void testAcceptance_23() throws Exception {
        
        String request;
        String response;
        
        request = "Delete /authentication/a/file.xxx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-a:pwd-a") + "\r\n"
                + "Content-Length: 0\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        
        request = "Head /authentication/a/file.xxx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-a:pwd-a") + "\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_404));
        
        request = "Put /authentication/a/file.xxx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-a:pwd-a") + "\r\n"
                + "Content-Length: 0\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_201));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8085/authentication/a/file.xxx")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("201", request, "usr-a")));

        request = "Get /authentication/a/file.xxx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Authorization: Basic " + Codec.encodeBase64("usr-a:pwd-a") + "\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
    } 
    
    /** 
     * Test case for acceptance.
     * For PUT requests, the number of bytes is written, which is specified with
     * Content-Length. If fewer bytes are present, the server waits until the
     * timeout (TIMEOUT = 15000). In this case the request is responded with status 424.
     * @throws Exception
     */      
    @Test
    public void testAcceptance_24() throws Exception {
        
        Timing timing = Timing.create(true);
        String request = "Put /put_test_1/test_file.3 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Length: 10\r\n"
                + "\r\n"
                + "12345";
        String response = this.sendRequest("127.0.0.1:8085", request);
        
        timing.assertTimeIn(16000);
        timing.assertTimeOut(15000);

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_424));
        
        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_424));
    } 
    
    /** 
     * Test case for acceptance.
     * For large PUT requests.
     * No server timeout may occur and the data must be written correctly.
     * In this case the request is responded with status 201.
     * @throws Exception
     */      
    @Test
    public void testAcceptance_25() throws Exception {
        
        String request;
        String response;
        
        request = "Delete /put_test_1/test_file.upload HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Length: 0\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
            
        long size = 3L *1000L *1000L *1000L;
        InputStream input = MockUtils.createInputStream(size);
        request = "Put /put_test_1/test_file.upload HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Length: " + size + "\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request, input);
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_201));

        File file = new File(AbstractSuite.getRootStage(), "/documents_vh_A/put_test_1/test_file.upload");
        Assert.assertEquals(size, file.length());
        Assert.assertEquals("---------E", new String(StreamUtils.tail(new FileInputStream(file), 10)));
        
        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_201));
    }     

    /** 
     * Test case for acceptance.
     * PUT-requests without a Content-Length creates a file.
     * @throws Exception
     */       
    @Test
    public void testAcceptance_98() throws Exception {
        
        String request;
        String response;
        
        request = "Put /put_test_1/test_file.98 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_201));

        request = "head /put_test_1/test_file.98/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LOCATION_DIFFUSE));
    }    
    
    /** 
     * Test case for acceptance.
     * PUT-requests with a Content-Length creates a file.
     * @throws Exception
     */       
    @Test
    public void testAcceptance_99() throws Exception {
        
        String request;
        String response;
        
        request = "Put /put_test_1/test_file.99 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Length: 0\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_201));

        request = "head /put_test_1/test_file.99 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = this.sendRequest("127.0.0.1:8085", request);
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH(0)));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LOCATION_DIFFUSE));
    }
}