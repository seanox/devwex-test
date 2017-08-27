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
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.seanox.test.utils.Executor;
import com.seanox.test.utils.Executor.Worker;
import com.seanox.test.utils.HttpUtils;
import com.seanox.test.utils.Pattern;

/**
 *  TestCases for {@link com.seanox.devwex.Worker}.
 */
public class WorkerTest_Performance extends AbstractTest {
    
    private static String createFailedTestWorkerInfo(Executor executor) {

        String result = "";
        for (Worker worker : executor.getWorkers(Worker.Filter.FAILED, Worker.Filter.INTERRUPTED))
            result += worker.toString();
        return result;
    }
    
    /** 
     *  TestCase for acceptance.
     *  Measures the execution time of 1000 (40 x 25) request.
     *  The first load test is slower because the server first increases the
     *  number of threads.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_1() throws Exception {
        
        Service.restart();
        
        Executor executor = Executor.create(40, TestWorker.class);
        
        Timing timing = Timing.create(true);
        executor.execute();
        boolean success = executor.await(3000);
        timing.assertTimeRangeIn(1000, 2000);
        String failedTestWorkerInfo = WorkerTest_Performance.createFailedTestWorkerInfo(executor);
        Assert.assertTrue(failedTestWorkerInfo, success);
        Assert.assertFalse(failedTestWorkerInfo, executor.isFailed());
        Assert.assertFalse(failedTestWorkerInfo, executor.isInterrupted());
        
        for (Worker worker : executor.getWorkers()) {
            Assert.assertTrue(worker.isExecuted());
            Assert.assertTrue(worker.isTerminated());
            Assert.assertFalse(worker.isFailed());
            Assert.assertFalse(worker.isInterrupted());
            TestWorker testWorker = (TestWorker)worker;
            Assert.assertTrue(testWorker.success);
            for (TestWorker.Response response : testWorker.responseList) {
                Assert.assertNull(response.exception);
                Assert.assertNotNull(response.data);
                String responseSring = new String(response.data);
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_STATUS_200));
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_IMAGE_JPEG));
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED));                    
            }
        }
    }
    
    /** 
     *  TestCase for acceptance.
     *  Measures the execution time of 1000 (40 x 25) request.
     *  The second load test is faster because the server has a large number
     *  of threads.
     *  @throws Exception
     */
    @Test
    public void testAcceptance_2() throws Exception {
        
        Service.restart();
        
        Timing timing = Timing.create(true);

        Executor executor1 = Executor.create(40, TestWorker.class);
        executor1.execute();
        boolean success1 = executor1.await(3000);
        timing.assertTimeRangeIn(1000, 2000);
        String failedTestWorkerInfo1 = WorkerTest_Performance.createFailedTestWorkerInfo(executor1);
        Assert.assertTrue(failedTestWorkerInfo1, success1);
        Assert.assertFalse(failedTestWorkerInfo1, executor1.isFailed());
        Assert.assertFalse(failedTestWorkerInfo1, executor1.isInterrupted());
        
        Executor executor2 = Executor.create(40, TestWorker.class);
        timing.restart();
        executor2.execute();
        boolean success2 = executor2.await(3000); 
        timing.assertTimeRangeIn(1000, 2000);
        String failedTestWorkerInfo2 = WorkerTest_Performance.createFailedTestWorkerInfo(executor2);
        Assert.assertTrue(failedTestWorkerInfo2, success2);
        Assert.assertFalse(failedTestWorkerInfo2, executor2.isFailed());
        Assert.assertFalse(failedTestWorkerInfo2, executor2.isInterrupted());
        
        for (Worker worker : executor1.getWorkers()) {
            Assert.assertTrue(worker.isExecuted());
            Assert.assertTrue(worker.isTerminated());
            Assert.assertFalse(worker.isFailed());
            Assert.assertFalse(worker.isInterrupted());
            TestWorker testWorker = (TestWorker)worker;
            Assert.assertTrue(testWorker.success);
            for (TestWorker.Response response : testWorker.responseList) {
                Assert.assertNull(response.exception);
                Assert.assertNotNull(response.data);
                String responseSring = new String(response.data);
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_STATUS_200));
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_IMAGE_JPEG));
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED));                    
            }
        }
        
        for (Worker worker : executor2.getWorkers()) {
            Assert.assertTrue(worker.isExecuted());
            Assert.assertTrue(worker.isTerminated());
            Assert.assertFalse(worker.isFailed());
            Assert.assertFalse(worker.isInterrupted());
            TestWorker testWorker = (TestWorker)worker;
            Assert.assertTrue(testWorker.success);
            for (TestWorker.Response response : testWorker.responseList) {
                Assert.assertNull(response.exception);
                Assert.assertNotNull(response.data);
                String responseSring = new String(response.data);
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_STATUS_200));
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_IMAGE_JPEG));
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED));                    
            }
        }
    } 
    
    private static void waitRuntimeReady() throws Exception {
        
        String shadow = null;
        Timing timing = Timing.create(true);
        while (timing.timeMillis() < 3000) {
            Thread.sleep(250);
            String v1 = String.format("%08d", Integer.valueOf(Thread.activeCount() /10));
            String v2 = String.format("%08d", Integer.valueOf((int)(AbstractSuite.getMemoryUsage() /1024 /1024)));
            if (shadow == null || (v1 + "\0" + v2).compareTo(shadow) < 0) {
                timing.restart();
                shadow = (v1 + "\0" + v2);
            }
        }
    }
    
    /** 
     *  TestCase for acceptance.
     *  The internal resource management must fast deallocate threads and
     *  memory.
     *  @throws Exception
     */    
    @Test
    public void testAcceptance_3() throws Exception {
        
        Service.restart();
        
        long threadCount1 = Thread.activeCount() /10;
        long memoryUsage1 = AbstractSuite.getMemoryUsage() /1024 /1024;
        
        Executor executor = Executor.create(40, TestWorker.class);
        long threadCount2 = Thread.activeCount() /10;
        long memoryUsage2 = AbstractSuite.getMemoryUsage() /1024 /1024;     
        
        Timing timing = Timing.create(true);
        executor.execute();
        boolean success = executor.await(3000);
        timing.assertTimeRangeIn(1000, 2000);
        String failedTestWorkerInfo = WorkerTest_Performance.createFailedTestWorkerInfo(executor);
        Assert.assertTrue(failedTestWorkerInfo, success);
        Assert.assertFalse(failedTestWorkerInfo, executor.isFailed());
        Assert.assertFalse(failedTestWorkerInfo, executor.isInterrupted());
        
        long threadCount3 = Thread.activeCount() /10;
        long memoryUsage3 = AbstractSuite.getMemoryUsage() /1024 /1024;
        
        WorkerTest_Performance.waitRuntimeReady();

        long threadCount4 = Thread.activeCount() /10;
        long memoryUsage4 = AbstractSuite.getMemoryUsage() /1024 /1024;

        for (Worker worker : executor.getWorkers()) {
            Assert.assertTrue(worker.isExecuted());
            Assert.assertTrue(worker.isTerminated());
            Assert.assertFalse(worker.isFailed());
            Assert.assertFalse(worker.isInterrupted());
            TestWorker testWorker = (TestWorker)worker;
            Assert.assertTrue(testWorker.success);
            for (TestWorker.Response response : testWorker.responseList) {
                Assert.assertNull(response.exception);
                Assert.assertNotNull(response.data);
                String responseSring = new String(response.data);
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_STATUS_200));
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_IMAGE_JPEG));
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED));                    
            }
        }
        
        Assert.assertTrue(threadCount2 > threadCount1);
        Assert.assertTrue(threadCount2 > threadCount4);
        Assert.assertTrue(threadCount3 > threadCount1);
        Assert.assertTrue(threadCount3 > threadCount4);
        
        Assert.assertFalse(threadCount4 > threadCount1);
        Assert.assertFalse(memoryUsage1 > memoryUsage3);
        Assert.assertFalse(memoryUsage2 > memoryUsage3);
        Assert.assertFalse(memoryUsage4 > memoryUsage3);
    }
    
    @After
    public void onAfter() throws Exception {
        Service.restart();
    }
    
    /**
     *  Internal class for a worker.
     *  A worker creates a set of requests, performs them and collects the
     *  responses.
     */
    public static class TestWorker extends Worker {
        
        /** set of requests */
        private List<String> requestList;
        
        /** collected set of responses */
        private List<Response> responseList;
        
        private boolean success;
        
        @Override
        protected void prepare() throws IOException {
            
            this.requestList = new ArrayList<>();
            this.responseList = new ArrayList<>();
            File resources = new File(AbstractSuite.getRootStage(), "/documents/performance");
            for (File file : resources.listFiles())
                this.requestList.add("GET /performance/" + file.getName() + " HTTP/1.0\r\n\r\n");
        }
        
        @Override
        protected void execute() {
            
            for (String request : this.requestList)
                this.responseList.add(Response.create("127.0.0.1:80", request));
            this.success = true;
        }
        
        @Override
        public String toString() {
            
            String result = this.getClass().getSimpleName() + " " + this.hashCode();
            for (int loop = 0; loop < this.requestList.size(); loop++) {
                Response response = this.responseList.get(loop);
                if (response.exception != null)
                    result += "\t" + this.requestList.get(loop)
                            + "\t\t" + response.exception;
            }
            return result;
        }
        
        /**
         *  Internal class for a response.
         *  Collection of response data (data, duration, possibly occurred
         *  exception).
         */
        static class Response {
            
            /** response data */
            byte[] data;
            
            /** execution time */
            long duration;
            
            /** possibly occurred exception */
            Exception exception;
            
            /**
             *  Performs a request and creates a new response object.
             *  @param  host
             *  @param  request
             *  @return the created response
             *  @throws IOException
             *  @throws InterruptedException
             */                
            static Response create(String host, String request) {
                
                Response response = new Response();
                response.duration = System.currentTimeMillis();
                try {response.data = HttpUtils.sendRequest(host, request);
                } catch (IOException | GeneralSecurityException exception) {
                    return Response.create(exception);
                }
                response.duration = System.currentTimeMillis() -response.duration;
                return response;
            }
            
            /**
             *  Creates a new response object for a occured exception.
             *  @param  host
             *  @param  request
             *  @return the created response
             *  @throws IOException
             *  @throws InterruptedException
             */                 
            static Response create(Exception exception) {
                
                Response response = new Response();
                response.exception = exception;
                return response;
            }
        }
    }
}