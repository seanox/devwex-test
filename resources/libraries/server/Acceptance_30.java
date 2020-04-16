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
package server;

import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import com.seanox.devwex.Initialize;
import com.seanox.devwex.Section;
import com.seanox.devwex.Service;

public class Acceptance_30 implements Runnable {

    private volatile ServerSocket socket;

    private volatile Socket accept;

    private volatile String caption;

    public Acceptance_30(String context, Object data) throws Throwable {

        InetAddress address;
        Section     options;

        int         port;

        context = context == null ? "" : context.trim();
        options = ((Initialize)data).get(context);
        
        try {port = Integer.parseInt(options.get("port"));
        } catch (Throwable throwable) {
            port = 0;
        }
        
        context = options.get("address", "auto").toLowerCase();
        address = context.equals("auto") ? null : InetAddress.getByName(context);
        
        this.socket = new ServerSocket(port, 0, address);
        this.socket.setSoTimeout(1000);
        this.caption = ("TCP ").concat(this.socket.getInetAddress().getHostAddress()).concat(":").concat(String.valueOf(port));
    }
    
    public String explain() {
        return this.caption;
    }

    public void destroy() {
        
        try {this.socket.close();
        } catch (Throwable throwable) {
        }
    }

    public void run() {

        Service.print(("SERVER ").concat(this.caption).concat(" READY"));
        
        Thread thread = null;
        while (!this.socket.isClosed())
            try {Thread.sleep(250);
            } catch (Throwable throwable) {
                break;
            }
        
        Service.print(("SERVER ").concat(this.caption).concat(" STOPPED"));
    }
}