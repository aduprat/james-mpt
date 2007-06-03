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

package org.apache.james.experimental.imapserver.encode.imap4rev1.legacy;

import org.apache.james.api.imap.ImapConstants;
import org.apache.james.experimental.imapserver.MockImapResponseWriter;
import org.apache.james.experimental.imapserver.encode.ImapEncoder;
import org.apache.james.experimental.imapserver.encode.ImapResponseComposer;
import org.apache.james.experimental.imapserver.message.response.imap4rev1.legacy.ErrorResponse;
import org.jmock.Mock;

import junit.framework.TestCase;

public class ErrorResponseMessageTest extends TestCase {

    private static final String ERROR = "An error message";
    private static final String TAG = "A Tag";
    
    ErrorResponse message;
    MockImapResponseWriter writer;
    ImapResponseComposer response;
    Mock mockNextEncoder;
    ErrorResponseEncoder encoder;
    
    protected void setUp() throws Exception {
        super.setUp();
        writer = new MockImapResponseWriter();
        mockNextEncoder = new Mock(ImapEncoder.class);
        encoder = new ErrorResponseEncoder((ImapEncoder) mockNextEncoder.proxy());
        response = new ImapResponseComposer(writer);
        message = new ErrorResponse(ERROR, TAG);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testWrite() {
        encoder.encode(message, response);
        assertEquals(4, writer.operations.size());
        assertEquals(new MockImapResponseWriter.TagOperation(TAG), writer.operations.get(0));
        assertEquals(new MockImapResponseWriter.TextMessageOperation(ImapConstants.BAD), 
                writer.operations.get(1));
        assertEquals(new MockImapResponseWriter.TextMessageOperation(ERROR),
                writer.operations.get(2));
        assertEquals(new MockImapResponseWriter.EndOperation(), 
                writer.operations.get(3));
    }

}