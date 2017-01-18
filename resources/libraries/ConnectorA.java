import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Map;

/**
 *  LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 *  im Folgenden Seanox Software Solutions oder kurz Seanox genannt.
 *  Diese Software unterliegt der Version 2 der GNU General Public License.
 *
 *  TODO: devwex-test
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

public class ConnectorA {
    
    /** Serversession */
    private Object listener;
    
    private Object environment;
    
    private Map<String, String> environmentMap;    

    /** Datenausgangsstrom */
    private OutputStream output;

    /** Status FALSE Session geschlossen */
    private boolean control;

    /** Responsecode */
    private int status;

    /** Connectortype */
    private int type;

    /** Konstante der Anwendungsversion */
    public static final String VERSION = "0.0000.0000";

    /** Kosntante für den Modultyp Service */
    public static final int SERVICE = 0;

    /** Kosntante für den Modultyp Service */
    public static final int PROCESS = 7;    
    
    /**
     *  Synchronizes the fields of two objects.
     *  For this purpose, the fields of the target object are searched in
     *  source object.
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
    
    public void bind(Object listener, int type)
            throws Exception {
        
        this.listener = listener;
        this.type = type;
        ConnectorA.synchronizeFields(listener, this);
        if (this.environment != null)
            this.environmentMap = (Map<String, String>)ConnectorA.getField(this.environment, "entries");
        this.service();
    }
    
    private void service()
            throws Exception {
        
        String string;

        try {
          
            this.status = 1;

            //the header is built and written out
            string = ("HTTP/1.0 ").concat("001 Test ok").concat("\r\n");
            string = string.concat("Server: ").concat(this.environmentMap.get("SERVER_SOFTWARE")).concat("\r\n");
            if (this.environmentMap.get("MODULE_OPTS").length() > 0)
                string = string.concat("Opts: ").concat(this.environmentMap.get("MODULE_OPTS")).concat("\r\n");
            string = string.concat("Modul: ").concat(this.getClass().getName()).concat("\r\n");
            string = string.concat("Modultype: ").concat(String.valueOf(this.type)).concat("\r\n\r\n");

            //the connection is marked as closed
            this.control = false;
            this.output.write(string.getBytes());

        } finally {
            ConnectorA.synchronizeFields(this, this.listener);
        }
    }
}