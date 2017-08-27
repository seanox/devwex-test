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

import com.seanox.test.utils.HttpUtils;
import com.seanox.test.utils.Pattern;

/**
 *  TestCases for {@link com.seanox.devwex.Worker}.
 */
public class WorkerTest_FileIndex extends AbstractTest {
    
    /** 
     *  TestCase for acceptance.
     *  Without sorting, the file index of directories must not contain '?'.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_1() throws Exception {
        
        String request = "GET / HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8081", request));

        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(header.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2") + "\r\n";
        Assert.assertTrue(body.contains("\r\nindex of: \r\n"));
        Assert.assertTrue(body.contains("\r\norder by: na\r\n"));
        Assert.assertFalse(body.contains("?"));
        
        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));  
    }
    
    /** 
     *  TestCase for acceptance.
     *  With sorting, the file index of directories must not contain '?'.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_2() throws Exception {
        
        String request = "GET /?d HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8081", request));

        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(header.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2") + "\r\n";
        Assert.assertTrue(body.contains("\r\nindex of: \r\n"));
        Assert.assertTrue(body.contains("\r\norder by: da\r\n"));
        Assert.assertFalse(body.contains("?"));
        
        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));  
    }
    
    /** 
     *  TestCase for acceptance.
     *  The path from subdirectories must be created correctly.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_3() throws Exception {
        
        String request = "GET /test_a/test/ HTTP/1.0\r\n"
                + "Host: vHb\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8081", request));

        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(header.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2") + "\r\n";
        Assert.assertTrue(body.contains("\r\nindex of: /test_a/test\r\n"));
        Assert.assertTrue(body.contains("\r\norder by: na\r\n"));
        Assert.assertFalse(body.contains("?"));
        
        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));  
    }
}