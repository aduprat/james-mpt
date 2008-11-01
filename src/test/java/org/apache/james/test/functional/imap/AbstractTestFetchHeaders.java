/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.test.functional.imap;

import java.util.Locale;

import org.apache.james.test.functional.HostSystem;

public abstract class AbstractTestFetchHeaders extends
        AbstractTestSelectedStateBase {

    public AbstractTestFetchHeaders(HostSystem system) {
        super(system);
    }

    public void testFetchHeaderFieldsUS() throws Exception {
        scriptTest("FetchHeaderFields", Locale.US);
    }

    public void testFetchHeaderFieldsITALY() throws Exception {
        scriptTest("FetchHeaderFields", Locale.ITALY);
    }

    public void testFetchHeaderFieldsKOREA() throws Exception {
        scriptTest("FetchHeaderFields", Locale.KOREA);
    }

    public void testFetchHeaderFieldsNotUS() throws Exception {
        scriptTest("FetchHeaderFieldsNot", Locale.US);
    }

    public void testFetchHeaderFieldsNotITALY() throws Exception {
        scriptTest("FetchHeaderFieldsNot", Locale.ITALY);
    }

    public void testFetchHeaderFieldsNotKOREA() throws Exception {
        scriptTest("FetchHeaderFieldsNot", Locale.KOREA);
    }
}
