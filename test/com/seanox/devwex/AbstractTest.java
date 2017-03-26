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

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 *  Abstract class to implements and use test classes.
 */
public abstract class AbstractTest extends AbstractSuite {
    
    /**
     *  TestWatcher to execute onBefore and onAfter methods before and after of
     *  a test unit. These methods can be used to prepare and finalize
     *  severally test units. 
     */
    @Rule
    public TestRule watcher = new TestWatcher() {
        
        @Override
        protected void starting(Description description) {

            String methodName;
            methodName = description.getMethodName();
            methodName = Character.toUpperCase(methodName.charAt(0)) + methodName.substring(1);
            methodName = "onBefore" + methodName;
            try {
                Method method = AbstractTest.this.getClass().getDeclaredMethod(methodName);
                method.setAccessible(true);
                method.invoke(AbstractTest.this);
            } catch (NoSuchMethodException exception) {
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        
        @Override
        protected void finished(Description description) {
            
            String methodName;
            methodName = description.getMethodName();
            methodName = Character.toUpperCase(methodName.charAt(0)) + methodName.substring(1);
            methodName = "onAfter" + methodName;
            try {
                Method method = AbstractTest.this.getClass().getDeclaredMethod(methodName);
                method.setAccessible(true);
                method.invoke(AbstractTest.this);
            } catch (NoSuchMethodException exception) {
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    };
    
    /**
     *  The method can be used to prepare the test class before the first test
     *  unit.
     *  @throws Exception
     */
    @Before
    public void onBefore() throws Exception {
    }
    
    /**
     *  The method can be used to finalize the test class after the last test
     *  unit.
     *  @throws Exception
     */    
    @After
    public void onAfter() throws Exception {
    }
    
    /** Simple time measurement and testing. */
    static class Timing {

        /** start time */
        private Long startTime;
        
        /** stop time */
        private Long stopTime;
        
        /** Constructor, creates a new Timing object. */
        private Timing() {
            this.reset();
        }
        
        /**
         *  Creates a new Timing object.
         *  The time measurement is not started automatically.
         *  @return the created Timing object
         */
        static Timing create() {
            return Timing.create(false);
        }
        
        /**
         *  Creates a new Timing object.
         *  With parameter 'start', the measurement will start automatically.
         *  @return the created Timing object
         */        
        static Timing create(boolean start) {
            
            Timing timing = new Timing();
            if (start)
                timing.start();
            return timing;
        }

        /** Starts the measurement, if it is not running yet. */
        void start() {
            
            long delta = 0;
            if (this.stopTime != null)
                delta = this.stopTime.longValue() -this.startTime.longValue();
            
            if (this.startTime == null)
                this.startTime = Long.valueOf(System.currentTimeMillis() -delta);
        }
        
        /** Stopps the measurement, if it is running. */
        void stop() {
            
            if (this.startTime != null)
                this.stopTime = Long.valueOf(System.currentTimeMillis());
        }
        
        /** Resets the measurement. */
        void reset() {
            
            this.startTime = null;
            this.stopTime  = null;
        }
        
        /** Restarts the measurement (reset + start). */
        void restart() {
            
            this.reset();
            this.start();
        }
        
        /**
         *  Gets the current measured time in milliseconds.
         *  @return the current measured time in milliseconds
         */
        long timeMillis() {

            if (this.startTime == null)
                return 0;
            return System.currentTimeMillis() -this.startTime.longValue();
        }
        
        /**
         *  Checks whether the currently measured time is greater than or equal
         *  to the specified millisecond.
         *  @param milliseconds
         */
        void assertTimeOut(int milliseconds) {
            if (this.timeMillis() < milliseconds)
                Assert.assertEquals("out of " + milliseconds + " ms", this.timeMillis() + " ms");
        }

        /**
         *  Checks whether the currently measured time is outside of the
         *  specified time frame in milliseconds.
         *  @param millisecondsFrom
         *  @param millisecondsTo
         */
        void assertTimeOut(int millisecondsFrom, int millisecondsTo) {
            
            long time = this.timeMillis();
            if (time >= millisecondsFrom && time <= millisecondsTo)
                Assert.assertEquals("out of " + millisecondsFrom + " - " + millisecondsTo + " ms", time + " ms");
        } 

        /**
         *  Checks whether the currently measured time is less than or equal to
         *  the specified millisecond.
         *  @param milliseconds
         */
        void assertTimeIn(int milliseconds) {
            if (this.timeMillis() > milliseconds)
                Assert.assertEquals("in " + milliseconds + " ms", this.timeMillis() + " ms");
        }   
        
        /**
         *  Checks whether the currently measured time is within the specified
         *  time frame in milliseconds.
         *  @param millisecondsFrom
         *  @param millisecondsTo
         */
        void assertTimeRangeIn(int millisecondsFrom, int millisecondsTo) {
            
            long time = this.timeMillis();
            if (time < millisecondsFrom || time > millisecondsTo)
                Assert.assertEquals("in " + millisecondsFrom + " - " + millisecondsTo + " ms", time + " ms");
        }   
    }
}