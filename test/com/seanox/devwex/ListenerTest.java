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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *  TestSuite for #{@link com.seanox.devwex.Listener}.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    ListenerTest_Request.class,
    ListenerTest_Locale.class,
    ListenerTest_Authentication.class,
    ListenerTest_MimeType.class,
    ListenerTest_AccessLog.class,
    ListenerTest_Filter.class,
    ListenerTest_Get.class,
    ListenerTest_Head.class,
    ListenerTest_Put.class,
    ListenerTest_Delete.class,
    ListenerTest_Status.class,
    ListenerTest_Gateway.class,
    ListenerTest_VirtualHost.class,
    ListenerTest_Performance.class
})
public class ListenerTest extends AbstractSuite {
}