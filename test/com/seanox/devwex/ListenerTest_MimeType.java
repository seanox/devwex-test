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
public class ListenerTest_MimeType extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  For the file extension {@code xxx} was not defined a mimetype.
     *  The request must be responded with status 200 and the standard mimetype.
     *  @throws Exception
     */
    @Test
    public void testAceptance_1() throws Exception {
        
        String request = "GET /mimetype_test.xxx HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:80", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nContent-Type: application/octet-stream\r\n.*$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  For the file extension {@code xls} was defined a mimetype.
     *  The request must be responded with status 200 and the defined a mimetype.
     *  @throws Exception
     */
    @Test
    public void testAceptance_2() throws Exception {
        
        String request = "GET /mimetype_test.xls HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:80", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nContent-Type: application/vnd.ms-excel\r\n.*$"));
    }    
    
    /** 
     *  TestCase for aceptance.
     *  For the file extension {@code xls} was defined a mimetype.
     *  The request with {@code Accept: *}{@code /*} must be responded with
     *  status 200 and the defined a mimetype.
     *  @throws Exception
     */
    @Test
    public void testAceptance_3() throws Exception {
        
        String request = "GET /mimetype_test.xls HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Accept: */*";
        String response = new String(TestUtils.sendRequest("127.0.0.1:80", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nContent-Type: application/vnd.ms-excel\r\n.*$"));
    }  
    
    /** 
     *  TestCase for aceptance.
     *  For the file extension {@code xls} was defined a mimetype.
     *  The request with {@code Accept: application/*} must be responded with
     *  status 200 and the defined a mimetype.
     *  @throws Exception
     */
    @Test
    public void testAceptance_4() throws Exception {
        
        String request = "GET /mimetype_test.xls HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Accept: application/*";
        String response = new String(TestUtils.sendRequest("127.0.0.1:80", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nContent-Type: application/vnd.ms-excel\r\n.*$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  For the file extension {@code xls} was defined a mimetype.
     *  The request with {@code Accept: *}{@code /vnd.ms-excel} must be
     *  responded with status 200 and the defined a mimetype.
     *  @throws Exception
     */
    @Test
    public void testAceptance_5() throws Exception {
        
        String request = "GET /mimetype_test.xls HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Accept: */vnd.ms-excel";
        String response = new String(TestUtils.sendRequest("127.0.0.1:80", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nContent-Type: application/vnd.ms-excel\r\n.*$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  For the file extension {@code xxx} was not defined a mimetype.
     *  The request must be responded with status 200 and the standard mimetype.
     *  @throws Exception
     */
    @Test
    public void testAceptance_6() throws Exception {
        
        String request = "GET /mimetype_test.xxx HTTP/1.0\r\n"
                + "Host: vHi\r\n"
                + "Accept: */*";
        String response = new String(TestUtils.sendRequest("127.0.0.1:80", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nContent-Type: application/octet-stream\r\n.*$"));
    }  
    
    /** 
     *  TestCase for aceptance.
     *  Accept and the content-type the server has determined do not match.
     *  The request must be responded with status 406.
     *  @throws Exception
     */
    @Test
    public void testAceptance_7() throws Exception {
        
        String request = "GET /mimetype_test.xxx HTTP/1.0\r\n"
                + "Host: vHi\r\n"
                + "Accept: no/*";
        String response = new String(TestUtils.sendRequest("127.0.0.1:80", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 406\\s+\\w+.*$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Accept and the content-type the server has determined do not match.
     *  The request must be responded with status 406.
     *  @throws Exception
     */
    @Test
    public void testAceptance_8() throws Exception {
        
        String request = "GET /mimetype_test.xxx HTTP/1.0\r\n"
                + "Host: vHi\r\n"
                + "Accept: */no";
        String response = new String(TestUtils.sendRequest("127.0.0.1:80", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 406\\s+\\w+.*$"));
    }  
    
    /** 
     *  TestCase for aceptance.
     *  For the file extension {@code xxx} was not defined a mimetype.
     *  The request must be responded with status 200 and the standard mimetype.
     *  Accept is blank and is ignored.
     *  @throws Exception
     */
    @Test
    public void testAceptance_9() throws Exception {
        
        String request = "GET /mimetype_test.xxx HTTP/1.0\r\n"
                + "Host: vHi\r\n"
                + "Accept:     ";
        String response = new String(TestUtils.sendRequest("127.0.0.1:80", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nContent-Type: application/octet-stream\r\n.*$"));
    } 
}