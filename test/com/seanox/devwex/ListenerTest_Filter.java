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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

/**
 *  TestCases for {@link com.seanox.devwex.Listener}.
 */
public class ListenerTest_Filter extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  Without 'Feld-C' the request is responded with status 403.
     *  filter expression: {@code GET IS EMPTY FELD-C}
     *  @throws Exception
     */
    @Test
    public void testAceptance_01() throws Exception {
        
        String request;
        String response;
        
        request = "GET / HTTP/1.0";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));
        
        request = "GET / HTTP/1.0\r\n"
                + "Feld-C: 123";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
    }    
    
    /** 
     *  TestCase for aceptance.
     *  The filter method ALL is working for all methods, also unknown methods.
     *  The request is responded with status 403.
     *  filter expression: {@code ALL IS CONTAINS REQUEST ABC}
     *  @throws Exception
     */
    @Test
    public void testAceptance_02() throws Exception {
        
        String request;
        String response;
        
        request = "GET /xxxAbCxxx HTTP/1.0\r\n"
                + "FELD-C: xxx\r\n"
                + "FELD-A: xxx";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));
        
        request = "GET /xxxAbxxx HTTP/1.0\r\n"
                + "FELD-C: xxx\r\n"
                + "FELD-A: xxx";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 404\\s+\\w+.*$"));
        
        request = "HEAD /xxxAbCxxx HTTP/1.0\r\n"
                + "FELD-C: xxx\r\n"
                + "FELD-A: xxx";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));
        
        request = "HEAD /xxxAbxxx HTTP/1.0\r\n"
                + "FELD-C: xxx\r\n"
                + "FELD-A: xxx";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 404\\s+\\w+.*$"));        
        
        request = "INVALID_METHOD /xxxAbCxxx HTTP/1.0\r\n"
                + "FELD-C: xxx\r\n"
                + "FELD-A: xxx";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));  
        
        request = "INVALID_METHOD /xxxAbxxx HTTP/1.0\r\n"
                + "FELD-C: xxx\r\n"
                + "FELD-A: xxx";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 405\\s+\\w+.*$"));  
    }  
    
    /** 
     *  TestCase for aceptance.
     *  If 'FeldA' contains 'A1', the HEAD request is responded with status 403
     *  but the GET request is responded with status 200.
     *  filter expression: {@code HEAD IS CONTAINS FELDA A1}
     *  @throws Exception
     */
    @Test
    public void testAceptance_03() throws Exception {
        
        String request;
        String response;
        
        request = "HEAD / HTTP/1.0\r\n"
                + "Felda: BA12\r\n"
                + "Feld-C: 123";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));
        
        request = "GET / HTTP/1.0\r\n"
                + "Felda: BA12\r\n"
                + "Feld-C: 123";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));        
    } 
    
    /** 
     *  TestCase for aceptance.
     *  If 'FeldA' ends not with 'A2', the request is responded with status 200
     *  but with 'A2' at the end, the request is responded with status 403.
     *  filter expression: {@code HEAD IS ENDS FELDA A2}
     *  @throws Exception
     */
    @Test
    public void testAceptance_04() throws Exception {
        
        String request;
        String response;
        
        request = "HEAD / HTTP/1.0\r\n"
                + "Felda: BA21\r\n"
                + "Feld-C: 123";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        
        request = "HEAD / HTTP/1.0\r\n"
                + "Felda: 1BA2\r\n"
                + "Feld-C: 123";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));   
    }
    
    /** 
     *  TestCase for aceptance.
     *  The filter must also work with encoded special characters (MIME/UFT8).
     *  The request is responded with status 403.
     *  filter expression: {@code GET IS CONTAINS PATH  -ß-}
     *  @throws Exception
     */
    @Test
    public void testAceptance_05() throws Exception {
        
        String request = "GET /xxx-%df%c3%9f- HTTP/1.0\r\n\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));  
    } 
    
    /** 
     *  TestCase for aceptance.
     *  If 'FeldA' starts not with 'A3', the request is responded with status
     *  200 but with 'A3' at the beginn, the request is responded with status
     *  403.
     *  filter expression: {@code HEAD IS STARTS FELDA A3}
     *  @throws Exception
     */
    @Test
    public void testAceptance_06() throws Exception {
        
        String request;
        String response;
        
        request = "HEAD / HTTP/1.0\r\n"
                + "Felda: BA3\r\n"
                + "Feld-C: 123";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        
        request = "HEAD / HTTP/1.0\r\n"
                + "Felda: A3B\r\n"
                + "Feld-C: 123";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));   
    }  
    
    /** 
     *  TestCase for aceptance.
     *  The filter must also work with encoded special characters (MIME/UFT8).
     *  The request is responded with status 403.
     *  filter expression: {@code GET IS CONTAINS PATH  -ß-}
     *  @throws Exception
     */
    @Test
    public void testAceptance_07() throws Exception {
        
        String request = "GET /xxx-%c3%9f- HTTP/1.0\r\n\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));  
    }
    
    /** 
     *  TestCase for aceptance.
     *  If 'FeldA' equals not 'A4', the request is responded with status 200
     *  but 'FeldA' equals 'A4', the request is responded with status 403.
     *  filter expression: {@code HEAD IS EQUALS FELDA A4}
     *  @throws Exception
     */
    @Test
    public void testAceptance_08() throws Exception {
        
        String request;
        String response;
        
        request = "HEAD / HTTP/1.0\r\n"
                + "Felda: BA4\r\n"
                + "Feld-C: 123";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        
        request = "HEAD / HTTP/1.0\r\n"
                + "Felda: a4\r\n"
                + "Feld-C: 123";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));   
    }  
    
    /** 
     *  TestCase for aceptance.
     *  The filter must also work with encoded special characters (MIME/UFT8).
     *  The request is responded with status 403.
     *  filter expression: {@code GET IS CONTAINS PATH  -ß-}
     *  @throws Exception
     */
    @Test
    public void testAceptance_09() throws Exception {
        
        String request = "GET /xxx-%c3%9f%df- HTTP/1.0\r\n\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));  
    }
    
    /** 
     *  TestCase for aceptance.
     *  If 'FeldA' equals not 'A4', the request is responded with status 200
     *  but 'FeldA' equals 'A4', the request is responded with status 403.
     *  filter expression: {@code HEAD IS EQUALS FELDA A4}
     *  @throws Exception
     */
    @Test
    public void testAceptance_10() throws Exception {
        
        String request;
        String response;
        
        request = "GET / HTTP/1.0\r\n"
                + "Felda: B1\r\n"
                + "Feld-C: 123";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));        

        request = "GET / HTTP/1.0\r\n"
                + "Feldb: B2\r\n"
                + "Feld-C: 123";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        
        request = "GET / HTTP/1.0\r\n"
                + "Feldb: B2\r\n"
                + "Felda: B1\r\n"
                + "Feld-C: 123";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));   
    } 
    
    /** 
     *  TestCase for aceptance.
     *  The filter must also work with encoded special characters (MIME/UFT8).
     *  The request is responded with status 403.
     *  filter expression: {@code GET IS CONTAINS PATH  -ß-}
     *  @throws Exception
     */
    @Test
    public void testAceptance_11() throws Exception {
        
        String request = "GET /xxx-%DF- HTTP/1.0\r\n\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));  
    }
    
    /** 
     *  TestCase for aceptance.
     *  The filter must also work with encoded special characters (MIME/UFT8).
     *  The request is responded with status 403.
     *  filter expression: {@code GET IS CONTAINS PATH  -ß-}
     *  @throws Exception
     */
    @Test
    public void testAceptance_12() throws Exception {
        
        String request = "GET /xxx-ß- HTTP/1.0\r\n\r\n";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));  
    } 
    
    /** 
     *  TestCase for aceptance.
     *  If the request contains {@code Felda: t1} and {@code Felda: field-C},
     *  the request is responded with status 200 and the content of
     *  {@code ../documents/filter_a.html}.
     *  filter expression: {@code GET IS CONTAINS FELDA T1  > ./stage/documents/filter_a.html}
     *  @throws Exception
     */
    @Test
    public void testAceptance_13() throws Exception {
        
        String request = "GET / HTTP/1.0\r\n"
                + "Felda: t1\r\n"
                + "Feld-C: 123";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));  
        Assert.assertTrue(response.contains("filter_a.html"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  If the request contains {@code Felda2: t1} the request is responded
     *  with status 403 because the target {@code ../documents/filter_a_.html}
     *  does not exists.
     *  filter expression: {@code GET IS CONTAINS FELDA2 T1 > ./stage/documents/filter_a.html_}
     *  @throws Exception
     */
    @Test
    public void testAceptance_14() throws Exception {
        
        String request = "GET / HTTP/1.0\r\n"
                + "Felda2: t1\r\n"
                + "Feld-C: 123";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));  
    }   
    
    /** 
     *  TestCase for aceptance.
     *  If the request contains {@code Felda: t2} the request is responded with
     *  status 302 and a redirect/location to {@code http://www.xxx.zz/a=1}.
     *  filter expression: {@code GET IS CONTAINS FELDA T2  > http://www.xxx.zz/a=1 [r]}
     *  @throws Exception
     */
    @Test
    public void testAceptance_15() throws Exception {
        
        String request = "GET / HTTP/1.0\r\n"
                + "Felda: t2\r\n"
                + "Feld-C: 123";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 302\\s+\\w+.*$"));  
        
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.contains("\r\nLocation: http://www.xxx.zz/a=1\r\n"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s302\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  If 'FeldA' equals not '3B1BB2', the request is responded with status
     *  200 but 'FeldA' equals '3B1BB2', the request is responded with status
     *  403.
     *  filter expression: {@code GET NOT EQUALS FELDA 3B1BB2}
     *  @throws Exception
     */
    @Test
    public void testAceptance_16() throws Exception {
        
        String request;
        String response;
        
        request = "GET / HTTP/1.0\r\n"
                + "Felda: 3B1BB2";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8087", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));  

        request = "GET / HTTP/1.0\r\n"
                + "Felda: 3BBB2";
        response = new String(TestHttpUtils.sendRequest("127.0.0.1:8087", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));  
    }  
    
    /** 
     *  TestCase for aceptance.
     *  If the request contains {@code Felda: t4} the request is responded with
     *  status 403 because for the redirect is the option {@code [R]} is
     *  missing.
     *  filter expression: {@code GET IS CONTAINS FELDA T4  > http://www.xxx.zz/a=1}
     *  @throws Exception
     */
    @Test
    public void testAceptance_17() throws Exception {
        
        String request = "GET / HTTP/1.0\r\n"
                + "Felda: t4\r\n"
                + "Feld-C: 123";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));  
        Assert.assertFalse(response.matches("(?si)^.*\\sLocation:.*$"));
    }  
    
    /** 
     *  TestCase for aceptance.
     *  If the request contains {@code Felda: t6} and {@code Feldb: t7} the
     *  request is responded with status 200 and the content of
     *  {@code ../documents/filter_a.html}.
     *  filter expression: {@code GET IS CONTAINS FELDA T6[A] GET IS CONTAINS FELDB T7 > ./stage/documents/filter_a.html}
     *  @throws Exception
     */
    @Test
    public void testAceptance_18() throws Exception {
        
        String request = "GET / HTTP/1.0\r\n"
                + "Felda: t6\r\n"
                + "Feldb: t7\r\n"
                + "Feld-C: 123";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));  
        Assert.assertTrue(response.contains("filter_a.html"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  If the request contains {@code Felda: t5}, then a module should be
     *  used. But the module definition is incorrect, the option {@code [M]} is
     *  missing here. So the request is responded with status 403.
     *  filter expression: {@code GET IS CONTAINS FELDA T5  > ConnectorA}
     *  @throws Exception
     */
    @Test
    public void testAceptance_19() throws Exception {
        
        String request = "GET / HTTP/1.0\r\n"
                + "Felda: t5\r\n"
                + "Feld-C: 123";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));  
        Assert.assertFalse(response.matches("(?si)^.*\\sLocation:.*$"));
    }  
    
    /** 
     *  TestCase for aceptance.
     *  If the request contains {@code Felda: t3}, then a module should be
     *  used. So the request is responded with status 001.
     *  filter expression: {@code GET IS CONTAINS FELDA T3  > ConnectorA [pA=3] [m]}
     *  @throws Exception
     */
    @Test
    public void testAceptance_20() throws Exception {
        
        String request = "GET / HTTP/1.0\r\n"
                + "Felda: t3\r\n"
                + "Feld-C: 123";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8086", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 001 Test ok\\s+\\w+.*$"));
        Assert.assertTrue(response.matches("(?s)^.*\r\nModul: ConnectorA\r\n.*$"));
        Assert.assertTrue(response.matches("(?s)^.*\r\nModultype: 1\r\n.*$"));
        Assert.assertTrue(response.matches("(?s)^.*\r\nOpts: ConnectorA \\[pA=3\\] \\[m\\]\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s1\\s\\d+\\s-\\s-$"));        
    }
    
    /** 
     *  TestCase for aceptance.
     *  VHP has three ALWAYS filters.
     *  All filters are executed because no filter changes the server status.
     *  The request is responded with status 200 and a test file is created in
     *  DocRoot. Each filter increases the value in the file. Finally, the file
     *  must contain the value "3".
     *  filter expression: {@code ALL ALWAYS > ConnectorE [M]}
     *  @throws Exception
     */
    @Test
    public void testAceptance_21() throws Exception {
        
        String docRoot = TestUtils.getRootStage().toString() + "/documents_vh_P";
        Path testFile = Paths.get(docRoot, "test.txt");
        if (Files.exists(testFile))
            Files.delete(testFile);
        Assert.assertFalse(Files.exists(testFile));
        
        String request = "GET / HTTP/1.0\r\n"
                + "Host: vHp";
        String response = new String(TestHttpUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));  
        Assert.assertTrue(Files.exists(testFile));
        String fileContent = new String(Files.readAllBytes(testFile));
        Assert.assertEquals("3", fileContent);
    }    
}