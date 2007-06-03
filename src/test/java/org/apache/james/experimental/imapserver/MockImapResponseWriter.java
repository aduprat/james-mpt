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

import org.apache.james.experimental.imapserver.encode.ImapResponseWriter;

public class MockImapResponseWriter implements ImapResponseWriter {

    public final List operations = new ArrayList();
    
    public void commandName(String commandName) {
        operations.add(new CommandNameOperation(commandName));

    }

    public void end() {
        operations.add(new EndOperation());
    }

    public void message(String message) {
        operations.add(new TextMessageOperation(message));
    }

    public void message(int number) {
        operations.add(new NumericMessageOperation(number));
    }

    public void responseCode(String responseCode) {
        operations.add(new ResponseCodeOperation(responseCode));
    }

    public void tag(String tag) {
        operations.add(new TagOperation(tag));
    }

    public void untagged() {
        operations.add(new UntaggedOperation());
    }

    public static class EndOperation {
        public boolean equals(Object obj) {
            return obj instanceof EndOperation;
        }

        public int hashCode() {
            return 3;
        }
        
    }
    
    public static class TextMessageOperation {
        public final String text;
        public TextMessageOperation(String text) {
            this.text = text;
        }
        public int hashCode() {
            final int PRIME = 31;
            int result = 1;
            result = PRIME * result + ((text == null) ? 0 : text.hashCode());
            return result;
        }
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final TextMessageOperation other = (TextMessageOperation) obj;
            if (text == null) {
                if (other.text != null)
                    return false;
            } else if (!text.equals(other.text))
                return false;
            return true;
        }
        
    }
    
    public static class NumericMessageOperation {
        public final int number;
        public NumericMessageOperation(int number) {
            this.number = number;
        }
        public int hashCode() {
            final int PRIME = 31;
            int result = 1;
            result = PRIME * result + number;
            return result;
        }
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final NumericMessageOperation other = (NumericMessageOperation) obj;
            if (number != other.number)
                return false;
            return true;
        }
        
    }
    
    
    public static class ResponseCodeOperation {
        public final String responseCode;

        public ResponseCodeOperation(final String responseCode) {
            super();
            this.responseCode = responseCode;
        }

        public int hashCode() {
            final int PRIME = 31;
            int result = 1;
            result = PRIME * result + ((responseCode == null) ? 0 : responseCode.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final ResponseCodeOperation other = (ResponseCodeOperation) obj;
            if (responseCode == null) {
                if (other.responseCode != null)
                    return false;
            } else if (!responseCode.equals(other.responseCode))
                return false;
            return true;
        }
        
    }
    
    public static class CommandNameOperation {
        public final String commandName;

        public CommandNameOperation(final String commandName) {
            super();
            this.commandName = commandName;
        }

        public int hashCode() {
            final int PRIME = 31;
            int result = 1;
            result = PRIME * result + ((commandName == null) ? 0 : commandName.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final CommandNameOperation other = (CommandNameOperation) obj;
            if (commandName == null) {
                if (other.commandName != null)
                    return false;
            } else if (!commandName.equals(other.commandName))
                return false;
            return true;
        }
        
    }
    
    public static class UntaggedOperation {
        public boolean equals(Object obj) {
            return obj instanceof UntaggedOperation;
        }

        public int hashCode() {
            return 2;
        }
        
    }
    
    public static class TagOperation {

        private final String tag;
        public TagOperation(String tag) {
            this.tag = tag;
        }
        public int hashCode() {
            final int PRIME = 31;
            int result = 1;
            result = PRIME * result + ((tag == null) ? 0 : tag.hashCode());
            return result;
        }
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final TagOperation other = (TagOperation) obj;
            if (tag == null) {
                if (other.tag != null)
                    return false;
            } else if (!tag.equals(other.tag))
                return false;
            return true;
        }
        
    }
    
    
}
