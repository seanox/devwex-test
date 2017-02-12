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
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.seanox.devwex.ListenerTest_Performance.Group.Worker.Response;
import com.seanox.test.utils.Pattern;

/**
 *  TestCases for {@link com.seanox.devwex.Listener}.
 */
public class ListenerTest_Performance extends AbstractTest {
    
    /** 
     *  TestCase for aceptance.
     *  Measures the execution time of 1000 (40 x 25) request.
     *  The first load test is slower because the server first increases the
     *  number of threads.
     *  @throws Exception
     */
    @Test
    public void testAceptance_1() throws Exception {
        
        Group group = Group.create(40);
        
        long time = System.currentTimeMillis();
        group.perform();
        time = System.currentTimeMillis() -time;
        Assert.assertTrue(time < 2500);
        Assert.assertTrue(time > 1000);
        
        for (Group.Worker worker : group.workerList) {
            Assert.assertEquals(2, worker.status);
            for (Response response : worker.responseList) {
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
     *  TestCase for aceptance.
     *  Measures the execution time of 1000 (40 x 25) request.
     *  The second load test is faster because the server has a large number
     *  of threads.
     *  @throws Exception
     */
    @Test
    public void testAceptance_2() throws Exception {
        
        Group group1 = Group.create(40);
        Group group2 = Group.create(40);
        
        long time1 = System.currentTimeMillis();
        group1.perform();
        time1 = System.currentTimeMillis() -time1;
        Assert.assertTrue(time1 < 1750);
        Assert.assertTrue(time1 > 1000);
        
        long time2 = System.currentTimeMillis();
        group2.perform();
        time2 = System.currentTimeMillis() -time2;
        Assert.assertTrue(time2 < 1500);
        Assert.assertTrue(time2 > 1000);
        
        for (Group.Worker worker : group1.workerList) {
            Assert.assertEquals(2, worker.status);
            for (Response response : worker.responseList) {
                Assert.assertNull(response.exception);
                Assert.assertNotNull(response.data);
                String responseSring = new String(response.data);
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_STATUS_200));
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_CONTENT_TYPE_IMAGE_JPEG));
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_CONTENT_LENGTH));
                Assert.assertTrue(responseSring.matches(Pattern.HTTP_RESPONSE_LAST_MODIFIED));                    
            }
        }
        
        for (Group.Worker worker : group2.workerList) {
            Assert.assertEquals(2, worker.status);
            for (Response response : worker.responseList) {
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
     *  TestCase for aceptance.
     *  The internal resource management must fast deallocate threads and
     *  memory.
     *  @throws Exception
     */    
    @Test
    public void testAceptance_3() throws Exception {
        
        long threadCount1 = Thread.activeCount() /10;
        long memoryUsage1 = TestUtils.nemoryUsage() /1024 /1024;
        
        Group group = Group.create(40);
        long threadCount2 = Thread.activeCount() /10;
        long memoryUsage2 = TestUtils.nemoryUsage() /1024 /1024;     
        
        group.perform();
        long threadCount3 = Thread.activeCount() /10;
        long memoryUsage3 = TestUtils.nemoryUsage() /1024 /1024;
        
        Thread.sleep(7500);
        long threadCount4 = Thread.activeCount() /10;
        long memoryUsage4 = TestUtils.nemoryUsage() /1024 /1024;

        for (Group.Worker worker : group.workerList) {
            Assert.assertEquals(2, worker.status);
            for (Response response : worker.responseList) {
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
        Assert.assertTrue(threadCount2 > threadCount3);
        Assert.assertTrue(threadCount2 > threadCount4);
        
        Assert.assertTrue(threadCount3 > threadCount1);
        Assert.assertTrue(threadCount3 > threadCount4);
        
        Assert.assertTrue(threadCount4 >= threadCount1
                || threadCount4 < threadCount1);

        Assert.assertTrue(memoryUsage1 > memoryUsage2);
        Assert.assertTrue(memoryUsage1 > memoryUsage4);

        Assert.assertTrue(memoryUsage3 > memoryUsage1);
        Assert.assertTrue(memoryUsage3 > memoryUsage2);
        Assert.assertTrue(memoryUsage3 > memoryUsage4);
    }
    
    /**
     *  Internal class for a group of worker.
     *  A group contains a lot of workers.
     *  The worker creates a set of requests, performs them and collects the
     *  responses. The group prepares the workers and starts them at the same
     *  time. 
     */    
    static class Group {

        /** current status */
        volatile int status;
        
        /** set of workers */
        volatile List<Worker> workerList;
        
        /**
         *  Creates a new group with a specific number of workers.
         *  The created set of workers is ready to perform all requests.
         *  @param  size number of workers
         *  @return the created set of workers is ready to perform all requests
         *  @throws IOException
         *  @throws InterruptedException
         */
        static Group create(int size)
                throws IOException, InterruptedException {
            
            Group group = new Group();
            size = Math.max(1, size);
            group.workerList = new ArrayList<>();
            for (int loop = 0; loop < size; loop++)
                group.workerList.add(Worker.create(group));
            return group;
        }
        
        /**
         *  Starts all workers through change the status and waits until all
         *  workers are finished.
         */
        void perform() {
            
            if (this.status != 0)
                throw new IllegalStateException();
            
            this.status = 1;
            
            for (boolean complete = false; !complete;) {
                complete = true;
                for (Worker worker : this.workerList) {
                    if (worker.status >= 2)
                        continue;
                    complete = false;
                    break;
                }
            }
        }
        
        /**
         *  Internal class for a worker.
         *  A worker creates a set of requests, performs them and collects the
         *  responses.
         */
        static class Worker extends Thread {
            
            /** group of workers */
            Group group;
            
            /** set of requests */
            List<String> requestList;
            
            /** collected set of responses */
            List<Response> responseList;
            
            /** current status */
            volatile int status;

            /**
             *  Creates a new worker object with a set of request.
             *  @param  group
             *  @return the created worker, ready to perform all requests
             *  @throws IOException
             *  @throws InterruptedException
             */
            static Worker create(Group group)
                    throws IOException, InterruptedException {
                
                Worker worker = new Worker();
                worker.requestList = new ArrayList<>();
                worker.responseList = new ArrayList<>();
                worker.group = group;
                File resources = new File(TestUtils.getRootStage(), "/documents/performance");
                for (File file : resources.listFiles())
                    worker.requestList.add("GET /performance/" + file.getName() + " HTTP/1.0\r\n\r\n");
                worker.start();
                while (worker.status < 1)
                    Thread.sleep(25);
                return worker;
            }
            
            @Override
            public void run() {
                
                this.status = 1;
                try {
                    while (this.group.status < 1)
                        Thread.sleep(25);
                    for (String request : this.requestList)
                        this.responseList.add(Response.create("127.0.0.1:80", request));
                    this.status = 2;
                } catch (Exception exception) {
                    this.status = 3;
                    this.responseList.add(Response.create(exception));
                }
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
                    try {response.data = TestHttpUtils.sendRequest(host, request);
                    } catch (IOException exception) {
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
}