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

/** Very elementary extension, only for internal use. */
public class ExtensionA extends AbstractExtension {
    
    public ExtensionA(String options) {
    }
    
    public void filter(Object worker, String options) throws Exception {
        this.perform(worker, options);
    }

    public void service(Object worker, String options) throws Exception {
        this.perform(worker, options);
    }
        
    private void perform(Object worker, String options) throws Exception {

        Meta meta = Meta.create(worker);

        try {
          
            meta.status = 1;

            //the header is built and written out
            String string = ("HTTP/1.0 ").concat("001 Test ok").concat("\r\n");
            string = string.concat("Server: ").concat(meta.environmentMap.get("SERVER_SOFTWARE")).concat("\r\n");
            if (meta.environmentMap.get("MODULE_OPTS").length() > 0)
                string = string.concat("Opts: ").concat(meta.environmentMap.get("MODULE_OPTS")).concat("\r\n");
            String method = new Throwable().getStackTrace()[1].getMethodName();
            method = method.substring(0, 1).toUpperCase().concat(method.substring(1).toLowerCase());
            string = string.concat("Modul: ").concat(this.getClass().getName() + "::" + method).concat("\r\n\r\n");

            //the connection is marked as closed
            meta.control = false;
            meta.output.write(string.getBytes());

        } finally {
            
            meta.synchronize();
        }
    }
}