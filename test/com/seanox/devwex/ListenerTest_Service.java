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
public class ListenerTest_Service extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER:L:BAS] SERVICE = ConnectorB}
     *  ConnectorB was defined for request processing.
     *  The request must be responded with status 002.
     *  The request will not be logged because this has to do ConnectorB, but
     *  it does not have the logic for that.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_01() throws Exception {
        
        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        
        String request = "GET / HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8089", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("002 Test ok")));
        Assert.assertTrue(response.contains("\r\nModul: ConnectorB\r\n"));
        Assert.assertTrue(response.contains("\r\nModultype: 0\r\n"));
        
        Thread.sleep(50);
        Assert.assertEquals(accessLog, AbstractSuite.getAccessLogTail());
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {[SERVER:K:BAS] SERVICE = ConnectorBxxx}
     *  ConnectorBxxx was defined for request processing.
     *  There must be an error for this request because ConnectorBxxx does not
     *  exist.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_02() throws Exception {
        
        String request = "GET / HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8088", request));
        
        Assert.assertTrue(response.isEmpty());
        
        Thread.sleep(50);
        String outputLog = AbstractSuite.getOutputLogTail();
        Assert.assertTrue(outputLog.contains("java.lang.ClassNotFoundException: ConnectorBxxx"));
    }    
}