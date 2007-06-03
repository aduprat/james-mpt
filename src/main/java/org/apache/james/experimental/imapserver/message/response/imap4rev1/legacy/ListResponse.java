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
package org.apache.james.experimental.imapserver.message.response.imap4rev1.legacy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.james.api.imap.ImapCommand;
import org.apache.james.experimental.imapserver.ImapSession;
import org.apache.james.experimental.imapserver.encode.ImapResponse;
import org.apache.james.experimental.imapserver.message.response.AbstractImapResponse;
import org.apache.james.imapserver.store.MailboxException;

/**
 * @deprecated responses should correspond directly to the specification
 */
public class ListResponse extends AbstractImapResponse {
    private List messages = new ArrayList();
    
    public ListResponse(final ImapCommand command, final String tag) {
        super(command, tag);
    }
    
    public void addMessageData(String message) {
        // TODO: this isn't efficient
        // TODO: better to stream results
    	// TODO: pass data objects back and then encode
        messages.add(message);
    }
    
    protected void doEncode(ImapResponse response, ImapSession session, ImapCommand command, String tag) throws MailboxException {
        for (final Iterator it=messages.iterator();it.hasNext();) {
            String message = (String) it.next();
            response.commandResponse(command, message);
        }
        session.unsolicitedResponses( response, false );
        response.commandComplete( command, tag );
    }
    
}
