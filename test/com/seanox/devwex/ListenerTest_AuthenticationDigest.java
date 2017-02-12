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
public class ListenerTest_AuthenticationDigest extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a/b/e [acC:group:BE[realm:Section-BE [D]}
     *  With a corrupt acc rule the authentication is ignored and the request
     *  is responded with status 200. But the 
     *  
     *  B:/Documents/Projects/devwex-test/stage/documents_vh_A/authentication/a/b/e/ [acC:group:BE[realm:Section-BE
     *  
     *  
     *  @throws Exception
     */
    @Test
    public void testAceptance_98() throws Exception {
        
        String request = "GET /authentication/a/b/e/ HTTP/1.0\r\n"
                + "Host: vHf\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_WWW_AUTHENTICATE_DIFFUSE));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Test for Basic Authentication:
     *  {@code /authentication/a/b/e/c [acC:group:BEC][realm:Section-BEC}
     *  With a correct acc rule after a corrupt acc rule, the authentication is
     *  requierd and the request is responded with status 401.
     *  @throws Exception
     */
    @Test
    public void testAceptance_99() throws Exception {
        
        String request = "GET /authentication/a/b/e/c/ HTTP/1.0\r\n"
                + "Host: vHf\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_401));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_WWW_AUTHENTICATE_DIGEST("Section-BEC")));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_401));
    } 
}