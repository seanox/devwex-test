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
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.junit.Assert;
import org.junit.Test;

import com.seanox.test.utils.HttpUtils;
import com.seanox.test.utils.Pattern;

/**
 *  Test cases for {@link com.seanox.devwex.Server}.<br>
 *  <br>
 *  ServerTest_Connection 5.1 20171231<br>
 *  Copyright (C) 2017 Seanox Software Solutions<br>
 *  All rights reserved.
 *
 *  @author  Seanox Software Solutions
 *  @version 5.1 20171231
 */
public class ServerTest_Connection extends AbstractTest {

    /** 
     *  Test case for acceptance.
     *  Connections with SSL/TLS must works.
     *  Cross connections does not work.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_01() throws Exception {
        
        String response;
         
        String request = "GET / HTTP/1.0\r\n"
                + "\r\n";
        response = new String(HttpUtils.sendRequest("127.0.0.1:443", request));
        Assert.assertFalse(response.matches(Pattern.HTTP_RESPONSE_DIFFUSE));

        response = new String(HttpUtils.sendRequest("127.0.0.1:443", request, AbstractSuite.getKeystore()));
        Assert.assertTrue(response.matches(Pattern.HTTP_RESPONSE_STATUS_200));
    } 
    
    /** 
     *  Test case for acceptance.
     *  Connections with SSL/TLS must works.
     *  Cross connections does not work.
     *  @throws Exception
     */        
    @Test(expected=SSLException.class)
    public void testAcceptance_02() throws Exception {

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
    private static void initHttpsMutualAuthenticationUrlConnection(File certificate, String password) throws Exception {
        
        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        clientStore.load(new FileInputStream(certificate), password.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientStore, ("changeIt").toCharArray());
        KeyManager[] kms = kmf.getKeyManagers();

        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        trustStore.load(new FileInputStream(new File(AbstractSuite.getRootStageCertificates(), "client.keystore")), ("changeIt").toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        TrustManager[] tms = tmf.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(kms, tms, new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession sslSession) {
                return true;
            }
        });        
    }    
    
    /** 
     *  Test case for acceptance.
     *  Configuration: {@code CLIENTAUTH = OFF} 
     *  Connection without client certificate must works.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_03() throws Exception {
        
        ServerTest_Connection.initHttpsUrlConnection();
        URL url = new URL("https://127.0.0.1");
        HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
        Assert.assertEquals(200, urlConn.getResponseCode());
    }
    
    /** 
     *  Test case for acceptance.
     *  Configuration: {@code CLIENTAUTH = OFF} 
     *  Connection with client certificate must ignore.
     *  @throws Exception
     */     
    public void testAcceptance_04() throws Exception {
        
        File certificate = new File(AbstractSuite.getRootStageCertificates(), "client_a.p12");
        ServerTest_Connection.initHttpsMutualAuthenticationUrlConnection(certificate, "changeIt");
        URL url = new URL("https://127.0.0.1");
        HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
        Assert.assertEquals(200, urlConn.getResponseCode());
    }
    
    /** 
     *  Test case for acceptance.
     *  Configuration: {@code CLIENTAUTH = OFF} 
     *  Connection with a unknown client certificate must ignore.
     *  @throws Exception
     */     
    public void testAcceptance_05() throws Exception {
        
        File certificate = new File(AbstractSuite.getRootStageCertificates(), "client_x.p12");
        ServerTest_Connection.initHttpsMutualAuthenticationUrlConnection(certificate, "changeIt");
        URL url = new URL("https://127.0.0.1");
        HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
        Assert.assertEquals(200, urlConn.getResponseCode());
    }      
    
    /** 
     *  Test case for acceptance.
     *  Configuration: {@code CLIENTAUTH = ON} 
     *  Connection without client certificate must fail.
     *  @throws Exception
     */     
    @Test(expected=SSLException.class)
    public void testAcceptance_06() throws Exception {
        
        ServerTest_Connection.initHttpsUrlConnection();
        URL url = new URL("https://127.0.0.2");
        HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
        Assert.assertNotEquals(200, urlConn.getResponseCode());
    }       
    
