/**
 * LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 * im Folgenden Seanox Software Solutions oder kurz Seanox genannt.
 * Diese Software unterliegt der Version 2 der GNU General Public License.
 *
 * Devwex, Advanced Server Development
 * Copyright (C) 2020 Seanox Software Solutions
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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * TestSuite for {@link com.seanox.devwex.Worker}.<br>
 * <br>
 * WorkerTest 5.1.0 20171231<br>
 * Copyright (C) 2017 Seanox Software Solutions<br>
 * All rights reserved.
 *
 * @author  Seanox Software Solutions
 * @version 5.1.0 20171231
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    WorkerTest_AccessLog.class,
    WorkerTest_Authentication.class,
    WorkerTest_Configuration.class,
    WorkerTest_Delete.class,
    WorkerTest_DirectoryIndex.class,
    WorkerTest_File.class,
    WorkerTest_Filter.class,
    WorkerTest_Gateway.class,
    WorkerTest_Get.class,
    WorkerTest_Head.class,
    WorkerTest_Locate.class,
    WorkerTest_MimeType.class,
    WorkerTest_Options.class,
    WorkerTest_Performance.class,
    WorkerTest_Put.class,
    WorkerTest_Request.class,
    WorkerTest_Status.class,
    WorkerTest_Text.class,
    WorkerTest_VirtualHost.class
})
public class WorkerTest extends AbstractSuite {
}