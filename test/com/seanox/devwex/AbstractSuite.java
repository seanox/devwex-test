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

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.seanox.test.utils.OutputStreamTail;
import com.seanox.test.utils.OutputStreams;

/**
 *  Abstract class to implements and use test suites.
 */
abstract class AbstractSuite {
    
    /** path of {@code ./stage} */
    private static final String DIRECTORY_STAGE = "./stage";

    /** path of {@code ./stage/program} */
    private static final String DIRECTORY_STAGE_PROGRAM = "./stage/program";
    
    /** path of {@code ./stage/libraries} */
    private static final String DIRECTORY_STAGE_LIBRARIES = "./stage/libraries";
    
    /** path of {@code ./resources} */
    private static final String DIRECTORY_RESOURCES = "./resources";    
    
    /** path of {@code ./resources/program} */
    private static final String DIRECTORY_RESOURCES_PROGRAM = "./resources/program";
    
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
    
    /**
     *  Execution with the loading of a new test class for the initialization
     *  of the test environment.
     *  @throws IOException
     */
    @BeforeClass
    public static void onBeforeClass() throws IOException {
        
        final File root = new File(".").getCanonicalFile();

        final File rootResources = new File(root, DIRECTORY_RESOURCES).getCanonicalFile();
        final File rootResourcesProgram = new File(root, DIRECTORY_RESOURCES_PROGRAM).getCanonicalFile();
        
        final File rootStage = new File(root, DIRECTORY_STAGE).getCanonicalFile();
        final File rootStageProgram = new File(root, DIRECTORY_STAGE_PROGRAM).getCanonicalFile();
        final File rootStageLibraries = new File(root, DIRECTORY_STAGE_LIBRARIES).getCanonicalFile();
        
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
        
        systemOutLog = new FileOutputStream(new File(root, DIRECTORY_STAGE + "/output.log")); 
        System.setOut(new PrintStream(new OutputStreams(systemOut, systemOutTail, systemOutLog)));
        
        Files.copy(Paths.get(rootResourcesProgram.toString(), "devwex.ini"), Paths.get("./devwex.ini"), StandardCopyOption.REPLACE_EXISTING);
        
        String libraries = rootStageLibraries.toString();
        System.setProperty("libraries", libraries);
        
        accessLog.createNewFile();
        outputLog.createNewFile();
        
        Service.main(new String[] {"start*"});
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
        final File rootStage = new File(root, DIRECTORY_STAGE).getCanonicalFile();
        
        return new String(Files.readAllBytes(Paths.get(rootStage.toString(), "access.log")));
    }

    /**
     *  Returns the content of the output log.
     *  @return the content of the output log
     */    
    static String getOutputLog() throws IOException {
        
        final File root = new File(".").getCanonicalFile();
        final File rootStage = new File(root, DIRECTORY_STAGE).getCanonicalFile();
        
        return new String(Files.readAllBytes(Paths.get(rootStage.toString(), "output.log")));
    }
    
    /**
     *  Returns the root stage as file.
     *  @return the root stage as file
     *  @throws IOException
     */
    static File getRootStage() throws IOException {
        
        final File root = new File(".").getCanonicalFile();
        final File rootStage = new File(root, DIRECTORY_STAGE).getCanonicalFile();
        
        return rootStage;
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
}