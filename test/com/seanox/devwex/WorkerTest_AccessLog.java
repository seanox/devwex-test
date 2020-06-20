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

import java.io.File;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import com.seanox.test.utils.HttpUtils;
import com.seanox.test.utils.Pattern;

/**
 * Test cases for {@link com.seanox.devwex.Worker}.<br>
 * <br>
 * WorkerTest_AccessLog 5.2.0 20200410<br>
 * Copyright (C) 2020 Seanox Software Solutions<br>
 * All rights reserved.
 *
 * @author  Seanox Software Solutions
 * @version 5.2.0 20200410
 */
public class WorkerTest_AccessLog extends AbstractTest {
    
    /** 
     * Test case for acceptance.
     * The usage of time symbols in the file name must work.
     * @throws Exception
     */
    @Test
    public void testAcceptance_1() throws Exception {
        
        String accessLog = new SimpleDateFormat("'access-'yyyyMMdd'.log'").format(new Date());
        File accessLogFile = new File(AbstractSuite.getRootStage(), accessLog);
        accessLogFile.delete();
        
        String request = "GET / HTTP/1.0\r\n"
                + "Host: vHk\r\n"
                + "\r\n"; 
        String response = new String(HttpUtils.sendRequest("127.0.0.1:80", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        
        Thread.sleep(AbstractTest.SLEEP);
        Assert.assertTrue(accessLogFile.exists());
    }
    
    /** 
     * Test case for acceptance.
     * The time zone must be set correctly in the access log.
     * @throws Exception
     */
    @Test
    public void testAcceptance_2() throws Exception {
        
        String response = this.sendRequest("127.0.0.1:80", "GET / HTTP/1.0\r\n\r\n");
        String pattern = new SimpleDateFormat("Z", Locale.US).format(new Date());
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.matches("^.*?\\[\\d{2}/\\w{3}/\\d{4}(:\\d{2}){3} \\" + pattern + "\\]\\s.*$"));
    }   

    /** 
     * Test case for acceptance.
     * Special characters (\, ") must be escaped.
     * @throws Exception
     */
    @Test
    public void testAcceptance_3() throws Exception {
        
        String request;
        
        request = "G\"ET /nix\"xxx\"_zzz\u00FF HTTP/1.0\r\n"
                + "UUID: Nix\"123\"\r\n";
        HttpUtils.sendRequest("127.0.0.1:80", request + "\r\n");

        request = "GET / HTTP/1.0\r\n";
        String response = this.sendRequest("127.0.0.1:80", request + "\r\n");

        String accessLog = this.accessStreamCapture.toString();
        Assert.assertTrue(accessLog, accessLog.contains(" \"G\\\"ET /nix\\\"xxx\\\"_zzz\\xFF HTTP/1.0\" "));
        Assert.assertTrue(accessLog, accessLog.contains(" \"Nix\\\"123\\\""));
    }
    
    /** 
     * Test case for acceptance.
     * The usage of CGI variables in the file name must work.
     * @throws Exception
     */
    @Test
    public void testAcceptance_4() throws Exception {
        
        String accessLog = new SimpleDateFormat("'access-'yyyyMMdd'_vHl.log'").format(new Date());
        File accessLogFile = new File(AbstractSuite.getRootStage(), accessLog);
        accessLogFile.delete();
        
        String request = "GET / HTTP/1.0\r\n"
                + "Host: vHl\r\n"
                + "\r\n"; 
        String response = new String(HttpUtils.sendRequest("127.0.0.1:80", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        
        Thread.sleep(AbstractTest.SLEEP);
        Assert.assertTrue(accessLogFile.exists());
    }
    
    /** 
     * Test case for acceptance.
     * Unicode characters must be ignored as default ASCII.
     * @throws Exception
     */
    @Test
    public void testAcceptance_5() throws Exception {
        
        String request = "GET /nix_xxx_zzz\ud801\udc00 HTTP/1.0\r\n";
        String response = this.sendRequest("127.0.0.1:80", request + "\r\n");
        
        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.contains(" \"GET /nix_xxx_zzz? HTTP/1.0\" "));
    }    
    
    /** 
     * Test case for acceptance.
     * Unicode characters in UTF-8 must be escaped.
     * @throws Exception
     */
    @Test
    public void testAcceptance_6() throws Exception {
        
        String request = "GET /nix_xxx_zzz" + URLEncoder.encode("\ud801\udc00", "UTF-8") +  " HTTP/1.0\r\n";
        String response = this.sendRequest("127.0.0.1:80", request + "\r\n");
        
        String accessLog = this.accessStreamCaptureLine(ACCESS_LOG_RESPONSE_UUID(response));
        Assert.assertTrue(accessLog, accessLog.contains(" \"GET /nix_xxx_zzz%F0%90%90%80 HTTP/1.0\" "));
    }    
}