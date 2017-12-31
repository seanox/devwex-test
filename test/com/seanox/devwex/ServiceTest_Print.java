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

import com.seanox.test.utils.ResourceUtils;

/**
 *  TestCases for {@link com.seanox.devwex.Service#print(Object)}.
 */
public class ServiceTest_Print extends AbstractTest {
    
    /** 
     *  TestCase for acceptance.
     *  Optional insertion of an indentation.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_01() throws Exception {
        
        String outputPattern;
        
        outputPattern = ResourceUtils.getContextContent("testAcceptance_01_1");
        
        Service.print(ResourceUtils.getContextContent());
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog1 = this.outputStreamCapture.toString().trim();
        outputLog1 = outputLog1.replaceAll("^[\\d-]+ [\\d:]+", "2000-01-01 14:00:00");
        Assert.assertEquals(outputPattern, outputLog1);
        
        Service.print("----------");
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog2 = this.outputStreamCapture.toString().trim(); 
        Service.print(ResourceUtils.getContextContent(), false);
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog3 = this.outputStreamCapture.toString().trim();
        Assert.assertEquals(outputLog2 + System.lineSeparator() + outputPattern.substring(20), outputLog3);
    } 
    
    /** 
     *  TestCase for acceptance.
     *  Optional insertion of an indentation.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_02() throws Exception {
        
        String outputPattern;
        
        outputPattern = ResourceUtils.getContextContent("testAcceptance_02_1");
        
        Service.print(ResourceUtils.getContextContent());
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog1 = this.outputStreamCapture.toString().trim();
        outputLog1 = outputLog1.replaceAll("^[\\d-]+ [\\d:]+", "2000-01-01 14:00:00");
        Assert.assertEquals(outputPattern, outputLog1);
        
        Service.print("----------");
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog2 = this.outputStreamCapture.toString().trim(); 
        Service.print(ResourceUtils.getContextContent(), false);
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog3 = this.outputStreamCapture.toString().trim();
        Assert.assertEquals(outputLog2 + System.lineSeparator() + outputPattern.substring(20), outputLog3);
    }  
    
    /** 
     *  TestCase for acceptance.
     *  Optional insertion of an indentation.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_03() throws Exception {

        String outputPattern;
        
        outputPattern = ResourceUtils.getContextContent("testAcceptance_03_1");
        
        Service.print(ResourceUtils.getContextContent());
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog1 = this.outputStreamCapture.toString().trim();
        outputLog1 = outputLog1.replaceAll("^[\\d-]+ [\\d:]+", "2000-01-01 14:00:00");
        Assert.assertEquals(outputPattern, outputLog1);
        
        Service.print("----------");
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog2 = this.outputStreamCapture.toString().trim(); 
        Service.print(ResourceUtils.getContextContent(), false);
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog3 = this.outputStreamCapture.toString().trim();
        Assert.assertEquals(outputLog2 + System.lineSeparator() + outputPattern.substring(20), outputLog3);
    } 
    
    /** 
     *  TestCase for acceptance.
     *  Optional insertion of an indentation.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_04() throws Exception {
        
        String outputPattern;
        
        outputPattern = ResourceUtils.getContextContent("testAcceptance_04_1");
        
        Service.print(ResourceUtils.getContextContent());
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog1 = this.outputStreamCapture.toString().trim();
        outputLog1 = outputLog1.replaceAll("^[\\d-]+ [\\d:]+", "2000-01-01 14:00:00");
        Assert.assertEquals(outputPattern, outputLog1);
        
        Service.print("----------");
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog2 = this.outputStreamCapture.toString().trim(); 
        Service.print(ResourceUtils.getContextContent(), false);
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog3 = this.outputStreamCapture.toString().trim();
        Assert.assertEquals(outputLog2 + System.lineSeparator() + outputPattern.substring(20), outputLog3);
    }

    /** 
     *  TestCase for acceptance.
     *  In case of Throwable/Error/Exception, there is no compulsory indentation.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_05() throws Exception {

        Service.print(new Throwable("###1"));
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog = this.outputStreamCapture.toString().trim();
        outputLog = outputLog.replaceAll("^[\\d-]+ [\\d:]+", "2000-01-01 14:00:00");
        Assert.assertTrue(outputLog.startsWith("2000-01-01 14:00:00 java.lang.Throwable: ###1"));
        Assert.assertFalse(outputLog.matches("^.*[\r\n][^\\s].*$"));
    }
    
    /** 
     *  TestCase for acceptance.
     *  In case of Throwable/Error/Exception, there is no compulsory indentation.
     *  Optional insertion of an indentation.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_06() throws Exception {

        Service.print(new Throwable("###1", new Throwable("###2")));
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog = this.outputStreamCapture.toString().trim();
        outputLog = outputLog.replaceAll("^[\\d-]+ [\\d:]+", "2000-01-01 14:00:00");
        Assert.assertTrue(outputLog.startsWith("2000-01-01 14:00:00 java.lang.Throwable: ###1"));
        Assert.assertTrue(outputLog.matches("(?si)^.*[\r\n]\\QCaused by: java.lang.Throwable: ###2\\E.*$"));
    }    

    /** 
     *  TestCase for acceptance.
     *  Optional insertion of an indentation.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_07() throws Exception {

        Service.print(new Throwable("###1", new Throwable("###2", new Throwable("###3"))));
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog = this.outputStreamCapture.toString().trim();
        outputLog = outputLog.replaceAll("^[\\d-]+ [\\d:]+", "2000-01-01 14:00:00");
        Assert.assertTrue(outputLog.startsWith("2000-01-01 14:00:00 java.lang.Throwable: ###1"));
        Assert.assertTrue(outputLog.matches("(?si)^.*[\r\n]\\QCaused by: java.lang.Throwable: ###2\\E.*$"));
        Assert.assertTrue(outputLog.matches("(?si)^.*[\r\n]\\QCaused by: java.lang.Throwable: ###3\\E.*$"));
    }
    
    /** 
     *  TestCase for acceptance.
     *  Empty contents are not output.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_08() throws Exception {
        
        Service.print("----------");
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog1 = this.outputStreamCapture.toString().trim();        
        for (String text : new String[] {"", " ", "  ", null}) {
            Service.print(text);
            Thread.sleep(AbstractTest.SLEEP);
            String outputLog = this.outputStreamCapture.toString().trim();
            Assert.assertEquals(outputLog1, outputLog);
        }

        Service.print("----------");
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog2 = this.outputStreamCapture.toString().trim();       
        for (String text : new String[] {"", " ", "  ", null}) {
            Service.print(text, false);
            Thread.sleep(AbstractTest.SLEEP);
            String outputLog = this.outputStreamCapture.toString().trim();
            Assert.assertEquals(outputLog2, outputLog);
        }
    }    
    
    /** 
     *  TestCase for acceptance.
     *  With option 'strict' line breaks are handled as white spaces and not
     *  written out. Without option 'strict' line breaks will be write as a
     *  single line break.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_09() throws Exception {
        
        Service.print("----------");
        Thread.sleep(AbstractTest.SLEEP);
        String outputLog1 = this.outputStreamCapture.toString().trim();
        for (String text : new String[] {"\r", "\n", "\r\n", "\n\r", "\r\r", "\n\n", "\r\n\r\n", "\n\r\n\r"}) {
            Service.print(text);
            Thread.sleep(AbstractTest.SLEEP);
            String outputLog2 = this.outputStreamCapture.toString().trim();
            Assert.assertEquals(outputLog1, outputLog2);
        }

        for (String text : new String[] {"\r", "\n", "\r\n", "\n\r", "\r\r", "\n\n", "\r\n\r\n", "\n\r\n\r"}) {
            Service.print("----------");
            Thread.sleep(AbstractTest.SLEEP);
            String outputLog3 = this.outputStreamCapture.toString().trim();         
            Service.print(text, false);
            Thread.sleep(AbstractTest.SLEEP);
            String outputLog4 = this.outputStreamCapture.toString().trim();       
            Assert.assertEquals(outputLog3, outputLog4); 
        }
    } 
}