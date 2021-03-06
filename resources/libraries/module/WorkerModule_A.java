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
package module;

public class WorkerModule_A extends AbstractWorkerModule {
    
    public WorkerModule_A(String options) {
    }
    
    public void filter(Worker worker, String options) throws Exception {
        this.perform(worker, options);
    }

    public void service(Worker worker, String options) throws Exception {
        this.perform(worker, options);
    }
        
    private void perform(Worker worker, String options) throws Exception {
          
        worker.status = 1;

        //the header is built and written out
        String string = ("HTTP/1.0 ").concat("001 Test ok").concat("\r\n");
        string = string.concat("Server: ").concat(worker.environmentMap.get("SERVER_SOFTWARE")).concat("\r\n");
        if (worker.environmentMap.get("MODULE_OPTS").length() > 0)
            string = string.concat("Opts: ").concat(worker.environmentMap.get("MODULE_OPTS")).concat("\r\n");
        String method = new Throwable().getStackTrace()[1].getMethodName();
        method = method.substring(0, 1).toUpperCase().concat(method.substring(1).toLowerCase());
        string = string.concat("Module: ").concat(this.getClass().getName() + "::" + method).concat("\r\n\r\n");

        //the connection is marked as closed
        worker.control = false;
        worker.output.write(string.getBytes());
    }
}