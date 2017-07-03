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
package com.seanox.test.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 *  Utils for mock data.
 */
public class MockUtils {
    
    /**
     *  Creates a readable InputStream.
     *  This starts with A, is filled with - and ends with E.
     *      <dir>e.g. {@code A-----E}</dir>
     *  <ul>
     *    <li>          
     *      Is the space less than 2, then the InputStream contains only E.
     *    <li>  
     *      Is the space less than 1, then the InputStream is empty.
     *  </ul>
     *  The Data will be created on the fly and will not stored in the memory.
     *  @param  size
     *  @return the created readable InputStream.
     */
    public static InputStream createInputStream(final int size) {
        return new InputStream() {
            private int counter;
            @Override
            public int read() throws IOException {
                try {
                    if (this.counter > size -1)
                        return -1;
                    else if (this.counter == size -1)
                        return 'E';
                    else if (this.counter > 0)
                        return '-';
                    else return 'A'; 
                    
                } finally {
                    this.counter++;
                }
            }
        };
    }
}