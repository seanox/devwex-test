/**
 *  LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 *  im Folgenden Seanox Software Solutions oder kurz Seanox genannt. Diese
 *  Software unterliegt der Version 2 der GNU General Public License.
 *
 *  Seanox Test Utilities
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
package com.seanox.test.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *  Utilities for manipulation and direct object access.
 */
public class Accession {

    /** Constructor, creates a new Accession object. */
    private Accession() {
        return;
    }

    /**
     *  Gets a field of an object, even if it is not public or in a superclass.
     *  @param  object object
     *  @param  name   name of the field
     *  @return the determined field, otherwise {@code null}
     *  @throws SecurityException
     */
    public static Field getField(Object object, String name) {

        Class<?>       sheet;
        Field          field;
        List<Class<?>> list;

        if (name == null)
            return null;
        name = name.trim();
        if (name.isEmpty())
            return null;

        sheet = (object instanceof Class) ? (Class<?>)object : object.getClass();

        for (list = new ArrayList<>(); sheet != null; sheet = sheet.getSuperclass()) {

            if (list.contains(sheet))
                continue;
            list.add(sheet);
            
            try {field = sheet.getDeclaredField(name);
            } catch (NoSuchFieldException exception) {
                continue;
            }
            
            field.setAccessible(true);
            
            return field;
        }

        return null;
    }

    /**
     *  Gets the value of a field from an object, even if it is not public or
     *  in a superclass. Primitive data types are returned as a corresponding
     *  wrapper object.
     *  @param  object object
     *  @param  name   name of the field
     *  @return the value of the field, primitive data types are returned as a
     *          corresponding wrapper object
     *  @throws IllegalAccessException
     *      In the case of an access violation.
     *  @throws NoSuchFieldException
     *      If the field does not exist.
     */
    public static Object get(Object object, String name)
        throws IllegalAccessException, NoSuchFieldException {

        Field field;

        field = Accession.getField(object, name);

        if (field == null)
            throw new NoSuchFieldException();

        if (field.getType().equals(Boolean.TYPE))
            return Boolean.valueOf(field.getBoolean(object));
        else if (field.getType().equals(Byte.TYPE))
            return Byte.valueOf(field.getByte(object));
        else if (field.getType().equals(Character.TYPE))
            return Character.valueOf(field.getChar(object));
        else if (field.getType().equals(Double.TYPE))
            return Double.valueOf(field.getDouble(object));
        else if (field.getType().equals(Float.TYPE))
            return Float.valueOf(field.getFloat(object));
        else if (field.getType().equals(Integer.TYPE))
            return Integer.valueOf(field.getInt(object));
        else if (field.getType().equals(Long.TYPE))
            return Long.valueOf(field.getLong(object));
        else if (field.getType().equals(Short.TYPE))
            return Short.valueOf(field.getShort(object));

        else if (!field.getType().isPrimitive())
            return field.get(object);

        return null;
    }
}