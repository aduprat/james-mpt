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

package org.apache.james.imap.main;


import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.james.api.imap.AbstractLogEnabled;
import org.apache.james.api.imap.ImapConstants;
import org.apache.james.api.imap.ImapSessionState;
import org.apache.james.api.imap.process.ImapSession;
import org.apache.james.api.imap.process.SelectedImapMailbox;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;

/**
 * Implements a session.
 */
public final class ImapSessionImpl extends AbstractLogEnabled implements ImapSession, ImapConstants
{
    private ImapSessionState state = ImapSessionState.NON_AUTHENTICATED;
    private SelectedImapMailbox selectedMailbox = null;
    
    private final Map attributesByKey;
    
    public ImapSessionImpl()
    {
        this.attributesByKey = new ConcurrentHashMap();
    }

    public List unsolicitedResponses( boolean useUid ) {
        return unsolicitedResponses(false, useUid);
    }

    public List unsolicitedResponses(boolean omitExpunged, boolean useUid) {
        final List results;
        final SelectedImapMailbox selected = getSelected();
        if (selected == null) {
            results = Collections.EMPTY_LIST;
        } else {
            results = selected.unsolicitedResponses(omitExpunged, useUid);
        }
        return results;
    }

    public void logout()
    {
        closeMailbox();
        state = ImapSessionState.LOGOUT;
    }

    public void authenticated( )
    {
        this.state = ImapSessionState.AUTHENTICATED;
    }

    public void deselect()
    {
        this.state = ImapSessionState.AUTHENTICATED;
        closeMailbox();
    }

    public void selected( SelectedImapMailbox mailbox )
    {
        setupLogger(mailbox);
        this.state = ImapSessionState.SELECTED;
        closeMailbox();
        this.selectedMailbox = mailbox;
    }

    public SelectedImapMailbox getSelected()
    {
        return this.selectedMailbox;
    }

    public ImapSessionState getState()
    {
        return this.state;
    }

    public void closeMailbox() {
        if (selectedMailbox != null) {
            selectedMailbox.deselect();
            selectedMailbox=null;
        }
        
    }

    public Object getAttribute(String key) {
        final Object result = attributesByKey.get(key);
        return result;
    }

    public void setAttribute(String key, Object value) {
        if (value == null) {
            attributesByKey.remove(key);
        } else {
            attributesByKey.put(key, value);
        }
    }
}
