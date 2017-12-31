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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import com.seanox.test.utils.HttpUtils;
import com.seanox.test.utils.HttpUtils.RequestEvent;
import com.seanox.test.utils.Pattern;
import com.seanox.test.utils.Timing;

/**
 *  TestCases for {@link com.seanox.devwex.Worker}.
 */
public class WorkerTest_Gateway extends AbstractTest {
    
    /** 
     *  TestCase for acceptance.
     *  Method {@code HEAD} was not defined for the CGI and the request is
     *  responded with status 403. For a method {@code HEAD} the server status
     *  is without content.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_01() throws Exception {
        
        String request = "HEAD /cgi_module.con HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_403));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_403));
    }
    
    /** 
     *  TestCase for acceptance.
     *  The file extension {@code *.con }" was defined as CGI with the module
     *  {@code module.WorkerModule_A}. Thus the module respponded the request with status
     *  001.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_02() throws Exception {
        
        String request = "GET /cgi_module.con HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("001 Test ok")));
        Assert.assertTrue(response.matches("(?s)^.*\r\nModule: module.WorkerModule_A::Service\r\n.*$"));
        Assert.assertTrue(response.matches("(?s)^.*\r\nOpts: module.WorkerModule_A \\[pa=1\\] \\[M\\]\r\n.*$"));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("1")));
    } 
    
    /** 
     *  TestCase for acceptance.
     *  The file extension {@code *.con }" was defined as CGI with the module
     *  {@code module.WorkerModule_A}. Thus the module respponded the request
     *  with status 001.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_03() throws Exception {
        
        String request = "GET /cgi_module.con/1 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_404));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_404));      
    } 
    
    /** 
     *  TestCase for acceptance.
     *  Content-Length is 25 but be sent 28 bytes but only 25 bytes must be
     *  sent to the CGI. The request is responded with status 200 and an echo
     *  of the request.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_04() throws Exception {
        
        String request = "POST /cgi_echo.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Type: application/x-www-form-urlencoded\r\n"
                + "Content-Length: 25\r\n"
                + "\r\n"
                + "parameter=xxx&xxx=1234567890";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:80", request));   
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.length() == 25);
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("200", request, 25)));
    }
    
    /** 
     *  TestCase for acceptance.
     *  The CGI script responds the request with {@code HTTP/1.1 123 Test ...}.
     *  So must also the response header contain {@code HTTP/1.0 123 Test ...}
     *  and be logged with status 123.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_05() throws Exception {
        
        String request = "GET /cgi_header_status_1.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("123 Test")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_SERVER));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("123")));      
    } 
    
    /** 
     *  TestCase for acceptance.
     *  For VHD a CGI application was defined which does not exist.
     *  The request is responded with status 502.
     *  The error must be logged in the std_out/outputl.log.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_06() throws Exception {

        String request = "GET /cgi_header_status_1.jsx HTTP/1.0\r\n"
                + "Host: vHd\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_502));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_SERVER));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_502));   
        
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog = this.outputStreamCapture.toString().trim();
        Assert.assertTrue(outputLog.matches("(?si)^^.*\\Q\"xxx.xxx\": CreateProcess error=2,\\E.*$"));
    } 
    
    /** 
     *  TestCase for acceptance.
     *  The environment variables {@code SERVER_PORT}, {@code SERVER_PROTOCOL},
     *  {@code GATEWAY_INTERFACE}, {@code CONTENT_LENGTH},
     *  {@code CONTENT_TYPE}, {@code QUERY_STRING}, {@code REQUEST_METHOD} and
     *  {@code REMOTE_ADDR}  must be set.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_07() throws Exception {
        
        String request;
        String response;
        String header;
        String body;
        
        request = "GET \\cgi_environment.jsx?parameter=SERVER_PORT,SERVER_PROTOCOL,"
               + "GATEWAY_INTERFACE,CONTENT_LENGTH,CONTENT_TYPE,QUERY_STRING,REQUEST_METHOD,"
               + "REMOTE_ADDR&a=123+456©ÿ%00ff\\/#123 456 HTTP/1.0\r\n"
               + "Host: vHa\r\n"
               + "Content-Length: 10\r\n"
               + "Content-Type: xxx/test\r\n"
               + "\r\n"
               + "1234567890";
        response = new String(HttpUtils.sendRequest("127.0.0.1:80", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.matches("(?si)^.*\r\n\\QSERVER_PORT=80\\E\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\n\\QSERVER_PROTOCOL=HTTP/1.0\\E\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\n\\QGATEWAY_INTERFACE=CGI/1.1\\E\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\n\\QCONTENT_LENGTH=10\\E\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\n\\QCONTENT_TYPE=xxx/test\\E\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\n\\QQUERY_STRING=parameter=SERVER_PORT,SERVER_PROTOCOL,GATEWAY_INTERFACE,CONTENT_LENGTH,CONTENT_TYPE,QUERY_STRING,REQUEST_METHOD,REMOTE_ADDR&a=123+456©ÿ%00ff\\/#123\\E\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\n\\QREQUEST_METHOD=GET\\E\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\n\\QREMOTE_ADDR=127.0.0.1\\E\r\n.*$"));
        
        request = "GET \\cgi_environment.jsx?parameter=DOCUMENT_ROOT,SERVER_SOFTWARE,REMOTE_PORT,UNIQUE_ID HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:80", request));

        header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        
        Assert.assertTrue(body.matches("(?si)^.*\r\nDOCUMENT_ROOT=[^\r\n]+/stage/documents_vh_A\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nSERVER_SOFTWARE=Seanox-Devwex/[^\r\n]+\r\n.*$")); 
        Assert.assertTrue(body.matches("(?si)^.*\r\nREMOTE_PORT=\\d+\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nUNIQUE_ID=[A-Z0-9]+\r\n.*$"));
    }
    
    /** 
     *  TestCase for acceptance.
     *  The environment variable {@code DOCUMENT_ROOT} must contain the value
     *  of {@code DOCROOT} and refer to the current work directory.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_08() throws Exception {
        
        String request = "GET \\cgi_environment.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.matches("(?si)^.*\r\nDOCUMENT_ROOT=[^\r\n]+/stage/documents_vh_A\r\n.*$"));
    } 
    
    /** 
     *  TestCase for acceptance.
     *  Only for modules will set the environment variable {@code MODULE_OPTS}.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_09() throws Exception {
        
        String request = "GET \\cgi_environment.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertFalse(body.matches("(?si)^.*\\sMODULE_OPTS=.*$"));
    }
    
    /** 
     *  TestCase for acceptance.
     *  For the CGI typical environment variables {@code SCRIPT_FILENAME},
     *  {@code PATH_TRANSLATED}, {@code DOCUMENT_ROOT}, {@code REQUEST_URI},
     *  {@code SCRIPT_URL}, {@code SCRIPT_URI}, {@code QUERY_STRING} and
     *  {@code PATH_BASE} must be set correctly. The environment variables
     *  {@code PATH_ABSOLUTE} and {@code PATH_INFO} must not be set.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_10() throws Exception {
        
        String request = "GET \\cgi_environment.jsx?parameter=SCRIPT_FILENAME,PATH_TRANSLATED,REQUEST_URI HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");

        if (File.separator.equals("\\")) {
            Assert.assertTrue(body.matches("(?si)^.*\r\nSCRIPT_FILENAME=[^\r\n]+\\\\stage\\\\documents_vh_A\\\\cgi_environment\\.jsx\r\n.*$"));
            Assert.assertTrue(body.matches("(?si)^.*\r\nPATH_TRANSLATED=[^\r\n]+\\\\stage\\\\documents_vh_A\\\\cgi_environment\\.jsx\r\n.*$"));
        } else {
            Assert.assertTrue(body.matches("(?si)^.*\r\nSCRIPT_FILENAME=[^\r\n]+/stage/documents_vh_A/cgi_environment\\.jsx\r\n.*$"));
            Assert.assertTrue(body.matches("(?si)^.*\r\nPATH_TRANSLATED=[^\r\n]+/stage/documents_vh_A/cgi_environment\\.jsx\r\n.*$"));
        }
        Assert.assertTrue(body.matches("(?si)^.*\r\nDOCUMENT_ROOT=[^\r\n]+/stage/documents_vh_A\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nREQUEST_URI=/cgi_environment\\.jsx\\?parameter=SCRIPT_FILENAME,PATH_TRANSLATED,REQUEST_URI\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nSCRIPT_URL=/cgi_environment\\.jsx\r\n.*$"));        
        Assert.assertTrue(body.matches("(?si)^.*\r\nSCRIPT_URI=http://vHa:8080/cgi_environment\\.jsx\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nQUERY_STRING=parameter=SCRIPT_FILENAME,PATH_TRANSLATED,REQUEST_URI\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nPATH_BASE=/cgi_environment\\.jsx\r\n.*$"));        
        Assert.assertFalse(body.matches("(?si)^.*\r\nPATH_ABSOLUTE=.*$"));     
        Assert.assertFalse(body.matches("(?si)^.*\r\nPATH_INFO=.*$"));
    } 
    
    /** 
     *  TestCase for acceptance.
     *  The CGI script responds the request with {@code HTTP/1.1 401 Test ...}.
     *  The first line with the HTTP status must be built by the server.
     *  The custom HTTP status must not be included in the response.
     *  There are no duplicates of the HTTP status allowed.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_11() throws Exception {
        
        String request = "GET /cgi_header_status_C.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("401 Authorization Required")));
        Assert.assertFalse(response.matches("(?s)^.*\r\nHTTP/1\\.0 401.*$"));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_SERVER));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_401));      
    }  
    
    /** 
     *  TestCase for acceptance.
     *  The CGI script responds the request with {@code HTTP/1.1 401 Test ...}.
     *  The status text is individual but is respondedd with the server
     *  standard {@code HTTP/1.0 401 Authorization Required}.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_12() throws Exception {

        String request = "GET /cgi_header_status_C.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("401 Authorization Required")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_SERVER));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_401));      
    } 
    
    /** 
     *  TestCase for acceptance.
     *  For the CGI all request-header-parameters will be passed with the
     *  prefix 'HTTP_...'. Duplicates are overwritten.
     *  @throws Exception
     */ 
    @Test
    public void testAcceptance_13() throws Exception {
        
        String request;
        String response;
        String header;
        String body;        
        
        request = "GET \\cgi_environment.jsx?parameter=HTTP_TEST_123 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Test-123: erfolgReich\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        
        Assert.assertTrue(body.matches("(?si)^.*\r\nHTTP_HOST=vHa\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nHTTP_TEST_123=erfolgReich\r\n.*$"));

        request = "GET \\cgi_environment.jsx?parameter=HTTP_TEST_123 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Test-123: erfolgReich_1\r\n"
                + "Test-123: erfolgReich_2\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        
        Assert.assertTrue(body.matches("(?si)^.*\r\nHTTP_HOST=vHa\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nHTTP_TEST_123=erfolgReich_2\r\n.*$"));  
        Assert.assertFalse(body.matches("(?si)^.*\r\nHTTP_TEST_123=erfolgReich_1\r\n.*$"));  
    }
    
