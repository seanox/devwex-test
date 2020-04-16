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

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.seanox.test.utils.HttpUtils;

/**
 * Test cases for {@link com.seanox.devwex.Service}.<br>
 * <br>
 * ServiceTest_Server 5.1 20180220<br>
 * Copyright (C) 2018 Seanox Software Solutions<br>
 * All rights reserved.
 *
 * @author  Seanox Software Solutions
 * @version 5.1 20180220
 */
public class ServiceTest_Server extends AbstractTest {
    
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
     * A server can be configured and started by different instances.
     *     [REMOTE:INI]    25001
     *     [REMOTE:A:INI]  25002
     *     [REMOTE:B:INI]  25003
     *     [REMOTE::INI]   25004
     * @throws Exception
     */    
    @Test      
    public void testAcceptance_01() throws Exception {
        
        for (int port = 25001; port < 25004; port++) {
            String response = new String(HttpUtils.sendRequest("127.0.0.1:" + port, "sTatuS\r"));
            Assert.assertNotNull(response);
            Assert.assertTrue(response, response.contains("\r\nSAPI: "));
            Assert.assertTrue(response, response.contains("\r\nTIME: "));
            Assert.assertTrue(response, response.contains("\r\nTIUP: "));            
        }
    }
    
    /** 
     * Test case for acceptance.
     * Tests when a implementation (default scope) is used in different instances.
     *     [COUNT:INI] com.seanox.devwex
     *     [COUNT:A:INI] com.seanox.devwex
     * @throws Exception
     */     
    @Test      
    public void testAcceptance_02() throws Exception {
        
        String response;
        response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9001));
        Assert.assertEquals("1 com.seanox.devwex.Count$1", response);
        for (int loop = 2; loop < 10; loop++) {
            response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9001));
            Assert.assertEquals(String.valueOf(loop) + " com.seanox.devwex.Count$1", response);
        }
        response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9002));
        Assert.assertEquals("1 com.seanox.devwex.Count$1", response);
        for (int loop = 2; loop < 15; loop++) {
            response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9002));
            Assert.assertEquals(String.valueOf(loop) + " com.seanox.devwex.Count$1", response);
        }        
        response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9001));
        Assert.assertEquals("10 com.seanox.devwex.Count$1", response);
    }
    
    /** 
     * Test case for acceptance.
     * Tests when a implementation (external scope) is used in different instances.
     *     [COUNT:B1:INI] example
     *     [COUNT:B2:INI] example
     * @throws Exception
     */       
    @Test      
    public void testAcceptance_03() throws Exception {
        
        String response;
        response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9003));
        Assert.assertEquals("1 server.Count$1", response);
        for (int loop = 2; loop < 10; loop++) {
            response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9003));
            Assert.assertEquals(String.valueOf(loop) + " server.Count$1", response);
        }
        response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9004));
        Assert.assertEquals("1 server.Count$1", response);
        for (int loop = 2; loop < 15; loop++) {
            response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9004));
            Assert.assertEquals(String.valueOf(loop) + " server.Count$1", response);
        }        
        response = new String(HttpUtils.sendRequest("127.0.0.1:" + 9003));
        Assert.assertEquals("10 server.Count$1", response);
    }  
    
    /** 
     * Test case for acceptance.
     * Tests if a scope does not exist.
     *     [COUNT:C:INI] example-x
     * @throws Exception
     */           
    @Test(expected=ConnectException.class)
    public void testAcceptance_04() throws Exception {
        
        String details = Service.details();
        Assert.assertFalse(details.contains(":9005"));
        HttpUtils.sendRequest("127.0.0.1:" + 9005);
    }  
    
    /** 
     * Test case for acceptance.
     * Tests if an error occurs in constructor of a server.
     * @throws Exception
     */        
    @Test(expected=ConnectException.class)
    public void testAcceptance_05() throws Exception {
        
        String details = Service.details();
        Assert.assertFalse(details.contains(":9006"));
        HttpUtils.sendRequest("127.0.0.1:" + 9006);
    }    
    
    /** 
     * Test case for acceptance.
     * Tests if an error occurs in the run-method of the server-thread.
     * @throws Exception
     */           
    @Test(expected=SocketTimeoutException.class)
    public void testAcceptance_06() throws Exception {

        String details = Service.details();
        Assert.assertTrue(details.contains(":9007"));
        HttpUtils.sendRequest("127.0.0.1:" + 9007);
    }
    
    /** 
     * Test case for acceptance.
     * Tests if an error occurs in the run-method of the accept.
     * @throws Exception
     */      
    @Test(expected=AssertionError.class)
    public void testAcceptance_07() throws Exception {
        
        String details = Service.details();
        Assert.assertFalse(details.contains(":9008"));
        HttpUtils.sendRequest("127.0.0.1:" + 9008);
    }
    
    /** 
     * Test case for acceptance.
     * Various (walid and invalid) implementations of the SAPI are checked.
     * @throws Exception
     */      
    @Test
    public void testAcceptance_08() throws Exception {

        Service.restart();
        Thread.sleep(250);
        AbstractTestUtils.waitOutputFacadeStream(AbstractSuite.outputStream);
        
        String output = this.outputStreamCapture.toString();
        
        //Server 11 has no logic, but the API is implemented correctly
        Assert.assertFalse(output.contains("Exception: server.Acceptance_11"));
        Assert.assertTrue(output.contains("SERVICE INITIATE ACCEPTANCE_11"));
        
        //Server 12 the destroy-method is not implemented correctly
        Assert.assertTrue(output.contains("java.lang.NoSuchMethodException: server.Acceptance_12.destroy()"));
        
        //Server 13 the explain-method has a different but compatible return type
        Assert.assertFalse(output.contains("Exception: server.Acceptance_13"));        
        Assert.assertTrue(output.contains("SERVICE INITIATE ACCEPTANCE_13"));
        
        //Server 14 the constructor is not implemented correctly
        Assert.assertTrue(output.contains("java.lang.NoSuchMethodException: server.Acceptance_14.<init>(java.lang.String, java.lang.Object)"));        
        
        //Server 15 the constructor is not implemented correctly, looks compatible but is it not
        Assert.assertTrue(output.contains("java.lang.NoSuchMethodException: server.Acceptance_15.<init>(java.lang.String, java.lang.Object)"));          
        
        //Server 16 the constructor is not implemented correctly
        Assert.assertTrue(output.contains("java.lang.NoSuchMethodException: server.Acceptance_16.<init>(java.lang.String, java.lang.Object)"));  
        
        //Server 17 has not implemented java.lang.Runnable
        Assert.assertTrue(output.matches("(?si)^.*\\QSERVICE INITIATE ACCEPTANCE_17\\E[\r\n]+[^\r\n]+\\Qjava.lang.NoSuchMethodException: java.lang.Runnable\\E.*$"));
        
        //Server 18 has implemented java.lang.Thread, not nice but it is ok
        Assert.assertFalse(output.contains("Exception: server.Acceptance_18"));        
        Assert.assertTrue(output.contains("SERVICE INITIATE ACCEPTANCE_18"));
        
        //Server 19 has implemented java.lang.Thread, but  not the run-method, but this is default implemented in java.lang.Thread
        Assert.assertFalse(output.contains("Exception: server.Acceptance_19"));        
        Assert.assertTrue(output.contains("SERVICE INITIATE ACCEPTANCE_19"));   
        
        //Server 21 the implementation of the explain-method is optional, errors in the signature are tolerated
        Assert.assertFalse(output.contains("Exception: server.Acceptance_20"));        
        Assert.assertTrue(output.contains("SERVICE INITIATE ACCEPTANCE_20"));         

        //Server 21 the implementation of the explain-method is optional, errors in the signature are tolerated
        Assert.assertFalse(output.contains("Exception: server.Acceptance_21"));        
        Assert.assertTrue(output.contains("SERVICE INITIATE ACCEPTANCE_21"));         
        
        //Server 21 the implementation of the explain-method is optional, errors in the signature are tolerated
        //          optionally the derstroy-method can have a return value/type, this is ignored
        Assert.assertFalse(output.contains("Exception: server.Acceptance_22"));        
        Assert.assertTrue(output.contains("SERVICE INITIATE ACCEPTANCE_22")); 
        
        //Server 31 the definition of the scope is not clean, it must be a valid package
        Assert.assertTrue(output.contains("SERVICE INITIATE ACCEPTANCE_3X"));
        Assert.assertTrue(output.contains("ClassNotFoundException: server.Acceptance_30.Acceptance_3x"));        
        
        //Server 32 the definition of the scope is not clean, it must be a valid package
        Assert.assertTrue(output.contains("SERVICE INITIATE ACCEPTANCE_31"));
        Assert.assertTrue(output.contains("ClassNotFoundException: server.Acceptance_31x.Acceptance_31"));

        //Server 33 the definition of the scope is not clean, it must be a valid package
        Assert.assertTrue(output.contains("SERVICE INITIATE ACCEPTANCE_32"));
        Assert.assertTrue(output.contains("ClassNotFoundException: server..Acceptance_32"));
        
        //servers and virtuals must be detect correctly
        //even if the use of blanks in the section is unclean
        Assert.assertTrue(output.matches("(?is).*SERVICE\\s+INITIATE\\s+ACCEPTANCE.*"));
        Assert.assertFalse(output.matches("(?is).*SERVICE\\s+INITIATE\\s+VIRTUAL.*"));
        Assert.assertTrue(output.matches("(?is).*:90[0-9][0-9].*"));
        Assert.assertTrue(output.matches("(?is).*:911[3-5].*"));
        Assert.assertFalse(output.matches("(?is).*:911[0-26-9].*"));
        Assert.assertFalse(output.matches("(?is).*:912[0-9].*"));        
    }
}