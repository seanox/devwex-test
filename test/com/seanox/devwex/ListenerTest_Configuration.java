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
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import com.seanox.test.utils.Pattern;

/**
 *  TestCases for {@link com.seanox.devwex.Listener}.
 */
public class ListenerTest_Configuration extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] IDENTITY = ON}
     *  The CGI-parameter {@code SERVER_NAME} must be available.
     *  @throws Exception
     */
    @Test
    public void testAceptance_01() throws Exception {
        
        String request = "GET \\cgi_environment.jsx?parameter=SERVER_NAME HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));

        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2") + "\r\n";
        Assert.assertTrue(body.contains("\r\nSERVER_NAME=vHa\r\n"));
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));  
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] IDENTITY = ?}
     *  The CGI-parameter {@code SERVER_NAME} is not available.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_02() throws Exception {
        
        String request = "GET \\documents\\cgi_environment.jsx?parameter=SERVER_NAME HTTP/1.0\r\n"
                + "Host: vHc\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));

        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2") + "\r\n";
        Assert.assertFalse(body.contains("\r\nSERVER_NAME\r\n"));
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));  
    }

    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] IDENTITY = OFF}
     *  The CGI-parameter {@code SERVER_NAME} is not available.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_03() throws Exception {
        
        String request = "GET \\cgi_environment.jsx?parameter=SERVER_NAME HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));

        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2") + "\r\n";
        Assert.assertFalse(body.contains("\r\nSERVER_NAME\r\n"));
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));  
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] docroot = ?}
     *  The DooRoot is the current working directory.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_04() throws Exception {
        
        String request = "GET / HTTP/1.0\r\n"
                + "Host: vHq\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));

        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2") + "\r\n";
        for (File file : new File(TestUtils.getRootStage(), "..").listFiles())
            Assert.assertTrue(body.contains("\r\nname:" + file.getName() + "\r\n"));
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));  
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] DEFAULT = index_1.html, index_2.html}
     *  In the requested directory, the file index_1.html is found and shown.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_05() throws Exception {
        
        String request = "GET /test_a/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED));     
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH(12)));
        String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("index_1.html", body);
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));  
    }

    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] DEFAULT = index_1.html, index_2.html}
     *  In the requested directory, the file index_2.html is found and shown.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_06() throws Exception {
        
        String request = "GET /test_b/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH(12)));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED));     
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2") + "\r\n";
        Assert.assertTrue(body.contains("\r\nindex_2.html\r\n"));
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));          
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] DEFAULT = index_1.html, index_2.html}
     *  In the requested directory, the files index_1.html and index_2.html is
     *  not found, therefore the directory is shown.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_07() throws Exception {
        
        String request = "GET /test_c/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));     
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2") + "\r\n";
        Assert.assertTrue(body.contains("\r\nindex of: /test_c\r\n"));
        Assert.assertTrue(body.contains("\r\ntemplate: /system_vh_A/index.html\r\n"));
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));  
    }    

    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] DEFAULT = ' '}
     *  Without correct default, the directory is shown.
     *  Without ACCESSLOG, the StdIO is used for logging.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_08() throws Exception {
        
        String request = "GET /?test=1234567 HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8094", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH(0)));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_TEXT_HTML));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));     
        String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("", body);
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertFalse(accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request)));
        String outputLog = TestUtils.getOutputLogTail();
        outputLog = outputLog.replaceAll("(?i)^.*?\\s(\\d+(?:\\.\\d+){3}.*$)", "$1");
        Assert.assertTrue(outputLog.matches(Pattern.ACCESS_LOG_STATUS("200", request)));
    }

    /** 
     *  TestCase for aceptance.
     *  Configuration:
     *      {@code [SERVER/VIRTUAL:BAS] MAXACCESS = 3}
     *      {@code [SERVER/VIRTUAL:BAS] BACKLOG = 5}
     *  It will: 3 connections are accepted, 5 are reserved and each additional
     *  are refused.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_09() throws Exception {
        
        int threadCount = Thread.activeCount();
        
        try {
            LinkedList<Object> socketList = new LinkedList<>();
            for (int loop = 0; loop < 9; loop++) {
                try {socketList.add(new Socket("127.0.0.1", 8093));
                } catch (Exception exception) {
                    socketList.add(exception);
                }
            }
            
            String pattern = "";
            for (Object object : socketList) {
                pattern += object instanceof Exception ? "0" : "1";
                if (object instanceof Socket)
                    try {((Socket)object).close();
                    } catch (Exception exception) {
                        socketList.add(exception);
                    }
            }
            
            Assert.assertEquals("111111100", pattern);
        } finally {
            while (Thread.activeCount() > threadCount)
                Thread.sleep(50);
            
            String shadow = null;
            long timeout  = System.currentTimeMillis();
            while (System.currentTimeMillis() -timeout < 3000) {
                Thread.sleep(50);
                String accessLog = TestUtils.getAccessLogTail();
                if (!accessLog.equals(shadow))
                    timeout = System.currentTimeMillis();
                shadow = accessLog;
            }
        }
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] METHODS = XXX}
     *  The method XXX is included in the method list, but not implemented.
     *  The request must be responded with status 501.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_10() throws Exception {
        
        String request = "XXX / HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_501));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_TEXT_HTML));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_501)); 
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] METHODS = XXX}
     *  The method ZZZ is included in the method list and not implemented.
     *  The request must be responded with status 405.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_11() throws Exception {
        
        String request = "ZZZ / HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_405));
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_405));          
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] METHODS = AAA}
     *  The method AAA is included in the method list, but not implemented.
     *  The request must be responded with status 501.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_12() throws Exception {
        
        String request = "AAA / HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_501));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_TEXT_HTML));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_501)); 
    }

    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] METHODS = BBB}
     *  The method BBB is included in the method list, but no module is
     *  assigned. The request must be responded with status 501.
     *  @throws Exception
     */        
    @Test
    public void testAceptance_13() throws Exception {
        
        String request = "BBB / HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_501));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_TEXT_HTML));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_501)); 
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] METHODS = BBB}
     *  The method CCC is not included in the method list. The request must be
     *  responded with status 405, even if a module exists.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_14() throws Exception {
        
        String request = "CCC / HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_405));
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_405));          
    }
    
    private void onBeforeTestAceptance_15() throws Exception {
        
        Path path = new File(TestUtils.getRootStage(), "/documents/commons/hidden.txt").toPath();
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE);
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] INDEX = ON}
     *  The file index includes all entries of a directory, even hidden entries.
     *  Access to hidden files is allowed.
     *  @throws Exception
     */ 
    @Test
    public void testAceptance_15() throws Exception {
        
        String request;
        String response;
        String body;

        request = "GET /commons/ HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8081", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.contains("\r\nname:hidden.txt\r\n"));
        
        request = "GET /commons/hidden.txt HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8081", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("123", body);
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] INDEX = ON [S]}
     *  The file index includes all not hidden entries of a directory.
     *  Access to hidden files is allowed.
     *  @throws Exception
     */ 
    @Test
    public void testAceptance_16() throws Exception {
        
        String request;
        String response;
        String body;
            
        request = "GET /commons/ HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8082", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertFalse(body.contains("\r\nname:hidden.txt\r\n"));
        
        request = "GET /commons/hidden.txt HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8082", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("123", body);
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] INDEX = ON [S]}
     *  The file index includes all not hidden entries of a directory.
     *  Access to hidden files is allowed.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_17() throws Exception {
        
        String request;
        String response;
        String body;
            
        request = "GET /commons/ HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8083", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertFalse(body.contains("\r\nname:hidden.txt\r\n"));
        
        request = "GET /commons/hidden.txt HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8083", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("123", body);
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] INDEX = OFF [S]}
     *  The file index is not allowed. The request must be responded with
     *  status 403. Access to hidden files is allowed.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_18() throws Exception {
        
        String request;
        String response;
        String body;
            
        request = "GET /commons/ HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8084", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_403));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertFalse(body.contains("\r\nname:hidden.txt\r\n"));
        
        request = "GET /commons/hidden.txt HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8084", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("123", body);
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] METHODS = ALL}
     *  The alias ALL will no longer supported, because the Allow-Header can
     *  not be filled correctly. The request must be responded with status 405.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_19() throws Exception {
        
        String request = "GET / HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8091", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_405));
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_405));          
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] METHODS = XYZ ...}
     *  The method XYZ is included in the method list, but not implemented.
     *  The request must be responded with status 501.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_21() throws Exception {
        
        String request = "XYZ / HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8091", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_501));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_TEXT_HTML));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_501)); 
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] METHODS = HEAD, ALL 123}
     *  The method HEAD is supported, but GET not. The request must be
     *  responded with status 405.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_23() throws Exception {
        
        String request;
        String response;
        String accessLog;
        
        request = "GET / HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8090", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_405));
        
        Thread.sleep(50);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_405));  
        
        request = "HEAD / HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8090", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        
        Thread.sleep(50);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));          
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] METHODS = HEAD ALL 123}
     *  The method ALL 123 will no supported. The request must be responded
     *  with status 400.
     *  @throws Exception
     */  
    @Test
    public void testAceptance_24() throws Exception {
        
        String request = "ALL 123 / HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8090", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_400));
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_400));          
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] METHODS = ALL}
     *  The alias ALL will no longer supported, because the Allow-Header can
     *  not be filled correctly. The request must be responded with status 405.
     *  @throws Exception
     */  
    @Test
    public void testAceptance_25() throws Exception {
        
        String request = "HEAD / HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8092", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_405));
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_405));          
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] METHODS = ALL get}
     *  In the case of status 405, the response header ALLOW must be set
     *  correctly.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_26() throws Exception {
        
        String request = "XYZ / HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8092", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_405));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_ALLOW("ALL", "GET")));
        
        Thread.sleep(50);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_405));          
    }
    
    private void onBeforeTestAceptance_27() {
        
        System.setProperty("env_PARAM_D", "abc");
        System.setProperty("env_PARAM_E", "");
        System.setProperty("env_PARAM_G", "def");
        System.setProperty("env_PARAM_J", "");
        
        Service.restart();
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] ENV_PARAM_A = }
     *  The value of ENV_PARAM_A is static and empty. Section works smart and
     *  therefore the variable does not exist.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_27() throws Exception {
        
        String request;
        String response;
        String body;
        
        request = "GET /env.test?exist=ENV_PARAM_A HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("false", body);
        
        request = "GET /env.test?value=ENV_PARAM_A HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("", body);
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] ENV_PARAM_B = 123}
     *  The value of ENV_PARAM_C is static and is used normally.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_28() throws Exception {
        
        String request;
        String response;
        String body;
        
        request = "GET /env.test?exist=ENV_PARAM_B HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("true", body);
        
        request = "GET /env.test?value=ENV_PARAM_B HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("123", body);
    }

    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] ENV_PARAM_C [?] = 456}
     *  The value of ENV_PARAM_C is dynamically. Bcause no corresponding
     *  environment variable exists, the default value 456 is used.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_29() throws Exception {
        
        String request;
        String response;
        String body;
        
        request = "GET /env.test?exist=ENV_PARAM_C HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("true", body);
        
        request = "GET /env.test?value=ENV_PARAM_C HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("456", body);
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] ENV_PARAM_D [?] = 789}
     *  The value of ENV_PARAM_D is dynamically and is set by the corresponding
     *  environment variable. The default value 789 is not used.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_30() throws Exception {
        
        String request;
        String response;
        String body;
        
        request = "GET /env.test?exist=ENV_PARAM_D HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("true", body);
        
        request = "GET /env.test?value=ENV_PARAM_D HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("abc", body);
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] ENV_PARAM_E [?]}
     *  The value of ENV_PARAM_F is dynamically, but the corresponding
     *  environment variable is empty. Section works smart and therefore the
     *  variable does not exist.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_31() throws Exception {
     
        String request;
        String response;
        String body;
        
        request = "GET /env.test?exist=ENV_PARAM_E HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("false", body);
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] ENV_PARAM_F [?]}
     *  The value of ENV_PARAM_F is dynamically, but there is no corresponding
     *  environment variable. Section works smart and therefore the variable
     *  does not exist.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_32() throws Exception {
     
        String request;
        String response;
        String body;
        
        request = "GET /env.test?exist=ENV_PARAM_F HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("false", body);
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] ENV_PARAM_G [?]}
     *  The value of ENV_PARAM_G is dynamically and is set by value of the
     *  environment variable.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_33() throws Exception {
        
        String request;
        String response;
        String body;
        
        request = "GET /env.test?exist=ENV_PARAM_G HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("true", body);
        
        request = "GET /env.test?value=ENV_PARAM_G HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("def", body);
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] ENV_PARAM_H = xyz [?]}
     *  The function {@code [?]} at the end of the line is ignored and is a
     *  part of the value.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_34() throws Exception {
        
        String request;
        String response;
        String body;
        
        request = "GET /env.test?exist=ENV_PARAM_H HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("true", body);
        
        request = "GET /env.test?value=ENV_PARAM_H HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("xyz [?]", body);
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] ENV_PARAM_I = uvw [+]}
     *  The function {@code [+]} at the end of the line is ignored and is a
     *  part of the value.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_35() throws Exception {
        
        String request;
        String response;
        String body;
        
        request = "GET /env.test?exist=ENV_PARAM_I HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("true", body);
        
        request = "GET /env.test?value=ENV_PARAM_I HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("uvw [+]", body);
    }

    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] ENV_PARAM_J [?] = xxx}
     *  The parameter is overwritten with an empty environment variable.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_36() throws Exception {
        
        String request;
        String response;
        String body;
        
        request = "GET /env.test?exist=ENV_PARAM_J HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("false", body);
        
        request = "GET /env.test?value=ENV_PARAM_J HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("", body);
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [COMMONS] RELOAD = ON}
     *  If the configuration file is changed, the server must restart and load
     *  the new configuration.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_37() throws Exception {
        
        String request;
        String response;
        
        try {
            request = "GET / HTTP/1.0\r\n"
                    + "\r\n";
            response = new String(TestHttpUtils.sendRequest("127.0.0.1:8999", request));
            Assert.fail();
        } catch (Exception exception) {
            Assert.assertTrue(exception instanceof ConnectException);
        }
        
        File configFile = new File(TestUtils.getRootStage().getParentFile(), "devwex.ini");
        try (PrintWriter output = new PrintWriter(new FileOutputStream(configFile, true))) {
            output.print("\r\n");
            output.print("[SERVER:0:BAS] EXTENDS serVER:A:BAS\r\n");
            output.print("  PORT = 8999\r\n");
            output.print("[SERVER:0:REF] EXTENDS serVER:A:REF\r\n");
            output.print("[SERVER:0:ACC] EXTENDS serVER:A:ACC\r\n");
            output.print("[SERVER:0:CGI] EXTENDS serVER:A:CGI\r\n");
            output.print("[SERVER:0:ENV] EXTENDS serVER:A:ENV\r\n");
            output.print("[SERVER:0:FLT] EXTENDS serVER:A:FLT\r\n");
            output.print("[SERVER:0:MOD] EXTENDS serVER:A:MOD\r\n");
        }
        
        Thread.sleep(1500);
        
        request = "GET / HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8999", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
    }   
    
    //TODO: Testfall mit leerer + ungueltiger Konfiguration, der Server muss durchstarten aber mit der letzten geladenen Konfiguration o.ae.
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] ACCESS = OFF}
     *  The logging will be completely disabled.
     *  @throws Exception
     */   
    @Test
    public void testAceptance_38() throws Exception {
        
        Thread.sleep(50);

        String accessLog = TestUtils.getAccessLogTail();
        String outputLog = TestUtils.getOutputLogTail();

        Assert.assertFalse(new File(TestUtils.getRootStage().getParentFile(), "OfF").exists());
        
        String request = "GET / HTTP/1.0\r\n"
                + "Host: vHj\r\n"
                + "\r\n";
        TestHttpUtils.sendRequest("127.0.0.1:8080", request);

        Thread.sleep(50);
        Assert.assertEquals(accessLog, TestUtils.getAccessLogTail());
        Assert.assertEquals(outputLog, TestUtils.getOutputLogTail());
        
        Assert.assertFalse(new File(TestUtils.getRootStage().getParentFile(), "OfF").exists());
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:ENV] TESTINIT = ... + ...}
     *  The line function {@code +} must work correctly.
     *  The vlaue of the key TESTINIT must be assembled correctly.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_39() throws Exception {
        
        String request;
        String response;
        String body;
        
        request = "GET /env.test?exist=TESTINIT HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("true", body);
        
        request = "GET /env.test?value=TESTINIT HTTP/1.0\r\n"
                + "\r\n";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("003")));
        body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("com.seanox.devwex.Extension [*] ;xxx 123;olli ooo", body);
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] IDENTITY = ON}
     *  The server signature in the response header is activated.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_40() throws Exception {
        
        for (String request : new String[] {
                "GET \\cgi_environment.jsx?parameter=SERVER_NAME HTTP/1.0\r\nHost: vHa\r\n\r\n",
                "GET / HTTP/1.0\r\nHost: vHa\r\n\r\n",
                "GET /filter_a.html HTTP/1.0\r\nHost: vHa\r\n\r\n",
                "GET /nix HTTP/1.0\r\nHost: vHa\r\n\r\n",
                "HEAD \\cgi_environment.jsx?parameter=SERVER_NAME HTTP/1.0\r\nHost: vHa\r\n\r\n",
                "HEAD / HTTP/1.0\r\nHost: vHa\r\n\r\n",
                "HEAD /filter_a.html HTTP/1.0\r\nHost: vHa\r\n\r\n",
                "HEAD /nix HTTP/1.0\r\nHost: vHa\r\n\r\n"}) {
            String response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_SERVER_DIFFUSE));
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_DATE));
        }
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] IDENTITY = ON}
     *  The server signature in the response header is disabled.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_41() throws Exception {
        
        for (String request : new String[] {
                "GET \\cgi_environment.jsx?parameter=SERVER_NAME HTTP/1.0\r\n\r\n",
                "GET / HTTP/1.0\r\n\r\n",
                "GET /filter_a.html HTTP/1.0\r\n\r\n",
                "GET /nix HTTP/1.0\r\n\r\n",
                "HEAD \\cgi_environment.jsx?parameter=SERVER_NAME HTTP/1.0\r\n\r\n",
                "HEAD / HTTP/1.0\r\n\r\n",
                "HEAD /filter_a.html HTTP/1.0\r\n\r\n",
                "HEAD /nix HTTP/1.0\r\n\r\n"}) {
            String response = new String(TestHttpUtils.sendRequest("127.0.0.1:80", request));
            Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_SERVER_DIFFUSE));
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_DATE));
        }
    }
}