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

import com.seanox.test.utils.Accession;
import com.seanox.test.utils.Pattern;
import com.seanox.test.utils.ResourceUtils;
import com.seanox.test.utils.TextUtils;

/**
 * Test cases for {@link com.seanox.devwex.Worker}.<br>
 * <br>
 * WorkerTest_File 5.1 20171231<br>
 * Copyright (C) 2017 Seanox Software Solutions<br>
 * All rights reserved.
 *
 * @author  Seanox Software Solutions
 * @version 5.1.0 20171231
 */
public class WorkerTest_File extends AbstractTest {
    
    private static String fileNormalize(String path) throws Exception {
        return (String)Accession.invoke(Worker.class, "fileNormalize",
                new Object[] {path});
    }
    
    /** 
     * Test case for acceptance.
     * Test method {@link Worker#fileNormalize(String)}
     * @throws Exception
     */ 
    @Test
    public void testAcceptance_01() throws Exception {
        
        String content = ResourceUtils.getContent();
        String[] lines = TextUtils.split(content, Pattern.LINE_BREAK); 
        for (int loop = 0; loop < lines.length; loop += 2)
            Assert.assertEquals("#" + (loop +1) + ": " + lines[loop], lines[loop +1],
                    WorkerTest_File.fileNormalize(lines[loop]));
    }
}