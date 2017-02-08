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
public class ListenerTest_VirtualHost extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  Virtual hosts must be resolved correctly.
     *  @throws Exception
     */
    @Test
    public void testAceptance_1() throws Exception {
        
        String request = "GET \\cgi_environment.jsx HTTP/1.0\r\n"
                + "Host: vhA";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.matches("(?si)^.*\r\nHTTP_HOST=vhA\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nVIRTUAL_A=Virtualhost A\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nSERVER_C=Server C\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nSERVER_B=Server B\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nSERVER_A=Server A\r\n.*$"));
    }
}