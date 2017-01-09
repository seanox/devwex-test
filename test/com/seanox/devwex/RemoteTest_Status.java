/**
 *  LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 *  im Folgenden Seanox Software Solutions oder kurz Seanox genannt. Diese
 *  Software unterliegt der Version 2 der GNU General Public License.
 *
 *  Seanox Commons, Advanced Programming Interface
 *  Copyright (C) 2016 Seanox Software Solutions
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

import java.io.PrintWriter;
import java.net.Socket;

import org.junit.Assert;
import org.junit.Test;

import com.seanox.test.utils.StreamUtils;

/**
 *  TestCases for {@link com.seanox.devwex.Remote}.
 */
public class RemoteTest_Status extends AbstractTest {
    
    /** 
     *  TestCase for command: STATUS.
     *  The command {@code STATUS[CR]} must return {@code VERS}, {@code TIME},
     *  {@code TIUP} and {@code SAPI}.
     *  @throws Exception
     */
    @Test
    public void testStatus_1() throws Exception {

        String result = null;
        try (Socket socket = new Socket("127.0.0.1", 25001)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.print("sTatuS\r");
            writer.flush();
            result = new String(StreamUtils.read(socket.getInputStream()));              
        }
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("\r\nSAPI: "));
        Assert.assertTrue(result.contains("\r\nTIME: "));
        Assert.assertTrue(result.contains("\r\nTIUP: "));
    }
    
    /** 
     *  TestCase for command: STATUS.
     *  The command {@code STATUS[LF]} must return {@code VERS}, {@code TIME},
     *  {@code TIUP} and {@code SAPI}.
     *  @throws Exception
     */
    @Test
    public void testStatus_2() throws Exception {

        String result = null;
        try (Socket socket = new Socket("127.0.0.1", 25001)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.print("sTatuS\n");
            writer.flush();
            result = new String(StreamUtils.read(socket.getInputStream()));    
        }
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("\r\nSAPI: "));
        Assert.assertTrue(result.contains("\r\nTIME: "));
        Assert.assertTrue(result.contains("\r\nTIUP: "));
    }
    
    /** 
     *  TestCase for command: STATUS.
     *  The command {@code STATUS[CRLF]} must return {@code VERS}, {@code TIME},
     *  {@code TIUP} and {@code SAPI}.
     *  @throws Exception
     */
    @Test
    public void testStatus_3() throws Exception {

        String result = null;
        try (Socket socket = new Socket("127.0.0.1", 25001)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.print("sTatuS\r\n");
            writer.flush();
            result = new String(StreamUtils.read(socket.getInputStream()));              
        }
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result.contains("\r\nSAPI: "));
        Assert.assertTrue(result.contains("\r\nTIME: "));
        Assert.assertTrue(result.contains("\r\nTIUP: "));
    }
}