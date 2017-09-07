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
import java.lang.reflect.Method;
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

import org.junit.ClassRule;
import org.junit.rules.ExternalResource;

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
    
    /** internal counter of executed test units */
    private static volatile List<String> trace;
    
    /** internal shared new system and error output stream  */
    private static volatile OutputStream systemOutputLog;

    /** original system output stream */
    private static volatile PrintStream systemOutput;
    
    /** tail of system output stream*/
    private static volatile OutputStreamTail systemOutputTail;

    /** original error output stream */
    private static volatile PrintStream systemError;

    /** tail of error output stream*/
    private static volatile OutputStreamTail systemErrorTail;
    
    /** keystore for SSL connections */
    private static volatile Keystore keystore;
    
    /**
     *  Resource Management
     *  Execution with loading of a new test class for the initialization of
     *  the test environment and execution at the end of a test class to stop
     *  the test environment.
     */     
    @ClassRule
    public static volatile ExternalResource resource = new ExternalResource() {
       
        @Override
        @SuppressWarnings("unchecked")
        protected void before() throws IOException {
            
            if (++AbstractSuite.counter > 1)
                return;
    
            AbstractSuite.systemOutput = System.out;
            AbstractSuite.systemOutputTail = new OutputStreamTail();
            System.setOut(new PrintStream(new OutputStreams(AbstractSuite.systemOutput, AbstractSuite.systemOutputTail)));
    
            AbstractSuite.systemError = System.err;
            AbstractSuite.systemErrorTail = new OutputStreamTail();
            System.setErr(new PrintStream(new OutputStreams(AbstractSuite.systemError, AbstractSuite.systemErrorTail)));

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
            
            AbstractSuite.systemOutputLog = new FileOutputStream(new File(root, AbstractSuite.PATH_STAGE + "/output.log")); 
            System.setOut(new PrintStream(new OutputStreams(AbstractSuite.systemOutput, AbstractSuite.systemOutputTail, AbstractSuite.systemOutputLog)));
    
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
            
            File accessLog = new File(rootStage, "access.log");
            accessLog.createNewFile();
            
            File outputLog = new File(rootStage, "output.log");
            outputLog.createNewFile();
            
            Service.main(new String[] {"start"});   
        }
        
        @Override
        protected void after() {
            
            if (--AbstractSuite.counter > 0)
                return;
            System.setOut(AbstractSuite.systemOutput);
            System.setErr(AbstractSuite.systemError);
        }
    };
    
    /**
     *  Writes a trace information to the system output stream.
     *  @param source
     */
    static void traceOutput(Class<? extends AbstractTest> source) {
        AbstractSuite.traceOutput(source, null);
    }

    /**
     *  Writes a trace information to the system output stream.
     *  @param source
     *  @param method
     */
    static void traceOutput(Class<? extends AbstractTest> source, Method method) {
        
        if (AbstractSuite.trace == null)
            AbstractSuite.trace = new LinkedList<>();
        if (!AbstractSuite.trace.contains(source.getName())) {
            Service.print("[" + source.getName() + "]", false);
            AbstractSuite.trace.add(source.getName());
        }
        if (method != null)
            Service.print("[" + source.getName() + "] -> " + method.getName(), false);
    }
    
    /**
     *  Returns the tail of system output stream.
     *  @return the tail of system output stream
     */
    static String getOutputTail() {
        return AbstractSuite.systemOutputTail.toString();
    }

    /**
     *  Returns the tail of error output stream.
     *  @return the tail of error output stream
     */
    static String getErrorTail() {
        return AbstractSuite.systemErrorTail.toString();
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
     *  Returns the content for a test class (section) in the output log.
     *  If the section can not be found, {@code null} is returned.
     *  @return the content for a test class (section) in the output log or
     *          {@code null}, if the section can not be found
     */    
    static String getOutputLog(Trace trace) throws IOException {

        String string = AbstractSuite.getOutputLog();
        if (trace.source == null)
            return string;
        String pattern = "[" + trace.source + "]";
        if (trace.method != null) {
            pattern += " -> " + trace.method;
            pattern = "(?s)^.*[\\r\\n]+\\Q" + pattern + "\\E[\\r\\n]+(.*?)(?:(?:[\\r\\n]+\\[.*$)|(?:$))";
        } else pattern = "(?s)^.*[\\r\\n]+\\Q" + pattern + "\\E[\\r\\n]+(.*?)(?:(?:[\\r\\n]+\\[[^\\]]+\\](?!\\s+->).*$)|(?:$))";
        if (string.matches(pattern))
            return string.replaceAll(pattern, "$1");
        return null;
    }
    
    /** Trace Scope */
    public static class Trace {
        
        /** Class */
        private String source;
        
        /** Method */
        private String method;

        /**
         *  Constructor, creates a new Trace object.
         *  @param types
         */
        private Trace(Type... types) {
            
            List<Type> typeList = Arrays.asList(types);
            StackTraceElement trace = new Throwable().getStackTrace()[2];
            if (typeList.contains(Type.CLASS)
                    || typeList.contains(Type.METHOD))
                this.source = trace.getClassName();
            if (typeList.contains(Type.METHOD))
                this.method = trace.getMethodName();
        }
        
        /**
         *  Creates a new Trace object.
         *  @param  types
         *  @return the create Trace object
         */
        public static Trace create(Type... types) {
            return new Trace(types);
        }
        
        @Override
        public String toString() {
            return "Section [source=" + this.source + ", method=" + this.method + "]";
        }
        
        /** Enum with Trace elements */
        public static enum Type {
            
            /** Class */
            CLASS,
            
            /** Method (incl. {@link #CLASS} */
            METHOD
        }
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
        return AbstractSuite.getAccessLogTail(true);
    }
    
    /**
     *  Returns the last entry of the access log.
     *  @param  normalize removes all whitespaces at the beginning and end
     *  @return the last entry of the access log
     *  @throws IOException
     */
    static String getAccessLogTail(boolean normalize) throws IOException {
    
        String string;
        string = AbstractSuite.getAccessLog();
        if (!normalize)
            string += "!"; 
        String[] accessLog = string.split("[\r\n]+");
        string = accessLog[accessLog.length -1];
        if (normalize)
            return string;
        return string.substring(0, string.length() -1);
    } 
    
    /**
     *  Returns the last entry of the output.
     *  @return the last entry of the output
     *  @throws IOException
     */
    static String getOutputLogTail() throws IOException {
        return AbstractSuite.getOutputLogTail(true);
    }

    /**
     *  Returns the last entry of the output.
     *  @param  normalize removes all whitespaces at the beginning and end
     *  @return the last entry of the output
     *  @throws IOException
     */
    static String getOutputLogTail(boolean normalize) throws IOException {
    
        String string;
        string = AbstractSuite.getOutputLog();
        if (!normalize)
            string += "!"; 
        String[] outputLog = string.split("[\r\n]+");
        List<String> outputLogList = Arrays.asList(outputLog);
        Collections.reverse(outputLogList);
        LinkedList<String> outputLogTailList = new LinkedList<>();
        for (String log : outputLogList) {
            outputLogTailList.addFirst(log);
            if (log.matches("^\\d{4}(-\\d{2}){2} \\d{2}(:\\d{2}){2}((\\s.*)|$)"))
                break;
        }
        string = String.join("\r\n", outputLogTailList.toArray(new String[0]));
        if (normalize)
            return string;
        return string.substring(0, string.length() -1);
    }
    
    /**
     *  Returns the last line of the output.
     *  @return the last line of the output
     *  @throws IOException
     */
    static String getOutputLogTailLine() throws IOException {
        return AbstractSuite.getOutputLogTailLine(true);
    }
    
    /**
     *  Returns the last line of the output.
     *  @param  normalize removes all whitespaces at the beginning and end
     *  @return the last line of the output
     *  @throws IOException
     */
    static String getOutputLogTailLine(boolean normalize) throws IOException {
    
        String string;
        string = AbstractSuite.getOutputLog();
        if (!normalize)
            string += "!";         
        String outputLog[] = string.split("[\r\n]+");
        string = outputLog[outputLog.length -1];
        if (normalize)
            return string;
        return string.substring(0, string.length() -1);
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