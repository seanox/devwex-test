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

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.seanox.test.utils.HttpUtils;
import com.seanox.test.utils.Pattern;
import com.seanox.test.utils.StreamUtils;

/**
 *  TestCases for {@link com.seanox.devwex.Worker}.
 */
public class WorkerTest_Request extends AbstractTest {
    
    /** 
     *  TestCase for timeout.
     *  The timeout is defined to 15 seconds. Because the request is not
     *  terminated, the server waits for the request end. Therefore the
     *  request must be responded with status 408.
     *  @throws Exception
     */  
    @Test(timeout=16000)
    public void testTimeout_1() throws Exception {
       
        String response = new String(HttpUtils.sendRequest("127.0.0.1:80"));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_408));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_408));
    }
    
    /** 
     *  TestCase for timeout.
     *  The timeout is defined to 15 seconds and the request is transmitted
     *  slowly. The total duration is over 15 seconds, but the individual parts
     *  of the request are transmitted less than 15 seconds. The request must
     *  be transmitted completely and responded with status 200.
     *  @throws Exception
     */      
    @Test(timeout=25000)
    public void testTimeout_2() throws Exception {
        
        try (Socket socket = new Socket("127.0.0.1", 8085)) {
            
            InputStream input = new BufferedInputStream(socket.getInputStream(), 65535);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.print("GET / HTTP/1.0");
            writer.flush();
            
            for (int loop1 = 1; loop1 < 10; loop1++) {
                writer.printf("%nline-%02d: ", Integer.valueOf(loop1));
                for (int loop2 = 1; loop2 < 10; loop2++) {
                    Thread.sleep(275);
                    writer.print("x");
                    writer.flush();
                    Assert.assertFalse(input.available() > 0);
                }
            }
            writer.print("\r\n\r\n");
            writer.flush();
            
            String response = new String(StreamUtils.read(input));
            
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
            Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
            
            Thread.sleep(50);
            String accessLog = AbstractSuite.getAccessLogTail();
            Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", "GET / HTTP/1.0")));
        }
    }    
    
    /** 
     *  TestCase for timeout.
     *  The timeout is defined to 15 seconds and the request is transmitted
     *  slowly. The total duration is over 15 seconds, but the individual parts
     *  of the request are transmitted less than 15 seconds. Because the
     *  request is not terminated, the server waits for the request end.
     *  Therefore the request  must be responded with status 408.
     *  @throws Exception
     */      
    @Test(timeout=40000)
    public void testTimeout_3() throws Exception {
        
        try (Socket socket = new Socket("127.0.0.1", 8085)) {
            
            InputStream input = new BufferedInputStream(socket.getInputStream(), 65535);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.print("GET / HTTP/1.0");
            writer.flush();
            
            for (int loop1 = 1; loop1 < 10; loop1++) {
                writer.printf("%nline-%02d: ", Integer.valueOf(loop1));
                for (int loop2 = 1; loop2 < 10; loop2++) {
                    Thread.sleep(275);
                    writer.print("x");
                    writer.flush();
                    Assert.assertFalse(input.available() > 0);
                }
            }

            Thread.sleep(250);
            Assert.assertTrue(input.available() <= 0);
            
            String response = new String(StreamUtils.read(input));
            
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_408));
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
            Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
            
            Thread.sleep(50);
            String accessLog = AbstractSuite.getAccessLogTail();
            Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("408", "GET / HTTP/1.0")));
        }
    }  
    
    /** 
     *  TestCase for aceptance.
     *  Without a path, then terminates the request with status 400.
     *  @throws Exception
     */
    @Test
    public void testAceptance_1() throws Exception {
        
        String request = "GET\r\n\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:80", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_400));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("400", request)));
    }
    
    /** 
     *  TestCase for aceptance.
     *  The path starts without a slash, then terminates the request with
     *  status 400.
     *  @throws Exception
     */
    @Test
    public void testAceptance_2() throws Exception {
        
        String request = "GET XXX\r\n\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:80", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_400));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("400", request)));
    }
    
    /** 
     *  TestCase for aceptance.
     *  If the path starts with backslash, the server will change it to slash
     *  and respond to it with status 200.
     *  @throws Exception
     */
    @Test
    public void testAceptance_3() throws Exception {
        
        String request = "GET \\\r\n\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:80", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_TEXT_HTML));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request)));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Each request line is limited to 32768 characters and will be terminated
     *  with status 413 if it is overlength.
     *  @throws Exception
     */
    @Test
    public void testAceptance_5() throws Exception {
        
        String request = "GET /";
        while (request.length() < 40000)
            request += "x";
        request += "\r\n\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:80", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_413));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("413", request)));
    }    
    
    /** 
     *  TestCase for aceptance.
     *  The request is limited to 65535 characters and will be terminated with
     *  status 200 if it is big but not overlength.
     *  @throws Exception
     */
    @Test
    public void testAceptance_6() throws Exception {
        
        String requestLine = "";
        while (requestLine.length() < 1000)
            requestLine += "x";
        
        String request = "GET / HTTP/1.0\r\n";
        int loop = 0;
        while (request.length() <= 65535)
            request += String.format("line-%02d: %s%n", Integer.valueOf(++loop), requestLine);
        
        String response = new String(HttpUtils.sendRequest("127.0.0.1:80", request + "\r\n"));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_413));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("413", request)));
    }    
    
    /** 
     *  TestCase for aceptance.
     *  The request is limited to 65535 characters and will be terminated with
     *  status 200 if big but it is not overlength.
     *  @throws Exception
     */
    @Test
    public void testAceptance_7() throws Exception {
        
        String requestLine = "";
        while (requestLine.length() < 1000)
            requestLine += "x";
        
        String request = "GET / HTTP/1.0\r\n";
        int loop = 0;
        while (request.length() <= 64535)
            request += "x-x" + ((++loop < 10) ? "0" : "") + loop + "x: " + requestLine + "\r\n";
        
        String response = new String(HttpUtils.sendRequest("127.0.0.1:80", request + "\r\n"));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_TEXT_HTML));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request)));
    }
    
    /** 
     *  TestCase for aceptance.
     *  A request whose header contains no data but only {@code [CRLF][CRLF]}
     *  is terminated with status 400.
     *  @throws Exception
     */
    @Test
    public void testAceptance_8() throws Exception {
        
        String response = new String(HttpUtils.sendRequest("127.0.0.1:80", "\r\n\r\n"));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_400));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_400));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Aborted requests should not be blocked. The value of MAXACCESS must not
     *  be reached. All 150 queries must be responded with status 200, even if
     *  the MAXACCESS is set to 100.
     *  @throws Exception
     */
    @Test
    public void testAceptance_9() throws Exception {
        
        int threadCount = Thread.activeCount();
        
        List<Socket> sockets = new ArrayList<>();
        try {
            for (int loop = 1; loop <= 150; loop++) {
                Socket socket = new Socket("127.0.0.1", 80);
                sockets.add(socket);
                if (loop >= 95 && (loop % 2) == 0) {
                    PrintWriter writer = new PrintWriter(socket.getOutputStream());
                    writer.print("GET / HTTP/1.0\r\n\r\n");
                    writer.flush();
                    
                    String response = new String(StreamUtils.read(socket.getInputStream()));
                    socket.close();

                    Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
                }
            }
        } finally {
            int socketOpened = 0;
            int socketClosed = 0;
            for (Socket socket : sockets) {
                if (socket.isClosed())
                    socketClosed++;
                 else socketOpened++;
                if (!socket.isClosed())
                    socket.close();
            }
            Assert.assertTrue(socketOpened > 100);
            Assert.assertTrue(socketClosed > 20);
            
            while (Thread.activeCount() > threadCount)
                Thread.sleep(50);
            
            AbstractSuite.waitOutputReady();
        }
    }     
}