    /** 
     *  TestCase for acceptance.
     *  The environment variables {@code HTTP_HOST} is always set.
     *  For a virtual host with the name and for a server with the IP.
     *  @throws Exception
     */ 
    @Test
    public void testAcceptance_14() throws Exception {
        
        String request;
        String response;
        String header;
        String body;
        
        request = "GET \\cgi_environment.jsx?parameter=HTTP_HOST HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:80", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));        

        header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.matches("(?si)^.*\r\nHTTP_HOST=vHa\r\n.*$"));
        Assert.assertFalse(body.matches("(?si)^.*\r\nHTTP_HOST=127\\.0\\.0\\.1\r\n.*$"));

        request = "GET \\cgi_environment.jsx?parameter=HTTP_HOST HTTP/1.0\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:80", request));

        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertFalse(body.matches("(?si)^.*\r\nHTTP_HOST=vHa\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nHTTP_HOST=127\\.0\\.0\\.1\r\n.*$"));        
    }
    
    /** 
     *  TestCase for acceptance.
     *  The CGI reads the data very slowly.
     *  The request is canceled with status 502.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_16() throws Exception {
        
        String content = "x";
        while (content.length() < 1024 *1024)
            content += content;
        
        String request = "POST /cgi_read_slow.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Length: " + content.length() + "\r\n"
                + "\r\n"
                + content;
        
        String response = "HTTP/1.0 502 xxx\r\n\r\n";
        try {response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        } catch (IOException exception) {
        }
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_502));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_502));      
    }
    
    /** 
     *  TestCase for acceptance.
     *  An invalid {@code DOCROOT} has been configured for VHC.
     *  The server uses an alternative working directory as {@code DOCROOT}.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_17() throws Exception {
        
        String request = "GET \\stage\\documents\\cgi_environment.jsx HTTP/1.0\r\n"
                + "Host: vHc\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:80", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        
        String stage = AbstractSuite.getRootStage().getParentFile().toString().replace('\\', '/');
        Assert.assertTrue(body.matches("(?si)^.*\r\nDOCUMENT_ROOT=\\Q" + stage + "\\E\r\n.*$"));
    } 
    
    /** 
     *  TestCase for acceptance.
     *  If the CGI response starts with {@code HTTP/STATUS}, then the server
     *  responds to the request. The CGI outstream is read completely, but not
     *  sent to the client.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_18() throws Exception {

        String request;
        String response;
        String accessLog;
        
        request = "GET /cgi_header_status_2.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("123 UND NUN")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertFalse(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(AbstractTest.SLEEP);
        accessLog = this.accessStreamCaptureTail();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("123")));
        
        request = "GET /cgi_header_status_3.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(AbstractTest.SLEEP);
        accessLog = this.accessStreamCaptureTail();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_200)); 
        
        request = "GET /cgi_header_status_4.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(AbstractTest.SLEEP);
        accessLog = this.accessStreamCaptureTail();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));
        
        request = "GET /cgi_header_status_5.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(AbstractTest.SLEEP);
        accessLog = this.accessStreamCaptureTail();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));
        
        request = "GET /cgi_header_status_6.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertFalse(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(AbstractTest.SLEEP);
        accessLog = this.accessStreamCaptureTail();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));   
        
        request = "GET /cgi_header_status_7.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertFalse(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(AbstractTest.SLEEP);
        accessLog = this.accessStreamCaptureTail();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));   
        
        request = "GET /cgi_header_status_8.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertFalse(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(AbstractTest.SLEEP);
        accessLog = this.accessStreamCaptureTail();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));  
        
        request = "GET /cgi_header_status_9.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("200 Success")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(AbstractTest.SLEEP);
        accessLog = this.accessStreamCaptureTail();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));   
        
        request = "GET /cgi_header_status_A.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("200 Success")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(AbstractTest.SLEEP);
        accessLog = this.accessStreamCaptureTail();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));
        
        request = "GET /cgi_header_status_B.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS("444 AAA BBB")));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        Assert.assertTrue(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(AbstractTest.SLEEP);
        accessLog = this.accessStreamCaptureTail();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS("444")));
    } 
    
    /** 
     *  TestCase for acceptance.
     *  For the CGI a timeout of 30 seconds was defined.
     *  The request is responded with status 200 and is logged with status 504.
     *  Reason, the header has already begun.
     *  @throws Exception
     */     
    @Test(timeout=60000)
    public void testAcceptance_19() throws Exception {
        
        Timing timing = Timing.create(true);
        String request = "GET /cgi_timeout_status_200.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        timing.assertTimeIn(31000);
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_504));   
    }  
    
    /** 
     *  TestCase for acceptance.
     *  For the CGI a timeout of 30 seconds was defined.
     *  The request is responded with status 504 and is logged with status 504.
     *  Reason, the header has not yet started.
     *  @throws Exception
     */      
    @Test(timeout=60000)
    public void testAcceptance_20() throws Exception {
        
        Timing timing = Timing.create(true);
        String request = "GET /cgi_timeout_status_504.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        timing.assertTimeIn(31000);
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_504));
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_504));   
    }   
    
    /** 
     *  TestCase for acceptance.
     *  The CGI response header is limited to 65535 bytes.
     *  In the case of an overlength, the request is responded with status 502.
     *  @throws Exception
     */       
    @Test
    public void testAcceptance_21() throws Exception {

        String request;
        String response;
        String accessLog;
        
        request = "GET /cgi_header_flood_1.jsx HTTP/1.0\r\n\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:80", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_502));
        
        Thread.sleep(AbstractTest.SLEEP);
        accessLog = this.accessStreamCaptureTail();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_502));   
        
        request = "GET /cgi_header_flood_2.jsx HTTP/1.0\r\n\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:80", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_502));
        
        Thread.sleep(AbstractTest.SLEEP);
        accessLog = this.accessStreamCaptureTail();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_502));
    } 
    
    /**
     *  TestCase for acceptance.
     *  If the server is stopped or restarted, running CGI processes must be
     *  terminated. In this example, a CGI counter is started. The CGI process
     *  is running and the server is restared. The counter must stop!
     *  @throws Exception
     */
    @Test
    public void testAcceptance_22() throws Exception {
        
        String request = "GET /cgi_count.jsx HTTP/1.0\r\n\r\n";
        HttpUtils.sendRequest("127.0.0.1:80", request, (RequestEvent)null);
        
        Thread.sleep(2500);
        
        Path counterPath = Paths.get(AbstractSuite.getRootStage().toString(), "/documents/cgi_count.txt");
        
        int counterContent1 = Integer.parseInt(new String(Files.readAllBytes(counterPath)));
        HttpUtils.sendRequest("127.0.0.1:25001", "RESTaRT\r\n");
        Thread.sleep(2500);
        int counterContent2 = Integer.parseInt(new String(Files.readAllBytes(counterPath)));
        Thread.sleep(2500);
        int counterContent3 = Integer.parseInt(new String(Files.readAllBytes(counterPath)));
        
        Assert.assertTrue(counterContent1 <= counterContent2 && counterContent1 <= counterContent3);
        Assert.assertTrue(counterContent2 == counterContent3);
        
        Thread.sleep(AbstractTest.SLEEP);
        String accessLog = this.accessStreamCapture.toString().trim();
        Assert.assertTrue(accessLog, accessLog.matches(Pattern.ACCESS_LOG_STATUS_503));  
    }

    /** 
     *  TestCase for acceptance.
     *  For {@code SERVER:X}, JSX was configured as XCGI.
     *  The environment variables must be transferred via Std_IO.
     *  For the CGI all request-header-parameters will be passed with the
     *  prefix 'HTTP_...'. Duplicates are ignored, only the first parameter
     *  will be used.
     *  @throws Exception
     */       
    @Test
    public void testAcceptance_23() throws Exception {

        String request = "POST /cgi_echo.jsx HTTP/1.0\r\n"
                + "Content-Length: 10\r\n"
                + "AAA: A1\r\n"
                + "AAA: A1\r\n"
                + "AAA: A2\r\n"
                + "AAC: A2\r\n"
                + "\r\n"
                + "1234567890";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");

        Assert.assertTrue(body.matches("(?s)^.*\r\nHTTP_AAA=A1\r\nHTTP_AAC=A2\r\n.*$"));
    }
    
    /** 
     *  TestCase for acceptance.
     *  Method {@code ALL} was defined for the CGI but {@code METHODS} does
     *  this not allow and the request is responded with status 403.
     *  is without content.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_24() throws Exception {
        
        String request;
        String response;
        
        request = "Get /method.php HTTP/1.0\r\n"
                + "Host: vHb\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_404));
        
        request = "Zet /method.php HTTP/1.0\r\n"
                + "Host: vHb\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:8080", request));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_405));
    }    
}