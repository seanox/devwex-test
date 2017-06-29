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

import java.io.File;
import java.io.FileInputStream;
import java.net.SocketException;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

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
    
    /**
     *  Initializes the HttpsURLConnection without a client certificate.
     *  @throws Exception
     */
    private static void initHttpsUrlConnection() throws Exception {
        
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] {new X509TrustManager() {
            
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
                return;
            }
            
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
                return;
            }
        }}, null);        

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession sslSession) {
                return true;
            }
        });     
    }
    
    /**
     *  Initializes the HttpsURLConnection with a client certificate.
     *  @throws Exception
     */    
    private static void initHttpsMutualAuthenticationUrlConnection() throws Exception {
        
        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        clientStore.load(new FileInputStream(new File(AbstractSuite.getRootStageCertificates(), "client.p12")), ("changeIt").toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientStore, ("changeIt").toCharArray());
        KeyManager[] kms = kmf.getKeyManagers();

        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(new File(AbstractSuite.getRootStageCertificates(), "client_ma.jks")), ("changeIt").toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        TrustManager[] tms = tmf.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kms, tms, new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession sslSession) {
                return true;
            }
        });        
    }    
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code CLIENTAUTH = OFF} 
     *  SSL/TLS Connection without client certificate (handshake) must works.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_03() throws Exception {
        
        ServerTest_Connection.initHttpsUrlConnection();
        URL url = new URL("https://127.0.0.1");
        HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
        Assert.assertEquals(200, urlConn.getResponseCode());
    }
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code CLIENTAUTH = OFF} 
     *  SSL/TLS Connection with client certificate (handshake) must fail.
     *  @throws Exception
     */     
    @Test(expected=SSLHandshakeException.class)
    public void testAceptance_04() throws Exception {
        
        ServerTest_Connection.initHttpsMutualAuthenticationUrlConnection();
        URL url = new URL("https://127.0.0.1");
        HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
        Assert.assertNotEquals(200, urlConn.getResponseCode());
    }    
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code CLIENTAUTH = ON} 
     *  SSL/TLS Connection without client certificate (handshake) must fail.
     *  @throws Exception
     */     
    @Test(expected=SocketException.class)
    public void testAceptance_05() throws Exception {
        
        ServerTest_Connection.initHttpsUrlConnection();
        URL url = new URL("https://127.0.0.2");
        HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
        Assert.assertNotEquals(200, urlConn.getResponseCode());
    }       
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code CLIENTAUTH = ON} 
     *  SSL/TLS Connection with client certificate (handshake) must works.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_06() throws Exception {
        
        ServerTest_Connection.initHttpsMutualAuthenticationUrlConnection();
        URL url = new URL("https://127.0.0.2");
        HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
        Assert.assertEquals(200, urlConn.getResponseCode());
    }   
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code CLIENTAUTH = AUTO} 
     *  SSL/TLS Connection without client certificate (handshake) must works.
     *  @throws Exception
     */      
    @Test
    public void testAceptance_07() throws Exception {
        
        ServerTest_Connection.initHttpsUrlConnection();
        URL url = new URL("https://127.0.0.3");
        HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
        Assert.assertEquals(200, urlConn.getResponseCode());
    } 
    
    /** 
     *  TestCase for aceptance.
     *  Configuration: {@code CLIENTAUTH = AUTO} 
     *  SSL/TLS Connection with client certificate (handshake) must works.
     *  @throws Exception
     */     
    @Test
    public void testAceptance_08() throws Exception {

        ServerTest_Connection.initHttpsMutualAuthenticationUrlConnection();
        URL url = new URL("https://127.0.0.3");
        HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
        Assert.assertEquals(200, urlConn.getResponseCode());
    }   
}