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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

/**
 *  TestCases for {@link com.seanox.devwex.Listener}.
 */
public class ListenerTest_AccessLog extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  The usage of time symbols in the file name must work.
     *  @throws Exception
     */
    @Test
    public void testAceptance_1() throws Exception {
        
        String accessLog = new SimpleDateFormat("'access-'yyyy-MMdd'.log'").format(new Date());
        File accessLogFile = new File(TestUtils.getRootStage(), accessLog);
        accessLogFile.delete();
        
        String request = "GET / HTTP/1.0\r\n"
                + "Host: vHk"; 
        String response = new String(TestUtils.sendRequest("127.0.0.1:80", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        
        Thread.sleep(250);
        Assert.assertTrue(accessLogFile.exists());
    }
    
    /** 
     *  TestCase for aceptance.
     *  The time zone must be set correctly in the access log.
     *  @throws Exception
     */
    @Test
    public void testAceptance_2() throws Exception {
        
        TestUtils.sendRequest("127.0.0.1:80", "GET / HTTP/1.0\r\n\r\n");
        
        String pattern = new SimpleDateFormat("Z", Locale.US).format(new Date());
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^.*?\\[\\d{2}/\\w{3}/\\d{4}(:\\d{2}){3} \\" + pattern + "\\]\\s.*$"));
    }    
}