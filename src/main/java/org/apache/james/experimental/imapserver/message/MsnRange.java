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

package org.apache.james.experimental.imapserver.message;

/**
 * Represents a range of Message Sequence Numbers.
 */
public class MsnRange {

    private int _lowVal;
    private int _highVal;

    public MsnRange(int singleVal) {
        _lowVal = singleVal;
        _highVal = singleVal;
    }

    public MsnRange(int lowVal, int highVal) {
        _lowVal = lowVal;
        _highVal = highVal;
    }

    public int getLowVal() {
        return _lowVal;
    }

    public int getHighVal() {
        return _highVal;
    }

    public boolean includes(int msn) {
        return _lowVal <= msn && msn <= _highVal;
    }

}
