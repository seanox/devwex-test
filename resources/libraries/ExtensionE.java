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

/** Very elementary extension, only for internal use. */
public class ExtensionE extends AbstractWorkerExtension {
    
    public ExtensionE(String options) {
    }    
    
    public void filter(Worker worker, String options) throws Exception {
        
        String docRoot = worker.environmentMap.get("DOCUMENT_ROOT");
        
        int value = 1;
        Path testFile = Paths.get(docRoot, "test.txt");
        if (Files.exists(testFile))
            value = Integer.valueOf(new String(Files.readAllBytes(testFile))).intValue() +1;
        Files.write(testFile, String.valueOf(value).getBytes());
    }
}