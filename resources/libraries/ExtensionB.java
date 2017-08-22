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
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.Socket;

public class ExtensionB {

    /** referenced worker */
    private Object worker;
    
    /** socket of worker */
    private Socket socket;   
    
    /** worker output stream */
    private OutputStream output;

    /** worker connection control */
    private boolean control;

    /** worker response status */
    private int status;

    /** modul type */
    private int type;
    
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
    
    public void bind(Object worker, int type) throws Exception {
        
        this.worker = worker;
        this.type = type;
        ExtensionB.synchronizeFields(worker, this);
        if (this.output == null)
            this.output = this.socket.getOutputStream();
        this.service();
    }

    private void service() throws Exception {
        
        String string;

        try {
            
            Thread.sleep(250);
            
            this.status = 2;

            //the header is built and written out
            string = ("HTTP/1.0 ").concat("002 Test ok").concat("\r\n");
            string = string.concat("Modul: ").concat(this.getClass().getName()).concat("\r\n");
            string = string.concat("Modultype: ").concat(String.valueOf(this.type)).concat("\r\n\r\n");

            //the connection is marked as closed
            this.control = false;
            this.output.write(string.getBytes());

        } finally {
            ExtensionB.synchronizeFields(this, this.worker);
        }        
    }
}