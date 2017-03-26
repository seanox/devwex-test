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
 *  TestCases for {@link com.seanox.devwex.Initialize}.
 */
public class InitializeTest_ToString extends AbstractTest {
    
    /** TestCase for aceptance. */
    @Test
    public void testAceptance_1() {
        
        Initialize initialize = Initialize.parse(ResourceUtils.getContextContent());
        Assert.assertEquals(ResourceUtils.getContextContent("testAceptance_1_1"), InitializeTest.toString(initialize));
    }
    
    /** TestCase for aceptance. */
    @Test
    public void testAceptance_2() {
        
        Initialize initialize = Initialize.parse(ResourceUtils.getContextContent());
        Assert.assertEquals(ResourceUtils.getContextContent("testAceptance_2_1"), InitializeTest.toString(initialize));
    }
    
    /** TestCase for aceptance. */
    @Test
    public void testAceptance_3() {
        
        Initialize initialize = Initialize.parse(ResourceUtils.getContextContent());
        Assert.assertEquals(ResourceUtils.getContextContent("testAceptance_3_1"), InitializeTest.toString(initialize));
    }
    
    /** TestCase for aceptance. */
    @Test
    public void testAceptance_4() {
        
        Initialize initialize = Initialize.parse(ResourceUtils.getContextContent());
        Assert.assertEquals(ResourceUtils.getContextContent("testAceptance_4_1"), InitializeTest.toString(initialize));
    }
}