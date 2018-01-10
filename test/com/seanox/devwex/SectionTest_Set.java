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
 *  Test cases for {@link com.seanox.devwex.Section#set(String, String)}.<br>
 *  <br>
 *  SectionTest_Set 5.1 20171231<br>
 *  Copyright (C) 2017 Seanox Software Solutions<br>
 *  All rights reserved.
 *
 *  @author  Seanox Software Solutions
 *  @version 5.1 20171231
 */
public class SectionTest_Set extends AbstractTest {
    
    /** Test case for a invalid key. */
    @Test(expected=Exception.class)
    public void testKeyInvalid_1() {
        
        Section section = new Section();
        section.set("", null);
    }
    
    /** Test case for a invalid key. */
    @Test(expected=IllegalArgumentException.class)
    public void testKeyInvalid_2() {
        
        Section section = new Section();
        section.set(" ", null);
    }

    /** Test case for a invalid key. */
    @Test(expected=IllegalArgumentException.class)
    public void testKeyInvalid_3() {
        
        Section section = new Section();
        section.set("   ", null);
    }

    /** Test case for a invalid key. */
    @Test(expected=IllegalArgumentException.class)
    public void testKeyInvalid_5() {
        
        Section section = new Section();
        section.set(null, null);
    }

    /** Test case for a invalid key. */
    @Test(expected=IllegalArgumentException.class)
    public void testKeyInvalid_6() {
        
        Section section = new Section();
        section.set(" \0\0 ", null);
    }

    /** Test case for a invalid key. */
    @Test(expected=IllegalArgumentException.class)
    public void testKeyInvalid_7() {
        
        Section section = new Section();
        section.set(" \r\n ", null);
    }
    
    /** Test case for a invalid key. */
    @Test(expected=IllegalArgumentException.class)
    public void testKeyInvalid_8() {
        
        Section section = new Section();
        section.set(" \07\07 ", null);
    }

    /** Test case for a invalid key. */
    @Test(expected=IllegalArgumentException.class)
    public void testKeyInvalid_9() {
        
        Section section = new Section();
        section.set(" \40\40 ", null);
    }

    /** Test case for a invalid key. */
    @Test(expected=IllegalArgumentException.class)
    public void testKeyInvalid_A() {
        
        Section section = new Section();
        section.set(" \27\27 ", null);
    }
    
    /** Test case for key tolerance. */
    @Test
    public void testKeyTolerance_1() {
        
        Section section = new Section();
        section.set("A", "a1");
        Assert.assertEquals("a1", section.get("A"));
        Assert.assertEquals("a1", section.get("a"));
        
        section.set("a", "a2");
        Assert.assertEquals("a2", section.get("A"));
        Assert.assertEquals("a2", section.get("a"));
        
        section.set(" a", "a3");
        Assert.assertEquals("a3", section.get("A"));
        Assert.assertEquals("a3", section.get("a"));
        
        section.set(" a ", "a4");
        Assert.assertEquals("a4", section.get("A"));
        Assert.assertEquals("a4", section.get("a"));

        section.set("a ", "a5");
        Assert.assertEquals("a5", section.get("A"));
        Assert.assertEquals("a5", section.get("a"));
    }
    
    /** Test case for overwrite a key. */
    @Test
    public void testKeyOverwrite_1() {
        
        Section section = new Section();
        section.set("A", "a1");
        section.set("a", "a2");
        section.set(" A", "a3");
        section.set(" a   ", "a4");
        Assert.assertEquals("a4", section.get("A"));
        Assert.assertEquals("a4", section.get("a"));
    }
}