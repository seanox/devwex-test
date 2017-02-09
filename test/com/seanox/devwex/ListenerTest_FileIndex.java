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
public class ListenerTest_FileIndex extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  Without sorting, the file index of directories must not contain '?'.
     *  @throws Exception
     */
    @Test
    public void testAceptance_1() throws Exception {
        
        String request = "GET / HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8081", request));

        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertTrue(header.matches("(?si)^.*\r\nContent-Type: \\w+.*$"));
        Assert.assertTrue(header.matches("(?si)^.*\r\nContent-Length: \\d+.*$"));
        Assert.assertFalse(header.matches("(?si)^.*\\sLast-Modified:.*$"));
        
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2") + "\r\n";
        Assert.assertTrue(body.contains("\r\nindex of: \r\n"));
        Assert.assertTrue(body.contains("\r\norder by: na\r\n"));
        Assert.assertFalse(body.contains("?"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));  
    }
    
    /** 
     *  TestCase for aceptance.
     *  With sorting, the file index of directories must not contain '?'.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_2() throws Exception {
        
        String request = "GET /?d HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8081", request));

        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertTrue(header.matches("(?si)^.*\r\nContent-Type: \\w+.*$"));
        Assert.assertTrue(header.matches("(?si)^.*\r\nContent-Length: \\d+.*$"));
        Assert.assertFalse(header.matches("(?si)^.*\\sLast-Modified:.*$"));
        
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2") + "\r\n";
        Assert.assertTrue(body.contains("\r\nindex of: \r\n"));
        Assert.assertTrue(body.contains("\r\norder by: da\r\n"));
        Assert.assertFalse(body.contains("?"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));  
    }
    
    /** 
     *  TestCase for aceptance.
     *  The path from subdirectories must be created correctly.
     *  @throws Exception
     */
    @Test
    public void testAceptance_3() throws Exception {
        
        String request = "GET /test_a/test/ HTTP/1.0\r\n"
                + "Host: vHb\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8081", request));

        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertTrue(header.matches("(?si)^.*\r\nContent-Type: \\w+.*$"));
        Assert.assertTrue(header.matches("(?si)^.*\r\nContent-Length: \\d+.*$"));
        Assert.assertFalse(header.matches("(?si)^.*\\sLast-Modified:.*$"));
        
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2") + "\r\n";
        Assert.assertTrue(body.contains("\r\nindex of: /test_a/test\r\n"));
        Assert.assertTrue(body.contains("\r\norder by: na\r\n"));
        Assert.assertFalse(body.contains("?"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));  
    }
}