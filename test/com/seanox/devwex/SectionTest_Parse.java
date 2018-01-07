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

import com.seanox.test.utils.ResourceUtils;

/**
 *  TestCases for {@link com.seanox.devwex.Section#parse(String)}.<br>
 *  <br>
 *  SectionTest_Parse 5.1 20171231<br>
 *  Copyright (C) 2017 Seanox Software Solutions<br>
 *  All rights reserved.
 *
 *  @author  Seanox Software Solutions
 *  @version 5.1 20171231
 */
public class SectionTest_Parse extends AbstractTest {
    
    private void testAcceptanceBefore_1() {
        System.setProperty("param-c", "p_c");
    }
    
    /** TestCase for acceptance. */
    @Test
    @BeforeTest("testAcceptanceBefore_1")
    public void testAcceptance_1() {

        Section section = Section.parse(ResourceUtils.getContent());
        Assert.assertEquals(ResourceUtils.getContent("testAcceptance_1_1"), SectionTest.toString(section));
    }

    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_2() {
        
        Section section = Section.parse(ResourceUtils.getContent());
        Assert.assertEquals(ResourceUtils.getContent("testAcceptance_2_1"), SectionTest.toString(section));
    }
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_3() {
        
        Section section = Section.parse(ResourceUtils.getContent());
        Assert.assertEquals(ResourceUtils.getContent("testAcceptance_3_1"), SectionTest.toString(section));
    }    
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_4() {
        
        Section section = Section.parse(ResourceUtils.getContent());
        Assert.assertEquals(ResourceUtils.getContent("testAcceptance_4_1"), SectionTest.toString(section));
    } 
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_5() {
        
        Section section = Section.parse(ResourceUtils.getContent());
        Assert.assertEquals(ResourceUtils.getContent("testAcceptance_5_1"), SectionTest.toString(section));
    }

    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_6() {
        
        Section section = Section.parse(ResourceUtils.getContent());
        Assert.assertEquals(ResourceUtils.getContent("testAcceptance_6_1"), SectionTest.toString(section));
    }

    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_7() {
        
        Section section = Section.parse(ResourceUtils.getContent());
        Assert.assertEquals(ResourceUtils.getContent("testAcceptance_7_1"), SectionTest.toString(section));
    }
    
    /** TestCase for acceptance. */
    @Test
    public void testAcceptance_8() {
        
        Section section = Section.parse(ResourceUtils.getContent());
        Assert.assertEquals(ResourceUtils.getContent("testAcceptance_8_1"), SectionTest.toString(section));
    }    
    
    /** TestCase for override keys. */
    @Test
    public void testOverride_1() {
        
        Section section = Section.parse(ResourceUtils.getContent());
        Assert.assertEquals(ResourceUtils.getContent("testOverride_1_1"), SectionTest.toString(section));
    }

    /** TestCase for dynamic keys. */
    @Test
    public void testDynamic_1() {
        
        Section section = Section.parse(ResourceUtils.getContent());
        Assert.assertEquals(ResourceUtils.getContent("testDynamic_1_1").toLowerCase(), SectionTest.toString(section).toLowerCase());
    }
}