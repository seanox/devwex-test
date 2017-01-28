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
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import com.seanox.test.utils.Pattern;

/**
 *  TestCases for {@link com.seanox.devwex.Listener}.
 */
public class ListenerTest_Gateway extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  Method {@code HEAD} was not defined for the CGI and the request is
     *  responded with status 403. For a method {@code HEAD} the server status
     *  is without content.
     *  @throws Exception
     */
    @Test
    public void testAceptance_01() throws Exception {
        
        String request = "HEAD /cgi_module.con HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 403\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s403\\s\\d+\\s-\\s-$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  The file extension {@code *.con }" was defined as CGI with the module
     *  {@code ConnectorA}. Thus the module respponded the request with status
     *  001.
     *  @throws Exception
     */
    @Test
    public void testAceptance_02() throws Exception {
        
        String request = "GET /cgi_module.con HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 001 Test ok\\s+\\w+.*$"));
        Assert.assertTrue(response.matches("(?s)^.*\r\nModul: ConnectorA\r\n.*$"));
        Assert.assertTrue(response.matches("(?s)^.*\r\nModultype: 7\r\n.*$"));
        Assert.assertTrue(response.matches("(?s)^.*\r\nOpts: ConnectorA \\[pa=1\\] \\[M\\]\r\n.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s1\\s\\d+\\s-\\s-$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  The file extension {@code *.con }" was defined as CGI with the module
     *  {@code ConnectorA}. Thus the module respponded the request with status
     *  001.
     *  @throws Exception
     */
    @Test
    public void testAceptance_03() throws Exception {
        
        String request = "GET /cgi_module.con/1 HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 404\\s+\\w+.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nContent-Type: \\w+.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nContent-Length: \\d+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s404\\s\\d+\\s-\\s-$"));      
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Content-Length is 25 but be sent 28 bytes but only 25 bytes must be
     *  sent to the CGI. The request is responded with status 200 and an echo
     *  of the request.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_04() throws Exception {
        
        String request = "POST /cgi_echo.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Type: application/x-www-form-urlencoded\r\n"
                + "Content-Length: 25\r\n"
                + "\r\n"
                + "parameter=xxx&xxx=1234567890";
        String response = new String(TestUtils.sendRequest("127.0.0.1:80", request));   
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        String body = response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.length() == 25);
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s25\\s-\\s-$"));      
    }
    
    /** 
     *  TestCase for aceptance.
     *  The CGI script responds the request with {@code HTTP/1.1 123 Test ...}.
     *  So must also the response header contain {@code HTTP/1.0 123 Test ...}
     *  and be logged with status 123.
     *  @throws Exception
     */
    @Test
    public void testAceptance_05() throws Exception {
        
        String request = "GET /cgi_header_status_1.jsx HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 123 Test\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nServer: Seanox-Devwex.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s123\\s\\d+\\s-\\s-$"));      
    } 
    
    /** 
     *  TestCase for aceptance.
     *  For VHD a CGI application was defined which does not exist.
     *  The request is responded with status 502.
     *  The error must be logged in the std_out/outputl.log.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_06() throws Exception {
        
        String request = "GET /cgi_header_status_1.jsx HTTP/1.0\r\n"
                + "Host: vHd";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 502\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nServer: Seanox-Devwex.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s502\\s\\d+\\s-\\s-$"));   
        
        Thread.sleep(250);
        String outputLog = TestUtils.getOutputLogTail();
        Assert.assertTrue(outputLog.matches("(?si)^^.*\\Q\"xxx.xxx\": CreateProcess error=2,\\E.*$"));   
    } 
    
    /** 
     *  TestCase for aceptance.
     *  The environment variables {@code SERVER_PORT}, {@code SERVER_PROTOCOL},
     *  {@code GATEWAY_INTERFACE}, {@code CONTENT_LENGTH},
     *  {@code CONTENT_TYPE}, {@code QUERY_STRING}, {@code REQUEST_METHOD} and
     *  {@code REMOTE_ADDR}  must be set.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_07() throws Exception {
        
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
        response = new String(TestUtils.sendRequest("127.0.0.1:80", request));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        
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
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:80", request + "\r\n\r\n"));

        header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        
        Assert.assertTrue(body.matches("(?si)^.*\r\nDOCUMENT_ROOT=[^\r\n]+/stage/documents_vh_A\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nSERVER_SOFTWARE=Seanox-Devwex/[^\r\n]+\r\n.*$")); 
        Assert.assertTrue(body.matches("(?si)^.*\r\nREMOTE_PORT=\\d+\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nUNIQUE_ID=[A-Z0-9]+\r\n.*$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  The environment variable {@code DOCUMENT_ROOT} must contain the value
     *  of {@code DOCROOT} and refer to the current work directory.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_08() throws Exception {
        
        String request = "GET \\cgi_environment.jsx HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.matches("(?si)^.*\r\nDOCUMENT_ROOT=[^\r\n]+/stage/documents_vh_A\r\n.*$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Only for modules will set the environment variable {@code MODULE_OPTS}.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_09() throws Exception {
        
        String request = "GET \\cgi_environment.jsx HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertFalse(body.matches("(?si)^.*\\sMODULE_OPTS=.*$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  For the CGI typical environment variables {@code SCRIPT_FILENAME},
     *  {@code PATH_TRANSLATED}, {@code DOCUMENT_ROOT}, {@code REQUEST_URI},
     *  {@code SCRIPT_URL}, {@code SCRIPT_URI}, {@code QUERY_STRING} and
     *  {@code PATH_BASE} must be set correctly. The environment variables
     *  {@code PATH_ABSOLUTE} and {@code PATH_INFO} must not be set.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_10() throws Exception {
        
        String request = "GET \\cgi_environment.jsx?parameter=SCRIPT_FILENAME,PATH_TRANSLATED,REQUEST_URI HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        
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
     *  TestCase for aceptance.
     *  The CGI script responds the request with {@code HTTP/1.1 401 Test ...}.
     *  The first line with the HTTP status must be built by the server.
     *  The custom HTTP status must not be included in the response.
     *  There are no duplicates of the HTTP status allowed.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_11() throws Exception {
        
        String request = "GET /cgi_header_status_C.jsx HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401 Authorization Required\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?s)^.*\r\nHTTP/1\\.0 401.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nServer: Seanox-Devwex.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));      
    }  
    
    /** 
     *  TestCase for aceptance.
     *  The CGI script responds the request with {@code HTTP/1.1 401 Test ...}.
     *  The status text is individual but is respondedd with the server
     *  standard {@code HTTP/1.0 401 Authorization Required}.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_12() throws Exception {

        String request = "GET /cgi_header_status_C.jsx HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 401 Authorization Required\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\r\nServer: Seanox-Devwex.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s401\\s\\d+\\s-\\s-$"));      
    } 
    
    /** 
     *  TestCase for aceptance.
     *  For the CGI all request-header-parameters will be passed with the
     *  prefix 'HTTP_...'. Duplicates are overwritten.
     *  @throws Exception
     */ 
    @Test
    public void testAceptance_13() throws Exception {
        
        String request;
        String response;
        String header;
        String body;        
        
        request = "GET \\cgi_environment.jsx?parameter=HTTP_TEST_123 HTTP/1.0\r\n"
               + "Host: vHa\r\n"
               + "Test-123: erfolgReich";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        
        header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        
        Assert.assertTrue(body.matches("(?si)^.*\r\nHTTP_HOST=vHa\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nHTTP_TEST_123=erfolgReich\r\n.*$"));

        request = "GET \\cgi_environment.jsx?parameter=HTTP_TEST_123 HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Test-123: erfolgReich_1\r\n"
                + "Test-123: erfolgReich_2";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));

        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        
        header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        
        Assert.assertTrue(body.matches("(?si)^.*\r\nHTTP_HOST=vHa\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nHTTP_TEST_123=erfolgReich_2\r\n.*$"));  
        Assert.assertFalse(body.matches("(?si)^.*\r\nHTTP_TEST_123=erfolgReich_1\r\n.*$"));  
    }
    
    /** 
     *  TestCase for aceptance.
     *  The environment variables {@code HTTP_HOST} is always set.
     *  For a virtual host with the name and for a server with the IP.
     *  @throws Exception
     */ 
    @Test
    public void testAceptance_14() throws Exception {
        
        String request;
        String response;
        String header;
        String body;
        
        request = "GET \\cgi_environment.jsx?parameter=HTTP_HOST HTTP/1.0\r\n"
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:80", request + "\r\n\r\n"));

        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));        

        header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertTrue(body.matches("(?si)^.*\r\nHTTP_HOST=vHa\r\n.*$"));
        Assert.assertFalse(body.matches("(?si)^.*\r\nHTTP_HOST=127\\.0\\.0\\.1\r\n.*$"));

        request = "GET \\cgi_environment.jsx?parameter=HTTP_HOST HTTP/1.0";
        response = new String(TestUtils.sendRequest("127.0.0.1:80", request + "\r\n\r\n"));

        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        
        header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        Assert.assertFalse(body.matches("(?si)^.*\r\nHTTP_HOST=vHa\r\n.*$"));
        Assert.assertTrue(body.matches("(?si)^.*\r\nHTTP_HOST=127\\.0\\.0\\.1\r\n.*$"));        
    }
    
    /** 
     *  TestCase for aceptance.
     *  The CGI reads the data very slowly.
     *  The request is canceled with status 502.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_16() throws Exception {
        
        String content = "x";
        while (content.length() < 1024 *1024)
            content += content;
        
        String request = "POST /cgi_read_slow.jsx HTTP/1.0\r\n"
                + "Host: vHa\r\n"
                + "Content-Length: " + content.length() + "\r\n"
                + "\r\n"
                + content;
        
        String response = "HTTP/1.0 502 xxx\r\n\r\n";
        try {response = new String(TestUtils.sendRequest("127.0.0.1:8080", request));
        } catch (IOException exception) {
        }
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 502\\s+\\w+.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s502\\s\\d+\\s-\\s-$"));      
    }
    
    /** 
     *  TestCase for aceptance.
     *  An invalid {@code DOCROOT} has been configured for VHC.
     *  The server uses an alternative working directory as {@code DOCROOT}.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_17() throws Exception {
        
        String request = "GET \\stage\\documents\\cgi_environment.jsx HTTP/1.0\r\n"
                + "Host: vHc";
        String response = new String(TestUtils.sendRequest("127.0.0.1:80", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");
        
        String stage = TestUtils.getRootStage().getParentFile().toString().replace('\\', '/');
        Assert.assertTrue(body.matches("(?si)^.*\r\nDOCUMENT_ROOT=\\Q" + stage + "\\E\r\n.*$"));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  If the CGI response starts with {@code HTTP/STATUS}, then the server
     *  responds to the request. The CGI outstream is read completely, but not
     *  sent to the client.
     *  @throws Exception
     */
    @Test
    public void testAceptance_18() throws Exception {
        
        String request;
        String response;
        String accessLog;
        
        request = "GET /cgi_header_status_2.jsx HTTP/1.0\r\n"
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 123 UND NUN\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s123\\s\\d+\\s-\\s-$"));
        
        request = "GET /cgi_header_status_3.jsx HTTP/1.0\r\n"
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$")); 
        
        request = "GET /cgi_header_status_4.jsx HTTP/1.0\r\n"
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
        
        request = "GET /cgi_header_status_5.jsx HTTP/1.0\r\n"
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
        
        request = "GET /cgi_header_status_6.jsx HTTP/1.0\r\n"
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));   
        
        request = "GET /cgi_header_status_7.jsx HTTP/1.0\r\n"
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));   
        
        request = "GET /cgi_header_status_8.jsx HTTP/1.0\r\n"
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));  
        
        request = "GET /cgi_header_status_9.jsx HTTP/1.0\r\n"
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200 Success\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));   
        
        request = "GET /cgi_header_status_A.jsx HTTP/1.0\r\n"
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200 Success\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s200\\s\\d+\\s-\\s-$"));
        
        request = "GET /cgi_header_status_B.jsx HTTP/1.0\r\n"
                + "Host: vHa";
        response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 444 AAA BBB\\s+\\w+.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Type:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sContent-Length:.*$"));
        Assert.assertFalse(response.matches("(?si)^.*\\sLast-Modified:.*$"));
        Assert.assertTrue(response.matches("(?si)^.*\\sBerlin.*$"));
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s444\\s\\d+\\s-\\s-$"));                                              
    } 
    
    /** 
     *  TestCase for aceptance.
     *  For the CGI a timeout of 30 seconds was defined.
     *  The request is responded with status 200 and is logged with status 504.
     *  Reason, the header has already begun.
     *  @throws Exception
     */     
    @Test(timeout=31000)
    public void testAceptance_19() throws Exception {
        
        String request = "GET /cgi_timeout_status_200.jsx HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s504\\s\\d+\\s-\\s-$"));   
    }  
    
    /** 
     *  TestCase for aceptance.
     *  For the CGI a timeout of 30 seconds was defined.
     *  The request is responded with status 504 and is logged with status 504.
     *  Reason, the header has not yet started.
     *  @throws Exception
     */      
    @Test(timeout=31000)
    public void testAceptance_20() throws Exception {
        
        String request = "GET /cgi_timeout_status_504.jsx HTTP/1.0\r\n"
                + "Host: vHa";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 504\\s+\\w+.*$"));
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s504\\s\\d+\\s-\\s-$"));   
    }   
    
    /** 
     *  TestCase for aceptance.
     *  The CGI response header is limited to 65535 bytes.
     *  In the case of an overlength, the request is responded with status 502.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_21() throws Exception {

        String request;
        String response;
        String accessLog;
        
        request = "GET /cgi_header_flood_1.jsx HTTP/1.0\r\n\r\n";
        response = new String(TestUtils.sendRequest("127.0.0.1:80", request));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 502\\s+\\w+.*$"));
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s502\\s\\d+\\s-\\s-$"));   
        
        request = "GET /cgi_header_flood_2.jsx HTTP/1.0\r\n\r\n";
        response = new String(TestUtils.sendRequest("127.0.0.1:80", request));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 502\\s+\\w+.*$"));
        
        Thread.sleep(250);
        accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s502\\s\\d+\\s-\\s-$"));  
    } 
    
    /**
     *  TestCase for aceptance.
     *  If the server is stopped or restarted, running CGI processes must be
     *  terminated. In this example, a CGI counter is started. The CGI process
     *  is running and the server is restared. The counter must stop!
     *  @throws Exception
     */
    @Test
    public void testAceptance_22() throws Exception {
        
        String request = "GET /cgi_count.jsx HTTP/1.0\r\n\r\n";
        TestUtils.sendRequest("127.0.0.1:80", request, null);
        
        Thread.sleep(2500);
        
        Path counterPath = Paths.get(TestUtils.getRootStage().toString(), "/documents/cgi_count.txt");
        
        int counterContent1 = Integer.valueOf(new String(Files.readAllBytes(counterPath))).intValue();
        TestUtils.sendRequest("127.0.0.1:25001", "RESTaRT\r\n");
        Thread.sleep(2500);
        int counterContent2 = Integer.valueOf(new String(Files.readAllBytes(counterPath))).intValue();
        Thread.sleep(2500);
        int counterContent3 = Integer.valueOf(new String(Files.readAllBytes(counterPath))).intValue();
        
        Assert.assertTrue(counterContent1 <= counterContent2 && counterContent1 <= counterContent3);
        Assert.assertTrue(counterContent2 == counterContent3);
        
        Thread.sleep(250);
        String accessLog = TestUtils.getAccessLogTail();
        Assert.assertTrue(accessLog.matches("^\\d+(\\.\\d+){3}\\s-\\s- \\[[^]]+\\]\\s\"[^\"]+\"\\s503\\s\\d+\\s-\\s-$"));  
    }

    /** 
     *  TestCase for aceptance.
     *  For {@code SERVER:X}, JSX was configured as XCGI.
     *  The environment variables must be transferred via Std_IO.
     *  For the CGI all request-header-parameters will be passed with the
     *  prefix 'HTTP_...'. Duplicates are not overwritten.
     *  @throws Exception
     */       
    @Test
    public void testAceptance_23() throws Exception {

        String request = "POST /cgi_echo.jsx HTTP/1.0\r\n"
                + "Content-Length: 10\r\n"
                + "AAA: A1\r\n"
                + "AAA: A1\r\n"
                + "AAA: A2\r\n"
                + "AAC: A2\r\n"
                + "\r\n"
                + "1234567890";
        String response = new String(TestUtils.sendRequest("127.0.0.1:8080", request + "\r\n\r\n"));
        
        Assert.assertTrue(response.matches("(?s)^HTTP/1\\.0 200\\s+\\w+.*$"));
        
        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.trim().length() > 0);
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2");

        Assert.assertTrue(body.matches("(?s)^.*\r\nHTTP_AAA=A1\r\nHTTP_AAA=A1\r\nHTTP_AAA=A2\r\nHTTP_AAC=A2\r\n.*$"));
    }   
}