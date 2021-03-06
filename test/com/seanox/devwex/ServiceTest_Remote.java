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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test cases for {@link com.seanox.devwex.Service}.<br>
 * <br>
 * ServiceTest_Remote 5.1.0 20171231<br>
 * Copyright (C) 2017 Seanox Software Solutions<br>
 * All rights reserved.
 *
 * @author  Seanox Software Solutions
 * @version 5.1.0 20171231
 */
public class ServiceTest_Remote extends AbstractTest {
    
    /** 
     * Preparation of the runtime environment.
     * @throws Exception
     */
    @BeforeClass
    public static void initiate() throws Exception {
        
        Files.copy(Paths.get("./devwex.ini"), Paths.get("./devwex.ini_"), StandardCopyOption.REPLACE_EXISTING); 
        Files.copy(Paths.get("./devwex.sapi"), Paths.get("./devwex.ini"), StandardCopyOption.REPLACE_EXISTING);
        
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
     * Default connection must work.
     * But not here because the default is not used.
     * @throws Exception
     */     
    @Test
    public void testAcceptance_01() throws Exception {
        
        Service.main(new String[] {"status"});
        
        Thread.sleep(AbstractTest.SLEEP);
        String output = this.outputStreamCapture.toString();
        Assert.assertTrue(output.contains("REMOTE ACCESS FAILED"));
        Assert.assertTrue(output.contains("Connection refused: connect"));
        Assert.assertFalse(output.contains("Network is unreachable: connect"));
        Assert.assertFalse(output.contains("unknow_host")); 
        Assert.assertFalse(output.contains("SAPI"));
    } 
    
    /** 
     * Test case for acceptance.
     * If an unknown host is used, an error must occur.
     * @throws Exception
     */      
    @Test
    public void testAcceptance_02() throws Exception {
        
        Service.main(new String[] {"status", "unknow_host"});
        
        Thread.sleep(AbstractTest.SLEEP);
        String output = this.outputStreamCapture.toString();
        Assert.assertTrue(output.contains("REMOTE ACCESS FAILED"));
        Assert.assertTrue(output.contains("unknow_host")); 
        Assert.assertFalse(output.contains("Network is unreachable: connect"));
        Assert.assertFalse(output.contains("Connection refused: connect"));
        Assert.assertFalse(output.contains("SAPI"));       
    } 
    
    /** 
     * Test case for acceptance.
     * If an invalid port is used, an error must occur.
     * @throws Exception
     */      
    @Test
    public void testAcceptance_03() throws Exception {
        
        Service.main(new String[] {"status", "1234"});

        Thread.sleep(AbstractTest.SLEEP);
        String output = this.outputStreamCapture.toString();
        Assert.assertTrue(output.contains("REMOTE ACCESS FAILED"));
        Assert.assertTrue(output.contains("Network is unreachable: connect"));
        Assert.assertFalse(output.contains("Connection refused: connect"));
        Assert.assertFalse(output.contains("unknow_host")); 
        Assert.assertFalse(output.contains("SAPI"));
    }     
    
    /** 
     * Test case for acceptance.
     * If an invalid port is used, an error must occur.
     * @throws Exception
     */      
    @Test
    public void testAcceptance_04() throws Exception {

        Service.main(new String[] {"status", "127.0.0.1:18000"});

        Thread.sleep(AbstractTest.SLEEP);
        String output = this.outputStreamCapture.toString();
        Assert.assertTrue(output.contains("REMOTE ACCESS FAILED"));
        Assert.assertTrue(output.contains("Connection refused: connect"));
        Assert.assertFalse(output.contains("Network is unreachable: connect"));
        Assert.assertFalse(output.contains("unknow_host"));        
        Assert.assertFalse(output.contains("SAPI"));
    } 
    
    /** 
     * Test case for acceptance.
     * If the correct connection is used, a status response must be returned.
     * @throws Exception
     */      
    @Test
    public void testAcceptance_05() throws Exception {

        Service.main(new String[] {"status", "127.0.0.1:18001"});
        
        Thread.sleep(AbstractTest.SLEEP);
        String output = this.outputStreamCapture.toString();
        Assert.assertFalse(output.contains("REMOTE ACCESS FAILED"));
        Assert.assertFalse(output.contains("Connection refused: connect"));
        Assert.assertFalse(output.contains("Network is unreachable: connect"));
        Assert.assertFalse(output.contains("unknow_host"));
        Assert.assertTrue(output.contains("SAPI"));
    }     
}