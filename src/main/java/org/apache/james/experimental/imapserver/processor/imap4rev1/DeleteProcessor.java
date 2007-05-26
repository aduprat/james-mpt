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

package org.apache.james.experimental.imapserver.processor.imap4rev1;

import org.apache.james.experimental.imapserver.AuthorizationException;
import org.apache.james.experimental.imapserver.ImapSession;
import org.apache.james.experimental.imapserver.ProtocolException;
import org.apache.james.experimental.imapserver.commands.ImapCommand;
import org.apache.james.experimental.imapserver.message.ImapMessage;
import org.apache.james.experimental.imapserver.message.ImapResponseMessage;
import org.apache.james.experimental.imapserver.message.request.ImapRequest;
import org.apache.james.experimental.imapserver.message.request.imap4rev1.DeleteRequest;
import org.apache.james.experimental.imapserver.message.response.imap4rev1.CommandCompleteResponse;
import org.apache.james.experimental.imapserver.processor.ImapProcessor;
import org.apache.james.experimental.imapserver.processor.base.AbstractImapRequestProcessor;
import org.apache.james.imapserver.store.MailboxException;
import org.apache.james.mailboxmanager.MailboxManagerException;


public class DeleteProcessor extends AbstractImapRequestProcessor {
	
	public DeleteProcessor(final ImapProcessor next) {
        super(next);
    }

    protected boolean isAcceptable(ImapMessage message) {
        return (message instanceof DeleteRequest);
    }
    
    protected ImapResponseMessage doProcess(ImapRequest message, ImapSession session, String tag, ImapCommand command) throws MailboxException, AuthorizationException, ProtocolException {
        final DeleteRequest request = (DeleteRequest) message;
        final ImapResponseMessage result = doProcess(request, session, tag, command);
		return result;
	}

	private ImapResponseMessage doProcess(DeleteRequest request, ImapSession session, String tag, ImapCommand command) throws MailboxException, AuthorizationException, ProtocolException {
		final String authType = request.getMailboxName();
		final ImapResponseMessage result = doProcess(authType, session, tag, command);
		return result;
	}
	
	private ImapResponseMessage doProcess(String mailboxName, 
			ImapSession session, String tag, ImapCommand command) throws MailboxException, AuthorizationException, ProtocolException {
        try {
            final String fullMailboxName = session.buildFullName(mailboxName);
            if (session.getSelected() != null) {
                if (session.getSelected().getMailbox().getName().equals(
                        fullMailboxName)) {
                    session.deselect();
                }
            }
            session.getMailboxManager().deleteMailbox(fullMailboxName);
        } catch (MailboxManagerException e) {
            throw new MailboxException(e);
        }

        final CommandCompleteResponse result = 
            new CommandCompleteResponse(false, command, tag);
        return result;
	}
}
