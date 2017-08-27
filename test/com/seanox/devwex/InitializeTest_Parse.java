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
 *  TestCases for {@link com.seanox.devwex.Initialize#parse(String)}.
 */
public class InitializeTest_Parse extends AbstractTest {
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_1() {
        
        Initialize initialize = Initialize.parse(ResourceUtils.getContextContent());
        Assert.assertEquals(ResourceUtils.getContextContent("testAcceptance_1_1"), InitializeTest.toString(initialize));
    }
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_2() {

        Initialize initialize = Initialize.parse(ResourceUtils.getContextContent());
        Assert.assertEquals(ResourceUtils.getContextContent("testAcceptance_2_1"), InitializeTest.toString(initialize));
    }
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_3() {

        Initialize initialize = Initialize.parse(ResourceUtils.getContextContent());
        Assert.assertEquals(ResourceUtils.getContextContent("testAcceptance_3_1"), InitializeTest.toString(initialize));
    }
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_4() {

        Initialize initialize = Initialize.parse(ResourceUtils.getContextContent());
        Assert.assertEquals(ResourceUtils.getContextContent("testAcceptance_4_1"), InitializeTest.toString(initialize));
    }
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_5() {

        Initialize initialize = Initialize.parse(ResourceUtils.getContextContent());
        Assert.assertEquals(ResourceUtils.getContextContent("testAcceptance_5_1"), InitializeTest.toString(initialize));
    }
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_6() {

        Initialize initialize = Initialize.parse(ResourceUtils.getContextContent());
        Assert.assertEquals(ResourceUtils.getContextContent("testAcceptance_6_1"), InitializeTest.toString(initialize));
    }  
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_7() {

        Initialize initialize = Initialize.parse(ResourceUtils.getContextContent());
        Assert.assertEquals(ResourceUtils.getContextContent("testAcceptance_7_1"), InitializeTest.toString(initialize));
    } 
}