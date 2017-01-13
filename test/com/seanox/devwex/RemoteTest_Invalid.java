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
 *  TestCases for {@link com.seanox.devwex.Remote}.
 */
public class RemoteTest_Invalid extends AbstractTest {
    
    /** 
     *  TestCase for an unknown command and overlength.
     *  The length of the request is limited to 65535 bytes and must be
     *  responded with {@code INFO: UNKNOWN COMMAND}.
     *  @throws Exception
     */
    @Test
    public void testUnknownCommand_1() throws Exception {

        String command = "";
        while (command.length() < 65536)
            command += "XXXXXXXXX";
        String response = new String(TestUtils.sendRequest("127.0.0.1:25001", command + "\r\n"));
        
        Assert.assertEquals("INFO: UNKNOWN COMMAND\r\n", response);
    }
    
    /** 
     *  TestCase for an unknown command.
     *  The command {@code Restar[\r\n]123} is invalid and must be
     *  responded with {@code INFO: UNKNOWN COMMAND}.
     *  @throws Exception
     */
    @Test
    public void testUnknownCommand_2() throws Exception {
        
        String response = new String(TestUtils.sendRequest("127.0.0.1:25001", "restar\r\n123"));
        
        Assert.assertEquals("INFO: UNKNOWN COMMAND\r\n", response);
    }
}