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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Map;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.seanox.test.utils.Accession;

/**
 *  TestSuite for {@link com.seanox.devwex.Initialize}.<br>
 *  <br>
 *  InitializeTest 5.1 20171231<br>
 *  Copyright (C) 2017 Seanox Software Solutions<br>
 *  All rights reserved.
 *
 *  @author  Seanox Software Solutions
 *  @version 5.1 20171231
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    InitializeTest_Contains.class,
    InitializeTest_Get.class,
    InitializeTest_Parse.class,
    InitializeTest_Set.class,
    InitializeTest_ToString.class
})
public class InitializeTest extends AbstractSuite {
    
    /**
     *  R&uuml;ckgabe der formatierten Sektion als String. Der Zeilenumbruch
     *  erfolgt abh&auml;ngig vom aktuellen Betriebssystem.
     *  @return die formatierte Sektion als String
     */
    @SuppressWarnings("unchecked")
    static String toString(com.seanox.devwex.Initialize initialize) {
        
        ByteArrayOutputStream buffer;
        PrintStream           writer;
        String                section;
        String                shadow;

        buffer = new ByteArrayOutputStream();
        writer = new PrintStream(buffer);
        shadow = new String();
        
        Map<String, Section> entries;
        try {entries = (Map<String, Section>)Accession.get(initialize, "entries");
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        
        for (String key : entries.keySet()) {
            
            //leere Schluessek werden ignoriert
            if (key.trim().isEmpty())
                continue;
            
            section = SectionTest.toString(entries.get(key)).trim();
            
            //der Schluessel wird kodiert wenn:
            //  - enthaelt ;[] < 0x1F
            if (key.matches("^(?s)\\s*(?:(?:.*[\\x00-\\x1F\\[\\];])).*$"))
                key = String.format("0x%X", new BigInteger(1, key.getBytes()));
            
            if (!shadow.isEmpty())
                writer.println();
            writer.println(("[").concat(key).concat("]"));
            if (!section.isEmpty())
                writer.println(section.trim());
            
            shadow = section;
        }
        
        return buffer.toString();
    }
}