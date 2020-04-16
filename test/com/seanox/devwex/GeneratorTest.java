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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import org.junit.Assert;
import org.junit.Test;

import com.seanox.test.utils.ResourceUtils;
import com.seanox.test.utils.Timing;

/**
 * Test cases for {@link com.seanox.devwex.Generator}.<br>
 * <br>
 * GeneratorTest 5.1 20171231<br>
 * Copyright (C) 2017 Seanox Software Solutions<br>
 * All rights reserved.
 *
 * @author  Seanox Software Solutions
 * @version 5.1 20171231
 */
public class GeneratorTest extends AbstractTest {
    
    /** Test case for acceptance. */
    @Test
    public void testAcceptance_1() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent().getBytes());
        Assert.assertEquals(ResourceUtils.getContent("testAcceptance_1_1"), new String(generator.extract()));
    }
    
    /** Test case for acceptance. */
    @Test
    public void testAcceptance_2() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent().getBytes());
        Assert.assertEquals(ResourceUtils.getContent("testAcceptance_2_1"), new String(generator.extract()));
    }
    
    /** Test case for acceptance. */
    @Test
    public void testAcceptance_3() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testAcceptance_0_0").getBytes());
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    }
    
    /** Test case for acceptance. */
    @Test
    public void testAcceptance_4() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testAcceptance_0_0").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        String path = new String();
        for (String entry : ("/1/22/333/4444/55555").split("/")) {
            path = path.concat(entry);
            values.put("base", path);
            values.put("name", entry);
            generator.set("path", values);
        }
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    }
    
    /** 
     * Test case for acceptance. 
     * @throws Exception
     */
    @Test
    public void testAcceptance_5() throws Exception {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testAcceptance_0_0").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int loop = 1; loop < 7; loop++) {
            String charX = Character.toString((char)('A' -1 + loop));
            values.put("case", charX + "1");
            values.put("name", charX + "2");
            values.put("date", charX + "3");
            values.put("size", charX + "4");
            values.put("type", charX + "5");
            values.put("mime", charX + "6");
            buffer.write(generator.extract("file", values));
        }
        values.put("file", buffer.toByteArray());
        generator.set(values);
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    }
    
    /** 
     * Test case for acceptance. 
     * @throws Exception
     */
    public void testAcceptance_6() throws Exception {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testAcceptance_0_1").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        values.put("name", "A");
        values.put("date", "B");
        values.put("size", "C");
        values.put("type", "D");
        values.put("mime", "E");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Timing timing = Timing.create(true);
        for (long loop = 1; loop < 50000; loop++) {
            values.put("case", "X" + loop);
            buffer.write(generator.extract("file", values));
        }
        values.put("file", buffer.toByteArray());
        generator.set("file", values);
        generator.extract();
        timing.assertTimeIn(1250);
    }    
    
    /** Test case for acceptance. */
    public void testAcceptance_7() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testAcceptance_0_1").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        values.put("name", "A");
        values.put("date", "B");
        values.put("size", "C");
        values.put("type", "D");
        values.put("mime", "E");
        Timing timing = Timing.create(true);
        for (long loop = 1; loop < 2500; loop++) {
            values.put("case", "X" + loop);
            generator.set("file", values);
        }
        generator.extract();
        timing.assertTimeIn(1250);
    }
    
    /** Test case for acceptance. */
    @Test
    public void testAcceptance_8() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testAcceptance_0_1").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        String path = new String();
        for (String entry : ("/1/22/333/4444/55555").split("/")) {
            path = path.concat(entry);
            values.put("base", path);
            values.put("name", entry);
            generator.set("path", values);
        }
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    }
    
    /** Test case for acceptance. */
    @Test
    public void testAcceptance_9() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testAcceptance_0_2").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        String path = new String();
        for (String entry : ("/1/22/333/4444/55555").split("/")) {
            path = path.concat(entry);
            values.put("base", path);
            values.put("name", entry);
            generator.set("path", values);
        }
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    }

    /** Test case for acceptance. */
    @Test
    public void testAcceptance_A() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testAcceptance_0_2").getBytes());
        System.out.println("[INF] " + new String(generator.extract()));
        Hashtable<String, Object> values = new Hashtable<>();
        String path = new String();
        for (String entry : ("1/22/333/4444/55555").split("/")) {
            path = path.concat(entry);
            values.put("base", path);
            values.put("name", entry);
            generator.set("path", values);
        }
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    }
    
    /** Test case for acceptance. */
    @Test
    public void testAcceptance_B() {
        
        Assert.assertEquals("A\00\00\07\00\00B", new String(Generator.parse(("A#[0x0000070000]B").getBytes()).extract()));
    }
    
    /** 
     * Test case for acceptance. 
     * @throws Exception
     */
    @Test
    public void testAcceptance_C() throws Exception {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testAcceptance_0_0").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        for (int loop = 1; loop < 7; loop++) {
            String charX = Character.toString((char)('A' -1 + loop));
            values.put("case", charX + "1");
            values.put("name", charX + "2");
            values.put("date", charX + "3");
            values.put("size", charX + "4");
            values.put("type", charX + "5");
            values.put("mime", charX + "6");
            generator.set("file", values);
        }
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    }    
    
    /** Test case for acceptance. */
    @Test
    @SuppressWarnings("unchecked")
    public void testAcceptance_D() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testAcceptance_D_1").getBytes());
        Hashtable<String, String> values = new Hashtable() {{
            put("a", new Hashtable() {{
                put("a1", "xa1");
                put("a2", "xa2");
                put("a3", "xa3");
                put("b", new Hashtable() {{
                    put("b1", "xb1");
                    put("b2", "xb2");
                    put("b3", "xb3");
                    put("c", new Hashtable() {{
                        put("c1", "xc1");
                        put("c2", "xc2");
                        put("c3", "xc3");
                    }});
                }});
            }});
        }};
        generator.set(values);
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()).replaceAll("\\s+", ""));
    }
    
    /** Test case for acceptance. */
    @Test
    public void testAcceptance_E() {

        Generator generator = Generator.parse(ResourceUtils.getContent("testAcceptance_E_1").getBytes());
        Hashtable<String, Object> values = new Hashtable() {{
            put("row", new ArrayList() {{
                add(new Hashtable() {{
                    put("cell", new ArrayList() {{
                        add("A1");
                        add("A2");
                        add("A3");
                    }});
                }});
                add(new Hashtable() {{
                    put("cell", new ArrayList() {{
                        add("B1");
                        add("B2");
                        add("B3");
                    }});
                }});
                add(new Hashtable() {{
                    put("cell", new ArrayList() {{
                        add("C1");
                        add("C2");
                    }});
                }});
                add(new Hashtable() {{
                    put("cell", new ArrayList() {{
                        add("D1");
                    }});
                }});
                add(new Hashtable() {{
                    put("cell", new ArrayList() {{
                    }});
                }});
            }});
        }};
        generator.set("table", values);
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    }
    
    /** Test case for acceptance. */
    @Test
    public void testAcceptance_F() {

        String template = "#[0x5065746572]#[0x7c756e64]#[0x7c646572]#[0x7c576f6c66]";
        Generator generator = Generator.parse(template.getBytes());
        Assert.assertEquals("Peter|und|der|Wolf", new String(generator.extract()));
    }

    /** Test case for acceptance. */
    @Test
    public void testAcceptance_G() {

        String template = "#[0x5065746572]#[0x7C756E64]#[0x7C646572]#[0x7C576F6C66]";
        Generator generator = Generator.parse(template.getBytes());
        Assert.assertEquals("Peter|und|der|Wolf", new String(generator.extract()));
    }
    
    /** Test case for acceptance. */
    @Test
    public void testAcceptance_H() {

        String template = "#[0X5065746572]#[0X7C756E64]#[0X7C646572]#[0X7C576F6C66]";
        Generator generator = Generator.parse(template.getBytes());
        Assert.assertEquals("Peter|und|der|Wolf", new String(generator.extract()));
    }    

    /** Test case for recursion. */
    @Test
    public void testRecursion_1() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testRecursion_0_1").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        values.put("teST", "xx1");
        generator.set("path", values);
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    }   
    
    /** Test case for recursion. */
    @Test
    public void testRecursion_2() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testRecursion_0_1").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        values.put("teST", "xx1");
        generator.set("path", values);
        values.put("teST", "xx2");
        generator.set("path", values);
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    }   
    
    /** Test case for recursion. */
    @Test
    public void testRecursion_3() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testRecursion_0_1").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        values.put("teST", "xx1");
        generator.set("path", values);
        values.put("teST", "xx2");
        generator.set("path", values);
        values.put("teST", "xx3");
        generator.set("path", values);
        values.put("teST", "xx4");
        generator.set("path", values);
        values.put("teST", "xx5");
        generator.set("path", values);
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    }    
    
    /** Test case for recursion. */
    @Test
    public void testRecursion_4() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testRecursion_0_2").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        values.put("teST", "xx1");
        generator.set("path", values);
        values.put("teST", "xx2");
        generator.set("path", values);
        values.put("teST", "xx3");
        generator.set("path", values);
        values.put("teST", "xx4");
        generator.set("path", values);
        values.put("teST", "xx5");
        generator.set("path", values);
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    }    

    /** Test case for recursion. */
    @Test
    public void testRecursion_5() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testRecursion_0_3").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        values.put("bv", "bv-ok");
        values.put("cv", "cv-ok");
        values.put("dv", "dv-ok");
        values.put("b1v", "b1v-ok");
        generator.set("a", values);
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    } 
    
    /** Test case for recursion. */
    @Test
    public void testRecursion_6() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testRecursion_0_3").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        values.put("bv", "bv-ok");
        values.put("cv", "cv-ok");
        values.put("dv", "dv-ok");
        values.put("b1v", "b1v-ok");
        generator.set("a", values);
        generator.set("b", values);
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    } 
    
    /** Test case for recursion. */
    @Test
    public void testRecursion_7() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testRecursion_0_3").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        values.put("bv", "bv-ok");
        values.put("cv", "cv-ok");
        values.put("dv", "dv-ok");
        values.put("b1v", "b1v-ok");
        generator.set("a", values);
        generator.set("b", values);
        generator.set("c", values);
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    }
    
    /** Test case for recursion. */
    @Test
    public void testRecursion_8() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testRecursion_0_3").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        values.put("bv", "bv-ok");
        values.put("cv", "cv-ok");
        values.put("dv", "dv-ok");
        values.put("b1v", "b1v-ok");
        generator.set("a", values);
        generator.set("b", values);
        generator.set("c", values);
        generator.set("d", values);
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    }     

    /** Test case for recursion. */
    @Test
    public void testRecursion_9() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testRecursion_0_3").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        values.put("bv", "bv-ok");
        values.put("cv", "cv-ok");
        values.put("dv", "dv-ok");
        values.put("b1v", "b1v-ok");
        generator.set("d", values);
        generator.set("c", values);
        generator.set("b", values);
        generator.set("a", values);
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    }
    
    /** Test case for recursion. */
    @Test
    public void testRecursion_A() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testRecursion_0_3").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        values.put("bv", "bv-ok");
        values.put("cv", "cv-ok");
        values.put("dv", "dv-ok");
        values.put("b1v", "b1v-ok");
        values.put("a", values);
        values.put("b", values);
        values.put("c", values);
        values.put("d", values);
        generator.set(values);
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    } 

    /** Test case for recursion. */
    @Test
    public void testRecursion_B() {
        
        Generator generator = Generator.parse(ResourceUtils.getContent("testRecursion_B_1").getBytes());
        Hashtable<String, Object> values = new Hashtable<>();
        values.put("a", "xa");
        values.put("b", "xb");
        values.put("c", "xc");
        generator.set(values);
        Assert.assertEquals(ResourceUtils.getContent(), new String(generator.extract()));
    } 
    
    /** Test case for nullable. */
    @Test
    public void testNullable_1() {
      
        Generator generator = Generator.parse(null);
        generator.extract(null);
        generator.extract("");
        generator.extract(null, null);
        generator.extract("", new Hashtable<>());
        generator.set(null);
        generator.set(new Hashtable<>());
        generator.set(null, null);
        generator.set("", new Hashtable<>());
    }     
}