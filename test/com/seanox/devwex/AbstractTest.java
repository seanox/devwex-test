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
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.GeneralSecurityException;
import java.util.UUID;
import java.util.regex.Matcher;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.seanox.test.utils.HttpUtils;
import com.seanox.test.utils.HttpUtils.Authentication;
import com.seanox.test.utils.HttpUtils.Keystore;
import com.seanox.test.utils.OutputFacadeStream;
import com.seanox.test.utils.Pattern;
import com.seanox.test.utils.Timing;

/**
 * Abstract class to implements and use test classes.<br>
 * <br>
 * AbstractTest 5.2.0 20200416<br>
 * Copyright (C) 2020 Seanox Software Solutions<br>
 * All rights reserved.
 *
 * @author  Seanox Software Solutions
 * @version 5.2.0 20200416
 */
public abstract class AbstractTest extends AbstractSuite {
    
    /** capture for the output stream, will be reset before each test */
    protected OutputFacadeStream.Capture outputStreamCapture;

    /** capture for the error stream, will be reset before each test */
    protected OutputFacadeStream.Capture errorStreamCapture;
    
    /** capture for the access stream, will be reset before each test */    
    protected OutputFacadeStream.Capture accessStreamCapture;

    /** execution time of the current test (time measurement) */
    protected Timing timing;
    
    /** duration of general breaks and interruption of tests */
    final static long SLEEP = 50;
    
    /** general timeout for I/O */
    final static long TIMEOUT = 60 *1000;
    
    /**
     * Dynamic pattern for access-log entries with UUID
     * @param  uuid
     * @return pattern for access-log entries with UUID 
     */
    static String ACCESS_LOG_UUID(String uuid) {
        
        if (uuid == null
                || uuid.trim().isEmpty())
            throw new IllegalArgumentException("Invalid uuid");
        return "(?s)^.*(?<=(?:^|\\R)\\Q" + uuid + "\\E\\R)\\s*(\\S+.*?)\\s*(?:(?:\\R.*$)|$)";
    }   

    /** pattern for UUID in HTTP response */
    static final String HTTP_RESPONSE_UUID = "(?s)^.*\r\nUUID:\\s*(\\S+).*$";
    
    /**
     * Dynamic pattern for access-log entries with response UUID
     * @param  response
     * @return pattern for access-log entries with UUID 
     */
    static String ACCESS_LOG_RESPONSE_UUID(String response) {
        
        if (response == null
                || !response.matches(HTTP_RESPONSE_UUID))
            return null;
        String uuid = response.replaceAll(HTTP_RESPONSE_UUID, "$1");
        return ACCESS_LOG_UUID(uuid);
    }

