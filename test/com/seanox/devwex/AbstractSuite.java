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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.seanox.test.utils.HttpUtils.Keystore;
import com.seanox.test.utils.OutputStreamTail;
import com.seanox.test.utils.OutputStreams;

/**
 *  Abstract class to implements and use test suites.
 */
public abstract class AbstractSuite {
    
    /** path of {@code ./stage} */
    private static final String PATH_STAGE = "./stage";

    /** path of {@code ./stage/libraries} */
    private static final String PATH_STAGE_LIBRARIES = "./stage/libraries";

    /** path of {@code ./stage/certificates} */
    private static final String PATH_STAGE_CERTIFICATES = "./stage/certificates";

    /** path of {@code ./resources} */
    private static final String PATH_RESOURCES = "./resources";    
    
    /** path of {@code ./resources/program} */
    private static final String PATH_RESOURCES_PROGRAM = "./resources/program";
    
    /** internal counter of executed test units */
    private static volatile int counter;
    
    /** internal shared new system and error output stream  */
    private static volatile OutputStream systemOutLog;

    /** original system output stream */
    private static volatile PrintStream systemOut;
    
    /** tail of system output stream*/
    private static volatile OutputStreamTail systemOutTail;

    /** original error output stream */
    private static volatile PrintStream systemErr;

    /** tail of error output stream*/
    private static volatile OutputStreamTail systemErrTail;
    
    /** keystore for SSL connections */
    private static volatile Keystore keystore;
    