    /** 
     *  Test case for acceptance.
     *  Configuration: {@code CLIENTAUTH = ON} 
     *  Connection with client certificate must works.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_07() throws Exception {
        
        File certificate = new File(AbstractSuite.getRootStageCertificates(), "client_a.p12");
        ServerTest_Connection.initHttpsMutualAuthenticationUrlConnection(certificate, "changeIt");
        URL url = new URL("https://127.0.0.2");
        HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
        Assert.assertEquals(200, urlConn.getResponseCode());
    }   
    
    /** 
     *  Test case for acceptance.
     *  Configuration: {@code CLIENTAUTH = AUTO} 
     *  Connection without client certificate must works.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_08() throws Exception {
        
        ServerTest_Connection.initHttpsUrlConnection();
        URL url = new URL("https://127.0.0.3");
        HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
        Assert.assertEquals(200, urlConn.getResponseCode());
    } 
    
    /** 
     *  Test case for acceptance.
     *  Configuration: {@code CLIENTAUTH = AUTO} 
     *  Connection with client certificate must works.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_09() throws Exception {

        File certificate = new File(AbstractSuite.getRootStageCertificates(), "client_a.p12");
        ServerTest_Connection.initHttpsMutualAuthenticationUrlConnection(certificate, "changeIt");
        URL url = new URL("https://127.0.0.3");
        HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
        Assert.assertEquals(200, urlConn.getResponseCode());
    } 
    
    /** 
     *  Test case for acceptance.
     *  Configuration: {@code CLIENTAUTH = OFF} 
     *  Connection with known and unknown client certificates must works.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_10() throws Exception {

        for (char client : ("abcx").toCharArray()) {
            File certificate = new File(AbstractSuite.getRootStageCertificates(), "client_" + client + ".p12");
            ServerTest_Connection.initHttpsMutualAuthenticationUrlConnection(certificate, "changeIt");
            URL url = new URL("https://127.0.0.1");
            HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
            Assert.assertEquals(200, urlConn.getResponseCode());
        }
    } 
    
    /** 
     *  Test case for acceptance.
     *  Configuration: {@code CLIENTAUTH = ON} 
     *  Connection with known client certificates must works.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_11() throws Exception {

        for (char client : ("abc").toCharArray()) {
            File certificate = new File(AbstractSuite.getRootStageCertificates(), "client_" + client + ".p12");
            ServerTest_Connection.initHttpsMutualAuthenticationUrlConnection(certificate, "changeIt");
            URL url = new URL("https://127.0.0.2");
            HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
            Assert.assertEquals(200, urlConn.getResponseCode());
        }
    }     

    /** 
     *  Test case for acceptance.
     *  Configuration: {@code CLIENTAUTH = ON} 
     *  Connection with unknown client certificate must fail.
     *  @throws Exception
     */      
    @Test(expected=SSLException.class)
    public void testAcceptance_12() throws Exception {
            
        File certificate = new File(AbstractSuite.getRootStageCertificates(), "client_x.p12");
        ServerTest_Connection.initHttpsMutualAuthenticationUrlConnection(certificate, "changeIt");
        URL url = new URL("https://127.0.0.2");
        HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
        Assert.assertNotEquals(200, urlConn.getResponseCode());
    }     
    
    /** 
     *  Test case for acceptance.
     *  Configuration: {@code CLIENTAUTH = AUTO}
     *  Connection with known and unknow client certificates must work.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_13() throws Exception {

        for (char client : ("abcx").toCharArray()) {
            File certificate = new File(AbstractSuite.getRootStageCertificates(), "client_" + client + ".p12");
            ServerTest_Connection.initHttpsMutualAuthenticationUrlConnection(certificate, "changeIt");
            URL url = new URL("https://127.0.0.3");
            HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
            Assert.assertEquals(200, urlConn.getResponseCode());
        }
    }
    
    /** 
     *  Test case for acceptance.
     *  Configuration: {@code CLIENTAUTH = AUTO} + {@code MUTUAL AUTH = ALL IS EMPTY auth_cert} 
     *  Connection with client certificate must works.
     *  @throws Exception
     */     
    @Test
    public void testAcceptance_14() throws Exception {

        for (char client : ("abc").toCharArray()) {
            File certificate = new File(AbstractSuite.getRootStageCertificates(), "client_" + client + ".p12");
            ServerTest_Connection.initHttpsMutualAuthenticationUrlConnection(certificate, "changeIt");
            URL url = new URL("https://127.0.0.4");
            HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
            Assert.assertEquals(200, urlConn.getResponseCode());
        }
    }    
    
    /** 
     *  Test case for acceptance.
     *  Configuration: {@code CLIENTAUTH = AUTO} + {@code MUTUAL AUTH = ALL IS EMPTY auth_cert} 
     *  Connection with a unknown client certificate must be denied with status
     *  403.
     *  @throws Exception
     */     
    public void testAcceptance_15() throws Exception {
            
        File certificate = new File(AbstractSuite.getRootStageCertificates(), "client_x.p12");
        ServerTest_Connection.initHttpsMutualAuthenticationUrlConnection(certificate, "changeIt");
        URL url = new URL("https://127.0.0.4");
        HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
        Assert.assertEquals(403, urlConn.getResponseCode());
    }  
    
    /** 
     *  Test case for acceptance.
     *  Configuration: {@code CLIENTAUTH = AUTO} + {@code MUTUAL AUTH = ALL IS EMPTY auth_cert} 
     *  Connection without a client certificate must works.
     *  @throws Exception
     */     
    public void testAcceptance_16() throws Exception {
        
        ServerTest_Connection.initHttpsUrlConnection();
        URL url = new URL("https://127.0.0.4");
        HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
        Assert.assertEquals(403, urlConn.getResponseCode());
    }    
}