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

/**
 *  TestCases for {@link com.seanox.devwex.Service}.
 */
public class ServiceTest_Server extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  A server can be configured and started by different instances.
     *      [REMOTE:BAS]    25001
     *      [REMOTE:A:BAS]  25002
     *      [REMOTE:B:BAS]  25003
     *      [REMOTE::BAS]   25004
     *  @throws Exception
     */    
    @Test
    public void testAceptance_01() throws Exception {
        
        for (int port = 25001; port < 25004; port++) {
            String response = new String(HttpUtils.sendRequest("127.0.0.1:" + port, "sTatuS\r"));
            Assert.assertNotNull(response);
            Assert.assertTrue(response, response.contains("\r\nSAPI: "));
            Assert.assertTrue(response, response.contains("\r\nTIME: "));
            Assert.assertTrue(response, response.contains("\r\nTIUP: "));            
        }
    }
    
    @Test
    public void testAceptance_02() throws Exception {
        
        String response;
        response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9001));
        Assert.assertEquals("1 com.seanox.devwex.Count$1", response);
        for (int loop = 2; loop < 10; loop++) {
            response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9001));
            Assert.assertEquals(String.valueOf(loop) + " com.seanox.devwex.Count$1", response);
        }
        response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9002));
        Assert.assertEquals("1 com.seanox.devwex.Count$1", response);
        for (int loop = 2; loop < 15; loop++) {
            response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9002));
            Assert.assertEquals(String.valueOf(loop) + " com.seanox.devwex.Count$1", response);
        }        
        response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9001));
        Assert.assertEquals("10 com.seanox.devwex.Count$1", response);
    }
    
    @Test
    public void testAceptance_03() throws Exception {
        
        String response;
        response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9003));
        Assert.assertEquals("1 example.Count$1", response);
        for (int loop = 2; loop < 10; loop++) {
            response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9003));
            Assert.assertEquals(String.valueOf(loop) + " example.Count$1", response);
        }
        response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9004));
        Assert.assertEquals("1 example.Count$1", response);
        for (int loop = 2; loop < 15; loop++) {
            response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9004));
            Assert.assertEquals(String.valueOf(loop) + " example.Count$1", response);
        }        
        response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9003));
        Assert.assertEquals("10 example.Count$1", response);
    }    
    
    //TODO; [COUNT:B:BAS]   example-x (existiert nicht)
    
    //TODO: Beispiel für fehlerhaft implementierte Server API
    //TODO: Beispiel für Fehler beim Initialisieren
    //TODO: Beispiel für Fehler beim Run
}