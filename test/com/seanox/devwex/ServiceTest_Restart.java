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

import org.junit.Assert;
import org.junit.Test;

import com.seanox.test.utils.Executor;
import com.seanox.test.utils.Executor.Worker;

/**
 *  TestCases for {@link com.seanox.devwex.Service}.
 */
public class ServiceTest_Restart extends AbstractTest {
    
    /** 
     *  TestCase for acceptance.
     *  Asynchronous restart must work.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_01() throws Exception {

        int loops = 50;
        Executor executor = Executor.create(loops, new Worker() {
            @Override
            protected void execute() {
                Assert.assertTrue(Service.restart());
            }
        });
        
        executor.execute();
        Assert.assertTrue(executor.await(60000));
        
        String outputLog = this.outputStreamCapture.toString();
        Assert.assertFalse(outputLog.contains(".Exception"));
        Assert.assertFalse(outputLog.contains(".Error"));
        Assert.assertFalse(outputLog.contains(".Throwable"));
        Assert.assertEquals(loops +1, outputLog.split("SERVICE RESTARTED").length);
    } 
}