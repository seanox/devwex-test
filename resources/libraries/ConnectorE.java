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
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/** Very elementary module, only for internal use. */
public class ConnectorE {
    
    /** copy of listener environment */
    private Object environment;
    
    /** internal map of listener environment */
    private Map<String, String> environmentMap;    
    
    /**
     *  Synchronizes the fields of two objects.
     *  In the target object is searched for the fields from the source object
     *  and synchronized when if they exist.
     *  @param source
     *  @param target
     */
    private static void synchronizeFields(Object source, Object target)
            throws Exception {

        for (Field inport : source.getClass().getDeclaredFields()) {

            Field export;
            try {export = target.getClass().getDeclaredField(inport.getName());
            } catch (NoSuchFieldException exception) {
                continue;
            }

            export.setAccessible(true);
            inport.setAccessible(true);

            if (inport.getType().equals(Boolean.TYPE))
                export.setBoolean(target, inport.getBoolean(source));
            else if (inport.getType().equals(Integer.TYPE))
                export.setInt(target, inport.getInt(source));
            else if (inport.getType().equals(Long.TYPE))
                export.setLong(target, inport.getLong(source));
            else if (!inport.getType().isPrimitive())
                export.set(target, inport.get(source));
        }
    }
    
    private static Object getField(Object source, String field)
            throws Exception {
        
        Field export;
        try {export = source.getClass().getDeclaredField(field);
        } catch (NoSuchFieldException exception) {
            return null;
        }
        export.setAccessible(true);
        return export.get(source);
    }

    public void bind(Object listener, int type) throws Throwable {

        ConnectorE.synchronizeFields(listener, this);
        if (this.environment != null)
            this.environmentMap = (Map<String, String>)ConnectorE.getField(this.environment, "entries");
        this.service();
    }
    
    public void service() throws Exception {
        
        String docRoot = this.environmentMap.get("DOCUMENT_ROOT");
        
        int value = 1;
        Path testFile = Paths.get(docRoot, "test.txt");
        if (Files.exists(testFile))
            value = Integer.valueOf(new String(Files.readAllBytes(testFile))).intValue() +1;
        Files.write(testFile, String.valueOf(value).getBytes());
    }
}