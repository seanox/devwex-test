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
 *  TestCases for {@link com.seanox.devwex.Remote}.
 */
public class RemoteTest_Timeout extends AbstractTest {

    /** 
     *  TestCase for timeout.
     *  The data access from remote access is limited to 10 seconds.
     *  The request has to be aborted after ca. 10 seconds.
     *  A response is not expected.
     *  @throws Exception
     */    
    @Test(timeout=11000)
    public void testTimeout_1() throws Exception {
        
        String response = new String(HttpUtils.sendRequest("127.0.0.1:25001"));
        
        Assert.assertTrue(response.isEmpty());
    }    
    
    /** 
     *  TestCase for timeout.
     *  The data access from remote access is limited to 10 seconds.
     *  The request has to be aborted after ca. 10 seconds, even if a request
     *  is started. A response is not expected.
     *  @throws Exception
     */   
    @Test(timeout=11000)
    public void testTimeout_2() throws Exception {
        
        String response = new String(HttpUtils.sendRequest("127.0.0.1:25001", "sTatuS"));
        
        Assert.assertTrue(response.isEmpty());
    }     
}