    /**
     * Counts the active threads of servers.
     * @return the active threads of servers
     */
    private static int activeServerThreadCount() {
        
        int count = 0;
        for (StackTraceElement[] stackTraceElements : Thread.getAllStackTraces().values()) {
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                if (!Server.class.getName().equals(stackTraceElement.getClassName())
                        && !Remote.class.getName().equals(stackTraceElement.getClassName())
                        && !Worker.class.getName().equals(stackTraceElement.getClassName()))
                    continue;
                count++;
                break;
            }
            
        }
        return count;
    }
    
    /**
     * Counts the active threads without the threads of servers.
     * @return the active threads without the threads of servers
     */
    private static int activeThreadCount() {
        return Thread.activeCount() -AbstractTest.activeServerThreadCount();
    }    
    
    /**
     * Rule for general preparation and postprocessing of the individual tests
     * during execution.
     */
    @Rule
    public TestRule testWatcher = new TestWatcher() {
        
        private int threadCount;
        
        @Override
        protected void starting(Description description) {
            
            this.threadCount = AbstractTest.activeThreadCount();
            
            AbstractTest.this.outputStreamCapture = AbstractSuite.outputStream.capture();
            AbstractTest.this.errorStreamCapture  = AbstractSuite.errorStream.capture();
            AbstractTest.this.accessStreamCapture = AbstractSuite.accessStream.capture();
            
            AbstractTest.this.timing = Timing.create(true);
        }
        
        @Override
        protected void finished(Description description) {
            
            AbstractTest.this.timing.stop();
            
            if (AbstractTest.this.timing.timeMillis() < SLEEP
                    && description.getClassName().matches("^com\\.seanox\\.devwex\\.(Remote|Server|Service|Worker).*$"))
                try {Thread.sleep(SLEEP);
                } catch (InterruptedException exception) {
                }
                
            try (OutputFacadeStream.Capture outputStreamCapture = AbstractSuite.outputStream.capture();
                    OutputFacadeStream.Capture errorStreamCapture = AbstractSuite.errorStream.capture();
                    OutputFacadeStream.Capture accessStreamCapture = AbstractSuite.accessStream.capture()) {

                while (AbstractTest.activeThreadCount() > this.threadCount
                        || outputStreamCapture.size() > 0
                        || errorStreamCapture.size() > 0
                        || accessStreamCapture.size() > 0) {
                    outputStreamCapture.reset();
                    errorStreamCapture.reset();
                    accessStreamCapture.reset();
                    Thread.yield();
                }
            } catch (IOException exception) {
            }
            
            try {AbstractTest.this.outputStreamCapture.close();
            } catch (Exception exception) {
            }
            try {AbstractTest.this.errorStreamCapture.close();
            } catch (Exception exception) {
            }
            try {AbstractTest.this.accessStreamCapture.close();
            } catch (Exception exception) {
            }            
        }
    };
    
    /**
     * Returns the last line of the output stream. 
     * @return the last line of the output stream
     */
    String outputStreamCaptureTail() {
        
        String log = this.outputStreamCapture.toString().trim();
        String[] lines = log.split(Pattern.LINE_BREAK);
        return lines[lines.length -1];
    }
    
    /**
     * Finds a line in the output log for a pattern.
     * @param  pattern
     * @return to the pattern determined line, otherwise {@code null}.
     */      
    String outputStreamCaptureLine(String pattern) {
        
        String log = this.outputStreamCapture.toString().trim();
        Matcher matcher = java.util.regex.Pattern.compile(pattern).matcher(log);
        if (matcher.find()
                && matcher.groupCount() > 0)
            return matcher.group(1);
        return null;        
    }
    
    /**
     * Returns the last line of the error stream. 
     * @return the last line of the error stream
     */
    String errorStreamCaptureTail() {
        
        String log = this.errorStreamCapture.toString().trim();
        String[] lines = log.split(Pattern.LINE_BREAK);
        return lines[lines.length -1];
    }
    
    /**
     * Finds a line in the error log for a pattern.
     * @param  pattern
     * @return to the pattern determined line, otherwise {@code null}.
     */    
    String errorStreamCaptureLine(String pattern) {
        
        String log = this.errorStreamCapture.toString().trim();
        Matcher matcher = java.util.regex.Pattern.compile(pattern).matcher(log);
        if (matcher.find()
                && matcher.groupCount() > 0)
            return matcher.group(1);
        return null;  
    }
    
    /**
     * Returns the last line of the access stream. 
     * @return the last line of the access stream
     */
    String accessStreamCaptureTail() {
        
        String log = this.accessStreamCapture.toString().trim();
        String[] lines = log.split(Pattern.LINE_BREAK);
        return lines[lines.length -1];
    }
    
    /**
     * Finds a line in the access log for a pattern.
     * @param  pattern
     * @return to the pattern determined line, otherwise {@code null}.
     */
    String accessStreamCaptureLine(String pattern) {
        
        String log = this.accessStreamCapture.toString().trim();
        Matcher matcher = java.util.regex.Pattern.compile(pattern).matcher(log);
        if (matcher.find()
                && matcher.groupCount() > 0)
            return matcher.group(1);
        return null;
    }
    
    /**
     * Extends the request header is extended by a UUID, to localize it in the
     * access log.
     * @param  request
     * @return request with UUID
     * @throws Exception
     */
    private static String createUniqueRequest(String request)
            throws Exception {
        
        String uuid = UUID.randomUUID().toString();
        if (!request.matches(HTTP_RESPONSE_UUID))
            request = request.replaceAll("(^[^\\r\\n]+)", "$1\r\nUUID: " + uuid);
        else uuid = request.replaceAll(HTTP_RESPONSE_UUID, "$1");
        File accessLog = new File(AbstractSuite.PATH_STAGE_ACCESS_LOG);
        Files.write(accessLog.toPath(), (uuid + "\r\n").getBytes(), StandardOpenOption.APPEND);
        return uuid;
    }
    
    /**
     * Sends a HTTP request to a server.
     * The request header is extended by a UUID to localize it in the access log.
     * @param  address address
     * @param  request request
     * @throws IOException
     * @throws GeneralSecurityException
     */     
    String sendRequest(String address, String request)
            throws Exception {

        String uuid = AbstractTest.createUniqueRequest(request);
        String response = new String(HttpUtils.sendRequest(address, request));
        response = response.replaceAll("(^[^\\r\\n]+)", "$1\r\nUUID: " + uuid);
        this.accessStreamCapture.await(AbstractTest.ACCESS_LOG_UUID(uuid), TIMEOUT);      
        return response;
    }
    
    /**
     * Sends a HTTP request to a server.
     * The request header is extended by a UUID to localize it in the access log.
     * @param  address        address
     * @param  request        request
     * @param  authentication authentication
     * @throws IOException
     * @throws GeneralSecurityException
     */     
    String sendRequest(String address, String request, Authentication authentication)
            throws Exception {
        
        String uuid = AbstractTest.createUniqueRequest(request);
        String response = new String(HttpUtils.sendRequest(address, request, authentication));
        response = response.replaceAll("(^[^\\r\\n]+)", "$1\r\nUUID: " + uuid);
        this.accessStreamCapture.await(AbstractTest.ACCESS_LOG_UUID(uuid), TIMEOUT);
        return response;
    }
    
    /**
     * Sends a HTTP request to a server.
     * The request header is extended by a UUID to localize it in the access log.
     * @param  address  address
     * @param  request  request
     * @param  keystore keystore
     * @return the received response
     * @throws IOException
     * @throws GeneralSecurityException
     */    
    String sendRequest(String address, String request, Keystore keystore)
            throws Exception {
        
        String uuid = AbstractTest.createUniqueRequest(request);
        String response = new String(HttpUtils.sendRequest(address, request, keystore));
        response = response.replaceAll("(^[^\\r\\n]+)", "$1\r\nUUID: " + uuid);
        this.accessStreamCapture.await(AbstractTest.ACCESS_LOG_UUID(uuid), TIMEOUT);
        return response;
    }
    
    /**
     * Sends a HTTP request to a server.
     * The request header is extended by a UUID to localize it in the access log.
     * @param  address address
     * @param  request request
     * @param  input   input
     * @return the received response
     * @throws IOException
     * @throws GeneralSecurityException
     */
    String sendRequest(String address, String request, InputStream input)
            throws Exception {
        
        String uuid = AbstractTest.createUniqueRequest(request);
        String response = new String(HttpUtils.sendRequest(address, request, input));
        response = response.replaceAll("(^[^\\r\\n]+)", "$1\r\nUUID: " + uuid);
        this.accessStreamCapture.await(AbstractTest.ACCESS_LOG_UUID(uuid), TIMEOUT);
        return response;
    }    
}