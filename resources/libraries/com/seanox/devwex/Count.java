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
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Count implements Runnable {

    private volatile ServerSocket socket;

    private volatile Socket accept;

    private volatile String caption;

    private volatile long count;

    public Count(String server, Object data) throws Throwable {

        InetAddress address;
        Section     options;

        int         port;

        server  = server == null ? "" : server.trim();
        options = ((Initialize)data).get(server.concat(":bas"));
        
        try {port = Integer.parseInt(options.get("port"));
        } catch (Throwable throwable) {
            port = 0;
        }
        
        server  = options.get("address", "auto").toLowerCase();
        address = server.equals("auto") ? null : InetAddress.getByName(server);
        
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
        while (!this.socket.isClosed()) {
            
            try {
                if (thread == null
                        || !thread.isAlive()) {
                    this.accept = this.socket.accept();
                    thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                String string = String.valueOf(++Count.this.count) + " " + this.getClass().getName(); 
                                Count.this.accept.setSoTimeout(10000);
                                Count.this.accept.getOutputStream().write(string.getBytes());
                            } catch (Exception exception) {
                                Service.print(exception);
                            } finally {
                                try {Count.this.accept.close();
                                } catch (IOException exception) {
                                    Service.print(exception);
                                }
                            }                            
                        }
                    };
                    thread.start();
                }
                
                try {Thread.sleep(250);
                } catch (Throwable throwable) {
                    this.destroy();
                }
                
            } catch (SocketException exception) {
                
                this.destroy();
                
            } catch (InterruptedIOException exception) {
                
                continue;
                
            } catch (Throwable throwable) {
                
                Service.print(throwable);
                try {this.accept.close();
                } catch (Throwable exception) {
                }
                try {Thread.sleep(25);
                } catch (Throwable throwable1) {
                    this.destroy();
                }
            }
        }
        
        Service.print(("SERVER ").concat(this.caption).concat(" STOPPED"));
    }
}