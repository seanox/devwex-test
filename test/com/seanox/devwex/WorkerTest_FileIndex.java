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
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.seanox.test.utils.HttpUtils;
import com.seanox.test.utils.Pattern;

/**
 *  TestCases for {@link com.seanox.devwex.Worker}.
 */
public class WorkerTest_FileIndex extends AbstractTest {
    
    /** 
     *  Preparation of the runtime environment.
     *  @throws Exception
     */
    @BeforeClass
    public static void oneBeforeClass() throws Exception {
        
        final File rootStage = new File(AbstractSuite.getRootStage(), "/documents/empty").getCanonicalFile();
        Files.walkFileTree(rootStage.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attributes)
                    throws IOException {
                if (path.toFile().getName().contains("ignore"))
                        path.toFile().delete();
                else if (path.toFile().getName().contains("hidden"))
                        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    
    /** 
     *  TestCase for acceptance.
     *  Without sorting, the file index of directories must not contain '?'.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_01() throws Exception {
        
        String request = "GET / HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8081", request));

        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(header.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2") + "\r\n";
        Assert.assertTrue(body.contains("\r\nindex of: \r\n"));
        Assert.assertTrue(body.contains("\r\norder by: na\r\n"));
        Assert.assertFalse(body.contains("?"));
        
        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));  
    }
    
    /** 
     *  TestCase for acceptance.
     *  With sorting, the file index of directories must not contain '?'.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_02() throws Exception {
        
        String request = "GET /?d HTTP/1.0\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8081", request));

        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(header.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2") + "\r\n";
        Assert.assertTrue(body.contains("\r\nindex of: \r\n"));
        Assert.assertTrue(body.contains("\r\norder by: da\r\n"));
        Assert.assertFalse(body.contains("?"));
        
        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));  
    }
    
    /** 
     *  TestCase for acceptance.
     *  The path from subdirectories must be created correctly.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_03() throws Exception {
        
        String request = "GET /test_a/test/ HTTP/1.0\r\n"
                + "Host: vHb\r\n"
                + "\r\n";
        String response = new String(HttpUtils.sendRequest("127.0.0.1:8081", request));

        String header = response.replaceAll(Pattern.HTTP_RESPONSE, "$1");
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_STATUS_200));
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE));
        Assert.assertTrue(header.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
        Assert.assertFalse(header.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE));
        
        String body = "\r\n" + response.replaceAll(Pattern.HTTP_RESPONSE, "$2") + "\r\n";
        Assert.assertTrue(body.contains("\r\nindex of: /test_a/test\r\n"));
        Assert.assertTrue(body.contains("\r\norder by: na x\r\n"));
        Assert.assertFalse(body.contains("?"));
        
        Thread.sleep(50);
        String accessLog = AbstractSuite.getAccessLogTail();
        Assert.assertTrue(accessLog.matches(Pattern.ACCESS_LOG_STATUS_200));  
    }
    
    /** 
     *  TestCase for acceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] INDEX = ON}
     *  Hidden files must be included in the index.
     *  The flag {@code x} must be set for an enpty directory.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_04() throws Exception {
        
        for (int loop = 1; loop <= 3; loop++) {
            String request = "GET /empty/" + loop + "/ HTTP/1.0\r\n"
                    + "\r\n";
            String response = new String(HttpUtils.sendRequest("127.0.0.1:80", request));
            if (loop == 1) {
                Assert.assertTrue(response.contains("\r\norder by: na x\r\n"));
                Assert.assertFalse(response.contains("case:file"));
            }
            if (loop == 2) {
                Assert.assertTrue(response.contains("\r\norder by: na\r\n"));
                Assert.assertTrue(response.contains("case:file"));
            }
            if (loop == 3) {
                Assert.assertTrue(response.contains("\r\norder by: na\r\n"));
                Assert.assertTrue(response.contains("case:file"));
            }
        }
    }
    
    /** 
     *  TestCase for acceptance.
     *  Configuration: {@code [SERVER/VIRTUAL:BAS] INDEX = ON [S]}
     *  The index must not contain hidden files.
     *  The flag {@code x} must be set for an enpty directory.
     *  @throws Exception
     */      
    @Test
    public void testAcceptance_05() throws Exception {
        
        for (int loop = 1; loop <= 3; loop++) {
            String request = "GET /empty/" + loop + "/ HTTP/1.0\r\n"
                    + "\r\n";
            String response = new String(HttpUtils.sendRequest("127.0.0.1:8082", request));
            if (loop == 1) {
                Assert.assertTrue(response.contains("\r\norder by: na x\r\n"));
                Assert.assertFalse(response.contains("case:file"));
            }
            if (loop == 2) {
                Assert.assertTrue(response.contains("\r\norder by: na x\r\n"));
                Assert.assertFalse(response.contains("case:file"));
            }
            if (loop == 3) {
                Assert.assertTrue(response.contains("\r\norder by: na\r\n"));
                Assert.assertTrue(response.contains("case:file"));
            }
        }
    }    
}