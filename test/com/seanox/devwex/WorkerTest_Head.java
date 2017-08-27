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
import java.util.StringTokenizer;

import org.junit.Assert;
import org.junit.Test;

import com.seanox.test.utils.HttpUtils;
import com.seanox.test.utils.Pattern;
import com.seanox.test.utils.HttpUtils.HeaderField;

/**
 *  TestCases for {@link com.seanox.devwex.Worker}.
 */
public class WorkerTest_Head extends AbstractTest {
    
    /** 
     *  TestCase for acceptance.
     *  Directories are responded with status 200 and without Content-Length.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_01() throws Exception {
        
        String request = "HEAD / HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
    } 
    
    /** 
     *  TestCase for acceptance.
     *  Directories without a slah at the end, are responded with status 302
     *  and a redirect.
     *  @throws Exception
     */       
    @Test
    public void testAcceptance_02() throws Exception {
        
        String request = "HEAD /test_a HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
    
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_302));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8080/test_a/")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
    
        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_302));
    } 
    
    /** 
     *  TestCase for acceptance.
     *  Directories these not exists, are responded with status 404.
     *  @throws Exception
     */   
    @Test
    public void testAcceptance_03() throws Exception {
        
        String request = "HEAD /test_ax HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
    
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_404));
    }
    
    /** 
     *  TestCase for acceptance.
     *  Files are responded with status 200, Content-Length, Content-Type and
     *  LastModified.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_04() throws Exception {
        
        String request = "HEAD /method_file.txt HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED));
    }
    
    /** 
     *  TestCase for acceptance.
     *  Files with a slah at the end, are responded with status 302 and a
     *  redirect.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_05() throws Exception {
        
        String request = "HEAD /method_file.txt/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_302));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LOCATION("http://vHa:8080/method_file.txt")));

        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_302));
    }
    
    /** 
     *  TestCase for acceptance.
     *  Requests with a correct If-Modified-Since are responded with status
     *  304.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_06() throws Exception {
        
        String request;
        String response;        
        
        request = "Get /documents/commons/lastmodified.jsx HTTP/1.0\r\n"
                + "File: ../../documents_vh_A/method_file.txt\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        String lastModified = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        
        request = "HEAD /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: " + lastModified + "\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_304));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LOCATION_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_304));
    }
    
    /** 
     *  TestCase for acceptance.
     *  Requests with a incorrect If-Modified-Since are responded with status
     *  200.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_07() throws Exception {
        
        String request = "HEAD /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: Mon, 19 Jan 2004 16:58:55 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
    }
    
    /** 
     *  TestCase for acceptance.
     *  Requests with a correct If-Modified-Since (incl. length) are responded
     *  with status 304.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_08() throws Exception {

        String request;
        String response;        
        
        request = "Get /documents/commons/lastmodified.jsx HTTP/1.0\r\n"
                + "File: ../../documents_vh_A/method_file.txt\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        String lastModified = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");

        request = "HEAD /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: " + lastModified + "; xxx; length=15\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_304));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LOCATION_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));

        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_304));        
    }
    
    /** 
     *  TestCase for acceptance.
     *  Requests with a If-Modified-Since (correct date, invalid length) are
     *  responded with status 200.
     *  @throws Exception
     */   
    @Test
    public void testAcceptance_09() throws Exception {

        String request;
        String response;        
        
        request = "Get /documents/commons/lastmodified.jsx HTTP/1.0\r\n"
                + "File: ../../documents_vh_A/method_file.txt\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        String lastModified = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");

        request = "HEAD /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: " + lastModified + "; xxx; length=20\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));    
    }
    
    /** 
     *  TestCase for acceptance.
     *  Requests with an other If-Modified-Since are responded with status 200.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_10() throws Exception {
        
        String request = "HEAD /method_file.txt HTTP/1.0\r\n"
                + "If-Modified-Since: Mon, 19 Jan 2004 16:58:57 GMT; xxx; length=20\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
    }
    
    /** 
     *  TestCase for acceptance.
     *  Requested directories with a default page are responded with status 200
     *  and the details of the default page.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_11() throws Exception {
        
        String request = "HEAD /test_a/ HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
    }
    
    /** 
     *  TestCase for acceptance.
     *  The If-Modified-Since is ignored for directories.
     *  The request are responded with status 200.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_12() throws Exception {
        
        String request;
        String response;        
        
        request = "Get /documents/commons/lastmodified.jsx HTTP/1.0\r\n"
                + "File: ../../documents_vh_A/test_d\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        String lastModified = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");

        request = "HEAD /test_d/ HTTP/1.0\r\n"
                + "If-Modified-Since: " + lastModified + "\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_TEXT_HTML));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
    }
    
    /** 
     *  TestCase for acceptance.
     *  The If-Modified-Since is ignored for directories.
     *  The request are responded with status 403, because the access is
     *  forbidden.
     *  @throws Exception
     */       
    @Test
    public void testAcceptance_13() throws Exception {
        
        String request = "HEAD /forbidden HTTP/1.0\r\n"
                + "If-Modified-Since: Mon, 11 Jan 2004 19:11:58 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_403));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
    }
    
    /** 
     *  TestCase for acceptance.
     *  The If-Modified-Since is ignored for absolutes paths.
     *  The request are responded with status 200.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_14() throws Exception {
        
        String request = "HEAD /absolute HTTP/1.0\r\n"
                + "If-Modified-Since: Mon, 11 Jan 2004 19:11:58 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
    }
    
    /** 
     *  TestCase for acceptance.
     *  The If-Modified-Since is ignored for absolutes paths.
     *  The request are responded with status 200.
     *  @throws Exception
     */       
    @Test
    public void testAcceptance_15() throws Exception {
        
        String request = "HEAD /absolutexxx HTTP/1.0\r\n"
                + "If-Modified-Since: Mon, 11 Jan 2004 19:11:58 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
    }
    
    /** 
     *  TestCase for acceptance.
     *  The If-Modified-Since is ignored for the CGI.
     *  The request are responded with status 403, because the CGI does not
     *  exists.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_16() throws Exception {
        
        String request = "HEAD /method.php HTTP/1.0\r\n"
                + "If-Modified-Since: Mon, 11 Jan 2004 19:11:58 GMT\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_403));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
    }
    
    /** 
     *  TestCase for acceptance.
     *  The request are responded with status 200 by the CGI self, if the HEAD
     *  method allowed.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_17() throws Exception {
        
        String request = "hEAD /method.jsx HTTP/1.0\r\n"
                + "Host: vHb\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertEquals("hallo", body);
    }
    
    private static void assertAcceptance_18(int count, String path, String start, String end) throws Exception {
        
        if (start != null
                && start.contains("-")
                && end == null)
            end = "";
        if (end != null
                && end.contains("-")
                && start == null)
            start = "";

        String request = "Head " + path + " HTTP/1.0\r\n"
                + "Host: vHa\r\n";
        if (start != null || end != null) {
            request += "Range: bYteS = " + (start != null ? start : "");
            if (start != null && end != null)
                request += count % 2 == 0 ? "-" : " - ";
            request += end != null ? end : "";
            request += "\r\n";
        }
        
        if (start != null && start.contains(";"))
            end = null;
        
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request + "\r\n"));
        
        int code = 0;
        
        if (path.contains("nix")) {
        
            code = 404;
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_404));
            Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
            Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_RANGE_DIFFUSE));  
            
        } else if (path.equals("/")) {
            
            code = 200;
            Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
            Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
            Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_RANGE_DIFFUSE));             
        
        } else {
            
            code = 206;
            
            long fileSize     = new File(AbstractSuite.getRootStage(), "documents_vh_A/" + path).length();
            long responseSize = fileSize;
            long startPos     = 0;
            long endPos       = fileSize -1;     

            String range = "";
            if (start != null)
                range += " " + start;
            if (start != null && end != null)
                range += "-";
            if (end != null)
                range += " " + end;
            range = range.replaceAll(";.*$", "").trim();
            
            if (range.matches("^(\\d+)*\\s*-\\s*(\\d+)*$")) {
                StringTokenizer tokenizer = new StringTokenizer(range, "-");
                startPos = Long.parseLong(tokenizer.nextToken().trim());
                if (tokenizer.hasMoreTokens()) {
                    endPos = Long.parseLong(tokenizer.nextToken().trim());
                } else if (range.startsWith("-")) {
                    if (startPos > 0) {
                        endPos   = fileSize -1;
                        startPos = Math.max(0, fileSize -startPos);
                    } else endPos = -1;
                }
                endPos = Math.min(endPos, fileSize -1);
                if (startPos >= fileSize) {
                    code = 416;
                } else if (startPos < fileSize
                        && startPos <= endPos) {
                    endPos++;
                } else {
                    startPos = 0;
                    endPos   = fileSize;
                    code     = 200;
                }
                
                responseSize = Math.min(fileSize, Math.max(0, endPos -startPos));
            }
            
            if (start != null)
                start = start.replaceAll("\\s*;.*$", "");
            if (end != null)
                end = end.replaceAll("\\s*;.*$", "");

            if (start == null
                    || end == null)
                code = 200;
            if (start != null
                    && start.trim().isEmpty()
                    && endPos == 0)
                code = 200;
            if (startPos > endPos)
                code = 200; 
            if (startPos >= fileSize
                    && fileSize > 0
                    && start != null
                    && end != null)
                code = 416;            
            if (start != null
                    && !start.matches("^\\d+$")
                    && !start.trim().isEmpty())
                code = 200;  
            if (end != null
                    && !end.matches("^\\d+$")
                    && !end.trim().isEmpty())
                code = 200;  

            if (code != 206)
                responseSize = fileSize;  
            
            if (code == 206) {
                Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_206));
                Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH(responseSize)));
                Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_RANGE(startPos, endPos -1, fileSize)));
            } else if (code == 416) {
                Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_416));
                Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
                Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_RANGE_DIFFUSE));
            } else {
                Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
                Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH(fileSize)));
                Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_RANGE_DIFFUSE));
            }
        }
        
        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS(String.valueOf(code), request, 0)));  
    }
    
    /** 
     *  TestCase for acceptance.
     *  The correct response for HEAD request with a Range header is checked.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_18() throws Exception {
        
        for (String path : new String[] {"/partial_content.txt", "/partial_content_empty.txt",
                "/partial_content-nix.txt", "/"}) {

            int count = 0;

            WorkerTest_Head.assertAcceptance_18(++count, path, "0",      "0");
            WorkerTest_Head.assertAcceptance_18(++count, path, "0",      "1");    
            WorkerTest_Head.assertAcceptance_18(++count, path, "0",      "127");
            WorkerTest_Head.assertAcceptance_18(++count, path, "0",      "65535");
            WorkerTest_Head.assertAcceptance_18(++count, path, "1",      "0");
            WorkerTest_Head.assertAcceptance_18(++count, path, "1",      "1");
            WorkerTest_Head.assertAcceptance_18(++count, path, "1",      "127");    
            WorkerTest_Head.assertAcceptance_18(++count, path, "1",      "65535");
            WorkerTest_Head.assertAcceptance_18(++count, path, "127",    "256");
            WorkerTest_Head.assertAcceptance_18(++count, path, "256",    "127");
    
            WorkerTest_Head.assertAcceptance_18(++count, path, "127",    "0");
            WorkerTest_Head.assertAcceptance_18(++count, path, "127",    "1");
            WorkerTest_Head.assertAcceptance_18(++count, path, "65535",  "0");
            WorkerTest_Head.assertAcceptance_18(++count, path, "65535",  "1");
            WorkerTest_Head.assertAcceptance_18(++count, path, "256",    "65535");
            WorkerTest_Head.assertAcceptance_18(++count, path, "65535",  "256");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-256",   "127");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-127",   "256");
            WorkerTest_Head.assertAcceptance_18(++count, path, "256",    "-127");
            WorkerTest_Head.assertAcceptance_18(++count, path, "127",    "-256");
    
            WorkerTest_Head.assertAcceptance_18(++count, path, "0",      "A");
            WorkerTest_Head.assertAcceptance_18(++count, path, "1",      "A");
            WorkerTest_Head.assertAcceptance_18(++count, path, "256",    "B");
            WorkerTest_Head.assertAcceptance_18(++count, path, "65535",  "C");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-0",     "A");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-1",     "A");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-256",   "B");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-65535", "C");
            WorkerTest_Head.assertAcceptance_18(++count, path, "A",      "0");
            WorkerTest_Head.assertAcceptance_18(++count, path, "A",      "1");
    
            WorkerTest_Head.assertAcceptance_18(++count, path, "B",      "256");
            WorkerTest_Head.assertAcceptance_18(++count, path, "C",      "65535");
            WorkerTest_Head.assertAcceptance_18(++count, path, "A",      "-0");
            WorkerTest_Head.assertAcceptance_18(++count, path, "A",      "-1");
            WorkerTest_Head.assertAcceptance_18(++count, path, "B",      "-256");
            WorkerTest_Head.assertAcceptance_18(++count, path, "C",      "-65535");
            WorkerTest_Head.assertAcceptance_18(++count, path, "0",      "");
            WorkerTest_Head.assertAcceptance_18(++count, path, "256",    "");
            WorkerTest_Head.assertAcceptance_18(++count, path, "65535",  "");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-0",     "");
    
            WorkerTest_Head.assertAcceptance_18(++count, path, "-1",     "");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-256",   "");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-65535", "");
            WorkerTest_Head.assertAcceptance_18(++count, path, null,     "0");
            WorkerTest_Head.assertAcceptance_18(++count, path, null,     "256");
            WorkerTest_Head.assertAcceptance_18(++count, path, null,     "65535");
            WorkerTest_Head.assertAcceptance_18(++count, path, null,     "A");
            WorkerTest_Head.assertAcceptance_18(++count, path, null,      null);
            WorkerTest_Head.assertAcceptance_18(++count, path, "",       "0");
            WorkerTest_Head.assertAcceptance_18(++count, path, "",       "256");
           
            WorkerTest_Head.assertAcceptance_18(++count, path, "",       "65535");
            WorkerTest_Head.assertAcceptance_18(++count, path, "",       "-0");
            WorkerTest_Head.assertAcceptance_18(++count, path, "",       "-1");
            WorkerTest_Head.assertAcceptance_18(++count, path, "",       "-256");
            WorkerTest_Head.assertAcceptance_18(++count, path, "",       "-65535");
            WorkerTest_Head.assertAcceptance_18(++count, path, "0",      " ");
            WorkerTest_Head.assertAcceptance_18(++count, path, "1",      " ");
            WorkerTest_Head.assertAcceptance_18(++count, path, "256",    " ");
            WorkerTest_Head.assertAcceptance_18(++count, path, "65535",  " ");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-0",     " ");
            
            WorkerTest_Head.assertAcceptance_18(++count, path, null,       "-0");
            WorkerTest_Head.assertAcceptance_18(++count, path, null,       "-1");
            WorkerTest_Head.assertAcceptance_18(++count, path, null,       "-256");
            WorkerTest_Head.assertAcceptance_18(++count, path, null,       "-65535");
            WorkerTest_Head.assertAcceptance_18(++count, path, "0",      null);
            WorkerTest_Head.assertAcceptance_18(++count, path, "1",      null);
            WorkerTest_Head.assertAcceptance_18(++count, path, "256",    null);
            WorkerTest_Head.assertAcceptance_18(++count, path, "65535",  null);
            WorkerTest_Head.assertAcceptance_18(++count, path, "-0",     null);  
            WorkerTest_Head.assertAcceptance_18(++count, path, null,    "65535");
            WorkerTest_Head.assertAcceptance_18(++count, path, null,  "256");
            WorkerTest_Head.assertAcceptance_18(++count, path, null,   "127");            
            
            WorkerTest_Head.assertAcceptance_18(++count, path, "-1",     " ");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-256",   " ");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-65535", " ");
            WorkerTest_Head.assertAcceptance_18(++count, path, " ",      "0");
            WorkerTest_Head.assertAcceptance_18(++count, path, " ",      "1");
            WorkerTest_Head.assertAcceptance_18(++count, path, " ",      "256");
            WorkerTest_Head.assertAcceptance_18(++count, path, " ",      "65535");
            WorkerTest_Head.assertAcceptance_18(++count, path, " ",      "-0");
            WorkerTest_Head.assertAcceptance_18(++count, path, " ",      "-1");
            WorkerTest_Head.assertAcceptance_18(++count, path, " ",      "-256");
            
            WorkerTest_Head.assertAcceptance_18(++count, path, " ",      "-65535");
            WorkerTest_Head.assertAcceptance_18(++count, path, "0",      "-");
            WorkerTest_Head.assertAcceptance_18(++count, path, "1",      "-");
            WorkerTest_Head.assertAcceptance_18(++count, path, "256",    "-");
            WorkerTest_Head.assertAcceptance_18(++count, path, "65535",  "-");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-0",     "-");    
            WorkerTest_Head.assertAcceptance_18(++count, path, "-1",     "-");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-256",   "-");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-65535", "-");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-",      "0");
    
            WorkerTest_Head.assertAcceptance_18(++count, path, "-",      "256");    
            WorkerTest_Head.assertAcceptance_18(++count, path, "-",      "65535");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-",      "-0");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-",      "-1");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-",      "-256");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-",      "-65535");
            WorkerTest_Head.assertAcceptance_18(++count, path, "0",      ";");
            WorkerTest_Head.assertAcceptance_18(++count, path, "1",      ";");    
            WorkerTest_Head.assertAcceptance_18(++count, path, "256",    ";");
            WorkerTest_Head.assertAcceptance_18(++count, path, "65535",  ";");
            WorkerTest_Head.assertAcceptance_18(++count, path, "0;",      null);
            WorkerTest_Head.assertAcceptance_18(++count, path, "1;",      null);    
            WorkerTest_Head.assertAcceptance_18(++count, path, "256;",    null);
            WorkerTest_Head.assertAcceptance_18(++count, path, "65535;",  null);            
    
            WorkerTest_Head.assertAcceptance_18(++count, path, "-0",     ";");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-1",     ";");
            WorkerTest_Head.assertAcceptance_18(++count, path, "-256",   ";");    
            WorkerTest_Head.assertAcceptance_18(++count, path, "-65535", ";");
            WorkerTest_Head.assertAcceptance_18(++count, path, ";",      "0");
            WorkerTest_Head.assertAcceptance_18(++count, path, ";",      "1");
            WorkerTest_Head.assertAcceptance_18(++count, path, ";",      "256");
            WorkerTest_Head.assertAcceptance_18(++count, path, ";",      "65535");    
            WorkerTest_Head.assertAcceptance_18(++count, path, ";",      "-0");
            WorkerTest_Head.assertAcceptance_18(++count, path, ";",      "-1");
            
            WorkerTest_Head.assertAcceptance_18(++count, path, ";",      "-256");
            WorkerTest_Head.assertAcceptance_18(++count, path, ";",      "-65535");
            WorkerTest_Head.assertAcceptance_18(++count, path, "1",      "");
            WorkerTest_Head.assertAcceptance_18(++count, path, "",       "1");
        }
    }
    
    /** 
     *  TestCase for acceptance.
     *  The correct response for HEAD request with a Range,
     *  If-Modified-Since and If-UnModified-Since header is checked.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_21() throws Exception {
        
        for (String path : new String[] {"/partial_content.txt", "/partial_content_empty.txt",
                "/partial_content-nix.txt", "/"}) {
            
            String requestLastModified = "HEAD " + path + " HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "\r\n";
            String responseLastModified = new String(HttpUtils.sendRequest("127.0.0.1:8080", requestLastModified));       
            String lastModified = HttpUtils.getResponseHeaderValue(responseLastModified, HeaderField.LAST_MODIFIED);
            
            if (path.equals("/")) {
                requestLastModified = "Get /documents/commons/lastmodified.jsx HTTP/1.0\r\n"
                        + "File: ../../documents_vh_A\r\n"
                        + "Host: vHa\r\n"
                        + "\r\n";
                responseLastModified = new String(HttpUtils.sendRequest("127.0.0.1:8080", requestLastModified));
                lastModified = responseLastModified.replaceAll(Pattern.HTTP_RESPONSE, "$2");
            }
            
            for (String request : new String[] {
                    "HEAD " + path + "?304 HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "Range: bytes=2-10\r\n"
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n",
                    
                    "HEAD " + path + "?304 HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "Range: bytes=2-10\r\n"
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n",

                    "HEAD " + path + "?206 HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "Range: bytes=2-10\r\n"
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n",

                    "HEAD " + path + "?412 HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "Range: bytes=2-10\r\n"
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n",

                    "HEAD " + path + "?206 HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "Range: bytes=2-10\r\n"
                    + "If-UnModified-Since: " + lastModified + "\r\n\r\n",

                    "HEAD " + path + "?304 HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "Range: bytes=2-10\r\n"
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n"
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n",

                    "HEAD " + path + "?206 HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "Range: bytes=2-10\r\n"
                    + "If-UnModified-Since: " + lastModified + "\r\n"
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n",

                    "HEAD " + path + "?304 HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "Range: bytes=2-10\r\n"
                    + "If-UnModified-Since: " + lastModified + "\r\n"
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n",

                    "HEAD " + path + "?412 HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "Range: bytes=2-10\r\n"
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n"
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n",

                    "HEAD " + path + "?200 HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n",

                    "HEAD " + path + "?304 HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n",

                    "HEAD " + path + "?412 HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n",

                    "HEAD " + path + "?200 HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: " + lastModified + "\r\n\r\n",

                    "HEAD " + path + "?304 HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n"
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n",

                    "HEAD " + path + "?200 HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: " + lastModified + "\r\n"
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n",

                    "HEAD " + path + "?304 HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: " + lastModified + "\r\n"
                    + "If-Modified-Since: " + lastModified + "\r\n\r\n",

                    "HEAD " + path + "?412 HTTP/1.0\r\n"
                    + "Host: vHa\r\n"
                    + "If-UnModified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n"
                    + "If-Modified-Since: Thu, 07 Oct 1980 10:20:30 GMT\r\n\r\n"}) {
                
                String code = request.replaceAll("(?s)^.*?\\?(\\d+) .*$", "$1"); 

                if (path.equals("/partial_content_empty.txt")) {
                    request = request.replaceAll("(?s)(\r\nRange: bytes=)2-10(\r\n)", "$10-0$2");
                    if (code.equals("206"))
                        code = "200";
                }
                
                if (path.equals("/partial_content-nix.txt")) {
                    request = request.replaceAll("(?s)(\r\nRange: bytes=)2-10(\r\n)", "$2");
                    code = "404";
                }    
                
                if (path.equals("/")) {
                    request = request.replaceAll("(?s)(\r\nRange: bytes=)2-10(\r\n)", "$2");
                    code = "200";
                }

                String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
                Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS(code)));
            }            
        }
    }
}