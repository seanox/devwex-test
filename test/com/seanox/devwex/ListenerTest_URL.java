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
public class ListenerTest_URL extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  The determination of a URL must also work in the mix of UFT8 / MIME.
     *  The request is responded with status 200.
     *  @throws Exception
     */
    @Test
    public void testAceptance_01() throws Exception {
        
        String request = "GET /url_ma%c3%9f%FF_1.html HTTP/1.0\r\n"
                + "Host: vHa\r\n";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
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
                + "Host: vHa\r\n";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
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
                + "Host: vHa\r\n";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 404\\s+\\w+.*$"));
    }    
    
    /** 
     *  TestCase for aceptance.
     *  Breaking out of the DocRoot must not happen if the path contains masked
     *  special characters.
     *  @throws Exception
     */
    @Test
    public void testAceptance_04() throws Exception {
        
        String request;
        String response;
        
        request = "GET / HTTP/1.0\r\n\r\n";
        response = new String(TestUtils.sendRequest("127.0.0.1:8081", request));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        String header1 = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header1.trim().length() > 0);
        String body1 = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body1.length() > 0);  
        
        request = "GET /..%c0%af..%c0%af..%c0%af..%c0%af..%c0%af.. HTTP/1.0\r\n\r\n";
        response = new String(TestUtils.sendRequest("127.0.0.1:8081", request));
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
    public void testAceptance_05() throws Exception {
        
        String request;
        String response;
        
        request = "GET / HTTP/1.0\r\n\r\n";
        response = new String(TestUtils.sendRequest("127.0.0.1:8081", request));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        String header1 = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header1.trim().length() > 0);
        String body1 = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body1.length() > 0);  
        
        request = "GET /commons/%2e%2e/%2e%2e%5c%2e%2e%c0%af%2f%2f%2e%2e%2f HTTP/1.0\r\n\r\n";
        response = new String(TestUtils.sendRequest("127.0.0.1:8081", request));
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
    public void testAceptance_06() throws Exception {
        
        String request;
        String response;
        
        request = "GET / HTTP/1.0\r\n\r\n";
        response = new String(TestUtils.sendRequest("127.0.0.1:8081", request));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        String header1 = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header1.trim().length() > 0);
        String body1 = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body1.length() > 0);  
        
        request = "GET /xxx/%2e%2e/%2e%2e%5c%2e%2e%c0%af%2f%2f%2e%2e%2f HTTP/1.0\r\n\r\n";
        response = new String(TestUtils.sendRequest("127.0.0.1:8081", request));
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
    public void testAceptance_07() throws Exception {
        
        String request;
        String response;
        
        request = "GET / HTTP/1.0\r\n\r\n";
        response = new String(TestUtils.sendRequest("127.0.0.1:8081", request));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        String header1 = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header1.trim().length() > 0);
        String body1 = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body1.length() > 0);  
        
        request = "GET /xxx/%2e%2e/%2e%2e%5c%2e%2e%%c0%af%2f%2f%2e%2e%2f HTTP/1.0\r\n\r\n";
        response = new String(TestUtils.sendRequest("127.0.0.1:8081", request));
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
    public void testAceptance_08() throws Exception {
        
        String request;
        String response;
        
        for (int loop = 0; loop < 20; loop++) {

            request = "/";
            while (request.length() < ((loop +1) *2) +1)
                request += "%20/";

            request = "GET " + request + " HTTP/1.0\r\n\r\n";
            response = new String(TestUtils.sendRequest("127.0.0.1:8080", request));
            Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 404\\s+\\w+.*$"));
        }        
    }      
}