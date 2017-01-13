/**
 *  LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 *  im Folgenden Seanox Software Solutions oder kurz Seanox genannt.
 *  Diese Software unterliegt der Version 2 der GNU General Public License.
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

/**
 *  Pattern for regular expressions.
 */
public class Pattern {

    /** cross platform line break */
    public static final String LINE_BREAK = "(?:(?:\\r\\n)|(?:\\n\\r)|[\\r\\n])";
    
    /** spaces without line break */
    public static final String LINE_SPACE = "[^\\S\\r\\n]";
    
    /** 
     *  Pattern for netweork connection.<br>
     *  Format: {@code host:port}<br>
     *  Grouping:
     *  <dir>
     *    0: match<br>
     *    1: host<br>
     *    2: port<br>
     *  </ul> 
     */
    public static final String NETWORK_CONNECTION = "^(?i:([a-z_\\-\\d\\.:]+):(\\d{1,5}))$";

    /** 
     *  Pattern for a HTTP response.<br>
     *  Grouping:
     *  <dir>
     *    0: match<br>
     *    1: header<br>
     *    2: body<br>
     *  </ul> 
     */
    public static final String HTTP_RESPONSE = "(?s)^(.*?)(?:(?:\r\n){2})(.*)$";
}