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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Hashtable;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.seanox.test.utils.Accession;

/**
 * Test cases for {@link com.seanox.devwex.Service}.<br>
 * <br>
 * ServiceTest_Module 5.1.1 20200620<br>
 * Copyright (C) 2020 Seanox Software Solutions<br>
 * All rights reserved.
 *
 * @author  Seanox Software Solutions
 * @version 5.1.1 20200620
 */
public class ServiceTest_Module extends AbstractTest {
    
    /** 
     * Preparation of the runtime environment.
     * @throws Exception
     */
    @BeforeClass
    public static void initiate() throws Exception {
        
        Files.copy(Paths.get("./devwex.ini"), Paths.get("./devwex.ini_"), StandardCopyOption.REPLACE_EXISTING); 
        Files.copy(Paths.get("./devwex.xapi"), Paths.get("./devwex.ini"), StandardCopyOption.REPLACE_EXISTING);
        
        Thread.sleep(250);
        AbstractTestUtils.waitOutputFacadeStream(AbstractSuite.outputStream);
    }
    
    /** 
     * Restoration of the runtime environment.
     * @throws Exception
     */
    @AfterClass
    public static void terminate() throws Exception {
        
        Files.copy(Paths.get("./devwex.ini_"), Paths.get("./devwex.ini"), StandardCopyOption.REPLACE_EXISTING); 
        Files.delete(Paths.get("./devwex.ini_"));
        
        Thread.sleep(250);
        AbstractTestUtils.waitOutputFacadeStream(AbstractSuite.outputStream);
    }
    
    /** 
     * Test case for acceptance.
     * Checks various variants of valid and invalid modules.
     * @throws Exception
     */     
    @Test
    public void testAcceptance_01() throws Exception {
        
        Service.restart();
        Thread.sleep(250);
        AbstractTestUtils.waitOutputFacadeStream(AbstractSuite.outputStream);
        
        String output = this.outputStreamCapture.toString();
        for (int loop = 1; loop < 15; loop++) {
            String pattern = String.format("\\QSERVICE INITIATE MODULE module.Acceptance_%02d\\E", Integer.valueOf(loop));
            Assert.assertTrue(pattern, output.matches("(?s).*" + pattern + ".*"));
            if (loop != 8)
                pattern = pattern + "[\r\n]+\\d{4}(-\\d{2}){2} \\d{2}(:\\d{2}){2} \\Qjava.lang.NoSuchMethodException: Invalid interface\\E";
            else pattern = pattern + "[\r\n]+\\d{4}(-\\d{2}){2} \\d{2}(:\\d{2}){2} \\Qjava.lang.Throwable: module.Acceptance_08\\E";
            if (Arrays.asList(5, 6, 7, 9, 10, 11, 12, 14, 15).contains(loop))
                Assert.assertFalse(pattern, output.matches("(?s).*" + pattern + ".*"));
            else Assert.assertTrue(pattern, output.matches("(?s).*" + pattern + ".*"));
        }
    }
    
    @SuppressWarnings("unchecked")
    private static Object getModule(String module) throws Exception {
        
        Object service = Accession.get(Service.class, "service");
        Object modules = Accession.get(service, "modules");
        
        for (Object entry : ((Hashtable<Class<?>, Object>)modules).values())
            if (entry.getClass().getName().equals(module))
                return entry;
        return null;
    }
    
    private static String getModuleExplain(String module) throws Exception {
        
        if (ServiceTest_Module.getModule(module) == null)
            return null;
        return (String)Accession.invoke(ServiceTest_Module.getModule(module), "explain");
    }
    
    /** 
     * Test case for acceptance.
     * The automatic setting of section keys as parameters is no longer
     * necessary. Check the new behavior.
     * @throws Exception
     */     
    @Test
    public void testAcceptance_02() throws Exception {
        
        Object module;
        
        Assert.assertNull(ServiceTest_Module.getModuleExplain("module.Acceptance_00"));
        Assert.assertNull(ServiceTest_Module.getModuleExplain("module.Acceptance_15"));
        Assert.assertEquals("123", ServiceTest_Module.getModuleExplain("module.Acceptance_16"));
        Assert.assertEquals("[123]", ServiceTest_Module.getModuleExplain("module.Acceptance_17"));
        Assert.assertEquals("[*]", ServiceTest_Module.getModuleExplain("module.Acceptance_18"));
        Assert.assertEquals("[*] [*]", ServiceTest_Module.getModuleExplain("module.Acceptance_19"));
        Assert.assertEquals("[*] 123 [*]", ServiceTest_Module.getModuleExplain("module.Acceptance_20"));

        this.outputStreamCapture.reset();
        
        Assert.assertNull(ServiceTest_Module.getModule("module.Acceptance_21"));
        module = Service.load("module.Acceptance_21");
        Assert.assertNotNull(module);
        module = Service.load((Class<?>)module, null);
        Assert.assertNotNull(module);
        Assert.assertNotNull(ServiceTest_Module.getModule("module.Acceptance_21"));
        Assert.assertNull(ServiceTest_Module.getModuleExplain("module.Acceptance_21"));
        Assert.assertTrue(this.outputStreamCapture.toString().contains("SERVICE INITIATE MODULE module.Acceptance_21"));
    }
}