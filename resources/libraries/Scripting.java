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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/** Very simple command line interface for scripting, based on the CGI. */
public class Scripting {
    
    /**
     *  Main method of application.
     *  Reads and execute the script file of the environment variable
     *  {@code PATH_TRANSLATED}.
     *  @param options
     */
    public static void main(String[] options) {
        
        try {
            Path pathTranslated = Paths.get(System.getenv("PATH_TRANSLATED"));
            System.setProperty("user.dir", pathTranslated.getParent().toRealPath().toString());
            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByName("JavaScript");
            String script = new String(Files.readAllBytes(pathTranslated));
            engine.eval(script);            
        } catch (Exception exception) {
            System.out.print("\r\n\r\n");
            exception.printStackTrace(System.out);
        }
    }
}