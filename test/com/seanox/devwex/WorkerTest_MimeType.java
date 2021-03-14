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

import org.junit.Assert;
import org.junit.Test;

import com.seanox.test.utils.HttpUtils;
import com.seanox.test.utils.Pattern;

/**
 * Test cases for {@link com.seanox.devwex.Worker}.<br>
 * <br>
 * WorkerTest_MimeType 5.1.0 20171231<br>
 * Copyright (C) 2017 Seanox Software Solutions<br>
 * All rights reserved.
 *
 * @author  Seanox Software Solutions
 * @version 5.1.0 20171231
 */
public class WorkerTest_MimeType extends AbstractTest {
    
    /** 
     * Test case for acceptance.
     * For the file extension {@code xxx} was not defined a mimetype.
     * The request must be responded with status 200 and the standard mimetype.
     * @throws Exception
     */
    @Test
    public void testAcceptance_1() throws Exception {
        
        String request = "GET /mimetype_test.xxx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:18080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_OCTET_STREAM));
    }
    
    /** 
     * Test case for acceptance.
     * For the file extension {@code xls} was defined a mimetype.
     * The request must be responded with status 200 and the defined a mimetype.
     * @throws Exception
     */
    @Test
    public void testAcceptance_2() throws Exception {
        
        String request = "GET /mimetype_test.xls HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:18080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_APPLICATION_VND_MS_EXCEL));
    }    
    
    /** 
     * Test case for acceptance.
     * For the file extension {@code xls} was defined a mimetype.
     * The request with {@code Accept: *}{@code /*} must be responded with
     * status 200 and the defined a mimetype.
     * @throws Exception
     */
    @Test
    public void testAcceptance_3() throws Exception {
        
        String request = "GET /mimetype_test.xls HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Accept: */*\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:18080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_APPLICATION_VND_MS_EXCEL));
    }  
    
    /** 
     * Test case for acceptance.
     * For the file extension {@code xls} was defined a mimetype.
     * The request with {@code Accept: application/*} must be responded with
     * status 200 and the defined a mimetype.
     * @throws Exception
     */
    @Test
    public void testAcceptance_4() throws Exception {
        
        String request = "GET /mimetype_test.xls HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Accept: application/*\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:18080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_APPLICATION_VND_MS_EXCEL));
    }
    
    /** 
     * Test case for acceptance.
     * For the file extension {@code xls} was defined a mimetype.
     * The request with {@code Accept: *}{@code /vnd.ms-excel} must be responded
     * with status 200 and the defined a mimetype.
     * @throws Exception
     */
    @Test
    public void testAcceptance_5() throws Exception {
        
        String request = "GET /mimetype_test.xls HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Accept: */vnd.ms-excel\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:18080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_APPLICATION_VND_MS_EXCEL));
    } 
    
    /** 
     * Test case for acceptance.
     * For the file extension {@code xxx} was not defined a mimetype.
     * The request must be responded with status 200 and the standard mimetype.
     * @throws Exception
     */
    @Test
    public void testAcceptance_6() throws Exception {
        
        String request = "GET /mimetype_test.xxx HTTP/1.0\r\n"
                + "Host: vHi\r\n"
                + "Accept: */*\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:18080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_OCTET_STREAM));
    }  
    
    /** 
     * Test case for acceptance.
     * Accept and the content-type the server has determined do not match.
     * The request must be responded with status 406.
     * @throws Exception
     */
    @Test
    public void testAcceptance_7() throws Exception {
        
        String request = "GET /mimetype_test.xxx HTTP/1.0\r\n"
                + "Host: vHi\r\n"
                + "Accept: no/*\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:18080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_406));
    }
    
    /** 
     * Test case for acceptance.
     * Accept and the content-type the server has determined do not match.
     * The request must be responded with status 406.
     * @throws Exception
     */
    @Test
    public void testAcceptance_8() throws Exception {
        
        String request = "GET /mimetype_test.xxx HTTP/1.0\r\n"
                + "Host: vHi\r\n"
                + "Accept: */no\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:18080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_406));
    }  
    
    /** 
     * Test case for acceptance.
     * For the file extension {@code xxx} was not defined a mimetype.
     * The request must be responded with status 200 and the standard mimetype.
     * Accept is blank and is ignored.
     * @throws Exception
     */
    @Test
    public void testAcceptance_9() throws Exception {
        
        String request = "GET /mimetype_test.xxx HTTP/1.0\r\n"
                + "Host: vHi\r\n"
                + "Accept:     \r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:18080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_OCTET_STREAM));
    } 
}