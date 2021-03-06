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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.seanox.test.utils.Pattern;
import com.seanox.test.utils.StreamUtils;
import com.seanox.test.utils.Timing;

/**
 * Test cases for {@link com.seanox.devwex.Worker}.<br>
 * <br>
 * WorkerTest_Request 5.2.0 20200411<br>
 * Copyright (C) 2020 Seanox Software Solutions<br>
 * All rights reserved.
 *
 * @author  Seanox Software Solutions
 * @version 5.2.0 20200411
 */
public class WorkerTest_Request extends AbstractTest {
    
    /** 
     * Test case for timeout.
     * The timeout is defined to 15 seconds. Because the request is not
     * terminated, the server waits for the request end. Therefore the request
     * must be responded with status 408.
     * @throws Exception
     */  
    @Test(timeout=17500)
    public void testTimeout_1() throws Exception {
        
        String response = this.sendRequest("127.0.0.1:18080", "");
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_408));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_408));
    }
    
    /** 
     * Test case for timeout.
     * The timeout is defined to 15 seconds and the request is transmitted
     * slowly. The total duration is over 15 seconds, but the individual parts
     * of the request are transmitted less than 15 seconds. The request must be
     * transmitted completely and responded with status 200.
     * @throws Exception
     */ 
    @Test
    public void testTimeout_2() throws Exception {
        
        String uuid = UUID.randomUUID().toString();
        Files.write(new File(PATH_STAGE_ACCESS_LOG).toPath(), (uuid + "\r\n").getBytes(), StandardOpenOption.APPEND);
        
        try (Socket socket = new Socket("127.0.0.1", 18185)) {
            
            Timing timing = Timing.create(true);
            
            InputStream input = new BufferedInputStream(socket.getInputStream(), 65535);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.print("GET / HTTP/1.0");
            writer.flush();
            
            for (int loop1 = 1; loop1 < 10; loop1++) {
                writer.printf("%nline-%02d: ", Integer.valueOf(loop1));
                for (int loop2 = 1; loop2 < 10; loop2++) {
                    Thread.sleep(300);
                    writer.print("x");
                    writer.flush();
                    Assert.assertFalse(input.available() > 0);
                }
            }
            writer.print("\r\n\r\n");
            writer.flush();
            
            String response = new String(StreamUtils.read(input));
            
            timing.assertTimeOut(20000);
            
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
            Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
            
            this.accessStreamCapture.await(ACCESS_LOG_UUID(uuid));
            String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_UUID(uuid));
            Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", "GET / HTTP/1.0")));
        }
    }    
    
    /** 
     * Test case for timeout.
     * The timeout is defined to 15 seconds and the request is transmitted
     * slowly. The total duration is over 15 seconds, but the individual parts
     * of the request are transmitted less than 15 seconds. Because the request
     * is not terminated, the server waits for the request end.
     * Therefore the request  must be responded with status 408.
     * @throws Exception
     */    
    @Test
    public void testTimeout_3() throws Exception {
        
        String uuid = UUID.randomUUID().toString();
        Files.write(new File(PATH_STAGE_ACCESS_LOG).toPath(), (uuid + "\r\n").getBytes(), StandardOpenOption.APPEND);
        
        try (Socket socket = new Socket("127.0.0.1", 18185)) {
            
            Timing timing = Timing.create(true);

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
            
            timing.assertTimeOut(25000);
            
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_408));
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
            Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
            
            this.accessStreamCapture.await(ACCESS_LOG_UUID(uuid));
            String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_UUID(uuid));
            Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("408", "GET / HTTP/1.0")));
        }
    }  
    
    /** 
     * Test case for acceptance.
     * Without a path, then terminates the request with status 400.
     * @throws Exception
     */
    @Test
    public void testAcceptance_1() throws Exception {
            
        String request = "GET\r\n\r\n";
        String response = this.sendRequest("127.0.0.1:18080", request);
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_400));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("400", request)));
    }
    
    /** 
     * Test case for acceptance.
     * The path starts without a slash, then terminates the request with status 400.
     * @throws Exception
     */
    @Test
    public void testAcceptance_2() throws Exception {
        
        String request = "GET XXX\r\n\r\n";
        String response = this.sendRequest("127.0.0.1:18080", request);
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_400));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("400", request)));
    }
    
    /** 
     * Test case for acceptance.
     * If the path starts with backslash, the server will change it to slash and
     * respond to it with status 200.
     * @throws Exception
     */
    @Test
    public void testAcceptance_3() throws Exception {
        
        String request = "GET \\\r\n\r\n";
        String response = this.sendRequest("127.0.0.1:18080", request);
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_TEXT_HTML));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request)));
    }
    
    /** 
     * Test case for acceptance.
     * Each request line is limited to 32768 characters and will be terminated
     * with status 413 if it is overlength.
     * @throws Exception
     */
    @Test
    public void testAcceptance_5() throws Exception {
        
        String request = "GET /";
        while (request.length() < 40000)
            request += "x";
        request += "\r\n\r\n";
        String response = this.sendRequest("127.0.0.1:18080", request);
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_413));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("413", request)));
    }    
    
    /** 
     * Test case for acceptance.
     * The request is limited to 65535 characters and will be terminated with
     * status 200 if it is big but not overlength.
     * @throws Exception
     */
    @Test
    public void testAcceptance_6() throws Exception {
        
        String requestLine = "";
        while (requestLine.length() < 1000)
            requestLine += "x";
        
        String request = "GET / HTTP/1.0\r\n";
        int loop = 0;
        while (request.length() <= 65535)
            request += String.format("line-%02d: %s%n", Integer.valueOf(++loop), requestLine);
        
        String response = this.sendRequest("127.0.0.1:18080", request + "\r\n");
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_413));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("413", request)));
    }    
    
    /** 
     * Test case for acceptance.
     * The request is limited to 65535 characters and will be terminated with
     * status 200 if big but it is not overlength.
     * @throws Exception
     */
    @Test
    public void testAcceptance_7() throws Exception {
        
        String requestLine = "";
        while (requestLine.length() < 1000)
            requestLine += "x";
        
        String request = "GET / HTTP/1.0\r\n";
        int loop = 0;
        while (request.length() <= 64535)
            request += "x-x" + ((++loop < 10) ? "0" : "") + loop + "x: " + requestLine + "\r\n";
        
        String response = this.sendRequest("127.0.0.1:18080", request + "\r\n");
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_TEXT_HTML));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request)));
    }
    
    /** 
     * Test case for acceptance.
     * A request whose header contains no data but only {@code [CRLF][CRLF]} is
     * terminated with status 400.
     * @throws Exception
     */
    @Test
    public void testAcceptance_8() throws Exception {
        
        String response = this.sendRequest("127.0.0.1:18080", "\r\n\r\n");
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_400));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_400));
    } 
    
    /** 
     * Test case for acceptance.
     * Aborted requests should not be blocked. The value of MAXACCESS must not
     * be reached. All 150 queries must be responded with status 200, even if
     * the MAXACCESS is set to 100.
     * @throws Exception
     */
    @Test
    public void testAcceptance_9() throws Exception {
        
        Service.restart();
        Thread.sleep(SLEEP);
        
        List<Socket> sockets = new ArrayList<>();
        try {
            for (int loop = 1; loop <= 150; loop++) {
                Socket socket = new Socket("127.0.0.1", 18080);
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
            
            AbstractTestUtils.waitOutputFacadeStream(com.seanox.test.AbstractSuite.outputStream);
        }
    }     
}