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

/**
 *  TestCases for {@link com.seanox.devwex.Service}.
 */
public class ServiceTest_Remote extends AbstractTest {
    
    /** 
     *  Preparation of the runtime environment.
     *  @throws Exception
     */
    @BeforeClass
    public static void oneBeforeClass() throws Exception {
        
        Files.copy(Paths.get("./devwex.ini"), Paths.get("./devwex.ini_"), StandardCopyOption.REPLACE_EXISTING); 
        Files.copy(Paths.get("./devwex.sapi"), Paths.get("./devwex.ini"), StandardCopyOption.REPLACE_EXISTING);
        
        Thread.sleep(250);
        AbstractSuite.waitOutputReady();
    }
    
    /** 
     *  Restoration of the runtime environment.
     *  @throws Exception
     */
    @AfterClass
    public static void oneAfterClass() throws Exception {
        
        Files.copy(Paths.get("./devwex.ini_"), Paths.get("./devwex.ini"), StandardCopyOption.REPLACE_EXISTING); 
        Files.delete(Paths.get("./devwex.ini_"));
        
        Thread.sleep(250);
        AbstractSuite.waitOutputReady();
    } 
    
    /** 
     *  TestCase for acceptance.
     *  Default connection must work.
     *  But not here because the default is not used.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_01() throws Exception {
        
        Service.main(new String[] {"status"});
        
        Thread.sleep(50);
        String output = AbstractSuite.getOutputLog(Trace.create(Trace.Type.CLASS, Trace.Type.METHOD));
        Assert.assertTrue(output.contains("REMOTE ACCESS FAILED"));
        Assert.assertTrue(output.contains("Network is unreachable: connect"));
        Assert.assertFalse(output.contains("unknow_host")); 
        Assert.assertFalse(output.contains("Connection refused: connect"));
        Assert.assertFalse(output.contains("SAPI"));
    } 
    
    /** 
     *  TestCase for acceptance.
     *  If an unknown host is used, an error must occur.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_02() throws Exception {
        
        Service.main(new String[] {"status", "unknow_host"});
        
        Thread.sleep(50);
        String output = AbstractSuite.getOutputLog(Trace.create(Trace.Type.CLASS, Trace.Type.METHOD));
        Assert.assertTrue(output.contains("REMOTE ACCESS FAILED"));
        Assert.assertTrue(output.contains("unknow_host")); 
        Assert.assertFalse(output.contains("Network is unreachable: connect"));
        Assert.assertFalse(output.contains("Connection refused: connect"));
        Assert.assertFalse(output.contains("SAPI"));        
    } 
    
    /** 
     *  TestCase for acceptance.
     *  If an invalid port is used, an error must occur.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_03() throws Exception {
        
        Service.main(new String[] {"status", "1234"});
        
        Thread.sleep(50);
        String output = AbstractSuite.getOutputLog(Trace.create(Trace.Type.CLASS, Trace.Type.METHOD));
        Assert.assertTrue(output.contains("REMOTE ACCESS FAILED"));
        Assert.assertTrue(output.contains("Network is unreachable: connect"));
        Assert.assertFalse(output.contains("Connection refused: connect"));
        Assert.assertFalse(output.contains("unknow_host")); 
        Assert.assertFalse(output.contains("SAPI"));   
    }     
    
    /** 
     *  TestCase for acceptance.
     *  If an invalid port is used, an error must occur.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_04() throws Exception {
        
        Service.main(new String[] {"status", "127.0.0.1:25000"});

        Thread.sleep(50);
        String output = AbstractSuite.getOutputLog(Trace.create(Trace.Type.CLASS, Trace.Type.METHOD));
        Assert.assertTrue(output.contains("REMOTE ACCESS FAILED"));
        Assert.assertTrue(output.contains("Connection refused: connect"));
        Assert.assertFalse(output.contains("Network is unreachable: connect"));
        Assert.assertFalse(output.contains("unknow_host"));        
        Assert.assertFalse(output.contains("SAPI"));
    } 
    
    /** 
     *  TestCase for acceptance.
     *  If the correct connection is used, a status response must be returned.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_05() throws Exception {
        
        Service.main(new String[] {"status", "127.0.0.1:25001"});
        
        Thread.sleep(50);
        String output = AbstractSuite.getOutputLog(Trace.create(Trace.Type.CLASS, Trace.Type.METHOD));
        Assert.assertFalse(output.contains("REMOTE ACCESS FAILED"));
        Assert.assertFalse(output.contains("Connection refused: connect"));
        Assert.assertFalse(output.contains("Network is unreachable: connect"));
        Assert.assertFalse(output.contains("unknow_host"));
        Assert.assertTrue(output.contains("SAPI"));        
    }     
}