/**
 *  LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 *  im Folgenden Seanox Software Solutions oder kurz Seanox genannt.
 *  Diese Software unterliegt der Version 2 der GNU General Public License.
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

/**
 *  TestCases for {@link com.seanox.devwex.Section}.<br>
 *  <br>
 *  SectionTest_Get 5.1 20171231<br>
 *  Copyright (C) 2017 Seanox Software Solutions<br>
 *  All rights reserved.
 *
 *  @author  Seanox Software Solutions
 *  @version 5.1 20171231
 */
public class SectionTest_Get extends AbstractTest {
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_01() {
        
        Section section = new Section();
        for (String key : new String[] {null, "", " ", " \t ", " \r ", " \n ", " \7 "}) {
            String value = section.get(key);
            Assert.assertNull(value);
        }
    }
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_02() {
        
        Section section = new Section(false);
        for (String key : new String[] {null, "", " ", " \t ", " \r ", " \n ", " \7 "}) {
            String value = section.get(key);
            Assert.assertNull(value);
        }
    }    
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_03() {
        
        Section section = new Section(true);
        for (String key : new String[] {null, "", " ", " \t ", " \r ", " \n ", " \7 "}) {
            String value = section.get(key);
            Assert.assertNotNull(value);
        }
    }

    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_04() {
        
        Section section = new Section();
        for (String key : new String[] {null, "", " ", " \t ", " \r ", " \n ", " \7 "}) {
            String value = section.get(key, "o");
            Assert.assertEquals("o", value);
            Assert.assertEquals(0, section.size());
        }
    }
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_05() {
        
        Section section = new Section(false);
        for (String key : new String[] {null, "", " ", " \t ", " \r ", " \n ", " \7 "}) {
            String value = section.get(key, "o");
            Assert.assertEquals("o", value);
            Assert.assertEquals(0, section.size());
        }
    }    
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_06() {
        
        Section section = new Section(true);
        for (String key : new String[] {null, "", " ", " \t ", " \r ", " \n ", " \7 "}) {
            String value = section.get(key, "o");
            Assert.assertEquals("o", value);
            Assert.assertEquals(0, section.size());
        }
    }    
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_07() {
        
        Section section = new Section();
        String value = section.get("x");
        Assert.assertNull(value);
    }
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_08() {
        
        Section section = new Section(false);
        String value = section.get("x");
        Assert.assertNull(value);
    }    

    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_09() {
        
        Section section = new Section(true);
        String value = section.get("x");
        Assert.assertEquals("", value);
        Assert.assertEquals(0, section.size());
    }    

    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_10() {
        
        Section section = new Section(true);
        String value = section.get("x", "a");
        Assert.assertEquals("a", value);
        Assert.assertEquals(0, section.size());
    }    
}