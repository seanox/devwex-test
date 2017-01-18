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
               + "Host: vHad";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sWWW-Authenticate:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
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
               + "Host: vHad";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401\\s+\\w+.*$"));
        Assert.assertTrue(response.contains("\r\nWWW-Authenticate: Digest realm=\"Section-BEC\""));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));
    } 
}