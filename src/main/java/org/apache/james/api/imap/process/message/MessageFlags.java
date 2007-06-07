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

package org.apache.james.api.imap.message;

import javax.mail.Flags;


/**
 * The set of flags associated with a message.
 * TODO - why not use javax.mail.Flags instead of having our own.
 *
 * <p>Reference: RFC 2060 - para 2.3</p>
 */
public class MessageFlags
{
    public static final Flags ALL_FLAGS = new Flags();
    static {
        ALL_FLAGS.add(Flags.Flag.ANSWERED);
        ALL_FLAGS.add(Flags.Flag.DELETED);
        ALL_FLAGS.add(Flags.Flag.DRAFT);
        ALL_FLAGS.add(Flags.Flag.FLAGGED);
        ALL_FLAGS.add(Flags.Flag.RECENT);
        ALL_FLAGS.add(Flags.Flag.SEEN);
    }
    
    public static final String ANSWERED = "\\ANSWERED";
    public static final String DELETED = "\\DELETED";
    public static final String DRAFT = "\\DRAFT";
    public static final String FLAGGED = "\\FLAGGED";
    public static final String SEEN = "\\SEEN";

    /**
     * Returns IMAP formatted String of MessageFlags for named user
     */
    public static String format(Flags flags)
    {
        StringBuffer buf = new StringBuffer();
        buf.append( "(" );
        if ( flags.contains(Flags.Flag.ANSWERED) ) {
            buf.append( "\\Answered " );
        }
        if ( flags.contains(Flags.Flag.DELETED) ) {
            buf.append( "\\Deleted " );
        }
        if ( flags.contains(Flags.Flag.DRAFT) ) {
            buf.append( "\\Draft " );
        }
        if ( flags.contains(Flags.Flag.FLAGGED) ) {
            buf.append( "\\Flagged " );
        }
        if ( flags.contains(Flags.Flag.RECENT) ) {
            buf.append( "\\Recent " );
        }
        if ( flags.contains(Flags.Flag.SEEN) ) {
            buf.append( "\\Seen " );
        }
        // Remove the trailing space, if necessary.
        if ( buf.length() > 1 )
        {
            buf.setLength( buf.length() - 1 );
        }
        buf.append( ")" );
        return buf.toString();
    }
}

