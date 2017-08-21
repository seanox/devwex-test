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
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.Map;

/** 
 *  Abstract class to implements very elementary extensions.
 *  Only for internal use.
 */
class AbstractExtension {
    
    public String explain() {
        return null;
    }
    
    public void destroy() {
        return;
    }
    
    static class Meta {
        
        /** referenced worker */
        private Object worker;
        
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
        
        static Meta create(Object worker) throws Exception {
            
            Meta meta = new Meta(); 
            meta.worker = worker;
            Meta.synchronizeFields(worker, meta);
            if (meta.environment != null)
                meta.environmentMap = (Map<String, String>)Meta.getField(meta.environment, "entries");
            if (meta.output == null)
                meta.output = meta.socket.getOutputStream(); 
            
            return meta; 
        }
        
        void synchronize() throws Exception {
            Meta.synchronizeFields(this, this.worker);
        }
    }    
}