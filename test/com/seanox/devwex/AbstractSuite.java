/**
 * LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 * im Folgenden Seanox Software Solutions oder kurz Seanox genannt.
 * Diese Software unterliegt der Version 2 der GNU General Public License.
 *
 * Devwex, Advanced Server Development
 * Copyright (C) 2021 Seanox Software Solutions
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
package com.seanox.devwex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;

import com.seanox.test.utils.HttpUtils.Keystore;
import com.seanox.test.utils.OutputFacadeStream;

/**
 * Abstract class to implements and use test suites.<br>
 * <br>
 * AbstractSuite 5.3.0 20210314<br>
 * Copyright (C) 2021 Seanox Software Solutions<br>
 * All rights reserved.
 *
 * @author  Seanox Software Solutions
 * @version 5.3.0 20210314
 */
public class AbstractSuite extends com.seanox.test.AbstractSuite {
    
    /** path of {@code ./stage} */
    final static String PATH_STAGE = "./stage";

    /** path of {@code ./stage/libraries} */
    final static String PATH_STAGE_LIBRARIES = "./stage/libraries";

    /** path of {@code ./stage/certificates} */
    final static String PATH_STAGE_CERTIFICATES = "./stage/certificates";

    /** path of {@code ./stage/output.log} */
    final static String PATH_STAGE_OUTPUT_LOG = "./stage/output.log";

    /** path of {@code ./stage/error.log} */
    final static String PATH_STAGE_ERROR_LOG = "./stage/error.log";

    /** path of {@code ./stage/access.log} */
    final static String PATH_STAGE_ACCESS_LOG = "./stage/access.log";
    
    /** path of {@code ./resources} */
    final static String PATH_RESOURCES = "./resources";    
    
    /** path of {@code ./resources/program} */
    final static String PATH_RESOURCES_PROGRAM = "./resources/program";
    
    /** keystore for SSL connections */
    private static Keystore keystore;
    
    /** synchronization of access file and acesss stream */
    private static Thread accessSynchronize;
    
    /** continuation reader for the access log */
    final static OutputFacadeStream accessStream = new OutputFacadeStream();  
    
    @Initiate
    @SuppressWarnings("unchecked")
    private static void initiate()
            throws Exception {
        
        if (!Charset.defaultCharset().name().equals("ISO-8859-1"))
            throw new RuntimeException("Character encoding ISO-8859-1 required");
        
        String version = System.getProperty("java.version");
        if (Integer.valueOf(version.split("\\.")[0]).intValue() < 11)
            throw new RuntimeException("Java 11 or higher required");
        
        final File root = new File(".").getCanonicalFile();
        final File rootStage = new File(root, AbstractSuite.PATH_STAGE).getCanonicalFile();

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
        
        final File rootResources = new File(root, AbstractSuite.PATH_RESOURCES).getCanonicalFile();

        final File rootResourcesProgram = new File(root, AbstractSuite.PATH_RESOURCES_PROGRAM).getCanonicalFile();
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
        
        if (Files.exists(rootResourcesProgram.toPath()))
            Files.walkFileTree(rootResourcesProgram.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attributes)
                        throws IOException {
                    File file = path.toFile();
                    if (file.isFile())
                        Files.copy(path, Paths.get("./" + file.getName()), StandardCopyOption.REPLACE_EXISTING); 
                    return FileVisitResult.CONTINUE;
                }
            });
        
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
        
        final File rootStageLibraries = new File(root, AbstractSuite.PATH_STAGE_LIBRARIES).getCanonicalFile();
        String libraries = rootStageLibraries.toString();
        System.setProperty("libraries", libraries);

        File outputLog = new File(root, AbstractSuite.PATH_STAGE_OUTPUT_LOG);
        outputLog.createNewFile();
        com.seanox.test.AbstractSuite.outputStream.mount(new FileOutputStream(outputLog));
        
        File errorLog = new File(root, AbstractSuite.PATH_STAGE_ERROR_LOG);
        errorLog.createNewFile();
        com.seanox.test.AbstractSuite.errorStream.mount(new FileOutputStream(errorLog));

        File accessLog = new File(root, AbstractSuite.PATH_STAGE_ACCESS_LOG);
        accessLog.createNewFile();
        
        AbstractSuite.accessSynchronize = new Thread() {
            
            @Override
            public void run() {
                try (FileInputStream input = new FileInputStream(accessLog)) {
                    byte[] bytes = new byte[65535];
                    while (true) {
                        try {Thread.sleep(10);
                        } catch (InterruptedException exception) {
                            break;
                        }
                        int size = input.read(bytes);
                        if (size > 0)
                            AbstractSuite.accessStream.write(bytes, 0, size);
                    }
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                }
            }
        {
            this.setDaemon(true);
            this.start();
        }};
                
        Service.main(new String[] {"start"});   
    }

    @Terminate
    private static void terminate()
            throws Exception {
        
        AbstractSuite.accessSynchronize.interrupt();
        
        com.seanox.test.AbstractSuite.outputStream.unmount(System.out);
        com.seanox.test.AbstractSuite.outputStream.close();
        com.seanox.test.AbstractSuite.errorStream.unmount(System.err);
        com.seanox.test.AbstractSuite.outputStream.close();
        AbstractSuite.accessStream.close();
    }

    /**
     * Returns the root stage as file.
     * @return the root stage as file
     * @throws IOException
     */
    static File getRoot()
            throws IOException {
        return new File(".").getCanonicalFile();
    }
    
    /**
     * Returns the root stage as file.
     * @return the root stage as file
     * @throws IOException
     */
    static File getRootStage()
            throws IOException {
        return new File(AbstractSuite.getRoot(), PATH_STAGE).getCanonicalFile();
    }

    /**
     * Returns the root stage access log as file.
     * @return the root stage access log as file
     * @throws IOException
     */
    static File getRootStageAccessLog()
            throws IOException {
        return new File(AbstractSuite.getRootStage(), "access.log");
    }

    /**
     * Returns the root stage output log as file.
     * @return the root stage output log as file
     * @throws IOException
     */
    static File getRootStageOutputLog()
            throws IOException {
        return new File(AbstractSuite.getRootStage(), "output.log");
    }
    
    /**
     * Returns the root stage certificates directory as file.
     * @return the root stage certificates directory as file
     * @throws IOException
     */
    static File getRootStageCertificates()
            throws IOException {
        return new File(AbstractSuite.getRoot(), PATH_STAGE_CERTIFICATES);
    }

    /**
     * Returns the default keystore for SSL connections.
     * @return the default keystore for SSL connections
     */    
    static Keystore getKeystore() {
        return AbstractSuite.keystore;
    }
}