    /**
     *  Execution with the loading of a new test class for the initialization
     *  of the test environment.
     *  @throws IOException
     */
    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void onBeforeClass() throws IOException {
        
        final File root = new File(".").getCanonicalFile();

        final File rootStage = new File(root, PATH_STAGE).getCanonicalFile();
        
        File accessLog = new File(rootStage, "access.log");
        File outputLog = new File(rootStage, "output.log");
        
        if (++counter > 1)
            return;

        systemOut = System.out;
        systemOutTail = new OutputStreamTail();
        System.setOut(new PrintStream(new OutputStreams(systemOut, systemOutTail)));

        systemErr = System.err;
        systemErrTail = new OutputStreamTail();
        System.setErr(new PrintStream(new OutputStreams(systemErr, systemErrTail)));
        
        if (Files.exists(rootStage.toPath()))
            Files.walkFileTree(rootStage.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attributes)
                        throws IOException {
                    if (Files.exists(path))
                        Files.delete(path);
                    return FileVisitResult.CONTINUE;
                }
    
                @Override
                public FileVisitResult postVisitDirectory(Path path, IOException exception)
                        throws IOException {
                    if (Files.exists(path))
                        Files.delete(path);                
                    return FileVisitResult.CONTINUE;
                }
            });
        
        rootStage.mkdir();
        
        final File rootResources = new File(root, PATH_RESOURCES).getCanonicalFile();

        final File rootResourcesProgram = new File(root, PATH_RESOURCES_PROGRAM).getCanonicalFile();
        Files.walkFileTree(rootResources.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attributes)
                    throws IOException {
                Files.createDirectories(rootStage.toPath().resolve(rootResources.toPath().relativize(path)));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attributes)
                    throws IOException {
                Files.copy(path, rootStage.toPath().resolve(rootResources.toPath().relativize(path)));
                return FileVisitResult.CONTINUE;
            }
        });
        
        systemOutLog = new FileOutputStream(new File(root, PATH_STAGE + "/output.log")); 
        System.setOut(new PrintStream(new OutputStreams(systemOut, systemOutTail, systemOutLog)));
        
        Files.copy(Paths.get(rootResourcesProgram.toString(), "devwex.ini"), Paths.get("./devwex.ini"), StandardCopyOption.REPLACE_EXISTING);
        
        String content = new String(Files.readAllBytes(Paths.get("./devwex.ini")));
        Initialize initialize = Initialize.parse(content);
        List<String> sectionList = Collections.list(initialize.elements());
        for (String sectionName : sectionList) {
            if (!sectionName.matches("(?i)server:.*:ssl$"))
                continue;
            Section section = initialize.get(sectionName);
            if (!section.contains("keystore")
                    || !section.contains("password"))
                continue;
            String keystore = section.get("keystore");
            String password = section.get("password");
            AbstractSuite.keystore = new Keystore() {

                public String getPassword() {
                    return password;
                }
                
                public File getFile() {
                    return new File(keystore);
                }
            };
            break;
        }
        
        final File rootStageLibraries = new File(root, PATH_STAGE_LIBRARIES).getCanonicalFile();
        String libraries = rootStageLibraries.toString();
        System.setProperty("libraries", libraries);
        
        accessLog.createNewFile();
        outputLog.createNewFile();
        
        Service.main(new String[] {"start"});
    }

    /**
     *  Execution at the end of a test class to stop the test environment.
     *  @throws IOException
     */
    @AfterClass
    public static void onAfterClass() {
        
        if (--counter > 0)
            return;
        System.setOut(systemOut);
        System.setErr(systemErr);
    }
    
    /**
     *  Returns the tail of system output stream.
     *  @return the tail of system output stream
     */
    static String getOutTail() {
        return AbstractSuite.systemOutTail.toString();
    }

    /**
     *  Returns the tail of error output stream.
     *  @return the tail of error output stream
     */
    static String getErrTail() {
        return AbstractSuite.systemErrTail.toString();
    }
    
    /**
     *  Returns the content of the access log.
     *  @return the content of the access log
     */    
    static String getAccessLog() throws IOException {
        
        final File root = new File(".").getCanonicalFile();
        final File rootStage = new File(root, PATH_STAGE).getCanonicalFile();
        
        return new String(Files.readAllBytes(Paths.get(rootStage.toString(), "access.log")));
    }

    /**
     *  Returns the content of the output log.
     *  @return the content of the output log
     */    
    static String getOutputLog() throws IOException {
        
        final File root = new File(".").getCanonicalFile();
        final File rootStage = new File(root, PATH_STAGE).getCanonicalFile();
        
        return new String(Files.readAllBytes(Paths.get(rootStage.toString(), "output.log")));
    }
    
    /**
     *  Returns the root stage as file.
     *  @return the root stage as file
     *  @throws IOException
     */
    static File getRoot() throws IOException {
        return new File(".").getCanonicalFile();
    }
    
    /**
     *  Returns the root stage as file.
     *  @return the root stage as file
     *  @throws IOException
     */
    static File getRootStage() throws IOException {
        return new File(AbstractSuite.getRoot(), PATH_STAGE).getCanonicalFile();
    }

    /**
     *  Returns the root stage access log as file.
     *  @return the root stage access log as file
     *  @throws IOException
     */
    static File getRootStageAccessLog() throws IOException {
        return new File(AbstractSuite.getRootStage(), "access.log");
    }

    /**
     *  Returns the root stage output log as file.
     *  @return the root stage output log as file
     *  @throws IOException
     */
    static File getRootStageOutputLog() throws IOException {
        return new File(AbstractSuite.getRootStage(), "output.log");
    }
    
    /**
     *  Returns the root stage certificates directory as file.
     *  @return the root stage certificates directory as file
     *  @throws IOException
     */
    static File getRootStageCertificates() throws IOException {
        return new File(AbstractSuite.getRoot(), PATH_STAGE_CERTIFICATES);
    }

    /**
     *  Returns the last entry of the access log.
     *  @return the last entry of the access log
     *  @throws IOException
     */
    static String getAccessLogTail() throws IOException {
    
        String[] accessLog = AbstractSuite.getAccessLog().split("[\r\n]+");
        return accessLog[accessLog.length -1];
    }

    /**
     *  Returns the last entry of the output.
     *  @return the last entry of the output
     *  @throws IOException
     */
    static String getOutputLogTail() throws IOException {
    
        String[] outputLog = AbstractSuite.getOutputLog().split("[\r\n]+");
        List<String> outputLogList = Arrays.asList(outputLog);
        Collections.reverse(outputLogList);
        LinkedList<String> outputLogTailList = new LinkedList<>();
        for (String log : outputLogList) {
            outputLogTailList.addFirst(log);
            if (log.matches("^\\d{4}(-\\d{2}){2} \\d{2}(:\\d{2}){2}((\\s.*)|$)"))
                break;
        }
        return String.join("\r\n", outputLogTailList.toArray(new String[0]));
    }
    
    /**
     *  Returns the current memory usage.
     *  @return the current memory usage
     */
    static long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() -runtime.freeMemory();
    }

    /**
     *  Wait until the OutputStream is ready.
     *  @throws InterruptedException
     *  @throws IOException
     */
    static void waitOutputReady() throws InterruptedException, IOException {
        
        String shadow = null;
        long timeout = System.currentTimeMillis();
        while (System.currentTimeMillis() -timeout < 3000) {
            Thread.sleep(50);
            String accessLog = AbstractSuite.getAccessLogTail();
            String outputLog = AbstractSuite.getOutputLogTail();
            if (!(accessLog + "\0" + outputLog).equals(shadow))
                timeout = System.currentTimeMillis();
            shadow = (accessLog + "\0" + outputLog);
        }
    }

    /**
     *  Returns the default keystore for SSL connections.
     *  @return the default keystore for SSL connections
     */    
    public static Keystore getKeystore() {
        return AbstractSuite.keystore;
    }
}