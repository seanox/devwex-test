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
 *  TestCases for {@link com.seanox.devwex.Listener}.
 */
public class ListenerTest_VirtualHost extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  Virtual hosts must be resolved correctly.
     *  @throws Exception
     */
    @Test
    public void testAceptance_01() throws Exception {
        
        String request = "GET \\cgi_environment.jsx HTTP/1.0\r\n"
                + "Host: vhA\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.matches("(?si)^.*\r\nHTTP_HOST=vhA\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nVIRTUAL_A=Virtualhost A\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nSERVER_A=Server A\r\n.*$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Virtual hosts must be resolved correctly.
     *  @throws Exception
     */
    @Test
    public void testAceptance_02() throws Exception {
        
        String request = "GET \\cgi_environment.jsx HTTP/1.0\r\n"
                + "Host: vhA\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8081", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.matches("(?si)^.*\r\nHTTP_HOST=vhA\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nVIRTUAL_A=Virtualhost A\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nSERVER_C=Server C\r\n.*$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  The virtual host must observe the parameter SERVER.
     *  @throws Exception
     */
    @Test
    public void testAceptance_03() throws Exception {
        
        for (int port : new int[] {8081, 8082, 8083, 80}) {
            String request = "GET \\cgi_environment.jsx HTTP/1.0\r\n"
                    + "Host: vhS\r\n"
                    + "\r\n";
            String response = new String(HttpUtils.sendRequest("127.0.0.1:" + port, request));
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));

            String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
            Assert.assertTrue(header.trim().length() > 0);
            String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
            if (port != 80)
                Assert.assertTrue(body.matches("(?si)^.*\r\nVIRTUAL_S=Virtualhost S\r\n.*$"));
            else 
                Assert.assertFalse(body.matches("(?si)^.*\r\nVIRTUAL_S=Virtualhost S\r\n.*$"));
        }
    }
    
    /** 
     *  TestCase for aceptance.
     *  The virtual host must observe the parameter SERVER.
     *  @throws Exception
     */
    @Test
    public void testAceptance_04() throws Exception {
        
        for (int port : new int[] {8081, 8082, 8083, 80}) {
            String request = "GET \\cgi_environment.jsx HTTP/1.0\r\n"
                    + "Host: vhT\r\n"
                    + "\r\n";
            String response = new String(HttpUtils.sendRequest("127.0.0.1:" + port, request));
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));

            String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
            Assert.assertTrue(header.trim().length() > 0);
            String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
            Assert.assertTrue(body.matches("(?si)^.*\r\nVIRTUAL_T=Virtualhost T\r\n.*$"));
        }
    }
}