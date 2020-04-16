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

import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link com.seanox.devwex.Initialize}.<br>
 * <br>
 * InitializeTest_Get 5.1 20171231<br>
 * Copyright (C) 2017 Seanox Software Solutions<br>
 * All rights reserved.
 *
 * @author  Seanox Software Solutions
 * @version 5.1 20171231
 */
public class InitializeTest_Get extends AbstractTest {
    
    /** Test case for acceptance. */
    @Test
    public void testAcceptance_01() {
        
        Initialize initialize = new Initialize();
        for (String key : new String[] {null, "", " ", " \t ", " \r ", " \n ", " \7 "}) {
            Section section = initialize.get(key);
            Assert.assertNull(section);
        }
    }
    
    /** Test case for acceptance. */
    @Test
    public void testAcceptance_02() {
        
        Initialize initialize = new Initialize(false);
        for (String key : new String[] {null, "", " ", " \t ", " \r ", " \n ", " \7 "}) {
            Section section = initialize.get(key);
            Assert.assertNull(section);
        }
    }    
    
    /** Test case for acceptance. */
    @Test
    public void testAcceptance_03() {
        
        Initialize initialize = new Initialize(true);
        for (String key : new String[] {null, "", " ", " \t ", " \r ", " \n ", " \7 "}) {
            Section section = initialize.get(key);
            Assert.assertNotNull(section);
        }
    }
    
    /** Test case for acceptance. */
    @Test
    public void testAcceptance_04() {
        
        Initialize initialize = new Initialize();
        Section section = initialize.get("x");
        Assert.assertNull(section);
    }
    
    /** Test case for acceptance. */
    @Test
    public void testAcceptance_05() {
        
        Initialize initialize = new Initialize(false);
        Section section = initialize.get("x");
        Assert.assertNull(section);
    }    
    
    /** Test case for acceptance. */
    @Test
    public void testAcceptance_06() {
        
        Initialize initialize = new Initialize(true);
        Section section = initialize.get("x");
        Assert.assertNotNull(section);
        Assert.assertTrue(initialize.size() == 1);
        section.set("a", "1");
        section = initialize.get("x");
        Assert.assertNotNull(section);
        Assert.assertTrue(section.size() == 1);
        Assert.assertEquals("1", section.get("a"));
    }     
}