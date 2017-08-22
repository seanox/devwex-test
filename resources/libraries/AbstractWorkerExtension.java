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
import java.util.Map;

class AbstractWorkerExtension extends AbstractExtension {
    
    public void filter(Object facade, String options) throws Exception {

        Worker worker = Worker.create(facade);
        try {this.filter(worker, options);
        } finally {
            worker.synchronize();
        }
    }
    
    public void filter(Worker worker, String options) throws Exception {
        return;
    }

    public void service(Object facade, String options) throws Exception {
        
        Worker worker = Worker.create(facade);
        try {this.service(worker, options);
        } finally {
            worker.synchronize();
        }
    }    

    public void service(Worker worker, String options) throws Exception {
        return;
    }
    
    static class Worker {
        
        /** referenced worker */
        private Object facade;
        
        /** socket of worker */
        protected Socket socket;   
        
        /** worker output stream */
        protected OutputStream output;
        
        /** worker environment */
        private Object environment;
        
        /** internal map of worker environment */
        protected Map<String, String> environmentMap;    

        /** worker connection control */
        protected boolean control;

        /** worker response status */
        protected int status;        

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
            
            Field export = source.getClass().getDeclaredField(field);
            export.setAccessible(true);
            return export.get(source);
        }        
        
        static Worker create(Object facade) throws Exception {
            
            Worker worker = new Worker(); 
            worker.facade = facade;
            Worker.synchronizeFields(facade, worker);
            if (worker.environment != null)
                worker.environmentMap = (Map<String, String>)Worker.getField(worker.environment, "entries");
            if (worker.output == null)
                worker.output = worker.socket.getOutputStream(); 
            
            return worker; 
        }
        
        void synchronize() throws Exception {
            Worker.synchronizeFields(this, this.facade);
        }
    }    
}