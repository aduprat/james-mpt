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

package org.apache.james.experimental.imapserver;

import java.util.ArrayList;
import java.util.List;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.james.mailboxmanager.MailboxListener;
import org.apache.james.mailboxmanager.MailboxManagerException;
import org.apache.james.mailboxmanager.MessageResult;
import org.apache.james.mailboxmanager.mailbox.ImapMailboxSession;

public class SelectedMailboxSession extends AbstractLogEnabled implements MailboxListener {

    private ImapSession _session;
    private boolean _readonly;
    private boolean _sizeChanged;
    private List expungedMsn = new ArrayList();
    private ImapMailboxSession mailbox;

    public SelectedMailboxSession(ImapMailboxSession mailbox, ImapSession session, boolean readonly) throws MailboxManagerException {
        this.mailbox = mailbox;
        _session = session;
        _readonly = readonly;
        // TODO make this a weak reference (or make sure deselect() is *always* called).
        mailbox.addListener(this,MessageResult.MSN | MessageResult.UID);
    }

    public void deselect() {
        mailbox.removeListener(this);
        mailbox = null;
    }


    public void mailboxDeleted() {
        try {
            _session.closeConnection("Mailbox " + mailbox.getName() + " has been deleted");
        } catch (MailboxManagerException e) {
            getLogger().error("error closing connection", e);
        }
    }


    public boolean isSizeChanged() {
        return _sizeChanged;
    }

    public void setSizeChanged(boolean sizeChanged) {
        _sizeChanged = sizeChanged;
    }
    

    public void close() throws MailboxManagerException  {
        mailbox.close();
        mailbox=null;
    }

    public void create() {
        throw new RuntimeException("should not create a selected mailbox");
        
    }

    public void expunged(MessageResult mr) {
        expungedMsn.add(new Integer(mr.getSize()));
    }

    public void added(MessageResult mr) {
       _sizeChanged = true;
    }

    public void flagsUpdated(MessageResult mr,MailboxListener silentListener) {
    }

    public ImapMailboxSession getMailbox() {
        return mailbox;
    }

    public void mailboxRenamed(String origName, String newName) {
        // TODO Auto-generated method stub
        
    }

    public void mailboxRenamed(String newName) {
        // TODO Auto-generated method stub
        
    }

}
