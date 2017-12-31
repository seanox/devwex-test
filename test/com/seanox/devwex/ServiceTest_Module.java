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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.seanox.test.utils.ResourceUtils;

/**
 *  TestCases for {@link com.seanox.devwex.Service}.
 */
public class ServiceTest_Module extends AbstractTest {
    
    /** 
     *  Preparation of the runtime environment.
     *  @throws Exception
     */
    @BeforeClass
    public static void onBeforeClass() throws Exception {
        
        Files.copy(Paths.get("./devwex.ini"), Paths.get("./devwex.ini_"), StandardCopyOption.REPLACE_EXISTING); 
        Files.copy(Paths.get("./devwex.xapi"), Paths.get("./devwex.ini"), StandardCopyOption.REPLACE_EXISTING);
        
        Thread.sleep(250);
        AbstractTestUtils.waitOutputFacadeStream(AbstractSuite.outputStream);
    }
    
    /** 
     *  Restoration of the runtime environment.
     *  @throws Exception
     */
    @AfterClass
    public static void onAfterClass() throws Exception {
        
        Files.copy(Paths.get("./devwex.ini_"), Paths.get("./devwex.ini"), StandardCopyOption.REPLACE_EXISTING); 
        Files.delete(Paths.get("./devwex.ini_"));
        
        Thread.sleep(250);
        AbstractTestUtils.waitOutputFacadeStream(AbstractSuite.outputStream);
    }
    
    /** 
     *  TestCase for acceptance.
     *  Checks various variants of valid and invalid modules.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_01() throws Exception {
        
        Service.restart();
        Thread.sleep(250);
        AbstractTestUtils.waitOutputFacadeStream(AbstractSuite.outputStream);
        
        String output = this.outputStreamCapture.toString();
        for (int loop : new int[] {5, 6, 7, 9, 10, 11, 12, 14, 15}) {
            String pattern = String.format("(?si).*Exception:[^\r\n]+_%02d.*", Integer.valueOf(loop));
            Assert.assertFalse(pattern, output.matches(pattern));
        }

        for (String pattern : ResourceUtils.getContextContent().split("[\r\n]+"))
            Assert.assertTrue(pattern, output.contains(pattern));
    }
}