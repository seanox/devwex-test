/**
 *  LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 *  im Folgenden Seanox Software Solutions oder kurz Seanox genannt.
 *  Diese Software unterliegt der Version 2 der GNU General Public License.
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

import java.io.IOException;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.seanox.test.utils.OutputFacadeStream;
import com.seanox.test.utils.Pattern;
import com.seanox.test.utils.Timing;

/**
 *  Abstract class to implements and use test classes.<br>
 *  <br>
 *  AbstractTest 5.1 20171231<br>
 *  Copyright (C) 2017 Seanox Software Solutions<br>
 *  All rights reserved.
 *
 *  @author  Seanox Software Solutions
 *  @version 5.1 20171231
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
    static final long SLEEP = 50;

    /**
     *  Counts the active threads of servers.
     *  @return the active threads of servers
     */
    private static int activeServerThreadCount() {
        
        int count = 0;
        for (StackTraceElement[] stackTraceElements : Thread.getAllStackTraces().values()) {
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                if (!Server.class.getName().equals(stackTraceElement.getClassName())
                        && !Remote.class.getName().equals(stackTraceElement.getClassName()))
                    continue;
                count++;
                break;
            }
            
        }
        return count;
    }
    
    /**
     *  Counts the active threads without the threads of servers.
     *  @return the active threads without the threads of servers
     */
    private static int activeThreadCount() {
        return Thread.activeCount() -(AbstractTest.activeServerThreadCount() *2);
    }    
    
    /**
     *  TestWatcher to execute onBefore and onAfter methods before and after of
     *  a test unit. These methods can be used to prepare and finalize
     *  severally test units. 
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
                    try {Thread.sleep(25);
                    } catch (InterruptedException exception1) {
                        break;
                    }
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
     *  Returns the last line of the output stream. 
     *  @return the last line of the output stream
     */
    String outputStreamCaptureTail() {
        
        String log = this.outputStreamCapture.toString().trim();
        String[] lines = log.split(Pattern.LINE_BREAK);
        return lines[lines.length -1];
    }  
    
    /**
     *  Returns the last line of the error stream. 
     *  @return the last line of the error stream
     */
    String errorStreamCaptureTail() {
        
        String log = this.errorStreamCapture.toString().trim();
        String[] lines = log.split(Pattern.LINE_BREAK);
        return lines[lines.length -1];
    }     
    
    /**
     *  Returns the last line of the access stream. 
     *  @return the last line of the access stream
     */
    String accessStreamCaptureTail() {
        
        String log = this.accessStreamCapture.toString().trim();
        String[] lines = log.split(Pattern.LINE_BREAK);
        return lines[lines.length -1];
    }  
}