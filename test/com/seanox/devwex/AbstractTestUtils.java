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
package com.seanox.devwex;

import java.io.IOException;

import com.seanox.test.utils.OutputFacadeStream;
import com.seanox.test.utils.Pattern;

/**
 *  Utils for AbstractTest.<br>
 *  <br>
 *  AbstractTestUtils 5.0.1 20171231<br>
 *  Copyright (C) 2017 Seanox Software Solutions<br>
 *  All rights reserved.
 *
 *  @author  Seanox Software Solutions
 *  @version 5.0.1 20171231
 */
class AbstractTestUtils {
    
    private AbstractTestUtils() {
    }

    /**
     *  Waits for the end of the output in a data stream.
     *  @param  output OutputFacadeStream
     *  @throws IOException
     *  @throws InterruptedException
     */
    static void waitOutputFacadeStream(OutputFacadeStream output)
            throws IOException, InterruptedException {
        AbstractTestUtils.waitOutputFacadeStream(output, 1000);
    }

    /**
     *  Waits for the end of the output in a data stream.
     *  @param  output  OutputFacadeStream
     *  @param  timeout timeout in milliseconds
     *  @throws IOException
     *  @throws InterruptedException
     */    
    static void waitOutputFacadeStream(OutputFacadeStream output, long timeout)
            throws IOException, InterruptedException {
        
        if (timeout < 0)
            throw new IllegalArgumentException("Invalid timeout");
        try (OutputFacadeStream.Capture capture = output.capture()) {
            long timing = System.currentTimeMillis();
            while (System.currentTimeMillis() -timing < timeout) {
                Thread.sleep(AbstractTest.SLEEP);
                if (capture.size() > 0)
                    timing = System.currentTimeMillis(); 
                capture.reset();
            }
        }
    }
    
    /**
     *  Returns the last line of a string.
     *  @param  string
     *  @return the last line of a string
     */
    static String tail(String string) {
        
        if (string == null)
            return null;
        String[] lines = string.split(Pattern.LINE_BREAK);
        if (lines.length <= 0)
            return "";
        return lines[lines.length -1];
    }
}