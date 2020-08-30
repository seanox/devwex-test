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

/**
 * Test cases for {@link com.seanox.devwex.Remote}.<br>
 * <br>
 * RemoteTest_Status 5.1.0 20171231<br>
 * Copyright (C) 2017 Seanox Software Solutions<br>
 * All rights reserved.
 *
 * @author  Seanox Software Solutions
 * @version 5.1.0 20171231
 */
public class RemoteTest_Status extends AbstractTest {
    
    /** 
     * Test case for command: STATUS.
     * The command {@code STATUS[CR]} must return {@code VERS}, {@code TIME},
     * {@code TIUP} and {@code SAPI}.
     * @throws Exception
     */
    @Test
    public void testStatus_1() throws Exception {
        
        String response = new String(HttpUtils.sendRequest("127.0.0.1:25001", "sTatuS\r"));
        
        Assert.assertNotNull(response);
        Assert.assertTrue(response, response.contains("\r\nSAPI: "));
        Assert.assertTrue(response, response.contains("\r\nTIME: "));
        Assert.assertTrue(response, response.contains("\r\nTIUP: "));
    }
    
    /** 
     * Test case for command: STATUS.
     * The command {@code STATUS[LF]} must return {@code VERS}, {@code TIME},
     * {@code TIUP} and {@code SAPI}.
     * @throws Exception
     */
    @Test
    public void testStatus_2() throws Exception {
        
        String response = new String(HttpUtils.sendRequest("127.0.0.1:25001", "sTatuS\n"));

        Assert.assertNotNull(response);
        Assert.assertTrue(response, response.contains("\r\nSAPI: "));
        Assert.assertTrue(response, response.contains("\r\nTIME: "));
        Assert.assertTrue(response, response.contains("\r\nTIUP: "));
    }
    
    /** 
     * Test case for command: STATUS.
     * The command {@code STATUS[CRLF]} must return {@code VERS}, {@code TIME},
     * {@code TIUP} and {@code SAPI}.
     * @throws Exception
     */
    @Test
    public void testStatus_3() throws Exception {
        
        String response = new String(HttpUtils.sendRequest("127.0.0.1:25001", "sTatuS\r\n"));
        
        Assert.assertNotNull(response);
        Assert.assertTrue(response, response.contains("\r\nSAPI: "));
        Assert.assertTrue(response, response.contains("\r\nTIME: "));
        Assert.assertTrue(response, response.contains("\r\nTIUP: "));
    }
}