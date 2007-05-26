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

package org.apache.james.experimental.imapserver.commands;

import org.apache.james.experimental.imapserver.ImapSessionState;
import org.apache.james.experimental.imapserver.message.IdRange;

/**
 * A base class for ImapCommands only valid in the SELECTED state.
 *
 * @version $Revision: 109034 $
 */
abstract public class SelectedStateCommand extends CommandTemplate
{
    /**
     * Subclasses of this command are only valid in the
     * {@link ImapSessionState#SELECTED} state.
     */
    public boolean validForState( ImapSessionState state )
    {
        return ( state == ImapSessionState.SELECTED );
    }

    protected boolean includes(IdRange[] idSet, long id) {
        for (int i = 0; i < idSet.length; i++) {
            IdRange idRange = idSet[i];
            if (idRange.includes(id)) {
                return true;
            }
        }
        return false;
    }
}
