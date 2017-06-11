/**
 *  LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 *  im Folgenden Seanox Software Solutions oder kurz Seanox genannt. Diese
 *  Software unterliegt der Version 2 der GNU General Public License.
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

import javax.net.ssl.SSLException;

import org.junit.Assert;
import org.junit.Test;

import com.seanox.test.utils.HttpUtils;
import com.seanox.test.utils.Pattern;

/**
 *  TestCases for {@link com.seanox.devwex.Server}.
 */
public class ServerTest_Connection extends AbstractTest {

    /** 
     *  TestCase for aceptance.
     *  Connections with SSL/TLS must works.
     *  Cross connections does not work.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_01() throws Exception {
        
        String response;
         
        String request = "GET / HTTP/1.0\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:443", request));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_DIFFUSE));

        response = new String(HttpUtils.sendRequest("127.0.0.1:443", request, AbstractSuite.getKeystore()));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Connections with SSL/TLS must works.
     *  Cross connections does not work.
     *  @throws Exception
     */        
    @Test(expected=SSLException.class)
    public void testAceptance_02() throws Exception {

        String request = "GET / HTTP/1.0\r\n"
                + "\r\n";
        HttpUtils.sendRequest("127.0.0.1:80", request, AbstractSuite.getKeystore());
        Assert.fail();
    } 
    
    //TODO: Test SSL/TLS + Client Certificate
    //https://docs.oracle.com/cd/E19509-01/820-3503/ggezy/index.html
    //https://docs.oracle.com/cd/E19509-01/820-3503/6nf1il6er/index.html
}