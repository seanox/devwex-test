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
     *  TestCase for aceptance.
     *  Optional insertion of an indentation.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_01() throws Exception {
        
        String outputLog;
        String outputPattern;
        
        outputPattern = ResourceUtils.getContextContent("testAceptance_01_1");
        
        Service.print(ResourceUtils.getContextContent());
        Thread.sleep(50);
        outputLog = AbstractSuite.getOutputLogTail();
        outputLog = outputLog.replaceAll("^[\\d-]+ [\\d:]+", "2000-01-01 14:00:00");
        Assert.assertEquals(outputPattern, outputLog);
        
        Service.print("----------");
        Thread.sleep(50);
        String outputLogEnd = AbstractSuite.getOutputLogTail(); 
        Service.print(ResourceUtils.getContextContent(), false);
        Thread.sleep(50);
        outputLog = AbstractSuite.getOutputLogTail();
        Assert.assertEquals(outputLogEnd + System.lineSeparator() + outputPattern.substring(20), outputLog);
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Optional insertion of an indentation.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_02() throws Exception {
        
        String outputLog;
        String outputPattern;
        
        outputPattern = ResourceUtils.getContextContent("testAceptance_02_1");
        
        Service.print(ResourceUtils.getContextContent());
        Thread.sleep(50);
        outputLog = AbstractSuite.getOutputLogTail();
        outputLog = outputLog.replaceAll("^[\\d-]+ [\\d:]+", "2000-01-01 14:00:00");
        Assert.assertEquals(outputPattern, outputLog);
        
        Service.print("----------");
        Thread.sleep(50);
        String outputLogEnd = AbstractSuite.getOutputLogTail(); 
        Service.print(ResourceUtils.getContextContent(), false);
        Thread.sleep(50);
        outputLog = AbstractSuite.getOutputLogTail();
        Assert.assertEquals(outputLogEnd + System.lineSeparator() + outputPattern.substring(20), outputLog);
    }  
    
    /** 
     *  TestCase for aceptance.
     *  Optional insertion of an indentation.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_03() throws Exception {
        
        String outputLog;
        String outputPattern;
        
        outputPattern = ResourceUtils.getContextContent("testAceptance_03_1");
        
        Service.print(ResourceUtils.getContextContent());
        Thread.sleep(50);
        outputLog = AbstractSuite.getOutputLogTail();
        outputLog = outputLog.replaceAll("^[\\d-]+ [\\d:]+", "2000-01-01 14:00:00");
        Assert.assertEquals(outputPattern, outputLog);
        
        Service.print("----------");
        Thread.sleep(50);
        String outputLogEnd = AbstractSuite.getOutputLogTail(); 
        Service.print(ResourceUtils.getContextContent(), false);
        Thread.sleep(50);
        outputLog = AbstractSuite.getOutputLogTail();
        Assert.assertEquals(outputLogEnd + System.lineSeparator() + outputPattern.substring(20), outputLog);
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Optional insertion of an indentation.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_04() throws Exception {
        
        String outputLog;
        String outputPattern;
        
        outputPattern = ResourceUtils.getContextContent("testAceptance_04_1");
        
        Service.print(ResourceUtils.getContextContent());
        Thread.sleep(50);
        outputLog = AbstractSuite.getOutputLogTail();
        outputLog = outputLog.replaceAll("^[\\d-]+ [\\d:]+", "2000-01-01 14:00:00");
        Assert.assertEquals(outputPattern, outputLog);
        
        Service.print("----------");
        Thread.sleep(50);
        String outputLogEnd = AbstractSuite.getOutputLogTail(); 
        Service.print(ResourceUtils.getContextContent(), false);
        Thread.sleep(50);
        outputLog = AbstractSuite.getOutputLogTail();
        Assert.assertEquals(outputLogEnd + System.lineSeparator() + outputPattern.substring(20), outputLog);
    }

    /** 
     *  TestCase for aceptance.
     *  In case of Throwable/Error/Exception, there is no compulsory indentation.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_05() throws Exception {
        
        Service.print(new Throwable("###1"));
        Thread.sleep(50);
        String outputLog = AbstractSuite.getOutputLogTail();
        outputLog = outputLog.replaceAll("^[\\d-]+ [\\d:]+", "2000-01-01 14:00:00");
        Assert.assertTrue(outputLog.startsWith("2000-01-01 14:00:00 java.lang.Throwable: ###1"));
        Assert.assertFalse(outputLog.matches("^.*[\r\n][^\\s].*$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  In case of Throwable/Error/Exception, there is no compulsory indentation.
     *  Optional insertion of an indentation.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_06() throws Exception {
        
        Service.print(new Throwable("###1", new Throwable("###2")));
        Thread.sleep(50);
        String outputLog = AbstractSuite.getOutputLogTail();
        outputLog = outputLog.replaceAll("^[\\d-]+ [\\d:]+", "2000-01-01 14:00:00");
        Assert.assertTrue(outputLog.startsWith("2000-01-01 14:00:00 java.lang.Throwable: ###1"));
        Assert.assertTrue(outputLog.matches("(?si)^.*[\r\n]\\QCaused by: java.lang.Throwable: ###2\\E.*$"));
    }    

    /** 
     *  TestCase for aceptance.
     *  Optional insertion of an indentation.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_07() throws Exception {
        
        Service.print(new Throwable("###1", new Throwable("###2", new Throwable("###3"))));
        Thread.sleep(50);
        String outputLog = AbstractSuite.getOutputLogTail();
        outputLog = outputLog.replaceAll("^[\\d-]+ [\\d:]+", "2000-01-01 14:00:00");
        Assert.assertTrue(outputLog.startsWith("2000-01-01 14:00:00 java.lang.Throwable: ###1"));
        Assert.assertTrue(outputLog.matches("(?si)^.*[\r\n]\\QCaused by: java.lang.Throwable: ###2\\E.*$"));
        Assert.assertTrue(outputLog.matches("(?si)^.*[\r\n]\\QCaused by: java.lang.Throwable: ###3\\E.*$"));
    }
    
    /** 
     *  TestCase for aceptance.
     *  Empty contents are not output.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_08() throws Exception {
        
        String outputLogEnd;
        
        Service.print("----------");
        Thread.sleep(50);
        outputLogEnd = AbstractSuite.getOutputLogTail();        
        for (String text : new String[] {"", " ", "  ", null}) {
            Service.print(text);
            Thread.sleep(50);
            String outputLog = AbstractSuite.getOutputLogTail();
            Assert.assertEquals(outputLogEnd, outputLog);
        }

        Service.print("----------");
        Thread.sleep(50);
        outputLogEnd = AbstractSuite.getOutputLogTail(false);        
        for (String text : new String[] {"", " ", "  ", null}) {
            Service.print(text, false);
            Thread.sleep(50);
            String outputLog = AbstractSuite.getOutputLogTail(false);
            Assert.assertEquals(outputLogEnd, outputLog);
        }
    }    
    
    /** 
     *  TestCase for aceptance.
     *  With option 'strict' line breaks are handled as white spaces and not
     *  written out. Without option 'strict' line breaks will be write as a
     *  single line break.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_09() throws Exception {
        
        String outputLogEnd;
        
        Service.print("----------");
        Thread.sleep(50);
        outputLogEnd = AbstractSuite.getOutputLogTail();
        for (String text : new String[] {"\r", "\n", "\r\n", "\n\r", "\r\r", "\n\n", "\r\n\r\n", "\n\r\n\r"}) {
            Service.print(text);
            Thread.sleep(50);
            String outputLog = AbstractSuite.getOutputLogTail();
            Assert.assertEquals(outputLogEnd, outputLog);
        }

        for (String text : new String[] {"\r", "\n", "\r\n", "\n\r", "\r\r", "\n\n", "\r\n\r\n", "\n\r\n\r"}) {
            Service.print("----------");
            Thread.sleep(50);
            outputLogEnd = AbstractSuite.getOutputLogTail(false);            
            Service.print(text, false);
            Thread.sleep(50);
            String outputLog = AbstractSuite.getOutputLogTail(false);
            Assert.assertEquals(outputLogEnd, outputLog);
        }        
    } 
}