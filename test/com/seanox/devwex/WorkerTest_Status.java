/**
 * LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 * im Folgenden Seanox Software Solutions oder kurz Seanox genannt.
 * Diese Software unterliegt der Version 2 der GNU General Public License.
 *
 * Devwex, Advanced Server Development
 * Copyright (C) 2020 Seanox Software Solutions
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of version 2 of the GNU General Public License as published by the
 * Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.seanox.devwex;

import org.junit.Assert;
import org.junit.Test;

import com.seanox.test.utils.HttpUtils;
import com.seanox.test.utils.Pattern;

/**
 * Test cases for {@link com.seanox.devwex.Worker}.<br>
 * <br>
 * WorkerTest_Status 5.1.0 20171231<br>
 * Copyright (C) 2017 Seanox Software Solutions<br>
 * All rights reserved.
 *
 * @author  Seanox Software Solutions
 * @version 5.1.0 20171231
 */
public class WorkerTest_Status extends AbstractTest {
    
    /** 
     * Test case for acceptance.
     * Request of a forbidden url.
     * Template for status 403 is not dedicated and is contained in status-4xx.html.
     * The response must contains 'Template: status-4xx.html'.
     * @throws Exception
     */
    @Test
    public void testAcceptance_1() throws Exception {
        
        String request = "Get /forbidden.html HTTP/1.0\r\n"
                + "Host: vHo\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:18185", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_403));
        Assert.assertTrue(response.contains("Template: status-4xx.html"));
    }
    
    /** 
     * Test case for acceptance.
     * Request of a faulty CGI application.
     * Template for status 502 is not dedicated and is contained in status.html.
     * The response must contains 'Template: status.html'.
     * @throws Exception
     */
    @Test
    public void testAcceptance_2() throws Exception {
        
        String request = "Get /error.cgi HTTP/1.0\r\n"
                + "Host: vHo\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:18185", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_502));
        Assert.assertTrue(response.contains("Template: status.html"));
    }
    
    /** 
     * Test case for acceptance.
     * Request of a not exsting url.
     * Template for status 404 is dedicated.
     * The response must contains 'Template: status-404.html'.
     * @throws Exception
     */
    @Test
    public void testAcceptance_3() throws Exception {
        
        String request = "Get /not_found.html HTTP/1.0\r\n"
                + "Host: vHo\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:18185", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_404));
        Assert.assertTrue(response.contains("Template: status-404.html"));
    }    
}