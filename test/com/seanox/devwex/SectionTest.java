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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.seanox.test.utils.Accession;

/**
 *  TestSuite for {@link com.seanox.devwex.Section}.<br>
 *  <br>
 *  SectionTest 5.1 20171231<br>
 *  Copyright (C) 2017 Seanox Software Solutions<br>
 *  All rights reserved.
 *
 *  @author  Seanox Software Solutions
 *  @version 5.1 20171231
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    SectionTest_Contains.class,
    SectionTest_Parse.class,
    SectionTest_Set.class,
    SectionTest_Get.class,
    SectionTest_ToString.class
})
public class SectionTest extends AbstractSuite {
    
    /**
     *  R&uuml;ckgabe der formatierten Struktur als String.
     *  Der Zeilenumbruch erfolgt abh&auml;ngig vom aktuellen Betriebssystem.
     *  @return die formatierte Struktur als String
     */
    @SuppressWarnings("unchecked")
    static String toString(Section section) {
        
        ByteArrayOutputStream buffer;
        Map<String, String>   entries;
        PrintStream           writer;
        String                value;
        String                space;

        int                   indent;

        entries = new LinkedHashMap<>();
        buffer  = new ByteArrayOutputStream();
        writer  = new PrintStream(buffer);
        
        indent = 0;
        
        Map<String, String> entriesSrc;
        try {entriesSrc = (Map<String, String>)Accession.get(section, "entries");
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }        
        
        for (String key : entriesSrc.keySet()) {
            
            //leere Schluessek werden ignoriert
            if (key.trim().isEmpty())
                continue;

            //der Wert wird kodiert wenn:
            //  - beginnt mit +=   
            //  - enthaelt < 0x1F 
            value = entriesSrc.get(key).trim();
            if (value.matches("^(?s)\\s*(?:(?:[\\+=])|(?:.*[\\x00-\\x1F])).*$"))
                value = String.format("0x%X", new BigInteger(1, value.getBytes()));
            
            //der Schluessel wird kodiert wenn:
            //  - beginnt mit +=   
            //  - enthaelt ;=[] < 0x1F
            if (key.matches("^(?s)\\s*(?:(?:[\\+=])|(?:.*[\\x00-\\x1F\\[\\];=])).*$"))
                key = String.format("0x%X", new BigInteger(1, key.getBytes()));
            
            //der Schluessel wird erweitert wenn:
            //  - der Wert ein Semikolon (Kommentarzeichen) enthaelt
            indent = Math.max(indent, key.length() +(value.contains(";") ? 4 : 0));
            
            entries.put(key, value);
        }
        
        //die Einrueckung wird als Platzhalter aufgebaut
        space = " ";
        while (space.length() < indent)
            space = space.concat(space);
        space = space.substring(0, indent);
        
        for (String key : entries.keySet()) {
            
            value = entries.get(key);
            
            if (!value.isEmpty()) {

                //der Schluessel wird ausgeglichen
                //der Schluessel wird erweitert wenn:
                //   - der Wert ein Semikolon (Kommentarzeichen) enthaelt
                if (value.contains(";"))
                    key = key.concat(space.substring(key.length(), indent -4)).concat(" [+]");
                else key = key.concat(space.substring(key.length(), indent));

                writer.println(key.concat(" = ").concat(value));
                
            } else writer.println(key);
        }
        
        return buffer.toString();
    }
}