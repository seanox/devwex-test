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
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 *  Abstract class to implements and use test classes.
 */
abstract class AbstractTest extends AbstractSuite {
    
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

    @Before
    public void onBefore() throws Exception {
    }
    
    @After
    public void onAfter() throws Exception {
    }
}