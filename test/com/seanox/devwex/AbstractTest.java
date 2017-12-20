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

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.seanox.test.utils.OutputFacadeStream;

/**
 *  Abstract class to implements and use test classes.
 */
public abstract class AbstractTest extends AbstractSuite {
    
    /** capture for the output stream, will be reset before each test */
    protected OutputFacadeStream.Capture outputStreamCapture;

    /** capture for the error stream, will be reset before each test */
    protected OutputFacadeStream.Capture errorStreamCapture;
    
    /** capture for the access stream, will be reset before each test */    
    protected OutputFacadeStream.Capture accessStreamCapture;
    
    /**
     *  TestWatcher to execute onBefore and onAfter methods before and after of
     *  a test unit. These methods can be used to prepare and finalize
     *  severally test units. 
     */
    @Rule
    public TestRule watcher = new TestWatcher() {

        @Override
        protected void starting(Description description) {
            
            AbstractTest.this.outputStreamCapture = AbstractSuite.outputStream.capture();
            AbstractTest.this.errorStreamCapture = AbstractSuite.errorStream.capture();
            AbstractTest.this.accessStreamCapture = AbstractSuite.accessStream.capture();
        }
        
        @Override
        protected void finished(Description description) {
            
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
}